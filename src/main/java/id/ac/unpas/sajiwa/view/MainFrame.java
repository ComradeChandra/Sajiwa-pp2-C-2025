package id.ac.unpas.sajiwa.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * MainFrame = RUMAH UTAMA APLIKASI
 * Isinya: Sidebar (Kiri) + Content Panel (Kanan)
 */
public class MainFrame extends JFrame {
    
    // Panel khusus buat gonta-ganti isi halaman (Buku/Anggota/dll)
    private JPanel contentPanel; 

    public MainFrame() {
        initComponents();
    }

    private void initComponents() {
        // 1. Setup Jendela Utama
        setTitle("Sajiwa Library System v1.0");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 750); // Ukuran default pas dibuka
        setLocationRelativeTo(null); // Biar muncul di tengah layar
        
        // Kita pake layout BorderLayout (Barat, Timur, Tengah, dll)
        setLayout(new BorderLayout());
        
        // ==========================================================
        // 2. BIKIN SIDEBAR (MENU KIRI)
        // ==========================================================
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(44, 62, 80)); // Warna Biru Gelap (Elegan)
        sidebar.setPreferredSize(new Dimension(260, 0)); // Lebar Sidebar
        sidebar.setBorder(new EmptyBorder(30, 20, 30, 20)); // Jarak pinggir (Padding)
        
        // --- LOGO & JUDUL ---
        JLabel lblIcon = new JLabel("🏛️"); // Ceritanya Logo
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
        lblIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblTitle = new JLabel("SAJIWA");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 28));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblSubtitle = new JLabel("Library System");
        lblSubtitle.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblSubtitle.setForeground(new Color(189, 195, 199)); // Abu muda
        lblSubtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Masukin Logo ke Sidebar
        sidebar.add(lblIcon);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(lblTitle);
        sidebar.add(lblSubtitle);
        sidebar.add(Box.createRigidArea(new Dimension(0, 50))); // Jarak ke tombol menu
        
        // --- TOMBOL MENU ---
        JButton btnBuku = createMenuButton("📚  Data Buku");
        JButton btnAnggota = createMenuButton("👥  Data Anggota");
        JButton btnLaporan = createMenuButton("📄  Laporan");
        JButton btnLogout = createMenuButton("🚪  Logout");
        
        // --- LOGIKA PINDAH HALAMAN (NAVIGATION) ---
        
        // 1. Klik Menu Buku -> Tampilin Panel Fitri
        btnBuku.addActionListener(e -> {
            gantiHalaman(new BukuPanel()); // Panggil class BukuPanel punya Fitri
        });
        
        // 2. Klik Menu Anggota -> Sementara kasih info dulu (karena Murod blm beres)
        btnAnggota.addActionListener(e -> {
            // Nanti kalau AnggotaPanel udah jadi, ganti baris ini:
            // gantiHalaman(new AnggotaPanel()); 
            JOptionPane.showMessageDialog(this, "Halaman Anggota sedang dikerjakan Murod! 🚧");
        });
        
        // 3. Klik Laporan
        btnLaporan.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Fitur Laporan Coming Soon!");
        });

        // 4. Klik Logout
        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Yakin mau keluar?", "Logout", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
        
        // Masukin tombol ke sidebar
        sidebar.add(btnBuku);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10))); // Spasi antar tombol
        sidebar.add(btnAnggota);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnLaporan);
        
        // Tombol Logout ditaruh paling bawah (pake Glue)
        sidebar.add(Box.createVerticalGlue()); 
        sidebar.add(btnLogout);

        // ==========================================================
        // 3. BIKIN CONTENT PANEL (AREA TENGAH)
        // ==========================================================
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        
        // DEFAULT VIEW: Pas pertama aplikasi dibuka, langsung munculin BukuPanel
        contentPanel.add(new BukuPanel(), BorderLayout.CENTER);

        // Gabungin Sidebar & Content ke Frame Utama
        add(sidebar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
    }
    
    // --- Method Helper: Buat Gonta-Ganti Isi Halaman ---
    private void gantiHalaman(JPanel panelBaru) {
        contentPanel.removeAll(); // Buang panel lama
        contentPanel.add(panelBaru, BorderLayout.CENTER); // Pasang panel baru
        contentPanel.revalidate(); // Refresh layout
        contentPanel.repaint(); // Gambar ulang
    }
    
    // --- Method Helper: Buat Bikin Tombol Sidebar yg Ganteng ---
    private JButton createMenuButton(String text) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(220, 50)); // Ukuran tombol
        btn.setBackground(new Color(52, 73, 94)); // Warna dasar
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setFocusPainted(false); // Ilangin garis fokus
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Padding teks
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Kursor jadi tangan
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Efek Hover (Warna berubah pas mouse lewat)
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(46, 204, 113)); // Jadi Hijau pas disorot
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(52, 73, 94)); // Balik ke warna asal
            }
        });
        return btn;
    }
}