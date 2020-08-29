/**
* SQLiteJDBCDriverConnection.java
* Código da Conexão com o banco de dados de um serviço de gerenciamento 
* de notas de alunos usando o método de serialização Protocol Buffer.
* Autores: Lucas Souza Santos & Alan Rodrigo Patriarca 
* Data de Criação: 26/08/2020
* Ultima atualização: 30/08/2020
 */

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