import java.io.*;
import java.lang.*;
import java.util.*;
import java.awt.Point;
import java.util.Arrays;
import java.lang.Math;

class doorsHC {
    static class PointCompare implements Comparator {
	public int compare(Object a, Object b)
	{
	    if (ccw(start_p, (Point)a, (Point)b) == CCW) {
		return -1;
	    } else if (ccw(start_p, (Point)b, (Point)a) == CCW) {
		return 1;
	    } else {
		return 0;
	    }
	}
    };
    

    static PrintWriter out;
    static Scanner sc;
    
    static Point start_p;

    static final int CCW = 0;
    static final int CW = 1;
    static final int CNEITHER = 2;
    
    public static int ccw(Point a, Point b, Point c)
    {
	long dx1 = Math.round(b.getX()) - Math.round(a.getX());
	long dx2 = Math.round(c.getX()) - Math.round(b.getX());
	long dy1 = Math.round(b.getY()) - Math.round(a.getY());
	long dy2 = Math.round(c.getY()) - Math.round(b.getY());
	long t1 = dy2 * dx1;
	long t2 = dy1 * dx2;
	
	if (t1 == t2) {
	    if (dx1 * dx2 < 0 || dy1 * dy2 < 0) {
		if (dx1*dx1 + dy1*dy1 >= dx2*dx2 + dy2*dy2) {
		    return CNEITHER;
		} else {
		    return CW;
		}
	    } else {
		return CCW;
	    }
	} else if (t1 > t2) {
	    return CCW;
	} else {
	    return CW;
	}
    }
    
    static int convex_hull(Point[] polygon, int n, Point[] hull) {
	int count, best_i, i;
	
	if (n == 1) {
	    hull[0] = polygon[0];
	    return 1;
	}
	
	/* find the first point: min y, and then min x */
	start_p = new Point(polygon[0]);
	best_i = 0;
	for (i = 1; i < n; i++) {
	    if ((polygon[i].getY() < start_p.getY()) ||
		(polygon[i].getY() == start_p.getY() && 
		 polygon[i].getX() < start_p.getX())) {
		start_p = new Point(polygon[i]);
		best_i = i;
	    }
	}
	polygon[best_i] = new Point(polygon[0]);
	polygon[0] = new Point(start_p);

	/* get simple closed polygon */
	Arrays.sort(polygon, 1, n, new PointCompare());

	/* do convex hull */
	count = 0;
	hull[count] = new Point(polygon[count]); count++;
	hull[count] = new Point(polygon[count]); count++;
	for (i = 2; i < n; i++) {
	    while (count > 1 &&
		   ccw(hull[count-2], hull[count-1], polygon[i]) == CW) {
		/* pop point */
		count--;
	    }
	    hull[count++] = new Point(polygon[i]);
	}
	return count;
    }

    static final int MAX_DP = 500;

    static public void add_booth(Point[] P, int x1, int y1, int x2, int y2, 
				 int i)
    {
	P[i++] = new Point(x1, y1);
	P[i++] = new Point(x1, y2);
	P[i++] = new Point(x2, y1);
	P[i++] = new Point(x2, y2);
    }


    static public boolean point_in_poly(Point[] poly, int n, Point p)
    {
	int i, j;
	boolean c = false;
	
	/* first check to see if point is one of the vertices */
	for (i = 0; i < n; i++) {
	    if (p.getX() == poly[i].getX() && p.getY() == poly[i].getY()) {
		return true;
	    }
	}
	
	/* now check if it's on the boundary */
	for (i = 0; i < n-1; i++) {
	    if (ccw(poly[i], poly[i+1], p) == CNEITHER) {
		return true;
	    }
	}
	if (ccw(poly[n-1], poly[0], p) == CNEITHER) {
	    return true;
	}
	
	/* finally check if it's inside */
	for (i = 0, j = n-1; i < n; j = i++) {
	    if (((poly[i].getY() <= p.getY() && p.getY() < poly[j].getY()) ||
		 (poly[j].getY() <= p.getY() && p.getY() < poly[i].getY())) &&
		( (poly[j].getY() >= poly[i].getY() &&
		   (p.getX() - poly[i].getX()) * (poly[j].getY()-poly[i].getY()) < 
		   (poly[j].getX() - poly[i].getX()) * (p.getY() - poly[i].getY())) ||
		  (poly[j].getY() < poly[i].getY() &&
		   (p.getX() - poly[i].getX()) * (poly[j].getY()-poly[i].getY()) >
		   (poly[j].getX() - poly[i].getX()) * (p.getY() - poly[i].getY()))))
		c = !c;
	}
	return c;
    }

