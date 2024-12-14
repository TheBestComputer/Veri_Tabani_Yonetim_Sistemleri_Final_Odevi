import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ProjeRepository {
    public void projeEkle(String ad, String baslangicTarihi, String bitisTarihi) {
        String sql = "INSERT INTO Projeler (Ad, BaslangicTarihi, BitisTarihi) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, ad);
            stmt.setString(2, baslangicTarihi);
            stmt.setString(3, bitisTarihi);

            stmt.executeUpdate();
            System.out.println("Proje başarıyla eklendi.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ProjeRepository repository = new ProjeRepository();
        // Test için proje ekleyelim
        repository.projeEkle("Proje 1", "2024-01-01", "2024-12-31");
    }
}

