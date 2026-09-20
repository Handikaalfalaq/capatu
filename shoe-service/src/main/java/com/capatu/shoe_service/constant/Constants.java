package com.capatu.shoe_service.constant;

public final class Constants {

    private Constants() {
    }

    public static final String RESOURCE_REF_CODE = "Referensi code";
    public static final String RESOURCE_SHOE = "shoe";

    public static final String CREATED = "%s berhasil dibuat";
    public static final String UPDATED = "%s berhasil diubah";
    public static final String DELETED = "%s berhasil dihapus";
    public static final String NOT_FOUND = "%s tidak ditemukan";
    public static final String IN_USE = "%s masih digunakan dan tidak bisa dihapus";

    public static final String CODE_ALREADY_EXISTS = "Kode %s sudah ada untuk type %s";

    public static final String INVALID_SHOE_TYPE_REF = "shoeTypeRefId tidak valid, harus id jenis sepatu";
    public static final String INVALID_FEATURE_REF = "featureRefIds tidak valid, id %s bukan fitur sepatu";


    public static final String NAME_FORMAT = "^ *[A-Za-z][A-Za-z0-9 ]*$";

    public static final String VALIDATION_REQUIRED = "wajib diisi";
    public static final String VALIDATION_MAX_LENGTH = "maksimal {max} karakter";
    public static final String VALIDATION_NAME_FORMAT = "hanya boleh huruf, angka, dan spasi, diawali huruf";

    public static final String VALIDATION_MIN_VALUE = "minimal {value}";
    public static final String VALIDATION_MAX_VALUE = "maksimal {value}";

    public static final String VALIDATION_POSITIVE = "harus lebih dari 0";
    public static final String VALIDATION_POSITIVE_OR_ZERO = "tidak boleh negatif";
    public static final String VALIDATION_PAST_OR_PRESENT = "tidak boleh di masa depan";
    public static final String VALIDATION_DIGITS = "maksimal {integer} digit bulat dan {fraction} desimal";

    public static final String SIZE_SYSTEM_FORMAT = "^(EU|US|UK|CM)$";
    public static final String VALIDATION_SIZE_SYSTEM = "harus EU, US, UK, atau CM";

    public static final int DEFAULT_PAGE = 0;
    public static final int DEFAULT_SIZE = 10;
    public static final String DEFAULT_SORT_FIELD = "id";

    public static final String SHOE_TYPE = "SHOE_TYPE";
    public static final String SHOE_FEATURE = "SHOE_FEATURE";
    public static final String ACTIVITY_TYPE = "ACTIVITY_TYPE";
    public static final String MAINTENANCE_ACTION = "MAINTENANCE_ACTION";
}
