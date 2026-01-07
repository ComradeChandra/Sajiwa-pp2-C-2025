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
    private String userRole; // Nambahin Role User

    public MainFrame() {
        this("admin"); // Default Admin kalau dipanggil tanpa parameter
    }

    public MainFrame(String role) {
        this.userRole = role;
        initComponents();
    }

    private void initComponents() {
        // 1. Setup Jendela Utama
        setTitle("Sajiwa Library System v1.0 - " + userRole.toUpperCase());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 750); // Ukuran default pas opened
        setLocationRelativeTo(null); // Biar muncul di tengah layar
        
        // Kita pake layout BorderLayout (Barat, Timur, Tengah, dll)
        setLayout(new BorderLayout());
        
        // ==========================================================
        // 2. BIKIN SIDEBAR (MENU KIRI)
        // ==========================================================
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(25, 42, 86)); // Warna Navy (Sesuai request)
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
        // Warna Button Biru Muda, Teks Navy
        Color btnColor = new Color(180, 210, 255);
        Color txtColor = new Color(25, 42, 86);
        
        JButton btnDashboard = createMenuButton("🏠  Dashboard", btnColor, txtColor);
        JButton btnBuku = createMenuButton("📚  Data Buku", btnColor, txtColor);
        JButton btnAnggota = createMenuButton("👥  Data Anggota", btnColor, txtColor);
        JButton btnKategori = createMenuButton("🏷️  Kategori Buku", btnColor, txtColor);
        JButton btnTransaksi = createMenuButton("🔄  Transaksi", btnColor, txtColor);
        JButton btnLaporan = createMenuButton("📄  Laporan", btnColor, txtColor);
        JButton btnLogout = createMenuButton("🚪  Logout", new Color(231, 76, 60), Color.WHITE);
        
        // --- LOGIKA PINDAH HALAMAN (NAVIGATION) ---
        
        // 0. Dashboard
        btnDashboard.addActionListener(e -> gantiHalaman(new DashboardPanel()));

        // 1. Klik Menu Buku -> Tampilin BukuPanel
        btnBuku.addActionListener(e -> {
            boolean isEditable = userRole.equalsIgnoreCase("admin") || userRole.equalsIgnoreCase("petugas");
            gantiHalaman(new BukuPanel(isEditable)); 
        });
        
        // 2. Klik Menu Anggota -> Tampilin AnggotaPanel (SUDAH DIPERBAIKI)
        btnAnggota.addActionListener(e -> {
            // Logic pesan "Sedang dikerjakan" sudah dihapus
            // Sekarang langsung load panel anggota
            gantiHalaman(new AnggotaPanel()); 
        });
        
        btnKategori.addActionListener(e -> {
            gantiHalaman(new KategoriPanel());
        });
        
        btnTransaksi.addActionListener(e -> {
            gantiHalaman(new PeminjamanPanel());
        });
        
        // 3. Klik Laporan
        btnLaporan.addActionListener(e -> {
            // Panggil Service Reporting untuk mencetak semua laporan jadi satu PDF
            new id.ac.unpas.sajiwa.util.ReportService().cetakLaporanGabungan();
        });

        // 4. Klik Logout
        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Yakin mau keluar?", "Logout", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                new LoginView().setVisible(true); // Balik ke Login
                dispose();
            }
        });
        
        // Masukin tombol ke sidebar sesuai ROLE
        sidebar.add(btnDashboard);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        
        sidebar.add(btnBuku);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10))); // Spasi antar tombol
        
        if (userRole.equalsIgnoreCase("admin") || userRole.equalsIgnoreCase("petugas")) {
            // Admin/Petugas bisa lihat semua
            sidebar.add(btnAnggota);
            sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
            sidebar.add(btnKategori);
            sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
            sidebar.add(btnTransaksi);
            sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
            sidebar.add(btnLaporan);
        } else {
            // User Biasa (Mahasiswa) cuma bisa Dashboard & Buku (Read Only logic nanti di Panel)
            // Tombol lain tidak ditampilkan
        }
        
        // Tombol Logout ditaruh paling bawah (pake Glue)
        sidebar.add(Box.createVerticalGlue()); 
        sidebar.add(btnLogout);

        // ==========================================================
        // 3. BIKIN CONTENT PANEL (AREA TENGAH)
        // ==========================================================
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        
        // DEFAULT VIEW: DashboardPanel
        contentPanel.add(new DashboardPanel(), BorderLayout.CENTER);

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
    
    // --- Method Helper: Buat Bikin Tombol Sidebar (Updated Parameter) ---
    private JButton createMenuButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(220, 50)); // Ukuran tombol
        btn.setBackground(bg); // Warna dasar custom
        btn.setForeground(fg); // Warna teks custom
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setFocusPainted(false); // Ilangin garis fokus
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Padding teks
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Kursor jadi tangan
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Efek Hover (Warna berubah pas mouse lewat)
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(bg.darker()); // Jadi sedikit lebih gelap pas disorot
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(bg); // Balik ke warna asal
            }
        });
        return btn;
    }

    public static void main(String[] args) {
         try {
            UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf());
        } catch (Exception ex) {
            System.err.println("Gagal load skin FlatLaf.");
        }

        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}


/*
 * ==================================================================================
 * CATATAN PRIBADI (CHANDRA)
 * ==================================================================================
 * 1. Navigation Logic:
 *    - Menggunakan contentPanel (JPanel kosong di tengah) sebagai container halaman.
 *    - Method gantiHalaman(JPanel panelBaru) bertugas:
 *      a. Menghapus panel lama (contentPanel.removeAll).
 *      b. Menambahkan panel baru yang dikirim via parameter (contentPanel.add).
 *      c. Me-refresh tampilan agar perubahan terlihat (revalidate & repaint).
 *
 * 2. Layout Management:
 *    - Sidebar menggunakan BoxLayout (Y_AXIS) agar tombol tersusun vertikal rapi.
 *    - Content Area menggunakan BorderLayout agar panel anak mengisi seluruh ruang kosong.
 *
 * 3. Integrasi Modules:
 *    - Menghubungkan semua modul (Buku, Anggota, Kategori, Transaksi) ke satu navigasi pusat.
 *    - Tombol "Data Anggota" & "Transaksi" sekarang sudah aktif memanggil Panel masing-masing.
 *
 * 4. Security (Role Access):
 *    - Constructor MainFrame sekarang menerima parameter 'role'.
 *    - Kalau role == 'user', tombol-tombol manajemen (CRUD) di-hide pakai if-else 
 *      sederhana. User cuma bisa liat Dashboard & Cari Buku.
 * 
 * 5. Update Terakhir:
 *    - Udah nambahin fitur Dashboard sebagai tampilan default.
 *    - Udah nambahin fitur Laporan (PDF) di tombol paling bawah.
 * ==================================================================================
 */