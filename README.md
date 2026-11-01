# Masteran Burung Premium

Starter project Android Kotlin + Jetpack Compose + Media3.

## Fitur starter
- Antarmuka aplikasi tema alam premium.
- Daftar kategori dan pencarian sederhana.
- Streaming audio melalui Media3.
- MediaSessionService untuk dukungan pemutaran latar belakang.
- Workflow GitHub Actions untuk menghasilkan APK debug.

## Catatan penting sebelum rilis
- URL audio di MainActivity.kt adalah audio contoh teknis, bukan rekaman burung. Ganti dengan URL audio burung yang Anda miliki hak distribusinya.
- Pengunduhan offline, database playlist/favorit, dan pengelolaan katalog server masih perlu diimplementasikan pada tahap berikutnya.
- Untuk Play Store, siapkan AAB release yang ditandatangani, kebijakan privasi, aset listing, deklarasi izin, dan pengujian.
- Background playback memerlukan pengujian pada perangkat nyata dan penanganan izin notifikasi sesuai versi Android.
- Tidak ada fungsi otomatisasi transaksi atau bypass proteksi platform lain.
