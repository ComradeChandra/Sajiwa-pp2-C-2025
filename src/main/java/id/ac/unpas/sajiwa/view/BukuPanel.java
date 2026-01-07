package id.ac.unpas.sajiwa.view;

import id.ac.unpas.sajiwa.controller.BukuController;
import id.ac.unpas.sajiwa.model.Buku;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Panel Manajemen Data Buku
 * Menggabungkan Tampilan Fitri dengan Logika MVC Chandra
 */
public class BukuPanel extends JPanel {
    // Komponen UI
    private JTextField txtIsbn, txtJudul, txtStok, txtIdKategori, txtCari;
    private JButton btnTambah, btnUpdate, btnHapus, btnBersih, btnCari, btnRefresh, btnCetak;
    private JTable tableBuku;
    private DefaultTableModel tableModel;
    
    // State
    private String selectedIsbn = null;
    private boolean isEditable = true; // Default true (Admin)

    public BukuPanel() {
        this(true); // Constructor tanpa argumen = editable
    }

    public BukuPanel(boolean isEditable) {
        this.isEditable = isEditable;
        initComponents();
        // [PENTING] Integrasi MVC: Controller menangani logika
        new BukuController(this);
        
        // Atur aksesibilitas berdasarkan role
        if (!isEditable) {
            btnTambah.setVisible(false);
            btnUpdate.setVisible(false);
            btnHapus.setVisible(false);
            btnBersih.setVisible(false);
            // Non-editable jangan tampilkan form input juga supaya lebih bersih?
            // Atau cukup disable button saja
            // Untuk saat ini disable button saja cukup
        }
    }

    private void initComponents() {
        // Layout Utama
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(230, 242, 255));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // --- HEADER ---
        JLabel lblJudul = new JLabel("📚 Manajemen Data Buku");
        lblJudul.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblJudul.setForeground(new Color(25, 42, 86)); 
        add(lblJudul, BorderLayout.NORTH);

        // --- PANEL TENGAH ---
        JPanel panelCenter = new JPanel(new BorderLayout(15, 15));
        panelCenter.setOpaque(false);

