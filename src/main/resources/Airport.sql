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

CREATE TYPE "flight_type" AS ENUM (
    'domestic',
    'international',
    'charter',
    'cargo',
    'special'
    );

CREATE TYPE "flight_status" AS ENUM (
    'valid',
    'completed',
    'cancelled',
    'postponed'
    );

CREATE TYPE "aircraft_type" AS ENUM (
    'passenger',
    'private',
    'cargo',
    'war'
    );

CREATE TABLE "airport" (
                           "id" integer PRIMARY KEY,
                           "name" varchar(255) NOT NULL,
                           "location" varchar(255) NOT NULL
);

CREATE TABLE "employee" (
                            "id" integer PRIMARY KEY,
                            "airport" integer NOT NULL,
                            "personal_info" integer UNIQUE NOT NULL,
                            "brigade" integer,
                            "department" integer,
                            "salary" integer NOT NULL,
                            "employment_date" timestamp NOT NULL,
                            "experience" integer NOT NULL
);

CREATE TABLE "personal_info" (
                                 "id" integer PRIMARY KEY,
                                 "full_name" varchar(255) NOT NULL,
                                 "gender" varchar(10) NOT NULL,
                                 "age" integer NOT NULL,
                                 "phone_number" bigint NOT NULL,
                                 "children" integer NOT NULL
);

CREATE TABLE "brigade" (
                           "id" integer PRIMARY KEY
);

CREATE TABLE "brigade_head" (
                                "id" integer PRIMARY KEY,
                                "employee" integer UNIQUE NOT NULL
);

CREATE TABLE "department_head" (
                                   "id" integer PRIMARY KEY,
                                   "employee" integer UNIQUE NOT NULL
);

CREATE TABLE "delayed_flight" (
                                  "id" integer PRIMARY KEY,
                                  "delay_reason" delay_reason NOT NULL,
                                  "old_date" timestamp NOT NULL,
                                  "new_date" timestamp
);

CREATE TABLE "department" (
                              "id" integer PRIMARY KEY,
                              "department_title" varchar(100) NOT NULL
);

CREATE TABLE "aircraft" (
                            "id" integer PRIMARY KEY,
                            "type" aircraft_type NOT NULL,
                            "arrival_time" timestamp NOT NULL,
                            "airport" integer,
                            "experience" integer NOT NULL,
                            "age" integer NOT NULL
);

CREATE TABLE "crew" (
                        "id" integer PRIMARY KEY,
                        "aircraft" integer,
                        "pilot_brigade" integer UNIQUE NOT NULL,
                        "technicians_brigade" integer UNIQUE NOT NULL,
                        "maintenance_brigade" integer UNIQUE NOT NULL
);

CREATE TABLE "seat" (
                        "id" integer PRIMARY KEY,
                        "name" varchar(20) NOT NULL
);

CREATE TABLE "route" (
                         "id" integer PRIMARY KEY,
                         "departure_airport" integer NOT NULL,
                         "destination_airport" integer NOT NULL,
                         "transfer_airport" integer
);

CREATE TABLE "booked_ticket" (
                                 "id" integer PRIMARY KEY,
                                 "booking_start" timestamp NOT NULL,
                                 "booking_end" timestamp NOT NULL
);

CREATE TABLE "ticket" (
                          "id" integer PRIMARY KEY,
                          "flight" integer NOT NULL,
                          "passenger" integer,
                          "seat" integer UNIQUE NOT NULL,
                          "purchase_date" timestamp,
                          "refund_date" timestamp,
                          "ticket_price" integer,
                          "status" ticket_status NOT NULL,
                          "baggage" bool
);

CREATE TABLE "passenger" (
                             "id" integer PRIMARY KEY,
                             "full_name" varchar(255) NOT NULL,
                             "gender" varchar(10) NOT NULL,
                             "age" integer NOT NULL,
                             "phone_number" bigint NOT NULL,
                             "passport" bigint NOT NULL,
                             "international_passport" bigint
);

