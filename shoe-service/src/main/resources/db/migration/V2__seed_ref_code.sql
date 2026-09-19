-- Data awal ref_code.

INSERT INTO ref_code (type, code, name)
VALUES ('SHOE_TYPE', 'RUNNING',       'Sepatu Lari'),
       ('SHOE_TYPE', 'TRAIL_RUNNING', 'Sepatu Trail'),
       ('SHOE_TYPE', 'HIKING',        'Sepatu Gunung'),
       ('SHOE_TYPE', 'CYCLING',       'Sepatu Sepeda'),
       ('SHOE_TYPE', 'CASUAL',        'Sepatu Kasual');

INSERT INTO ref_code (type, code, name)
VALUES ('SHOE_FEATURE', 'CARBON_PLATE', 'Plat Karbon'),
       ('SHOE_FEATURE', 'WATERPROOF',   'Anti Air'),
       ('SHOE_FEATURE', 'GORE_TEX',     'Gore-Tex'),
       ('SHOE_FEATURE', 'WIDE_TOE_BOX', 'Wide Toe Box');

INSERT INTO ref_code (type, code, name)
VALUES ('ACTIVITY_TYPE', 'EASY_RUN', 'Easy Run'),
       ('ACTIVITY_TYPE', 'LONG_RUN', 'Long Run'),
       ('ACTIVITY_TYPE', 'HIKING',   'Hiking'),
       ('ACTIVITY_TYPE', 'CYCLING',  'Bersepeda');

INSERT INTO ref_code (type, code, name)
VALUES ('MAINTENANCE_ACTION', 'WASH',   'Cuci'),
       ('MAINTENANCE_ACTION', 'REPAIR', 'Perbaikan'),
       ('MAINTENANCE_ACTION', 'RELACE', 'Ganti Tali');
