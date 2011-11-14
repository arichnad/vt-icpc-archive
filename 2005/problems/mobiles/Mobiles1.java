// Mobiles1.java - problem F - ngb

import java.io.*;
import java.util.*;

public class Mobiles1
{
    static final double EPS = 1e-9;

    private interface Weighted
    {
	double getMinWeight();
	boolean isWeightVariable();
	double[] getProfile();
	double solve();
    }

    private static class Bar implements Weighted
    {
	private final double length;
	private final Weighted left;
	private final Weighted right;

	private double minWeight = Double.NaN;

	Bar(final double length_,
	    final Weighted left_,
	    final Weighted right_)
	{
	    length = length_;
	    left = left_;
	    right = right_;
	}

	public double getMinWeight()
	{
	    if (Double.isNaN(minWeight)) {
		minWeight = left.getMinWeight() + right.getMinWeight();
	    }
	    return minWeight;
	}

	public boolean isWeightVariable()
	{
	    return left.isWeightVariable() || right.isWeightVariable();
	}

	public double[] getProfile()
	{
	    final double[] aa = left.getProfile();
	    final double[] bb = right.getProfile();
	    if (aa == null || bb == null) {
		return null;
	    }

	    final double[] result = new double[1 + Math.max(aa.length, bb.length)];
	    result[0] = length * 0.5;
	    for (int ii = Math.min(aa.length, bb.length) - 1; ii >= 0; --ii) {
		double space = length - (aa[ii] + bb[ii]);
		if (space < EPS) {
		    return null;
		}
	    }		
	    for (int ii = 0; ii < aa.length; ++ii) {
		result[ii+1] = length * 0.5 + aa[ii];
	    }
	    for (int ii = 0; ii < bb.length; ++ii) {
		result[ii+1] = Math.max(result[ii+1], length * 0.5 + bb[ii]);
	    }
	    return result;
	}

	public double solve()
	{
	    final double aa = left.getMinWeight();
	    final double bb = right.getMinWeight();
	    if (left.isWeightVariable()) {
		// aa + X = bb
		final double x = bb - aa;
		if (x <= EPS || Math.abs(x - left.solve()) > EPS) {
		    return Double.NaN;
		}
		return x;
	    }
	    else {
		final double x = aa - bb;
		if (x <= EPS || Math.abs(x - right.solve()) > EPS) {
		    return Double.NaN;
		}
		return x;
	    }
	}
    }

    private static class Decorative implements Weighted
    {
	private final double weight;

	Decorative(final double weight_)
	{
	    weight = weight_;
	}

	public double getMinWeight()
	{
	    return weight;
	}

	public boolean isWeightVariable()
	{
	    return false;
	}

	public double[] getProfile()
	{
	    return new double[1];
	}

	public double solve()
	{
	    return Double.NaN;
	}
    }

    private static class Variable extends Decorative
    {
	Variable()
	{
	    super(0.0);
	}

	public boolean isWeightVariable()
	{
	    return true;
	}
    }

    public static void main(final String[] args) throws IOException
    {
	final ArrayList pieces = new ArrayList();

	final BufferedReader xi = new BufferedReader(new InputStreamReader(System.in));
	for (int linenum = 1; ; ++linenum) {
	    final String line = xi.readLine();
	    if (line == null) {
		throw new EOFException("EOF before proper terminating line");
	    }
	    final String[] tokens = line.split(" +");
	    final int index = Integer.parseInt(tokens[0]);
	    if (index <= 0) {
		System.out.println(solve(pieces));
		pieces.clear();
		if (index < 0) {
		    break;
		}
	    }
	    else {
		pieces.add(tokens);
	    }	    
	}
    }

    private static String solve(final ArrayList pieces)
    {
	final Weighted[] parts = new Weighted[pieces.size()];
	final boolean[] top = new boolean[pieces.size()];
	Arrays.fill(top, true);
	int xIndex = -1;
	for (int ii = 0; ii < parts.length; ++ii) {
	    parse(pieces, parts, ii, top);
	    if (parts[ii] instanceof Variable) {
		xIndex = ii;
	    }
	}
	for (int ii = 0; ii < parts.length; ++ii) {
	    if (top[ii]) {
		final double x = parts[ii].solve();
		if (Double.isNaN(x)) {
		    return "The mobile cannot be balanced.";
		}
		final boolean swings = parts[ii].getProfile() != null;

		final long xx = (long) Math.round(x * 100);
		return "Object " + (xIndex + 1) + " must have weight " + (xx / 100) + "." +
		    ((xx / 10) % 10) + (xx % 10) + "\nThe mobile will " +
		    (swings ? "" : "not ") + "swing freely.";
	    }
	}
	throw new RuntimeException();
    }

    private static Weighted parse(final ArrayList pieces, 
				  final Weighted[] parts, 
				  final int index,
				  final boolean[] top)
    {
	if (parts[index] == null) {
	    final String[] tokens = (String[]) pieces.get(index);
	    if (tokens[1].equals("B")) {
		final int leftIndex = Integer.parseInt(tokens[3]) - 1;
		final int rightIndex = Integer.parseInt(tokens[4]) - 1;
		final Bar b = new Bar(Double.parseDouble(tokens[2]), 
				      parse(pieces, parts, leftIndex, top),
				      parse(pieces, parts, rightIndex, top));
		top[leftIndex] = false;
		top[rightIndex] = false;
		parts[index] = b;
	    }
	    else if (tokens[2].equals("X")) {
		parts[index] = new Variable();
	    }
	    else {
		parts[index] = new Decorative(Double.parseDouble(tokens[2]));
	    }
	}
	return parts[index];
    }
}
