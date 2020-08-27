import java.net.*;
import java.io.*;

public class ServidorTcpAddressBook {
  public static void main(String args[]) {
    
    //Conexao com banco de dados
    SQLiteJDBCDriverConnection databaseCon = new SQLiteJDBCDriverConnection();
    databaseCon.main(new String[1]);

    try {
      int serverPort = 7000;
      ServerSocket listenSocket = new ServerSocket(serverPort);
      while (true) {
        Socket clientSocket = listenSocket.accept();

        DataInputStream inClient = new DataInputStream(clientSocket.getInputStream());
        DataOutputStream outClient = new DataOutputStream(clientSocket.getOutputStream());

        String valueStr = inClient.readLine();
        int sizeBuffer = Integer.valueOf(valueStr);
        byte[] buffer = new byte[sizeBuffer];
        inClient.read(buffer);

        /* realiza o unmarshalling */
        Addressbook.Req p = Addressbook.Req.parseFrom(buffer);

        /* exibe na tela */
        System.out.println("--\n" + p + "--\n");

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