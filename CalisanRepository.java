import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

class CalisanRepository {
    public static void main(String[] args) {
        CalisanRepository repository = new CalisanRepository();

        // Proje ekleme örneği
        repository.calisanEkle("Alperen", "Sağut", "revengeofzamazingo@gmail.com");

        // Proje listeleme örneği
        List<String> calisanlar = repository.calisanListele();
        for (String calisan : calisanlar) {
            System.out.println(calisan);
        }
    }

    public void calisanEkle(String ad, String soyad, String email) {
        String sql = "INSERT INTO Calisanlar (Ad, Soyad, Email) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, ad);
            stmt.setString(2, soyad);
            stmt.setString(3, email);

            stmt.executeUpdate();
            System.out.println("Personel başarıyla eklendi.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> calisanListele() {
        String sql = "SELECT * FROM Calisanlar";
        List<String> calisanlar = new ArrayList<>();
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String ad = rs.getString("Ad");
                String soyad = rs.getString("Soyad");
                String email = rs.getString("Email");
                calisanlar.add(ad + ": " + soyad + " - " + email);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return calisanlar;
    }
}