package com.capatu.shoe_service.constant;

import java.math.BigDecimal;

public final class Constants {

    private Constants() {
    }

    public static final String RESOURCE_REF_CODE = "Referensi code";
    public static final String RESOURCE_SHOE = "shoe";
    public static final String RESOURCE_USAGE_LOG = "usage log";
    public static final String RESOURCE_MAINTENANCE_LOG = "maintenance log";

    public static final String CREATED = "%s berhasil dibuat";
    public static final String UPDATED = "%s berhasil diubah";
    public static final String DELETED = "%s berhasil dihapus";
    public static final String NOT_FOUND = "%s tidak ditemukan";
    public static final String IN_USE = "%s masih digunakan dan tidak bisa dihapus";
    public static final String TYPE_IN_USE = "%s masih digunakan dan type-nya tidak bisa diubah";

    public static final String CODE_ALREADY_EXISTS = "Kode %s sudah ada untuk type %s";

    public static final String INVALID_SHOE_TYPE_REF = "shoeTypeRefId tidak valid, harus id jenis sepatu";
    public static final String INVALID_FEATURE_REF = "featureRefIds tidak valid, id %s bukan fitur sepatu";

    public static final String INVALID_ACTIVITY_TYPE_REF = "activityTypeRefId tidak valid, harus id jenis aktivitas";
    public static final String SHOE_RETIRED = "%s sudah pensiun dan tidak bisa mencatat pemakaian baru";
    public static final String USAGE_BEFORE_PURCHASE = "activityDate tidak boleh sebelum tanggal beli sepatu (%s)";
    public static final String SHOE_CANNOT_CHANGE = "Sepatu pada %s tidak bisa diubah";
    public static final String INVALID_RANGE = "Rentang %s tidak valid, nilai awal lebih besar dari nilai akhir";

    public static final String INVALID_MAINTENANCE_ACTION_REF = "actionRefId tidak valid, harus id tindakan perawatan";
    public static final String SHOE_RETIRED_MAINTENANCE = "%s sudah pensiun dan tidak bisa mencatat perawatan baru";
    public static final String ACTION_BEFORE_PURCHASE = "actionDate tidak boleh sebelum tanggal beli sepatu (%s)";
    public static final String WASH_CODE = "CUCI";


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

    public static final BigDecimal MAX_DISTANCE_KM = new BigDecimal("9999.99");

    public static final String SHOE_TYPE = "SHOE_TYPE";
    public static final String SHOE_FEATURE = "SHOE_FEATURE";
    public static final String ACTIVITY_TYPE = "ACTIVITY_TYPE";
    public static final String MAINTENANCE_ACTION = "MAINTENANCE_ACTION";
}
