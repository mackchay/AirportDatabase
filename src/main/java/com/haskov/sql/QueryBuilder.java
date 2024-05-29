package com.haskov.sql;

import java.io.*;
import java.util.Objects;

public abstract class QueryBuilder {

    protected static String readFileToString(String filePath) {
        StringBuilder stringBuilder = new StringBuilder();

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream(filePath);
             BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(inputStream)))) {

            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return stringBuilder.toString();
    }

    protected String getMinMaxValues(Integer min, Integer max) {
        if (min != null && max != null) {
            return " between " + min + " and " + max;
        }
        if (min != null) {
            return " > " + min;
        }
        if (max != null) {
            return " < " + max;
        }
        return "";
    }

    protected abstract String appendFilter(Filter f, StringBuilder builder);

}
