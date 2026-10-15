-- ============================================================
-- Database  : barbershop_db
-- Program   : Minpro-4-PBO-ManajemenBarbershop
-- Cara pakai: import file ini lewat phpMyAdmin (tab Import)
--             atau jalankan: mysql -u root -p < barbershop_db.sql
-- Data awal (dummy) diisi lewat file ini, bukan lewat kode Java.
-- ============================================================

CREATE DATABASE IF NOT EXISTS barbershop_db
    CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE barbershop_db;

-- Hapus tabel lama (urutan dibalik karena ada foreign key)
DROP TABLE IF EXISTS pelayanan;
DROP TABLE IF EXISTS layanan;
DROP TABLE IF EXISTS barber;
DROP TABLE IF EXISTS pelanggan;

-- ------------------------------------------------------------
-- Tabel pelanggan
-- ------------------------------------------------------------
CREATE TABLE pelanggan (
    id_pelanggan VARCHAR(10) NOT NULL,
    nama         VARCHAR(50) NOT NULL,
    no_hp        VARCHAR(15) NOT NULL,
    jenis        VARCHAR(10) NOT NULL DEFAULT 'REGULER',
    PRIMARY KEY (id_pelanggan),
    CONSTRAINT chk_pelanggan_jenis CHECK (jenis IN ('REGULER', 'MEMBER'))
) ENGINE=InnoDB;

-- ------------------------------------------------------------
-- Tabel barber
-- ------------------------------------------------------------
CREATE TABLE barber (
    id_barber         VARCHAR(10) NOT NULL,
    nama              VARCHAR(50) NOT NULL,
    pengalaman        INT         NOT NULL,
    jenis             VARCHAR(10) NOT NULL DEFAULT 'BIASA',
    status_kehadiran  VARCHAR(20) NOT NULL DEFAULT 'Aktif',
    PRIMARY KEY (id_barber),
    CONSTRAINT chk_barber_jenis CHECK (jenis IN ('BIASA', 'SENIOR')),
    CONSTRAINT chk_barber_pengalaman CHECK (pengalaman BETWEEN 0 AND 50),
    CONSTRAINT chk_barber_kehadiran CHECK (status_kehadiran IN ('Aktif', 'Tidak Tersedia'))
) ENGINE=InnoDB;

-- ------------------------------------------------------------
-- Tabel layanan
-- ------------------------------------------------------------
CREATE TABLE layanan (
    id_layanan    VARCHAR(10) NOT NULL,
    nama_layanan  VARCHAR(50) NOT NULL,
    harga         INT         NOT NULL,
    durasi_menit  INT         NOT NULL,
    PRIMARY KEY (id_layanan)
) ENGINE=InnoDB;

-- ------------------------------------------------------------
-- Tabel pelayanan
-- ------------------------------------------------------------
CREATE TABLE pelayanan (
    id_pelayanan       VARCHAR(10) NOT NULL,
    id_pelanggan       VARCHAR(10) NOT NULL,
    id_barber          VARCHAR(10) NOT NULL,
    id_layanan         VARCHAR(10) NOT NULL,
    nomor_antrean      INT         NOT NULL,
    total_bayar        INT         NOT NULL,
    status_pelayanan   VARCHAR(20) NOT NULL DEFAULT 'Menunggu',
    status_pembayaran  VARCHAR(20) NOT NULL DEFAULT 'Belum Dibayar',
    metode_pembayaran  VARCHAR(10) NOT NULL DEFAULT '-',
    PRIMARY KEY (id_pelayanan),
    CONSTRAINT fk_pelayanan_pelanggan FOREIGN KEY (id_pelanggan) REFERENCES pelanggan (id_pelanggan),
    CONSTRAINT fk_pelayanan_barber    FOREIGN KEY (id_barber)    REFERENCES barber (id_barber),
    CONSTRAINT fk_pelayanan_layanan   FOREIGN KEY (id_layanan)   REFERENCES layanan (id_layanan)
) ENGINE=InnoDB;

-- ============================================================
-- DATA AWAL (DUMMY)
-- ============================================================
INSERT INTO pelanggan (id_pelanggan, nama, no_hp, jenis) VALUES
    ('PLG001', 'Budi Santoso', '081234567890', 'MEMBER'),
    ('PLG002', 'Rina Aulia',   '082345678901', 'REGULER');

INSERT INTO barber (id_barber, nama, pengalaman, jenis, status_kehadiran) VALUES
    ('BRB001', 'Andi Pratama',  10, 'SENIOR', 'Aktif'),
    ('BRB002', 'Joko Santoso',   3, 'BIASA',  'Aktif');

INSERT INTO layanan (id_layanan, nama_layanan, harga, durasi_menit) VALUES
    ('LYN001', 'Potong Rambut',  30000, 30),
    ('LYN002', 'Cukur Jenggot',  15000, 15),
    ('LYN003', 'Cuci Rambut',    20000, 20),
    ('LYN004', 'Hair Styling',   25000, 25),
    ('LYN005', 'Paket Komplit',  60000, 60);

-- PLY001: member + barber senior, potong rambut: (30000 + 10000) - 10% = 36000
-- PLY002: reguler + barber biasa, cukur jenggot: 15000
INSERT INTO pelayanan (id_pelayanan, id_pelanggan, id_barber, id_layanan, nomor_antrean,
                       total_bayar, status_pelayanan, status_pembayaran, metode_pembayaran) VALUES
    ('PLY001', 'PLG001', 'BRB001', 'LYN001', 1, 36000, 'Selesai',  'Lunas',         'QRIS'),
    ('PLY002', 'PLG002', 'BRB002', 'LYN002', 1, 15000, 'Menunggu', 'Belum Dibayar', '-');
