package model;

import util.Format;
import util.Validator;

/**
 * ABSTRACT CLASS turunan Orang; mengimplementasi interface MemilikiKapasitas.
 * Subclass: BarberBiasa dan BarberSenior.
 */
public abstract class Barber extends Orang implements MemilikiKapasitas {

    public static final String HADIR_AKTIF = "Aktif";
    public static final String HADIR_TIDAK_TERSEDIA = "Tidak Tersedia";

    public static final String TERSEDIA = "Tersedia";
    public static final String MELAYANI = "Melayani";
    public static final String PENUH = "Penuh";
    public static final String TIDAK_TERSEDIA = "Tidak Tersedia";

    private int pengalaman;
    private int jumlahPelangganAktif;
    private String statusKehadiran;

    protected Barber(String idBarber, String namaBarber, int pengalaman) {
        super(idBarber, namaBarber);
        setPengalaman(pengalaman);
        this.jumlahPelangganAktif = 0;
        this.statusKehadiran = HADIR_AKTIF;
    }

    // ---------- Getter ----------
    public String getIdBarber() {
        return getId();
    }

    public String getNamaBarber() {
        return getNama();
    }

    public int getPengalaman() {
        return pengalaman;
    }

    public int getJumlahPelangganAktif() {
        return jumlahPelangganAktif;
    }

    public String getStatusKehadiran() {
        return statusKehadiran;
    }

    // ---------- Setter (dengan validasi) ----------
    public void setNamaBarber(String namaBarber) {
        setNama(namaBarber);
    }

    public final void setPengalaman(int pengalaman) {
        if (!Validator.isPengalamanValid(pengalaman)) {
            throw new IllegalArgumentException("Pengalaman harus antara 0 sampai 50 tahun.");
        }
        this.pengalaman = pengalaman;
    }

    /**
     * Dipakai DAO saat memuat data: jumlah antrean aktif dihitung dari tabel
     * pelayanan.
     */
    public void setJumlahPelangganAktif(int jumlah) {
        if (jumlah < 0) {
            throw new IllegalArgumentException("Jumlah pelanggan aktif tidak boleh negatif.");
        }
        this.jumlahPelangganAktif = jumlah;
    }

    public void setStatusKehadiran(String statusKehadiran) {
        if (!HADIR_AKTIF.equals(statusKehadiran) && !HADIR_TIDAK_TERSEDIA.equals(statusKehadiran)) {
            throw new IllegalArgumentException("Status kehadiran tidak valid.");
        }
        this.statusKehadiran = statusKehadiran;
    }

    // ---------- Perilaku ----------
    public void tambahPelanggan() {
        if (jumlahPelangganAktif >= getKapasitas()) {
            throw new IllegalStateException("Barber sudah penuh.");
        }
        jumlahPelangganAktif++;
    }

    public void kurangiPelanggan() {
        if (jumlahPelangganAktif > 0) {
            jumlahPelangganAktif--;
        }
    }

    /**
     * Status dihitung otomatis dari kehadiran dan jumlah antrean.
     */
    public String getStatusBarber() {
        if (HADIR_TIDAK_TERSEDIA.equals(statusKehadiran)) {
            return TIDAK_TERSEDIA;
        }
        if (jumlahPelangganAktif == 0) {
            return TERSEDIA;
        }
        if (jumlahPelangganAktif < getKapasitas()) {
            return MELAYANI;
        }
        return PENUH;
    }

    public boolean isSenior() {
        return false;
    }

    @Override
    public void tampilkanData() {
        super.tampilkanData();
        cetak("Pengalaman", pengalaman + " tahun");
        cetak("Pelanggan Dilayani", jumlahPelangganAktif + "/" + getKapasitas());
        cetak("Biaya Tambahan", Format.rupiah(getBiayaTambahan()));
        cetak("Kehadiran", statusKehadiran);
        cetak("Status Barber", getStatusBarber());
    }
}
