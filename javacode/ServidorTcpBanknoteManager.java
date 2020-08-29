import java.net.*;
import java.io.*;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.lang.Integer; 

public class ServidorTcpBanknoteManager {

  static Connection dbConnection;

  public static String addNota(BanknoteManager.Req req, BanknoteManager.Res.Builder res) {
    int RA = req.getRA();
    String discCode = req.getDiscCode();
    int ano = req.getAno();
    int semestre = req.getSemestre();
    float nota = req.getNota();

    try {

      Statement statement = dbConnection.createStatement();

      /* Busca pelo aluno */
      ResultSet resultSet = statement.executeQuery("SELECT * FROM aluno WHERE (ra = " + String.valueOf(RA) + ");");
      if(!resultSet.isBeforeFirst()){
        res.setRetorno("RA inexistente");
        res.build();
        return "RA inexistente";
      }

      /* Busca pela disiplina */
      resultSet = statement.executeQuery("SELECT * FROM disciplina WHERE (codigo = '" + String.valueOf(discCode) + "');");
      if(!resultSet.isBeforeFirst()){
        res.setRetorno("Disciplina inexistente");
        res.build();
        return "Disciplina inexistente";
      }

      /* Busca pela matricula */
      resultSet = statement.executeQuery("SELECT * FROM matricula WHERE (ra_aluno = " + String.valueOf(RA) + " AND cod_disciplina = '" + String.valueOf(discCode) + "' AND ano = "+ String.valueOf(ano) +" AND semestre = "+ String.valueOf(semestre) +");");
      if(!resultSet.isBeforeFirst()){
        res.setRetorno("Matricula do aluno em " + String.valueOf(ano) + "/" + String.valueOf(semestre) + " inexistente");
        res.build();
        return ("Matricula do aluno em " + String.valueOf(ano) + "/" + String.valueOf(semestre) + " inexistente");
      }

      /* Atualiza nota */
      statement.execute("UPDATE matricula SET nota = " + String.valueOf(nota) + " WHERE (ra_aluno = " + String.valueOf(RA) + " AND cod_disciplina = '" + String.valueOf(discCode) + "' AND ano = "+ String.valueOf(ano) +" AND semestre = "+ String.valueOf(semestre) +");");
      res.setRetorno("1");
      res.build();

    } catch (SQLException e) {
      res.setRetorno(String.valueOf(e.getMessage()));
      res.build();
      return String.valueOf(e.getMessage());
    }
    return "1";
  }

  public static String rmNota(BanknoteManager.Req req, BanknoteManager.Res.Builder res) {
    int RA = req.getRA();
    String discCode = req.getDiscCode();
    int ano = req.getAno();
    int semestre = req.getSemestre();

    try {

      Statement statement = dbConnection.createStatement();

      /* Busca pelo aluno */
      ResultSet resultSet = statement.executeQuery("SELECT * FROM aluno WHERE (ra = " + String.valueOf(RA) + ");");
      if(!resultSet.isBeforeFirst()){
        res.setRetorno("RA inexistente");
        res.build();
        return "RA inexistente";
      }

      /* Busca pela disiplina */
      resultSet = statement.executeQuery("SELECT * FROM disciplina WHERE (codigo = '" + String.valueOf(discCode) + "');");
      if(!resultSet.isBeforeFirst()){
        res.setRetorno("Disciplina inexistente");
        res.build();
        return "Disciplina inexistente";
      }

      /* Busca pela matricula */
      resultSet = statement.executeQuery("SELECT * FROM matricula WHERE (ra_aluno = " + String.valueOf(RA) + " AND cod_disciplina = '" + String.valueOf(discCode) + "' AND ano = "+ String.valueOf(ano) +" AND semestre = "+ String.valueOf(semestre) +");");
      if(!resultSet.isBeforeFirst()){
        res.setRetorno("Matricula do aluno em " + String.valueOf(ano) + "/" + String.valueOf(semestre) + " inexistente");
        res.build();
        return ("Matricula do aluno em " + String.valueOf(ano) + "/" + String.valueOf(semestre) + " inexistente");
      }

      /* remove nota */
      statement.execute("UPDATE matricula SET nota = '' WHERE (ra_aluno = " + String.valueOf(RA) + " AND cod_disciplina = '" + String.valueOf(discCode) + "' AND ano = "+ String.valueOf(ano) +" AND semestre = "+ String.valueOf(semestre) +");");
      res.setRetorno("1");
      res.build();

    } catch (SQLException e) {
      res.setRetorno(String.valueOf(e.getMessage()));
      res.build();
      return String.valueOf(e.getMessage());
    }
    return "1";
  }

