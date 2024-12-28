
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

    public Map<Integer, String> projeIdListele() {
        Map<Integer, String> projeMap = new LinkedHashMap<>();
        String sql = "SELECT Id, Ad FROM Projeler";

        try (Connection conn = DatabaseHelper.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                projeMap.put(rs.getInt("Id"), rs.getString("Ad"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return projeMap;
    }

    public List<String> projeListele(int kullaniciId) {
        String sql = "SELECT * FROM Projeler WHERE kullaniciId = ?";
        List<String> projeler = new ArrayList<>();
        try (Connection conn = DatabaseHelper.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ) {

            stmt.setInt(1, kullaniciId);
            try(ResultSet rs = stmt.executeQuery()){
                while (rs.next()) {
                    String id = rs.getString("Id");
                    String ad = rs.getString("Ad");
                    String baslangicTarihi = rs.getString("BaslangicTarihi");
                    String bitisTarihi = rs.getString("BitisTarihi");
                    projeler.add(id + " - " + ad + " - " + baslangicTarihi + " - " + bitisTarihi);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return projeler;
    }

    public List<String> projeGorevListele(int projeId) {
        List<String> gorevler = new ArrayList<>();
        String sql = "SELECT * FROM Gorevler WHERE ProjeId = ?";

        try (Connection conn = DatabaseHelper.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, projeId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String gorevAdi = rs.getString("GorevAdi");
                    String durum = rs.getString("Durum");
                    gorevler.add(gorevAdi + " - " + durum);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return gorevler;
    }
}
