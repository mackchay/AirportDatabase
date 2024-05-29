-- Проверка минимальной зарплаты сотрудников
CREATE OR REPLACE FUNCTION enforce_min_salary()
    RETURNS TRIGGER AS $$
DECLARE
BEGIN
    IF NEW.salary < 30000 THEN
        NEW.salary := 30000;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER check_min_salary
    BEFORE INSERT OR UPDATE
    ON employee
    FOR EACH ROW
EXECUTE FUNCTION enforce_min_salary();

-- Обновлять статус полёта
CREATE OR REPLACE FUNCTION update_flight_status()
    RETURNS TRIGGER AS $$
DECLARE
BEGIN
    IF NEW.landing < NOW() THEN
        NEW.status := 'completed';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER update_flight_status_trigger
    BEFORE UPDATE
    ON flight
    FOR EACH ROW
EXECUTE FUNCTION update_flight_status();

-- Уставновка статуса "purchased" при установке даты покупки
CREATE OR REPLACE FUNCTION update_ticket_status()
    RETURNS TRIGGER AS $$
DECLARE
    -- Здесь можно объявить переменные, если они нужны
BEGIN
    IF NEW.purchase_date IS NOT NULL THEN
        NEW.status := 'purchased';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER update_ticket_status_trigger
    BEFORE UPDATE
    ON ticket
    FOR EACH ROW
EXECUTE FUNCTION update_ticket_status();

-- Предотвращение конфликтов в расписании
CREATE OR REPLACE FUNCTION check_flight_schedule_conflict()
    RETURNS TRIGGER AS $$
DECLARE
    -- Здесь можно объявить переменные, если они нужны
BEGIN
    IF EXISTS (SELECT 1
               FROM flight
               WHERE flight.aircraft = NEW.aircraft
                 AND flight.id != NEW.id
                 AND (
                   (NEW.departure BETWEEN flight.departure AND flight.landing)
                       OR (NEW.landing BETWEEN flight.departure AND flight.landing)
                   )) THEN
        RAISE EXCEPTION 'Schedule conflict for aircraft %', NEW.aircraft;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER check_flight_schedule_conflict_trigger
    BEFORE INSERT OR UPDATE
    ON flight
    FOR EACH ROW
EXECUTE FUNCTION check_flight_schedule_conflict();

-- Удаление информации связанной с пассажиром
CREATE OR REPLACE FUNCTION delete_related_passenger_info()
    RETURNS TRIGGER AS $$
DECLARE
    -- Здесь можно объявить переменные, если они нужны
BEGIN
    DELETE FROM ticket WHERE passenger = OLD.id;
    DELETE FROM custom_control_transaction WHERE passenger = OLD.id;
    RETURN OLD;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER delete_related_passenger_info_trigger
    AFTER DELETE
    ON passenger
    FOR EACH ROW
EXECUTE FUNCTION delete_related_passenger_info();

-- Удаление информации связанной с работником
CREATE OR REPLACE FUNCTION delete_related_employee_info()
    RETURNS TRIGGER AS $$
DECLARE
    -- Здесь можно объявить переменные, если они нужны
BEGIN
    -- Удаление записей из связанных таблиц
    DELETE FROM pilot WHERE employee = OLD.id;
    DELETE FROM technician WHERE employee = OLD.id;
    DELETE FROM dispatcher WHERE employee = OLD.id;
    DELETE FROM cashier WHERE employee = OLD.id;
    DELETE FROM security_staff WHERE employee = OLD.id;
    DELETE FROM information_service WHERE employee = OLD.id;
    DELETE FROM maintenance_staff WHERE employee = OLD.id;
    DELETE FROM administration WHERE employee = OLD.id;
    DELETE FROM brigade_head WHERE employee = OLD.id;
    DELETE FROM department_head WHERE employee = OLD.id;
    -- Удаление персональной информации
    DELETE FROM personal_info WHERE id = OLD.personal_info;
    RETURN OLD;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER delete_related_employee_info_trigger
    AFTER DELETE
    ON employee
    FOR EACH ROW
EXECUTE FUNCTION delete_related_employee_info();

-- Обновление статуса когда мы бронируем билет
CREATE OR REPLACE FUNCTION update_ticket_status_to_booked()
    RETURNS TRIGGER AS $$
