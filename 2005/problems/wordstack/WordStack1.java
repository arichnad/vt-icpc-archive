// WordStack1 - ngb

import java.io.*;

public class WordStack1
{
  public static void main(final String[] args) throws IOException
  {
    final BufferedReader xi = new BufferedReader(new InputStreamReader(System.in));
    int n;
    while ((n = Integer.parseInt(xi.readLine())) > 0) {
      final String[] words = new String[n];
      for (int i = 0; i < n; ++i) {
        words[i] = xi.readLine();
      }

      // note that oversizing the array allows cur==n in the call to visit
      // to indicate zero dist to first choice
      final int[][] d = new int[n + 1][n + 1];
      for (int i = 0; i < n; ++i) {
        for (int j = i + 1; j < n; ++j) {
          d[i][j] = d[j][i] = dist(words, i, j);
        }
      }

      System.out.println(visit(n, d, new boolean[n]));
    }
  }

  private static int dist(final String[] words, final int i, final int j)
  {
    int best = 0;
    final int iLen = words[i].length();
    for (int offset = -(iLen - 1); offset <= (iLen - 1); ++offset) {
      int score = 0;
      for (int k = 0; k < iLen; ++k) {
        if (k + offset >= 0 && k + offset < words[j].length()) {
          if (words[i].charAt(k) == words[j].charAt(k + offset)) {
            ++score;
          }
        }
      }
      best = Math.max(best, score);
    }
    return best;
  }

  private static int visit(final int cur, final int[][] d, final boolean[] v)
  {
    int best = 0;
    for (int i = 0; i < v.length; ++i) {
      if (!v[i]) {
        v[i] = true;
        best = Math.max(best, d[cur][i] + visit(i, d, v));
        v[i] = false;
      }
    }
    return best;
  }
}
