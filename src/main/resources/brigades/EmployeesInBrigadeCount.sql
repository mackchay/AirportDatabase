with brigade_salary as (select employee.id, SUM(salary) as total_salary, AVG(salary) as avg_salary
                         from employee
                                 join public.brigade b on employee.brigade = b.id
                         group by employee.id)
select count(distinct e.id)
 from employee e
         inner join personal_info p on e.personal_info = p.id
         inner join public.brigade b on b.id = e.brigade
         inner join public.department d on d.id = e.department
         inner join public.airport a on a.id = e.airport
         left JOIN
      crew c ON (e.brigade = c.pilot_brigade and department_title = 'pilot')
          OR (e.brigade = c.technicians_brigade and department_title = 'technician')
          OR (e.brigade = c.maintenance_brigade and department_title = 'maintenance')
         INNER JOIN
      flight ON c.aircraft = flight.aircraft
         INNER JOIN
      brigade_salary ON e.id = brigade_salary.id