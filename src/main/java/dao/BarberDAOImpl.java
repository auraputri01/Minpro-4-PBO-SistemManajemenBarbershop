package dao;

import config.Koneksi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import model.Barber;
import model.BarberBiasa;
import model.BarberSenior;
import model.Pelayanan;

public class BarberDAOImpl extends BaseDAO implements BarberDAO {
    private static final String JENIS_SENIOR = "SENIOR";
    private static final String JENIS_BIASA = "BIASA";

    // Jumlah antrean aktif tidak disimpan di tabel barber, tetapi dihitung dari tabel pelayanan.
    private static final String SELECT_BARBER =
            "SELECT b.id_barber, b.nama, b.pengalaman, b.jenis, b.status_kehadiran, "
            + "(SELECT COUNT(*) FROM pelayanan p WHERE p.id_barber = b.id_barber "
            + " AND p.status_pelayanan IN (?, ?)) AS jumlah_aktif "
            + "FROM barber b ";

    @Override
    public ArrayList<Barber> findAll() {
        String sql = SELECT_BARBER + "ORDER BY b.id_barber";
        ArrayList<Barber> hasil = new ArrayList<>();
        try (Connection c = Koneksi.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, Pelayanan.MENUNGGU);
            ps.setString(2, Pelayanan.DIPROSES);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    hasil.add(petakan(rs));
                }
            }
        } catch (SQLException e) {
            throw galat("Gagal membaca data barber", e);
        }
        return hasil;
    }

    @Override
    public Barber findById(String idBarber) {
        String sql = SELECT_BARBER + "WHERE b.id_barber = ?";
        try (Connection c = Koneksi.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, Pelayanan.MENUNGGU);
            ps.setString(2, Pelayanan.DIPROSES);
            ps.setString(3, idBarber);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? petakan(rs) : null;
            }
        } catch (SQLException e) {
            throw galat("Gagal mencari barber", e);
        }
    }

    @Override
    public void insert(Barber b) {
        String sql = "INSERT INTO barber (id_barber, nama, pengalaman, jenis, status_kehadiran) "
                + "VALUES (?, ?, ?, ?, ?)";
        try (Connection c = Koneksi.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, b.getIdBarber());
            ps.setString(2, b.getNamaBarber());
            ps.setInt(3, b.getPengalaman());
            ps.setString(4, b.isSenior() ? JENIS_SENIOR : JENIS_BIASA);
            ps.setString(5, b.getStatusKehadiran());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw galat("Gagal menyimpan barber", e);
        }
    }

    @Override
    public void update(Barber b) {
        String sql = "UPDATE barber SET nama = ?, pengalaman = ?, status_kehadiran = ? WHERE id_barber = ?";
        try (Connection c = Koneksi.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, b.getNamaBarber());
            ps.setInt(2, b.getPengalaman());
            ps.setString(3, b.getStatusKehadiran());
            ps.setString(4, b.getIdBarber());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw galat("Gagal mengubah barber", e);
        }
    }

    @Override
    public void delete(String idBarber) {
        String sql = "DELETE FROM barber WHERE id_barber = ?";
        try (Connection c = Koneksi.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, idBarber);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw galat("Gagal menghapus barber", e);
        }
    }

    @Override
    public String generateId() {
        return idBerikutnya("barber", "id_barber", "BRB");
    }

    private Barber petakan(ResultSet rs) throws SQLException {
        String id = rs.getString("id_barber");
        String nama = rs.getString("nama");
        int pengalaman = rs.getInt("pengalaman");
        Barber barber = JENIS_SENIOR.equals(rs.getString("jenis"))
                ? new BarberSenior(id, nama, pengalaman)
                : new BarberBiasa(id, nama, pengalaman);
        barber.setStatusKehadiran(rs.getString("status_kehadiran"));
        barber.setJumlahPelangganAktif(rs.getInt("jumlah_aktif"));
        return barber;
    }
}