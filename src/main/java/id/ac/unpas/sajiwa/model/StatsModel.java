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
