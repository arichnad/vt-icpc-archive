import java.util.*;
import java.io.*;

public class RightHandRule {
  char[][] map;
  int x, y;

  static BufferedReader in;

  static final boolean DEBUG = false;

  public RightHandRule(int x, int y) throws IOException{
    this.x = x;
    this.y = y;
    map = new char[x][y];
    for(int i=0;i<x;i++)
      Arrays.fill(map[i],'X');
    for(int i=y-1;i>=0;i--){
      char[] line = in.readLine().toCharArray();
      for(int lx = 0;lx<Math.min(line.length,x);lx++)
	map[lx][i] = line[lx];
    }

    int found=0,entrances=0;

    for(int ex=0;ex<x;ex++){
      if(map[ex][0] == 'E'){
	entrances++;
	if (doSearch(ex,0,Dirs.NORTH)) found++;
      }
      if(map[ex][y-1] == 'E'){
	entrances++;
	if (doSearch(ex,y-1,Dirs.SOUTH)) found++;
      }
    }

    for(int ey=1;ey<(y-1);ey++){
      if(map[0][ey] == 'E'){
	entrances++;
	if (doSearch(0,ey,Dirs.EAST)) found++;
      }
      if(map[x-1][ey] == 'E'){
	entrances++;
	if (doSearch(x-1,ey,Dirs.WEST)) found++;
      }
    }
   
    System.out.println("The goal would be found from "+found+" out of "+
		       entrances+" entrances.");
 
  }

  boolean doSearch(int ex, int ey, Dirs h){
    while(true){
      if(DEBUG) System.out.print("at "+ex+","+ey+" ");
      if(lookForGoal(ex,ey))
	return true;
      // handles blocked entrance
      if((ex+h.dsx)< 0 || (ey+h.dsy)< 0 ||
	 (ex+h.dsx)>= x|| (ey+h.dsy)>= y)
	return false;
      switch(map[ex+h.dsx][ey+h.dsy]){
	// space ahead is empty: can go straight, turn right, or reach exit
	// based on what is ahead-right
      case ' ':
	switch(map[ex+h.drx][ey+h.dry]){
	case 'X':
	case 'G':
	  if(DEBUG) System.out.println("case \' X\' straight");
	  ex+=h.dsx;
	  ey+=h.dsy;
	  break;
	case 'E':
	  if(DEBUG) System.out.println("case \' E\' leave");
	  return lookForGoal(ex+h.dsx,ey+h.dsy);
	case ' ':
	  if(DEBUG) System.out.println("case \'  \' turn right");
	  ex+=h.drx;
	  ey+=h.dry;
	  h = h.right;
	  break;
	}
	break;
	// space ahead is wall, can turn left
      case 'X':
	if(DEBUG) System.out.println("case \'X\' turn left");
	h = h.left;
	break;
	// space ahead is exit, exit without seeing goal
      case 'E':
	if(DEBUG) System.out.println("case \'E\' leave");
	return false;
      }
    }
  }

  boolean lookForGoal(int ex, int ey){
    for(int xx = ex-1;xx>0&&map[xx][ey]!='X' && map[xx][ey]!='E';xx--)
      if(map[xx][ey]=='G')
	return true;
    for(int xx = ex+1;xx<x&&map[xx][ey]!='X' && map[xx][ey]!='E';xx++)
      if(map[xx][ey]=='G')
	return true;

    for(int yy = ey-1;yy>0&&map[ex][yy]!='X' && map[ex][yy]!='E';yy--)
      if(map[ex][yy]=='G')
	return true;
    for(int yy = ey+1;yy<y&&map[ex][yy]!='X' && map[ex][yy]!='E';yy++)
      if(map[ex][yy]=='G')
	return true;
    return false;
  }
    


  public static void main(String[] args) throws IOException{
	in = new BufferedReader(new InputStreamReader(System.in));
    String l;

    while((l = in.readLine()) != null){
      StringTokenizer toks = new StringTokenizer(l);
      int w= Integer.parseInt(toks.nextToken());
      int h= Integer.parseInt(toks.nextToken());
      if(w < 3 || h < 3)
	break;
      new RightHandRule(w, h);
    }
  }

  

}


enum Dirs {
  NORTH (0,1,1,1),
    EAST (1,0,1,-1),
    SOUTH (0,-1,-1,-1),
    WEST (-1,0,-1,1);

  static{
    NORTH.setTurns(EAST,WEST);
    EAST.setTurns(SOUTH,NORTH);
    SOUTH.setTurns(WEST,EAST);
    WEST.setTurns(NORTH,SOUTH);
  }

  public int dsx,dsy,drx,dry;
  public Dirs right, left;
  Dirs(int dsx, int dsy, int drx, int dry){
    this.dsx = dsx;
    this.dsy = dsy;
    this.drx = drx;
    this.dry = dry;
  }

  private void setTurns(Dirs right, Dirs left){
    this.right = right;
    this.left = left;
  }
}










