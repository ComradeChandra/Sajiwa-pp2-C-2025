package id.ac.unpas.sajiwa.model;

import java.util.Date;

public class Peminjaman {
    private int id_peminjaman;
    private int id_anggota;
    private String isbn;
    private Date tanggal_pinjam;
    private Date tanggal_kembali;
    private String status;

    public Peminjaman() {
    }

    public Peminjaman(int id_peminjaman, int id_anggota, String isbn, Date tanggal_pinjam, Date tanggal_kembali, String status) {
        this.id_peminjaman = id_peminjaman;
        this.id_anggota = id_anggota;
        this.isbn = isbn;
        this.tanggal_pinjam = tanggal_pinjam;
        this.tanggal_kembali = tanggal_kembali;
        this.status = status;
    }

    // Constructor tanpa idPeminjaman (misal untuk insert baru, auto increment)
    public Peminjaman(int id_anggota, String isbn, Date tanggal_pinjam, Date tanggal_kembali, String status) {
        this.id_anggota = id_anggota;
        this.isbn = isbn;
        this.tanggal_pinjam = tanggal_pinjam;
        this.tanggal_kembali = tanggal_kembali;
        this.status = status;
    }

    // ===================== Getter & Setter =====================

    public int getIdPeminjaman() {
        return id_peminjaman;
    }

    public void setIdPeminjaman(int idPeminjaman) {
        this.id_peminjaman = idPeminjaman;
    }

    public int getIdAnggota() {
        return id_anggota;
    }

    public void setIdAnggota(int idAnggota) {
        this.id_anggota = idAnggota;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Date getTanggalPinjam() {
        return tanggal_pinjam;
    }

    public void setTanggalPinjam(Date tanggalPinjam) {
        this.tanggal_pinjam = tanggalPinjam;
    }

    public Date getTanggalKembali() {
        return tanggal_kembali;
    }

    public void setTanggalKembali(Date tanggalKembali) {
        this.tanggal_kembali     = tanggalKembali;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
