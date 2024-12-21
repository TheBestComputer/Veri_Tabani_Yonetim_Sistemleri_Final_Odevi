
import java.awt.*;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

class GorevRepository {

    public static void main(String[] args) {
        GorevRepository repository = new GorevRepository();

        // Proje ekleme örneği
        repository.gorevEkle(1, 1, "Yeni Proje", "Devam Ediyor", Date.valueOf("2024-01-01"), Date.valueOf("2024-12-31"), 100);

        // Proje listeleme örneği
        List<String> gorevler = repository.gorevListele();
        for (String gorev : gorevler) {
            System.out.println(gorev);
        }
        repository.gorevDurumuGuncelle(1);
    }

    public void gorevEkle(int projeId, int calisanId, String ad, String durum, Date baslangicTarihi, Date bitisTarihi, int adamGun) {
        String sql = "INSERT INTO Gorevler (int projeId, int calisanId, String ad, String durum, Date baslangicTarihi, Date bitisTarihi, int adamGun) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseHelper.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, projeId);
            stmt.setInt(2, calisanId);
            stmt.setString(3, ad);
            stmt.setString(4, durum);
            stmt.setDate(5, baslangicTarihi);
            stmt.setDate(6, bitisTarihi);
            stmt.setInt(7, adamGun);
            stmt.executeUpdate();

            System.out.println("Görev başarıyla eklendi.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> gorevListele() {
        String sql = "SELECT g.Id, g.Ad, g.Durum, g.BaslangicTarihi, g.BitisTarihi, g.AdamGun, c.Ad AS CalisanAd, c.Soyad AS CalisanSoyad, p.Ad AS ProjeAd, "
                + "DATEDIFF(g.BitisTarihi, CURRENT_DATE) AS GecikmeSuresi "
                + "FROM Gorevler g "
                + "JOIN Calisanlar c ON g.CalisanId = c.Id "
                + "JOIN Projeler p ON g.ProjeId = p.Id";
        List<String> gorevler = new ArrayList<>();
        try (Connection conn = DatabaseHelper.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("Id");
                String gorevAd = rs.getString("Ad");
                String durum = rs.getString("Durum");
                Date baslangicTarihi = rs.getDate("BaslangicTarihi");
                Date bitisTarihi = rs.getDate("BitisTarihi");
                int adamGun = rs.getInt("AdamGun");
                String calisanAd = rs.getString("CalisanAd");
                String calisanSoyad = rs.getString("CalisanSoyad");
                String projeAd = rs.getString("ProjeAd");
                int gecikmeSuresi = rs.getInt("GecikmeSuresi");

                gorevler.add("Görev ID: " + id + ", Görev Adı: " + gorevAd + ", Durum: " + durum + ", Başlangıç Tarihi: " + baslangicTarihi + ", Bitiş Tarihi: " + bitisTarihi + ", Adam Gün: " + adamGun + ", Çalışan: " + calisanAd + " " + calisanSoyad + ", Proje: " + projeAd + ", Gecikme Süresi: " + (gecikmeSuresi < 0 ? 0 : gecikmeSuresi) + " gün");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return gorevler;
    }

    public void gorevDurumuGuncelle(int gorevId) {
        String selectSql = "SELECT BitisTarihi, Durum FROM Gorevler WHERE Id = ?";
        String updateSql = "UPDATE Gorevler SET Durum = ? WHERE Id = ?";
        try (Connection conn = DatabaseHelper.getConnection(); PreparedStatement selectStmt = conn.prepareStatement(selectSql); PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {

            selectStmt.setInt(1, gorevId);
            try (ResultSet rs = selectStmt.executeQuery()) {
                if (rs.next()) {
                    Date bitisTarihi = rs.getDate("BitisTarihi");
                    String mevcutDurum = rs.getString("Durum");
                    Date bugun = new Date(System.currentTimeMillis());

                    if (mevcutDurum.equals("Tamamlandı")) {
                        System.out.println("Görev zaten tamamlanmış.");
                        return;
                    }

                    String yeniDurum = "";
                    if (bugun.after(bitisTarihi)) {
                        yeniDurum = "Tamamlanacak";
                    } else if (bugun.equals(bitisTarihi)) {
                        yeniDurum = "Tamamlandı";
                    } else {
                        yeniDurum = "Devam Ediyor";
                    }

                    updateStmt.setString(1, yeniDurum);
                    updateStmt.setInt(2, gorevId);
                    updateStmt.executeUpdate();
                    System.out.println("Görev durumu güncellendi.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
        public void projeGorevIslemleri(String projeAdi) {
        JFrame frame = new JFrame("Proje Görev İşlemleri");
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(4, 1));

        JButton btnGorevEkle = new JButton("Görev Ekle");
        JButton btnGorevListele = new JButton("Görevleri Listele");
        JButton btnGorevGuncelle = new JButton("Görev Durumunu Güncelle");

        panel.add(btnGorevEkle);
        panel.add(btnGorevListele);
        panel.add(btnGorevGuncelle);

        frame.add(panel);

        btnGorevEkle.addActionListener(e -> {
            JFrame gorevEkleFrame = new JFrame("Yeni Görev Ekle");
            gorevEkleFrame.setSize(300, 300);
            gorevEkleFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            gorevEkleFrame.setLocationRelativeTo(null);

            JPanel gorevPanel = new JPanel(new GridLayout(7, 2));

            JLabel lblAd = new JLabel("Görev Adı:");
            JTextField txtAd = new JTextField();
            JLabel lblCalisan = new JLabel("Çalışan Seç:");
            JComboBox<String> cmbCalisan = new JComboBox<>();
            loadCalisanlar(cmbCalisan); // Çalışanları yükle
            JLabel lblDurum = new JLabel("Durum:");
            String[] durumlar = {"Tamamlanacak", "Devam Ediyor", "Tamamlandı"};
            JComboBox<String> cmbDurum = new JComboBox<>(durumlar);
            JLabel lblBaslangic = new JLabel("Başlangıç Tarihi (YYYY-MM-DD):");
            JTextField txtBaslangic = new JTextField();
            JLabel lblBitis = new JLabel("Bitiş Tarihi (YYYY-MM-DD):");
            JTextField txtBitis = new JTextField();
            JLabel lblAdamGun = new JLabel("Adam Gün:");
            JTextField txtAdamGun = new JTextField();

            JButton btnKaydet = new JButton("Kaydet");

            gorevPanel.add(lblAd);
            gorevPanel.add(txtAd);
            gorevPanel.add(lblCalisan);
            gorevPanel.add(cmbCalisan);
            gorevPanel.add(lblDurum);
            gorevPanel.add(cmbDurum);
            gorevPanel.add(lblBaslangic);
            gorevPanel.add(txtBaslangic);
            gorevPanel.add(lblBitis);
            gorevPanel.add(txtBitis);
            gorevPanel.add(lblAdamGun);
            gorevPanel.add(txtAdamGun);
            gorevPanel.add(btnKaydet);

            gorevEkleFrame.add(gorevPanel);

            btnKaydet.addActionListener(ev -> {
                String gorevAdi = txtAd.getText();
                String durum = (String) cmbDurum.getSelectedItem(); // Seçilen durumu al
                int calisanId = cmbCalisan.getSelectedIndex() + 1; // Seçilen çalışanı al
                Date baslangicTarihi = Date.valueOf(txtBaslangic.getText());
                Date bitisTarihi = Date.valueOf(txtBitis.getText());
                int adamGun = Integer.parseInt(txtAdamGun.getText());

                // Veritabanına görev ekleme işlemi
                gorevEkle(1, calisanId, gorevAdi, durum, baslangicTarihi, bitisTarihi, adamGun);
                JOptionPane.showMessageDialog(gorevEkleFrame, "Görev başarıyla eklendi.");

                gorevEkleFrame.dispose(); // Yeni görev ekleme penceresini kapat
            });

            gorevEkleFrame.setVisible(true);
        });

        btnGorevListele.addActionListener(e -> {
            List<String> gorevler = gorevListele();
            StringBuilder sb = new StringBuilder();
            for (String gorev : gorevler) {
                sb.append(gorev).append("\n");
            }
            JTextArea textArea = new JTextArea(sb.toString());
            textArea.setEditable(false);
            JOptionPane.showMessageDialog(frame, new JScrollPane(textArea), "Görev Listesi", JOptionPane.INFORMATION_MESSAGE);
        });

        btnGorevGuncelle.addActionListener(e -> {
            String gorevIdStr = JOptionPane.showInputDialog(frame, "Görev ID'sini girin:");
            try {
                int gorevId = Integer.parseInt(gorevIdStr);
                gorevDurumuGuncelle(gorevId);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Geçersiz Görev ID'si.");
            }
        });

        frame.setVisible(true);
    }

    // Çalışanları yüklemek için fonksiyon
    private void loadCalisanlar(JComboBox<String> cmbCalisan) {
        String sql = "SELECT Id, Ad, Soyad FROM Calisanlar";
        try (Connection conn = DatabaseHelper.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int calisanId = rs.getInt("Id");
                String calisanAdi = rs.getString("Ad") + " " + rs.getString("Soyad");
                cmbCalisan.addItem(calisanAdi); // Çalışan adını ekle
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
    }
}
