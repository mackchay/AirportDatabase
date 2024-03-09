CREATE TYPE "delay_reason" AS ENUM (
  'weather_conditions',
  'technical_problems',
  'tickets_are_not_sold_out'
);

CREATE TYPE "ticket_status" AS ENUM (
  'not_purchased',
  'booked',
  'purchased'
);

CREATE TYPE "aircraft_status" AS ENUM (
  'ready',
  'on_repair',
  'required_technical_inspection'
);

CREATE TABLE "employees" (
  "id" integer PRIMARY KEY,
  "personal_info" integer UNIQUE,
  "s" integer,
  "brigades" integer,
  "salary" integer,
  "job_title" integer
);

CREATE TABLE "job_titles" (
  "id" integer PRIMARY KEY,
  "job_title" varchar(255)
);

CREATE TABLE "personal_info" (
  "id" integer PRIMARY KEY,
  "full_name" varchar(255),
  "gender" varchar(10),
  "age" integer,
  "phone_number" bigint,
  "children" integer
);

CREATE TABLE "brigades" (
  "id" integer PRIMARY KEY,
  "brigade_type" varchar(255),
  "brigade_head" integer
);

CREATE TABLE "delayed_flights" (
  "id" integer PRIMARY KEY,
  "delay_reason" delay_reason
);

CREATE TABLE "departments" (
  "id" integer PRIMARY KEY,
  "department_name" varchar(255),
  "department_head" integer
);

CREATE TABLE "aircraft" (
  "id" integer PRIMARY KEY,
  "type" varchar(255),
  "crew" integer UNIQUE,
  "arrival_time" date,
  "experience" integer
);

CREATE TABLE "crew" (
  "id" integer PRIMARY KEY,
  "pilot_brigade" integer,
  "technicians_brigade" integer,
  "maintenance_brigade" integer
);

CREATE TABLE "seats" (
  "id" integer PRIMARY KEY,
  "name" varchar(20),
  "aircraft" integer
);

CREATE TABLE "route" (
  "id" integer PRIMARY KEY,
  "departure" varchar(255),
  "destination" varchar(255),
  "transfer" varchar(255)
);

CREATE TABLE "booked_tickets" (
  "id" integer PRIMARY KEY,
  "booking_start" timestamp,
  "booking_end" timestamp
);

CREATE TABLE "tickets" (
  "id" integer PRIMARY KEY,
  "flight" integer,
  "passenger" integer,
  "seat" integer UNIQUE,
  "purchase_date" date,
  "status" ticket_status,
  "baggage" bool
);

CREATE TABLE "passengers" (
  "id" integer PRIMARY KEY,
  "full_name" varchar(255),
  "gender" varchar(10),
  "age" integer,
  "phone_number" bigint,
  "passport" bigint,
  "international_passport" bigint
);

CREATE TABLE "medical_examinations" (
  "id" integer PRIMARY KEY,
  "pilot" integer,
  "date" timestamp,
  "result" bool
);

CREATE TABLE "schedule" (
  "id" integer PRIMARY KEY,
  "flight_type" varchar(255),
  "route" integer,
  "ticket_price" integer,
  "departure" timestamp,
  "landing" timestamp,
  "status" varchar(255)
);

CREATE TABLE "pilots" (
  "employee" integer PRIMARY KEY,
  "license_type" varchar(255),
  "experience" integer,
  "employment_date" integer,
  "specialization" varchar(255)
);

CREATE TABLE "dispatchers" (
  "employee" integer PRIMARY KEY,
  "certification_level" varchar(255),
  "experience" integer,
  "employment_date" integer,
  "handling_stressful_situations" varchar(255)
);

CREATE TABLE "technicians" (
  "employee" integer PRIMARY KEY,
  "specialization" varchar,
  "skill_level" varchar(255),
  "experience" integer,
  "employment_date" integer
);

CREATE TABLE "cashiers" (
  "employee" integer PRIMARY KEY,
  "equipment_skills" varchar(255),
  "ticket_sales_policies_and_procedure_knowledge" varchar(255),
  "communication_skills" varchar(255),
  "experience" integer,
  "employment_date" integer
);

CREATE TABLE "security_staff" (
  "employee" integer PRIMARY KEY,
  "security_training_level" varchar(255),
  "experience" integer,
  "employment_date" integer,
  "detecting_and_responding_to_security_incidents_skills" varchar(255)
);

CREATE TABLE "information_service" (
  "employee" integer PRIMARY KEY,
  "airport_procedures_and_regulations_knowledge" varchar(255),
  "communication_skills" varchar(255),
  "flight_schedules_and_etc_knowledge" varchar(255),
  "experience" integer,
  "employment_date" integer
);

