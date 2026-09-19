package com.capatu.shoe_service.utils;

import jakarta.persistence.Transient;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.temporal.Temporal;

public final class EntityFieldUtils {

    private EntityFieldUtils() {
    }

    public static Field find(Class<?> entityClass, String name) {
        Field field = ReflectionUtils.findField(entityClass, name);

        if (field == null || Modifier.isStatic(field.getModifiers()) || field.isAnnotationPresent(Transient.class)) {
            return null;
        }
        return field;
    }

    public static boolean isSortableType(Class<?> type) {
        return type.isPrimitive()
                || type == String.class
                || type == Boolean.class
                || Number.class.isAssignableFrom(type)
                || Temporal.class.isAssignableFrom(type);
    }
}
