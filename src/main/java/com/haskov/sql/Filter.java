package com.haskov.sql;

import java.lang.reflect.Field;

import static dk.brics.automaton.BasicOperations.isEmpty;

public class Filter {

    public static boolean areAllFieldsEmpty(Object obj) {
        if (obj == null) {
            return true;
        }

        Field[] fields = obj.getClass().getDeclaredFields();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                Object value = field.get(obj);
                if (isEmpty(value)) {
                    return false;
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    private static boolean isEmpty(Object value) {
        if (value == null) {
            return true;
        }
        if (value instanceof String && ((String) value).isEmpty()) {
            return true;
        }
        // Дополнительные проверки для других типов могут быть добавлены здесь
        return false;
    }
}
