select distinct hd.id,
                e.id,
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
                children
 from department_head as hd
         inner join public.employee e on e.id = hd.employee
         inner join personal_info p on e.personal_info = p.id
         inner join public.brigade b on b.id = e.brigade
         inner join public.department d on d.id = e.department
         inner join public.airport a on a.id = e.airport