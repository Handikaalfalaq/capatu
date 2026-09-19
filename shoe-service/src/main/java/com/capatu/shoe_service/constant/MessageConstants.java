package com.capatu.shoe_service.constant;

public final class MessageConstants {

    private MessageConstants() {
    }

    // Nama resource yang dimasukkan ke template di bawah
    public static final String RESOURCE_REF_CODE = "Referensi code";

    // Template dengan satu nama resource
    public static final String CREATED = "%s berhasil dibuat";
    public static final String UPDATED = "%s berhasil diubah";
    public static final String DELETED = "%s berhasil dihapus";
    public static final String NOT_FOUND = "%s tidak ditemukan";
    public static final String IN_USE = "%s masih digunakan dan tidak bisa dihapus";

    // Template khusus ref code: kode, lalu type
    public static final String CODE_ALREADY_EXISTS = "Kode %s sudah ada untuk type %s";


    public static final String NAME_FORMAT = "^ *[A-Za-z][A-Za-z0-9 ]*$";

    public static final String VALIDATION_REQUIRED = "wajib diisi";
    public static final String VALIDATION_MAX_LENGTH = "maksimal {max} karakter";
    public static final String VALIDATION_NAME_FORMAT = "hanya boleh huruf, angka, dan spasi, diawali huruf";
}
