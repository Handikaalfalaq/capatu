package com.capatu.shoe_service.constant;

public final class MessageConstants {

    private MessageConstants() {
    }

    public static final String RESOURCE_REF_CODE = "Referensi code";

    public static final String CREATED = "%s berhasil dibuat";
    public static final String UPDATED = "%s berhasil diubah";
    public static final String DELETED = "%s berhasil dihapus";
    public static final String NOT_FOUND = "%s tidak ditemukan";
    public static final String IN_USE = "%s masih digunakan dan tidak bisa dihapus";

    public static final String CODE_ALREADY_EXISTS = "Kode %s sudah ada untuk type %s";


    public static final String NAME_FORMAT = "^ *[A-Za-z][A-Za-z0-9 ]*$";

    public static final String VALIDATION_REQUIRED = "wajib diisi";
    public static final String VALIDATION_MAX_LENGTH = "maksimal {max} karakter";
    public static final String VALIDATION_NAME_FORMAT = "hanya boleh huruf, angka, dan spasi, diawali huruf";

    public static final String VALIDATION_MIN_VALUE = "minimal {value}";
    public static final String VALIDATION_MAX_VALUE = "maksimal {value}";

    public static final String SORT_DIRECTION_FORMAT = "(?i)^(ASC|DESC)$";
    public static final String VALIDATION_SORT_DIRECTION = "harus ASC atau DESC";

    public static final String INVALID_FILTER_FIELD = "Field '%s' tidak dapat difilter";
    public static final String INVALID_FILTER_VALUE = "Nilai filter untuk field '%s' tidak valid";
    public static final String INVALID_SORT_FIELD = "Field '%s' tidak dapat diurutkan";

    public static final int DEFAULT_PAGE = 0;
    public static final int DEFAULT_SIZE = 10;
    public static final String DEFAULT_SORT_FIELD = "id";
}
