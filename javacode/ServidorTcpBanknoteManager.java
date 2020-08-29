import java.net.*;
import java.io.*;
import java.sql.*;

public class ServidorTcpBanknoteManager {

  static Connection dbConnection;
  public static void main(String args[]) {
    
    // Conexao com banco de dados
    dbConnection = SQLiteJDBCDriverConnection.connect();

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

        /* Instancia a resposta */
        BanknoteManager.Res.Builder res = BanknoteManager.Res.newBuilder();

        /* Chama a funcionalidade de acordo com o opCode */
        switch(opCode) {
          case "addNota":
            Functionalities.addNota(req, res, dbConnection);
          break;

          case "rmNota":
            Functionalities.rmNota(req, res, dbConnection);
          break;

          case "listAlunos":
            Functionalities.listAlunos(req, res, dbConnection);
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