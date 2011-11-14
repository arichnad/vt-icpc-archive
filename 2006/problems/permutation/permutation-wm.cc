#define PROBLEM "permutation"

#include <fstream>
#include <vector>

using namespace std;

typedef std::vector<int> ivec;

int main()
{
  ifstream in(PROBLEM ".in");
  ofstream out(PROBLEM ".out");

  for (;;) {
    int n;
    in >> n;
    if (!in || n==0) break;

    ivec a;
    a.resize(n,0);
    for (int k=0; k<n; ++k) in >> a[k];

    ivec pi;
    pi.resize(n,0);
    for (int k=0; k<n; ++k) pi[k]=-1;

    for (int k=0; k<n; ++k) {
      int j=0;
      int i=0;
      for (;;) {
	while (pi[i] >= 0) ++i;
	if (j >= a[k]) break;
	++j,++i;
      }
      pi[i]=k;
    }

    for (int k=0; k<n; ++k) {
      if (k > 0) out << ",";
      out << pi[k]+1;
    }
    out << endl;
  }

  return 0;
}
