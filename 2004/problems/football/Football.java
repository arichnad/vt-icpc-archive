import java.io.*;
import java.util.*;

public class Football {
  public static void main(String[] argv) throws IOException{
    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    
    StringTokenizer tok = new StringTokenizer(in.readLine());
    double speed = Double.parseDouble(tok.nextToken());
    double weight = Double.parseDouble(tok.nextToken());
    double strength = Double.parseDouble(tok.nextToken());
    
    while(speed!=0 || weight!=0 || strength!=0){
      boolean hasPosition = false;
      if(speed <= 4.5 && weight >= 150 && strength >= 200){
	System.out.print("Wide Receiver ");
	hasPosition=true;
      }
      if(speed <= 6.0 && weight >= 300 && strength >= 500){
	System.out.print("Lineman ");
	hasPosition=true;
      }
      if(speed <= 5.0 && weight >= 200 && strength >= 300){
	System.out.print("Quarterback ");
	hasPosition=true;
      }
      if(!hasPosition)
	System.out.print("No positions");
      System.out.println();

      tok = new StringTokenizer(in.readLine());
      speed = Double.parseDouble(tok.nextToken());
      weight = Double.parseDouble(tok.nextToken());
      strength = Double.parseDouble(tok.nextToken());
    }
  }
}
