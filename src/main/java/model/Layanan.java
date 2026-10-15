package model;

public class Layanan {
    private final String idLayanan;
    private final String namaLayanan;
    private final int harga;
    private final int durasiMenit;

    public Layanan(String idLayanan, String namaLayanan, int harga, int durasiMenit) {
        this.idLayanan = idLayanan;
        this.namaLayanan = namaLayanan;
        this.harga = harga;
        this.durasiMenit = durasiMenit;
    }

    public String getIdLayanan() { return idLayanan; }
    public String getNamaLayanan() { return namaLayanan; }
    public int getHarga() { return harga; }
    public int getDurasiMenit() { return durasiMenit; }
}