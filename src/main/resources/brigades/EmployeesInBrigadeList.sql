WITH brigade_salary AS (SELECT b.id as brigade_id,
                               d2.id as department_id,
                               SUM(salary) AS total_salary,
                               AVG(salary) AS avg_salary
                         FROM employee
                                 JOIN
                             public.brigade b ON employee.brigade = b.id
                                 JOIN public.department d2 on d2.id = employee.department
                        GROUP BY b.id, d2.id)
SELECT distinct e.id,
                airport,
                department,
                brigade,
                salary,
                department_title,
                employment_date,
                experience,
                full_name,
                gender,
                age,
                phone_number,
                children,
                avg_salary,
                total_salary
 FROM employee e
         INNER JOIN
     personal_info p ON e.personal_info = p.id
         INNER JOIN
     public.brigade b ON b.id = e.brigade
         INNER JOIN
     public.department d ON d.id = e.department
         INNER JOIN
     public.airport a ON a.id = e.airport
         left JOIN
     crew c ON (e.brigade = c.pilot_brigade and department_title = 'pilot')
         OR (e.brigade = c.technicians_brigade and department_title = 'technician')
         OR (e.brigade = c.maintenance_brigade and department_title = 'maintenance')
         INNER JOIN
     flight ON c.aircraft = flight.aircraft
         INNER JOIN
     brigade_salary ON b.id = brigade_salary.brigade_id AND d.id = brigade_salary.department_id
