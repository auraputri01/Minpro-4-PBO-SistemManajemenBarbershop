package model;

public class PelangganMember extends Pelanggan {
    public static final int PERSEN_DISKON = 10;

    public PelangganMember(String id, String nama, String noHp) {
        super(id, nama, noHp);
    }

    @Override
    public String getPeran() {
        return "Member";
    }

    @Override
    public boolean isMember() {
        return true;
    }

    @Override
    public int hitungDiskon(int totalHarga) {
        return totalHarga * PERSEN_DISKON / 100;
    }

    @Override
    public void tampilkanData() {
        super.tampilkanData();
        cetak("Diskon", PERSEN_DISKON + "%");
    }
}