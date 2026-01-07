package id.ac.unpas.sajiwa.model;

import id.ac.unpas.sajiwa.database.KoneksiDB; // Recompile
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KategoriBukuModel {
    // Validasi umum
    private void validateKategori(KategoriBuku kategori) {
        if (kategori == null) {
            throw new IllegalArgumentException("Data kategori tidak boleh null");
        }
        if (kategori.getNamaKategori() == null || kategori.getNamaKategori().trim().isEmpty()) {
            throw new IllegalArgumentException("Nama kategori wajib diisi");
        }
    }

    // INSERT kategori
    public void addKategori(KategoriBuku kategori) {
        validateKategori(kategori);

        String sql = "INSERT INTO kategori_buku (nama_kategori) VALUES (?)";

        try (Connection conn = KoneksiDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, kategori.getNamaKategori());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Gagal menambahkan kategori: " + e.getMessage());
        }
    }

    // SELECT semua kategori
    public List<KategoriBuku> getAllKategori() {
        List<KategoriBuku> listKategori = new ArrayList<>();
        String sql = "SELECT * FROM kategori_buku";

        try (Connection conn = KoneksiDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                KategoriBuku kategori = new KategoriBuku();
                kategori.setIdKategori(rs.getInt("id_kategori"));
                kategori.setNamaKategori(rs.getString("nama_kategori"));
                listKategori.add(kategori);
            }

        } catch (SQLException e) {
            System.err.println("Gagal mengambil data kategori: " + e.getMessage());
        }

        return listKategori;
    }

    // UPDATE kategori
    public void updateKategori(KategoriBuku kategori) {
        validateKategori(kategori);

        String sql = "UPDATE kategori_buku SET nama_kategori = ? WHERE id_kategori = ?";

        try (Connection conn = KoneksiDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, kategori.getNamaKategori());
            pstmt.setInt(2, kategori.getIdKategori());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Gagal mengupdate kategori: " + e.getMessage());
        }
    }

    // DELETE kategori
    public void deleteKategori(int idKategori) {
        if (idKategori <= 0) {
            throw new IllegalArgumentException("ID kategori tidak valid");
        }

        String sql = "DELETE FROM kategori_buku WHERE id_kategori = ?";

        try (Connection conn = KoneksiDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idKategori);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Gagal menghapus kategori: " + e.getMessage());
        }
    }
}
