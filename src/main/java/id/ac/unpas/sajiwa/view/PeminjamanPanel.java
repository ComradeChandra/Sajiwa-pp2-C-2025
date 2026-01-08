package id.ac.unpas.sajiwa.view;

import id.ac.unpas.sajiwa.controller.PeminjamanController;
import id.ac.unpas.sajiwa.model.Peminjaman;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PeminjamanPanel extends JPanel {
    
    private JComboBox<String> cmbAnggota;
    private JComboBox<String> cmbBuku;
    // [TAMBAHAN] Fitur Search & Filter
    private JComboBox<String> cmbFilterStatus;
    private JTextField txtCari;
    private JButton btnCari;

    private JSpinner spinnerTanggal;
    private JButton btnPinjam, btnKembali, btnRefresh;
    private JTable tablePeminjaman;
    private DefaultTableModel tableModel;
    
    // Controller
    @SuppressWarnings("unused")
    private PeminjamanController controller;

    public PeminjamanPanel() {
        initComponents();
        // Init Controller (Pastikan Controller dibuat SETELAH components siap)
        // Controller akan mengisi data ComboBox saat inisialisasi
        this.controller = new PeminjamanController(this);
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(230, 242, 255));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // TITLE
        JLabel lblTitle = new JLabel("🔄 Transaksi Peminjaman & Pengembalian");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblTitle.setForeground(new Color(25, 42, 86));
        add(lblTitle, BorderLayout.NORTH);
        
        // CENTER
        JPanel panelCenter = new JPanel(new BorderLayout(15, 15));
        panelCenter.setOpaque(false);
        
        // FORM
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBackground(Color.WHITE);
        panelForm.setBorder(BorderFactory.createTitledBorder(" Form Peminjaman "));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Anggota
        gbc.gridx = 0; gbc.gridy = 0;
        panelForm.add(new JLabel("Pilih Anggota :"), gbc);
        gbc.gridx = 1;
        cmbAnggota = new JComboBox<>();
        cmbAnggota.setPreferredSize(new Dimension(250, 30));
        panelForm.add(cmbAnggota, gbc);
        
        // Buku
        gbc.gridx = 0; gbc.gridy = 1;
        panelForm.add(new JLabel("Pilih Buku :"), gbc);
        gbc.gridx = 1;
        cmbBuku = new JComboBox<>();
        cmbBuku.setPreferredSize(new Dimension(250, 30));
        panelForm.add(cmbBuku, gbc);
        
        // Tanggal
        gbc.gridx = 0; gbc.gridy = 2;
        panelForm.add(new JLabel("Tanggal Pinjam :"), gbc);
        gbc.gridx = 1;
        spinnerTanggal = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(spinnerTanggal, "yyyy-MM-dd");
        spinnerTanggal.setEditor(dateEditor);
        panelForm.add(spinnerTanggal, gbc);
        
        // Buttons
        JPanel panelBtn = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBtn.setOpaque(false);
        btnPinjam = new JButton("➡️ Pinjam Buku");
        btnPinjam.setBackground(new Color(46, 204, 113));
        btnPinjam.setForeground(Color.WHITE);
        
        btnKembali = new JButton("⬅️ Kembalikan Buku");
        btnKembali.setBackground(new Color(230, 126, 34));
        btnKembali.setForeground(Color.WHITE);
        
        btnRefresh = new JButton("🔄 Refresh");
        btnRefresh.setBackground(new Color(52, 152, 219));
        btnRefresh.setForeground(Color.WHITE);
        
        panelBtn.add(btnPinjam);
        panelBtn.add(btnKembali);
        panelBtn.add(btnRefresh);
        
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        panelForm.add(panelBtn, gbc);
        
        // TABLE
        String[] columns = {"ID", "Peminjam", "Buku", "Tgl Pinjam", "Tgl Kembali", "Status", "ISBN", "ID Anggota"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tablePeminjaman = new JTable(tableModel);
        tablePeminjaman.setRowHeight(25);
        
        // Hide technical columns
        tablePeminjaman.getColumnModel().getColumn(0).setMinWidth(0);
        tablePeminjaman.getColumnModel().getColumn(0).setMaxWidth(0); // ID
        tablePeminjaman.getColumnModel().getColumn(6).setMinWidth(0);
        tablePeminjaman.getColumnModel().getColumn(6).setMaxWidth(0); // ISBN
        tablePeminjaman.getColumnModel().getColumn(7).setMinWidth(0);
        tablePeminjaman.getColumnModel().getColumn(7).setMaxWidth(0); // ID Anggota
        
        JScrollPane scroll = new JScrollPane(tablePeminjaman);
        
        // [TAMBAHAN] Panel Search & Filter di atas Tabel
        JPanel panelTableContainer = new JPanel(new BorderLayout(5, 5));
        panelTableContainer.setOpaque(false);
        panelTableContainer.setBorder(BorderFactory.createTitledBorder(" Daftar Riwayat Transaksi "));
        
        JPanel panelSearch = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelSearch.setOpaque(false);
        
        panelSearch.add(new JLabel("Status:"));
        cmbFilterStatus = new JComboBox<>(new String[]{"Semua", "Dipinjam", "Dikembalikan"});
        panelSearch.add(cmbFilterStatus);
        
        txtCari = new JTextField(20);
        txtCari.putClientProperty("JTextField.placeholderText", "Cari Nama / Buku...");
        panelSearch.add(txtCari);
        
        btnCari = new JButton("🔍 Cari");
        btnCari.setBackground(new Color(52, 73, 94));
        btnCari.setForeground(Color.WHITE);
        panelSearch.add(btnCari);
        
        panelTableContainer.add(panelSearch, BorderLayout.NORTH);
        panelTableContainer.add(scroll, BorderLayout.CENTER);

        panelCenter.add(panelForm, BorderLayout.NORTH);
        panelCenter.add(panelTableContainer, BorderLayout.CENTER);
        
        add(panelCenter, BorderLayout.CENTER);
    }
    
    // Getters
    public JComboBox<String> getCmbAnggota() { return cmbAnggota; }
    public JComboBox<String> getCmbBuku() { return cmbBuku; }
    public JComboBox<String> getCmbFilterStatus() { return cmbFilterStatus; } // [TAMBAHAN]
    public JTextField getTxtCari() { return txtCari; } // [TAMBAHAN]
    public JButton getBtnCari() { return btnCari; } // [TAMBAHAN]
    public JSpinner getSpinnerTanggal() { return spinnerTanggal; }
    public JButton getBtnPinjam() { return btnPinjam; }
    public JButton getBtnKembali() { return btnKembali; }
    public JButton getBtnRefresh() { return btnRefresh; }
    public JTable getTable() { return tablePeminjaman; }
    public DefaultTableModel getTableModel() { return tableModel; }
    
    public void setTableData(List<Peminjaman> list) {
        tableModel.setRowCount(0);
        for(Peminjaman p : list) {
            tableModel.addRow(new Object[] {
                p.getIdPeminjaman(),
                p.getNimAnggota() + " - " + p.getNamaAnggota(),
                p.getIsbnBuku() + " - " + p.getJudulBuku(),
                p.getTanggalPinjam(),
                p.getTanggalKembali() == null ? "-" : p.getTanggalKembali(),
                p.getStatus(),
                p.getIsbnBuku(),
                p.getIdAnggota()
            });
        }
    }

    /* CATATAN PRIBADI (CHANDRA):
       1. New View: Panel UI untuk form transaksi.
       2. Desain: Menggunakan GridBagLayout supaya form rapi (Label kiri, Input kanan).
       3. Komponen Pintar: Pakai JComboBox untuk pilih Anggota & Buku (User gak perlu hafal ID/ISBN).
       4. Hidden Column: Tabel menyimpan ID Anggota & ISBN di kolom tersembunyi (width=0) buat keperluan coding back-end.
    */
}
