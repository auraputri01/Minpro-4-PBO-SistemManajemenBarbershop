package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Pengaturan koneksi JDBC ke MySQL/MariaDB.
 * Ubah URL, USER, dan PASSWORD di sini jika berbeda dengan komputer Anda.
 */
public class Koneksi {
    private static final String URL = "jdbc:mysql://localhost:3306/barbershop_db";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private Koneksi() {
    }

    /** Membuka koneksi baru. Pemanggil wajib menutupnya (try-with-resources). */
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            throw new DatabaseException("Gagal terhubung ke database: " + e.getMessage(), e);
        }
    }

    /** Memeriksa apakah database bisa dihubungi. */
    public static void tes() {
        try (Connection c = getConnection()) {
            // koneksi berhasil dibuka lalu langsung ditutup
        } catch (SQLException e) {
            throw new DatabaseException("Gagal terhubung ke database: " + e.getMessage(), e);
        }
    }
}