CREATE TABLE "medical_examination" (
                                       "id" integer PRIMARY KEY,
                                       "pilot" integer NOT NULL,
                                       "date" timestamp NOT NULL,
                                       "result" bool NOT NULL
);

CREATE TABLE "flight" (
                          "id" integer PRIMARY KEY,
                          "flight_type" flight_type NOT NULL,
                          "aircraft" integer,
                          "route" integer NOT NULL,
                          "departure" timestamp NOT NULL,
                          "landing" timestamp NOT NULL,
                          "status" flight_status NOT NULL
);

CREATE TABLE "pilot" (
                         "employee" integer PRIMARY KEY,
                         "license_type" varchar(255),
                         "specialization" varchar(255)
);

CREATE TABLE "dispatcher" (
                              "employee" integer PRIMARY KEY,
                              "certification_level" varchar(255),
                              "handling_stressful_situations" varchar(255)
);

CREATE TABLE "technician" (
                              "employee" integer PRIMARY KEY,
                              "specialization" varchar(255),
                              "skill_level" varchar(255)
);

CREATE TABLE "cashier" (
                           "employee" integer PRIMARY KEY,
                           "equipment_skills" varchar(255),
                           "ticket_sales_policies_and_procedure_knowledge" varchar(255),
                           "communication_skills" varchar(255)
);

CREATE TABLE "security_staff" (
                                  "employee" integer PRIMARY KEY,
                                  "security_training_level" varchar(255),
                                  "detecting_and_responding_to_security_incidents_skills" varchar(255)
);

CREATE TABLE "information_service" (
                                       "employee" integer PRIMARY KEY,
                                       "airport_procedures_and_regulations_knowledge" varchar(255),
                                       "communication_skills" varchar(255),
                                       "flight_schedules_and_etc_knowledge" varchar(255)
);

CREATE TABLE "administration" (
                                  "employee" integer PRIMARY KEY,
                                  "analytical_and_planning_skills" varchar(255),
                                  "aviation_industry_knowledge" varchar(255)
);

CREATE TABLE "maintenance_staff" (
                                     "employee" integer PRIMARY KEY,
                                     "passenger_service_professionalism" varchar(255),
                                     "luggage_handling_skills" varchar(255),
                                     "cleanliness_and_accuracy_in_salon_service" varchar(255)
);

CREATE TABLE "technical_inspection" (
                                        "id" integer PRIMARY KEY,
                                        "aircraft" integer NOT NULL,
                                        "date" timestamp NOT NULL,
                                        "result" bool NOT NULL
);

CREATE TABLE "maintenance" (
                               "id" integer PRIMARY KEY,
                               "aircraft" integer NOT NULL,
                               "date" timestamp NOT NULL
);

CREATE TABLE "repair" (
                          "id" integer PRIMARY KEY,
                          "aircraft" integer NOT NULL,
                          "repair_type" varchar(255),
                          "date" timestamp NOT NULL
);

CREATE TABLE "custom_control_transaction" (
                                              "id" integer PRIMARY KEY,
                                              "inspection_type" varchar(255),
                                              "passenger" integer NOT NULL,
                                              "date" timestamp NOT NULL,
                                              "result" bool NOT NULL
);

CREATE TABLE "hangar_space" (
                                "id" integer PRIMARY KEY,
                                "hangar" integer NOT NULL,
                                "aircraft" integer NOT NULL,
                                "arrival_time" timestamp NOT NULL,
                                "departure_time" timestamp,
                                "status" aircraft_status NOT NULL
);

CREATE TABLE "hangar" (
                          "id" integer PRIMARY KEY,
                          "airport" integer NOT NULL
);

COMMENT ON COLUMN "ticket"."status" IS 'reserved, purchased, not purchased, returned';

COMMENT ON COLUMN "technician"."specialization" IS 'for example, avionics, mechanic, flight mechanic)';

ALTER TABLE "employee" ADD FOREIGN KEY ("brigade") REFERENCES "brigade" ("id");

ALTER TABLE "employee" ADD FOREIGN KEY ("personal_info") REFERENCES "personal_info" ("id");

ALTER TABLE "crew" ADD FOREIGN KEY ("pilot_brigade") REFERENCES "brigade" ("id");

