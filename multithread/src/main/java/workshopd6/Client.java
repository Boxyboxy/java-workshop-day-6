package workshopd6;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class Client {
  public static void main(String[] args) throws IOException {
    String IPString;
    int port;

    // if no arguments are in the command line
    if (args.length == 0) {
      // assigning variables to be used in constructor
      IPString = "localhost";
      port = 12345;
    } else {
      String[] argumentsFromCL = args[0].split(":");
      IPString = argumentsFromCL[0];
      port = Integer.parseInt(argumentsFromCL[1]);
    }

    // constructing object
    Client client = new Client(IPString, port);

    // establish connection
    client.establishConnection();
    client.start(System.in);

    /*
     * // storing fortune cookie message in a string
     * String messageReceived = client.requestCookie();
     * // displaying message
     * System.out.println(messageReceived);
     * // close connection
     * client.closeConnection();
     */
  }

  // attributes
  private int clientPort;
  private String IPaddress;
  private Socket socket;
  private String fortuneMessage;

  // constructor
  public Client(String IPaddress, int port) {
    this.clientPort = port;
    this.IPaddress = IPaddress;

  }

  // establish connection, throwing IOException required
  public void establishConnection() throws IOException {
    this.socket = new Socket(this.IPaddress, this.clientPort);
    System.out.println("Relevant information on the socket: " + this.socket);
  }

  // start
  public void start(InputStream fromConsole) throws IOException {
    System.out.println("Welcome. Enter 'get-cookie' to get a revelation");
    while (true) {
      String input;
      // need to use reader/scanner when reading from console
      BufferedReader br = new BufferedReader(new InputStreamReader(fromConsole));
      input = br.readLine().trim();

      // processInput is set up in such a way that once it returns false, program
      // exits while loop
      if (this.processInput(input)) {
        return;
      }
    }
  }

  // if true is returned, programme will be exited based on code in start()
  // function
  private boolean processInput(String input) throws IOException {
    if (input.isBlank()) {
      System.err.println("Input cannot be blank.");
      return false;
    }

    switch (input.toLowerCase()) {
      case "get-cookie":
        this.sendToServer(input);
        System.out.println("Enter 'cookie-text' to open the cookie");
        this.fortuneMessage = receiveFromServer();
        break;
      case "cookie-text":
        System.out.println(fortuneMessage);
        System.out.println("Enter 'get-cookie' to get another cookie, or enter 'close' to exit.");
        break;
      case "close":
        this.sendToServer(input);
        this.socket.close();
        return true;
      default:
        System.out.println("Command not recognized. Please enter only 'get-cookie' or 'cookie-text'.");
    }
    return false;
  }

  // sending message to server
  private void sendToServer(String message) throws IOException {
    // create output stream for the connected socket
    OutputStream os = socket.getOutputStream();
    DataOutputStream dos = new DataOutputStream(os);
    // write message
    dos.writeUTF(message);
    dos.flush();
  }

  private String receiveFromServer() throws IOException {
    // get input stream from the socket
    InputStream is = socket.getInputStream();
    DataInputStream dis = new DataInputStream(is);
    // read the response from server
    String response = dis.readUTF();
    return response;
  }

}
