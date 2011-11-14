import java.io.*;
import java.util.*;

public class LineOfSight {

  public static void main(String[] argv){
    Scanner in = new Scanner(System.in);
    
    java.text.NumberFormat f = java.text.NumberFormat.getInstance();
    f.setMaximumFractionDigits(2);
    f.setMinimumFractionDigits(2);
    f.setGroupingUsed(false);

    double hx1 = in.nextDouble();
    double hx2 = in.nextDouble();
    double hy = in.nextDouble(); 

    while(hx1!=0&&hx2!=0&&hy!=0){
      double rx1 = in.nextDouble();
      double rx2 = in.nextDouble();
      double ry = in.nextDouble();

      if(ry >=hy)
	System.err.println("Road above house?");
      
      int nObs=in.nextInt();

      Set<Obstruct> obsSet = new HashSet<Obstruct>();

      for(int i=0;i<nObs;i++){
	Obstruct o = new Obstruct();

	o.x1 = in.nextDouble();
	o.x2 = in.nextDouble();
	o.y = in.nextDouble();

	if (o.y >= hy || o.y <= ry)
	  continue;
	
	o.blockx1 = hx2 - ((hx2-o.x1)/(hy-o.y))*(hy-ry);
	o.blockx2 = hx1 - ((hx1-o.x2)/(hy-o.y))*(hy-ry);

	if(o.blockx1 < rx1)
	  o.blockx1 = rx1;
	if(o.blockx1 > rx2)
	  o.blockx1 = rx2;

	if(o.blockx2 < rx1)
	  o.blockx2 = rx1;
	if(o.blockx2 > rx2)
	  o.blockx2 = rx2;

	System.err.println("obstructs "+o.blockx1+" to "+o.blockx2);

	obsSet.add(o);
      }

      // find min start block
      Obstruct curOb = findNextBlock(rx1, obsSet);
      double maxLen;
      if(curOb == null)
	maxLen = rx2-rx1;
      else{
	maxLen = curOb.blockx1 - rx1;
	
	// go to end of min and search for next already or start block
	Obstruct nextOb;
	while((nextOb = findNextBlock(curOb.blockx2, obsSet)) != null){
	  double nextLen = nextOb.blockx1 - curOb.blockx2;
	  if(nextLen > maxLen)
	    maxLen = nextLen;
	  curOb = nextOb;
	}

	if(curOb.blockx2 < rx2){
	  double nextLen = rx2 - curOb.blockx2;
	  if(nextLen > maxLen)
	    maxLen = nextLen;
	}
      }

      if(maxLen == 0)
	System.out.println("No View");
      else
	System.out.println(f.format(maxLen));

      hx1 = in.nextDouble();
      hx2 = in.nextDouble();
      hy = in.nextDouble(); 
    }
  }

  static Obstruct findNextBlock(double start, Set<Obstruct> os){
    double minX = Double.MAX_VALUE;
    Obstruct minO = null;

    for(Obstruct o : os){
      // if start still to left of right point of obstruction
      if(o.blockx2 > start){
	// find the minimum 
	if(o.blockx1 < minX){
	  minX = o.blockx1;
	  minO = o;
	}
      }
    }

    return minO;
  }

}


class Obstruct {
  double x1;
  double x2;
  double y;

  double blockx1;
  double blockx2;
}

