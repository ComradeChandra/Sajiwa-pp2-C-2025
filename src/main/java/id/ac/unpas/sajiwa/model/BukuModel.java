package id.ac.unpas.sajiwa.model;

import id.ac.unpas.sajiwa.database.KoneksiDB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * CLASS MODEL: BUKU (CRUD)
 * ------------------------
 * Kelas ini bertanggung jawab menangani seluruh operasi database (CRUD)
 * untuk tabel 'buku'. Mengimplementasikan pemisahan logika (MVC) agar
 * Controller tidak berisi kode SQL.
 */
public class BukuModel {

    /**
     * Method: READ (Menampilkan Semua Data)
     * Mengambil seluruh baris data dari tabel buku.
     * * Alur Proses:
     * 1. Buka Koneksi ke Database.
     * 2. Eksekusi Query SELECT.
     * 3. Konversi hasil (ResultSet) menjadi List objek Buku.
     */
    public List<Buku> getAllBuku() {
        List<Buku> listBuku = new ArrayList<>();
        String sql = "SELECT * FROM buku"; 

        // Menggunakan try-with-resources untuk manajemen koneksi yang aman
        try (Connection conn = KoneksiDB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Iterasi membaca setiap baris data dari database
            while (rs.next()) {
                Buku b = new Buku();
                b.setIsbn(rs.getString("isbn"));
                b.setJudul(rs.getString("judul"));
                b.setStok(rs.getInt("stok"));
                
                listBuku.add(b);
            }
        } catch (SQLException e) {
            System.err.println("Gagal mengeksekusi query getAllBuku: " + e.getMessage());
        }
        
        return listBuku;
    }
    
    // TODO: Implementasikan method create(), update(), dan delete() di bawah ini.

    /* CATATAN PRIBADI (CHANDRA):
       1. Arsitektur MVC: File ini adalah implementasi tugas Modul 10 untuk memisahkan
          logika database dari View/Controller.
       2. JDBC Standar: Menggunakan Statement dan ResultSet sesuai materi praktikum.
       3. Resource Management: Menggunakan try-with-resources agar koneksi database
          otomatis ditutup (close) setelah query selesai dijalankan.
    */
}