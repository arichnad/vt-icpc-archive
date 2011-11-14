import java.util.*;
import java.io.*;

public class Dominoes {
  int[][] field;

  int K, w, h;
  
  Domino[] D;

  public Dominoes(int K, int w, int h){
    K++;
    this.K = K;
    this.w = w;
    this.h = h;

    field = new int[w][h];
    for(int i=0;i<w;i++)
      for(int j=0;j<h;j++)
	field[i][j] = -1;

    createDominoes();

    System.out.println((K-1)+" "+w+" "+h);
    if(!placeDomino(0))
      System.out.println("No packing is possible");
    
  }

  void createDominoes(){
    D = new Domino[(K)*(K+1)/2];
    int n=0;
    for(int i=0;i<K;i++)
      for(int j=i;j<K;j++){
	D[n] = new Domino();
	D[n].d1 = i;
	D[n].d2 = j;
	n++;
      }
  }


  boolean placeDomino(int i){
    if(i==D.length){
      for(int d=0;d<D.length;d++){
	System.out.println(D[d]);
      }
      return true;
    }
    for(int x = 0; x < w; x++)
      for(int y = 0; y < h ; y++){
	if(tryPlace(i, x, y, x-1, y) ||
	   tryPlace(i, x, y, x+1, y) ||
	   tryPlace(i, x, y, x, y+1) ||
	   tryPlace(i, x, y, x, y-1))
	  return true;
      }

    return false;
  }

  boolean tryPlace(int i, int x1, int y1, int x2, int y2){
    if(x2 < 0 || x2 >= w || y2 < 0 || y2 >= h)
      return false;
    // not empty place
    if(field[x1][y1] != -1 || field[x2][y2] != -1)
      return false;
    // border OK
    if(isValid(D[i].d1,x1,y1) && isValid(D[i].d2,x2,y2)){
      // mark D
      D[i].x1 = x1;
      D[i].x2 = x2;
      D[i].y1 = y1;
      D[i].y2 = y2;
      // mark field
      field[x1][y1] = D[i].d1;
      field[x2][y2] = D[i].d2;
      if(placeDomino(i+1))
	return true;
      // clear field
      field[x1][y1] = -1;
      field[x2][y2] = -1;
      // clear D (for debugging)
      D[i].x1 = -1;
      D[i].x2 = -1;
      D[i].y1 = -1;
      D[i].y2 = -1;
    }

    return false;
  }

  boolean isValid(int pips, int x, int y){
    return matchOrEmpty(pips, x-1,y) &&
      matchOrEmpty(pips, x+1,y) &&
      matchOrEmpty(pips, x,y-1) &&
      matchOrEmpty(pips, x,y+1);
  }

  boolean matchOrEmpty(int pips, int x, int y){
    return ((x < 0 || x >= w || y < 0 || y >= h) ||
	    (field[x][y] == -1) ||
	    (field[x][y] == pips));
  }

       

  public static void main(String[] args) throws IOException{
    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    String l;

    while((l = in.readLine()) != null){
      StringTokenizer toks = new StringTokenizer(l);
      int K= Integer.parseInt(toks.nextToken());
      int w= Integer.parseInt(toks.nextToken());
      int h= Integer.parseInt(toks.nextToken());
      if(K==-1)
	break;
      new Dominoes(K, w, h);
    }
  }
}

class Domino {
  int d1, d2;
  int x1, y1;
  int x2, y2;

  public String toString(){
    return d1+" "+d2+" "+x1+" "+y1+" "+x2+" "+y2;
  }
}








