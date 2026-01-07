package id.ac.unpas.sajiwa.util;

import id.ac.unpas.sajiwa.database.KoneksiDB;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import javax.swing.*;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import java.io.File;
import java.awt.Desktop;

/**
 * Service untuk Menangani Laporan (PDF Reporting)
 * Menggunakan JasperReports dengan Template XML (JRXML) yang di-embed langsung.
 */
public class ReportService {

    // --- TEMPLATE JRXML UNTUK BUKU (BEAUTIFIED) ---
    private static final String REPORT_BUKU_XML = 
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        "<jasperReport xmlns=\"http://jasperreports.sourceforge.net/jasperreports\" " +
        "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
        "xsi:schemaLocation=\"http://jasperreports.sourceforge.net/jasperreports http://jasperreports.sourceforge.net/xsd/jasperreport.xsd\" " +
        "name=\"LaporanBuku\" pageWidth=\"595\" pageHeight=\"842\" columnWidth=\"555\" leftMargin=\"20\" rightMargin=\"20\" topMargin=\"20\" bottomMargin=\"20\">\n" +
        "    <queryString>\n" +
        "        <![CDATA[SELECT b.isbn, b.judul, b.stok, k.nama_kategori FROM buku b JOIN kategori_buku k ON b.id_kategori = k.id_kategori]]>\n" +
        "    </queryString>\n" +
        "    <field name=\"isbn\" class=\"java.lang.String\"/>\n" +
        "    <field name=\"judul\" class=\"java.lang.String\"/>\n" +
        "    <field name=\"stok\" class=\"java.lang.Integer\"/>\n" +
        "    <field name=\"nama_kategori\" class=\"java.lang.String\"/>\n" +
        "    <background><band splitType=\"Stretch\"/></background>\n" +
        "    <title>\n" +
        "        <band height=\"80\" splitType=\"Stretch\">\n" +
        "            <rectangle>\n" +
        "                <reportElement x=\"0\" y=\"0\" width=\"555\" height=\"80\" backcolor=\"#192A56\"/>\n" +
        "                <graphicElement><pen lineWidth=\"0.0\"/></graphicElement>\n" +
        "            </rectangle>\n" +
        "            <staticText>\n" +
        "                <reportElement x=\"20\" y=\"10\" width=\"515\" height=\"40\" forecolor=\"#FFFFFF\"/>\n" +
        "                <textElement verticalAlignment=\"Middle\"><font size=\"26\" isBold=\"true\"/></textElement>\n" +
        "                <text><![CDATA[SAJIWA LIBRARY]]></text>\n" +
        "            </staticText>\n" +
        "            <staticText>\n" +
        "                <reportElement x=\"20\" y=\"50\" width=\"515\" height=\"20\" forecolor=\"#BDC3C7\"/>\n" +
        "                <textElement><font size=\"12\"/></textElement>\n" +
        "                <text><![CDATA[Laporan Data Koleksi Buku Perpustakaan]]></text>\n" +
        "            </staticText>\n" +
        "        </band>\n" +
        "    </title>\n" +
        "    <columnHeader>\n" +
        "        <band height=\"40\" splitType=\"Stretch\">\n" +
        "            <rectangle>\n" +
        "                <reportElement x=\"0\" y=\"0\" width=\"555\" height=\"40\" backcolor=\"#E3F2FD\"/>\n" +
        "                <graphicElement><pen lineWidth=\"0.0\"/></graphicElement>\n" +
        "            </rectangle>\n" +
        "            <staticText><reportElement x=\"10\" y=\"0\" width=\"100\" height=\"40\"/><textElement verticalAlignment=\"Middle\"><font isBold=\"true\"/></textElement><text><![CDATA[ISBN]]></text></staticText>\n" +
        "            <staticText><reportElement x=\"110\" y=\"0\" width=\"245\" height=\"40\"/><textElement verticalAlignment=\"Middle\"><font isBold=\"true\"/></textElement><text><![CDATA[Judul Buku]]></text></staticText>\n" +
        "            <staticText><reportElement x=\"355\" y=\"0\" width=\"120\" height=\"40\"/><textElement verticalAlignment=\"Middle\"><font isBold=\"true\"/></textElement><text><![CDATA[Kategori]]></text></staticText>\n" +
        "            <staticText><reportElement x=\"475\" y=\"0\" width=\"80\" height=\"40\"/><textElement verticalAlignment=\"Middle\"><font isBold=\"true\"/></textElement><text><![CDATA[Stok]]></text></staticText>\n" +
        "        </band>\n" +
        "    </columnHeader>\n" +
        "    <detail>\n" +
        "        <band height=\"30\" splitType=\"Stretch\">\n" +
        "            <textField><reportElement x=\"10\" y=\"0\" width=\"100\" height=\"30\"/><textElement verticalAlignment=\"Middle\"/><textFieldExpression><![CDATA[$F{isbn}]]></textFieldExpression></textField>\n" +
        "            <textField><reportElement x=\"110\" y=\"0\" width=\"245\" height=\"30\"/><textElement verticalAlignment=\"Middle\"/><textFieldExpression><![CDATA[$F{judul}]]></textFieldExpression></textField>\n" +
        "            <textField><reportElement x=\"355\" y=\"0\" width=\"120\" height=\"30\"/><textElement verticalAlignment=\"Middle\"/><textFieldExpression><![CDATA[$F{nama_kategori}]]></textFieldExpression></textField>\n" +
        "            <textField><reportElement x=\"475\" y=\"0\" width=\"80\" height=\"30\"/><textElement verticalAlignment=\"Middle\"/><textFieldExpression><![CDATA[$F{stok}]]></textFieldExpression></textField>\n" +
        "            <line><reportElement x=\"0\" y=\"29\" width=\"555\" height=\"1\" forecolor=\"#EEEEEE\"/> </line>\n" +
        "        </band>\n" +
        "    </detail>\n" +
        "    <pageFooter>\n" +
        "        <band height=\"50\">\n" +
        "             <textField pattern=\"dd/MM/yyyy HH:mm\">\n" +
        "                <reportElement x=\"0\" y=\"20\" width=\"200\" height=\"20\" forecolor=\"#95a5a6\"/>\n" +
        "                <textFieldExpression><![CDATA[new java.util.Date()]]></textFieldExpression>\n" +
        "             </textField>\n" +
        "             <textField>\n" +
        "                <reportElement x=\"455\" y=\"20\" width=\"100\" height=\"20\" forecolor=\"#95a5a6\"/>\n" +
        "                <textElement textAlignment=\"Right\"/>\n" +
        "                <textFieldExpression><![CDATA[$V{PAGE_NUMBER}]]></textFieldExpression>\n" +
        "             </textField>\n" +
        "        </band>\n" +
        "    </pageFooter>\n" +
        "</jasperReport>";

