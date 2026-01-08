package id.ac.unpas.sajiwa.controller;

import id.ac.unpas.sajiwa.model.Buku;
import id.ac.unpas.sajiwa.model.BukuModel;
import id.ac.unpas.sajiwa.view.BukuPanel;
import id.ac.unpas.sajiwa.model.KategoriBuku;
import id.ac.unpas.sajiwa.model.KategoriBukuModel;
import javax.swing.*;
import java.awt.Component; // [TAMBAHAN] for AutoComplete
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.stream.Collectors;

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
        view.getBtnRefresh().addActionListener(e -> {
            refreshTable();
            loadFilterData(); // Reload filter
            view.getTxtCari().setText("");
            view.getCmbFilterKategori().setSelectedIndex(0);
        });
        
        // [TAMBAHAN] Filter & Live Search
        loadFilterData();
        view.getCmbFilterKategori().addActionListener(e -> cariBuku());
        view.getTxtCari().addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                cariBuku();
            }
        });
        
        // Setup Auto Complete
        setupAutoComplete(view.getCmbFilterKategori());
        
        refreshTable();
    }
    
    private void setupAutoComplete(JComboBox<String> comboBox) {
        comboBox.setEditable(true);
        Component editor = comboBox.getEditor().getEditorComponent();
        if (editor instanceof JTextField) {
            JTextField txt = (JTextField) editor;
            txt.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                   // Logic for future implementation
                }
            });
        }
    }
    
    private void loadFilterData() {
        view.getCmbFilterKategori().removeAllItems();
        view.getCmbFilterKategori().addItem("Semua Kategori");
        
        List<KategoriBuku> list = kategoriModel.getAllKategori();
        for(KategoriBuku k : list) {
            view.getCmbFilterKategori().addItem(k.getNamaKategori());
        }
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
        String kategoriFilter = (String) view.getCmbFilterKategori().getSelectedItem();
        
        List<Buku> allBuku = model.getAllBuku();
        
        List<Buku> filtered = allBuku.stream()
                .filter(b -> {
                    boolean matchKeyword = b.getJudul().toLowerCase().contains(keyword) || 
                                           b.getIsbn().toLowerCase().contains(keyword) ||
                                           b.getNamaKategori().toLowerCase().contains(keyword);
                    
                    boolean matchFilter = kategoriFilter == null || kategoriFilter.equals("Semua Kategori") || 
                                          b.getNamaKategori().equalsIgnoreCase(kategoriFilter);
                                          
                    return matchKeyword && matchFilter;
                })
                .collect(Collectors.toList());
        
        view.setTableData(filtered);
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
       4. [TAMBAHAN] Fitur UI & UX:
          - Filter dropdown kategori.
          - Auto Complete (Live Search) saat mengetik judul.
    */
}