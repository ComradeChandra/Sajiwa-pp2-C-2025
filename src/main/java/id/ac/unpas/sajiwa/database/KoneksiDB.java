package id.ac.unpas.sajiwa.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class KoneksiDB {
    // Menyiapkan variabel untuk menyimpan koneksi
    private static Connection koneksi;

    // Method untuk menghubungkan ke database (Sesuai materi JDBC)
    public static Connection getConnection() throws SQLException {
        // Cek apakah koneksi masih kosong (null)
        if (koneksi == null) {
            try {
                // 1. Register Driver MySQL
                String url = "jdbc:mysql://localhost:3306/sajiwa_perpus_db";
                String user = "root";
                String password = ""; // Laragon default kosong

                // 2. Mendaftarkan driver (wajib di materi lama/baru)
                DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());

                // 3. Membuat koneksi
                koneksi = DriverManager.getConnection(url, user, password);
                System.out.println("Berhasil Konek ke Database!");

            } catch (SQLException e) {
                System.out.println("Gagal Konek: " + e.getMessage());
            }
        }
        return koneksi;
    }
    
    /* CATATAN PRIBADI (CHANDRA):
       1. Database: Aku buat ini connect ke 'sajiwa_perpus_db' yang udah dibuat di PHPMyAdmin.
       2. Logika: Pake pengecekan 'if (koneksi == null)' biar koneksinya cuma dibuat sekali (Singleton).
       3. Driver: Pake 'com.mysql.cj.jdbc.Driver' sesuai library MySQL Connector 8 di pom.xml.
    */
}