select count(distinct e.id)
from employee e
         inner join personal_info p on e.personal_info = p.id
         inner join public.brigade b on b.id = e.brigade
         inner join public.department d on d.id = e.department
         inner join public.airport a on a.id = e.airport
         inner join public.pilot p2 on e.id = p2.employee
         left join public.medical_examination me on p2.employee = me.pilot