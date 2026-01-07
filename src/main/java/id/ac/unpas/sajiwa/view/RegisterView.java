package id.ac.unpas.sajiwa.view;

import javax.swing.*;
import java.awt.*;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

/**
 * RegisterView - Tampilan REgister Modern Tema: Navy & Light Blue (Senada dengan
 * MainFrame)
 */
public class RegisterView extends JFrame {

    // Komponen Public untuk Controller
    public JTextField txtUsername = new JTextField();
    public JTextField txtProdi = new JTextField();
    public JTextField txtNpm = new JTextField();
    public JPasswordField txtPassword = new JPasswordField();
    public JTextField txtConfirmPassword = new JTextField();
    public JButton btnRegister = new JButton("REGISTER");
    public JButton btnBack = new JButton("KEMBALI");

    public RegisterView() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Sajiwa Library - Register");
        setSize(400, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // --- PANEL UTAMA ---
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(230, 242, 255));
        mainPanel.setLayout(null);
        add(mainPanel);

        // --- HEADER AREA ---
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(25, 42, 86));
        headerPanel.setBounds(0, 0, 400, 120); // Diperpendek sedikit biar muat banyak input
        headerPanel.setLayout(null);
        mainPanel.add(headerPanel);

        JLabel lblLogo = new JLabel("🏛️");
        lblLogo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
        lblLogo.setBounds(180, 15, 50, 50);
        headerPanel.add(lblLogo);

        JLabel lblWelcome = new JLabel("REGISTER ACCOUNT", SwingConstants.CENTER);
        lblWelcome.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblWelcome.setForeground(Color.WHITE);
        lblWelcome.setBounds(0, 70, 400, 25);
        headerPanel.add(lblWelcome);

        // --- FORM INPUT (Sistem Koordinat Y Dinamis) ---
        int inputWidth = 300;
        int inputX = (400 - inputWidth) / 2;
        int currentY = 140; // Titik awal Y setelah header
        int labelHeight = 20;
        int fieldHeight = 35;
        int spacing = 10;   // Jarak antar label ke field
        int nextGroup = 15; // Jarak antar grup input

        // 1. Username
        JLabel lblUser = createFormLabel("Username", inputX, currentY, inputWidth);
        mainPanel.add(lblUser);
        currentY += labelHeight + spacing;

        styleField(txtUsername);
        txtUsername.setBounds(inputX, currentY, inputWidth, fieldHeight);
        mainPanel.add(txtUsername);
        currentY += fieldHeight + nextGroup;

        // 2. NPM
        JLabel lblNpm = createFormLabel("NPM", inputX, currentY, inputWidth);
        mainPanel.add(lblNpm);
        currentY += labelHeight + spacing;

        styleField(txtNpm);
        txtNpm.setBounds(inputX, currentY, inputWidth, fieldHeight);
        mainPanel.add(txtNpm);
        currentY += fieldHeight + nextGroup;

        // 3. Prodi
        JLabel lblProdi = createFormLabel("Program Studi", inputX, currentY, inputWidth);
        mainPanel.add(lblProdi);
        currentY += labelHeight + spacing;

        styleField(txtProdi);
        txtProdi.setBounds(inputX, currentY, inputWidth, fieldHeight);
        mainPanel.add(txtProdi);
        currentY += fieldHeight + nextGroup;

        // 4. Password
        JLabel lblPass = createFormLabel("Password", inputX, currentY, inputWidth);
        mainPanel.add(lblPass);
        currentY += labelHeight + spacing;

        styleField(txtPassword);
        txtPassword.setBounds(inputX, currentY, inputWidth, fieldHeight);
        mainPanel.add(txtPassword);
        currentY += fieldHeight + nextGroup;

        // 5. Konfirmasi Password
        JLabel lblConfirm = createFormLabel("Konfirmasi Password", inputX, currentY, inputWidth);
        mainPanel.add(lblConfirm);
        currentY += labelHeight + spacing;

        styleField(txtConfirmPassword);
        txtConfirmPassword.setBounds(inputX, currentY, inputWidth, fieldHeight);
        mainPanel.add(txtConfirmPassword);
        currentY += fieldHeight + 25; // Spasi lebih besar sebelum tombol

        // --- BUTTON REGISTER ---
        btnRegister.setBounds(inputX, currentY, inputWidth, 45);
        btnRegister.setBackground(new Color(25, 42, 86));
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setFont(new Font("SansSerif", Font.BOLD, 15));
        btnRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRegister.setFocusPainted(false);

        btnRegister.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnRegister.setBackground(new Color(41, 128, 185));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnRegister.setBackground(new Color(25, 42, 86));
            }
        });
        mainPanel.add(btnRegister);

        // --- BUTTON BACK (KEMBALI) ---
        currentY += 45 + 10; // Geser ke bawah tombol register
        btnBack.setBounds(inputX, currentY, inputWidth, 45);
        btnBack.setBackground(new Color(149, 165, 166)); // Warna abu-abu
        btnBack.setForeground(Color.WHITE);
        btnBack.setFont(new Font("SansSerif", Font.BOLD, 15));
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBack.setFocusPainted(false);
        // Hover effect simple
        btnBack.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnBack.setBackground(new Color(127, 140, 141));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnBack.setBackground(new Color(149, 165, 166));
            }
        });
        mainPanel.add(btnBack);

        // Footer
        JLabel lblFooter = new JLabel("© 2024 Sajiwa Team Project", SwingConstants.CENTER);
        lblFooter.setFont(new Font("SansSerif", Font.ITALIC, 10));
        lblFooter.setForeground(Color.GRAY);
        lblFooter.setBounds(0, 630, 400, 20);
        mainPanel.add(lblFooter);
    }

// --- Helper Method agar kode tidak berulang-ulang ---
    private JLabel createFormLabel(String text, int x, int y, int w) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 13));
        lbl.setForeground(new Color(25, 42, 86));
        lbl.setBounds(x, y, w, 20);
        return lbl;
    }

    private void styleField(JTextField field) {
        field.putClientProperty("JComponent.roundRect", true);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 210, 255)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
    }
}

/*
 * ==================================================================================
 * CATATAN PRIBADI (CHANDRA)
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