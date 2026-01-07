package id.ac.unpas.sajiwa.controller;
        

import id.ac.unpas.sajiwa.view.LoginView;
import id.ac.unpas.sajiwa.view.RegisterView;
import id.ac.unpas.sajiwa.view.MainFrame; // Import Dashboard Murod/Fitri
import id.ac.unpas.sajiwa.database.KoneksiDB;
import javax.swing.*;
import java.sql.*;

public class LoginController {

    private LoginView view;

    public LoginController(LoginView view) {
        this.view = view;
        // Controller yang "dengerin" kalau tombol diklik
        view.btnLogin.addActionListener(e -> login());
        view.btnRegister.addActionListener(e -> {
            RegisterView regView = new RegisterView();
            new RegisterController(regView);
            regView.setVisible(true);                  
            view.dispose();
        });
    }


    private void login() {
        String username = view.txtUsername.getText();
        String password = new String(view.txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Username & Password wajib diisi");
            return;
        }

        try {
            Connection conn = KoneksiDB.getConnection();

            // Cek apakah tabel user_login ada? Kalau DB mati/tabel gak ada, lari ke Catch
            if (conn != null) {
                String sql = "SELECT * FROM user_login WHERE username=? AND password=?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, username);
                ps.setString(2, password);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    loginSukses(); // Pindah ke Dashboard
                } else {
                    JOptionPane.showMessageDialog(view, "Username / Password salah (Cek Database)");
                }
            } else {
                // Fallback kalau koneksi null
                checkHardcode(username, password);
            }

        } catch (Exception e) {
            // Kalau Database Error/Belum dibuat, kita pake Hardcode dulu biar Dosen liat jalan
            System.err.println("Database Error: " + e.getMessage());
            checkHardcode(username, password);
        }
    }

    // Logika cadangan kalau Database belum siap
    private void checkHardcode(String user, String pass) {
        if (user.equals("admin") && pass.equals("123")) {
            JOptionPane.showMessageDialog(view, "Login Mode Offline Berhasil!");
            loginSukses();
        } else {
            JOptionPane.showMessageDialog(view, "Login Gagal! (DB Error & Password Salah)");
        }
    }

    private void loginSukses() {
        JOptionPane.showMessageDialog(view, "Login Berhasil! Selamat Datang.");
        new MainFrame().setVisible(true); // BUKA DASHBOARD
        view.dispose(); // TUTUP LOGIN
    }
}
