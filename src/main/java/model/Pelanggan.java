package model;

/** ABSTRACT CLASS turunan Orang; mengimplementasi interface Diskonable. */
public abstract class Pelanggan extends Orang implements Diskonable {
    private String noHp;

    protected Pelanggan(String idPelanggan, String nama, String noHp) {
        super(idPelanggan, nama);
        this.noHp = noHp;
    }

    public String getIdPelanggan() {
        return getId();
    }

    public String getNoHp() {
        return noHp;
    }

    public void setNoHp(String noHp) {
        this.noHp = noHp;
    }

    /** Apakah pelanggan ini member. Default false, di-override oleh PelangganMember. */
    public boolean isMember() {
        return false;
    }

    // OVERRIDING dari Orang
    @Override
    public void tampilkanData() {
        super.tampilkanData();
        cetak("No HP", noHp);
    }
}