// Extrusion1 - ngb

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class Extrusion1
{
  public static void main(final String[] args) throws IOException
  {
    final BufferedReader xi = new BufferedReader(new InputStreamReader(System.in));
    while (true) {
      final int n = Integer.parseInt(xi.readLine());
      if (n < 3) {
        break;
      }
      final double[] x = new double[n];
      final double[] y = new double[n];
      for (int i = 0; i < n; ++i) {
        final String[] s = xi.readLine().split(" +");
        x[i] = Double.parseDouble(s[0]);
        y[i] = Double.parseDouble(s[1]);
      }
      final double v = Double.parseDouble(xi.readLine());

      double a = 0;
      for (int i = 0; i < n - 1; ++i) {
        a += area(x[i], y[i], x[i+1], y[i+1]);
      }
      a += area(x[n-1], y[n-1], x[0], y[0]);

      final double len = v / a;
      final long z = Math.round(len * 100);
      System.out.println("BAR LENGTH: " + (z / 100) + "." + ((z / 10) % 10) + (z % 10));
    }
  }

  private static double area(final double x1, final double y1, final double x2, final double y2)
  {
    return x1 * y1 / 2 + (x2 - x1) * y1 + (x2 - x1) * (y2 - y1) / 2 - x2 * y2 / 2;
  }
}
