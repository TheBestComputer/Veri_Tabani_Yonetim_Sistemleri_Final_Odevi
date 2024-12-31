
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

class ProjeRepository {

    public void projeEkle(String ad, String baslangicTarihi, String bitisTarihi, String kullaniciId) {
        String sql = "INSERT INTO Projeler (Ad, BaslangicTarihi, BitisTarihi, kullaniciId) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseHelper.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            int kullaniciIdSayisi = Integer.parseInt(kullaniciId);
            stmt.setString(1, ad);
            stmt.setString(2, baslangicTarihi);
            stmt.setString(3, bitisTarihi);
            stmt.setInt(4, kullaniciIdSayisi);

            stmt.executeUpdate();
            System.out.println("Proje başarıyla eklendi.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public List<String> projeListele(int kullaniciId) {
        String sql = "SELECT p.Id, p.Ad, p.BaslangicTarihi, p.BitisTarihi, " +
                "(SELECT COUNT(*) FROM Gorevler g WHERE g.ProjeId = p.Id AND g.Durum != 'Tamamlandı' AND g.BitisTarihi < CURRENT_DATE) AS BitmeyenGorevSayisi, "
                +
                "(SELECT MAX(DATEDIFF(CURRENT_DATE, g.BitisTarihi)) FROM Gorevler g WHERE g.ProjeId = p.Id AND (g.Durum != 'Tamamlandı' OR g.BitisTarihi < CURRENT_DATE)) AS MaxGecikme "
                +
                "FROM Projeler p WHERE p.KullaniciId = ?";
        List<String> projeler = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try (Connection conn = DatabaseHelper.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, kullaniciId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String id = rs.getString("Id");
                    String ad = rs.getString("Ad");
                    LocalDate baslangicTarihi = rs.getDate("BaslangicTarihi").toLocalDate();
                    LocalDate bitisTarihi = rs.getDate("BitisTarihi").toLocalDate();
                    int bitmeyenGorevSayisi = rs.getInt("BitmeyenGorevSayisi");
                    int maxGecikme = rs.getInt("MaxGecikme");

                    // Bitiş tarihini gecikme kadar ileri at
                    LocalDate guncellenmisBitisTarihi = maxGecikme > 0 ? bitisTarihi.plusDays(maxGecikme) : bitisTarihi;

                    projeler.add(id + " - " + ad +
                            " - " + baslangicTarihi.format(formatter) +
                            " - " + guncellenmisBitisTarihi.format(formatter) +
                            " - Bitmeyen Görev Sayısı: " + bitmeyenGorevSayisi +
                            " - Maksimum Gecikme: " + (maxGecikme > 0 ? maxGecikme + " gün" : "Yok"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return projeler;
    }
}
