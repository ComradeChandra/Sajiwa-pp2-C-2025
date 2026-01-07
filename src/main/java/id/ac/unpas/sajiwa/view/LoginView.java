package id.ac.unpas.sajiwa.view;

import javax.swing.*;
import java.awt.*;

/**
 * LoginView - Tampilan Login Modern
 * Tema: Navy & Light Blue (Senada dengan MainFrame)
 */
public class LoginView extends JFrame {
    // Komponen Public untuk Controller
    public JTextField txtUsername = new JTextField();
    public JPasswordField txtPassword = new JPasswordField();
    public JButton btnLogin = new JButton("LOGIN");

    public LoginView() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Sajiwa Library - Login");
        setSize(400, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // --- PANEL UTAMA (Background Biru Muda) ---
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(230, 242, 255));
        mainPanel.setLayout(null);
        add(mainPanel);

        // --- HEADER / LOGO AREA ---
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(25, 42, 86)); // Navy
        headerPanel.setBounds(0, 0, 400, 150);
        headerPanel.setLayout(null);
        mainPanel.add(headerPanel);

        JLabel lblLogo = new JLabel("🏛️");
        lblLogo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 50));
        lblLogo.setBounds(175, 20, 60, 60);
        headerPanel.add(lblLogo);

        JLabel lblWelcome = new JLabel("SAJIWA LIBRARY", SwingConstants.CENTER);
        lblWelcome.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblWelcome.setForeground(Color.WHITE);
        lblWelcome.setBounds(0, 85, 400, 30);
        headerPanel.add(lblWelcome);

        JLabel lblSub = new JLabel("Please enter your credentials", SwingConstants.CENTER);
        lblSub.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblSub.setForeground(new Color(189, 195, 199));
        lblSub.setBounds(0, 110, 400, 20);
        headerPanel.add(lblSub);

        // --- FORM INPUT ---
        int inputWidth = 300;
        int inputX = (400 - inputWidth) / 2;

        // Label Username
        JLabel lblUser = new JLabel("Username");
        lblUser.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblUser.setForeground(new Color(25, 42, 86));
        lblUser.setBounds(inputX, 180, 100, 25);
        mainPanel.add(lblUser);

        // Field Username
        txtUsername.setBounds(inputX, 210, inputWidth, 35);
        txtUsername.putClientProperty("JComponent.roundRect", true); // Efek bulat FlatLaf
        txtUsername.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 210, 255)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        mainPanel.add(txtUsername);

        // Label Password
        JLabel lblPass = new JLabel("Password");
        lblPass.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblPass.setForeground(new Color(25, 42, 86));
        lblPass.setBounds(inputX, 260, 100, 25);
        mainPanel.add(lblPass);

        // Field Password
        txtPassword.setBounds(inputX, 290, inputWidth, 35);
        txtPassword.putClientProperty("JComponent.roundRect", true);
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 210, 255)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        mainPanel.add(txtPassword);

        // --- BUTTON LOGIN ---
        btnLogin.setBounds(inputX, 360, inputWidth, 45);
        btnLogin.setBackground(new Color(25, 42, 86)); // Navy
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("SansSerif", Font.BOLD, 15));
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.setFocusPainted(false);
        
        // Efek Hover Tombol
        btnLogin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnLogin.setBackground(new Color(41, 128, 185));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnLogin.setBackground(new Color(25, 42, 86));
            }
        });
        mainPanel.add(btnLogin);

        // Footer teks
        JLabel lblFooter = new JLabel("© 2024 Sajiwa Team Project", SwingConstants.CENTER);
        lblFooter.setFont(new Font("SansSerif", Font.ITALIC, 10));
        lblFooter.setForeground(Color.GRAY);
        lblFooter.setBounds(0, 430, 400, 20);
        mainPanel.add(lblFooter);
    }
}

/*
 * ==================================================================================
 * CATATAN PENGEMBANG (DEV LOG)
 * ==================================================================================
 * 1. Desain Konsisten:
 * Warna sengaja disamakan dengan MainFrame (Navy & Biru Muda) supaya user nggak 
 * kaget pas pindah dari login ke menu utama. Terkesan satu kesatuan aplikasi.
 * * 2. Layouting:
 * Di sini saya tetap pakai setBounds (null layout) biar gampang ngatur posisi 
 * elemen di tengah secara presisi. Ukuran frame saya buat 400x500 biar agak tinggi
 * dan lega, mirip login app modern.
 * * 3. FlatLaf Hint:
 * Ada kode 'putClientProperty("JComponent.roundRect", true)'. Ini bakal bikin 
 * textfield jadi agak bulat ujungnya, tapi syaratnya di main class kamu harus 
 * panggil FlatLaf dulu. Kalau nggak pakai FlatLaf, tampilannya bakal tetap rapi 
 * kok cuma kotak biasa aja.
 * * 4. UX:
 * Udah ditambahin hover effect di tombol login. Jadi pas mouse masuk, warnanya 
 * berubah biru terang sedikit biar kerasa "interaktif".
 * ==================================================================================
 */