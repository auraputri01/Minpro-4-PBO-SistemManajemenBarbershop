package model;

public class BarberSenior extends Barber {
    public BarberSenior(String id, String nama, int pengalaman) {
        super(id, nama, pengalaman);
    }

    @Override
    public String getPeran() {
        return "Barber Senior";
    }

    @Override
    public boolean isSenior() {
        return true;
    }

    @Override
    public int getKapasitas() {
        return 7;
    }

    @Override
    public int getBiayaTambahan() {
        return 10000;
    }
}