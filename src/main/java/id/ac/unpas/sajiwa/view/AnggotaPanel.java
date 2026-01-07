/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package id.ac.unpas.sajiwa.view;


import id.ac.unpas.sajiwa.model.Anggota;
import id.ac.unpas.sajiwa.model.AnggotaModel;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 *
 * @author Fitriyani Rahmadini
 */
public class AnggotaPanel extends JPanel {
    // Komponen UI
    private JTextField txtNim, txtNama, txtCari;
    private JComboBox<String> cmbProdi, cmbStatus;
    private JTable tableAnggota;
    private DefaultTableModel tableModel;
    
    // Model & Data Helper
    private AnggotaModel anggotaModel;
    private int selectedId = -1; // Untuk menyimpan ID Anggota saat diedit (Primary Key)

    public AnggotaPanel() {
        anggotaModel = new AnggotaModel();
        initComponents();
        loadData(); 
    }

    private void initComponents() {
        // Layout Utama
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(230, 242, 255)); // Background Biru Muda
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // --- HEADER ---
        JLabel lblJudul = new JLabel("👥 Manajemen Data Anggota");
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

        JButton btnSimpan = createStyledButton("💾 Simpan", new Color(46, 204, 113), Color.WHITE);
        JButton btnUpdate = createStyledButton("✏️ Update", new Color(243, 156, 18), Color.WHITE);
        JButton btnHapus = createStyledButton("🗑️ Hapus", new Color(231, 76, 60), Color.WHITE);
        JButton btnReset = createStyledButton("🔄 Reset", new Color(52, 152, 219), Color.WHITE);

        // Event Listeners
        btnSimpan.addActionListener(e -> tambahAnggota());
        btnUpdate.addActionListener(e -> updateAnggota());
        btnHapus.addActionListener(e -> hapusAnggota());
        btnReset.addActionListener(e -> bersihkanForm());

        panelButtons.add(btnSimpan);
        panelButtons.add(btnUpdate);
        panelButtons.add(btnHapus);
        panelButtons.add(btnReset);

        // Masukkan Tombol ke Form Panel (Baris Terakhir)
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
        JButton btnCari = createStyledButton("🔍 Cari", new Color(52, 73, 94), Color.WHITE);
        
        btnCari.addActionListener(e -> cariAnggota());
        
        panelCari.add(txtCari);
        panelCari.add(btnCari);

        // Tabel
        String[] columns = {"ID", "NIM", "Nama Mahasiswa", "Prodi", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tableAnggota = new JTable(tableModel);
        tableAnggota.setRowHeight(28);
        tableAnggota.setSelectionBackground(new Color(180, 210, 255)); // Highlight Biru Muda
        tableAnggota.setSelectionForeground(Color.BLACK);
        
        // Sembunyikan Kolom ID (Kunci Utama) agar tidak mengganggu pemandangan, tapi datanya ada
        tableAnggota.getColumnModel().getColumn(0).setMinWidth(0);
        tableAnggota.getColumnModel().getColumn(0).setMaxWidth(0);
        tableAnggota.getColumnModel().getColumn(0).setWidth(0);

        JScrollPane scrollPane = new JScrollPane(tableAnggota);
        scrollPane.getViewport().setBackground(Color.WHITE);

        // Listener Klik Tabel
        tableAnggota.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tableAnggota.getSelectedRow() != -1) {
                int row = tableAnggota.getSelectedRow();
                // Ambil data dari tabel ke form
                selectedId = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
                txtNim.setText(tableModel.getValueAt(row, 1).toString());
                txtNama.setText(tableModel.getValueAt(row, 2).toString());
                cmbProdi.setSelectedItem(tableModel.getValueAt(row, 3).toString());
                cmbStatus.setSelectedItem(tableModel.getValueAt(row, 4).toString());
            }
        });

        panelTabel.add(panelCari, BorderLayout.NORTH);
        panelTabel.add(scrollPane, BorderLayout.CENTER);

        // Gabungkan
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

    // --- LOGIKA CRUD (Create, Read, Update, Delete) ---

    private boolean validateForm() {
        if (txtNim.getText().trim().isEmpty() || txtNama.getText().trim().isEmpty() ||
            cmbProdi.getSelectedIndex() == 0 || cmbStatus.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi lengkap!", "Validasi", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<Anggota> list = anggotaModel.getAllAnggota();
        for (Anggota a : list) {
            tableModel.addRow(new Object[]{
                a.getIdAnggota(), a.getNim(), a.getNamaMahasiswa(), a.getProgramStudi(), a.getStatusAnggota()
            });
        }
    }
    
    private void cariAnggota() {
        String keyword = txtCari.getText().toLowerCase();
        if(keyword.isEmpty()) { loadData(); return; }
        
        tableModel.setRowCount(0);
        List<Anggota> list = anggotaModel.getAllAnggota();
        for (Anggota a : list) {
            if (a.getNamaMahasiswa().toLowerCase().contains(keyword) || a.getNim().toLowerCase().contains(keyword)) {
                tableModel.addRow(new Object[]{
                    a.getIdAnggota(), a.getNim(), a.getNamaMahasiswa(), a.getProgramStudi(), a.getStatusAnggota()
                });
            }
        }
    }

    private void tambahAnggota() {
        if (!validateForm()) return;
        
        Anggota a = new Anggota();
        a.setNim(txtNim.getText().trim());
        a.setNamaMahasiswa(txtNama.getText().trim());
        a.setProgramStudi(cmbProdi.getSelectedItem().toString());
        a.setStatusAnggota(cmbStatus.getSelectedItem().toString());

        anggotaModel.addAnggota(a);
        JOptionPane.showMessageDialog(this, "Anggota berhasil disimpan!");
        bersihkanForm();
        loadData();
    }

    private void updateAnggota() {
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this, "Pilih anggota dari tabel dulu!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!validateForm()) return;

        Anggota a = new Anggota();
        a.setIdAnggota(selectedId); // PENTING: ID untuk WHERE clause di database
        a.setNim(txtNim.getText().trim());
        a.setNamaMahasiswa(txtNama.getText().trim());
        a.setProgramStudi(cmbProdi.getSelectedItem().toString());
        a.setStatusAnggota(cmbStatus.getSelectedItem().toString());

        anggotaModel.updateAnggota(a);
        JOptionPane.showMessageDialog(this, "Data anggota diperbarui!");
        bersihkanForm();
        loadData();
    }

    private void hapusAnggota() {
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this, "Pilih anggota yang akan dihapus!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, "Yakin hapus anggota ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            anggotaModel.deleteAnggota(selectedId); // Hapus berdasarkan ID
            JOptionPane.showMessageDialog(this, "Data dihapus!");
            bersihkanForm();
            loadData();
        }
    }
    
    

    private void bersihkanForm() {
        selectedId = -1;
        txtNim.setText("");
        txtNama.setText("");
        cmbProdi.setSelectedIndex(0);
        cmbStatus.setSelectedIndex(0);
        txtCari.setText("");
        tableAnggota.clearSelection();
    }
}
