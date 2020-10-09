/**
* Functionalities.java
* Código das funcionalidades de um serviço de gerenciamento 
* de notas de alunos usando o método de serialização Protocol Buffer.
* Autores: Lucas Souza Santos & Alan Rodrigo Patriarca 
* Data de Criação: 26/08/2020
* Ultima atualização: 30/08/2020
 */

import java.sql.*;

public class Functionalities {

  public static String addNota(BanknoteManager.Req req, BanknoteManager.Res.Builder res, Connection dbConnection) {
    /* Obtendo os dados para a busca e insercao */
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
        return "RA inexistente";
      }

      /* Busca pela disiplina */
      resultSet = statement.executeQuery("SELECT * FROM disciplina WHERE (codigo = '" + String.valueOf(discCode) + "');");
      if(!resultSet.isBeforeFirst()){
        res.setRetorno("Disciplina inexistente");
        return "Disciplina inexistente";
      }

      /* Busca pela matricula */
      resultSet = statement.executeQuery("SELECT * FROM matricula WHERE (ra_aluno = " + String.valueOf(RA) + " AND cod_disciplina = '" + String.valueOf(discCode) + "' AND ano = "+ String.valueOf(ano) +" AND semestre = "+ String.valueOf(semestre) +");");
      if(!resultSet.isBeforeFirst()){
        res.setRetorno("Matricula do aluno em " + String.valueOf(ano) + "/" + String.valueOf(semestre) + " inexistente");
        return ("Matricula do aluno em " + String.valueOf(ano) + "/" + String.valueOf(semestre) + " inexistente");
      }

      /* Atualiza nota */
      statement.execute("UPDATE matricula SET nota = " + String.valueOf(nota) + " WHERE (ra_aluno = " + String.valueOf(RA) + " AND cod_disciplina = '" + String.valueOf(discCode) + "' AND ano = "+ String.valueOf(ano) +" AND semestre = "+ String.valueOf(semestre) +");");
      res.setRetorno("1");

    } catch (SQLException e) {
      res.setRetorno(String.valueOf(e.getMessage()));
      return String.valueOf(e.getMessage());
    }
    return "1";
  }

  public static String rmNota(BanknoteManager.Req req, BanknoteManager.Res.Builder res, Connection dbConnection) {
    /* Obtendo os dados para a busca e remocao */
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
        return "RA inexistente";
      }

      /* Busca pela disiplina */
      resultSet = statement.executeQuery("SELECT * FROM disciplina WHERE (codigo = '" + String.valueOf(discCode) + "');");
      if(!resultSet.isBeforeFirst()){
        res.setRetorno("Disciplina inexistente");
        return "Disciplina inexistente";
      }

      /* Busca pela matricula */
      resultSet = statement.executeQuery("SELECT * FROM matricula WHERE (ra_aluno = " + String.valueOf(RA) + " AND cod_disciplina = '" + String.valueOf(discCode) + "' AND ano = "+ String.valueOf(ano) +" AND semestre = "+ String.valueOf(semestre) +");");
      if(!resultSet.isBeforeFirst()){
        res.setRetorno("Matricula do aluno em " + String.valueOf(ano) + "/" + String.valueOf(semestre) + " inexistente");
        return ("Matricula do aluno em " + String.valueOf(ano) + "/" + String.valueOf(semestre) + " inexistente");
      }

      /* remove nota */
      statement.execute("UPDATE matricula SET nota = -1 WHERE (ra_aluno = " + String.valueOf(RA) + " AND cod_disciplina = '" + String.valueOf(discCode) + "' AND ano = "+ String.valueOf(ano) +" AND semestre = "+ String.valueOf(semestre) +");");
      res.setRetorno("1");

    } catch (SQLException e) {
      res.setRetorno(String.valueOf(e.getMessage()));
      return String.valueOf(e.getMessage());
    }
    return "1";
  }

  public static String listAlunos(BanknoteManager.Req req, BanknoteManager.Res.Builder res, Connection dbConnection) {
    /* Obtendo os dados para a busca */
    String discCode = req.getDiscCode();
    int ano = req.getAno();
    int semestre = req.getSemestre();

    try {

      Statement statement = dbConnection.createStatement();

      /* Busca pela disiplina */
      ResultSet resultSet = statement.executeQuery("SELECT * FROM disciplina WHERE (codigo = '" + String.valueOf(discCode) + "');");
      if(!resultSet.isBeforeFirst()){
        res.setRetorno("Disciplina inexistente");
        return "Disciplina inexistente";
      }
      
      /* Lista alunos */
      resultSet = statement.executeQuery("SELECT * FROM aluno WHERE (select ra_aluno FROM matricula WHERE ano = " + String.valueOf(ano) + " AND semestre = " + String.valueOf(semestre) + " AND cod_disciplina = '" + String.valueOf(discCode) + "' AND matricula.ra_aluno = aluno.ra);");
      if(!resultSet.isBeforeFirst()){
        res.setRetorno("Nesta disciplina nao ha alunos matriculados em " + String.valueOf(ano) + "/" + String.valueOf(semestre));
        return ("Nesta disciplina nao ha alunos matriculados em " + String.valueOf(ano) + "/" + String.valueOf(semestre));
      }

      while (resultSet.next()) {

        /* Construindo Aluno */
        BanknoteManager.Aluno.Builder aluno = BanknoteManager.Aluno.newBuilder();
        
        /* Adicionando valores no aluno */
        aluno.setRA(resultSet.getInt("ra"));
        aluno.setNome(resultSet.getString("nome"));
        aluno.setPeriodo(resultSet.getInt("periodo"));

        /* Adicionando aluno */
        res.addAlunos(aluno);
      }
  
      res.setRetorno("1");

    } catch (SQLException e) {
      res.setRetorno(String.valueOf(e.getMessage()));
      return String.valueOf(e.getMessage());
    }
    return "1";
  }
}
