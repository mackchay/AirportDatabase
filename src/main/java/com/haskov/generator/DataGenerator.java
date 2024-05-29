package com.haskov.generator;

import com.github.javafaker.Faker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DataGenerator {

    public static void generateData(Connection connection) {
        try {
            connection.setAutoCommit(false);

            Faker faker = new Faker();
            generateAirports(connection, faker, 10);
            generatePersonalInfo(connection, faker, 50);
            generateDepartments(connection, 5);
            generateBrigades(connection, 25);
            generateEmployees(connection, faker, 50);
            generateDepartmentHeads(connection, 5);
            generateBrigadeHeads(connection, 5);
            generatePilots(connection, 10);
            generateDispatchers(connection, 10);
            generateTechnicians(connection, 10);
            generateCashiers(connection, 10);
            generateSecurityStaff(connection, 10);
            generateInformationService(connection, 10);
            generateAdministration(connection, 10);
            generateMaintenanceService(connection, 10);

            generateAircrafts(connection, 20);
            generateTechnicalInspections(connection, 20);
            generateMaintenance(connection, 20);
            generateRepairs(connection, 20);
            generateCrews(connection, 20);
            generateSeats(connection, 100);
            generateRoutes(connection, 10);
            generatePassengers(connection, faker, 50);
            generateMedicalExaminations(connection, 20);
            generateFlights(connection, 50);
            generateTickets(connection, 100);
            generateBookedTickets(connection, 30);
            generateDelayedFlights(connection, 10);
            generateCustomControlTransactions(connection, 50);
            generateHangars(connection, 5);
            generateHangarSpaces(connection, 20);

            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void generateAirports(Connection connection, Faker faker, int count) throws SQLException {
        String sql = "INSERT INTO airport (id, name, location) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            for (int i = 1; i <= count; i++) {
                ps.setInt(1, i);
                ps.setString(2, faker.aviation().airport());
                ps.setString(3, faker.address().city());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private static void generatePersonalInfo(Connection connection, Faker faker, int count) throws SQLException {
        String sql = "INSERT INTO personal_info (id, full_name, gender, age, phone_number, children) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            Random random = new Random();
            for (int i = 1; i <= count; i++) {
                ps.setInt(1, i);
                ps.setString(2, faker.name().fullName());
                ps.setString(3, random.nextBoolean() ? "Male" : "Female");
                ps.setInt(4, 20 + random.nextInt(40));
                ps.setLong(5, 7000000000L + random.nextInt(1000000000));
                ps.setInt(6, random.nextInt(4));
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private static void generateEmployees(Connection connection, Faker faker, int count) throws SQLException {
        String sql = "INSERT INTO employee (id, airport, personal_info, brigade, department, salary, employment_date, experience) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            Random random = new Random();
            for (int i = 1; i <= count; i++) {
                ps.setInt(1, i);
                ps.setInt(2, 1);
                ps.setInt(3, i);
                ps.setInt(4, (i - 1) % 5 + 1);
                ps.setInt(5, (i - 1) / 10 + 1);
                ps.setInt(6, 30000 + random.nextInt(20000));
                ps.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now().minusYears(random.nextInt(10))));
                ps.setInt(8, random.nextInt(20));
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private static void generateBrigades(Connection connection, int count) throws SQLException {
        String sql = "INSERT INTO brigade (id) VALUES (?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            for (int i = 1; i <= count; i++) {
                ps.setInt(1, i);
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private static void generateBrigadeHeads(Connection connection, int count) throws SQLException {
        String sql = "INSERT INTO brigade_head (id, employee) VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            for (int i = 1; i <= count; i++) {
                ps.setInt(1, i);
                ps.setInt(2, i * 10);
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private static void generateDepartmentHeads(Connection connection, int count) throws SQLException {
        String sql = "INSERT INTO department_head (id, employee) VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            for (int i = 1; i <= count; i++) {
                ps.setInt(1, i);
                ps.setInt(2, i);
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private static void generateDelayedFlights(Connection connection, int count) throws SQLException {
        String sql = "INSERT INTO delayed_flight (id, delay_reason, old_date, new_date) VALUES (?, ?::delay_reason, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            Random random = new Random();
            for (int i = 1; i <= count; i++) {
                ps.setInt(1, i);
                ps.setString(2, getRandomDelayReason(random));
                ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now().minusDays(random.nextInt(30))));
                ps.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now().plusDays(random.nextInt(30))));
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private static String getRandomDelayReason(Random random) {
        String[] reasons = {"weather_conditions", "technical_problems", "tickets_are_not_sold_out"};
        return reasons[random.nextInt(reasons.length)];
    }

    private static void generateDepartments(Connection connection, int count) throws SQLException {
        String sql = "INSERT INTO department (id, department_title) VALUES (?, ?)";
        ArrayList<String> titles = new ArrayList<>(List.of(new String[]{"pilot", "technician",
                "maintenance", "dispatcher", "service"}));
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            for (int i = 1; i <= count; i++) {
                ps.setInt(1, i);
                ps.setString(2, titles.get(i - 1));
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }


    private static void generateAircrafts(Connection connection, int count) throws SQLException {
        String sql = "INSERT INTO aircraft (id, type, arrival_time, airport, experience, age) VALUES " +
                "(?, ?::aircraft_type, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            Random random = new Random();
            for (int i = 1; i <= count; i++) {
                ps.setInt(1, i);
                ps.setString(2, getRandomAircraftType(random));
                ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now().minusDays(random.nextInt(30))));
                ps.setInt(4, random.nextInt(10) + 1);
                ps.setInt(5, random.nextInt(20));
                ps.setInt(6, random.nextInt(30));
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private static String getRandomAircraftType(Random random) {
        String[] types = {"passenger", "private", "cargo", "war"};
        return types[random.nextInt(types.length)];
    }

    private static void generateCrews(Connection connection, int count) throws SQLException {
        String sql = "INSERT INTO crew (id, aircraft, pilot_brigade, technicians_brigade, maintenance_brigade) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            Random random = new Random();
            for (int i = 1; i <= count; i++) {
                ps.setInt(1, i);
                ps.setInt(2, i);
                ps.setInt(3, i);
                ps.setInt(4, i);
                ps.setInt(5, i);
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private static void generateSeats(Connection connection, int count) throws SQLException {
        String sql = "INSERT INTO seat (id, name) VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            for (int i = 1; i <= count; i++) {
                ps.setInt(1, i);
                ps.setString(2, "Seat " + i);
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private static void generateRoutes(Connection connection, int count) throws SQLException {
        String sql = "INSERT INTO route (id, departure_airport, destination_airport, transfer_airport) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            Random random = new Random();
            for (int i = 1; i <= count; i++) {
                ps.setInt(1, i);
                ps.setInt(2, random.nextInt(10) + 1);
                ps.setInt(3, random.nextInt(10) + 1);
                if (random.nextBoolean()) {
                    ps.setInt(4, random.nextInt(10) + 1);
                } else {
                    ps.setNull(4, 0);
                }
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private static void generateBookedTickets(Connection connection, int count) throws SQLException {
        String sql = "INSERT INTO booked_ticket (id, booking_start, booking_end) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            Random random = new Random();
            for (int i = 1; i <= count; i++) {
                ps.setInt(1, i);
                ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now().minusDays(random.nextInt(30))));
                ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now().plusDays(random.nextInt(30))));
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private static void generateTickets(Connection connection, int count) throws SQLException {
        String sql = "INSERT INTO ticket (id, flight, passenger, seat, purchase_date, refund_date, ticket_price, status, baggage) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?::ticket_status, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            Random random = new Random();
            for (int i = 1; i <= count; i++) {
                ps.setInt(1, i);
                ps.setInt(2, random.nextInt(50) + 1);
                ps.setInt(3, random.nextInt(50) + 1);
                ps.setInt(4, i);
                ps.setTimestamp(5, random.nextBoolean() ? Timestamp.valueOf(LocalDateTime.now().minusDays(random.nextInt(30))) : null);
                ps.setTimestamp(6, random.nextBoolean() ? Timestamp.valueOf(LocalDateTime.now().plusDays(random.nextInt(30))) : null);
                ps.setInt(7, 100 + random.nextInt(1000));
                ps.setString(8, getRandomTicketStatus(random));
                ps.setBoolean(9, random.nextBoolean());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private static String getRandomTicketStatus(Random random) {
        String[] statuses = {"not_purchased", "booked", "purchased"};
        return statuses[random.nextInt(statuses.length)];
    }

    private static void generatePassengers(Connection connection, Faker faker, int count) throws SQLException {
        String sql = "INSERT INTO passenger (id, full_name, gender, age, phone_number, passport, international_passport) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            Random random = new Random();
            for (int i = 1; i <= count; i++) {
                ps.setInt(1, i);
                ps.setString(2, faker.name().fullName());
                ps.setString(3, random.nextBoolean() ? "Male" : "Female");
                ps.setInt(4, 18 + random.nextInt(60));
                ps.setLong(5, 1000000000L + random.nextInt(900000000));
                ps.setLong(6, 1000000000L + random.nextInt(900000000));
                ps.setLong(7,1000000000L + random.nextInt(900000000));
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private static void generateMedicalExaminations(Connection connection, int count) throws SQLException {
        String sql = "INSERT INTO medical_examination (id, pilot, date, result) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            Random random = new Random();
            for (int i = 1; i <= count; i++) {
                ps.setInt(1, i);
                ps.setInt(2, random.nextInt(10) + 1);
                ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now().minusDays(random.nextInt(30))));
                ps.setBoolean(4, random.nextBoolean());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private static void generateFlights(Connection connection, int count) throws SQLException {
        String sql = "INSERT INTO flight (id, flight_type, aircraft, route, departure, landing, status) VALUES " +
                "(?, ?::flight_type, ?, ?, ?, ?, ?::flight_status)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            Random random = new Random();
            for (int i = 1; i <= count; i++) {
                ps.setInt(1, i);
                ps.setString(2, getRandomFlightType(random));
                ps.setInt(3, i / 3 + 1);
                ps.setInt(4, random.nextInt(10) + 1);
                ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now().minusDays(i)));
                ps.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now().plusDays(i)));
                ps.setString(7, getRandomFlightStatus(random));
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private static String getRandomFlightType(Random random) {
        String[] types = {"domestic", "international", "charter", "cargo", "special"};
        return types[random.nextInt(types.length)];
    }

    private static String getRandomFlightStatus(Random random) {
        String[] statuses = {"valid", "completed", "cancelled", "postponed"};
        return statuses[random.nextInt(statuses.length)];
    }

    private static void generatePilots(Connection connection, int count) throws SQLException {
        String sql = "INSERT INTO pilot (employee, license_type, specialization) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            for (int i = 1; i <= count; i++) {
                ps.setInt(1, i);
                ps.setString(2, "License " + i);
                ps.setString(3, "Specialization " + i);
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private static void generateDispatchers(Connection connection, int count) throws SQLException {
        String sql = "INSERT INTO dispatcher (employee, certification_level, handling_stressful_situations) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            for (int i = 1; i <= count; i++) {
                ps.setInt(1, i);
                ps.setString(2, "Level " + i);
                ps.setString(3, "Skill " + i);
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private static void generateTechnicians(Connection connection, int count) throws SQLException {
        String sql = "INSERT INTO technician (employee, specialization, skill_level) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            for (int i = 1; i <= count; i++) {
                ps.setInt(1, i + 10);
                ps.setString(2, "Specialization " + i);
                ps.setString(3, "Level " + i);
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private static void generateCashiers(Connection connection, int count) throws SQLException {
        String sql = "INSERT INTO cashier (employee, equipment_skills, ticket_sales_policies_and_procedure_knowledge, communication_skills) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            for (int i = 1; i <= count; i++) {
                ps.setInt(1, i);
                ps.setString(2, "Skill " + i);
                ps.setString(3, "Knowledge " + i);
                ps.setString(4, "Skill " + i);
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private static void generateSecurityStaff(Connection connection, int count) throws SQLException {
        String sql = "INSERT INTO security_staff (employee, security_training_level, detecting_and_responding_to_security_incidents_skills) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            for (int i = 1; i <= count; i++) {
                ps.setInt(1, i);
                ps.setString(2, "Level " + i);
                ps.setString(3, "Skill " + i);
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private static void generateInformationService(Connection connection, int count) throws SQLException {
        String sql = "INSERT INTO information_service (employee, airport_procedures_and_regulations_knowledge, communication_skills, flight_schedules_and_etc_knowledge) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            for (int i = 1; i <= count; i++) {
                ps.setInt(1, i);
                ps.setString(2, "Knowledge " + i);
                ps.setString(3, "Skill " + i);
                ps.setString(4, "Knowledge " + i);
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private static void generateAdministration(Connection connection, int count) throws SQLException {
        String sql = "INSERT INTO administration (employee, analytical_and_planning_skills, aviation_industry_knowledge) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            for (int i = 1; i <= count; i++) {
                ps.setInt(1, i);
                ps.setString(2, "Skill " + i);
                ps.setString(3, "Knowledge " + i);
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private static void generateMaintenanceService(Connection connection, int count) throws SQLException {
        String sql = "INSERT INTO maintenance_staff (employee, passenger_service_professionalism, luggage_handling_skills, cleanliness_and_accuracy_in_salon_service) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            for (int i = 1; i <= count; i++) {
                ps.setInt(1, i + 20);
                ps.setString(2, "Skill " + i);
                ps.setString(3, "Skill " + i);
                ps.setString(4, "Skill " + i);
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private static void generateTechnicalInspections(Connection connection, int count) throws SQLException {
        String sql = "INSERT INTO technical_inspection (id, aircraft, date, result) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            Random random = new Random();
            for (int i = 1; i <= count; i++) {
                ps.setInt(1, i);
                ps.setInt(2, random.nextInt(20) + 1);
                ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now().minusDays(random.nextInt(30))));
                ps.setBoolean(4, random.nextBoolean());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private static void generateMaintenance(Connection connection, int count) throws SQLException {
        String sql = "INSERT INTO maintenance (id, aircraft, date) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            Random random = new Random();
            for (int i = 1; i <= count; i++) {
                ps.setInt(1, i);
                ps.setInt(2, random.nextInt(20) + 1);
                ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now().minusDays(random.nextInt(30))));
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private static void generateRepairs(Connection connection, int count) throws SQLException {
        String sql = "INSERT INTO repair (id, aircraft, repair_type, date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            Random random = new Random();
            String[] repairTypes = {"Routine", "Emergency", "Preventive"};
            for (int i = 1; i <= count; i++) {
                ps.setInt(1, i);
                ps.setInt(2, random.nextInt(20) + 1);
                ps.setString(3, repairTypes[random.nextInt(repairTypes.length)]);
                ps.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now().minusDays(random.nextInt(30))));
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private static void generateCustomControlTransactions(Connection connection, int count) throws SQLException {
        String sql = "INSERT INTO custom_control_transaction (id, inspection_type, passenger, date, result) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            Random random = new Random();
            String[] inspectionTypes = {"Baggage Screening", "Passenger Screening", "Document Verification"};
            for (int i = 1; i <= count; i++) {
                ps.setInt(1, i);
                ps.setString(2, inspectionTypes[random.nextInt(inspectionTypes.length)]);
                ps.setInt(3, random.nextInt(50) + 1);
                ps.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now().minusDays(random.nextInt(30))));
                ps.setBoolean(5, random.nextBoolean());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private static void generateHangarSpaces(Connection connection, int count) throws SQLException {
        String sql = "INSERT INTO hangar_space (id, hangar, aircraft, arrival_time, departure_time, status) " +
                "VALUES (?, ?, ?, ?, ?, ?::aircraft_status)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            Random random = new Random();
            String[] statuses = {"ready", "on_repair", "required_technical_inspection"};
            for (int i = 1; i <= count; i++) {
                ps.setInt(1, i);
                ps.setInt(2, random.nextInt(5) + 1);
                ps.setInt(3, random.nextInt(20) + 1);
                ps.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now().minusDays(random.nextInt(30))));
                ps.setTimestamp(5, random.nextBoolean() ? Timestamp.valueOf(LocalDateTime.now().plusDays(random.nextInt(30))) : null);
                ps.setString(6, statuses[random.nextInt(statuses.length)]);
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private static void generateHangars(Connection connection, int count) throws SQLException {
        String sql = "INSERT INTO hangar (id, airport) VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            for (int i = 1; i <= count; i++) {
                ps.setInt(1, i);
                ps.setInt(2, i);
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }
}
