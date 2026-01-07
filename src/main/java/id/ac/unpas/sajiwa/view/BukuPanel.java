package id.ac.unpas.sajiwa.view;

import id.ac.unpas.sajiwa.controller.BukuController; // Sambungin ke Otak Baru
import id.ac.unpas.sajiwa.model.Buku;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Panel View untuk mengelola data Buku
 * SUDAH DIPERBAIKI: Menggunakan Konsep MVC (View Pasif)
 */
public class BukuPanel extends JPanel {
    // Komponen UI
    private JTextField txtIsbn, txtJudul, txtStok, txtCari, txtId;
    private JButton btnTambah, btnUpdate, btnHapus, btnBersih, btnCari, btnRefresh;
    private JTable tableBuku;
    private DefaultTableModel tableModel;
    
    // Helper untuk Logic
    private String selectedIsbn = null;
    
    public BukuPanel() {
        initComponents();
        
        // [PENTING] Panggil Controller biar dia yang ngatur logika tombol & data
        // Jadi pas Panel ini dibuat, Controller langsung bekerja.
        new BukuController(this);
    }
    
    private void initComponents(){
        // [STYLE 1] Layout Utama dengan Jarak (Padding)
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(245, 245, 250)); // Warna Background Panel agak abu muda
        setBorder(new EmptyBorder(20, 20, 20, 20)); // Margin pinggir 20px
        
        // --- HEADER JUDUL ---
        JLabel lblJudul = new JLabel("📚 Manajemen Data Buku");
        lblJudul.setFont(new Font("SansSerif", Font.BOLD, 24)); // Font Besar
        lblJudul.setForeground(new Color(50, 50, 50)); // Warna Teks Gelap
        add(lblJudul, BorderLayout.NORTH);

        // --- PANEL TENGAH (FORM & BUTTON) ---
        JPanel panelCenter = new JPanel(new BorderLayout(10, 10));
        panelCenter.setOpaque(false); // Biar warna background tembus

