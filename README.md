# **SISTEM MANAJEMEN BARBERSHOP**
# **Mini Project 4 Praktikum Pemrograman Berorientasi Objek (PBO)**

### **Nama: Aura Putri Anandita Syarif NIM: 2509116094 Program Studi: Sistem Informasi (C)**

============================================================================

## **Deskripsi Singkat Program**

Sistem Manajemen Barbershop adalah program Java berbasis console untuk mengelola operasional barbershop, mulai dari data pelanggan, data barber, daftar layanan, antrean, proses pelayanan, sampai pembayaran. Program ini adalah pengembangan dari Mini Project 3. Pada Mini Project 3 seluruh data disimpan sementara di ArrayList sehingga hilang setiap program ditutup. Pada Mini Project 4 data disimpan permanen di database MySQL/MariaDB menggunakan JDBC dengan PreparedStatement dan pola DAO (Data Access Object).

Pelanggan terbagi menjadi Reguler dan Member (diskon 10%), sedangkan barber terbagi menjadi Barber biasa (maksimal 5 antrean, tanpa biaya tambahan) dan Barber Senior (maksimal 7 antrean, biaya tambahan Rp10.000). ID dibuat otomatis dengan format PLG001 untuk pelanggan, BRB001 untuk barber, PLY001 untuk pelayanan, dan LYN001 sampai LYN005 untuk layanan. Pengguna memilih data dari daftar bernomor tanpa mengetik ID. Data awal (dummy) berupa 2 pelanggan, 2 barber, 5 layanan, dan 2 pelayanan diisi lewat file database/barbershop_db.sql, bukan lewat kode Java, sehingga saat menu tampil data langsung muncul.

============================================================================

## **Penjelasan Struktur Package**

Proyek dibagi menjadi pola MVC ditambah lapisan akses data (DAO) dan pengaturan koneksi.

<img width="610" height="760" alt="image" src="https://github.com/user-attachments/assets/9f99b26f-e14b-47cb-9d29-77e0161972e2" />
<img width="325" height="267" alt="image" src="https://github.com/user-attachments/assets/d10cd4d9-6d5f-449b-8c25-ad733e9219b2" />

Package model berisi class data beserta perilakunya dan dua interface. Package View berisi Main sebagai satu-satunya class yang memakai Scanner dan System.out untuk menampilkan menu, serta Test yang memeriksa koneksi dan data awal. Package controller berisi BarbershopController yang menyimpan aturan bisnis seperti validasi, perhitungan total, dan perpindahan status. Controller tidak lagi menyimpan ArrayList sendiri, tetapi memanggil DAO untuk membaca dan menulis data. Package dao berisi interface dan implementasi untuk mengakses tiap tabel, dan hanya di package inilah query SQL ditulis. Package config berisi Koneksi yang mengatur koneksi JDBC dan DatabaseException untuk membungkus error database. Package util berisi Format untuk format rupiah dan Validator untuk aturan validasi input. Folder database berisi file SQL yang menjadi database program.

============================================================================

## **Penjelasan Alur Program**

Saat program dijalankan, Main memeriksa koneksi ke database terlebih dahulu. Jika gagal, program menampilkan pesan dan petunjuk perbaikan. Jika berhasil, menu utama tampil dengan pilihan kelola pelanggan, kelola barber, daftar layanan, pelayanan pelanggan, cek status pelanggan, status barber, ringkasan, dan keluar. Data yang tampil pada menu baca langsung berasal dari database, termasuk data awal dari file SQL.

Setiap operasi mengikuti alur yang sama. Main membaca input pengguna lalu memanggil BarbershopController. Controller memvalidasi dan menerapkan aturan bisnis, kemudian memanggil DAO. DAO menjalankan query ke database dengan PreparedStatement dan mengembalikan objek model ke Controller, yang diteruskan ke Main untuk ditampilkan. Saat menambah pelanggan atau barber, ID dibuat otomatis oleh DAO dari nomor terbesar yang ada di tabel. Untuk mengubah, menghapus, atau memilih data, program menampilkan daftar bernomor lalu pengguna memilih nomornya.

Saat mendaftarkan pelayanan, pengguna memilih pelanggan, barber, dan layanan. Controller menghitung total dengan rumus harga layanan ditambah biaya tambahan barber, lalu dikurangi diskon pelanggan, kemudian menyimpan pelayanan berstatus Menunggu beserta nomor antrean. Status berjalan dari Menunggu ke Diproses, lalu Selesai, dan terakhir dibayar dengan Tunai atau QRIS. Pelayanan yang masih Menunggu atau Diproses dapat dibatalkan. Jumlah antrean aktif barber tidak disimpan sebagai kolom, melainkan dihitung dari tabel pelayanan yang berstatus Menunggu atau Diproses, sehingga status barber (Tersedia, Melayani, Penuh) selalu sesuai isi database. Barber atau pelanggan yang sudah memiliki riwayat pelayanan tidak dapat dihapus agar data pendapatan tetap utuh.

============================================================================

## **Penjelasan Penerapan Encapsulation dan Inheritance**

