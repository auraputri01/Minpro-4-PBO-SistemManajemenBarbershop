package controller;

import dao.BarberDAO;
import dao.BarberDAOImpl;
import dao.LayananDAO;
import dao.LayananDAOImpl;
import dao.PelangganDAO;
import dao.PelangganDAOImpl;
import dao.PelayananDAO;
import dao.PelayananDAOImpl;
import java.util.ArrayList;
import model.Barber;
import model.BarberBiasa;
import model.BarberSenior;
import model.Layanan;
import model.Pelanggan;
import model.PelangganMember;
import model.PelangganReguler;
import model.Pelayanan;
import util.Validator;

/**
 * CONTROLLER: aturan bisnis (validasi, perhitungan, perpindahan status).
 * Data tidak lagi disimpan di ArrayList milik Controller, tetapi dibaca/ditulis
 * ke database lewat DAO. Tidak ada Scanner/System.out di sini.
 */
public class BarbershopController {
    private final PelangganDAO pelangganDAO = new PelangganDAOImpl();
    private final BarberDAO barberDAO = new BarberDAOImpl();
    private final LayananDAO layananDAO = new LayananDAOImpl();
    private final PelayananDAO pelayananDAO = new PelayananDAOImpl();

    // ===== PELANGGAN =====
    public ArrayList<Pelanggan> getDaftarPelanggan() { return pelangganDAO.findAll(); }

    public boolean idPelangganSudahAda(String id) { return cariPelanggan(id) != null; }

    public Pelanggan cariPelanggan(String id) { return pelangganDAO.findById(id); }

    // OVERLOADING (ID otomatis): tanpa parameter member -> Reguler
    public Pelanggan tambahPelanggan(String nama, String noHp) {
        return tambahPelanggan(nama, noHp, false);
    }

    // OVERLOADING (ID otomatis): dengan parameter member
    public Pelanggan tambahPelanggan(String nama, String noHp, boolean member) {
        return tambahPelanggan(pelangganDAO.generateId(), nama, noHp, member);
    }

    // OVERLOADING: ID diinput manual, tanpa parameter member
    public Pelanggan tambahPelanggan(String id, String nama, String noHp) {
        return tambahPelanggan(id, nama, noHp, false);
    }

    // OVERLOADING: ID diinput manual, dengan parameter member
    public Pelanggan tambahPelanggan(String id, String nama, String noHp, boolean member) {
        if (!Validator.isIdValid(id)) throw new IllegalArgumentException("ID pelanggan tidak valid.");
        if (idPelangganSudahAda(id)) throw new IllegalArgumentException("ID pelanggan sudah digunakan.");
        if (!Validator.isNamaValid(nama)) throw new IllegalArgumentException("Nama tidak valid.");
        if (!Validator.isNoHpValid(noHp)) throw new IllegalArgumentException("No HP tidak valid.");

        // POLIMORFISME: variabel bertipe Pelanggan, objek berbeda jenis
        Pelanggan p = member ? new PelangganMember(id, nama, noHp) : new PelangganReguler(id, nama, noHp);
        pelangganDAO.insert(p);
        return p;
    }

    public void ubahPelanggan(Pelanggan p, String nama, String noHp) {
        if (!Validator.isNamaValid(nama)) throw new IllegalArgumentException("Nama tidak valid.");
        if (!Validator.isNoHpValid(noHp)) throw new IllegalArgumentException("No HP tidak valid.");
        p.setNama(nama);
        p.setNoHp(noHp);
        pelangganDAO.update(p);
    }

    public void hapusPelanggan(Pelanggan p) {
        if (pelayananDAO.adaByPelanggan(p.getIdPelanggan())) {
            throw new IllegalStateException("Pelanggan masih memiliki riwayat pelayanan sehingga tidak dapat dihapus.");
        }
        pelangganDAO.delete(p.getIdPelanggan());
    }

