package id.ac.unpas.sajiwa.model;

public class Buku { // Recompile trigger
    // Atribut sesuai dengan kolom di Database MySQL
    private String isbn;
    private String judul;
    private int stok;
    
    // [TAMBAHAN LEAD] Atribut ini ditambahkan agar sinkron dengan BukuController
    // Controller membutuhkan setter/getter ini untuk mengisi data default ("-")
    private String pengarang;
    private String penerbit;
    private int tahunTerbit;

    // Atribut Tambahan (Opsional/Relasi)
    private int id_kategori;
    private String nama_kategori;

    // Constructor Kosong (Penting untuk inisialisasi awal tanpa data)
    public Buku() {}

    // Constructor Lengkap (Diupdate biar support semua data)
    public Buku(String isbn, String judul, String pengarang, String penerbit, int tahunTerbit, int stok) {
        this.isbn = isbn;
        this.judul = judul;
        this.pengarang = pengarang;
        this.penerbit = penerbit;
        this.tahunTerbit = tahunTerbit;
        this.stok = stok;
    }

    // --- GETTER & SETTER ---
    // Getter: Mengambil nilai dari atribut (Read)
    // Setter: Mengisi/Mengubah nilai atribut (Write)

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getJudul() { return judul; }
    public void setJudul(String judul) { this.judul = judul; }

    public int getStok() { return stok; }
    public void setStok(int stok) { this.stok = stok; }

    // [TAMBAHAN LEAD] Getter & Setter Baru (Solusi Error Controller)
    public String getPengarang() { return pengarang; }
    public void setPengarang(String pengarang) { this.pengarang = pengarang; }

    public String getPenerbit() { return penerbit; }
    public void setPenerbit(String penerbit) { this.penerbit = penerbit; }

    public int getTahunTerbit() { return tahunTerbit; }
    public void setTahunTerbit(int tahunTerbit) { this.tahunTerbit = tahunTerbit; }

    // Getter & Setter Kategori (Bawaan Murod)
    public int getIdKategori() {
        return id_kategori;
    }

    public void setIdKategori(int idKategori) {
        this.id_kategori = idKategori;
    }

    public String getNamaKategori() {
        return nama_kategori;
    }

    public void setNamaKategori(String namaKategori) {
        this.nama_kategori = namaKategori;
    }
}