ALTER TABLE "crew" ADD FOREIGN KEY ("technicians_brigade") REFERENCES "brigade" ("id");

ALTER TABLE "crew" ADD FOREIGN KEY ("maintenance_brigade") REFERENCES "brigade" ("id");

ALTER TABLE "ticket" ADD FOREIGN KEY ("passenger") REFERENCES "passenger" ("id");

ALTER TABLE "ticket" ADD FOREIGN KEY ("seat") REFERENCES "seat" ("id");

ALTER TABLE "flight" ADD FOREIGN KEY ("route") REFERENCES "route" ("id");

ALTER TABLE "ticket" ADD FOREIGN KEY ("flight") REFERENCES "flight" ("id");

ALTER TABLE "pilot" ADD FOREIGN KEY ("employee") REFERENCES "employee" ("id");

ALTER TABLE "security_staff" ADD FOREIGN KEY ("employee") REFERENCES "employee" ("id");

ALTER TABLE "information_service" ADD FOREIGN KEY ("employee") REFERENCES "employee" ("id");

ALTER TABLE "maintenance_staff" ADD FOREIGN KEY ("employee") REFERENCES "employee" ("id");

ALTER TABLE "dispatcher" ADD FOREIGN KEY ("employee") REFERENCES "employee" ("id");

ALTER TABLE "cashier" ADD FOREIGN KEY ("employee") REFERENCES "employee" ("id");

ALTER TABLE "administration" ADD FOREIGN KEY ("employee") REFERENCES "employee" ("id");

ALTER TABLE "medical_examination" ADD FOREIGN KEY ("pilot") REFERENCES "pilot" ("employee");

ALTER TABLE "technician" ADD FOREIGN KEY ("employee") REFERENCES "employee" ("id");

ALTER TABLE "maintenance" ADD FOREIGN KEY ("aircraft") REFERENCES "aircraft" ("id");

ALTER TABLE "repair" ADD FOREIGN KEY ("aircraft") REFERENCES "aircraft" ("id");

ALTER TABLE "custom_control_transaction" ADD FOREIGN KEY ("passenger") REFERENCES "passenger" ("id");

ALTER TABLE "delayed_flight" ADD FOREIGN KEY ("id") REFERENCES "flight" ("id");

ALTER TABLE "booked_ticket" ADD FOREIGN KEY ("id") REFERENCES "ticket" ("id");

ALTER TABLE "hangar_space" ADD FOREIGN KEY ("aircraft") REFERENCES "aircraft" ("id");

ALTER TABLE "hangar" ADD FOREIGN KEY ("airport") REFERENCES "airport" ("id");

ALTER TABLE "hangar_space" ADD FOREIGN KEY ("hangar") REFERENCES "hangar" ("id");

ALTER TABLE "route" ADD FOREIGN KEY ("departure_airport") REFERENCES "airport" ("id");

ALTER TABLE "route" ADD FOREIGN KEY ("destination_airport") REFERENCES "airport" ("id");

ALTER TABLE "route" ADD FOREIGN KEY ("transfer_airport") REFERENCES "airport" ("id");

ALTER TABLE "aircraft" ADD FOREIGN KEY ("airport") REFERENCES "airport" ("id");

ALTER TABLE "brigade_head" ADD FOREIGN KEY ("employee") REFERENCES "employee" ("id");

ALTER TABLE "department_head" ADD FOREIGN KEY ("employee") REFERENCES "employee" ("id");

ALTER TABLE "crew" ADD FOREIGN KEY ("aircraft") REFERENCES "aircraft" ("id");

ALTER TABLE "technical_inspection" ADD FOREIGN KEY ("aircraft") REFERENCES "aircraft" ("id");

ALTER TABLE "flight" ADD FOREIGN KEY ("aircraft") REFERENCES "aircraft" ("id");

ALTER TABLE "employee" ADD FOREIGN KEY ("airport") REFERENCES "airport" ("id");

ALTER TABLE "employee" ADD FOREIGN KEY ("department") REFERENCES "department" ("id");