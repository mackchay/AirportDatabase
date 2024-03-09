package com.haskov;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

public class ExecuteSqlScriptFromFile {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/airport";
        String user = "postgres";
        String password = "postgres";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            // Путь к вашему SQL-скрипту относительно корня проекта
            String sqlScriptPath = "src/main/resources/Airport.sql";
            String sqlScript = readFile(sqlScriptPath);

            try (Statement statement = connection.createStatement()) {
                // Разделение SQL-скрипта на отдельные запросы
                String[] queries = sqlScript.split(";");

                // Выполнение каждого запроса
                for (String query : queries) {
                    statement.addBatch(query);
                }

                // Выполнение пакета запросов
                int[] result = statement.executeBatch();
                System.out.println("Запросы успешно выполнены.");
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private static String readFile(String path) throws IOException {
        Path filePath = Paths.get(path);
        return Files.lines(filePath).collect(Collectors.joining(" "));
    }
}