package id.ac.unpas.sajiwa.model;

import id.ac.unpas.sajiwa.database.KoneksiDB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AnggotaModel {

    // READ / MENAMPILKAN DATA
    public List<Anggota> getAllAnggota() {
        List<Anggota> listAnggota = new ArrayList<>();
        String sql = "SELECT * FROM anggota";

        try (Connection conn = KoneksiDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Anggota anggota = new Anggota(
                        rs.getInt("id_anggota"),
                        rs.getString("nim"),
                        rs.getString("nama_mahasiswa"),
                        rs.getString("program_studi"),
                        rs.getString("status_anggota")
                );
                listAnggota.add(anggota);
            }

        } catch (SQLException e) {
            System.err.println("Gagal mengambil data anggota: " + e.getMessage());
        }

        return listAnggota;
    }

    // CREATE / TAMBAH DATA
    public void addAnggota(Anggota anggota) {
        String sql = "INSERT INTO anggota (nim, nama_mahasiswa, program_studi, status_anggota) "
                + "VALUES (?, ?, ?, ?)";

        if (anggota.getNim() == null || anggota.getNim().trim().isEmpty()) {
            System.out.println("❌ NIM tidak boleh kosong!");
            return; // HENTIKAN proses insert
        }

        try (Connection conn = KoneksiDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, anggota.getNim());
            pstmt.setString(2, anggota.getNamaMahasiswa());
            pstmt.setString(3, anggota.getProgramStudi());
            pstmt.setString(4, anggota.getStatusAnggota());

            int hasil = pstmt.executeUpdate();
            if (hasil > 0) {
                System.out.println("Anggota berhasil ditambahkan");
            } else {
                System.out.println("Anggota gagal ditambahkan");
            }

        } catch (SQLException e) {
            System.err.println("Gagal menambahkan anggota: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // UPDATE / UBAH DATA
    public void updateAnggota(Anggota anggota) {
        String sql = "UPDATE anggota SET nim = ?, nama_mahasiswa = ?, "
                + "program_studi = ?, status_anggota = ? WHERE id_anggota = ?";

        try (Connection conn = KoneksiDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, anggota.getNim());
            pstmt.setString(2, anggota.getNamaMahasiswa());
            pstmt.setString(3, anggota.getProgramStudi());
            pstmt.setString(4, anggota.getStatusAnggota());
            pstmt.setInt(5, anggota.getIdAnggota());

            int hasil = pstmt.executeUpdate();
            if (hasil > 0) {
                System.out.println("Anggota berhasil diperbarui");
            } else {
                System.out.println("Anggota gagal diperbarui");
            }

        } catch (SQLException e) {
            System.err.println("Gagal mengubah data anggota: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // DELETE / HAPUS DATA
    public void deleteAnggota(int id_anggota) {
        String sql = "DELETE FROM anggota WHERE id_anggota = ?";

        try (Connection conn = KoneksiDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id_anggota);
            int hasil = pstmt.executeUpdate();

            if (hasil > 0) {
                System.out.println("Anggota berhasil dihapus");
            } else {
                System.out.println("Anggota gagal dihapus");
            }
        } catch (SQLException e) {
            System.err.println("Gagal menghapus anggota: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
