import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class KullaniciRepository {
    public boolean authenticateUser(String email, String password) {
        try (Connection connection = DatabaseHelper.getConnection()) {
            String query = "SELECT * FROM Kullanicilar WHERE Email = ? AND password = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, email);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int kullaniciId(String email, String password) {
        try (Connection connection = DatabaseHelper.getConnection()) {
            String query = "SELECT Id FROM Kullanicilar WHERE Email = ? AND password = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, email);
            statement.setString(2, password);
    
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("Id"); // Kullanıcı Id'sini döndür
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Kullanıcı bulunamazsa -1 döndür
    }

    public boolean registerUser(String email, String password) {
        try (Connection connection = DatabaseHelper.getConnection()) {
            String query = "INSERT INTO Kullanicilar (Email, password) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, email);
            statement.setString(2, password);

            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
