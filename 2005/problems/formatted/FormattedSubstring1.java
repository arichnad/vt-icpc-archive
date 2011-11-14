// FormattedSubstring1.java - problem E - ngb

import java.io.*;
import java.util.*;

public class FormattedSubstring1
{
    public static void main(final String[] args) throws IOException
    {
	final BufferedReader xi = new BufferedReader(new InputStreamReader(System.in));
	for (int linenum = 1; ; ++linenum) {
	    final String line = xi.readLine();
	    if (line == null) {
		throw new EOFException("EOF before proper terminating line read");
	    }
	    final int p1 = line.indexOf(' ');
	    final int p2 = line.indexOf(' ', p1 + 1);
	    if (p2 < 0) {
		throw new IllegalArgumentException("incorrect format on line " + linenum +
						   ", should be 'B E STR', was '" + line + "'");
	    }
	    final int begin = Integer.parseInt(line.substring(0, p1));
	    final int end = Integer.parseInt(line.substring(p1 + 1, p2));
	    final String str = line.substring(p2 + 1);
	    if (begin == -1 && end == -1 && str.length() == 0) {
		break;
	    }
	    if (begin < 0 || begin > end || end > str.length()) {
		throw new IllegalArgumentException("invalid positions on line " + linenum +
						   ", cannot begin at " + begin + " and end at " +
						   end + " with text length " + str.length());
	    }

	    System.out.println(solve(begin, end, str));
	}
    }

    private static String solve(final int begin, final int end, final String str)
    {
	final StringBuffer result = new StringBuffer();
	final ArrayList stack = new ArrayList();
	for (int pos = 0; pos < str.length(); ++pos) {
	    if (pos == begin) {
		for (int ii = 0; ii < stack.size(); ++ii) {
		    result.append('<').append(stack.get(ii)).append('>');
		}
	    }
	    if (pos == end) {
		for (int ii = stack.size() - 1; ii >= 0; --ii) {
		    result.append("</").append(stack.get(ii)).append('>');
		}
	    }
	    if (str.charAt(pos) == '<') {
		if (str.charAt(pos + 1) == '/') {
		    stack.remove(stack.size() - 1);
		}
		else {
		    int p = str.indexOf('>', pos);
		    stack.add(str.substring(pos + 1, p));
		}
	    }
	    if (pos >= begin && pos < end) {
		result.append(str.charAt(pos));
	    }
	}
	return result.toString();
    }
}