    static public long direction(Point p1, Point p2, Point p3)
    {
	long x1 = Math.round(p3.getX()) - Math.round(p1.getX());
	long y1 = Math.round(p3.getY()) - Math.round(p1.getY());
	long x2 = Math.round(p2.getX()) - Math.round(p1.getX());
	long y2 = Math.round(p2.getY()) - Math.round(p1.getY());
	return x1*y2 - x2*y1;
    }
    
    static public boolean on_segment(Point p1, Point p2, Point p3)
    {
	return ((p1.getX() <= p3.getX() && p3.getX() <= p2.getX()) || (p2.getX() <= p3.getX() && p3.getX() <= p1.getX())) &&
	    ((p1.getY() <= p3.getY() && p3.getY() <= p2.getY()) || (p2.getY() <= p3.getY() && p3.getY() <= p1.getY()));
    }
    
    static public boolean intersect(Point a1, Point a2, Point b1, Point b2)
    {
	long d1 = direction(b1, b2, a1);
	long d2 = direction(b1, b2, a2);
	long d3 = direction(a1, a2, b1);
	long d4 = direction(a1, a2, b2);
	
	if (((d1 > 0 && d2 < 0) || (d1 < 0 && d2 > 0)) &&
	    ((d3 > 0 && d4 < 0) || (d3 < 0 && d4 > 0))) {
	    return true;
	} else {
	    return (d1 == 0 && on_segment(b1, b2, a1)) ||
		(d2 == 0 && on_segment(b1, b2, a2)) ||
		(d3 == 0 && on_segment(a1, a2, b1)) ||
		(d4 == 0 && on_segment(a1, a2, b2));
	}
    }

    static public boolean intersect_hull(Point[] h1, int n1, Point[] h2, int n2)
    {
	int i, j;
	
	for (i = 0; i < n1; i++) {
	    if (point_in_poly(h2, n2, h1[i])) {
		return true;
	    }
	}
	
	for (i = 0; i < n2; i++) {
	    if (point_in_poly(h1, n1, h2[i])) {
		return true;
	    }
	}
	
	for (i = 0; i < n1; i++) {
	    for (j = 0; j < n2; j++) {
		if (intersect(h1[i], h1[(i+1)%n1], h2[j], h2[(j+1)%n2])) {
		    return true;
		}
	    }
	}
	
	return false;
    }
    
    public static void main(String[] args) throws IOException {

      sc = new Scanner(System.in);
      out = new PrintWriter(System.out);
      /*
        try {
            sc = new Scanner(new File("doors.in"));
            out = new PrintWriter(new FileOutputStream("doors.out"));
        }
        catch (FileNotFoundException e) {
            System.err.println("Cannot open input/output file.");
            System.exit(1);
        }
      */

	int D, P;
	int x1, y1, x2, y2;
	int i;
	Point [] doors = new Point[MAX_DP*4];
	Point [] penguins = new Point[MAX_DP*4];
	Point [] dhull = new Point[MAX_DP*4];
	Point [] phull = new Point[MAX_DP*4];
	int dhsize, phsize;
	int case_num = 1;

	D = sc.nextInt();
	P = sc.nextInt();
	while (D != 0 && P != 0) {
	    for (i = 0; i < D; i++) {
		x1 = sc.nextInt();
		y1 = sc.nextInt();
		x2 = sc.nextInt();
		y2 = sc.nextInt();
		add_booth(doors, x1, y1, x2, y2, i*4);
	    }
	    for (i = 0; i < P; i++) {
		x1 = sc.nextInt();
		y1 = sc.nextInt();
		x2 = sc.nextInt();
		y2 = sc.nextInt();
		add_booth(penguins, x1, y1, x2, y2, i*4);
	    }
	    dhsize = convex_hull(doors, 4*D, dhull);
	    phsize = convex_hull(penguins, 4*P, phull);


	    if (case_num > 1) {
		out.println();
	    }
	    out.print("Case " + (case_num++) + ": It is ");
	    if (intersect_hull(dhull, dhsize, phull, phsize)) {
		out.print("not ");
	    }
	    out.println("possible to separate the two groups of vendors.");
	    out.flush();
	    D = sc.nextInt();
	    P = sc.nextInt();

	}


	out.close();
    }
}
