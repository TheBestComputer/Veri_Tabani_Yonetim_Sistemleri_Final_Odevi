import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

class GorevRepository {

    // Görev Ekleme Metodu
    public void gorevEkle(int projeId, int calisanId, String ad, String durum, Date baslangicTarihi, Date bitisTarihi,
                          int adamGun) {
        String sql = "INSERT INTO Gorevler (ProjeId, CalisanId, Ad, Durum, BaslangicTarihi, BitisTarihi, AdamGun) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
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

    // Belirli Projeye Ait Görev Listeleme Metodu
    public List<String> gorevListeleByProje(int projeId) {
    String sql = "SELECT g.Id, g.Ad, g.Durum, g.BaslangicTarihi, g.BitisTarihi, g.AdamGun, " +
                 "COALESCE(c.Ad, 'Atanmamış') AS CalisanAd, " +
                 "COALESCE(c.Soyad, '') AS CalisanSoyad, " +
                 "p.Ad AS ProjeAd, " +
                 "DATEDIFF(g.BitisTarihi, CURRENT_DATE) AS GecikmeSuresi " +
                 "FROM Gorevler g " +
                 "LEFT JOIN Calisanlar c ON g.CalisanId = c.Id " + 
                 "JOIN Projeler p ON g.ProjeId = p.Id " +
                 "WHERE g.ProjeId = ?";
    return fetchGorevler(sql, projeId);
}

    private List<String> fetchGorevler(String sql, Object... params) {
        List<String> gorevler = new ArrayList<>();
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }

            try (ResultSet rs = stmt.executeQuery()) {
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

                    gorevler.add("Görev ID: " + id + ", Görev Adı: " + gorevAd + ", Durum: " + durum
                            + ", Başlangıç Tarihi: " + baslangicTarihi + ", Bitiş Tarihi: " + bitisTarihi
                            + ", Adam Gün: " + adamGun + ", Çalışan: " + calisanAd + " " + calisanSoyad
                            + ", Proje: " + projeAd + ", Gecikme Süresi: " + (gecikmeSuresi < 0 ? 0 : gecikmeSuresi) + " gün");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return gorevler;
    }

    // Görev Durumu Güncelleme
    public void gorevDurumuGuncelle(int gorevId) {
        String selectSql = "SELECT BitisTarihi, Durum FROM Gorevler WHERE Id = ?";
        String updateSql = "UPDATE Gorevler SET Durum = ? WHERE Id = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement selectStmt = conn.prepareStatement(selectSql);
             PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {

            selectStmt.setInt(1, gorevId);
            try (ResultSet rs = selectStmt.executeQuery()) {
                if (rs.next()) {
                    Date bitisTarihi = rs.getDate("BitisTarihi");
                    String mevcutDurum = rs.getString("Durum");
                    Date bugun = new Date(System.currentTimeMillis());

                    if ("Tamamlandı".equals(mevcutDurum)) {
                        System.out.println("Görev zaten tamamlanmış.");
                        return;
                    }

                    String yeniDurum = bugun.after(bitisTarihi) ? "Tamamlanacak" :
                            bugun.equals(bitisTarihi) ? "Tamamlandı" : "Devam Ediyor";

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

    // Çalışanları Yükleme
    public List<String> loadCalisanlar() {
        String sql = "SELECT Id, Ad, Soyad FROM Calisanlar";
        List<String> calisanlar = new ArrayList<>();
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int calisanId = rs.getInt("Id");
                String calisanAdi = rs.getString("Ad") + " " + rs.getString("Soyad");
                calisanlar.add("Çalışan ID: " + calisanId + ", İsim: " + calisanAdi);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return calisanlar;
    }
        // Bitiş Tarihine Rağmen Bitmeyen Görevleri Getirme
    public List<String> bitmeyenGorevleriGetir(int projeId) {
        String sql = "SELECT Id, Ad, Durum, BaslangicTarihi, BitisTarihi, AdamGun " +
                     "FROM Gorevler WHERE ProjeId = ? AND Durum != 'Tamamlandı' AND BitisTarihi < CURRENT_DATE";
        List<String> bitmeyenGorevler = new ArrayList<>();
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, projeId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("Id");
                    String ad = rs.getString("Ad");
                    String durum = rs.getString("Durum");
                    Date bitisTarihi = rs.getDate("BitisTarihi");
                    bitmeyenGorevler.add("Görev ID: " + id + ", Ad: " + ad + ", Durum: " + durum
                            + ", Bitiş Tarihi: " + bitisTarihi);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bitmeyenGorevler;
    }

    // Gecikmeyi Hesapla ve Güncelle
    public void gecikmeyiHesaplaVeGuncelle(int projeId) {
        List<String> bitmeyenGorevler = bitmeyenGorevleriGetir(projeId);

        if (!bitmeyenGorevler.isEmpty()) {
            int maxGecikme = 0;
            for (String gorev : bitmeyenGorevler) {
                // Parse date and calculate delay (assume the format includes date in "Bitiş Tarihi: " segment)
                LocalDate bitisTarihi = LocalDate.parse(gorev.split("Bitiş Tarihi: ")[1].trim());
                int gecikme = (int) ChronoUnit.DAYS.between(bitisTarihi, LocalDate.now());
                maxGecikme = Math.max(maxGecikme, gecikme);
            }

            if (maxGecikme > 0) {
                String sql = "UPDATE Projeler SET BitisTarihi = DATE_ADD(BitisTarihi, INTERVAL ? DAY) WHERE Id = ?";
                try (Connection conn = DatabaseHelper.getConnection();
                     PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setInt(1, maxGecikme);
                    stmt.setInt(2, projeId);
                    stmt.executeUpdate();
                    System.out.println("Proje bitiş tarihi gecikme süresine göre güncellendi.");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void bitisTarihiniIleriAt(int projeId) {
    String selectSql = "SELECT MAX(DATEDIFF(CURRENT_DATE, g.BitisTarihi)) AS MaxGecikme " +
                       "FROM Gorevler g WHERE g.ProjeId = ? AND g.Durum != 'Tamamlandı' AND g.BitisTarihi < CURRENT_DATE";
    String updateSql = "UPDATE Projeler SET BitisTarihi = DATE_ADD(BitisTarihi, INTERVAL ? DAY) WHERE Id = ?";

    try (Connection conn = DatabaseHelper.getConnection();
         PreparedStatement selectStmt = conn.prepareStatement(selectSql);
         PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {

        // Bitmeyen görevlerin maksimum gecikmesini hesapla
        selectStmt.setInt(1, projeId);
        try (ResultSet rs = selectStmt.executeQuery()) {
            if (rs.next()) {
                int maxGecikme = rs.getInt("MaxGecikme");

                if (maxGecikme > 0) {
                    // Bitiş tarihini gecikme kadar ileri at
                    updateStmt.setInt(1, maxGecikme);
                    updateStmt.setInt(2, projeId);
                    updateStmt.executeUpdate();
                    System.out.println("Proje bitiş tarihi " + maxGecikme + " gün ileri alındı.");
                } else {
                    System.out.println("Gecikme yok, bitiş tarihi değişmedi.");
                }
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
}
