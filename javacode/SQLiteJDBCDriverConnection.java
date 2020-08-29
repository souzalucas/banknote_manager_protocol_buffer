import java.sql.*;

public class SQLiteJDBCDriverConnection {
  static Connection connection;

  public static Connection connect() {    
    try {

      connection = DriverManager.getConnection("jdbc:sqlite:../database/gerenciamento_notas.db");

      System.out.println("Conexão com o banco de dados realizada!");

    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return connection;
  }
}