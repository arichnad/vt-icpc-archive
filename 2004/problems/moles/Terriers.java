import java.io.*;
import java.util.*;

public class Terriers {
  
  public static void main(String[] argv){
    int i=1;
    Scanner in = new Scanner(System.in);
    int w, l, n, t;
    w = in.nextInt();
    l = in.nextInt();
    n = in.nextInt();
    t = in.nextInt();
    while(w!=0){
      System.out.println("Observation Set "+i);
      new Terriers(in, w+1, l+1, n, t);
      w = in.nextInt();
      l = in.nextInt();
      n = in.nextInt();
      t = in.nextInt();
      i++;
    }
  }


  public Terriers (Scanner in, int W, int L, int N, int T){
    boolean[][] p = new boolean[W][L];

    int[][][] terriers = new int[T][N][2]; 
    
    for(int n=0;n<N;n++)
      for(int t=0;t<T;t++){
	terriers[t][n][0] = in.nextInt();
	terriers[t][n][1] = in.nextInt();
      }

    for(int i=0;i<W;i++)
      for(int j=0;j<L;j++)
	p[i][j] = true;

    for(int time = 0 ; time < T ; time++){
      for(int dog = 0; dog < N; dog++)
	clearAt(terriers[time][dog], p);
      p = updatePossibles(terriers[time], p);
    }

    int x = 0;
    boolean hasP = false;

    for(int l=0;l<L;l++)
      for(int w=0;w<W;w++){
	if(p[w][l]){
	  if(x==8){
	    System.out.println();
	    x=0;
	  }
	  if(x>0)
	    System.out.print(" ");
	  System.out.printf("(%d,%d)", w, l);
	  x++;
	  hasP = true;
	}
      }
    
    if(!hasP){
      System.out.println("No possible locations");
    } else
      System.out.println();

  }

  void clearAt(int[] coord, boolean[][] p){
    for(int dw=-1;dw<2;dw++)
      for(int dl=-1;dl<2;dl++){
	if(dw!=0 && dl!=0)
	  continue;
	int nw = coord[0]+dw;
	int nl = coord[1]+dl;
	if(nw < 0 || nw >= p.length ||
	   nl < 0 || nl >= p[0].length)
	  continue;
	p[nw][nl] = false;
      }
  }

  boolean[][] updatePossibles(int[][] dogs, boolean[][] p){
    boolean[][] newp = new boolean[p.length][p[0].length];
    for(int i=0;i<p.length;i++)
      for(int j=0;j<p[0].length;j++)
	newp[i][j] = false;

    // set movement based on mole position
    for(int i=0;i<p.length;i++)
      for(int j=0;j<p[0].length;j++)
	if(p[i][j])
	  for(int dw=-1;dw<2;dw++)
	    for(int dl=-1;dl<2;dl++){
	      if(dw!=0 && dl!=0)
		continue;
	      int nw = i+dw;
	      int nl = j+dl;
	      if(nw < 0 || nw >= p.length ||
		 nl < 0 || nl >= p[0].length)
		continue;
	      newp[nw][nl] = true;
	    }

    // reclear based on dog position
    for(int d = 0 ; d < dogs.length ; d++)
      clearAt(dogs[d], newp);

    return newp;
  }
	  
}
