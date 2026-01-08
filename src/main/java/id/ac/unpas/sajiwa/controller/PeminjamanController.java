package id.ac.unpas.sajiwa.controller;

import id.ac.unpas.sajiwa.model.*;
import id.ac.unpas.sajiwa.view.PeminjamanPanel;
import javax.swing.*;
import java.awt.Component; // [FIX] Import Component
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class PeminjamanController {
    
    private PeminjamanPanel view;
    private PeminjamanModel model;
    private AnggotaModel anggotaModel;
    private BukuModel bukuModel;
    
    public PeminjamanController(PeminjamanPanel view) {
        this.view = view;
        this.model = new PeminjamanModel();
        this.anggotaModel = new AnggotaModel();
        this.bukuModel = new BukuModel();
        
        initController();
        loadComboData();
        refreshTable();
    }
    
    private void initController() {
        view.getBtnPinjam().addActionListener(e -> pinjamBuku());
        view.getBtnKembali().addActionListener(e -> kembalikanBuku());
        view.getBtnRefresh().addActionListener(e -> {
            loadComboData();
            refreshTable();
            view.getTxtCari().setText("");
            view.getCmbFilterStatus().setSelectedIndex(0);
        });
        
        // [TAMBAHAN] Fitur Search & Filter
        view.getBtnCari().addActionListener(e -> cariData());
        view.getCmbFilterStatus().addActionListener(e -> cariData());
        
        // Auto Complete / Live Search
        view.getTxtCari().addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                cariData();
            }
        });
        
        // Setup Auto Complete for ComboBox components (Search Suggestion)
        setupAutoComplete(view.getCmbAnggota());
        setupAutoComplete(view.getCmbBuku());
    }
    
    private void setupAutoComplete(JComboBox<String> comboBox) {
        // Simple ComboBox Editor Search
        comboBox.setEditable(true);
        Component editor = comboBox.getEditor().getEditorComponent();
        if (editor instanceof JTextField) {
            JTextField txt = (JTextField) editor;
            txt.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    // Fitur ini kompleks jika full implementasi, 
                    // Basic: Membiarkan user mengetik untuk filter default swing (jika ada)
                    // Atau: Custom Popup. 
                    // Untuk saat ini kita biarkan default editable agar user bisa ketik manual.
                }
            });
        }
    }

    private void cariData() {
        String keyword = view.getTxtCari().getText().toLowerCase();
        String statusFilter = (String) view.getCmbFilterStatus().getSelectedItem();
        
        List<Peminjaman> allData = model.getAllPeminjaman();
        
        List<Peminjaman> filtered = allData.stream()
            .filter(p -> {
                boolean matchName = p.getNamaAnggota().toLowerCase().contains(keyword);
                boolean matchBook = p.getJudulBuku().toLowerCase().contains(keyword);
                boolean matchStatus = statusFilter.equals("Semua") || p.getStatus().equalsIgnoreCase(statusFilter);
                
                return (matchName || matchBook) && matchStatus;
            })
            .collect(Collectors.toList());
            
        view.setTableData(filtered);
    }
    
    private void loadComboData() {
        // Load Anggota
        view.getCmbAnggota().removeAllItems();
        List<Anggota> listAnggota = anggotaModel.getAllAnggota();
        for(Anggota a : listAnggota) {
            view.getCmbAnggota().addItem(a.getIdAnggota() + " - " + a.getNamaMahasiswa());
        }
        
        // Load Buku
        view.getCmbBuku().removeAllItems();
        List<Buku> listBuku = bukuModel.getAllBuku();
        for(Buku b : listBuku) {
            view.getCmbBuku().addItem(b.getIsbn() + " - " + b.getJudul() + " (Stok: " + b.getStok() + ")");
        }
    }
    
    private void refreshTable() {
        List<Peminjaman> list = model.getAllPeminjaman();
        view.setTableData(list);
    }
    
    private void pinjamBuku() {
        try {
            // Get Selected Anggota ID
            String selectedAnggota = (String) view.getCmbAnggota().getSelectedItem();
            if(selectedAnggota == null) {
                JOptionPane.showMessageDialog(view, "Pilih Anggota dulu!");
                return;
            }
            int idAnggota = Integer.parseInt(selectedAnggota.split(" - ")[0]);
            
            // Get Selected Buku ISBN
            String selectedBuku = (String) view.getCmbBuku().getSelectedItem();
            if(selectedBuku == null) {
                JOptionPane.showMessageDialog(view, "Pilih Buku dulu!");
                return;
            }
            String isbn = selectedBuku.split(" - ")[0];
            
            // Cek Stok (Simple logic string parsing or re-query)
            if(selectedBuku.contains("(Stok: 0)")) {
                JOptionPane.showMessageDialog(view, "Stok buku habis!");
                return;
            }
            
            // Get Date
            Date utilDate = (Date) view.getSpinnerTanggal().getValue();
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
            
            Peminjaman p = new Peminjaman();
            p.setIdAnggota(idAnggota);
            p.setIsbnBuku(isbn);
            p.setTanggalPinjam(sqlDate);
            
            model.addPeminjaman(p);
            
            JOptionPane.showMessageDialog(view, "Peminjaman Berhasil!");
            refreshTable();
            loadComboData(); // Reload stok
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(view, "Gagal Pinjam: " + e.getMessage());
        }
    }
    
    private void kembalikanBuku() {
        int selectedRow = view.getTable().getSelectedRow();
        if(selectedRow == -1) {
            JOptionPane.showMessageDialog(view, "Pilih transaksi yang ingin dikembalikan!");
            return;
        }
        
        String status = (String) view.getTableModel().getValueAt(selectedRow, 5);
        if("Dikembalikan".equals(status)) {
            JOptionPane.showMessageDialog(view, "Buku sudah dikembalikan!");
            return;
        }
        
        int idPeminjaman = (int) view.getTableModel().getValueAt(selectedRow, 0);
        String isbn = (String) view.getTableModel().getValueAt(selectedRow, 6);
        
        // Tanggal kembali hari ini
        java.sql.Date tglKembali = new java.sql.Date(System.currentTimeMillis());
        
        model.kembalikanBuku(idPeminjaman, tglKembali, isbn);
        
        JOptionPane.showMessageDialog(view, "Buku Dikembalikan!");
        refreshTable();
        loadComboData(); // Reload stok
    }

    /* CATATAN PRIBADI (CHANDRA):
       1. New Controller: Menghandle logika Transaksi (Peminjaman & Pengembalian).
       2. Logika Unit:
          - Pinjam: Cek stok buku, jika ada -> insert transaksi -> kurangi stok (-1).
          - Kembali: Update status transaksi -> update tanggal kembali -> tambah stok (+1).
       3. Integrasi: Menggabungkan AnggotaModel (untuk pilih peminjam) dan BukuModel (untuk pilih buku).
       4. Auto Reload: Setiap aksi berhasil, data combo box & tabel di-refresh biar stok selalu update.
    */
}
