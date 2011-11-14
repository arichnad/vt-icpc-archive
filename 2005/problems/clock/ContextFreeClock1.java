// ContextFreeClock1.java - ngb

import java.io.*;
import java.util.regex.*;

public class ContextFreeClock1
{
  public static void main(final String[] args) throws IOException
  {
    final BufferedReader xi = new BufferedReader(new InputStreamReader(System.in));
    for (int lineNum = 1; ; ++lineNum) {
      final String line = xi.readLine();
      if (line == null) {
        System.err.println("Premature EOF");
        break;
      }
      if (line.equals("-1 00:00:00")) {
        break;
      }

      Matcher matcher = Pattern.compile("(\\d+) (\\d\\d):(\\d\\d):(\\d\\d)").matcher(line);
      if (!matcher.matches()) {
        System.err.println("Invalid input on line " + lineNum + ": " + line);
        break;
      }

      final int angle = Integer.parseInt(matcher.group(1));
      final int hh = Integer.parseInt(matcher.group(2));
      final int mm = Integer.parseInt(matcher.group(3));
      final int ss = Integer.parseInt(matcher.group(4));
      if (angle < 0 || angle >= 360 || hh < 0 || hh >= 60 || mm < 0 || mm >= 60 || ss < 0 || ss >= 60) {
        System.err.println("Value out of range on line " + lineNum + ": " + line);
        break;
      }
      final int overheardElevenths = (hh * 3600 + mm * 60 + ss) * 11;

      // first time of the day that matches the angle is 120 * angle / 11 seconds
      int elevenths = 120 * angle;

      // advance to the next possible solution by adding 120 * 360 / 11 seconds
      while (elevenths < overheardElevenths) {
        elevenths += 120 * 360;
      }

      // round down to the nearest second and fit to a 24 hour clock
      final int seconds = (elevenths / 11) % 86400;

      // print the result
      System.out.println(fmt2(seconds / 3600) + ":" +
          fmt2((seconds / 60) % 60) + ":" +
          fmt2(seconds % 60));
    }
  }

  static String fmt2(final int n)
  {
    return (n < 10 ? "0" : "") + n;
  }
}
