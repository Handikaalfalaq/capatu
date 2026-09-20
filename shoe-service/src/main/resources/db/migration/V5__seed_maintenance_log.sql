INSERT INTO maintenance_log (shoe_id, action_ref_id, action_date, notes)
SELECT s.id, r.id, DATE '2026-09-20', v.notes
FROM shoe s
JOIN (VALUES ('CUCI', 'data awal, cuci pertama'),
             ('GANTI_TALI', 'data awal, ganti tali')) AS v(code, notes) ON TRUE
JOIN ref_code r ON r.type = 'MAINTENANCE_ACTION' AND r.code = v.code
WHERE s.name = 'Pegasus Biru';

UPDATE shoe
SET last_washed_at = (
        SELECT MAX(m.action_date)
        FROM maintenance_log m
        JOIN ref_code r ON r.id = m.action_ref_id
        WHERE m.shoe_id = shoe.id AND r.code = 'CUCI'),
    modification_time = now()
WHERE id IN (SELECT shoe_id FROM maintenance_log);

UPDATE shoe
SET usage_count_since_last_wash = (
        SELECT COUNT(*)
        FROM usage_log u
        WHERE u.shoe_id = shoe.id AND (shoe.last_washed_at IS NULL OR u.activity_date > shoe.last_washed_at))
WHERE id IN (SELECT shoe_id FROM maintenance_log);
