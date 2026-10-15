package dao;

import config.Koneksi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import model.Pelanggan;
import model.PelangganMember;
import model.PelangganReguler;

public class PelangganDAOImpl extends BaseDAO implements PelangganDAO {

    private static final String KOLOM = "id_pelanggan, nama, no_hp, jenis";

    @Override
    public ArrayList<Pelanggan> findAll() {
        String sql = "SELECT " + KOLOM + " FROM pelanggan ORDER BY id_pelanggan";
        ArrayList<Pelanggan> hasil = new ArrayList<>();
        try (Connection c = Koneksi.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                hasil.add(petakan(rs));
            }
        } catch (SQLException e) {
            throw galat("Gagal membaca data pelanggan", e);
        }
        return hasil;
    }

    @Override
    public Pelanggan findById(String idPelanggan) {
        String sql = "SELECT " + KOLOM + " FROM pelanggan WHERE id_pelanggan = ?";
        try (Connection c = Koneksi.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, idPelanggan);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? petakan(rs) : null;
            }
        } catch (SQLException e) {
            throw galat("Gagal mencari pelanggan", e);
        }
    }

    @Override
    public void insert(Pelanggan pelanggan) {
        String sql = "INSERT INTO pelanggan (" + KOLOM + ") VALUES (?, ?, ?, ?)";
        try (Connection c = Koneksi.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, pelanggan.getIdPelanggan());
            ps.setString(2, pelanggan.getNama());
            ps.setString(3, pelanggan.getNoHp());
            ps.setString(4, (pelanggan instanceof PelangganMember) ? "MEMBER" : "REGULER");
            ps.executeUpdate();
        } catch (SQLException e) {
            throw galat("Gagal menambah pelanggan", e);
        }
    }

    @Override
    public void update(Pelanggan pelanggan) {
        String sql = "UPDATE pelanggan SET nama = ?, no_hp = ? WHERE id_pelanggan = ?";
        try (Connection c = Koneksi.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, pelanggan.getNama());
            ps.setString(2, pelanggan.getNoHp());
            ps.setString(3, pelanggan.getIdPelanggan());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw galat("Gagal mengubah pelanggan", e);
        }
    }

    @Override
    public void delete(String idPelanggan) {
        String sql = "DELETE FROM pelanggan WHERE id_pelanggan = ?";
        try (Connection c = Koneksi.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, idPelanggan);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw galat("Gagal menghapus pelanggan", e);
        }
    }

    @Override
    public String generateId() {
        return idBerikutnya("pelanggan", "id_pelanggan", "PLG");
    }

    private Pelanggan petakan(ResultSet rs) throws SQLException {
        String id    = rs.getString("id_pelanggan");
        String nama  = rs.getString("nama");
        String noHp  = rs.getString("no_hp");
        String jenis = rs.getString("jenis");

        if ("MEMBER".equalsIgnoreCase(jenis)) {
            return new PelangganMember(id, nama, noHp);
        }
        return new PelangganReguler(id, nama, noHp);
    }
}