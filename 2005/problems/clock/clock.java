/**
 * clock.java
 *
 * Created on October 30, 2005
 *
 * @author  Cary
 */
import java.io.*;
import java.util.*;
import java.text.*;
import java.lang.Math.*;

public class clock {

// main routine
    public static void main(String[] args) throws Exception {

// define / initialize some variables
		String buff;  // stirng buffer for reads
		StringTokenizer tok; // tokenizer for reads
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		// pretty printer

// define some constatants
		// seconds per hour
		int secPerHour = 60 * 60;
		//calculate seconds in 12 hours
		int secPer12 = 12 * secPerHour;
		//calculate fixed delta between times that the hands overlap (secs)
		// keep as double to not loose fraction until we print
		double delta = ((double)secPer12) / 11.0;

		double timeNow;
// start looping
        buff = in.readLine().trim();
        tok = new StringTokenizer(buff,": /t");
        double angle = Double.parseDouble(tok.nextToken().trim());
        while (angle >= 0) {
		// finish the read
			// read hour
			double val = Double.parseDouble(tok.nextToken().trim());
			timeNow = val * secPerHour;
			// read minute
			val = Double.parseDouble(tok.nextToken().trim());
			timeNow += val * 60;
			// read second
			val = Double.parseDouble(tok.nextToken().trim());
			timeNow += val;

		// find first time today that this angle is possible: (sec)
		// note that in the time it takes the minute hand to overtake
		// the hour hand, their angle varies from 0 to 360
			double firstTime = delta * (angle / 360.0);

			double outTime;

			if (firstTime >= timeNow) {
				outTime = firstTime;
			}
			else {
				outTime = firstTime;
				while ( ((int)outTime) < ((int)timeNow)) {
					outTime += delta;
				}
			}
		// output the result, keeping in mind 24 hour clock
		// make sure we handle going to the next day correctly
			int outInt = ((int)outTime) % (secPer12 * 2);

			timeHolder toutint = new timeHolder(outInt);

			System.out.println( "" + toutint);
	// read the next one.
	        buff = in.readLine().trim();
	        tok = new StringTokenizer(buff,": /t");
			angle = Double.parseDouble(tok.nextToken().trim());
		}
    } // end main
}  // end clock

class timeHolder {
	int secs = 0;
	public timeHolder(int h, int m, int s) {
		secs = (h * 60 + m) * 60 + s;
	}

	public timeHolder(int sec) {
		secs = sec;
	}

	public timeHolder(double sec) {
		secs = (int)sec;
	}

	public String toString () {
        NumberFormat intf = NumberFormat.getInstance();
        intf.setMinimumIntegerDigits(2);
        intf.setMaximumIntegerDigits(2);
		return "" + intf.format(secs/3600) +
			":" + intf.format( (secs%3600) / 60) +
			":"	+ intf.format( secs % 60);
	}
}
