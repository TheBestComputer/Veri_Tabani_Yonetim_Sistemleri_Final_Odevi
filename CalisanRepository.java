import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

class CalisanRepository {

    public void calisanEkle(String ad, String soyad, String email, int kullaniciId) {
        String sql = "INSERT INTO Calisanlar (Ad, Soyad, Email, kullaniciId) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseHelper.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, ad);
            stmt.setString(2, soyad);
            stmt.setString(3, email);
            stmt.setInt(4, kullaniciId);

            stmt.executeUpdate();
            System.out.println("Personel başarıyla eklendi.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Map<Integer, String> calisanIdListele(int kullaniciId) {
        Map<Integer, String> calisanMap = new LinkedHashMap<>();
        String sql = "SELECT Id, CONCAT(Ad, ' ', Soyad) AS AdSoyad FROM Calisanlar WHERE kullaniciId = ?";

        try (Connection conn = DatabaseHelper.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql);) {

            stmt.setInt(1, kullaniciId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                calisanMap.put(rs.getInt("Id"), rs.getString("AdSoyad"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return calisanMap;
    }

    public List<String> calisanListele(int kullaniciId) {
        String sql = "SELECT * FROM Calisanlar WHERE kullaniciId = ?";
        List<String> calisanlar = new ArrayList<>();
        try (Connection conn = DatabaseHelper.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql);) {

            stmt.setInt(1, kullaniciId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("Id");
                String ad = rs.getString("Ad");
                String soyad = rs.getString("Soyad");
                String email = rs.getString("Email");
                calisanlar.add(id + " - " + ad + "-" + soyad + " - " + email);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return calisanlar;
    }

    public boolean calisanSil(int id) {
        String sql = "DELETE FROM Calisanlar WHERE Id = ?";
        try (Connection conn = DatabaseHelper.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Çalışan başarıyla silindi.");
            } else {
                System.out.println("Belirtilen ID'ye sahip bir çalışan bulunamadı.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean calisanGuncelle(int id, String yeniAd, String yeniSoyad, String yeniEmail) {
        String sql = "UPDATE Calisanlar SET Ad = ?, Soyad = ?, Email = ? WHERE Id = ?";
        try (Connection conn = DatabaseHelper.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, yeniAd);
            stmt.setString(2, yeniSoyad);
            stmt.setString(3, yeniEmail);
            stmt.setInt(4, id);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Çalışan bilgileri başarıyla güncellendi.");
            } else {
                System.out.println("Belirtilen ID'ye sahip bir çalışan bulunamadı.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public void calisanGorevDurumlariniListele(int calisanId, StringBuilder sb) {
        String sql = "SELECT g.Ad, g.Durum, g.BaslangicTarihi, g.BitisTarihi "
                + "FROM Gorevler g "
                + "WHERE g.CalisanId = ?";

        try (Connection conn = DatabaseHelper.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, calisanId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String gorevAd = rs.getString("Ad");
                    String durum = rs.getString("Durum");
                    Date baslangicTarihi = rs.getDate("BaslangicTarihi");
                    Date bitisTarihi = rs.getDate("BitisTarihi");

                    sb.append(String.format("Görev Adı: %s, Durum: %s, Başlangıç Tarihi: %s, Bitiş Tarihi: %s\n",
                            gorevAd, durum, baslangicTarihi, bitisTarihi));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void calisanGorevTamamlamaDurumu(int calisanId, StringBuilder sb) {
        String sql = "SELECT g.Durum, g.BitisTarihi, CURRENT_DATE AS Bugun, "
                + "       CASE WHEN g.Durum = 'Tamamlandı' AND g.BitisTarihi >= CURRENT_DATE THEN 'Zamanında Tamamlandı' "
                + "            WHEN g.Durum = 'Tamamlandı' AND g.BitisTarihi < CURRENT_DATE THEN 'Geç Tamamlandı' "
                + "            ELSE 'Tamamlanmadı' END AS TamamlamaDurumu "
                + "FROM Gorevler g "
                + "WHERE g.CalisanId = ?";

        Map<String, Integer> tamamlamaDurumuSayaci = new HashMap<>();
        tamamlamaDurumuSayaci.put("Zamanında Tamamlandı", 0);
        tamamlamaDurumuSayaci.put("Geç Tamamlandı", 0);
        tamamlamaDurumuSayaci.put("Tamamlanmadı", 0);

        try (Connection conn = DatabaseHelper.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, calisanId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String tamamlamaDurumu = rs.getString("TamamlamaDurumu");
                    tamamlamaDurumuSayaci.put(tamamlamaDurumu, tamamlamaDurumuSayaci.get(tamamlamaDurumu) + 1);
                }
            }

            tamamlamaDurumuSayaci.forEach((durum, sayi) -> sb.append(String.format("%s: %d\n", durum, sayi)));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
