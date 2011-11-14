import java.io.*;
import java.util.*;

public class Hubs
{
    static final long INF = Integer.MAX_VALUE;

    final int n;
    final long[][] dist;

    Hubs()
    {
	final int[] dims = readLine();
	n = dims[0];
	final int m = dims[1];
	dist = new long[n][n];
	for (int i = 0; i < n; ++i) {
	    Arrays.fill(dist[i], INF);
	    dist[i][i] = 0;
	}

	for (int i = 0; i < m; ++i) {
	    final int[] road = readLine();
	    final int a = road[0] - 1;
	    final int b = road[1] - 1;
	    final int d = road[2];
	    dist[a][b] = Math.min(dist[a][b], d);
	    dist[b][a] = dist[a][b];
	}

	boolean change = true;
	while (change) {
	    change = false;
	    for (int i = 0; i < n; ++i) {
		for (int j = 0; j < n; ++j) {
		    for (int k = 0; k < n; ++k) {
			final long d = dist[i][j] + dist[j][k];
			if (d < dist[i][k]) {
			    change = true;
			    dist[i][k] = d;
			    dist[k][i] = d;
			}
		    }
		}
	    }
	}
    }

    String[] solve()
    {
	long bestScore = INF;
	final List best = new ArrayList();
	int bestHub1 = -1;
	int bestHub2 = -1;
	int bestAssignedTo1 = -1;
	for (int hub1 = 0; hub1 < n; ++hub1) {
	    for (int hub2 = hub1 + 1; hub2 < n; ++hub2) {
		for (int assignedTo1 = 0; assignedTo1 <= n - 2; ++assignedTo1) {
		    final long score = solve(hub1, hub2, assignedTo1, null);
		    // System.err.println(hub1 + ", " + hub2 + ", " + assignedTo1 + " -> " + score);
		    if (score < bestScore) {
			best.clear();
			bestScore = score;
		    }
		    if (score == bestScore) {
			best.add(new int[]{ hub1, hub2, assignedTo1 });
		    }
		}
	    }
	}
	System.err.println("best average distance = " + (bestScore * 2 / (n * (n - 1.0))) +
			   ", " + best.size() + " solution(s)");
	final int[] assignment = new int[n];
	final String[] result = new String[best.size()];
	for (int solution = 0; solution < best.size(); ++solution) {
	    final StringBuffer buf = new StringBuffer();
	    final int[] bb = (int[]) best.get(solution);
	    solve(bb[0], bb[1], bb[2], assignment);
	    for (int i = 0; i < n; ++i) {
		if (i > 0) {
		    buf.append(' ');
		}
		buf.append(1 + assignment[i]);
	    }
	    result[solution] = buf.toString();
	}
	return result;
    }

    // hub1 and hub2 are zero based, not equal
    // numAssignedTo1 doesn't count the hub itself
    // assignment won't be computed if it is null
    long solve(final int hub1, final int hub2, final int numAssignedTo1,
	       final int[] assignment)
    {
	// We total over paths from i -> j, j >= i.
	// (1+na1)*(1+na2) of these paths are inter-hub.
	// Each of the n(n-1)/2 paths includes a path to
	// and a path from a hub.  Initially we assign all
	// nodes to 1 and compute the improvement to be
	// made by switching to 2, then we greedily pick
	// to be assigned to 2.

	final int numAssignedTo2 = n - 2 - numAssignedTo1;
	long total = dist[hub1][hub2] * (1 + numAssignedTo1) * (1 + numAssignedTo2);
	long[] deltaIfHub2 = new long[n];
	for (int i = 0; i < n; ++i) {
	    total += (n - 1) * dist[hub1][i];
	    deltaIfHub2[i] = (n - 1) * (dist[hub2][i] - dist[hub1][i]);
	}

	// Manually assign hub2
	total += deltaIfHub2[hub2];
	deltaIfHub2[hub2] = INF;

	// Sort the best (most negative, or least positive
	// numAssignedTo1 is wrong) to the front, then
	// assign them.  We will reconstruct the actual
	// assignments below if we need them.
	Arrays.sort(deltaIfHub2);
	for (int i = 0; i < numAssignedTo2; ++i) {
	    total += deltaIfHub2[i];
	}

	if (assignment != null) {
	    int remainingFor2 = numAssignedTo2;
	    for (int i = 0; i < n; ++i) {
		if (i == hub1 || i == hub2) {
		    assignment[i] = -1;
		    continue;
		}
		long target = (n - 1) * (dist[hub2][i] - dist[hub1][i]);
		int j = 0;
		while (j < remainingFor2 && target != deltaIfHub2[j]) {
		    ++j;
		}
		if (j < remainingFor2) {
		    assignment[i] = hub2;
		    // copy the last eligible one down and shrink list
		    // to remove ourself
		    deltaIfHub2[j] = deltaIfHub2[--remainingFor2];
		}
		else {
		    assignment[i] = hub1;
		}
	    }
	}
	   
	return total;
    }

    private static BufferedReader xi;

    static int[] readLine()
    {
	try {
	    if (xi == null) {
		xi = new BufferedReader(new InputStreamReader(System.in));
	    }
	    final String line = xi.readLine();
	    final String[] tokens = line.split("[ \t]");
	    final int[] result = new int[tokens.length];
	    for (int i = 0; i < result.length; ++i) {
		result[i] = Integer.parseInt(tokens[i]);
	    }
	    return result;
	}
	catch (final IOException xx) {
	    throw new RuntimeException(xx);
	}
    }

    public static void main(final String[] args)
    {
	final boolean outputAllResults = args.length > 0 && args[0].equals("all");
	final boolean outputTestScript = args.length > 0 && args[0].equals("tester");

	if (outputTestScript) {
	    System.out.println("#!/usr/bin/perl -w");
	}

        final int numTestCases = readLine()[0];
	for (int i = 0; i < numTestCases; ++i) {
	    final String[] results = new Hubs().solve();
	    if (outputTestScript) {
		System.out.println("$_ = <>;");
		for (int j = 0; j < results.length; ++j) {
		    final String ss = results[j].replaceAll(" ", "\\\\s+");
		    System.out.print("m/^\\s*" + ss + "\\s*$/ or\n  ");
		}
		System.out.println("die \"Incorrect result on test case " +
				   (1 + i) + "\";");
	    }
	    else {
		System.out.println(results[0]);
		if (outputAllResults) {
		    for (int j = 1; j < results.length; ++j) {
			System.out.println(" or " + results[j]);
		    }
		}
	    }
	}

	if (outputTestScript) {
	    System.out.println("<> and die \"Unexpected data after last test case\";");
	    System.out.println("0;");
	}
    }
}
