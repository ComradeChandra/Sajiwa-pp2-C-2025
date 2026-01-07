package id.ac.unpas.sajiwa.controller;

import id.ac.unpas.sajiwa.model.KategoriBuku;
import id.ac.unpas.sajiwa.model.KategoriBukuModel;
import id.ac.unpas.sajiwa.view.KategoriPanel;
import java.util.List;
import javax.swing.JOptionPane;

/**
 * Controller untuk mengelola interaksi antara KategoriPanel (View) dan KategoriBukuModel (Model)
 */
public class KategoriBukuController {
    private final KategoriPanel view;
    private final KategoriBukuModel model;

    public KategoriBukuController(KategoriPanel view) {
        this.view = view;
        this.model = new KategoriBukuModel();
        
        initView();
        loadData();
    }

    private void initView() {
        view.getBtnSimpan().addActionListener(e -> simpanData());
        view.getBtnUpdate().addActionListener(e -> updateData());
        view.getBtnHapus().addActionListener(e -> hapusData());
        view.getBtnReset().addActionListener(e -> view.resetForm());
        view.getBtnCari().addActionListener(e -> cariData());
    }

    public void loadData() {
        List<KategoriBuku> list = model.getAllKategori();
        view.setTableData(list);
    }

    private void simpanData() {
        String nama = view.getTxtNamaKategori().getText().trim();
        if (nama.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Nama Kategori wajib diisi!");
            return;
        }

        KategoriBuku k = new KategoriBuku(0, nama);
        model.addKategori(k);
        
        JOptionPane.showMessageDialog(view, "Kategori Berhasil Disimpan!");
        view.resetForm();
        loadData();
    }

    private void updateData() {
        int selectedId = view.getSelectedId();
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(view, "Pilih data yang ingin diubah!");
            return;
        }

        String nama = view.getTxtNamaKategori().getText().trim();
        if (nama.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Nama Kategori wajib diisi!");
            return;
        }

        KategoriBuku k = new KategoriBuku(selectedId, nama);
        model.updateKategori(k);

        JOptionPane.showMessageDialog(view, "Kategori Berhasil Diperbarui!");
        view.resetForm();
        loadData();
    }

    private void hapusData() {
        int selectedId = view.getSelectedId();
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(view, "Pilih data yang ingin dihapus!");
            return;
        }

        int kf = JOptionPane.showConfirmDialog(view, "Hapus kategori ini?", "Hapus", JOptionPane.YES_NO_OPTION);
        if (kf == JOptionPane.YES_OPTION) {
            model.deleteKategori(selectedId);
            JOptionPane.showMessageDialog(view, "Kategori Berhasil Dihapus!");
            view.resetForm();
            loadData();
        }
    }

    private void cariData() {
        String keyword = view.getTxtCari().getText().toLowerCase();
        if (keyword.isEmpty()) {
            loadData();
            return;
        }

        List<KategoriBuku> allData = model.getAllKategori();
        List<KategoriBuku> filtered = allData.stream()
                .filter(k -> k.getNamaKategori().toLowerCase().contains(keyword))
                .toList();

        view.setTableData(filtered);
    }

    /* CATATAN PRIBADI (CHANDRA):
       1. New Controller: Dibuat khusus untuk misahin logika dari KategoriPanel (punya Fitri).
       2. Integrasi: Menghubungkan KategoriPanel (View) dengan KategoriBukuModel (Model).
       3. Error Handling: Menambahkan feedback JOptionPane ke user lewat view biar interaktif.
    */
}
