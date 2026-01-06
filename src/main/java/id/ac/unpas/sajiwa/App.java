package id.ac.unpas.sajiwa;

import id.ac.unpas.sajiwa.database.KoneksiDB; // Import file koneksi buatanmu
import java.sql.SQLException;

public class App {
    public static void main(String[] args) {
        System.out.println("Cek Koneksi Database...");
        
        try {
            // Ini cuma buat ngetes: Panggil fungsi koneksi yang udah kamu bikin tadi
            KoneksiDB.getConnection(); 
            // Kalau sukses, dia bakal nge-print pesan dari dalem KoneksiDB
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}