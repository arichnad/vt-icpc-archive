import java.io.*;
import java.util.*;
import java.awt.Point;

public class Mole
{
  /** Exclusive upper bound on x */
  private final int _width;

  /** Exclusive upper bound on y */
  private final int _height;

  private final int _terrierCount;

  private final int _observationCount;

  public Mole(final int width, final int height, final int terrierCount, final int observationCount)
  {
    _width = width;
    _height = height;
    _terrierCount = terrierCount;
    _observationCount = observationCount;
  }

  public List solve()
  {
    boolean[][] prev = new boolean[_height][_width];
    for (int y = 0; y < _height; ++y) {
      Arrays.fill(prev[y], true);
    }

    final Point[][] observations = new Point[_terrierCount][_observationCount];
    for (int terrier = 0; terrier < _terrierCount; ++terrier) {
      final int[] data = readLine();
      for (int t = 0; t < _observationCount; ++t) {
        observations[terrier][t] = new Point(data[2 * t], data[2 * t + 1]);
      }
    }

    boolean[][] next = new boolean[_height][_width];

    for (int t = 0; t < _observationCount; ++t) {
      // step (1), terriers catch moles
      for (int terrier = 0; terrier < _terrierCount; ++terrier) {
        final Point pp = observations[terrier][t];
        prev[pp.y][pp.x] = false;
        prev[Math.min(pp.y + 1, _height - 1)][pp.x] = false;
        prev[Math.max(pp.y - 1, 0)][pp.x] = false;
        prev[pp.y][Math.min(pp.x + 1, _width - 1)] = false;
        prev[pp.y][Math.max(pp.x - 1, 0)] = false;
      }

      // step (2a), moles move everywhere possible
      for (int y = 0; y < _height; ++y) {
        for (int x = 0; x < _width; ++x) {
          next[y][x] = prev[y][x] ||
              prev[Math.min(y + 1, _height - 1)][x] ||
              prev[Math.max(y - 1, 0)][x] ||
              prev[y][Math.min(x + 1, _width - 1)] ||
              prev[y][Math.max(x - 1, 0)];
        }
      }
      final boolean[][] tmp = next;
      next = prev;
      prev = tmp;
      // step (2b), moles don't move where terriers are
      for (int terrier = 0; terrier < _terrierCount; ++terrier) {
        final Point pp = observations[terrier][t];
        prev[pp.y][pp.x] = false;
        prev[Math.min(pp.y + 1, _height - 1)][pp.x] = false;
        prev[Math.max(pp.y - 1, 0)][pp.x] = false;
        prev[pp.y][Math.min(pp.x + 1, _width - 1)] = false;
        prev[pp.y][Math.max(pp.x - 1, 0)] = false;
      }

      // step (3) is just the step (1) of the next observation
      // step (4) is just the step (2) of the next observation
    }

    final ArrayList result = new ArrayList();
    for (int y = 0; y < _height; ++y) {
      for (int x = 0; x < _width; ++x) {
        if (prev[y][x]) {
          result.add(new Point(x, y));
        }
      }
    }
    return result;
  }

  static BufferedReader xi = new BufferedReader(new InputStreamReader(System.in));

  static int[] readLine()
  {
    try {
      final String line = xi.readLine().trim();
      final String[] tokens = line.split("[ \t]+");
      final int[] result = new int[tokens.length];
      for (int ii = tokens.length - 1; ii >= 0; --ii) {
        result[ii] = Integer.parseInt(tokens[ii]);
      }
      return result;
    }
    catch (final IOException xx) {
      throw new RuntimeException(xx);
    }
  }

  public static void main(final String[] args)
  {
    for (int observationSet = 1; ; ++observationSet) {
      final int[] dims = readLine();
      if (dims[0] == 0) {
        break;
      }

      // here we translate from the problem statement's
      // inclusive upper bound to our internal representation
      // of an exclusive upper bound
      final List results = new Mole(dims[0] + 1, dims[1] + 1, dims[2], dims[3]).solve();

      System.out.print("Observation Set " + observationSet);
      if (results.size() == 0) {
        System.out.println("\nNo possible locations");
      }
      else {
        for (int ii = 0, size = results.size(); ii < size; ++ii) {
          final Point pp = (Point) results.get(ii);
          System.out.print((ii % 8 == 0 ? "\n(" : " (") + pp.x + "," + pp.y + ")");
        }
        System.out.println();
      }
    }
  }
}