    // --- TEMPLATE JRXML UNTUK ANGGOTA (BEAUTIFIED) ---
    private static final String REPORT_ANGGOTA_XML = 
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        "<jasperReport xmlns=\"http://jasperreports.sourceforge.net/jasperreports\" " +
        "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
        "xsi:schemaLocation=\"http://jasperreports.sourceforge.net/jasperreports http://jasperreports.sourceforge.net/xsd/jasperreport.xsd\" " +
        "name=\"LaporanAnggota\" pageWidth=\"595\" pageHeight=\"842\" columnWidth=\"555\" leftMargin=\"20\" rightMargin=\"20\" topMargin=\"20\" bottomMargin=\"20\">\n" +
        "    <queryString>\n" +
        "        <![CDATA[SELECT id_anggota, nim, nama_mahasiswa, program_studi, status_anggota FROM anggota]]>\n" +
        "    </queryString>\n" +
        "    <field name=\"id_anggota\" class=\"java.lang.Integer\"/>\n" +
        "    <field name=\"nim\" class=\"java.lang.String\"/>\n" +
        "    <field name=\"nama_mahasiswa\" class=\"java.lang.String\"/>\n" +
        "    <field name=\"program_studi\" class=\"java.lang.String\"/>\n" +
        "    <field name=\"status_anggota\" class=\"java.lang.String\"/>\n" +
        "    <background><band splitType=\"Stretch\"/></background>\n" +
        "    <title>\n" +
        "        <band height=\"80\" splitType=\"Stretch\">\n" +
        "            <rectangle>\n" +
        "                <reportElement x=\"0\" y=\"0\" width=\"555\" height=\"80\" backcolor=\"#273C75\"/>\n" +
        "                <graphicElement><pen lineWidth=\"0.0\"/></graphicElement>\n" +
        "            </rectangle>\n" +
        "            <staticText>\n" +
        "                <reportElement x=\"20\" y=\"10\" width=\"515\" height=\"40\" forecolor=\"#FFFFFF\"/>\n" +
        "                <textElement verticalAlignment=\"Middle\"><font size=\"26\" isBold=\"true\"/></textElement>\n" +
        "                <text><![CDATA[SAJIWA LIBRARY]]></text>\n" +
        "            </staticText>\n" +
        "            <staticText>\n" +
        "                <reportElement x=\"20\" y=\"50\" width=\"515\" height=\"20\" forecolor=\"#BDC3C7\"/>\n" +
        "                <textElement><font size=\"12\"/></textElement>\n" +
        "                <text><![CDATA[Laporan Data Keanggotaan Mahasiswa]]></text>\n" +
        "            </staticText>\n" +
        "        </band>\n" +
        "    </title>\n" +
        "    <columnHeader>\n" +
        "        <band height=\"40\" splitType=\"Stretch\">\n" +
        "            <rectangle>\n" +
        "                <reportElement x=\"0\" y=\"0\" width=\"555\" height=\"40\" backcolor=\"#D1C4E9\"/>\n" +
        "                <graphicElement><pen lineWidth=\"0.0\"/></graphicElement>\n" +
        "            </rectangle>\n" +
        "            <staticText><reportElement x=\"10\" y=\"0\" width=\"90\" height=\"40\"/><textElement verticalAlignment=\"Middle\"><font isBold=\"true\"/></textElement><text><![CDATA[NIM]]></text></staticText>\n" +
        "            <staticText><reportElement x=\"100\" y=\"0\" width=\"180\" height=\"40\"/><textElement verticalAlignment=\"Middle\"><font isBold=\"true\"/></textElement><text><![CDATA[Nama Mahasiswa]]></text></staticText>\n" +
        "            <staticText><reportElement x=\"280\" y=\"0\" width=\"150\" height=\"40\"/><textElement verticalAlignment=\"Middle\"><font isBold=\"true\"/></textElement><text><![CDATA[Prodi]]></text></staticText>\n" +
        "            <staticText><reportElement x=\"430\" y=\"0\" width=\"100\" height=\"40\"/><textElement verticalAlignment=\"Middle\"><font isBold=\"true\"/></textElement><text><![CDATA[Status]]></text></staticText>\n" +
        "        </band>\n" +
        "    </columnHeader>\n" +
        "    <detail>\n" +
        "        <band height=\"30\" splitType=\"Stretch\">\n" +
        "            <textField><reportElement x=\"10\" y=\"0\" width=\"90\" height=\"30\"/><textElement verticalAlignment=\"Middle\"/><textFieldExpression><![CDATA[$F{nim}]]></textFieldExpression></textField>\n" +
        "            <textField><reportElement x=\"100\" y=\"0\" width=\"180\" height=\"30\"/><textElement verticalAlignment=\"Middle\"/><textFieldExpression><![CDATA[$F{nama_mahasiswa}]]></textFieldExpression></textField>\n" +
        "            <textField><reportElement x=\"280\" y=\"0\" width=\"150\" height=\"30\"/><textElement verticalAlignment=\"Middle\"/><textFieldExpression><![CDATA[$F{program_studi}]]></textFieldExpression></textField>\n" +
        "            <textField><reportElement x=\"430\" y=\"0\" width=\"100\" height=\"30\"/><textElement verticalAlignment=\"Middle\"/><textFieldExpression><![CDATA[$F{status_anggota}]]></textFieldExpression></textField>\n" +
        "            <line><reportElement x=\"0\" y=\"29\" width=\"555\" height=\"1\" forecolor=\"#EEEEEE\"/> </line>\n" +
        "        </band>\n" +
        "    </detail>\n" +
        "    <pageFooter>\n" +
        "        <band height=\"50\">\n" +
        "             <textField pattern=\"dd/MM/yyyy HH:mm\">\n" +
        "                <reportElement x=\"0\" y=\"20\" width=\"200\" height=\"20\" forecolor=\"#95a5a6\"/>\n" +
        "                <textFieldExpression><![CDATA[new java.util.Date()]]></textFieldExpression>\n" +
        "             </textField>\n" +
        "             <textField>\n" +
        "                <reportElement x=\"455\" y=\"20\" width=\"100\" height=\"20\" forecolor=\"#95a5a6\"/>\n" +
        "                <textElement textAlignment=\"Right\"/>\n" +
        "                <textFieldExpression><![CDATA[$V{PAGE_NUMBER}]]></textFieldExpression>\n" +
        "             </textField>\n" +
        "        </band>\n" +
        "    </pageFooter>\n" +
        "</jasperReport>";

