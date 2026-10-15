package dao;

import java.util.ArrayList;
import model.Pelanggan;

/** INTERFACE: kontrak akses data pelanggan. */
public interface PelangganDAO {
    ArrayList<Pelanggan> findAll();

    Pelanggan findById(String idPelanggan);

    void insert(Pelanggan pelanggan);

    void update(Pelanggan pelanggan);

    void delete(String idPelanggan);

    String generateId();
}