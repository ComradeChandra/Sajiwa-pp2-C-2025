package id.ac.unpas.sajiwa.model;

public class Buku {
    // Atribut sesuai dengan kolom di Database MySQL
    private String isbn;
    private String judul;
    private int stok;

    // Constructor Kosong (Penting untuk inisialisasi awal tanpa data)
    public Buku() {}

    // Constructor Lengkap (Untuk mengisi data secara langsung saat objek dibuat)
    public Buku(String isbn, String judul, int stok) {
        this.isbn = isbn;
        this.judul = judul;
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

}