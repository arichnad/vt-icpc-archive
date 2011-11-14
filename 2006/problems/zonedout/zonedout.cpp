#include <algorithm>
#include <iostream>
#include <iterator>
#include <set>
#include <string>
#include <sstream>
#include <queue>
#include <vector>

using namespace std;

struct Clerk {
  set<int> addsMarks;
  set<int> erases;
  set<int> distributesTo;
  set<int> logbook;
  bool firstTime;
};


struct Event {
  int clerkNum;
  set<int> form;

  Event (int clerk, const set<int>& aForm)
    : clerkNum(clerk), form(aForm)
    {}

};



istream& operator>> (istream& in, set<int>& s)
{
  string line;
  getline(in, line);
  s.clear();

  istringstream lineIn (line);
  copy (istream_iterator<int>(lineIn),
	istream_iterator<int>(),
	inserter(s, s.end()));
  return in;
}


ostream& operator<< (ostream& out, const set<int>& s)
{
  bool firstTime = true;
  for (set<int>::const_iterator p = s.begin(); p != s.end(); ++p)
    {
      if (!firstTime)
	out << ' ';
      out << *p;
      firstTime = false;
    }
  return out;
}




int main ()
{
  int numMarks, numClerks;
  string dummy;

  cin >> numMarks >> numClerks;

  while (numMarks > 0 && numClerks > 0) 
    {
      getline(cin,dummy);

      Clerk* clerks = new Clerk[numClerks];

      for (int i = 0; i < numClerks; ++i)
	{
	  cin >> clerks[i].addsMarks
	      >> clerks[i].erases
	      >> clerks[i].distributesTo;
	  clerks[i].firstTime = true;
	}
      
      queue<Event> q;
      q.push (Event(0,set<int>()));
      
      while (!q.empty())
	{
	  Event event = q.front();
	  q.pop();
	  set<int> form = event.form;
	  Clerk& clerk = clerks[event.clerkNum];
	  copy (clerk.logbook.begin(),
		clerk.logbook.end(),
		inserter(form, form.end()));
	  copy (clerk.addsMarks.begin(),
		clerk.addsMarks.end(),
		inserter(form, form.end()));
	  set<int> erasedForm;
	  set_difference (form.begin(), form.end(),
			  clerk.erases.begin(),
			  clerk.erases.end(),
			  inserter(erasedForm, erasedForm.end()));
	  if (clerk.firstTime || erasedForm != clerk.logbook) {
	    clerk.firstTime = false;
	    copy (erasedForm.begin(), erasedForm.end(), 
		  inserter(clerk.logbook, clerk.logbook.end()));
	    for (set<int>::iterator p = clerk.distributesTo.begin();
		 p != clerk.distributesTo.end(); ++p)
	      {
		q.push (Event(*p, erasedForm));
	      }
	    cerr << "clerk " << event.clerkNum
		 << " sends " << erasedForm << endl;
	  }
	}
      
      cout << clerks[0].logbook << endl;

      cin >> numMarks >> numClerks;
    }

  return 0;
}
