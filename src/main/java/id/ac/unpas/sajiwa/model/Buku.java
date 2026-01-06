package id.ac.unpas.sajiwa.model;

/**
 * CLASS ENTITY: BUKU
 * -------------------
 * Fungsinya hanya sebagai objek pembungkus (wrapper) data.
 * Data dari database akan dimasukkan ke sini sebelum dikirim ke Controller/View.
 */
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
    
    /* CATATAN PRIBADI (CHANDRA):
    1. Konsep Entity: File ini merepresentasikan satu baris data di tabel 'buku'.
    2. Kesepakatan Tim: Nama variabel (isbn, judul, stok) WAJIB sama persis 
       atau mendekati nama kolom di database biar kita nggak pusing mapping-nya.
    3. Tugas Model (Bagian 1): Ini adalah bagian Model yang Murod sebut "Atribut". 
       Benar, tapi ini baru separuh dari tugas Model.
    */
}