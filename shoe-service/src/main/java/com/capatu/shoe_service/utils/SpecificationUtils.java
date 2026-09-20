package com.capatu.shoe_service.utils;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public final class SpecificationUtils {

    private SpecificationUtils() {
    }

    public static <T> Specification<T> contains(String field, String text) {
        if (!StringUtils.hasText(text)) {
            return Specification.unrestricted();
        }
        String pattern = "%" + text.trim().toLowerCase() + "%";

        return (root, query, cb) -> cb.like(cb.lower(root.get(field)), pattern);
    }

    public static <T> Specification<T> equal(String field, Object value) {
        if (value == null || (value instanceof String text && !StringUtils.hasText(text))) {
            return Specification.unrestricted();
        }
        Object operand = value instanceof String text ? text.trim() : value;

        return (root, query, cb) -> cb.equal(root.get(field), operand);
    }

    public static <T> Specification<T> equalId(String relation, Long id) {
        if (id == null) {
            return Specification.unrestricted();
        }
        return (root, query, cb) -> cb.equal(root.get(relation).get("id"), id);
    }
}
