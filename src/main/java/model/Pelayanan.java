package model;

import util.Format;

/**
 * Catatan satu kali pelayanan (antrean, proses, dan pembayaran).
 *
 * @author Aura
 */
public class Pelayanan {
    public static final String MENUNGGU = "Menunggu";
    public static final String DIPROSES = "Diproses";
    public static final String SELESAI = "Selesai";
    public static final String DIBATALKAN = "Dibatalkan";

    public static final String TUNAI = "Tunai";
    public static final String QRIS = "QRIS";

    public static final String BELUM_DIBAYAR = "Belum Dibayar";
    public static final String LUNAS = "Lunas";

    private final String idPelayanan;
    private final String idPelanggan;
    private final String idBarber;
    private final String idLayanan;
    private final int nomorAntrean;
    private final int totalBayar;
    private String statusPelayanan = MENUNGGU;
    private String statusPembayaran = BELUM_DIBAYAR;
    private String metodePembayaran = "-";

    public Pelayanan(String idPelayanan, String idPelanggan, String idBarber,
                     String idLayanan, int nomorAntrean, int totalBayar) {
        this.idPelayanan = idPelayanan;
        this.idPelanggan = idPelanggan;
        this.idBarber = idBarber;
        this.idLayanan = idLayanan;
        this.nomorAntrean = nomorAntrean;
        this.totalBayar = totalBayar;
    }

        // OVERLOADING constructor: dipakai DAO untuk membentuk objek dari baris database
    public Pelayanan(String idPelayanan, String idPelanggan, String idBarber, String idLayanan,
                     int nomorAntrean, int totalBayar, String statusPelayanan,
                     String statusPembayaran, String metodePembayaran) {
        this(idPelayanan, idPelanggan, idBarber, idLayanan, nomorAntrean, totalBayar);
        this.statusPelayanan = statusPelayanan;
        this.statusPembayaran = statusPembayaran;
        this.metodePembayaran = metodePembayaran;
    }
    
    public String getIdPelayanan() { return idPelayanan; }
    public String getIdPelanggan() { return idPelanggan; }
    public String getIdBarber() { return idBarber; }
    public String getIdLayanan() { return idLayanan; }
    public int getNomorAntrean() { return nomorAntrean; }
    public int getTotalBayar() { return totalBayar; }
    public String getStatusPelayanan() { return statusPelayanan; }
    public String getStatusPembayaran() { return statusPembayaran; }
    public String getMetodePembayaran() { return metodePembayaran; }

    public void setStatusPelayanan(String statusPelayanan) {
        this.statusPelayanan = statusPelayanan;
    }

    public void bayar(String metode) {
        this.metodePembayaran = metode;
        this.statusPembayaran = LUNAS;
    }

    public boolean isLunas() {
        return LUNAS.equals(statusPembayaran);
    }

    /** Masih menempati antrean barber (Menunggu / Diproses). */
    public boolean isAktif() {
        return MENUNGGU.equals(statusPelayanan) || DIPROSES.equals(statusPelayanan);
    }
}
