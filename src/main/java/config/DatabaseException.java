package config;

/** Pembungkus SQLException agar class lain tidak wajib menangani checked exception. */
public class DatabaseException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public DatabaseException(String pesan, Throwable penyebab) {
        super(pesan, penyebab);
    }
}