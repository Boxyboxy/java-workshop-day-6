package workshopd6;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
  // java-cp fortunecookie.jar packageName.Server 12345 cookie_file.txt
  // above line runs server class
  // String[] args is equivalent to '12345 cookie_file.txt'
  public static void main(String[] args) throws IOException {

    int port;
    String fileString;
    if (args.length == 0) {
      port = 12345;
      fileString = "cookie_file.txt";
    } else {
      port = Integer.parseInt(args[0]);
      fileString = args[1];
    }
    // constructing object
    Server server = new Server(port, fileString);
    // start server
    server.startServer();
    // establish connection
    server.connectServer();
    // close connection
    server.closeConnection();

  }

  // attributes
  private int serverPort;
  private Cookie cookieJar;
  private ServerSocket serverSocket;
  private Socket socket;
  // ExecutorService is a built-in java API to eneable multi-threaded programming
  private ExecutorService threadPool;

  // constructor
  public Server(int serverPort, String cookieFileName) {
    this.serverPort = serverPort;
    this.cookieJar = new Cookie(cookieFileName);
  }

  // start server
  public void startServer() throws IOException {
    this.serverSocket = new ServerSocket(this.serverPort);
    // a threadpool with 3 threads is created
    this.threadPool = Executors.newFixedThreadPool(3);
    System.out.println("Server started on port: " + this.serverPort);
  }

  // establish connection
  public void connectServer() throws IOException {
    // while loop is used as you want the serverSocket to constantly accept new
    // sockets that are incoming
    int count = 1;
    while (true) {
      System.out.println("Connecting fella " + count);
      // Scans and accepts new socket connections.
      // redefining new sockets in the same pointer is fine because the
      // CookieClientHandler will be managing the sockets. You do not need the pointer
      // to assess the previous sockets introduced.
      this.socket = this.serverSocket.accept();
      System.out.println("Fella " + count + " connected");
      System.out.println("Relevant information on this socket: " + this.socket);
      System.out.println("Sending fella " + count + " to threadPool");
      threadPool.execute(new CookieClientHandler(this.socket, this.cookieJar));
      count++;
    }
  }

  // closing server socket connection, throwing IOException required
  public void closeConnection() throws IOException {
    this.serverSocket.close();
    System.out.println("Connection on serverSocket closed.");
  }
}
