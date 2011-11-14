import java.io.*;
import java.util.*;
import java.util.regex.Pattern;


public class gypsymoths {

    public int problemNumber;
    public int numTrees;
    double[] angles;
    double fieldOfView;

    public gypsymoths (int nTrees, int problemNum, Scanner sc)
    {
	numTrees = nTrees;
	problemNumber = problemNum;
	angles = new double[3*numTrees];
	
	double tx = sc.nextDouble();
	double ty = sc.nextDouble();
	fieldOfView = sc.nextDouble();

	for (int i = 0; i < numTrees; ++i)
	    {
		double x = sc.nextDouble();
		double y = sc.nextDouble();

		x -= tx;
		y -= ty;
		double angle = Math.atan2 (x,y);
		angle = angle * 180.0 / Math.PI;
		if (angle < 0.0)
		    angle += 360.0;

		angles[i] = angle - 360.0;
		angles[i+numTrees] = angle;
		angles[i+2*numTrees] = angle + 360.0;
	    }

	Arrays.sort (angles);
    }


    public class Solution {
	int maxVisible;
	double bestAngle;
    };


    public void findBest (Solution sol)
    {
	sol.maxVisible = 0;
	sol.bestAngle = 0.0;
	int start = 0;
	
	for (int ia = 0; ia < 3600; ++ia) {
	    double telescope = ((double)ia)/10.0;
	    double lowAngle = telescope - fieldOfView / 2.0;
	    double highAngle = telescope + fieldOfView / 2.0;
		
	    while (start < 3*numTrees
		   && angles[start] <= lowAngle)
		++start;
		
	    int stop = start;
	    while (stop < 3*numTrees
		   && angles[stop] < highAngle)
		++stop;
		
	    int cnt = stop - start;
	    if (cnt > sol.maxVisible) {
		sol.maxVisible = cnt;
		sol.bestAngle = telescope;
	    }
	}
    }
    
    
    
    public void solve ()
    {
	Solution s = new Solution();
	findBest (s);
	System.out.print ("Point the camera at angle ");
	System.out.println (s.bestAngle + " to view " + 
			    s.maxVisible + " infested trees.");
    }

  
	     
  public static void main (String[] argv)
  {
    int problemNumber = 0;
    
    Scanner inScan = new Scanner(System.in);
    
    int numTrees = inScan.nextInt();

    while (numTrees > 0) {
      ++problemNumber;
      gypsymoths gy = new gypsymoths(numTrees, problemNumber, inScan);
      gy.solve();
      
      numTrees = inScan.nextInt();
    }
  }
}
