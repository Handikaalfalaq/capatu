# Capatu

Aplikasi dokumentasi & manajemen sepatu (lari, hiking, dan jenis lainnya) untuk pemakaian pribadi. "Capatu" (dari "Catat" + "Sepatu") mendokumentasikan riwayat/jejak setiap sepatu — kapan dipakai, untuk apa, sudah berapa jauh, dan kapan terakhir dirawat — sehingga pengguna tahu kapan waktunya mencuci atau mengganti sepatu.

Dibuat untuk Technical Assessment Backend Developer (Java) — level **Middle**.

## Latar Belakang & Tujuan

Runner/hiker biasanya rotasi beberapa pasang sepatu untuk aktivitas berbeda (long run, speed training, trail, hiking harian). Pemakaian dan perawatan sepatu ini jarang terdokumentasi dengan baik — kebanyakan orang cuma mengandalkan ingatan atau catatan manual. Akibatnya:
- Sepatu dipakai terus tanpa rotasi yang sehat.
- Lupa terakhir kali dicuci.
- Tidak sadar sepatu sudah melewati batas jarak aman (berisiko cedera).

Capatu menjawab masalah ini dengan mendokumentasikan setiap pemakaian sepatu dan memberi reminder otomatis berdasarkan akumulasi jarak/pemakaian.

## Requirement Assessment yang Dipenuhi (Level Middle)

| Requirement | Implementasi di Capatu |
|---|---|
| Spring IoC | Layering standar: Controller → Service → Repository, dependency injection untuk semua komponen |
| Java Stream | Hitung total jarak per sepatu, filter sepatu yang overdue cuci/ganti, group usage by jenis aktivitas, cari sepatu paling jarang/sering dipakai (analisis rotasi) |
| Intermediate Native SQL Query | Join `shoe` + `usage_log` untuk akumulasi jarak, window function untuk ranking "sepatu paling sering dipakai", subquery untuk cari sepatu overdue maintenance |
| Containerization & Microservices | Dipecah jadi 2 service (`shoe-service` dan `reminder-service`), dijalankan dengan Docker Compose |


## Fitur Utama

1. **Katalog Sepatu** — data sepatu: nama, brand, model, jenis (running, hiking, trail, casual, dll), tanggal dibeli, kondisi.
2. **Log Pemakaian** — setiap dipakai: tanggal, jarak (km), jenis aktivitas (easy run, long run, speed, hiking, dll). Otomatis menambah akumulasi jarak sepatu tersebut.
3. **Log Perawatan** — kapan terakhir dicuci, kondisi sol/upper.
4. **Reminder Otomatis**:
   - Waktunya cuci (belum dicuci setelah N kali pemakaian).
   - Waktunya ganti (akumulasi jarak mendekati/lewat batas umur sepatu, umumnya 500–800km).
   - Rotasi tidak sehat (satu sepatu dipakai jauh lebih sering dibanding yang lain).

## Model Data


## Arsitektur Service

- **shoe-service**: CRUD katalog sepatu, catat usage log & maintenance log, endpoint agregasi (total km per sepatu, riwayat pemakaian).
- **reminder-service**: mengambil data dari `shoe-service` (via REST call), menghitung status reminder (cuci/ganti/rotasi), menyediakan endpoint daftar reminder aktif.
- **Database**: PostgreSQL.
- **Deployment**: Docker Compose (2 service + database).

## Keputusan yang Masih Perlu Difinalkan

- [ ] Skema komunikasi antar service: REST call langsung vs `reminder-service` query DB secara terjadwal (cron/scheduler)
- [ ] Apakah `reminder-service` punya database sendiri (true microservice, data terduplikasi/sync) atau share database dengan `shoe-service` (lebih simpel untuk level Middle)
- [ ] Struktur endpoint API detail (OpenAPI/Swagger)
- [ ] Struktur folder monorepo vs multi-repo untuk kedua service