    // --- TEMPLATE JRXML UNTUK KATEGORI (BEAUTIFIED) ---
    private static final String REPORT_KATEGORI_XML = 
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        "<jasperReport xmlns=\"http://jasperreports.sourceforge.net/jasperreports\" " +
        "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
        "xsi:schemaLocation=\"http://jasperreports.sourceforge.net/jasperreports http://jasperreports.sourceforge.net/xsd/jasperreport.xsd\" " +
        "name=\"LaporanKategori\" pageWidth=\"595\" pageHeight=\"842\" columnWidth=\"555\" leftMargin=\"20\" rightMargin=\"20\" topMargin=\"20\" bottomMargin=\"20\">\n" +
        "    <queryString>\n" +
        "        <![CDATA[SELECT id_kategori, nama_kategori FROM kategori_buku ORDER BY id_kategori ASC]]>\n" +
        "    </queryString>\n" +
        "    <field name=\"id_kategori\" class=\"java.lang.Integer\"/>\n" +
        "    <field name=\"nama_kategori\" class=\"java.lang.String\"/>\n" +
        "    <title>\n" +
        "        <band height=\"80\" splitType=\"Stretch\">\n" +
        "            <rectangle>\n" +
        "                <reportElement x=\"0\" y=\"0\" width=\"555\" height=\"80\" backcolor=\"#F1C40F\"/>\n" +
        "                <graphicElement><pen lineWidth=\"0.0\"/></graphicElement>\n" +
        "            </rectangle>\n" +
        "            <staticText>\n" +
        "                <reportElement x=\"20\" y=\"10\" width=\"515\" height=\"40\" forecolor=\"#2C3E50\"/>\n" +
        "                <textElement verticalAlignment=\"Middle\"><font size=\"26\" isBold=\"true\"/></textElement>\n" +
        "                <text><![CDATA[SAJIWA LIBRARY]]></text>\n" +
        "            </staticText>\n" +
        "            <staticText>\n" +
        "                <reportElement x=\"20\" y=\"50\" width=\"515\" height=\"20\" forecolor=\"#2C3E50\"/>\n" +
        "                <textElement><font size=\"12\"/></textElement>\n" +
        "                <text><![CDATA[Laporan Kategori Buku]]></text>\n" +
        "            </staticText>\n" +
        "        </band>\n" +
        "    </title>\n" +
        "    <columnHeader>\n" +
        "        <band height=\"40\">\n" +
        "            <rectangle>\n" +
        "                <reportElement x=\"0\" y=\"0\" width=\"555\" height=\"40\" backcolor=\"#FFF9C4\"/>\n" +
        "                <graphicElement><pen lineWidth=\"0.0\"/></graphicElement>\n" +
        "            </rectangle>\n" +
        "            <staticText><reportElement x=\"10\" y=\"0\" width=\"100\" height=\"40\"/><textElement verticalAlignment=\"Middle\"><font isBold=\"true\"/></textElement><text><![CDATA[ID Kategori]]></text></staticText>\n" +
        "            <staticText><reportElement x=\"110\" y=\"0\" width=\"445\" height=\"40\"/><textElement verticalAlignment=\"Middle\"><font isBold=\"true\"/></textElement><text><![CDATA[Nama Kategori]]></text></staticText>\n" +
        "        </band>\n" +
        "    </columnHeader>\n" +
        "    <detail>\n" +
        "        <band height=\"30\">\n" +
        "            <textField><reportElement x=\"10\" y=\"0\" width=\"100\" height=\"30\"/><textElement verticalAlignment=\"Middle\"/><textFieldExpression><![CDATA[$F{id_kategori}]]></textFieldExpression></textField>\n" +
        "            <textField><reportElement x=\"110\" y=\"0\" width=\"445\" height=\"30\"/><textElement verticalAlignment=\"Middle\"/><textFieldExpression><![CDATA[$F{nama_kategori}]]></textFieldExpression></textField>\n" +
         "            <line><reportElement x=\"0\" y=\"29\" width=\"555\" height=\"1\" forecolor=\"#EEEEEE\"/> </line>\n" +
        "        </band>\n" +
        "    </detail>\n" +
        "    <pageFooter>\n" +
         "        <band height=\"50\">\n" +
        "             <textField pattern=\"dd/MM/yyyy HH:mm\">\n" +
        "                <reportElement x=\"0\" y=\"20\" width=\"200\" height=\"20\" forecolor=\"#95a5a6\"/>\n" +
        "                <textFieldExpression><![CDATA[new java.util.Date()]]></textFieldExpression>\n" +
        "             </textField>\n" +
        "             <textField>\n" +
        "                <reportElement x=\"455\" y=\"20\" width=\"100\" height=\"20\" forecolor=\"#95a5a6\"/>\n" +
        "                <textElement textAlignment=\"Right\"/>\n" +
        "                <textFieldExpression><![CDATA[$V{PAGE_NUMBER}]]></textFieldExpression>\n" +
        "             </textField>\n" +
        "        </band>\n" +
        "    </pageFooter>\n" +
        "</jasperReport>";

