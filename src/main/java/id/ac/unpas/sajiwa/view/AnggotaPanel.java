/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package id.ac.unpas.sajiwa.view;

import id.ac.unpas.sajiwa.controller.AnggotaController;
import id.ac.unpas.sajiwa.model.Anggota;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Panel View untuk mengelola data Anggota
 * Menggunakan konsep MVC (View Pasif)
 * @author Fitriyani Rahmadini (Refactored by Chandra)
 */
public class AnggotaPanel extends JPanel {
    // Komponen UI
    private JTextField txtNim, txtNama, txtCari;
    private JComboBox<String> cmbProdi, cmbStatus;
    private JButton btnSimpan, btnUpdate, btnHapus, btnReset, btnCari, btnExport;
    private JTable tableAnggota;
    private DefaultTableModel tableModel;
    
    // State UI
    private int selectedId = -1; 

    public AnggotaPanel() {
        initComponents();
        // Integrasi MVC: Controller menangani logika
        new AnggotaController(this);
    }

    private void initComponents() {
        // Layout Utama
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(230, 242, 255)); // Background Biru Muda
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // --- HEADER ---
        JLabel lblJudul = new JLabel("👥 Manajemen Data Anggota");
        lblJudul.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblJudul.setForeground(new Color(25, 42, 86)); 
        add(lblJudul, BorderLayout.NORTH);

        // --- PANEL TENGAH ---
        JPanel panelCenter = new JPanel(new BorderLayout(15, 15));
        panelCenter.setOpaque(false);

