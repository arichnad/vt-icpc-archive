import java.io.*;
import java.util.*;
import java.util.regex.Pattern;


public class mirror {
  
  
  
  
  public static void main (String[] argv)
  {
    try {
      BufferedReader in = new BufferedReader
        (new InputStreamReader(System.in));

      String line = in.readLine();
      while (!line.equals("***")) {
        StringBuffer sb = new StringBuffer();
        for (int i = line.length()-1; i >= 0; --i) {
          sb.append (line.charAt(i));
        }
        System.out.println (sb);
        line = in.readLine();
      }
    } catch (Exception e) {System.err.println (e);}
  }

}