package id.ac.unpas.sajiwa.model;

import id.ac.unpas.sajiwa.database.KoneksiDB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PeminjamanModel {

    public PeminjamanModel() {
        createTableIfNotExists();
    }

    private void createTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS peminjaman (" +
                "id_peminjaman INT AUTO_INCREMENT PRIMARY KEY, " +
                "id_anggota INT NOT NULL, " +
                "isbn_buku VARCHAR(20) NOT NULL, " +
                "tanggal_pinjam DATE, " +
                "tanggal_kembali DATE, " +
                "status VARCHAR(20) DEFAULT 'Dipinjam', " +
                "FOREIGN KEY (id_anggota) REFERENCES anggota(id_anggota) ON DELETE CASCADE, " +
                "FOREIGN KEY (isbn_buku) REFERENCES buku(isbn) ON DELETE CASCADE" +
                ")";
        
        try (Connection conn = KoneksiDB.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("Gagal membuat tabel peminjaman: " + e.getMessage());
        }
    }

    public List<Peminjaman> getAllPeminjaman() {
        List<Peminjaman> list = new ArrayList<>();
        String sql = "SELECT p.*, a.nim, a.nama_mahasiswa, b.judul " +
                     "FROM peminjaman p " +
                     "JOIN anggota a ON p.id_anggota = a.id_anggota " +
                     "JOIN buku b ON p.isbn_buku = b.isbn " +
                     "ORDER BY p.tanggal_pinjam DESC";

        try (Connection conn = KoneksiDB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Peminjaman p = new Peminjaman();
                p.setIdPeminjaman(rs.getInt("id_peminjaman"));
                p.setIdAnggota(rs.getInt("id_anggota"));
                p.setIsbnBuku(rs.getString("isbn_buku"));
                p.setTanggalPinjam(rs.getDate("tanggal_pinjam"));
                p.setTanggalKembali(rs.getDate("tanggal_kembali"));
                p.setStatus(rs.getString("status"));
                // Extra info
                p.setNimAnggota(rs.getString("nim"));
                p.setNamaAnggota(rs.getString("nama_mahasiswa"));
                p.setJudulBuku(rs.getString("judul"));
                
                list.add(p);
            }
        } catch (SQLException e) {
            System.err.println("Error getAllPeminjaman: " + e.getMessage());
        }
        return list;
    }

    public void addPeminjaman(Peminjaman p) {
        String sql = "INSERT INTO peminjaman (id_anggota, isbn_buku, tanggal_pinjam, status) VALUES (?, ?, ?, 'Dipinjam')";
        
        try (Connection conn = KoneksiDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, p.getIdAnggota());
            pstmt.setString(2, p.getIsbnBuku());
            pstmt.setDate(3, p.getTanggalPinjam());
            
            pstmt.executeUpdate();
            
            // Kurangi Stok Buku (Opsional, tapi bagus)
             kurangiStok(p.getIsbnBuku());

        } catch (SQLException e) {
            System.err.println("Error addPeminjaman: " + e.getMessage());
        }
    }

    public void kembalikanBuku(int idPeminjaman, Date tanggalKembali, String isbn) {
        String sql = "UPDATE peminjaman SET tanggal_kembali = ?, status = 'Dikembalikan' WHERE id_peminjaman = ?";
        
        try (Connection conn = KoneksiDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDate(1, tanggalKembali);
            pstmt.setInt(2, idPeminjaman);
            
            pstmt.executeUpdate();
            
            // Tambah Stok Buku
            tambahStok(isbn);
            
        } catch (SQLException e) {
            System.err.println("Error kembalikanBuku: " + e.getMessage());
        }
    }
    
    private void kurangiStok(String isbn) {
        try (Connection conn = KoneksiDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("UPDATE buku SET stok = stok - 1 WHERE isbn = ?")) {
            pstmt.setString(1, isbn);
            pstmt.executeUpdate();
        } catch(SQLException e) {}
    }

    private void tambahStok(String isbn) {
        try (Connection conn = KoneksiDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("UPDATE buku SET stok = stok + 1 WHERE isbn = ?")) {
            pstmt.setString(1, isbn);
            pstmt.executeUpdate();
        } catch(SQLException e) {}
    }

    /* CATATAN PRIBADI (CHANDRA):
       1. New Model: Representasi tabel "peminjaman" di database.
       2. Fitur Spesial: Auto Create Table (CREATE TABLE IF NOT EXISTS) biar gak perlu import SQL manual.
       3. Relasi: Join 3 tabel sekaligus (peminjaman - anggota - buku) biar data yang tampil di tabel lengkap (ada nama & judul).
       4. Trigger Logic: Di sini ada logika "Kurangi Stok" saat pinjam & "Tambah Stok" saat kembali secara otomatis.
    */
}
