package id.ac.unpas.sajiwa.view;

import id.ac.unpas.sajiwa.controller.KategoriBukuController;
import id.ac.unpas.sajiwa.model.KategoriBuku;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class KategoriPanel extends JPanel {
    // Komponen UI
    private JTextField txtId, txtNamaKategori, txtCari;
    private JButton btnSimpan, btnUpdate, btnHapus, btnReset, btnCari;
    private JTable tableKategori;
    private DefaultTableModel tableModel;
    
    // State UI
    private int selectedId = -1;

    public KategoriPanel() {
        initComponents();
        // Integrasi MVC
        new KategoriBukuController(this);
    }

    private void initComponents() {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(230, 242, 255)); 
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // --- HEADER ---
        JLabel lblJudul = new JLabel("🏷️ Manajemen Kategori Buku");
        lblJudul.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblJudul.setForeground(new Color(25, 42, 86)); 
        add(lblJudul, BorderLayout.NORTH);

        // --- PANEL TENGAH (FORM + TABEL) ---
        JPanel panelCenter = new JPanel(new BorderLayout(15, 15));
        panelCenter.setOpaque(false);

        // 1. Panel Form Input
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBackground(Color.WHITE);
        panelForm.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(180, 210, 255)),
            " Input Kategori ",
            0, 0, new Font("SansSerif", Font.BOLD, 12), new Color(41, 128, 185)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- Input Fields ---
        
        // ID Kategori (Disabled - Auto)
        gbc.gridx = 0; gbc.gridy = 0;
        panelForm.add(createLabel("ID Kategori :"), gbc);
        gbc.gridx = 1;
        txtId = new JTextField(5);
        styleField(txtId);
        txtId.setEditable(false); // ID Auto Increment
        panelForm.add(txtId, gbc);

        // Nama Kategori
        gbc.gridx = 0; gbc.gridy = 1;
        panelForm.add(createLabel("Nama Kategori :"), gbc);
        gbc.gridx = 1;
        txtNamaKategori = new JTextField(25);
        styleField(txtNamaKategori);
        panelForm.add(txtNamaKategori, gbc);

        // 2. Panel Tombol Aksi
        JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelButtons.setOpaque(false);

        btnSimpan = createStyledButton("💾 Simpan", new Color(46, 204, 113), Color.WHITE);
        btnUpdate = createStyledButton("✏️ Update", new Color(243, 156, 18), Color.WHITE);
        btnHapus = createStyledButton("🗑️ Hapus", new Color(231, 76, 60), Color.WHITE);
        btnReset = createStyledButton("🔄 Reset", new Color(52, 152, 219), Color.WHITE);

        panelButtons.add(btnSimpan);
        panelButtons.add(btnUpdate);
        panelButtons.add(btnHapus);
        panelButtons.add(btnReset);

        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        panelForm.add(panelButtons, gbc);

        // 3. Panel Tabel
        JPanel panelTabel = new JPanel(new BorderLayout(5, 5));
        panelTabel.setOpaque(false);
        panelTabel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEmptyBorder(), "Data Master Kategori",
            0, 0, new Font("SansSerif", Font.BOLD, 12), new Color(25, 42, 86)
        ));

        // Search Bar
        JPanel panelCari = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelCari.setOpaque(false);
        txtCari = new JTextField(20);
        txtCari.putClientProperty("JTextField.placeholderText", "Cari Kategori...");
        btnCari = createStyledButton("🔍 Cari", new Color(52, 73, 94), Color.WHITE);
        panelCari.add(txtCari);
        panelCari.add(btnCari);

        // Tabel Setup
        String[] columns = {"ID", "Nama Kategori"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        
        tableKategori = new JTable(tableModel);
        tableKategori.setRowHeight(30);
        tableKategori.setSelectionBackground(new Color(180, 210, 255));
        tableKategori.setSelectionForeground(Color.BLACK);
        tableKategori.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        
        // Kita tampilkan kolom ID
        tableKategori.getColumnModel().getColumn(0).setPreferredWidth(50);
        tableKategori.getColumnModel().getColumn(0).setMaxWidth(100);

        JScrollPane scrollPane = new JScrollPane(tableKategori);
        scrollPane.getViewport().setBackground(Color.WHITE);

        // Selection Listener
        tableKategori.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tableKategori.getSelectedRow() != -1) {
                int row = tableKategori.getSelectedRow();
                selectedId = (int) tableModel.getValueAt(row, 0);
                txtId.setText(String.valueOf(selectedId));
                txtNamaKategori.setText(tableModel.getValueAt(row, 1).toString());
            }
        });

        panelTabel.add(panelCari, BorderLayout.NORTH);
        panelTabel.add(scrollPane, BorderLayout.CENTER);

        panelCenter.add(panelForm, BorderLayout.NORTH);
        panelCenter.add(panelTabel, BorderLayout.CENTER);

        add(panelCenter, BorderLayout.CENTER);
    }

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

    // --- Getters for Controller ---
    public JTextField getTxtNamaKategori() { return txtNamaKategori; }
    public JTextField getTxtCari() { return txtCari; }
    
    public JButton getBtnSimpan() { return btnSimpan; }
    public JButton getBtnUpdate() { return btnUpdate; }
    public JButton getBtnHapus() { return btnHapus; }
    public JButton getBtnReset() { return btnReset; }
    public JButton getBtnCari() { return btnCari; }
    
    public int getSelectedId() { return selectedId; }

    public void setTableData(List<KategoriBuku> list) {
        tableModel.setRowCount(0);
        for(KategoriBuku k : list) {
            tableModel.addRow(new Object[]{k.getIdKategori(), k.getNamaKategori()});
        }
    }

    public void resetForm() {
        selectedId = -1;
        txtId.setText("Auto");
        txtNamaKategori.setText("");
        tableKategori.clearSelection();
    }
}