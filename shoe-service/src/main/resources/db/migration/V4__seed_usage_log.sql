INSERT INTO usage_log (shoe_id, activity_date, distance_km, activity_type_ref_id, duration_minutes, notes)
SELECT s.id, DATE '2026-09-20', 5.3, r.id, 30, 'data awal'
FROM shoe s
JOIN ref_code r ON r.type = 'ACTIVITY_TYPE' AND r.code = 'EASY_RUN'
WHERE s.name = 'Pegasus Biru';

UPDATE shoe
SET total_distance_km = summary.total_distance_km,
    usage_count = summary.usage_count,
    last_used_at = summary.last_used_at,
    usage_count_since_last_wash = summary.usage_count_since_last_wash,
    modification_time = now()
FROM (
    SELECT u.shoe_id,
           SUM(u.distance_km)  AS total_distance_km,
           COUNT(*)            AS usage_count,
           MAX(u.activity_date) AS last_used_at,
           COUNT(*) FILTER (WHERE s.last_washed_at IS NULL OR u.activity_date > s.last_washed_at) AS usage_count_since_last_wash
    FROM usage_log u
    JOIN shoe s ON s.id = u.shoe_id
    GROUP BY u.shoe_id
) summary
WHERE shoe.id = summary.shoe_id;
