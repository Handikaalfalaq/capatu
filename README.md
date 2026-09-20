# Capatu (Catatan Sepatu)

Aplikasi dokumentasi & manajemen sepatu (lari, hiking, dan jenis lainnya) untuk pemakaian pribadi. Capatu mendokumentasikan riwayat/jejak setiap sepatu kapan dipakai, untuk apa, sudah berapa jauh, dan kapan terakhir dirawat sehingga pengguna tahu kapan waktunya mencuci atau mengganti sepatu.

## Latar Belakang & Tujuan

Runner/hiker biasanya rotasi beberapa pasang sepatu untuk aktivitas berbeda (long run, speed training, trail, hiking harian). Pemakaian dan perawatan sepatu ini jarang terdokumentasi dengan baik kebanyakan orang cuma mengandalkan ingatan atau catatan manual. Akibatnya:
- Sepatu dipakai terus tanpa rotasi yang sehat.
- Lupa terakhir kali dicuci.
- Tidak sadar sepatu sudah melewati batas jarak aman (berisiko cedera).

Capatu menjawab masalah ini dengan mendokumentasikan setiap pemakaian sepatu dan memberi reminder otomatis berdasarkan akumulasi jarak/pemakaian.

## Fitur Utama

- **Katalog sepatu** — data dasar tiap sepatu (nama, brand, jenis, dll) beserta status pemakaiannya.
- **Log pemakaian** — catat tiap kali sepatu dipakai (tanggal, jarak, jenis aktivitas), otomatis terakumulasi ke total jarak sepatu.
- **Log perawatan** — catat kapan terakhir dicuci/diperbaiki.
- **Reminder otomatis** — waktunya cuci, waktunya ganti, atau rotasi pemakaian yang tidak sehat antar sepatu.

## Cara Menjalankan

Yang dibutuhkan hanya Docker dan Docker Compose. Pastikan port 8081, 8082, 5433, dan 5434 tidak sedang dipakai.

Dari folder project:

```bash
docker compose up -d --build
```

Perintah ini menyalakan empat container: `shoe-service` (port 8081), `reminder-service` (port 8082), dan database Postgres masing-masing. Tabel dan data contoh dibuat otomatis. Tunggu sekitar 1 menit sampai kedua aplikasi selesai start, lalu coba:

```bash
curl http://localhost:8081/api/v1/shoe
curl http://localhost:8082/api/v1/reminders
```

Untuk menghentikan:

```bash
docker compose down        # data tetap tersimpan
docker compose down -v     # data ikut dihapus
```
