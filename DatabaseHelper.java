import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;

public class DatabaseHelper {
    private static final String URL = "jdbc:mysql://localhost:3306/ProjeYonetim?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "Furkan52";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    public static void main(String[] args) {
        try (Connection connection = getConnection()) {
            if (connection != null) {
                System.out.println("Veritabanına başarıyla bağlanıldı.");
            } else {
                System.out.println("Veritabanı bağlantısı başarısız.");
            }
        } catch (SQLException e) {
            System.out.println("Bağlantı hatası: " + e.getMessage());
        }
    }
}
