package id.ac.unpas.sajiwa.model;

import id.ac.unpas.sajiwa.database.KoneksiDB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BukuModel {

    /**
     * Method: READ (Menampilkan Semua Data)
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
    
    /**
     * Method: CREATE (Menambah Data Buku Baru)
     */
    public void addBuku(Buku buku) {
        String sql = "INSERT INTO buku (isbn, judul, stok) VALUES (?, ?, ?)";

        try (Connection conn = KoneksiDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, buku.getIsbn());
            pstmt.setString(2, buku.getJudul());
            pstmt.setInt(3, buku.getStok());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Gagal menambah buku: " + e.getMessage());
        }
    }

    /**
     * Method: UPDATE (Mengubah Data Buku)
     */
    public void updateBuku(Buku buku) {
        String sql = "UPDATE buku SET judul = ?, stok = ? WHERE isbn = ?";

        try (Connection conn = KoneksiDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, buku.getJudul());
            pstmt.setInt(2, buku.getStok());
            pstmt.setString(3, buku.getIsbn());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Gagal mengupdate buku: " + e.getMessage());
        }
    }

    /**
     * Method: DELETE (Menghapus Data Buku)
     */
    public void deleteBuku(String isbn) {
        String sql = "DELETE FROM buku WHERE isbn = ?";

        try (Connection conn = KoneksiDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, isbn);

            int result = pstmt.executeUpdate();

            if (result > 0) {
                System.out.println("Buku berhasil dihapus.");
            } else {
                System.out.println("Gagal menghapus buku. ISBN tidak ditemukan.");
            }

        } catch (SQLException e) {
            System.err.println("Gagal menghapus buku: " + e.getMessage());
        }
    }
}