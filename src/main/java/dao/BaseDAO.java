package dao;

import config.DatabaseException;
import config.Koneksi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class BaseDAO {

    protected DatabaseException galat(String pesan, SQLException sebab) {
        return new DatabaseException(pesan, sebab);
    }

    /**
     * Membuat ID berikutnya, misalnya prefix "BRB" -> "BRB001", "BRB002", dst.
     * Contoh pemanggilan: idBerikutnya("barber", "id_barber", "BRB")
     */
    protected String idBerikutnya(String tabel, String kolomId, String prefix) {
        String sql = "SELECT MAX(" + kolomId + ") FROM " + tabel
                + " WHERE " + kolomId + " LIKE ?";
        try (Connection c = Koneksi.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, prefix + "%");
            try (ResultSet rs = ps.executeQuery()) {
                int nomor = 0;
                if (rs.next() && rs.getString(1) != null) {
                    nomor = Integer.parseInt(rs.getString(1).substring(prefix.length()));
                }
                return String.format("%s%03d", prefix, nomor + 1);
            }
        } catch (SQLException e) {
            throw galat("Gagal membuat ID " + tabel, e);
        }
    }
}