    public int getJumlahPelangganMember() {
        int n = 0;
        for (Pelanggan p : pelangganDAO.findAll()) if (p.isMember()) n++;
        return n;
    }

    // ===== BARBER =====
    public ArrayList<Barber> getDaftarBarber() { return barberDAO.findAll(); }

    public boolean idBarberSudahAda(String id) { return cariBarber(id) != null; }

    public Barber cariBarber(String id) { return barberDAO.findById(id); }

    // OVERLOADING (ID otomatis): tanpa parameter senior -> Barber biasa
    public Barber tambahBarber(String nama, int pengalaman) {
        return tambahBarber(nama, pengalaman, false);
    }

    // OVERLOADING (ID otomatis): dengan parameter senior
    public Barber tambahBarber(String nama, int pengalaman, boolean senior) {
        return tambahBarber(barberDAO.generateId(), nama, pengalaman, senior);
    }

    // OVERLOADING: ID diinput manual, tanpa parameter senior
    public Barber tambahBarber(String id, String nama, int pengalaman) {
        return tambahBarber(id, nama, pengalaman, false);
    }

    // OVERLOADING: ID diinput manual, dengan parameter senior
    public Barber tambahBarber(String id, String nama, int pengalaman, boolean senior) {
        if (!Validator.isIdValid(id)) throw new IllegalArgumentException("ID barber tidak valid.");
        if (idBarberSudahAda(id)) throw new IllegalArgumentException("ID barber sudah digunakan.");
        if (!Validator.isNamaValid(nama)) throw new IllegalArgumentException("Nama tidak valid.");
        if (!Validator.isPengalamanValid(pengalaman)) throw new IllegalArgumentException("Pengalaman harus 0-50 tahun.");

        Barber b = senior ? new BarberSenior(id, nama, pengalaman) : new BarberBiasa(id, nama, pengalaman);
        barberDAO.insert(b);
        return b;
    }

    public void ubahBarber(Barber b, String nama, int pengalaman) {
        if (!Validator.isNamaValid(nama)) throw new IllegalArgumentException("Nama tidak valid.");
        if (!Validator.isPengalamanValid(pengalaman)) throw new IllegalArgumentException("Pengalaman harus 0-50 tahun.");
        b.setNama(nama);
        b.setPengalaman(pengalaman);
        barberDAO.update(b);
    }

    public void hapusBarber(Barber b) {
        if (pelayananDAO.adaByBarber(b.getIdBarber())) {
            throw new IllegalStateException("Barber masih memiliki riwayat pelayanan sehingga tidak dapat dihapus.");
        }
        barberDAO.delete(b.getIdBarber());
    }

    public void ubahStatusBarber(Barber b, boolean aktif) {
        Barber terbaru = barberDAO.findById(b.getIdBarber());   // data antrean paling baru dari database
        if (!aktif && terbaru.getJumlahPelangganAktif() > 0) {
            throw new IllegalStateException("Barber masih memiliki antrean aktif, tidak bisa diubah ke tidak tersedia.");
        }
        terbaru.setStatusKehadiran(aktif ? Barber.HADIR_AKTIF : Barber.HADIR_TIDAK_TERSEDIA);
        barberDAO.update(terbaru);
        b.setStatusKehadiran(terbaru.getStatusKehadiran());
    }

    public int getJumlahBarberSenior() {
        int n = 0;
        for (Barber b : barberDAO.findAll()) if (b.isSenior()) n++;
        return n;
    }

    public int getJumlahBarberDenganStatus(String status) {
        int n = 0;
        for (Barber b : barberDAO.findAll()) if (b.getStatusBarber().equals(status)) n++;
        return n;
    }

    // ===== LAYANAN =====
    public ArrayList<Layanan> getDaftarLayanan() { return layananDAO.findAll(); }

    public Layanan cariLayanan(String id) { return layananDAO.findById(id); }

