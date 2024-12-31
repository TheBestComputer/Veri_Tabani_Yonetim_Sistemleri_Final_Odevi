import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

class GorevRepository {

    // Görev Ekleme Metodu
    public void gorevEkle(int projeId, int calisanId, String ad, String durum, Date baslangicTarihi, Date bitisTarihi,
            float adamGun) {
        String sql = "INSERT INTO Gorevler (ProjeId, CalisanId, Ad, Durum, BaslangicTarihi, BitisTarihi, AdamGun) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseHelper.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, projeId);
            stmt.setInt(2, calisanId);
            stmt.setString(3, ad);
            stmt.setString(4, durum);
            stmt.setDate(5, baslangicTarihi);
            stmt.setDate(6, bitisTarihi);
            stmt.setFloat(7, adamGun);
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
                "DATEDIFF(CURRENT_DATE, g.BitisTarihi) AS GecikmeSuresi " +
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
                    float adamGun = rs.getFloat("AdamGun");
                    String calisanAd = rs.getString("CalisanAd");
                    String calisanSoyad = rs.getString("CalisanSoyad");
                    String projeAd = rs.getString("ProjeAd");
                    int gecikmeSuresi = rs.getInt("GecikmeSuresi");

                    gorevler.add("Görev ID: " + id + ", Görev Adı: " + gorevAd + ", Durum: " + durum
                            + ", Başlangıç Tarihi: " + baslangicTarihi + ", Bitiş Tarihi: " + bitisTarihi
                            + ", Adam Gün: " + adamGun + ", Çalışan: " + calisanAd + " " + calisanSoyad
                            + ", Proje: " + projeAd + ", Gecikme Süresi: " + (gecikmeSuresi < 0 ? 0 : gecikmeSuresi)
                            + " gün");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return gorevler;
    }

    // Görev Durumu Güncelleme
    public void gorevDurumuGuncelle(int gorevId) {
        String selectSql = "SELECT BaslangicTarihi, Durum FROM Gorevler WHERE Id = ?";
        String updateSql = "UPDATE Gorevler SET Durum = ? WHERE Id = ?";
        try (Connection conn = DatabaseHelper.getConnection();
                PreparedStatement selectStmt = conn.prepareStatement(selectSql);
                PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {

            selectStmt.setInt(1, gorevId);
            try (ResultSet rs = selectStmt.executeQuery()) {
                if (rs.next()) {
                    Date baslangicTarihi = rs.getDate("BaslangicTarihi");
                    String mevcutDurum = rs.getString("Durum");
                    Date bugun = new Date(System.currentTimeMillis());

                    if ("Tamamlandı".equals(mevcutDurum)) {
                        System.out.println("Görev zaten tamamlanmış.");
                        return;
                    }

                    String yeniDurum = bugun.after(baslangicTarihi) ? "Devam Ediyor" : "Tamamlanacak";

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

    public void tamamlandiOlarakIsaretle(int gorevId) throws Exception {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {

            connection = DatabaseHelper.getConnection();


            String kontrolQuery = "SELECT Durum, BitisTarihi FROM Gorevler WHERE Id = ?";
            preparedStatement = connection.prepareStatement(kontrolQuery);
            preparedStatement.setInt(1, gorevId);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String mevcutDurum = resultSet.getString("durum");
                if ("Tamamlandı".equalsIgnoreCase(mevcutDurum)) {
                    throw new Exception("Görev zaten tamamlandı olarak işaretlenmiş.");
                }
            } else {
                throw new Exception("Görev bulunamadı.");
            }


            String guncelleQuery = "UPDATE Gorevler SET Durum = ? WHERE Id = ?";
            preparedStatement = connection.prepareStatement(guncelleQuery);
            preparedStatement.setString(1, "Tamamlandı");
            preparedStatement.setInt(2, gorevId);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                throw new Exception("Görev durumu güncellenemedi.");
            }

        } catch (SQLException e) {
            throw new Exception("Veritabanı hatası: " + e.getMessage(), e);
        }
    }

}
