/**
 * house.java
 *
 * Created on October 17, 2004, 7:01 PM
 *
 * @author  Cary
 */
import java.io.*;
import java.util.*;
import java.text.*;
import java.lang.Math.*;

public class house {
    /** Creates a new instance of house */
    public house() {
    }
 // finds where a line  drawn between two points intercetps the line through a line segment.
 // note that this doesn't determine if the intercept is actually on the line segment
    private static double FindStreetIntercept (double x1, double y1, double x2, double y2, LineSegment st) {
        double slope;  // slope of the line
        double yint;  // y intercept of the line
        if (x1 == x2) { // if the two points are vertical, the line intersects at the same x 
           return x1;
        }
        // Y = mX + b
        // calculate m  (rise over run)
        slope = (y1 - y2) / (x1 - x2);
        // b = Y - mX  calculate b
        yint = y1 - slope*x1;
        // x value at a given y  is (Y - b) / m 
        return (st.getY() - yint) / slope;
    }
    
    static private LineSegment readCoordinates (BufferedReader in) throws Exception {
        double x1, x2, y;  // coordinates
        String buff;  // stirng buffer for reads
	StringTokenizer tok; // tokenizer for reads
        buff = in.readLine().trim();
        tok = new StringTokenizer(buff);
        x1 = Double.parseDouble(tok.nextToken().trim());
        x2 = Double.parseDouble(tok.nextToken().trim());
        y = Double.parseDouble(tok.nextToken().trim());
        return new LineSegment(x1,x2,y);
    }
   
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        String buff;  // stirng buffer for reads
	StringTokenizer tok; // tokenizer for reads
	BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);
        nf.setGroupingUsed(false);
        
// read in house position
        LineSegment House = readCoordinates(in);
        
        while (! ( House.getX1()==0 && House.getX2()==0 && House.getY() == 0)) {   
    // read in street position
            LineSegment Street = readCoordinates(in);
    // get number of obstacles        
            buff = in.readLine().trim();
            int numObstacles = Integer.parseInt(buff);

            ArrayList shadowgroup = new ArrayList();
            LineSegment shadow;
    // read and cast shadows of each obstacle
            for (int i = 0; i < numObstacles;i++) {
                LineSegment obstacle = readCoordinates(in);
                if (LineSegment.inRange(obstacle.getY(),Street.getY(),House.getY()) 
                    && (obstacle.getY() != Street.getY()) && (obstacle.getY() != House.getY())) { // ignore obstacles not between house and street
                    // cast shadows i.e. a line from left end of house to right end of obstacle
                    // and a line from right end of house to left end of obstacle.  Find the
                    // intercepts of these lines on the street.
                    shadow = new LineSegment(house.FindStreetIntercept (House.getX1(),House.getY(),obstacle.getX2(),obstacle.getY(),Street), 
                                            house.FindStreetIntercept (House.getX2(),House.getY(),obstacle.getX1(),obstacle.getY(),Street),
                                            Street.getY());
                    if (Street.DoesOverlap(shadow) ) {   // only worry about shadows that intersect the street
                        // make fit on street: if the endpoint is past the end of the street, only worry about the part that is on the street
                        if ( shadow.getX1() < Street.getX1()) { shadow.setX1(Street.getX1()); }
                        if ( shadow.getX2() > Street.getX2()) { shadow.setX2(Street.getX2()); }
                        shadowgroup.add(shadow);
                    }
                }  // end if obstacle is between house and street 
            }  // end for numObstacles
            if (shadowgroup.isEmpty()) {  // no shadows on the street, return the whole street
                System.out.println(nf.format(Street.getX2()  -  Street.getX1()) );
            }
            else {  // consolidate shadows 
                ArrayList finalshadows = new ArrayList();
                Collections.sort(shadowgroup);
                Iterator shadowiterator = shadowgroup.iterator();
                while (shadowiterator.hasNext()) {// loop through all shadows
                   shadow = (LineSegment)shadowiterator.next();
                   if (finalshadows.isEmpty()) {  // put the first one in the group
                       finalshadows.add(shadow);                  
                   }
                   else { 
                      LineSegment last = ((LineSegment)finalshadows.get(finalshadows.size()-1));
                      if (  last.DoesOverlap(shadow) ) { // check to see if they overlap
                         // replace with merged shadow
                         finalshadows.set(finalshadows.size()-1,last.merge(shadow) );
                      }
                      else {  // no overlap, add to the list
                         finalshadows.add(shadow);
                      }
                   }
                } // end while shadowiterator
                if ( Street.equals((LineSegment)finalshadows.get(0)) ) {  // if this shadow matches the street, then no view
                    System.out.println("No View");
                }
                else { // find street area that isn't shadowed
                   ArrayList openlist = new ArrayList();
                   Collections.sort(finalshadows);
                   Iterator fsi = finalshadows.iterator();
                   shadow = ((LineSegment)fsi.next());
                   if (Street.getX1() < shadow.getX1()) { // add inital segment if necessary
                       openlist.add(new LineSegment(Street.getX1(),shadow.getX1(),Street.getY()));
                   }  
                   while (fsi.hasNext()) {  // add all the spaces between shadows
                      LineSegment last = shadow;
                      shadow = ((LineSegment)fsi.next());
                      openlist.add(new LineSegment(last.getX2(),shadow.getX1(),Street.getY()));
                   }
                   if (Street.getX2() > shadow.getX2()) {  // add final segment if necessary
                       openlist.add(new LineSegment(shadow.getX2(),Street.getX2(),Street.getY()));
                   }
                   // find longest and print
                   double longest = -99999999;
                   int longpos = 0;
                   for (int i=0;i<openlist.size();i++) {
                       if ( ((LineSegment)openlist.get(i)).length() > longest) {
                          longest = ((LineSegment)openlist.get(i)).length();
                          longpos = i;
                       }               
                   }
                   LineSegment longline = ((LineSegment)openlist.get(longpos));
                    System.out.println("" + nf.format(longline.getX2() - longline.getX1()));
                }
            }
            House = readCoordinates(in);  // get the next one.
        }
    } // end main
}  // end house


