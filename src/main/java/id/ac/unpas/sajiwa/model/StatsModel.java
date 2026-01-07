package id.ac.unpas.sajiwa.model;

import id.ac.unpas.sajiwa.database.KoneksiDB;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Model khusus untuk mengambil data statistik Dashboard
 */
public class StatsModel {

    public int getCount(String tableName) {
        int count = 0;
        String sql = "SELECT COUNT(*) AS total FROM " + tableName;
        
        try (Connection conn = KoneksiDB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                count = rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    public int getCountPeminjamanAktif() {
        int count = 0;
        String sql = "SELECT COUNT(*) AS total FROM peminjaman WHERE status = 'Dipinjam'";
        
        try (Connection conn = KoneksiDB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                count = rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }
}

/*
 * ==================================================================================
 * CATATAN PRIBADI (CHANDRA)
 * ==================================================================================
 * 1. Kegunaan Model Ini:
 *    - Model ini khusus dibuat untuk kebutuhan Dashboard.
 *    - Isinya cuma query agregasi (COUNT) sederhana, bukan CRUD lengkap.
 * 
 * 2. Kenapa dipisah dari Model lain?
 *    - Biar ringan. Kalau kita panggil semua data (SELECT *) cuma buat ngitung jumlah,
 *      itu boros memori.
 *    - Jadi mending bikin query khusus "SELECT COUNT(*)" biar database yang kerja berat,
 *      Bukan aplikasi Java-nya.
 * 
 * 3. Query Peminjaman Aktif:
 *    - Perhatikan query 'WHERE status = 'Dipinjam''.
 *    - Ini penting biar admin tau ada berapa buku yang lagi ada di luar perpustakaan
 *      detik ini juga.
 * ==================================================================================
 */
