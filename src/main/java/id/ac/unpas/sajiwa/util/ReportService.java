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

    // --- TEMPLATE JRXML UNTUK BUKU ---
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
        "        <band height=\"79\" splitType=\"Stretch\">\n" +
        "            <staticText>\n" +
        "                <reportElement x=\"0\" y=\"0\" width=\"555\" height=\"79\" forecolor=\"#192A56\"/>\n" +
        "                <textElement textAlignment=\"Center\" verticalAlignment=\"Middle\">\n" +
        "                    <font size=\"24\" isBold=\"true\"/>\n" +
        "                </textElement>\n" +
        "                <text><![CDATA[LAPORAN DATA BUKU SAJIWA]]></text>\n" +
        "            </staticText>\n" +
        "        </band>\n" +
        "    </title>\n" +
        "    <columnHeader>\n" +
        "        <band height=\"30\" splitType=\"Stretch\">\n" +
        "            <staticText><reportElement x=\"0\" y=\"0\" width=\"100\" height=\"30\"/><box><bottomPen lineWidth=\"1.0\"/></box><textElement verticalAlignment=\"Middle\"><font isBold=\"true\"/></textElement><text><![CDATA[ISBN]]></text></staticText>\n" +
        "            <staticText><reportElement x=\"100\" y=\"0\" width=\"255\" height=\"30\"/><box><bottomPen lineWidth=\"1.0\"/></box><textElement verticalAlignment=\"Middle\"><font isBold=\"true\"/></textElement><text><![CDATA[Judul Buku]]></text></staticText>\n" +
        "            <staticText><reportElement x=\"355\" y=\"0\" width=\"100\" height=\"30\"/><box><bottomPen lineWidth=\"1.0\"/></box><textElement verticalAlignment=\"Middle\"><font isBold=\"true\"/></textElement><text><![CDATA[Kategori]]></text></staticText>\n" +
        "            <staticText><reportElement x=\"455\" y=\"0\" width=\"100\" height=\"30\"/><box><bottomPen lineWidth=\"1.0\"/></box><textElement verticalAlignment=\"Middle\"><font isBold=\"true\"/></textElement><text><![CDATA[Stok]]></text></staticText>\n" +
        "        </band>\n" +
        "    </columnHeader>\n" +
        "    <detail>\n" +
        "        <band height=\"20\" splitType=\"Stretch\">\n" +
        "            <textField><reportElement x=\"0\" y=\"0\" width=\"100\" height=\"20\"/><textElement verticalAlignment=\"Middle\"/><textFieldExpression><![CDATA[$F{isbn}]]></textFieldExpression></textField>\n" +
        "            <textField><reportElement x=\"100\" y=\"0\" width=\"255\" height=\"20\"/><textElement verticalAlignment=\"Middle\"/><textFieldExpression><![CDATA[$F{judul}]]></textFieldExpression></textField>\n" +
        "            <textField><reportElement x=\"355\" y=\"0\" width=\"100\" height=\"20\"/><textElement verticalAlignment=\"Middle\"/><textFieldExpression><![CDATA[$F{nama_kategori}]]></textFieldExpression></textField>\n" +
        "            <textField><reportElement x=\"455\" y=\"0\" width=\"100\" height=\"20\"/><textElement verticalAlignment=\"Middle\"/><textFieldExpression><![CDATA[$F{stok}]]></textFieldExpression></textField>\n" +
        "        </band>\n" +
        "    </detail>\n" +
        "</jasperReport>";

    // --- TEMPLATE JRXML UNTUK ANGGOTA ---
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
        "        <band height=\"79\" splitType=\"Stretch\">\n" +
        "            <staticText>\n" +
        "                <reportElement x=\"0\" y=\"0\" width=\"555\" height=\"79\" forecolor=\"#273C75\"/>\n" +
        "                <textElement textAlignment=\"Center\" verticalAlignment=\"Middle\">\n" +
        "                    <font size=\"24\" isBold=\"true\"/>\n" +
        "                </textElement>\n" +
        "                <text><![CDATA[LAPORAN ANGGOTA PERPUSTAKAAN]]></text>\n" +
        "            </staticText>\n" +
        "        </band>\n" +
        "    </title>\n" +
        "    <columnHeader>\n" +
        "        <band height=\"30\" splitType=\"Stretch\">\n" +
        "            <staticText><reportElement x=\"0\" y=\"0\" width=\"80\" height=\"30\"/><box><bottomPen lineWidth=\"1.0\"/></box><textElement verticalAlignment=\"Middle\"><font isBold=\"true\"/></textElement><text><![CDATA[NIM]]></text></staticText>\n" +
        "            <staticText><reportElement x=\"80\" y=\"0\" width=\"175\" height=\"30\"/><box><bottomPen lineWidth=\"1.0\"/></box><textElement verticalAlignment=\"Middle\"><font isBold=\"true\"/></textElement><text><![CDATA[Nama Mahasiswa]]></text></staticText>\n" +
        "            <staticText><reportElement x=\"255\" y=\"0\" width=\"150\" height=\"30\"/><box><bottomPen lineWidth=\"1.0\"/></box><textElement verticalAlignment=\"Middle\"><font isBold=\"true\"/></textElement><text><![CDATA[Prodi]]></text></staticText>\n" +
        "            <staticText><reportElement x=\"405\" y=\"0\" width=\"100\" height=\"30\"/><box><bottomPen lineWidth=\"1.0\"/></box><textElement verticalAlignment=\"Middle\"><font isBold=\"true\"/></textElement><text><![CDATA[Status]]></text></staticText>\n" +
        "        </band>\n" +
        "    </columnHeader>\n" +
        "    <detail>\n" +
        "        <band height=\"20\" splitType=\"Stretch\">\n" +
        "            <textField><reportElement x=\"0\" y=\"0\" width=\"80\" height=\"20\"/><textElement verticalAlignment=\"Middle\"/><textFieldExpression><![CDATA[$F{nim}]]></textFieldExpression></textField>\n" +
        "            <textField><reportElement x=\"80\" y=\"0\" width=\"175\" height=\"20\"/><textElement verticalAlignment=\"Middle\"/><textFieldExpression><![CDATA[$F{nama_mahasiswa}]]></textFieldExpression></textField>\n" +
        "            <textField><reportElement x=\"255\" y=\"0\" width=\"150\" height=\"20\"/><textElement verticalAlignment=\"Middle\"/><textFieldExpression><![CDATA[$F{program_studi}]]></textFieldExpression></textField>\n" +
        "            <textField><reportElement x=\"405\" y=\"0\" width=\"100\" height=\"20\"/><textElement verticalAlignment=\"Middle\"/><textFieldExpression><![CDATA[$F{status_anggota}]]></textFieldExpression></textField>\n" +
        "        </band>\n" +
        "    </detail>\n" +
        "</jasperReport>";

    // --- TEMPLATE JRXML UNTUK KATEGORI ---
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
        "        <band height=\"79\" splitType=\"Stretch\">\n" +
        "            <staticText>\n" +
        "                <reportElement x=\"0\" y=\"0\" width=\"555\" height=\"79\" forecolor=\"#e1b12c\"/>\n" +
        "                <textElement textAlignment=\"Center\" verticalAlignment=\"Middle\">\n" +
        "                    <font size=\"24\" isBold=\"true\"/>\n" +
        "                </textElement>\n" +
        "                <text><![CDATA[LAPORAN KATEGORI BUKU]]></text>\n" +
        "            </staticText>\n" +
        "        </band>\n" +
        "    </title>\n" +
        "    <columnHeader>\n" +
        "        <band height=\"30\">\n" +
        "            <staticText><reportElement x=\"0\" y=\"0\" width=\"100\" height=\"30\"/><box><bottomPen lineWidth=\"1.0\"/></box><textElement verticalAlignment=\"Middle\"><font isBold=\"true\"/></textElement><text><![CDATA[ID Kategori]]></text></staticText>\n" +
        "            <staticText><reportElement x=\"100\" y=\"0\" width=\"455\" height=\"30\"/><box><bottomPen lineWidth=\"1.0\"/></box><textElement verticalAlignment=\"Middle\"><font isBold=\"true\"/></textElement><text><![CDATA[Nama Kategori]]></text></staticText>\n" +
        "        </band>\n" +
        "    </columnHeader>\n" +
        "    <detail>\n" +
        "        <band height=\"20\">\n" +
        "            <textField><reportElement x=\"0\" y=\"0\" width=\"100\" height=\"20\"/><textElement verticalAlignment=\"Middle\"/><textFieldExpression><![CDATA[$F{id_kategori}]]></textFieldExpression></textField>\n" +
        "            <textField><reportElement x=\"100\" y=\"0\" width=\"455\" height=\"20\"/><textElement verticalAlignment=\"Middle\"/><textFieldExpression><![CDATA[$F{nama_kategori}]]></textFieldExpression></textField>\n" +
        "        </band>\n" +
        "    </detail>\n" +
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
