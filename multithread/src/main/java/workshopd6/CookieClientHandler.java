package workshopd6;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class CookieClientHandler implements Runnable {
  private final Socket socket;
  private Cookie cookieJar;

  public CookieClientHandler(Socket socket, Cookie cookieJar) {
    this.socket = socket;
    this.cookieJar = cookieJar;
  }

  @Override
  public void run() {
    try {
      // initiliase output stream to be used to send responses to the client later
      OutputStream os = socket.getOutputStream();
      BufferedOutputStream bos = new BufferedOutputStream(os);
      DataOutputStream dos = new DataOutputStream(bos);

      // get input stream from the socket
      InputStream is = socket.getInputStream();
      BufferedInputStream bis = new BufferedInputStream(is);
      DataInputStream dis = new DataInputStream(bis);

      // Checks if there is anything to read from client and acts accordingly based on
      // processRequest()
      while (is.available() == 0) { // .available() returns an estimate of the number of bytes that can be read from
                                    // this input stream without blocking by the next invocation fo a method for
                                    // this input stream
        String messageFromClient = dis.readUTF();
        processRequest(dos, messageFromClient);
      }
    } catch (IOException e) {
      System.err.println("Client " + this.socket + " has disconnected.");
    }

  }

  private void processRequest(DataOutputStream dos, String requestFromClient) throws IOException {
    switch (requestFromClient) {
      case "get-cookie":
        System.out.println("Sending a random fortune cookie to the client.");
        String response = cookieJar.cookieText();
        // writing response to server
        dos.writeUTF(response);
        dos.flush();
        return;
      case "close":
        this.socket.close();
      default:
        // writing response to server
        dos.writeUTF("server error");
        dos.flush();
    }
  }

}
