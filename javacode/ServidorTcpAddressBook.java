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


        String valueStr = inClient.readLine();
        int sizeBuffer = Integer.valueOf(valueStr);
        byte[] buffer = new byte[sizeBuffer];
        inClient.read(buffer);

        /* realiza o unmarshalling */
        Addressbook.Person p = Addressbook.Person.parseFrom(buffer);

        /* exibe na tela */
        System.out.println("--\n" + p + "--\n");
      } //while
    } catch (IOException e) {
      System.out.println("Listen_Socket:" + e.getMessage());
    } //catch
  } //main
} //class