package dao;

import java.util.ArrayList;
import model.Pelayanan;

/** INTERFACE: kontrak akses data pelayanan. */
public interface PelayananDAO {
    ArrayList<Pelayanan> findAll();

    Pelayanan findById(String idPelayanan);

    void insert(Pelayanan pelayanan);

    void update(Pelayanan pelayanan);

    String generateId();

    /** Jumlah seluruh pelayanan milik satu barber (dipakai untuk nomor antrean). */
    int hitungByBarber(String idBarber);

    boolean adaByPelanggan(String idPelanggan);

    boolean adaByBarber(String idBarber);
}