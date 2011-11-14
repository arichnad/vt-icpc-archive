import java.io.*;
import java.util.*;

public class contest {
  public static void main(String[] argv)
  throws Exception  {
    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

    int nTeams = Integer.parseInt(in.readLine().trim());

    int bestSolved = 0;
    int bestPenalty = Integer.MAX_VALUE;
    String bestName = null;

    for(int i=0;i<nTeams;i++){
      StringTokenizer t = new StringTokenizer(in.readLine());
      String name = t.nextToken();
      int solved=0;
      int penalty=0;
      for(int p=0;p<4;p++){
	int subs = Integer.parseInt(t.nextToken());
	int time = Integer.parseInt(t.nextToken());

	if(time > 0){
	  solved++;
	  penalty+=time+(subs-1)*20;
	}
      }

      System.err.println(name+" "+solved+" "+penalty);

      if(solved>bestSolved || (solved == bestSolved && penalty < bestPenalty)){
	bestSolved = solved;
	bestPenalty = penalty;
	bestName = name;
      }
    }

    System.out.println(bestName+" "+bestSolved+" "+bestPenalty);
  }
}
	