CREATE TABLE "administration" (
  "employee" integer PRIMARY KEY,
  "management_work_experience" integer,
  "analytical_and_planning_skills" varchar(255),
  "aviation_industry_knowledge" varchar(255),
  "employment_date" integer
);

CREATE TABLE "service_staff" (
  "employee" integer PRIMARY KEY,
  "passenger_service_professionalism" varchar(255),
  "luggage_handling_skills" varchar(255),
  "cleanliness_and_accuracy_in_salon_service" varchar(255),
  "experience" integer,
  "employment_date" integer
);

CREATE TABLE "maintenance" (
  "id" integer,
  "aircraft" integer,
  "service_type" varchar(255),
  "date" timestamp,
  "result" bool
);

CREATE TABLE "repair" (
  "id" integer,
  "aircraft" integer,
  "repair_type" varchar(255),
  "date" timestamp,
  "result" bool
);

CREATE TABLE "customs_control" (
  "id" integer,
  "inspection_type" varchar(255),
  "passenger" integer,
  "result" bool
);

CREATE TABLE "hangar" (
  "id" integer,
  "aircraft" integer,
  "status" aircraft_status
);

COMMENT ON COLUMN "tickets"."status" IS 'reserved, purchased, not purchased';

COMMENT ON COLUMN "technicians"."specialization" IS 'for example, avionics, mechanic, flight mechanic)';

ALTER TABLE "employees" ADD FOREIGN KEY ("brigades") REFERENCES "brigades" ("id");

ALTER TABLE "employees" ADD FOREIGN KEY ("s") REFERENCES "departments" ("id");

ALTER TABLE "departments" ADD FOREIGN KEY ("department_head") REFERENCES "employees" ("id");

ALTER TABLE "brigades" ADD FOREIGN KEY ("brigade_head") REFERENCES "employees" ("id");

ALTER TABLE "aircraft" ADD FOREIGN KEY ("crew") REFERENCES "crew" ("id");

ALTER TABLE "employees" ADD FOREIGN KEY ("personal_info") REFERENCES "personal_info" ("id");

ALTER TABLE "crew" ADD FOREIGN KEY ("pilot_brigade") REFERENCES "brigades" ("id");

ALTER TABLE "crew" ADD FOREIGN KEY ("technicians_brigade") REFERENCES "brigades" ("id");

ALTER TABLE "crew" ADD FOREIGN KEY ("maintenance_brigade") REFERENCES "brigades" ("id");

ALTER TABLE "seats" ADD FOREIGN KEY ("aircraft") REFERENCES "aircraft" ("id");

ALTER TABLE "tickets" ADD FOREIGN KEY ("passenger") REFERENCES "passengers" ("id");

ALTER TABLE "tickets" ADD FOREIGN KEY ("seat") REFERENCES "seats" ("id");

ALTER TABLE "schedule" ADD FOREIGN KEY ("route") REFERENCES "route" ("id");

ALTER TABLE "tickets" ADD FOREIGN KEY ("flight") REFERENCES "schedule" ("id");

ALTER TABLE "pilots" ADD FOREIGN KEY ("employee") REFERENCES "employees" ("id");

ALTER TABLE "security_staff" ADD FOREIGN KEY ("employee") REFERENCES "employees" ("id");

ALTER TABLE "information_service" ADD FOREIGN KEY ("employee") REFERENCES "employees" ("id");

ALTER TABLE "service_staff" ADD FOREIGN KEY ("employee") REFERENCES "employees" ("id");

ALTER TABLE "dispatchers" ADD FOREIGN KEY ("employee") REFERENCES "employees" ("id");

ALTER TABLE "cashiers" ADD FOREIGN KEY ("employee") REFERENCES "employees" ("id");

ALTER TABLE "administration" ADD FOREIGN KEY ("employee") REFERENCES "employees" ("id");

ALTER TABLE "medical_examinations" ADD FOREIGN KEY ("pilot") REFERENCES "pilots" ("employee");

ALTER TABLE "technicians" ADD FOREIGN KEY ("employee") REFERENCES "employees" ("id");

ALTER TABLE "maintenance" ADD FOREIGN KEY ("aircraft") REFERENCES "aircraft" ("id");

ALTER TABLE "repair" ADD FOREIGN KEY ("aircraft") REFERENCES "aircraft" ("id");

ALTER TABLE "customs_control" ADD FOREIGN KEY ("passenger") REFERENCES "passengers" ("id");

ALTER TABLE "hangar" ADD FOREIGN KEY ("id") REFERENCES "aircraft" ("id");

ALTER TABLE "employees" ADD FOREIGN KEY ("job_title") REFERENCES "job_titles" ("id");

ALTER TABLE "delayed_flights" ADD FOREIGN KEY ("id") REFERENCES "schedule" ("id");

ALTER TABLE "booked_tickets" ADD FOREIGN KEY ("id") REFERENCES "tickets" ("id");