        // 1. Panel Form Input
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBackground(Color.WHITE); // Background Putih bersih
        // Border dengan Judul
        panelForm.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)), 
            " Input Data Baru ", 
            javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, 
            javax.swing.border.TitledBorder.DEFAULT_POSITION, 
            new Font("SansSerif", Font.BOLD, 12)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Jarak antar komponen
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Input ISBN
        gbc.gridx = 0; gbc.gridy = 0;
        panelForm.add(new JLabel("ISBN Buku :"), gbc);
        gbc.gridx = 1;
        txtIsbn = new JTextField(20);
        panelForm.add(txtIsbn, gbc);
        
        // Input Judul
        gbc.gridx = 0; gbc.gridy = 1;
        panelForm.add(new JLabel("Judul Buku :"), gbc);
        gbc.gridx = 1;
        txtJudul = new JTextField(20);
        panelForm.add(txtJudul, gbc);
        
        // Input Stok
        gbc.gridx = 0; gbc.gridy = 2;
        panelForm.add(new JLabel("Stok Buku :"), gbc);
        gbc.gridx = 1;
        txtStok = new JTextField(20);
        panelForm.add(txtStok, gbc);
        
        // Id Kategori
        gbc.gridx = 0; gbc.gridy = 3;
        panelForm.add(new JLabel("Id Kategori :"), gbc);
        gbc.gridx = 1;
        txtId = new JTextField(20);
        panelForm.add(txtId, gbc);
        
        // 2. Panel Tombol Aksi (Warna-Warni)
        JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelButtons.setOpaque(false);
        
        // Inisialisasi Tombol (Listenernya ditangani Controller)
        btnTambah = createStyledButton("➕ Simpan", new Color(46, 204, 113), Color.WHITE); // Hijau
        btnUpdate = createStyledButton("✏️ Update", new Color(243, 156, 18), Color.WHITE); // Orange
        btnHapus = createStyledButton("🗑️ Hapus", new Color(231, 76, 60), Color.WHITE);   // Merah
        btnBersih = createStyledButton("🔄 Reset", new Color(52, 152, 219), Color.WHITE);  // Biru
        
        panelButtons.add(btnTambah);
        panelButtons.add(btnUpdate);
        panelButtons.add(btnHapus);
        panelButtons.add(btnBersih);
        
        // Masukin tombol ke GridForm paling bawah
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        panelForm.add(panelButtons, gbc);
        
        // --- PANEL TABEL (BAWAH) ---
        JPanel panelTabel = new JPanel(new BorderLayout(5, 5));
        panelTabel.setOpaque(false);
        panelTabel.setBorder(BorderFactory.createTitledBorder("Daftar Buku Tersedia"));
        
        // Panel Pencarian
        JPanel panelCari = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelCari.setOpaque(false);
        panelCari.add(new JLabel("🔍 Cari Judul/ISBN:"));
        txtCari = new JTextField(25);
        panelCari.add(txtCari);
        
        btnCari = new JButton("Cari");
        btnRefresh = new JButton("Refresh");
        
        panelCari.add(btnCari);
        panelCari.add(btnRefresh);
        
        // Setup Tabel
        String[] columns = {"ISBN", "Judul Buku", "Stok Tersedia"}; // ID Kategori bisa disembunyikan atau ditambahin
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        tableBuku = new JTable(tableModel);
        tableBuku.setRowHeight(25); // [STYLE] Baris tabel lebih tinggi biar lega
        tableBuku.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableBuku.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13)); // Header Tebal
        
        // Event Listener Tabel (Biar Controller tau baris mana yang dipilih)
        tableBuku.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = tableBuku.getSelectedRow();
                if (selectedRow != -1) {
                    selectedIsbn = tableModel.getValueAt(selectedRow, 0).toString();
                    txtIsbn.setText(tableModel.getValueAt(selectedRow, 0).toString());
                    txtJudul.setText(tableModel.getValueAt(selectedRow, 1).toString());
                    txtStok.setText(tableModel.getValueAt(selectedRow, 2).toString());
                    // txtId.setText(...) // Perlu ditangani Controller nanti kalau kolomnya ada
                    
                    txtIsbn.setEditable(false); 
                    txtIsbn.setBackground(new Color(240, 240, 240)); // [STYLE] Jadi abu-abu
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tableBuku);
        panelTabel.add(panelCari, BorderLayout.NORTH);
        panelTabel.add(scrollPane, BorderLayout.CENTER);
        
        // Gabungin Semuanya
        panelCenter.add(panelForm, BorderLayout.NORTH);
        panelCenter.add(panelTabel, BorderLayout.CENTER);
        
        add(panelCenter, BorderLayout.CENTER);
    }
    
    // [STYLE] Method helper buat bikin tombol warna-warni
    private JButton createStyledButton(String text, Color bg, Color textCol) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(textCol);
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setFocusPainted(false);
        return btn;
    }
    
    // ==========================================================
    // BAGIAN PENTING MVC: GETTER & SETTER
    // Biar Controller bisa akses komponen tanpa ngotak-ngatik UI
    // ==========================================================

    public JTextField getTxtIsbn() { return txtIsbn; }
    public JTextField getTxtJudul() { return txtJudul; }
    public JTextField getTxtStok() { return txtStok; }
    public JTextField getTxtId() { return txtId; }
    public JTextField getTxtCari() { return txtCari; }

    public JButton getBtnSimpan() { return btnTambah; } // btnTambah kita mapping jadi Simpan
    public JButton getBtnUpdate() { return btnUpdate; }
    public JButton getBtnHapus() { return btnHapus; }
    public JButton getBtnReset() { return btnBersih; }
    public JButton getBtnCari() { return btnCari; }
    public JButton getBtnRefresh() { return btnRefresh; }

    public String getSelectedIsbn() { return selectedIsbn; }

    // Method ini dipanggil Controller buat ngisi tabel
    public void setTableData(List<Buku> list) {
        tableModel.setRowCount(0); 
        for (Buku buku : list) {
            // Sesuaikan urutan kolom dengan tabel di atas
            Object[] row = { buku.getIsbn(), buku.getJudul(), buku.getStok() };
            tableModel.addRow(row); 
        }
    }
    
    // Method ini dipanggil Controller buat bersihin form
    public void clearForm() {
        selectedIsbn = null;
        txtIsbn.setText(""); txtJudul.setText(""); txtStok.setText(""); txtId.setText(""); txtCari.setText("");
        txtIsbn.setEditable(true); 
        txtIsbn.setBackground(Color.WHITE); 
        tableBuku.clearSelection();
    }

    // ==========================================================
    // MAIN METHOD BIAR BISA DI-RUN (SHIFT+F6)
    // ==========================================================
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf());
            // [STYLE] Kustomisasi tambahan buat FlatLaf biar makin cantik
            UIManager.put("Button.arc", 10); // Tombol agak bulat
            UIManager.put("Component.arc", 10); // Input field agak bulat
        } catch (Exception ex) {
            System.err.println("Gagal load skin FlatLaf.");
        }

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Aplikasi Perpustakaan - Manajemen Buku");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new BukuPanel());
            frame.pack();
            frame.setLocationRelativeTo(null); 
            frame.setVisible(true); 
        });
    }
}

/*
     * ==========================================================================
     * CATATAN PENTING 
     * ==========================================================================
     * * 1. KENAPA ADA "public static void main" DI BAWAH?
     * - File ini aslinya cuma "JPanel" (ibarat cuma selembar Kertas Gambar).
     * - Kertas gak bisa berdiri sendiri, butuh "JFrame" (Bingkai Kayu).
     * - Makanya kita bikin method main() buat bikin Bingkai sementara.
     * - CARA RUN: Tekan Shift + F6 (Run File).
     *
     * 2. KENAPA KODINGAN "// TODO" DIHAPUS?
     * - Karena Murod udah beres bikin "Otak"-nya (BukuModel).
     * - Tombol "Simpan" kamu sekarang udah disambungin ke database.
     * - Kalau kodingannya masih TODO, tombolnya cuma pajangan doang.
     *
     * 3. KALAU PAS DI-RUN MUNCUL ERROR MERAH (Exception)?
     * - Cek XAMPP: MySQL harus nyala (Start).
     * - Cek Database: Pastiin kamu udah jalanin script SQL "CREATE TABLE" 
     * di phpMyAdmin. Kalau tabel gak ada, Java pasti ngamuk.
     *
     * * ==========================================================================
     */