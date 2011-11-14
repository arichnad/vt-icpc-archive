// MakingChange1.java - ngb

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MakingChange1
{
  public static void main(final String[] args) throws IOException
  {
    final Pattern pattern = Pattern.compile("(\\d+) (\\d+) (\\d+) (\\d+) (\\d+)");

    final BufferedReader xi = new BufferedReader(new InputStreamReader(System.in));
    for (int linenum = 1; ; ++linenum) {
      final String line = xi.readLine();
      if (line == null) {
        throw new RuntimeException("EOF before proper terminating line");
      }
      final Matcher matcher = pattern.matcher(line);
      if (!matcher.matches()) {
        throw new RuntimeException("line " + linenum + " is malformed: '" + line + "'");
      }

      final int[] avail = {
          Integer.parseInt(matcher.group(1)),
          Integer.parseInt(matcher.group(2)),
          Integer.parseInt(matcher.group(3)),
          Integer.parseInt(matcher.group(4)),
      };
      final int target = Integer.parseInt(matcher.group(5));
      if (target > 99) {
        throw new RuntimeException("line " + linenum + " specifies " + target +
            " cents owed, should be 0..99");
      }

      if (avail[0] == 0 && avail[1] == 0 && avail[2] == 0 && avail[3] == 0 && target == 0) {
        // done
        break;
      }

      // we might have to use 1 less quarter than the max possible if
      // we don't have any nickels, otherwise we are always greedy
      final int q = Math.min(target / 25, avail[0]);
      final String[] resultRef = { null };
      final int score = solve(target, q, avail, resultRef);
      if (q > 0) {
        final String[] altRef = { null };
        if (solve(target, q - 1, avail, altRef) < score) {
          resultRef[0] = altRef[0];
        }
      }
      System.out.println(resultRef[0]);
    }
  }

  private static int solve(int target, final int quarters, final int[] avail, final String[] resultRef)
  {
    target -= quarters * 25;
    final int dimes = Math.min(target / 10, avail[1]);
    target -= dimes * 10;
    final int nickels = Math.min(target / 5, avail[2]);
    target -= nickels * 5;
    final int pennies = Math.min(target, avail[3]);
    target -= pennies;
    if (target > 0) {
      resultRef[0] = "Cannot dispense the desired amount.";
      return Integer.MAX_VALUE;
    }
    else {
      resultRef[0] = "Dispense " + quarters + " quarters, " + dimes + " dimes, " +
          nickels + " nickels, and " + pennies + " pennies.";
      return quarters + dimes + nickels + pennies;
    }
  }
}
