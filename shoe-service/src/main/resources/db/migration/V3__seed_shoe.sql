WITH new_shoe AS (
    INSERT INTO shoe (name, brand, model, shoe_type_ref_id, color, size_value, size_system,
                      weight_grams, purchase_date, purchase_price, target_lifespan_km)
    VALUES ('Pegasus Biru', 'Nike', 'Air Zoom Pegasus 40',
            (SELECT id FROM ref_code WHERE type = 'SHOE_TYPE' AND code = 'SEPATU_LARI'),
            'biru', 42.5, 'EU', 280, DATE '2026-09-20', 1599000, 600)
    RETURNING id
)
INSERT INTO shoe_feature_map (shoe_id, feature_ref_id)
SELECT new_shoe.id, ref_code.id
FROM new_shoe
JOIN ref_code ON ref_code.type = 'SHOE_FEATURE' AND ref_code.code IN ('PLAT_KARBON', 'ANTI_AIR');
