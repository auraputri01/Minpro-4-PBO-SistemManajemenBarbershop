package dao;

import java.util.ArrayList;
import model.Layanan;

/** INTERFACE: kontrak akses data layanan (hanya baca; data awal berasal dari file .sql). */
public interface LayananDAO {
    ArrayList<Layanan> findAll();

    Layanan findById(String idLayanan);
}