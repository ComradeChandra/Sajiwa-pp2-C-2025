package id.ac.unpas.sajiwa;

// Import MainFrame yang ada di folder view
import id.ac.unpas.sajiwa.view.MainFrame;
// Import FlatLaf biar ganteng
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.UIManager;
import javax.swing.SwingUtilities;

public class App {
    
    // INI ADALAH PINTU MASUK UTAMA APLIKASI
    public static void main(String[] args) {
        
        // 1. Dandanin dulu pake FlatLaf
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            System.err.println("Gagal load skin");
        }
        
        // 2. Panggil MainFrame (Si Sidebar tadi)
        SwingUtilities.invokeLater(() -> {
            MainFrame aplikasi = new MainFrame();
            aplikasi.setVisible(true); // TAMPILKAN!
        });
    }
}