    public void cetakLaporanBuku() {
        printReport("Laporan Buku", REPORT_BUKU_XML);
    }

    public void cetakLaporanAnggota() {
        printReport("Laporan Anggota", REPORT_ANGGOTA_XML);
    }

    public void cetakLaporanKategori() {
        printReport("Laporan Kategori", REPORT_KATEGORI_XML);
    }

    // METHOD SPESIAL: GABUNGKAN SEMUA LAPORAN JADI SATU PDF
    public void cetakLaporanGabungan() {
        try {
            List<JasperPrint> jasperPrintList = new ArrayList<>();
            Connection conn = KoneksiDB.getConnection();

            // 1. Generate Laporan Buku
            JasperDesign design1 = JRXmlLoader.load(new ByteArrayInputStream(REPORT_BUKU_XML.getBytes()));
            JasperReport report1 = JasperCompileManager.compileReport(design1);
            jasperPrintList.add(JasperFillManager.fillReport(report1, null, conn));

            // 2. Generate Laporan Anggota
            JasperDesign design2 = JRXmlLoader.load(new ByteArrayInputStream(REPORT_ANGGOTA_XML.getBytes()));
            JasperReport report2 = JasperCompileManager.compileReport(design2);
            jasperPrintList.add(JasperFillManager.fillReport(report2, null, conn));

            // 3. Generate Laporan Kategori
            JasperDesign design3 = JRXmlLoader.load(new ByteArrayInputStream(REPORT_KATEGORI_XML.getBytes()));
            JasperReport report3 = JasperCompileManager.compileReport(design3);
            jasperPrintList.add(JasperFillManager.fillReport(report3, null, conn));

            // 4. Gabungkan dan Export ke PDF
            String outputPath = System.getProperty("user.home") + File.separator + "Downloads" + File.separator + "Laporan_Sajiwa_Lengkap.pdf";
            
            JRPdfExporter exporter = new JRPdfExporter();
            exporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputPath));
            
