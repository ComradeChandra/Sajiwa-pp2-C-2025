package id.ac.unpas.sajiwa.view;

import id.ac.unpas.sajiwa.database.KoneksiDB;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserHistoryPanel extends JPanel {
    private String username;
    private boolean onlyActiveLoans; // True = Sedang Dipinjam, False = Semua Histori
    private JTable table;
    private DefaultTableModel tableModel;

    public UserHistoryPanel(String username, boolean onlyActiveLoans) {
        this.username = username;
        this.onlyActiveLoans = onlyActiveLoans;
        initComponents();
        loadData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(230, 242, 255));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // Title
        String titleText = onlyActiveLoans ? "📖 Buku Sedang Dipinjam" : "📜 Riwayat Peminjaman Saya";
        JLabel lblTitle = new JLabel(titleText);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblTitle.setForeground(new Color(25, 42, 86));
        add(lblTitle, BorderLayout.NORTH);

        // Table
        String[] columns = {"Judul Buku", "Tanggal Pinjam", "Tenggat Kembali", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
        
        // Refresh Button
        JButton btnRefresh = new JButton("Refresh Data");
        btnRefresh.addActionListener(e -> loadData());
        add(btnRefresh, BorderLayout.SOUTH);
    }

    private void loadData() {
        tableModel.setRowCount(0); // Reset data
        
       
        String sql = "SELECT b.judul, p.tanggal_pinjam, p.tanggal_kembali, p.status " +
                     "FROM peminjaman p " +
                     "JOIN buku b ON p.isbn_buku = b.isbn " +
                     "JOIN anggota a ON p.id_anggota = a.id_anggota " +
                     "WHERE a.nama_mahasiswa = ?";
        
        if (onlyActiveLoans) {
            sql += " AND p.status = 'Dipinjam'";
        } else {
            sql += " ORDER BY p.tanggal_pinjam DESC";
        }

        try (Connection conn = KoneksiDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, username);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String judul = rs.getString("judul"); // Changed from judul_buku
                    String tglPinjam = rs.getString("tanggal_pinjam");
                    String tglKembali = rs.getString("tanggal_kembali");
                    String status = rs.getString("status");
                    
                    if (tglKembali == null) tglKembali = "-";

                    tableModel.addRow(new Object[]{judul, tglPinjam, tglKembali, status});
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal memuat data: " + e.getMessage());
        }
    }
}

/*
 * ==================================================================================
 * CATATAN PRIBADI (CHANDRA)
 * ==================================================================================
 * 1. Purpose (Fitur Mahasiswa):
 *    - Panel ini khusus dibuat buat Mahasiswa. Mereka butuh liat status buku apa aja
 *      yang lagi mereka pinjam atau histori peminjaman mereka sebelumnya.
 *
 * 2. SQL JOIN Strategy:
 *    - Query di sini lumayan kompleks karena nge-JOIN 3 tabel sekaligus:
 *      [Peminjaman] -> [Buku] (via isbn) -> [Anggota] (via id_anggota).
 *    - Kenapa? Karena di tabel Peminjaman cuma ada ID/ISBN, sementara kita mau nampilin
 *      JUDUL BUKU (ada di tabel Buku) dan filter berdasarkan NAMA USER yang login 
 *      (ada di tabel Anggota).
 *
 * 3. Dynamic Filtering:
 *    - Ada parameter boolean 'onlyActiveLoans'.
 *    - Kalau true -> Tambah klausa "WHERE status = 'Dipinjam'" (Menu "Sedang Dipinjam").
 *    - Kalau false -> Tampilin semua histori (Menu "Riwayat Saya").
 *    - Jadi satu file class ini bisa dipake buat dua menu berbeda. Hemat kode! :D
 * ==================================================================================
 */
