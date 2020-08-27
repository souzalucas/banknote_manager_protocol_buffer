import java.sql.*;

public class SQLiteJDBCDriverConnection {
  private static void connect() {    
    try (Connection connection = DriverManager.getConnection("jdbc:sqlite:../database/gerenciamento_notas.db")) {

      System.out.println("Conexão realizada !!!!");

    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  public static void main(String[] args) {
    connect();
  } 
}