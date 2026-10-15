package model;

import util.Validator;

/**
 * SUPER-CLASS (abstract) untuk semua orang di dalam sistem barbershop.
 * Sub-class: Pelanggan dan Barber.
 *
 * Access modifier yang dipakai:
 *  - private   : atribut (id, nama) hanya bisa diakses lewat getter/setter
 *  - protected : constructor dan method cetak() hanya untuk class turunan
 *  - public    : getter, setter, dan method yang boleh dipanggil dari luar
 *
 * @author Aura
 */
public abstract class Orang {
    private final String id;
    private String nama;

    protected Orang(String id, String nama) {
        if (!Validator.isIdValid(id)) {
            throw new IllegalArgumentException("ID harus 3-10 karakter huruf/angka tanpa spasi.");
        }
        this.id = id;
        setNama(nama);
    }

    // ---------- Getter ----------
    public String getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    // ---------- Setter (dengan validasi) ----------
    public final void setNama(String nama) {
        if (!Validator.isNamaValid(nama)) {
            throw new IllegalArgumentException("Nama harus 2-50 karakter, diawali huruf, dan tidak boleh berisi angka.");
        }
        this.nama = nama.trim();
    }

    /** ABSTRACT: setiap sub-class wajib menyebutkan perannya sendiri. */
    public abstract String getPeran();

    /** Bagian tampilan yang sama untuk semua orang; sub-class menambahkan detailnya. */
    public void tampilkanData() {
        cetak("ID", id);
        cetak("Nama", nama);
        cetak("Jenis", getPeran());
    }

    protected void cetak(String label, Object nilai) {
        System.out.printf("%-20s: %s%n", label, nilai);
    }
}