    // ===== PELAYANAN =====
    public ArrayList<Pelayanan> getDaftarPelayanan() { return pelayananDAO.findAll(); }

    public Pelayanan cariPelayanan(String id) { return pelayananDAO.findById(id); }

    public Pelayanan buatPelayanan(Pelanggan pelanggan, Barber barberDipilih, Layanan layanan) {
        Barber barber = barberDAO.findById(barberDipilih.getIdBarber());   // status antrean terbaru
        String status = barber.getStatusBarber();
        if (Barber.TIDAK_TERSEDIA.equals(status)) throw new IllegalStateException("Barber sedang tidak tersedia.");
        if (Barber.PENUH.equals(status)) throw new IllegalStateException("Antrean barber sudah penuh.");

        int subtotal = layanan.getHarga() + barber.getBiayaTambahan();     // polimorfisme Barber
        int total = subtotal - pelanggan.hitungDiskon(subtotal);           // polimorfisme Pelanggan
        int nomorAntrean = pelayananDAO.hitungByBarber(barber.getIdBarber()) + 1;

        Pelayanan pelayanan = new Pelayanan(pelayananDAO.generateId(), pelanggan.getIdPelanggan(),
                barber.getIdBarber(), layanan.getIdLayanan(), nomorAntrean, total);
        pelayananDAO.insert(pelayanan);
        return pelayanan;
    }

    public void mulaiPelayanan(Pelayanan p) {
        if (!Pelayanan.MENUNGGU.equals(p.getStatusPelayanan())) {
            throw new IllegalStateException("Hanya pelayanan berstatus Menunggu yang dapat dimulai.");
        }
        p.setStatusPelayanan(Pelayanan.DIPROSES);
        pelayananDAO.update(p);
    }

    public void selesaikanPelayanan(Pelayanan p) {
        if (!Pelayanan.DIPROSES.equals(p.getStatusPelayanan())) {
            throw new IllegalStateException("Hanya pelayanan berstatus Diproses yang dapat diselesaikan.");
        }
        p.setStatusPelayanan(Pelayanan.SELESAI);
        pelayananDAO.update(p);
    }

    public void bayarPelayanan(Pelayanan p, String metode) {
        if (!Pelayanan.SELESAI.equals(p.getStatusPelayanan())) {
            throw new IllegalStateException("Pelayanan belum selesai.");
        }
        if (p.isLunas()) throw new IllegalStateException("Pelayanan sudah dibayar.");
        p.bayar(metode);
        pelayananDAO.update(p);
    }

    public void batalkanPelayanan(Pelayanan p) {
        if (!p.isAktif()) {
            throw new IllegalStateException("Pelayanan berstatus " + p.getStatusPelayanan() + " tidak dapat dibatalkan.");
        }
        p.setStatusPelayanan(Pelayanan.DIBATALKAN);
        pelayananDAO.update(p);
    }

    // ===== RINGKASAN =====
    public int getJumlahPelayananDenganStatus(String status) {
        int n = 0;
        for (Pelayanan p : pelayananDAO.findAll()) if (p.getStatusPelayanan().equals(status)) n++;
        return n;
    }

    public int getTotalPendapatan() {
        int total = 0;
        for (Pelayanan p : pelayananDAO.findAll()) if (p.isLunas()) total += p.getTotalBayar();
        return total;
    }

    public int getPendapatanBerdasarkanMetode(String metode) {
        int total = 0;
        for (Pelayanan p : pelayananDAO.findAll()) {
            if (p.isLunas() && p.getMetodePembayaran().equals(metode)) total += p.getTotalBayar();
        }
        return total;
    }

    public int getTotalBelumDibayar() {
        int total = 0;
        for (Pelayanan p : pelayananDAO.findAll()) {
            if (Pelayanan.SELESAI.equals(p.getStatusPelayanan()) && !p.isLunas()) total += p.getTotalBayar();
        }
        return total;
    }
}