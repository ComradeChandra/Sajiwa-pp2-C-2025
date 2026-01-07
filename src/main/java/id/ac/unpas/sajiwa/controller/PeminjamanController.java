package id.ac.unpas.sajiwa.controller;

import id.ac.unpas.sajiwa.model.*;
import id.ac.unpas.sajiwa.view.PeminjamanPanel;
import javax.swing.*;
import java.util.Date;
import java.util.List;

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
        });
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
