package id.ac.unpas.sajiwa.model;

public class KategoriBuku { // Recompile

    private int idKategori;
    private String namaKategori;

    // Constructor tanpa parameter
    public KategoriBuku() {
    }

    // Constructor dengan parameter
    public KategoriBuku(int idKategori, String namaKategori) {
        this.idKategori = idKategori;
        this.namaKategori = namaKategori;
    }

    @Override
    public String toString() {
        return namaKategori;
    }

    // Getter dan Setter
    public int getIdKategori() {
        return idKategori;
    }

    public void setIdKategori(int idKategori) {
        this.idKategori = idKategori;
    }

    public String getNamaKategori() {
        return namaKategori;
    }

    public void setNamaKategori(String namaKategori) {
        this.namaKategori = namaKategori;
    }
}

