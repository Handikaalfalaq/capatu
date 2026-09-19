package com.capatu.shoe_service.utils;

import com.capatu.shoe_service.constant.MessageConstants;
import com.capatu.shoe_service.dto.request.SortRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Field;
import java.util.List;

public final class SortUtils {

    private SortUtils() {
    }

    /**
     * Mengubah daftar sort dari client menjadi Sort. Field harus ada di entity dan bertipe dasar.
     * defaultField selalu ditambahkan di akhir (kalau belum ada) supaya urutan antar halaman stabil.
     */
    public static Sort toSort(List<SortRequest> sorts, Class<?> entityClass, String defaultField) {
        List<Sort.Order> orders = sorts == null ? List.of() : sorts.stream()
                .map(sort -> toOrder(sort, entityClass))
                .toList();

        boolean sortedByDefault = orders.stream().anyMatch(order -> defaultField.equals(order.getProperty()));

        return sortedByDefault ? Sort.by(orders) : Sort.by(orders).and(Sort.by(defaultField));
    }

    private static Sort.Order toOrder(SortRequest sort, Class<?> entityClass) {
        Field field = EntityFieldUtils.find(entityClass, sort.getField());

        if (field == null || !EntityFieldUtils.isSortableType(field.getType())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    MessageConstants.INVALID_SORT_FIELD.formatted(sort.getField()));
        }

        Sort.Direction direction = "DESC".equalsIgnoreCase(sort.getDirection()) ? Sort.Direction.DESC : Sort.Direction.ASC;
        return new Sort.Order(direction, sort.getField());
    }
}