// holds informaiton about a line segment.
class LineSegment implements Comparable{
    
    private double x1;
    private double x2;
    private double y;
 
    public double getX1() { return x1;}
    public double getX2() { return x2;}
    public double getY() { return y;}

    public void setX1(double newx1) { x1 = newx1;}
    public void setX2(double newx2) { x2 = newx2;}
    public void setY(double newy) { y = newy;}
    
    
    public boolean equals(Object o) {
      if (!(o instanceof LineSegment)) {
          return false;
      }
      LineSegment l = (LineSegment)o;
      return x1 == l.getX1() && x2 == l.getX2() &&
             y == l.getY();
    }
    
    public int hashCode() {
       return (int)(1000*x1+100*x2+10*y);
    }
    
// this comparision is only valid for lines with the same y values
    public int compareTo(Object o) {
        LineSegment l = (LineSegment)o;
        if (x1 > l.getX1()) { return 1; }
        if (x1 < l.getX1()) { return -1; }
        // same x1 value, sort on x2
        if (x2 > l.getX2()) { return 1; }
        if (x2 < l.getX2()) { return -1; }
        //exactly the same, return 0
        return 0;
    }
    
    // is one value in the range defined by two endpoint values?
    static public boolean inRange(double test, double end1,double end2) {
       if ((test >= Math.min(end1,end2)) && (test <= Math.max(end1,end2)) ) {
          return true;
       }
       return false;
    }
       
    // Do two line segments overlap?
    public boolean DoesOverlap(LineSegment l2) {
      if ( (LineSegment.inRange(x1,l2.getX1(),l2.getX2())) || (LineSegment.inRange(x2,l2.getX1(),l2.getX2())) || 
           (LineSegment.inRange(l2.getX1(),x1,x2)) || (LineSegment.inRange(l2.getX2(),x1,x2)) )  {
         return true;
      }
      return false;
    }
    
    // merge two line segments
    public LineSegment merge(LineSegment l2) {
      x1 = Math.min(x1,l2.getX1());
      x2 = Math.max(x2,l2.getX2());
      return this;
    }
    
    public double length() {
       return x2 - x1;
    }
    
    /** Creates a new instance of LineSegment */
    /** Note that the endpoints are sorted- this is assumed in */
    /** other methods in this class */
    public LineSegment(double inx1,double inx2,double iny) {
        if (inx1 > inx2) {
            x1 = inx2;
            x2 = inx1;
            y = iny;
        }
        else {
            x1 = inx1;
            x2 = inx2;
            y = iny;
        }
    }
 }