### ***Encapsulation***
Encapsulation diterapkan dengan menjadikan semua atribut model bersifat private dan hanya dapat diakses lewat getter dan setter. Setter juga memvalidasi nilainya sebelum disimpan. Misalnya setNama di class Orang memeriksa format nama, setPengalaman di class Barber memastikan nilainya 0 sampai 50, dan setStatusKehadiran hanya menerima status yang valid. Status pembayaran pada Pelayanan hanya berubah lewat method bayar. Constructor Orang dibuat protected agar hanya class turunan yang bisa memakainya. Koneksi database juga dienkapsulasi: URL, user, dan password disimpan private di class Koneksi dan semua class lain hanya memanggil getConnection. Query SQL hanya ada di class DAO sehingga Controller dan View tidak mengenal detail database.

### ***Inheritance***
Inheritance diterapkan dengan Orang sebagai class induk. Pelanggan dan Barber adalah turunan Orang sehingga mewarisi id, nama, dan method tampilkanData. PelangganReguler dan PelangganMember adalah turunan Pelanggan, sedangkan BarberBiasa dan BarberSenior adalah turunan Barber. Pada lapisan DAO, setiap class implementasi (PelangganDAOImpl, BarberDAOImpl, LayananDAOImpl, PelayananDAOImpl) mewarisi BaseDAO yang menyimpan fungsi bersama.

============================================================================

## **Penjelasan Penerapan Abstraction dan Polymorphism**

Abstraction diterapkan lewat abstract class dan interface. Orang adalah abstract class dengan abstract method getPeran sehingga setiap turunannya wajib menyebutkan perannya, dan class ini tidak bisa dibuat objeknya langsung. Pelanggan adalah abstract class yang mengimplementasi interface Diskonable sehingga subclass wajib mengisi method hitungDiskon. Barber adalah abstract class yang mengimplementasi interface MemilikiKapasitas sehingga subclass wajib mengisi method getKapasitas dan getBiayaTambahan. Pada lapisan data, BaseDAO adalah abstract class induk semua DAO, dan PelangganDAO, BarberDAO, LayananDAO, serta PelayananDAO adalah interface yang mendefinisikan kontrak akses data. Controller memegang DAO bertipe interface, misalnya PelangganDAO pelangganDAO = new PelangganDAOImpl().

Overriding terlihat pada beberapa method. getPeran di-override oleh PelangganReguler, PelangganMember, BarberBiasa, dan BarberSenior. hitungDiskon menghasilkan 0 untuk Reguler dan 10% untuk Member. getKapasitas menghasilkan 5 untuk Barber biasa dan 7 untuk Barber Senior, sedangkan getBiayaTambahan menghasilkan Rp0 dan Rp10.000. Method tampilkanData di-override pada Pelanggan, PelangganMember, dan Barber, dan semua method DAO di-override oleh class implementasinya. Polimorfisme dinamis terlihat saat DAO membaca database: kolom jenis menentukan objek yang dibuat (PelangganMember atau PelangganReguler, BarberSenior atau BarberBiasa), tetapi semuanya dikembalikan sebagai tipe induk Pelanggan dan Barber. Pada method buatPelayanan, Controller memanggil hitungDiskon dan getBiayaTambahan tanpa percabangan if/else, dan hasilnya mengikuti jenis objek sebenarnya.

Overloading diterapkan pada method tambahPelanggan dan tambahBarber di BarbershopController, masing-masing dengan empat versi: dengan atau tanpa parameter member/senior, dan dengan atau tanpa ID manual. Class Pelayanan memiliki dua constructor, satu untuk pelayanan baru dan satu lagi yang menerima seluruh kolom dari database. Format.rupiah memiliki versi untuk int dan long.

============================================================================

## **Penjelasan Penerapan JDBC**

Program memakai JDBC untuk terhubung ke database MySQL/MariaDB bernama barbershop_db. Alur penerapannya terdiri dari beberapa bagian.

Koneksi diatur di class config/Koneksi.java. Method getConnection memanggil DriverManager.getConnection dengan URL jdbc:mysql://localhost:3306/barbershop_db, user root, dan password kosong (bawaan XAMPP). Setiap operasi membuka koneksi baru dan menutupnya otomatis dengan try-with-resources. Error SQLException dibungkus menjadi DatabaseException agar class lain tidak wajib menangani checked exception.

Semua query memakai PreparedStatement dengan tanda tanya (?) sebagai parameter, lalu nilainya diisi lewat setString dan setInt. Contohnya pada PelangganDAOImpl:

<img width="1138" height="363" alt="image" src="https://github.com/user-attachments/assets/350e68e2-d56c-4545-a154-b823608efc83" />

Cara ini mencegah SQL injection karena input pengguna tidak digabung langsung ke teks query. Operasi baca memakai executeQuery dan ResultSet, lalu setiap baris diubah menjadi objek model oleh method petakan. Operasi tambah, ubah, dan hapus memakai executeUpdate.

Pola DAO memisahkan akses data dari logika program. Setiap tabel memiliki satu interface DAO dan satu class implementasi yang berisi operasi CRUD: findAll dan findById untuk membaca, insert untuk menambah, update untuk mengubah, dan delete untuk menghapus. Tabel pelayanan memiliki tambahan method untuk menghitung nomor antrean dan memeriksa riwayat. Tabel layanan hanya dibaca karena datanya berasal dari file SQL.

Struktur database terdiri dari empat tabel, yaitu pelanggan, barber, layanan, dan pelayanan. Tabel pelayanan memiliki tiga foreign key ke tabel lainnya, dan kolom jenis, pengalaman, serta status kehadiran dilindungi constraint CHECK. Data awal diisi di bagian bawah file database/barbershop_db.sql dengan perintah INSERT, bukan lewat kode Java.




























