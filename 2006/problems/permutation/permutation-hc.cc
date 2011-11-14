#include <fstream>
#include <cassert>

using namespace std;

#define MAX_N 500

int main(void)
{
  int n, a[MAX_N], perm[MAX_N], i, j, k;

  ifstream in("permutation.in");
  ofstream out("permutation.out");
  assert(in.good() && out.good());

  in >> n;
  while (n > 0) {
    for (i = 0; i < n; i++) {
      in >> a[i];
    }
    for (i = n-1, k = 0; i >= 0; i--, k++) {
      /* shift last a[i] elements one to the right */
      for (j = k-1; j >= a[i]; j--) {
	perm[j+1] = perm[j];
      }
      
      perm[a[i]] = i+1;
    }

    for (i = 0; i < n; i++) {
      out << perm[i] << ((i < n-1) ? ',' : '\n');
    }
    
    in >> n;
  }

  return 0;
}
