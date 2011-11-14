import java.io.*;
import java.lang.*;
import java.util.*;

class permutationHC {
    static PrintStream out;
    static Scanner sc;
    static final int MAX_N = 500;
    
    public static void main(String[] args) throws IOException {
	int n, i, j, k;
	int[] a = new int[MAX_N];
	int[] perm = new int[MAX_N];

	sc = new Scanner(System.in);
	out = System.out;

	n = sc.nextInt();
	while (n > 0) {
	    for (i = 0; i < n; i++) {
		a[i] = sc.nextInt();
	    }
	    for (i = n-1, k = 0; i >= 0; i--, k++) {
		for (j = k-1; j >= a[i]; j--) {
		    perm[j+1] = perm[j];
		}
		perm[a[i]] = i+1;
	    }

	    for (i = 0; i < n; i++) {
		out.print(perm[i]);
		if (i < n-1) {
		    out.print(",");
		}
	    }
	    out.print("\n");
	    n = sc.nextInt();
	}


	out.close();
    }
}
