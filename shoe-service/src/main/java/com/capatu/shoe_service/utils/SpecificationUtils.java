package com.capatu.shoe_service.utils;

import com.capatu.shoe_service.constant.Constants;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

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

    public static <T, V extends Comparable<? super V>> Specification<T> between(String field, V from, V to, V defaultFrom, V defaultTo) {
        if (from == null && to == null) {
            return Specification.unrestricted();
        }
        V start = from != null ? from : defaultFrom;
        V end = to != null ? to : defaultTo;

        if (start.compareTo(end) > 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, Constants.INVALID_RANGE.formatted(field));
        }
        return (root, query, cb) -> cb.between(root.<V>get(field), start, end);
    }
}
