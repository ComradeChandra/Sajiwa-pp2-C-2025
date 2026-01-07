package id.ac.unpas.sajiwa;

import id.ac.unpas.sajiwa.view.LoginView;
import id.ac.unpas.sajiwa.controller.LoginController; // Panggil Controller
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.UIManager;
import javax.swing.SwingUtilities;

public class App {
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            System.err.println("Gagal load skin");
        }
        
        SwingUtilities.invokeLater(() -> {
            // 1. Bikin Wajah (View)
            LoginView view = new LoginView();
            
            // 2. Pasang Otak (Controller) ke Wajah
            new LoginController(view); 
            
            // 3. Tampilkan Wajah
            view.setVisible(true);
        });
    }
}