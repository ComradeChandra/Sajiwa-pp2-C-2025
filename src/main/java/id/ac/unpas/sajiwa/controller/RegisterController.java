/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package id.ac.unpas.sajiwa.controller;
import id.ac.unpas.sajiwa.view.RegisterView;
import id.ac.unpas.sajiwa.view.LoginView;
import id.ac.unpas.sajiwa.model.Anggota;
import id.ac.unpas.sajiwa.model.AnggotaModel;
import id.ac.unpas.sajiwa.database.KoneksiDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.swing.JOptionPane;

/**
 *
 * @author Fitriyani Rahmadini
 */
public class RegisterController {
      
    private RegisterView view;
    private AnggotaModel anggotaModel;
    
    public RegisterController(RegisterView view) {
        this.view = view;
        this.anggotaModel = new AnggotaModel();
        
        // Listener Tombol Register
        view.btnRegister.addActionListener(e -> register());

        // Listener Tombol Kembali
        view.btnBack.addActionListener(e -> {
            LoginView loginView = new LoginView();
            new LoginController(loginView); // Pasang controller
            loginView.setVisible(true);
            view.dispose();
        });
    }

    private void register() {
        // 1. Ambil Data
        String username = view.txtUsername.getText();
        String npm = view.txtNpm.getText();
        String prodi = (String) view.txtProdi.getSelectedItem();
        String password = new String(view.txtPassword.getPassword());
        String confirmPassword = new String(view.txtConfirmPassword.getPassword());

        // 2. Validasi Empty
        if (username.isEmpty() || npm.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Harap isi semua kolom!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 3. Validasi Password Match
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(view, "Password dan Konfirmasi Password tidak cocok!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 4. Proses Simpan ke Database
        try {
            Connection conn = KoneksiDB.getConnection();
            if (conn != null) {
                // A. Simpan ke Tabel user_login (Akun buat Login)
                String sqlUser = "INSERT INTO user_login (username, password, role) VALUES (?, ?, ?)";
                PreparedStatement psUser = conn.prepareStatement(sqlUser);
                psUser.setString(1, username);
                psUser.setString(2, password);
                psUser.setString(3, "mahasiswa"); // Role default mahasiswa
                psUser.executeUpdate();

                // B. Simpan ke Tabel anggota (Biodata Perpustakaan)
                // Menggunakan AnggotaModel agar rapi
                Anggota anggotaBaru = new Anggota(npm, username, prodi, "Aktif");
                anggotaModel.addAnggota(anggotaBaru);

                // C. Berhasil
                JOptionPane.showMessageDialog(view, "Registrasi Berhasil! Silakan Login.");
                
                // Redirect ke Login
                LoginView loginView = new LoginView();
                new LoginController(loginView);
                loginView.setVisible(true);
                view.dispose();

            } else {
                JOptionPane.showMessageDialog(view, "Gagal terhubung ke database!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Gagal Registrasi: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}

/*
 * ==================================================================================
 * CATATAN PRIBADI (CHANDRA)
 * ==================================================================================
 * 1. Dual Table Insert Strategy:
 *    - Registrasi di sini agak tricky karena data harus masuk ke DUA tabel sekaligus:
 *      a. Tabel 'user_login' -> simpan username, password, dan role 'mahasiswa'.
 *      b. Tabel 'anggota' -> simpan profil lengkap (NPM, Nama, Prodi).
 *    - Ini dilakukan biar satu akun bisa login (user_login) DAN terdata sebagai anggota
 *      perpustakaan (anggota) secara otomatis.
 *
 * 2. Model Integrity (AnggotaModel):
 *    - Saya panggil 'anggotaModel.addAnggota(anggotaBaru)' daripada nulis raw SQL
 *      insert lagi di sini. Tujuannya biar kodingan lebih rapi dan kalau ada perubahan
 *      di tabel anggota, saya cuma perlu ubah di AnggotaModel.java aja.
 *
 * 3. User Experience (Redirect):
 *    - Setelah sukses register, user dipaksa logout (redirect ke LoginView) biar
 *      mereka login ulang pakai akun baru. Ini standar keamanan aplikasi umumnya.
 * ==================================================================================
 */
