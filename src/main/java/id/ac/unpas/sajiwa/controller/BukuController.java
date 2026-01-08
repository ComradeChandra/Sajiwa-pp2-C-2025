package id.ac.unpas.sajiwa.controller;

import id.ac.unpas.sajiwa.model.Buku;
import id.ac.unpas.sajiwa.model.BukuModel;
import id.ac.unpas.sajiwa.view.BukuPanel;
import id.ac.unpas.sajiwa.model.KategoriBukuModel;
import javax.swing.*;
import java.util.List;

public class BukuController {
    
    private BukuPanel view;
    private BukuModel model;
    private KategoriBukuModel kategoriModel;


    public BukuController(BukuPanel view) {
        this.view = view;
        this.model = new BukuModel();
        this.kategoriModel = new KategoriBukuModel();
        
        initController();
    }
    
    private void initController() {
        view.getBtnSimpan().addActionListener(e -> simpanBuku());
        view.getBtnUpdate().addActionListener(e -> updateBuku());
        view.getBtnHapus().addActionListener(e -> hapusBuku());
        view.getBtnReset().addActionListener(e -> resetForm());
        view.getBtnCari().addActionListener(e -> cariBuku());
        view.getBtnRefresh().addActionListener(e -> refreshTable());
        
        refreshTable();
    }
    
    private void refreshTable() {
        // Ganti findAll() jadi getAllBuku() (Sesuai punya Murod)
        List<Buku> list = model.getAllBuku(); 
        view.setTableData(list);
    }
    
    private void simpanBuku() {
        // Validasi
        if (view.getTxtIsbn().getText().isEmpty() ||
                view.getTxtJudul().getText().isEmpty() ||
                view.getTxtIdKategori().getText().isEmpty()) {

            JOptionPane.showMessageDialog(view,
                    "ISBN, Judul, dan ID Kategori wajib diisi!");
            return;
        }
        
        try {
            Buku buku = new Buku();
            buku.setIsbn(view.getTxtIsbn().getText());
            buku.setJudul(view.getTxtJudul().getText());

            // Karena di Panel Fitri cuma ada Stok, kita set Stok aja
            int stok = Integer.parseInt(view.getTxtStok().getText());
            buku.setStok(stok);

            int idKategori = Integer.parseInt(view.getTxtIdKategori().getText());

            if (!kategoriModel.existsById(idKategori)) {
                JOptionPane.showMessageDialog(view,
                        "ID Kategori tidak ditemukan di database!",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            buku.setIdKategori(idKategori);
            
            // Data lain set default dulu (karena Panel belum lengkap)
            buku.setPengarang("-");
            buku.setPenerbit("-");
            buku.setTahunTerbit(2024);
            // buku.setIdKategori(...) // Kalau mau ditambahin nanti

            // Panggil Model (Tanpa IF karena methodnya void)
            model.addBuku(buku);
            
            JOptionPane.showMessageDialog(view, "Buku Berhasil Disimpan!");
            resetForm();
            refreshTable();
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view, "Stok harus angka!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Gagal Simpan: " + e.getMessage());
        }
    }
    
    private void updateBuku() {
        if (view.getSelectedIsbn() == null) {
            JOptionPane.showMessageDialog(view, "Pilih buku di tabel dulu!");
            return;
        }

        if (view.getTxtIdKategori().getText().isEmpty()) {
            JOptionPane.showMessageDialog(view, "ID Kategori wajib diisi!");
            return;
        }

        try {
            Buku buku = new Buku();
            buku.setIsbn(view.getTxtIsbn().getText());
            buku.setJudul(view.getTxtJudul().getText());
            buku.setStok(Integer.parseInt(view.getTxtStok().getText()));

            int idKategori = Integer.parseInt(view.getTxtIdKategori().getText());

            if (!kategoriModel.existsById(idKategori)) {
                JOptionPane.showMessageDialog(view,
                        "ID Kategori tidak ditemukan di database!",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            buku.setIdKategori(idKategori);
            
            // Set default biar gak null
            buku.setPengarang("-");
            buku.setPenerbit("-");
            buku.setTahunTerbit(2024);

            model.updateBuku(buku);
            
            JOptionPane.showMessageDialog(view, "Buku Berhasil Diupdate!");
            resetForm();
            refreshTable();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Gagal Update: " + e.getMessage());
        }
    }
    
    private void hapusBuku() {
        String isbn = view.getSelectedIsbn();
        if (isbn == null) {
            JOptionPane.showMessageDialog(view, "Pilih buku yang mau dihapus!");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(view, "Yakin hapus buku ISBN: " + isbn + "?");
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                model.deleteBuku(isbn);
                JOptionPane.showMessageDialog(view, "Buku Terhapus!");
                resetForm();
                refreshTable();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(view, "Gagal Hapus: " + e.getMessage());
            }
        }
    }
    
    private void cariBuku() {
        String keyword = view.getTxtCari().getText().toLowerCase();
        
        // Ambil semua data dari model
        List<Buku> allBuku = model.getAllBuku();
        
        // Jika keyword kosong, tampilkan semua
        if (keyword.isEmpty()) {
            view.setTableData(allBuku);
            return;
        }
        
        // Filter manual (Client-side filtering)
        List<Buku> filtered = allBuku.stream()
                .filter(b -> b.getJudul().toLowerCase().contains(keyword) || 
                             b.getIsbn().toLowerCase().contains(keyword) ||
                             b.getNamaKategori().toLowerCase().contains(keyword))
                .toList();
        
        // Update tabel
        view.setTableData(filtered);
        
        if (filtered.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Data tidak ditemukan!");
        }
    }
    
    private void resetForm() {
        view.clearForm();
    }

    /* CATATAN PRIBADI (CHANDRA):
       1. Refactoring: Memindahkan logika CRUD Buku dari View ke sini (MVC Pattern).
       2. Search Logic: Implementasi fitur "Cari Buku" menggunakan Java Stream Filter.
          - Kelebihan: Cepat, tidak perlu query DB berulang-ulang.
          - Cara kerja: Load semua data -> Filter di memori berdasarkan Judul/ISBN/Kategori -> Update Tabel.
       3. Validasi: Menjaga agar stok buku yang diinput user harus berupa angka (NumberFormatException handling).
    */
}