/*************************************************************
**                                                          **
**  Sample solution for "Making Change"                     **
**       Steven J Zeil                                      **
**       11/8/2005                                         **
**                                                          **
*************************************************************/


#include <iomanip>
#include <iostream>
#include <string>
#include <list>

using namespace std;





int main()
{

  string units, name;
  double a, daily, pct;

  list<string> zeroAmounts;

  while ((cin >> a >> units >> daily) && a >= 0.0)
    {
      getline (cin, name);
      pct = 100.0 * a / daily;
      if (pct >= 1.0) 
	{
	  cout << name.substr(1) << " " 
	       << fixed << setprecision(1) << a 
	       << " " << units << " "
	       << fixed << setprecision(0) << pct
	       << "%" << endl;
	} 
      else
	zeroAmounts.push_back(name.substr(1));
    } 
  cout << "Provides no significant amount of:" << endl;
  for (list<string>::iterator i = zeroAmounts.begin(); 
       i != zeroAmounts.end(); ++i)
    cout << *i << endl;
  return 0;
}