        // 1. Panel Form Input
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBackground(Color.WHITE);
        panelForm.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(180, 210, 255)),
            " Input Data Mahasiswa ",
            0, 0, new Font("SansSerif", Font.BOLD, 12), new Color(41, 128, 185)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- Input Fields ---
        // NIM
        gbc.gridx = 0; gbc.gridy = 0;
        panelForm.add(createLabel("NIM :"), gbc);
        gbc.gridx = 1;
        txtNim = new JTextField(20);
        styleField(txtNim);
        panelForm.add(txtNim, gbc);

        // Nama
        gbc.gridx = 0; gbc.gridy = 1;
        panelForm.add(createLabel("Nama Lengkap :"), gbc);
        gbc.gridx = 1;
        txtNama = new JTextField(20);
        styleField(txtNama);
        panelForm.add(txtNama, gbc);

        // Prodi (Dropdown)
        gbc.gridx = 0; gbc.gridy = 2;
        panelForm.add(createLabel("Program Studi :"), gbc);
        gbc.gridx = 1;
        String[] listProdi = {"-- Pilih Prodi --", "Teknik Informatika", "Sistem Informasi", "Teknik Industri", "DKV", "Teknologi Pangan"};
        cmbProdi = new JComboBox<>(listProdi);
        panelForm.add(cmbProdi, gbc);

        // Status (Dropdown)
        gbc.gridx = 0; gbc.gridy = 3;
        panelForm.add(createLabel("Status Keanggotaan :"), gbc);
        gbc.gridx = 1;
        String[] listStatus = {"-- Pilih Status --", "Aktif", "Cuti", "Tidak Aktif", "Lulus"};
        cmbStatus = new JComboBox<>(listStatus);
        panelForm.add(cmbStatus, gbc);

        // 2. Panel Tombol Aksi
        JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelButtons.setOpaque(false);

        btnSimpan = createStyledButton("💾 Simpan", new Color(46, 204, 113), Color.WHITE);
        btnUpdate = createStyledButton("✏️ Update", new Color(243, 156, 18), Color.WHITE);
        btnHapus = createStyledButton("🗑️ Hapus", new Color(231, 76, 60), Color.WHITE);
        btnReset = createStyledButton("🔄 Reset", new Color(52, 152, 219), Color.WHITE);
        btnExport = createStyledButton("📄 Export PDF", new Color(142, 68, 173), Color.WHITE);

        // [TAMBAHAN] Fitur Cetak PDF
        btnExport.addActionListener(e -> {
            new id.ac.unpas.sajiwa.util.ReportService().cetakLaporanAnggota();
        });

        panelButtons.add(btnSimpan);
        panelButtons.add(btnUpdate);
        panelButtons.add(btnHapus);
        panelButtons.add(btnReset);
        panelButtons.add(btnExport);

        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        panelForm.add(panelButtons, gbc);

        // 3. Panel Tabel & Pencarian
        JPanel panelTabel = new JPanel(new BorderLayout(5, 5));
        panelTabel.setOpaque(false);
        panelTabel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEmptyBorder(), "Data Anggota Terdaftar",
            0, 0, new Font("SansSerif", Font.BOLD, 12), new Color(25, 42, 86)
        ));

        // Area Cari
        JPanel panelCari = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelCari.setOpaque(false);
        txtCari = new JTextField(20);
        txtCari.putClientProperty("JTextField.placeholderText", "Cari Nama / NIM...");
        btnCari = createStyledButton("🔍 Cari", new Color(52, 73, 94), Color.WHITE);
        
        panelCari.add(txtCari);
        panelCari.add(btnCari);

        // Tabel
        String[] columns = {"ID", "NIM", "Nama Mahasiswa", "Prodi", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tableAnggota = new JTable(tableModel);
        tableAnggota.setRowHeight(28);
        tableAnggota.setSelectionBackground(new Color(180, 210, 255));
        tableAnggota.setSelectionForeground(Color.BLACK);
        
        // Sembunyikan Kolom ID
        tableAnggota.getColumnModel().getColumn(0).setMinWidth(0);
        tableAnggota.getColumnModel().getColumn(0).setMaxWidth(0);
        tableAnggota.getColumnModel().getColumn(0).setWidth(0);

        JScrollPane scrollPane = new JScrollPane(tableAnggota);
        scrollPane.getViewport().setBackground(Color.WHITE);

        // Listener Klik Tabel
        tableAnggota.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tableAnggota.getSelectedRow() != -1) {
                int row = tableAnggota.getSelectedRow();
                selectedId = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
                txtNim.setText(tableModel.getValueAt(row, 1).toString());
                txtNama.setText(tableModel.getValueAt(row, 2).toString());
                cmbProdi.setSelectedItem(tableModel.getValueAt(row, 3).toString());
                cmbStatus.setSelectedItem(tableModel.getValueAt(row, 4).toString());
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
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 13));
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
        return btn;
    }

    // --- Public Getters (Untuk Controller) ---
    public JTextField getTxtNim() { return txtNim; }
    public JTextField getTxtNama() { return txtNama; }
    public JTextField getTxtCari() { return txtCari; }
    public JComboBox<String> getCmbProdi() { return cmbProdi; }
    public JComboBox<String> getCmbStatus() { return cmbStatus; }
    
    public JButton getBtnSimpan() { return btnSimpan; }
    public JButton getBtnUpdate() { return btnUpdate; }
    public JButton getBtnHapus() { return btnHapus; }
    public JButton getBtnExport() { return btnExport; }
    public JButton getBtnReset() { return btnReset; }
    public JButton getBtnCari() { return btnCari; }
    
    public int getSelectedId() { return selectedId; }

    public void setTableData(List<Anggota> list) {
        tableModel.setRowCount(0);
        for (Anggota a : list) {
            tableModel.addRow(new Object[]{
                a.getIdAnggota(), a.getNim(), a.getNamaMahasiswa(), a.getProgramStudi(), a.getStatusAnggota()
            });
        }
    }

    public void clearForm() {
        selectedId = -1;
        txtNim.setText("");
        txtNama.setText("");
        cmbProdi.setSelectedIndex(0);
        cmbStatus.setSelectedIndex(0);
        txtCari.setText("");
        tableAnggota.clearSelection();
    }
}
