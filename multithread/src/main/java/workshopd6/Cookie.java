package workshopd6;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;

public class Cookie {
  public static void main(String[] args) {

    Cookie cookie = new Cookie(
        "cookie_file.txt");
    cookie.loadCookie();
    System.out.println(cookie.cookieText());

  }

  File textFile;
  ArrayList<String> cookieList = new ArrayList<String>();

  // constructor
  public Cookie(String fileName) {
    Path currentRelativePath = Paths.get("");
    String s = currentRelativePath.toAbsolutePath().toString();
    fileName = s + "\\multithread\\" + fileName;
    this.textFile = Path.of(fileName).toFile();
    if (!this.textFile.exists()) {
      try {
        this.textFile.createNewFile(); // create text file
        System.out.println("User absent. File created: " + this.textFile.getName());

      } catch (IOException e) {
        System.out.println("An error occurred.");
        System.err.println(e);
      }
    }
    loadCookie();

  }

  // opens cookie, maybe load cookies into an arrayList?
  public void loadCookie() {
    Path cookiePath = this.textFile.toPath();
    String item;

    try {
      // initialise buffered reader,
      // Files.newBufferedReader(Path path) Opens a file for reading, returning a
      // BufferedReader that may be used to read
      // text from the file in an efficient manner.
      BufferedReader reader = Files.newBufferedReader(cookiePath);
      // object
      while ((item = reader.readLine()) != null) { // .readLine() reads a line of text
        this.cookieList.add(item);
      }
      reader.close(); // closes the stream and release system resource associated with it

    } catch (IOException e) {
      System.err.println(e);
    }

  }

  // return a random cookie text from the list of cookies in txt file
  public String cookieText() {
    Random rand = new Random();
    int selected = rand.nextInt(this.cookieList.size());
    String randomCookie = this.cookieList.get(selected);
    return randomCookie;
  }

}
