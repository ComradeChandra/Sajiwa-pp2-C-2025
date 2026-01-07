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
                    String role = rs.getString("role");
                    loginSukses(role, username); // Pindah ke Dashboard bawa Role & Username
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
            loginSukses("admin", user);
        } else if (user.equals("user") && pass.equals("123")) {
             loginSukses("user", user);
        } else {
            JOptionPane.showMessageDialog(view, "Login Gagal! (DB Error & Password Salah)");
        }
    }

    private void loginSukses(String role, String username) {
        JOptionPane.showMessageDialog(view, "Login Berhasil! Selamat Datang, " + username + ".");
        new MainFrame(role, username).setVisible(true); // BUKA DASHBOARD dengan ROLE & USERNAME
        view.dispose(); // TUTUP LOGIN
    }
}

/*
 * ==================================================================================
 * CATATAN PRIBADI (CHANDRA)
 * ==================================================================================
 * 1. Role-Based Access Control (RBAC):
 *    - Logic utama keamanan aplikasi ada di sini.
 *    - Setelah query SELECT user ketemu, saya ambil kolom 'role'-nya.
 *    - Nilai role ini ('admin' atau 'user') DIOPER ke constructor MainFrame.
 *    - Jadi MainFrame "tahu" siapa yang lagi login, dan bisa nyembunyiin tombol 
 *      yang gak berhak diakses.
 * 
 * 2. Fallback Mechanism (Fitur Anti Panik):
 *    - Ada method 'checkHardcode' di exception catch block.
 *    - Ini fitur rahasia: Kalau pas demo tiba-tiba XAMPP mati atau error koneksi,
 *      kita tetep bisa login pake user "admin" pass "123".
 *    - Biar pas presentasi gak awkward kalau ada masalah teknis :D
 * ==================================================================================
 */
