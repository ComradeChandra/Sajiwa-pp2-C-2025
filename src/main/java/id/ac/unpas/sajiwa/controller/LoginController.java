package id.ac.unpas.sajiwa.controller;

import id.ac.unpas.sajiwa.view.LoginView;

import javax.swing.*;
import java.sql.*;
import id.ac.unpas.sajiwa.database.KoneksiDB;


public class LoginController {

    private LoginView view;

    public LoginController(LoginView view) {
        this.view = view;

        view.btnLogin.addActionListener(e -> login());
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

            String sql = "SELECT * FROM user_login WHERE username=? AND password=?";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(view, "Login Berhasil");
                // TODO: buka menu utama
            } else {
                JOptionPane.showMessageDialog(view, "Username / Password salah");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, "Error DB: " + e.getMessage());
        }
    }


    
}
