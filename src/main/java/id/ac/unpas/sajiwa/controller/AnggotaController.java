package id.ac.unpas.sajiwa.controller;

import id.ac.unpas.sajiwa.model.Anggota;
import id.ac.unpas.sajiwa.model.AnggotaModel;
import id.ac.unpas.sajiwa.view.AnggotaPanel;
import id.ac.unpas.sajiwa.database.KoneksiDB;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.view.JasperViewer;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.util.HashMap;
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
        view.getBtnExport().addActionListener(e -> exportPdf());
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

    private void exportPdf() {
        try {
            // 1. Definisikan Template Report (JRXML) secara Programmatic (Inline String)
            // Ini trik biar gak ribet urus file path .jrxml di folder project
            String jrxml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<jasperReport xmlns=\"http://jasperreports.sourceforge.net/jasperreports\" " +
                "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
                "xsi:schemaLocation=\"http://jasperreports.sourceforge.net/jasperreports http://jasperreports.sourceforge.net/xsd/jasperreport.xsd\" " +
                "name=\"LaporanAnggota\" pageWidth=\"595\" pageHeight=\"842\" columnWidth=\"555\" leftMargin=\"20\" rightMargin=\"20\" topMargin=\"20\" bottomMargin=\"20\">\n" +
                "    <queryString>\n" +
                "        <![CDATA[SELECT * FROM anggota]]>\n" +
                "    </queryString>\n" +
                "    <field name=\"nim\" class=\"java.lang.String\"/>\n" +
                "    <field name=\"nama_mahasiswa\" class=\"java.lang.String\"/>\n" +
                "    <field name=\"program_studi\" class=\"java.lang.String\"/>\n" +
                "    <field name=\"status_anggota\" class=\"java.lang.String\"/>\n" +
                "    <title>\n" +
                "        <band height=\"50\">\n" +
                "            <staticText>\n" +
                "                <reportElement x=\"0\" y=\"10\" width=\"555\" height=\"30\"/>\n" +
                "                <textElement textAlignment=\"Center\">\n" +
                "                    <font size=\"18\" isBold=\"true\"/>\n" +
                "                </textElement>\n" +
                "                <text><![CDATA[LAPORAN DATA ANGGOTA SAJIWA PERPUS]]></text>\n" +
                "            </staticText>\n" +
                "        </band>\n" +
                "    </title>\n" +
                "    <columnHeader>\n" +
                "        <band height=\"30\">\n" +
                "            <staticText>\n" +
                "                <reportElement x=\"0\" y=\"0\" width=\"100\" height=\"30\"/>\n" +
                "                <textElement verticalAlignment=\"Middle\"><font isBold=\"true\"/></textElement>\n" +
                "                <text><![CDATA[NIM]]></text>\n" +
                "            </staticText>\n" +
                "            <staticText>\n" +
                "                <reportElement x=\"100\" y=\"0\" width=\"200\" height=\"30\"/>\n" +
                "                <textElement verticalAlignment=\"Middle\"><font isBold=\"true\"/></textElement>\n" +
                "                <text><![CDATA[NAMA MAHASISWA]]></text>\n" +
                "            </staticText>\n" +
                "            <staticText>\n" +
                "                <reportElement x=\"300\" y=\"0\" width=\"150\" height=\"30\"/>\n" +
                "                <textElement verticalAlignment=\"Middle\"><font isBold=\"true\"/></textElement>\n" +
                "                <text><![CDATA[PRODI]]></text>\n" +
                "            </staticText>\n" +
                "            <staticText>\n" +
                "                <reportElement x=\"450\" y=\"0\" width=\"100\" height=\"30\"/>\n" +
                "                <textElement verticalAlignment=\"Middle\"><font isBold=\"true\"/></textElement>\n" +
                "                <text><![CDATA[STATUS]]></text>\n" +
                "            </staticText>\n" +
                "        </band>\n" +
                "    </columnHeader>\n" +
                "    <detail>\n" +
                "        <band height=\"30\">\n" +
                "            <textField>\n" +
                "                <reportElement x=\"0\" y=\"0\" width=\"100\" height=\"30\"/>\n" +
                "                <textElement verticalAlignment=\"Middle\"/>\n" +
                "                <textFieldExpression><![CDATA[$F{nim}]]></textFieldExpression>\n" +
                "            </textField>\n" +
                "            <textField>\n" +
                "                <reportElement x=\"100\" y=\"0\" width=\"200\" height=\"30\"/>\n" +
                "                <textElement verticalAlignment=\"Middle\"/>\n" +
                "                <textFieldExpression><![CDATA[$F{nama_mahasiswa}]]></textFieldExpression>\n" +
                "            </textField>\n" +
                "            <textField>\n" +
                "                <reportElement x=\"300\" y=\"0\" width=\"150\" height=\"30\"/>\n" +
                "                <textElement verticalAlignment=\"Middle\"/>\n" +
                "                <textFieldExpression><![CDATA[$F{program_studi}]]></textFieldExpression>\n" +
                "            </textField>\n" +
                "            <textField>\n" +
                "                <reportElement x=\"450\" y=\"0\" width=\"100\" height=\"30\"/>\n" +
                "                <textElement verticalAlignment=\"Middle\"/>\n" +
                "                <textFieldExpression><![CDATA[$F{status_anggota}]]></textFieldExpression>\n" +
                "            </textField>\n" +
                "        </band>\n" +
                "    </detail>\n" +
                "</jasperReport>";

            // 2. Load JRXML dari String
            InputStream in = new ByteArrayInputStream(jrxml.getBytes(StandardCharsets.UTF_8));
            JasperDesign jasperDesign = JRXmlLoader.load(in);

            // 3. Compile Report
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

            // 4. Fill Report (isi data dari database)
            Connection conn = KoneksiDB.getConnection();
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap<>(), conn);
            
            // 5. Tampilkan Viewer
            JasperViewer viewer = new JasperViewer(jasperPrint, false); // false = jangan exit app pas close viewer
            viewer.setTitle("Laporan Data Anggota");
            viewer.setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(view, "Gagal Mencetak Laporan: " + e.getMessage());
        }
    }
    
    /* CATATAN PRIBADI (CHANDRA):
       1. Refactoring: Memindahkan semua logika CRUD yang tadinya di AnggotaPanel ke sini agar View bersih.
       2. Validasi: Validasi input tetap di controller sebelum kirim ke model.
       3. Search: Sementara pakai filtering stream di sisi Java, nanti kalau data banyak bisa dipindah ke query "LIKE" di SQL.
    */
}