        // 1. Form Input
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBackground(Color.WHITE);
        panelForm.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(180, 210, 255)),
            " Input Data Buku ",
            0, 0, new Font("SansSerif", Font.BOLD, 12), new Color(41, 128, 185)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // ISBN
        gbc.gridx = 0; gbc.gridy = 0;
        panelForm.add(createLabel("ISBN Buku :"), gbc);
        gbc.gridx = 1;
        txtIsbn = new JTextField(20);
        styleField(txtIsbn);
        panelForm.add(txtIsbn, gbc);

        // Judul
        gbc.gridx = 0; gbc.gridy = 1;
        panelForm.add(createLabel("Judul Buku :"), gbc);
        gbc.gridx = 1;
        txtJudul = new JTextField(20);
        styleField(txtJudul);
        panelForm.add(txtJudul, gbc);

        // Stok
        gbc.gridx = 0; gbc.gridy = 2;
        panelForm.add(createLabel("Stok Buku :"), gbc);
        gbc.gridx = 1;
        txtStok = new JTextField(20);
        styleField(txtStok);
        panelForm.add(txtStok, gbc);

        // ID Kategori (Fitur Baru dari Fitri)
        gbc.gridx = 0; gbc.gridy = 3;
        panelForm.add(createLabel("ID Kategori :"), gbc);
        gbc.gridx = 1;
        txtIdKategori = new JTextField(20);
        styleField(txtIdKategori);
        panelForm.add(txtIdKategori, gbc);

        // Tombol Aksi
        JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelButtons.setOpaque(false);
        
        btnTambah = createStyledButton("💾 Simpan", new Color(46, 204, 113), Color.WHITE);
        btnUpdate = createStyledButton("✏️ Update", new Color(243, 156, 18), Color.WHITE);
        btnHapus = createStyledButton("🗑️ Hapus", new Color(231, 76, 60), Color.WHITE);
        btnBersih = createStyledButton("🔄 Reset", new Color(52, 152, 219), Color.WHITE);
        btnCetak = createStyledButton("📄 Cetak PDF", new Color(155, 89, 182), Color.WHITE);

        btnCetak.addActionListener(e -> new id.ac.unpas.sajiwa.util.ReportService().cetakLaporanBuku());
        
        panelButtons.add(btnTambah);
        panelButtons.add(btnUpdate);
        panelButtons.add(btnHapus);
        panelButtons.add(btnBersih);
        panelButtons.add(btnCetak);

        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        panelForm.add(panelButtons, gbc);

        // 2. Tabel & Search
        JPanel panelTabel = new JPanel(new BorderLayout(5, 5));
        panelTabel.setOpaque(false);
        panelTabel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEmptyBorder(), "Daftar Buku Terdaftar",
            0, 0, new Font("SansSerif", Font.BOLD, 12), new Color(25, 42, 86)
        ));

        // Area Cari
        JPanel panelCari = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelCari.setOpaque(false);
        txtCari = new JTextField(20);
        txtCari.putClientProperty("JTextField.placeholderText", "Cari Judul / ISBN...");
        
        btnCari = createStyledButton("🔍 Cari", new Color(52, 73, 94), Color.WHITE);
        btnRefresh = createStyledButton("Refresh", new Color(149, 165, 166), Color.WHITE);
        
        panelCari.add(txtCari);
        panelCari.add(btnCari);
        panelCari.add(btnRefresh);

        // Setup Tabel (Dengan kolom Kategori)
        String[] columns = {"ISBN", "Judul Buku", "Stok", "ID Kategori", "Kategori"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tableBuku = new JTable(tableModel);
        tableBuku.setRowHeight(28);
        tableBuku.setSelectionBackground(new Color(180, 210, 255));
        tableBuku.setSelectionForeground(Color.BLACK);
        tableBuku.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));

        JScrollPane scrollPane = new JScrollPane(tableBuku);
        scrollPane.getViewport().setBackground(Color.WHITE);

        // Listener Klik Tabel
        tableBuku.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tableBuku.getSelectedRow() != -1) {
                int row = tableBuku.getSelectedRow();
                selectedIsbn = tableModel.getValueAt(row, 0).toString();
                
                txtIsbn.setText(selectedIsbn);
                txtJudul.setText(tableModel.getValueAt(row, 1).toString());
                txtStok.setText(tableModel.getValueAt(row, 2).toString());
                
                // Safe check for columns
                if (tableModel.getColumnCount() > 3)
                     txtIdKategori.setText(tableModel.getValueAt(row, 3).toString());
                
                txtIsbn.setEditable(false);
                txtIsbn.setBackground(new Color(240, 240, 240));
            }
        });

        panelTabel.add(panelCari, BorderLayout.NORTH);
        panelTabel.add(scrollPane, BorderLayout.CENTER);

        panelCenter.add(panelForm, BorderLayout.NORTH);
        panelCenter.add(panelTabel, BorderLayout.CENTER);
        add(panelCenter, BorderLayout.CENTER);
    }

    // --- Helper Styling ---
    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 13));
        lbl.setForeground(new Color(25, 42, 86));
        return lbl;
    }
    private void styleField(JTextField txt) {
        txt.putClientProperty("JComponent.roundRect", true);
    }
    private JButton createStyledButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // --- Getters untuk Controller ---
    public JTextField getTxtIsbn() { return txtIsbn; }
    public JTextField getTxtJudul() { return txtJudul; }
    public JTextField getTxtStok() { return txtStok; }
    public JTextField getTxtIdKategori() { return txtIdKategori; }
    public JTextField getTxtCari() { return txtCari; }

    public JButton getBtnSimpan() { return btnTambah; }
    public JButton getBtnUpdate() { return btnUpdate; }
    public JButton getBtnHapus() { return btnHapus; }
    public JButton getBtnReset() { return btnBersih; }
    public JButton getBtnCari() { return btnCari; }
    public JButton getBtnRefresh() { return btnRefresh; }

    public String getSelectedIsbn() { return selectedIsbn; }

    public void setTableData(List<Buku> list) {
        tableModel.setRowCount(0);
        for (Buku buku : list) {
            Object[] row = { 
                buku.getIsbn(), 
                buku.getJudul(), 
                buku.getStok(),
                buku.getIdKategori(),
                buku.getNamaKategori()
            };
            tableModel.addRow(row);
        }
    }

    public void clearForm() {
        selectedIsbn = null;
        txtIsbn.setText(""); 
        txtJudul.setText(""); 
        txtStok.setText(""); 
        txtIdKategori.setText(""); 
        txtCari.setText("");
        txtIsbn.setEditable(true); 
        txtIsbn.setBackground(Color.WHITE); 
        tableBuku.clearSelection();
    }
}