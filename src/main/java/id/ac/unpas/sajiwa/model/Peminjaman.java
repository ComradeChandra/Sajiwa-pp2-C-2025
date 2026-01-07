package id.ac.unpas.sajiwa.model;

import java.sql.Date;

public class Peminjaman {
    private int idPeminjaman;
    private int idAnggota;
    private String nimAnggota;
    private String namaAnggota;
    private String isbnBuku;
    private String judulBuku;
    private Date tanggalPinjam;
    private Date tanggalKembali;
    private String status;

    public Peminjaman() {}

    public Peminjaman(int idPeminjaman, int idAnggota, String isbnBuku, Date tanggalPinjam, Date tanggalKembali, String status) {
        this.idPeminjaman = idPeminjaman;
        this.idAnggota = idAnggota;
        this.isbnBuku = isbnBuku;
        this.tanggalPinjam = tanggalPinjam;
        this.tanggalKembali = tanggalKembali;
        this.status = status;
    }

    public int getIdPeminjaman() { return idPeminjaman; }
    public void setIdPeminjaman(int idPeminjaman) { this.idPeminjaman = idPeminjaman; }

    public int getIdAnggota() { return idAnggota; }
    public void setIdAnggota(int idAnggota) { this.idAnggota = idAnggota; }

    public String getNimAnggota() { return nimAnggota; }
    public void setNimAnggota(String nimAnggota) { this.nimAnggota = nimAnggota; }

    public String getNamaAnggota() { return namaAnggota; }
    public void setNamaAnggota(String namaAnggota) { this.namaAnggota = namaAnggota; }

    public String getIsbnBuku() { return isbnBuku; }
    public void setIsbnBuku(String isbnBuku) { this.isbnBuku = isbnBuku; }

    public String getJudulBuku() { return judulBuku; }
    public void setJudulBuku(String judulBuku) { this.judulBuku = judulBuku; }

    public Date getTanggalPinjam() { return tanggalPinjam; }
    public void setTanggalPinjam(Date tanggalPinjam) { this.tanggalPinjam = tanggalPinjam; }

    public Date getTanggalKembali() { return tanggalKembali; }
    public void setTanggalKembali(Date tanggalKembali) { this.tanggalKembali = tanggalKembali; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    /* CATATAN PRIBADI (CHANDRA):
       1. POJO (Plain Old Java Object): Class sederhana untuk menampung data Peminjaman.
       2. Encapsulation: Semua atribut private dan diakses lewat Getter/Setter.
       3. Extra Fields: Ada atribut tambahan (nimAnggota, namaAnggota, judulBuku) yang tidak ada di tabel database fisik,
          tapi berguna untuk menampung hasil JOIN query supaya bisa ditampilkan di tabel history tanpa query ulang.
    */
}
