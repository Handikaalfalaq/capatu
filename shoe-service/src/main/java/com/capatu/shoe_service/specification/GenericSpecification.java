package com.capatu.shoe_service.specification;

import com.capatu.shoe_service.constant.MessageConstants;
import com.capatu.shoe_service.dto.request.FilterCriteria;
import com.capatu.shoe_service.enums.FilterOperator;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import com.capatu.shoe_service.utils.EntityFieldUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

public final class GenericSpecification {

    private static final int MAX_IN_VALUES = 50;
    private static final char LIKE_ESCAPE = '\\';

    private GenericSpecification() {
    }

    private record Condition(String field, FilterOperator operator, Object operand) {
    }

    public static <T> Specification<T> fromFilters(Class<T> entityClass, List<FilterCriteria> filters) {
        if (filters == null || filters.isEmpty()) {
            return (root, query, cb) -> cb.conjunction();
        }

        List<Condition> conditions = filters.stream()
                .map(filter -> toCondition(entityClass, filter))
                .toList();

        return (root, query, cb) -> cb.and(conditions.stream()
                .map(condition -> toPredicate(condition, root, cb))
                .toArray(Predicate[]::new));
    }

    private static Condition toCondition(Class<?> entityClass, FilterCriteria filter) {
        Field field = EntityFieldUtils.find(entityClass, filter.getField());

        if (field == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    MessageConstants.INVALID_FILTER_FIELD.formatted(filter.getField()));
        }
        return new Condition(filter.getField(), filter.getOperator(), toOperand(filter, field.getType()));
    }

    private static <T> Predicate toPredicate(Condition condition, Root<T> root, CriteriaBuilder cb) {
        String field = condition.field();

        return switch (condition.operator()) {
            case EQ -> cb.equal(root.get(field), condition.operand());
            case NE -> cb.notEqual(root.get(field), condition.operand());
            case IN -> root.get(field).in((Collection<?>) condition.operand());
            case LIKE -> cb.like(cb.lower(root.<String>get(field)),
                    "%" + escapeLike(((String) condition.operand()).toLowerCase(Locale.ROOT)) + "%", LIKE_ESCAPE);
        };
    }

    private static Object toOperand(FilterCriteria filter, Class<?> fieldType) {
        if (filter.getOperator() == FilterOperator.LIKE && fieldType != String.class) {
            throw invalidValue(filter);
        }

        if (filter.getOperator() == FilterOperator.IN) {
            if (!(filter.getValue() instanceof List<?> items) || items.isEmpty() || items.size() > MAX_IN_VALUES) {
                throw invalidValue(filter);
            }
            return items.stream()
                    .map(item -> toScalar(filter, item, fieldType))
                    .toList();
        }

        return toScalar(filter, filter.getValue(), fieldType);
    }

    private static Object toScalar(FilterCriteria filter, Object raw, Class<?> fieldType) {
        if (fieldType == String.class) {
            if (raw instanceof String || raw instanceof Number) {
                return String.valueOf(raw);
            }
        } else if (fieldType == Boolean.class || fieldType == boolean.class) {
            if (raw instanceof Boolean flag) {
                return flag;
            }
            if (raw instanceof String text && ("true".equalsIgnoreCase(text) || "false".equalsIgnoreCase(text))) {
                return Boolean.parseBoolean(text);
            }
        } else if (fieldType == Long.class || fieldType == long.class) {
            return toLong(filter, raw);
        } else if (fieldType == Integer.class || fieldType == int.class) {
            long value = toLong(filter, raw);
            if (value >= Integer.MIN_VALUE && value <= Integer.MAX_VALUE) {
                return (int) value;
            }
        }
        throw invalidValue(filter);
    }

    private static long toLong(FilterCriteria filter, Object raw) {
        if (raw instanceof Integer || raw instanceof Long) {
            return ((Number) raw).longValue();
        }
        if (raw instanceof String text) {
            try {
                return Long.parseLong(text.trim());
            } catch (NumberFormatException e) {
                throw invalidValue(filter);
            }
        }
        throw invalidValue(filter);
    }

    private static String escapeLike(String text) {
        return text.replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_");
    }

    private static ResponseStatusException invalidValue(FilterCriteria filter) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST,
                MessageConstants.INVALID_FILTER_VALUE.formatted(filter.getField()));
    }
}
