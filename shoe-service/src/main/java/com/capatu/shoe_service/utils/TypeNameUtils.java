package com.capatu.shoe_service.utils;

import java.util.Locale;

public final class TypeNameUtils {

    private TypeNameUtils() {
    }
    public static String tidy(String text) {
        return text.trim().replaceAll("\\s+", " ");
    }

    public static String normalize(String typeName) {
        return typeName.trim().toLowerCase(Locale.ROOT).replaceAll("\\s+", " ");
    }

    public static String toType(String typeName) {
        return normalize(typeName).toUpperCase(Locale.ROOT).replace(' ', '_');
    }
}