  public static String listAlunos(BanknoteManager.Req req, BanknoteManager.Res.Builder res) {
    String discCode = req.getDiscCode();
    int ano = req.getAno();
    int semestre = req.getSemestre();

    try {

      Statement statement = dbConnection.createStatement();

      /* Busca pela disiplina */
      ResultSet resultSet = statement.executeQuery("SELECT * FROM disciplina WHERE (codigo = '" + String.valueOf(discCode) + "');");
      if(!resultSet.isBeforeFirst()){
        res.setRetorno("Disciplina inexistente");
        res.build();
        return "Disciplina inexistente";
      }
      
      /* Lista alunos */
      resultSet = statement.executeQuery("SELECT * FROM aluno, matricula WHERE (select ra_aluno FROM matricula WHERE ano = " + String.valueOf(ano) + " AND semestre = " + String.valueOf(semestre) + " AND cod_disciplina = '" + String.valueOf(discCode) + "') AND matricula.ra_aluno = aluno.ra;");
      if(!resultSet.isBeforeFirst()){
        res.setRetorno("Nesta disciplina nao ha alunos matriculados em " + String.valueOf(ano) + "/" + String.valueOf(semestre));
        res.build();
        return ("Nesta disciplina nao ha alunos matriculados em " + String.valueOf(ano) + "/" + String.valueOf(semestre));
      }

      while (resultSet.next()) {

        /* Construindo Aluno */
        BanknoteManager.Aluno.Builder aluno = BanknoteManager.Aluno.newBuilder();
        
        /* Adicionando valores no aluno */
        aluno.setRA(resultSet.getInt("ra"));
        aluno.setNome(resultSet.getString("nome"));
        aluno.setPeriodo(resultSet.getInt("periodo"));
        aluno.setNota(resultSet.getFloat("nota"));
        aluno.setFaltas(resultSet.getInt("faltas"));
        aluno.build();

        /* Adicionando aluno */
        res.addAlunos(aluno);
      }
  
      res.setRetorno("1");
      res.build();

    } catch (SQLException e) {
      res.setRetorno(String.valueOf(e.getMessage()));
      res.build();
      return String.valueOf(e.getMessage());
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
      
      Socket clientSocket = listenSocket.accept();
      while (true) {

        DataInputStream inClient = new DataInputStream(clientSocket.getInputStream());
        DataOutputStream outClient = new DataOutputStream(clientSocket.getOutputStream());

        /* Recebe a mensagem */
        String valueStr = inClient.readLine();
        int sizeBuffer = Integer.valueOf(valueStr);
        byte[] buffer = new byte[sizeBuffer];
        inClient.read(buffer);

        /* realiza o unmarshalling */
        BanknoteManager.Req req = BanknoteManager.Req.parseFrom(buffer);
        String opCode = req.getOpCode();

        /* Prepara resposta */
        BanknoteManager.Res.Builder res = BanknoteManager.Res.newBuilder();

        /* Chama a função de acordo com o opCode */
        switch(opCode) {
          case "addNota":
            addNota(req, res);
          break;

          case "rmNota":
            rmNota(req, res);
          break;

          case "listAlunos":
            listAlunos(req, res);
          break;

          default:
            res.setRetorno("opCode invalido!");
          break;
        }
        
        /* Serializa resposta */
        byte[] msg = res.build().toByteArray();
        
        /* Manda tamanho da resposta */
        String msgSize = String.valueOf(msg.length) + " \n";
        byte[] size = msgSize.getBytes();
        outClient.write(size);
        
        /* Manda resposta */
        outClient.write(msg);

      } //while
    } catch (IOException e) {
      System.out.println("Listen_Socket:" + e.getMessage());
    } //catch
  } //main
} //class