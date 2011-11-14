import java.io.*;
import java.util.*;

public class Vitamins {
  public static void main(String[] args) throws IOException{
    LinkedList<String> insignificant = new LinkedList<String>();

    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    String l;

    while((l = in.readLine()) != null){
      String[] fields = l.split(" ",4);
      double A= Double.parseDouble(fields[0]);
      String U = fields[1];
      double R = Double.parseDouble(fields[2]);
      String V = fields[3];

      if(A<0)
	break;
      
      double percent = A/R*100;

      if(percent >= 1){
	System.out.println(String.format("%s %.1f %s %.0f%%",
					 V,A,U,percent));
      }
      else
	insignificant.add(V);
      
    }

    System.out.println("Provides no significant amount of:");
    for(String s: insignificant)
      System.out.println(s);
  }

}
