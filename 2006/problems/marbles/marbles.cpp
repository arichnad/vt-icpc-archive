// Marbles solution by Steven Zeil

#include <algorithm>
#include <fstream>
#include <iomanip>
#include <iostream>
#include <queue>
#include <stack>

using namespace std;

struct State {
  int cnt[3];


  State (int* c)
  {
    for (int i = 0; i < 3; ++i)
      cnt[i] = c[i];
  }

  State (int c0 = 0, int c1 = 0, int c2 = 0)
  {
      cnt[0] = c0;
      cnt[1] = c1;
      cnt[2] = c2;
  }

  bool legal() const
  {
    return cnt[0] >= 0
      && cnt[1] >= 0
      && cnt[2] >= 0;
  }

  bool isASolution () const
  {
    return cnt[0] == cnt[1]
      && cnt[1] == cnt[2];
  }

  void move(int fromBucket, int toBucket)
  {
    cnt[fromBucket] -= cnt[toBucket];
    cnt[toBucket] *= 2;
  }

  int index () const
  {
    return 60 * cnt[0] + cnt[1];
  }

};

bool operator== (const State& left, const State& right)
{
  return equal(left.cnt, left.cnt+3, right.cnt);
}

bool operator!= (const State& left, const State& right)
{
  return !equal(left.cnt, left.cnt+3, right.cnt);
}


bool operator< (const State& left, const State& right)
{
  return lexicographical_compare(left.cnt, left.cnt+3, 
				 right.cnt, right.cnt+3);
}

istream& operator>> (istream& in, State& s)
{
  in >> s.cnt[0] >> s.cnt[1] >> s.cnt[2];
  return in;
}

ostream& operator<< (ostream& out, const State& s)
{
  out << setw(4) << s.cnt[0] << setw(4) << s.cnt[1] << setw(4) << s.cnt[2];
  return out;
}


int main (int argc, char** argv)
{
  // ifstream in ("marbles.in");
  istream& in = cin;

  // ofstream out ("marbles.out");
  ostream& out  = cout;

  State start;
  State final;
  in >> start;
  while (in)
    {
      State backtrace[3661];
      queue<State> pending;
      backtrace[start.index()] = start;
      pending.push(start);
      bool solved = false;

      // Basic solution idea: do a breadth-first traversal of the set of
      // states that can be reached from the start state until we 
      // find a solution or exhaust the state space.
      while ((!solved) && !pending.empty())
	{
	  State s = pending.front();
	  pending.pop();
	  if (s.isASolution())
	    {
	      solved = true;
	      final = s;
	    }
	  else
	    {
	      // Generate all possible states that are one step 
	      // away from this one
	      for (int i = 0; i < 3; ++i)
		for (int j = 0; j < 3; ++j)
		  if (i != j)
		    {
		      State s2 = s;
		      s2.move(i,j);
		      // Ignore states that are illegal or that 
		      // have already been encountered.
		      if (s2.legal() && backtrace[s2.index()] == State())
			{
			  backtrace[s2.index()] = s;
			  pending.push(s2);
			}
		    }
	    }
	}
      if (solved)
	{
	  State s = final;
	  stack<State> trace;
	  trace.push(s);
	  while (!(s == start))
	    {
	      s = backtrace[s.index()];
	      trace.push(s);
	    }
	  while (!trace.empty())
	    {
	      out << trace.top() << endl;
	      trace.pop();
	    }
	} 
      else
	{
	  out << start << endl;
	}
      out << "============" << endl;
      in >> start;
    }
  return 0;
}