            exporter.exportReport();

            JOptionPane.showMessageDialog(null, "Laporan Gabungan Berhasil Disimpan di:\n" + outputPath, "Suksess", JOptionPane.INFORMATION_MESSAGE);
            
            // Coba buka file otomatis
            if (Desktop.isDesktopSupported()) {
                File myFile = new File(outputPath);
                Desktop.getDesktop().open(myFile);
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Gagal Mencetak Laporan Gabungan: " + e.getMessage());
        }
    }

    private void printReport(String title, String xmlContent) {
        try {
            // 1. Load XML dari String
            InputStream in = new ByteArrayInputStream(xmlContent.getBytes());
            JasperDesign jasperDesign = JRXmlLoader.load(in);
            
            // 2. Compile Report
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            
            // 3. Fill Report dengan Data dari Database
            Map<String, Object> parameters = new HashMap<>();
            Connection conn = KoneksiDB.getConnection();
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
            
            // 4. Tampilkan di Viewer
            JasperViewer viewer = new JasperViewer(jasperPrint, false); // false = close viewer doesn't exit app
            viewer.setTitle(title);
            viewer.setVisible(true);
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Gagal Mencetak " + title + ": " + e.getMessage());
        }
    }
}

/*
 * ==================================================================================
 * CATATAN PRIBADI (CHANDRA)
 * ==================================================================================
 * 1. JasperReports tanpa file .jasper eksternal:
 *    - Biasanya Jasper perlu file .jasper/.jrxml di folder project.
 *    - Tapi di sini saya bereksperimen dengan menaruh kode XML-nya LANGSUNG di string Java 
 *      (Hardcoded XML).
 *    - ALASANNYA: Biar pas di-build jadi .jar atau dipindah komputer lain, kita gak 
 *      perlu repot bawa-bawa file .jasper nya. Aplikasinya jadi mandiri (portable).
 * 
 * 2. Teknik Kompilasi Runtime:
 *    - String XML tadi di-convert jadi InputStream -> diload -> dikompilasi (compileReport) 
 *      secara on-the-fly saat tombol diklik.
 *    - Memang sedikit lebih lambat sepersekian detik dibanding load file jadi, tapi 
 *      lebih fleksibel buat tubes ini.
 * 
 * 3. Desain Laporan:
 *    - Saya pakai ColumnHeader, Detail, PageFooter standar.
 *    - Warna & Layout ditulis manual via tag XML (agak rumit sih, tapi puas hasilnya).
 * ==================================================================================
 */
