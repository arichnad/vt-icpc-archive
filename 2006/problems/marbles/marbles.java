// Marbles solution by Steven Zeil

import java.io.*;
import java.util.*;

public class marbles {
  
  class State {
	public int[] cnt = new int[3];
    
	public State (int c0, int c1, int c2)
	{
      cnt[0] = c0;
      cnt[1] = c1;
      cnt[2] = c2;
	}
    
	public State (int index, int sum)
	{
      cnt[0] = index / 60;
      cnt[1] = index % 60;;
      cnt[2] = sum - cnt[0] - cnt[1];
	}
    
    
	public boolean legal()
	{
      return cnt[0] >= 0
		&& cnt[1] >= 0
		&& cnt[2] >= 0;
	}
    
	public boolean isASolution ()
	{
      return cnt[0] == cnt[1]
		&& cnt[1] == cnt[2];
	}
    
	public State move(int fromBucket, int toBucket)
	{
      State s = new State(cnt[0], cnt[1], cnt[2]);
      s.cnt[fromBucket] -= s.cnt[toBucket];
      s.cnt[toBucket] *= 2;
      return s;
	}
    
	public String toString()
	{
      return String.format ("%4d%4d%4d", cnt[0], cnt[1], cnt[2]);
	}
    
	public boolean equals (State s)
	{
      return cnt[0] == s.cnt[0]
		&& cnt[1] == s.cnt[1]
		&& cnt[2] == s.cnt[2];
	}
    
	public int index ()	    
	{
      return cnt[0]*60 + cnt[1];
	}
    
  }
  
  
  public static void main (String[] argv) throws java.io.IOException
  {
	marbles problem = new marbles();
	problem.solve();
  }
  
  
  public void solve() throws java.io.IOException
  {
    /*
    BufferedReader in =
      new BufferedReader(new FileReader("marbles.in"));
    PrintWriter out =
      new PrintWriter(new FileWriter("marbles.out"));
    */
    BufferedReader in =
      new BufferedReader(new InputStreamReader (System.in));
    PrintWriter out =
      new PrintWriter(new OutputStreamWriter (System.out));
    
	try {
      String line = in.readLine();
      String[] nums = line.split(" +");
      State start = new State(Integer.parseInt(nums[0]),
                              Integer.parseInt(nums[1]),
                              Integer.parseInt(nums[2]));
      while (start.cnt[0] > 0) {
		State stop = new State(0,0,0);
		int sum = start.cnt[0] + start.cnt[1] + start.cnt[2];
		int[] backtrace = new int[3661];
		Arrays.fill (backtrace, 0);
		boolean solved = false;
		
		LinkedList<State> pending = new LinkedList<State>();
		pending.add (start);
		
		// Basic solution idea: do a breadth-first traversal of the
		// set of states that can be reached from the start state
		// until we find a solution or exhaust the state space.
		while ((!solved) &&  pending.size() > 0) {
          State s = pending.getFirst();
          pending.remove();
          if (s.isASolution()) {
			solved = true;
			stop = s;
          } else {
			// Generate all possible states that are one step 
			// away from this one
			for (int i = 0; i < 3; ++i)
              for (int j = 0; j < 3; ++j)
				if (i != j) {
                  State s2 = s.move(i,j);
                  // Ignore states that are illegal or that 
                  // have already been encountered.
                  if (s2.legal() && 
                      backtrace[s2.index()] == 0) {
					backtrace[s2.index()] = s.index();
					pending.add(s2);
                  }
				}
          }
		}
		if (solved) {
          State s = stop;
          LinkedList<State> trace = new LinkedList<State>();
          trace.addFirst(s);
          while (!(s.equals(start))) {
			s = new State(backtrace[s.index()], sum);
			trace.addFirst(s);
          }
          for (Iterator<State> it = trace.iterator();
               it.hasNext(); ) {
			State s0 = it.next();
			out.println(s0);
          }
		} else {
          out.println(start);
		}
		out.println ("============");
        
		line = in.readLine();
		if (line != null) {
          nums = line.split(" +");
          start = new State(Integer.parseInt(nums[0]),
                            Integer.parseInt(nums[1]),
                            Integer.parseInt(nums[2]));
		} else {
          start = new State(0,0,0);
		}
      }
	} catch (java.io.IOException ex) {}
	in.close();
	out.close();
  }
}