DECLARE
    -- Здесь можно объявить переменные, если они нужны
BEGIN
    -- Обновляем статус билета на 'booked' (забронирован)
    UPDATE ticket
    SET status = 'booked'
    WHERE id = NEW.id;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER after_insert_booked_ticket
    AFTER INSERT
    ON booked_ticket
    FOR EACH ROW
EXECUTE FUNCTION update_ticket_status_to_booked();

-- Триггер для проверки назначения начальника отдела
CREATE OR REPLACE FUNCTION check_department_head_assignment() RETURNS TRIGGER AS $$
BEGIN
    IF NEW.employee IS NOT NULL THEN
        -- Проверяем, принадлежит ли работник к отделу, к которому его пытаются назначить начальником
        IF EXISTS (SELECT 1
                   FROM department
                   WHERE id = NEW.department::integer
                     AND id = (SELECT department FROM employee WHERE id = NEW.employee)) THEN
            RETURN NEW;
        ELSE
            RAISE EXCEPTION 'Employee does not belong to the department';
        END IF;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Триггер для проверки назначения начальника бригады
CREATE OR REPLACE FUNCTION check_brigade_head_assignment() RETURNS TRIGGER AS $$
BEGIN
    IF NEW.employee IS NOT NULL THEN
        -- Проверяем, принадлежит ли работник к отделу, к которому принадлежит бригада, которой его пытаются назначить начальником
        IF EXISTS (SELECT 1
                   FROM brigade
                   WHERE id = NEW.brigade::integer
                     AND department = (SELECT department FROM employee WHERE id = NEW.employee)) THEN
            RETURN NEW;
        ELSE
            RAISE EXCEPTION 'Employee does not belong to the brigade''s department';
        END IF;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Триггер для проверки назначения пилота
CREATE OR REPLACE FUNCTION check_pilot_assignment() RETURNS TRIGGER AS $$
BEGIN
    IF NEW.employee IS NOT NULL THEN
        -- Проверяем, принадлежит ли работник к отделу, к которому его пытаются назначить пилотом
        IF EXISTS (
            SELECT 1
            FROM employee e
                     JOIN department d ON e.department = d.id
            WHERE e.id = NEW.employee
              AND d.department_title = 'pilot'
        ) THEN
            RETURN NEW;
        ELSE
            RAISE EXCEPTION 'Employee does not belong to the pilot department';
        END IF;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER enforce_pilot_assignment
    BEFORE INSERT OR UPDATE
    ON pilot
    FOR EACH ROW
EXECUTE FUNCTION check_pilot_assignment();

-- Триггер для проверки назначения техника
CREATE OR REPLACE FUNCTION check_technician_assignment() RETURNS TRIGGER AS $$
BEGIN
    IF NEW.employee IS NOT NULL THEN
        -- Проверяем, принадлежит ли работник к отделу техников
        IF EXISTS (
            SELECT 1
            FROM employee e
                     JOIN department d ON e.department = d.id
            WHERE e.id = NEW.employee
              AND d.department_title = 'technician'
        ) THEN
            RETURN NEW;
        ELSE
            RAISE EXCEPTION 'Employee does not belong to the technician department';
        END IF;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER enforce_technician_assignment
    BEFORE INSERT OR UPDATE
    ON technician
    FOR EACH ROW
EXECUTE FUNCTION check_technician_assignment();

-- Триггер для проверки назначения обслуживающего персонала
CREATE OR REPLACE FUNCTION check_maintenance_assignment() RETURNS TRIGGER AS $$
BEGIN
    IF NEW.employee IS NOT NULL THEN
        -- Проверяем, принадлежит ли работник к отделу обслуживающего персонала
        IF EXISTS (
            SELECT 1
            FROM employee e
                     JOIN department d ON e.department = d.id
            WHERE e.id = NEW.employee
              AND d.department_title = 'maintenance'
        ) THEN
            RETURN NEW;
        ELSE
            RAISE EXCEPTION 'Employee does not belong to the maintenance department';
        END IF;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER enforce_maintenance_service_assignment
    BEFORE INSERT OR UPDATE
    ON maintenance_staff
    FOR EACH ROW
EXECUTE FUNCTION check_maintenance_assignment();
