package id.ac.unpas.sajiwa.view;

import id.ac.unpas.sajiwa.model.StatsModel;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class DashboardPanel extends JPanel {
    
    public DashboardPanel() {
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(230, 242, 255));
        setBorder(new EmptyBorder(30, 30, 30, 30));
        
        // --- HEADER ---
        JPanel header = new JPanel(new GridLayout(2, 1));
        header.setOpaque(false);
        
        JLabel lblWelcome = new JLabel("Selamat Datang di Sajiwa Library System");
        lblWelcome.setFont(new Font("SansSerif", Font.BOLD, 28));
        lblWelcome.setForeground(new Color(25, 42, 86));
        
        JLabel lblSub = new JLabel("Ringkasan Data Perpustakaan Terkini");
        lblSub.setFont(new Font("SansSerif", Font.PLAIN, 16));
        lblSub.setForeground(new Color(100, 100, 100));
        
        header.add(lblWelcome);
        header.add(lblSub);
        
        add(header, BorderLayout.NORTH);
        
        // --- CONTENT CARDS ---
        JPanel cardContainer = new JPanel(new GridLayout(1, 3, 20, 0)); // 1 Baris, 3 Kolom
        cardContainer.setOpaque(false);
        
        // Ambil Data Realtime
        StatsModel stats = new StatsModel();
        int totalBuku = stats.getCount("buku");
        int totalAnggota = stats.getCount("anggota");
        int dipinjam = stats.getCountPeminjamanAktif();
        
        cardContainer.add(createCard("Total Buku", totalBuku + " Judul", "📚", new Color(41, 128, 185)));
        cardContainer.add(createCard("Total Anggota", totalAnggota + " Orang", "👥", new Color(39, 174, 96)));
        cardContainer.add(createCard("Sedang Dipinjam", dipinjam + " Buku", "🔄", new Color(243, 156, 18)));
        
        // Tambahan Gambar Ilustrasi / Banner
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        centerPanel.add(cardContainer, BorderLayout.NORTH);
        
        JLabel lblFooter = new JLabel("Sajiwa Library System v1.0 - Developed by Kelompok 3", SwingConstants.CENTER);
        lblFooter.setBorder(new EmptyBorder(20, 0, 0, 0));
        centerPanel.add(lblFooter, BorderLayout.SOUTH);
        
        add(centerPanel, BorderLayout.CENTER);
    }
    
    private JPanel createCard(String title, String value, String icon, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            new EmptyBorder(20, 20, 20, 20)
        ));
        
        // Line Kiri Berwarna
        JPanel colorStrip = new JPanel();
        colorStrip.setBackground(color);
        colorStrip.setPreferredSize(new Dimension(5, 0));
        
        // Konten Teks
        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.setOpaque(false);
        
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblTitle.setForeground(Color.GRAY);
        
        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblValue.setForeground(color);
        
        textPanel.add(lblTitle);
        textPanel.add(lblValue);
        
        // Icon Besar
        JLabel lblIcon = new JLabel(icon);
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
        lblIcon.setBorder(new EmptyBorder(0, 10, 0, 0));
        
        card.add(colorStrip, BorderLayout.WEST);
        card.add(textPanel, BorderLayout.CENTER);
        card.add(lblIcon, BorderLayout.EAST);
        
        return card;
    }
}
