#include <algorithm>
#include <iostream>
#include <string>
#include <vector>

using namespace std;


struct Shrew {
  string name;
  string genes;

  bool couldBeDescendedFrom (const Shrew& mother, const Shrew& father,
			     string dominance);

};


bool Shrew::couldBeDescendedFrom (const Shrew& mother, const Shrew& father,
				  string dominance)
{
  for (int i = 0; i < dominance.length(); ++i)
    {
      if (dominance[i] == 'D')
	if (genes[i] == '1')
	  {
	    if (mother.genes[i] == '0' && father.genes[i] == '0')
	      return false;
	  }
	else
	  {
	    if (mother.genes[i] == '1' || father.genes[i] == '1')
	      return false;
	  }
      else
	if (genes[i] == '1')
	  {
	    if (mother.genes[i] == '0' || father.genes[i] == '0')
	      return false;
	  }
	else
	  {
	    if (mother.genes[i] == '1' && father.genes[i] == '1')
	      return false;
	  }
    }
  return true;
}






bool operator< (const Shrew& left, const Shrew& right)
{
  return left.name < right.name;
}



int main (int argc, char** argv)
{
  string dominance;
  cin >> dominance;
  string nm;

  vector<Shrew> females;
  vector<Shrew> males;

  while (cin >> nm && nm != "***")
    {
      Shrew shrew;
      shrew.name = nm;
      char gender;
      cin >> gender >> shrew.genes;
      if (gender == 'M')
	males.push_back(shrew);
      else
	females.push_back(shrew);
    }

  sort (females.begin(), females.end());
  sort (males.begin(), males.end());

  
  while (cin >> nm && nm != "***")
    {
      Shrew juvenile;
      juvenile.name = nm;
      cin >> juvenile.genes;

      bool firstPair = true;
      cout << juvenile.name << " by ";
      for (int f = 0; f < females.size(); ++f)
	for (int m = 0; m < males.size(); ++m)
	  {
	    if (juvenile.couldBeDescendedFrom(females[f], males[m], 
					      dominance))
	      {
		if (!firstPair)
		  cout << " or ";
		firstPair = false;
		cout << females[f].name << "-" << males[m].name;
	      }
	  }
      cout << endl;
    }

  return 0;
}

