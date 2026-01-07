package id.ac.unpas.sajiwa.view;

import id.ac.unpas.sajiwa.model.Buku;
import id.ac.unpas.sajiwa.model.BukuModel;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Panel Manajemen Data Buku
 * Tampilan disesuaikan agar seragam dengan AnggotaPanel & KategoriPanel
 * @author Fitriyani Rahmadini
 */
public class BukuPanel extends JPanel {
    // Komponen UI
    private JTextField txtIsbn, txtJudul, txtStok, txtIdKategori, txtCari;
    private JTable tableBuku;
    private DefaultTableModel tableModel;
    
    // Model & Data Helper
    private BukuModel bukuModel;
    private String selectedIsbn = null;

    public BukuPanel() {
        bukuModel = new BukuModel();
        initComponents();
        loadData(); 
    }

    private void initComponents() {
        // Layout Utama
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(230, 242, 255)); // Background Biru Muda (Konsisten)
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // --- HEADER ---
        JLabel lblJudul = new JLabel("📚 Manajemen Data Buku");
        lblJudul.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblJudul.setForeground(new Color(25, 42, 86)); // Navy
        add(lblJudul, BorderLayout.NORTH);

        // --- PANEL TENGAH (FORM + TABEL) ---
        JPanel panelCenter = new JPanel(new BorderLayout(15, 15));
        panelCenter.setOpaque(false);

        // 1. Panel Form Input
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

        // --- Input Fields ---
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

        // ID Kategori (Field yang diminta tambah)
        gbc.gridx = 0; gbc.gridy = 3;
        panelForm.add(createLabel("ID Kategori :"), gbc);
        gbc.gridx = 1;
        txtIdKategori = new JTextField(20);
        styleField(txtIdKategori);
        panelForm.add(txtIdKategori, gbc);

        // 2. Panel Tombol Aksi
        JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelButtons.setOpaque(false);

        JButton btnSimpan = createStyledButton("💾 Simpan", new Color(46, 204, 113), Color.WHITE);
        JButton btnUpdate = createStyledButton("✏️ Update", new Color(243, 156, 18), Color.WHITE);
        JButton btnHapus = createStyledButton("🗑️ Hapus", new Color(231, 76, 60), Color.WHITE);
        JButton btnReset = createStyledButton("🔄 Reset", new Color(52, 152, 219), Color.WHITE);

        btnSimpan.addActionListener(e -> tambahBuku());
        btnUpdate.addActionListener(e -> updateBuku());
        btnHapus.addActionListener(e -> hapusBuku());
        btnReset.addActionListener(e -> bersihkanForm());

        panelButtons.add(btnSimpan);
        panelButtons.add(btnUpdate);
        panelButtons.add(btnHapus);
        panelButtons.add(btnReset);

        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        panelForm.add(panelButtons, gbc);

        // 3. Panel Tabel & Pencarian
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
        JButton btnCari = createStyledButton("🔍 Cari", new Color(52, 73, 94), Color.WHITE);
        btnCari.addActionListener(e -> cariBuku());
        
        panelCari.add(txtCari);
        panelCari.add(btnCari);

        // Tabel Setup (Ditambah kolom ID Kategori)
        String[] columns = {"ISBN", "Judul Buku", "Stok", "ID Kategori"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tableBuku = new JTable(tableModel);
        tableBuku.setRowHeight(28);
        tableBuku.setSelectionBackground(new Color(180, 210, 255));
        tableBuku.setSelectionForeground(Color.BLACK);

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

    // --- Helper Styling (Sesuai Standar Panel Lainnya) ---
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

    // --- Logika CRUD ---
    private void loadData() {
        tableModel.setRowCount(0);
        List<Buku> list = bukuModel.getAllBuku();
        for (Buku b : list) {
            tableModel.addRow(new Object[]{
                b.getIsbn(), b.getJudul(), b.getStok(), b.getIdKategori()
            });
        }
    }

    private void tambahBuku() {
        if (txtIsbn.getText().isEmpty() || txtJudul.getText().isEmpty()) return;
        Buku b = new Buku();
        b.setIsbn(txtIsbn.getText());
        b.setJudul(txtJudul.getText());
        b.setStok(Integer.parseInt(txtStok.getText()));
        b.setIdKategori(Integer.parseInt(txtIdKategori.getText()));

        bukuModel.addBuku(b);
        JOptionPane.showMessageDialog(this, "Buku berhasil disimpan!");
        bersihkanForm();
        loadData();
    }

    private void updateBuku() {
        if (selectedIsbn == null) return;
        Buku b = new Buku();
        b.setIsbn(txtIsbn.getText());
        b.setJudul(txtJudul.getText());
        b.setStok(Integer.parseInt(txtStok.getText()));
        b.setIdKategori(Integer.parseInt(txtIdKategori.getText()));

        bukuModel.updateBuku(b);
        JOptionPane.showMessageDialog(this, "Data diperbarui!");
        bersihkanForm();
        loadData();
    }

    private void hapusBuku() {
        if (selectedIsbn == null) return;
        int conf = JOptionPane.showConfirmDialog(this, "Hapus buku ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (conf == JOptionPane.YES_OPTION) {
            bukuModel.deleteBuku(selectedIsbn);
            bersihkanForm();
            loadData();
        }
    }

    private void cariBuku() {
        String key = txtCari.getText().toLowerCase();
        tableModel.setRowCount(0);
        for (Buku b : bukuModel.getAllBuku()) {
            if (b.getJudul().toLowerCase().contains(key) || b.getIsbn().toLowerCase().contains(key)) {
                tableModel.addRow(new Object[]{b.getIsbn(), b.getJudul(), b.getStok(), b.getIdKategori()});
            }
        }
    }

    private void bersihkanForm() {
        selectedIsbn = null;
        txtIsbn.setText("");
        txtJudul.setText("");
        txtStok.setText("");
        txtIdKategori.setText("");
        txtIsbn.setEditable(true);
        txtIsbn.setBackground(Color.WHITE);
        tableBuku.clearSelection();
    }
}