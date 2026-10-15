package dao;

import config.Koneksi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import model.Pelayanan;

public class PelayananDAOImpl extends BaseDAO implements PelayananDAO {

    private static final String KOLOM =
            "id_pelayanan, id_pelanggan, id_barber, id_layanan, nomor_antrean, total_bayar, "
            + "status_pelayanan, status_pembayaran, metode_pembayaran";

    @Override
    public ArrayList<Pelayanan> findAll() {
        String sql = "SELECT " + KOLOM + " FROM pelayanan ORDER BY id_pelayanan";
        ArrayList<Pelayanan> hasil = new ArrayList<>();
        try (Connection c = Koneksi.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                hasil.add(petakan(rs));
            }
        } catch (SQLException e) {
            throw galat("Gagal membaca data pelayanan", e);
        }
        return hasil;
    }

    @Override
    public Pelayanan findById(String idPelayanan) {
        String sql = "SELECT " + KOLOM + " FROM pelayanan WHERE id_pelayanan = ?";
        try (Connection c = Koneksi.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, idPelayanan);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? petakan(rs) : null;
            }
        } catch (SQLException e) {
            throw galat("Gagal mencari pelayanan", e);
        }
    }

    @Override
    public void insert(Pelayanan p) {
        String sql = "INSERT INTO pelayanan (" + KOLOM + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection c = Koneksi.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, p.getIdPelayanan());
            ps.setString(2, p.getIdPelanggan());
            ps.setString(3, p.getIdBarber());
            ps.setString(4, p.getIdLayanan());
            ps.setInt(5, p.getNomorAntrean());
            ps.setInt(6, p.getTotalBayar());
            ps.setString(7, p.getStatusPelayanan());
            ps.setString(8, p.getStatusPembayaran());
            ps.setString(9, p.getMetodePembayaran());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw galat("Gagal menambah pelayanan", e);
        }
    }

    @Override
    public void update(Pelayanan p) {
        String sql = "UPDATE pelayanan SET status_pelayanan = ?, status_pembayaran = ?, "
                + "metode_pembayaran = ? WHERE id_pelayanan = ?";
        try (Connection c = Koneksi.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, p.getStatusPelayanan());
            ps.setString(2, p.getStatusPembayaran());
            ps.setString(3, p.getMetodePembayaran());
            ps.setString(4, p.getIdPelayanan());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw galat("Gagal mengubah pelayanan", e);
        }
    }

    @Override
    public String generateId() {
        return idBerikutnya("pelayanan", "id_pelayanan", "PLY");
    }

    @Override
    public int hitungByBarber(String idBarber) {
        return hitung("SELECT COUNT(*) FROM pelayanan WHERE id_barber = ?", idBarber);
    }

    @Override
    public boolean adaByPelanggan(String idPelanggan) {
        return hitung("SELECT COUNT(*) FROM pelayanan WHERE id_pelanggan = ?", idPelanggan) > 0;
    }

    @Override
    public boolean adaByBarber(String idBarber) {
        return hitungByBarber(idBarber) > 0;
    }

    private int hitung(String sql, String parameter) {
        try (Connection c = Koneksi.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, parameter);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw galat("Gagal menghitung data pelayanan", e);
        }
    }

    private Pelayanan petakan(ResultSet rs) throws SQLException {
        return new Pelayanan(
                rs.getString("id_pelayanan"), rs.getString("id_pelanggan"),
                rs.getString("id_barber"), rs.getString("id_layanan"),
                rs.getInt("nomor_antrean"), rs.getInt("total_bayar"),
                rs.getString("status_pelayanan"), rs.getString("status_pembayaran"),
                rs.getString("metode_pembayaran"));
    }
}