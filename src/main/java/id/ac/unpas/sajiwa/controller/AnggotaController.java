package id.ac.unpas.sajiwa.controller;

import id.ac.unpas.sajiwa.model.Anggota;
import id.ac.unpas.sajiwa.model.AnggotaModel;
import id.ac.unpas.sajiwa.view.AnggotaPanel;
import java.util.List;
import javax.swing.JOptionPane;

/**
 * Controller untuk mengelola interaksi antara AnggotaPanel (View) dan AnggotaModel (Model)
 */
public class AnggotaController {
    private final AnggotaPanel view;
    private final AnggotaModel model;

    public AnggotaController(AnggotaPanel view) {
        this.view = view;
        this.model = new AnggotaModel();
        
        initView();
        loadData();
    }

    private void initView() {
        // Registrasi Event Listener dari View ke Controller
        view.getBtnSimpan().addActionListener(e -> tambahAnggota());
        view.getBtnUpdate().addActionListener(e -> updateAnggota());
        view.getBtnHapus().addActionListener(e -> hapusAnggota());
        view.getBtnReset().addActionListener(e -> view.clearForm());
        view.getBtnCari().addActionListener(e -> cariAnggota());
    }

    public void loadData() {
        List<Anggota> list = model.getAllAnggota();
        view.setTableData(list);
    }

    private void tambahAnggota() {
        // Ambil data dari view
        String nim = view.getTxtNim().getText().trim();
        String nama = view.getTxtNama().getText().trim();
        String prodi = view.getCmbProdi().getSelectedItem().toString();
        String status = view.getCmbStatus().getSelectedItem().toString();

        // Validasi Sederhana
        if (nim.isEmpty() || nama.isEmpty() || view.getCmbProdi().getSelectedIndex() == 0 || view.getCmbStatus().getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(view, "Semua field harus diisi lengkap!", "Validasi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Buat objek model
        Anggota a = new Anggota();
        a.setNim(nim);
        a.setNamaMahasiswa(nama);
        a.setProgramStudi(prodi);
        a.setStatusAnggota(status);

        // Kirim ke Model
        model.addAnggota(a);
        
        // Feedback UI
        JOptionPane.showMessageDialog(view, "Anggota berhasil disimpan!");
        view.clearForm();
        loadData();
    }

    private void updateAnggota() {
        int selectedId = view.getSelectedId();
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(view, "Pilih anggota dari tabel dulu!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nim = view.getTxtNim().getText().trim();
        String nama = view.getTxtNama().getText().trim();
        String prodi = view.getCmbProdi().getSelectedItem().toString();
        String status = view.getCmbStatus().getSelectedItem().toString();

        if (nim.isEmpty() || nama.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Data tidak boleh kosong!", "Validasi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Anggota a = new Anggota();
        a.setIdAnggota(selectedId);
        a.setNim(nim);
        a.setNamaMahasiswa(nama);
        a.setProgramStudi(prodi);
        a.setStatusAnggota(status);

        model.updateAnggota(a);
        JOptionPane.showMessageDialog(view, "Data anggota diperbarui!");
        view.clearForm();
        loadData();
    }

    private void hapusAnggota() {
        int selectedId = view.getSelectedId();
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(view, "Pilih anggota yang akan dihapus!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(view, "Yakin hapus anggota ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            model.deleteAnggota(selectedId);
            JOptionPane.showMessageDialog(view, "Data dihapus!");
            view.clearForm();
            loadData();
        }
    }

    private void cariAnggota() {
        String keyword = view.getTxtCari().getText().toLowerCase();
        if (keyword.isEmpty()) {
            loadData();
            return;
        }

        // Logika pencarian sederhana di sisi client (bisa juga dipindah ke query SQL khusus di model)
        List<Anggota> allData = model.getAllAnggota();
        List<Anggota> filteredData = allData.stream()
                .filter(a -> a.getNamaMahasiswa().toLowerCase().contains(keyword) || a.getNim().toLowerCase().contains(keyword))
                .toList();
        
        view.setTableData(filteredData);
    }
    
    /* CATATAN PRIBADI (CHANDRA):
       1. Refactoring: Memindahkan semua logika CRUD yang tadinya di AnggotaPanel ke sini agar View bersih.
       2. Validasi: Validasi input tetap di controller sebelum kirim ke model.
       3. Search: Sementara pakai filtering stream di sisi Java, nanti kalau data banyak bisa dipindah ke query "LIKE" di SQL.
    */
}
