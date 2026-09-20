INSERT INTO shoe (name, brand, model, shoe_type_ref_id, color, size_value, size_system,
                  weight_grams, purchase_date, purchase_price, target_lifespan_km)
SELECT v.name, v.brand, v.model, t.id, v.color, v.size_value, 'EU',
       v.weight_grams, v.purchase_date, v.purchase_price, v.target_lifespan_km
FROM (VALUES
        ('Ultraboost Abu', 'Adidas', 'Ultraboost 22', 'SEPATU_LARI',   'abu-abu', 42.0, 310, DATE '2026-08-15', 2500000, 600),
        ('Speedgoat',      'Hoka',   'Speedgoat 5',   'SEPATU_TRAIL',  'hijau',   43.0, 300, DATE '2026-06-01', 2400000, 700),
        ('Kasual Polos',   'Ortus',  'KP-01',         'SEPATU_KASUAL', 'hitam',   41.0, 350, DATE '2026-05-01',  450000, NULL),
        ('Vaporfly Tua',   'Nike',   'Vaporfly 3',    'SEPATU_LARI',   'oranye',  42.5, 190, DATE '2025-11-01', 3200000, 300)
     ) AS v(name, brand, model, type_code, color, size_value, weight_grams, purchase_date, purchase_price, target_lifespan_km)
JOIN ref_code t ON t.type = 'SHOE_TYPE' AND t.code = v.type_code;

INSERT INTO shoe_feature_map (shoe_id, feature_ref_id)
SELECT s.id, f.id
FROM (VALUES
        ('Speedgoat',    'ANTI_AIR'),
        ('Speedgoat',    'GORE_TEX'),
        ('Vaporfly Tua', 'PLAT_KARBON')
     ) AS v(shoe_name, feature_code)
JOIN shoe s ON s.name = v.shoe_name
JOIN ref_code f ON f.type = 'SHOE_FEATURE' AND f.code = v.feature_code;

INSERT INTO usage_log (shoe_id, activity_date, distance_km, activity_type_ref_id, duration_minutes)
SELECT s.id, v.activity_date, v.distance_km, a.id, v.duration_minutes
FROM (VALUES
        ('Ultraboost Abu', DATE '2026-09-02',  6.0, 'EASY_RUN',  38),
        ('Ultraboost Abu', DATE '2026-09-04', 10.0, 'LONG_RUN',  65),
        ('Ultraboost Abu', DATE '2026-09-07',  7.5, 'EASY_RUN',  45),
        ('Ultraboost Abu', DATE '2026-09-09',  5.0, 'EASY_RUN',  30),
        ('Ultraboost Abu', DATE '2026-09-11',  8.0, 'EASY_RUN',  48),
        ('Ultraboost Abu', DATE '2026-09-14', 12.0, 'LONG_RUN',  80),
        ('Ultraboost Abu', DATE '2026-09-16',  6.5, 'EASY_RUN',  40),
        ('Ultraboost Abu', DATE '2026-09-19',  9.0, 'EASY_RUN',  55),
        ('Speedgoat',      DATE '2026-09-03', 15.0, 'HIKING',   240),
        ('Speedgoat',      DATE '2026-09-12',  9.0, 'HIKING',   150)
     ) AS v(shoe_name, activity_date, distance_km, activity_code, duration_minutes)
JOIN shoe s ON s.name = v.shoe_name
JOIN ref_code a ON a.type = 'ACTIVITY_TYPE' AND a.code = v.activity_code;

INSERT INTO usage_log (shoe_id, activity_date, distance_km, activity_type_ref_id, duration_minutes)
SELECT s.id, DATE '2026-01-03' + g * 7, 9.5, a.id, 55
FROM shoe s
CROSS JOIN generate_series(0, 29) AS g
JOIN ref_code a ON a.type = 'ACTIVITY_TYPE' AND a.code = 'EASY_RUN'
WHERE s.name = 'Vaporfly Tua';

UPDATE shoe
SET total_distance_km = summary.total_distance_km,
    usage_count = summary.usage_count,
    last_used_at = summary.last_used_at,
    usage_count_since_last_wash = summary.usage_count_since_last_wash,
    modification_time = now()
FROM (
    SELECT u.shoe_id,
           SUM(u.distance_km)   AS total_distance_km,
           COUNT(*)             AS usage_count,
           MAX(u.activity_date) AS last_used_at,
           COUNT(*) FILTER (WHERE s.last_washed_at IS NULL OR u.activity_date > s.last_washed_at) AS usage_count_since_last_wash
    FROM usage_log u
    JOIN shoe s ON s.id = u.shoe_id
    GROUP BY u.shoe_id
) summary
WHERE shoe.id = summary.shoe_id;
