import java.net.*;
import java.io.*;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ServidorTcpAddressBook {

  static Connection dbConnection;

  public static String addNota(Addressbook.Req req) {
    // System.out.println("--\n" + req + "--\n");
    
    int RA = req.getRA();
    String discCode = req.getDiscCode();
    float nota = req.getNota();

    try {

      Statement statement = dbConnection.createStatement();

      /* Busca pelo aluno */
      ResultSet resultSet = statement.executeQuery("SELECT * FROM aluno WHERE (ra = " + String.valueOf(RA) + ");");
      if(!resultSet.next()){
        return "RA não existe";
      }

      /* Busca pela disiplina */
      resultSet = statement.executeQuery("SELECT * FROM disciplina WHERE (codigo = '" + String.valueOf(discCode) + "');");
      if(!resultSet.next()){
        return "Disciplina não existe";
      }

      /* Atualiza nota */
      statement.execute("UPDATE matricula SET nota = " + nota + " WHERE (ra_aluno = " + String.valueOf(RA) + " AND cod_disciplina = '" + String.valueOf(discCode) + "');");

    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return "1";
  }

  public static String rmNota(Addressbook.Req req) {
    int RA = req.getRA();
    String discCode = req.getDiscCode();

    try {

      Statement statement = dbConnection.createStatement();

      /* Busca pelo aluno */
      ResultSet resultSet = statement.executeQuery("SELECT * FROM aluno WHERE (ra = " + String.valueOf(RA) + ");");
      if(!resultSet.next()){
        return "RA não existe";
      }

      /* Busca pela disiplina */
      resultSet = statement.executeQuery("SELECT * FROM disciplina WHERE (codigo = '" + String.valueOf(discCode) + "');");
      if(!resultSet.next()){
        return "Disciplina não existe";
      }

      /* remove nota */
      statement.execute("UPDATE matricula SET nota = '' WHERE (ra_aluno = " + String.valueOf(RA) + " AND cod_disciplina = '" + String.valueOf(discCode) + "');");

    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return "1";
  }

  public static String listAlunos(Addressbook.Req req) {
    String discCode = req.getDiscCode();
    int ano = req.getAno();
    int semestre = req.getSemestre();

    try {

      Statement statement = dbConnection.createStatement();

      /* Busca pela disiplina */
      ResultSet resultSet = statement.executeQuery("SELECT * FROM disciplina WHERE (codigo = '" + String.valueOf(discCode) + "');");
      if(!resultSet.next()){
        return "Disciplina não existe";
      }
      
      /* Lista alunos */

      resultSet = statement.executeQuery("SELECT * FROM aluno WHERE (select ra_aluno FROM matricula WHERE ano = " + ano + " AND semestre = " + semestre + " AND cod_disciplina = '" + discCode + "');");
      // if(!resultSet.next()){
      //   return "Não há alunos";
      // }

      while (resultSet.next()) {
        System.out.println(resultSet.getInt("ra")); 
      }

    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return "1";
  }

  public static void main(String args[]) {
    
    // Conexao com banco de dados
    SQLiteJDBCDriverConnection driverCon = new SQLiteJDBCDriverConnection();
    driverCon.main(new String[1]);
    dbConnection = driverCon.getConnection();

    try {
      int serverPort = 7000;
      ServerSocket listenSocket = new ServerSocket(serverPort);
      while (true) {
        Socket clientSocket = listenSocket.accept();

        DataInputStream inClient = new DataInputStream(clientSocket.getInputStream());
        DataOutputStream outClient = new DataOutputStream(clientSocket.getOutputStream());

        /* Recebe a mensagem */
        String valueStr = inClient.readLine();
        int sizeBuffer = Integer.valueOf(valueStr);
        byte[] buffer = new byte[sizeBuffer];
        inClient.read(buffer);

        /* realiza o unmarshalling */
        Addressbook.Req req = Addressbook.Req.parseFrom(buffer);
        String opCode = req.getOpCode();

        switch(opCode) {
          case "addNota":
            System.out.println("add");
            addNota(req);
          break;

          case "rmNota":
            System.out.println("rm");
            rmNota(req);
          break;

          case "listAlunos":
            System.out.println("list");
            listAlunos(req);
          break;
        }
        
        /* exibe na tela */
        // System.out.println("--\n" + p + "--\n");

        Addressbook.Res.Builder res = Addressbook.Res.newBuilder();
        
        Addressbook.Aluno.Builder aluno = Addressbook.Aluno.newBuilder();

        aluno.setRA(Integer.valueOf(1858));
        aluno.setNome("Lucas Souza");
        aluno.setPeriodo(Integer.valueOf(6));
        aluno.build();

        res.addAlunos(aluno);
        res.setRetorno("1");
        res.build();

        byte[] msg = res.build().toByteArray();

        outClient.write(msg);

      } //while
    } catch (IOException e) {
      System.out.println("Listen_Socket:" + e.getMessage());
    } //catch
  } //main
} //class