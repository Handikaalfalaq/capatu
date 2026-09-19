-- code diturunkan dari code_name: huruf besar, spasi jadi underscore (sama seperti type dari type_name).

INSERT INTO ref_code (type, type_name, code, code_name)
VALUES ('SHOE_TYPE', 'shoe type', 'SEPATU_LARI',   'Sepatu Lari'),
       ('SHOE_TYPE', 'shoe type', 'SEPATU_TRAIL',  'Sepatu Trail'),
       ('SHOE_TYPE', 'shoe type', 'SEPATU_GUNUNG', 'Sepatu Gunung'),
       ('SHOE_TYPE', 'shoe type', 'SEPATU_SEPEDA', 'Sepatu Sepeda'),
       ('SHOE_TYPE', 'shoe type', 'SEPATU_KASUAL', 'Sepatu Kasual');

INSERT INTO ref_code (type, type_name, code, code_name)
VALUES ('SHOE_FEATURE', 'shoe feature', 'PLAT_KARBON',  'Plat Karbon'),
       ('SHOE_FEATURE', 'shoe feature', 'ANTI_AIR',     'Anti Air'),
       ('SHOE_FEATURE', 'shoe feature', 'GORE_TEX',     'Gore Tex'),
       ('SHOE_FEATURE', 'shoe feature', 'WIDE_TOE_BOX', 'Wide Toe Box');

INSERT INTO ref_code (type, type_name, code, code_name)
VALUES ('ACTIVITY_TYPE', 'activity type', 'EASY_RUN',  'Easy Run'),
       ('ACTIVITY_TYPE', 'activity type', 'LONG_RUN',  'Long Run'),
       ('ACTIVITY_TYPE', 'activity type', 'HIKING',    'Hiking'),
       ('ACTIVITY_TYPE', 'activity type', 'BERSEPEDA', 'Bersepeda');

INSERT INTO ref_code (type, type_name, code, code_name)
VALUES ('MAINTENANCE_ACTION', 'maintenance action', 'CUCI',      'Cuci'),
       ('MAINTENANCE_ACTION', 'maintenance action', 'PERBAIKAN', 'Perbaikan'),
       ('MAINTENANCE_ACTION', 'maintenance action', 'GANTI_TALI', 'Ganti Tali');
