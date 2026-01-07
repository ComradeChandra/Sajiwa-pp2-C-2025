package id.ac.unpas.sajiwa.model;

public class Buku {
    // Atribut sesuai dengan kolom di Database MySQL
    private String isbn;
    private String judul;
    private int stok;
    private int id_kategori;
    private String nama_kategori;

    // Constructor Kosong (Penting untuk inisialisasi awal tanpa data)
    public Buku() {}

    // Constructor Lengkap (Untuk mengisi data secara langsung saat objek dibuat)
    public Buku(String isbn, String judul, int stok, int id_kategori, String nama_kategori) {
        this.isbn = isbn;
        this.judul = judul;
        this.stok = stok;
        this.id_kategori = id_kategori;
        this.nama_kategori = nama_kategori;
    }

    // --- GETTER & SETTER ---
    // Getter: Mengambil nilai dari atribut (Read)
    // Setter: Mengisi/Mengubah nilai atribut (Write)

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
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getJudul() { return judul; }
    public void setJudul(String judul) { this.judul = judul; }

    public int getStok() { return stok; }
    public void setStok(int stok) { this.stok = stok; }

}