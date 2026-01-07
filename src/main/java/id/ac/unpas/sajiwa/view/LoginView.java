package id.ac.unpas.sajiwa.view;

import javax.swing.*;

public class LoginView extends JFrame {
    // Komponen dibuat Public biar bisa diakses Controller
    public JTextField txtUsername = new JTextField();
    public JPasswordField txtPassword = new JPasswordField();
    public JButton btnLogin = new JButton("Login");

    public LoginView() {
        setTitle("Login Sistem Perpustakaan");
        setSize(350, 250);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel lblUser = new JLabel("Username");
        lblUser.setBounds(30, 30, 100, 25);
        add(lblUser);

        txtUsername.setBounds(130, 30, 150, 25);
        add(txtUsername);

        JLabel lblPass = new JLabel("Password");
        lblPass.setBounds(30, 70, 100, 25);
        add(lblPass);

        txtPassword.setBounds(130, 70, 150, 25);
        add(txtPassword);

        btnLogin.setBounds(30, 120, 100, 30);
        add(btnLogin);
        
        // GAK ADA LOGIKA DI SINI. SEMUA DI CONTROLLER.
    }
}