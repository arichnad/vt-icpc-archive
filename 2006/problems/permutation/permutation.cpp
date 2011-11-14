// Permutation recovery solution by S Zeil
#include <algorithm>
#include <iostream>
#include <iterator>
#include <vector>

using namespace std;

int main(int argc, char** argv)
{
  int n;
  while (cin >> n && n > 0)
    {
      vector<int> a(n,0);
      for (int i = 0; i < n; ++i)
	cin >> a[i];

      vector<int> result;
      for (int k = n; k > 0; --k)
	result.insert (result.begin()+a[k-1], k);

      copy (result.begin(), result.end()-1, 
	    ostream_iterator<int>(cout,","));
      cout << result.back() << endl;
    }
  return 0;
}
