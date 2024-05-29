package com.haskov.creator;

import com.haskov.generator.DataGenerator;
import com.haskov.sql.Config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

public class AirportDatabaseCreator {

    private static void executeSqlScriptFromFile(Connection connection, String[] queries) {
        try (Statement statement = connection.createStatement()) {
            for (String query : queries) {
                if (!query.trim().isEmpty()) {
                    System.out.println("Executing query: " + query.trim());
                    statement.execute(query.trim());
                }
            }

            System.out.println("Запросы успешно выполнены.");
        } catch (SQLException e) {
            System.err.println("Ошибка выполнения SQL запроса: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        String url = Config.getProperty("db.url");
        String user = Config.getProperty("db.user");
        String password = Config.getProperty("db.password");
        String sqlScriptPath = "src/main/resources/Airport.sql";
        String triggerSqlScriptPath = "src/main/resources/Triggers.sql";
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            connection.setAutoCommit(false);
            String[] queries1 = readFile(sqlScriptPath).split(";");
            executeSqlScriptFromFile(connection, queries1);
            String[] queries2 = readFile(triggerSqlScriptPath).split("\n\n");
            executeSqlScriptFromFile(connection, queries2);
            DataGenerator.generateData(connection);
            connection.commit();
        } catch (SQLException | IOException e) {
            System.err.println("Ошибка соединения или чтения файла: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private static String readFile(String path) throws IOException {
        Path filePath = Paths.get(path);
        return Files.lines(filePath).collect(Collectors.joining(System.lineSeparator()));
    }
}
