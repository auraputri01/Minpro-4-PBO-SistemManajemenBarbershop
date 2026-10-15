package dao;

import config.Koneksi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import model.Layanan;

public class LayananDAOImpl extends BaseDAO implements LayananDAO {

    @Override
    public ArrayList<Layanan> findAll() {
        String sql = "SELECT id_layanan, nama_layanan, harga, durasi_menit FROM layanan ORDER BY id_layanan";
        ArrayList<Layanan> hasil = new ArrayList<>();
        try (Connection c = Koneksi.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                hasil.add(petakan(rs));
            }
        } catch (SQLException e) {
            throw galat("Gagal membaca data layanan", e);
        }
        return hasil;
    }

    @Override
    public Layanan findById(String idLayanan) {
        String sql = "SELECT id_layanan, nama_layanan, harga, durasi_menit FROM layanan WHERE id_layanan = ?";
        try (Connection c = Koneksi.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, idLayanan);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? petakan(rs) : null;
            }
        } catch (SQLException e) {
            throw galat("Gagal mencari layanan", e);
        }
    }

    private Layanan petakan(ResultSet rs) throws SQLException {
        return new Layanan(rs.getString("id_layanan"), rs.getString("nama_layanan"),
                rs.getInt("harga"), rs.getInt("durasi_menit"));
    }
}