/*************************************************************
**                                                          **
**  Sample solution for "Making Change"                     **
**       Steven J Zeil                                      **
**       10/25/2005                                         **
**                                                          **
*************************************************************/


#include <iostream>
#include <algorithm>

using namespace std;




int main()
{
  int Q, D, N, P, C, C0;
  bool OK = false;

  cin >> Q >> D >> N >> P >> C0;
  while (Q + D + N + P + C0  > 0)
    {
      C = C0;
      int nQ = min(Q, C / 25);
      C = C - 25 * nQ;
      int nD = min(D, C / 10);
      C = C - 10 * nD;
      int nN = min(N, C / 5);
      C = C - 5 * nN;
      int nP = C;
      OK = (nP <= P);

      // Sometimes we can dispense fewer coins by using 3 dimes instead of
      // a quarter and 5 pennies

      if (nQ >= 1 && nP >= 5 && D-nD >= 3)
	{
	  --nQ;
	  nD += 3;
	  nP -= 5;
	  OK = nP >= 0 && nP <= P;
	}
      if (OK)
	cout << "Dispense " << nQ << " quarters, " 
	     << nD << " dimes, "
	     << nN << " nickels, and "
	     << nP  << " pennies."
	     << endl;
      else
	cout << "Cannot dispense the desired amount." << endl;

      cin >> Q >> D >> N >> P >> C0;
    }

  return 0;
}
