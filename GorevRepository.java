import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GorevRepository {

    private Connection baglan() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/ProjeYonetim";
        String kullaniciAdi = "root";
        String sifre = "";
        return DriverManager.getConnection(url, kullaniciAdi, sifre);
    }

    public void gorevEkle(int projeId, int calisanId, String ad, String durum, String baslangic, String bitis, int adamGun) {
        String sql = "INSERT INTO Gorevler (ProjeId, CalisanId, Ad, Durum, BaslangicTarihi, BitisTarihi, AdamGun) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = baglan(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, projeId);
            stmt.setInt(2, calisanId);
            stmt.setString(3, ad);
            stmt.setString(4, durum);
            stmt.setDate(5, Date.valueOf(baslangic));
            stmt.setDate(6, Date.valueOf(bitis));
            stmt.setInt(7, adamGun);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> gorevListele() {
        List<String> gorevler = new ArrayList<>();
        String sql = "SELECT g.Id, g.Ad, g.Durum, g.BaslangicTarihi, g.BitisTarihi, c.Ad, c.Soyad " +
                     "FROM Gorevler g JOIN Calisanlar c ON g.CalisanId = c.Id";

        try (Connection conn = baglan(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String gorev = "ID: " + rs.getInt("Id") + ", Ad: " + rs.getString("Ad") +
                               ", Durum: " + rs.getString("Durum") +
                               ", Başlangıç: " + rs.getDate("BaslangicTarihi") +
                               ", Bitiş: " + rs.getDate("BitisTarihi") +
                               ", Çalışan: " + rs.getString("Ad") + " " + rs.getString("Soyad");
                gorevler.add(gorev);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return gorevler;
    }

    public void gorevDurumGuncelle(int gorevId, String yeniDurum) {
        String sql = "UPDATE Gorevler SET Durum = ? WHERE Id = ?";

        try (Connection conn = baglan(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, yeniDurum);
            stmt.setInt(2, gorevId);
            stmt.executeUpdate();

            if ("Tamamlandı".equalsIgnoreCase(yeniDurum)) {
                projeBitisTarihiGuncelle(gorevId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void projeBitisTarihiGuncelle(int gorevId) {
        String sql = "SELECT g.BitisTarihi, p.Id, p.BitisTarihi AS ProjeBitis FROM Gorevler g " +
                     "JOIN Projeler p ON g.ProjeId = p.Id WHERE g.Id = ?";

        try (Connection conn = baglan(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, gorevId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Date gorevBitis = rs.getDate("BitisTarihi");
                    int projeId = rs.getInt("Id");
                    Date projeBitis = rs.getDate("ProjeBitis");

                    if (gorevBitis != null && gorevBitis.after(projeBitis)) {
                        String guncelleSql = "UPDATE Projeler SET BitisTarihi = ? WHERE Id = ?";
                        try (PreparedStatement guncelleStmt = conn.prepareStatement(guncelleSql)) {
                            guncelleStmt.setDate(1, gorevBitis);
                            guncelleStmt.setInt(2, projeId);
                            guncelleStmt.executeUpdate();
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}