#include <algorithm>
#include <iostream>
#include <string>

using namespace std;

int main()
{
  string line;
  getline (cin, line);
  while (line != "***")
    {
      int i = 0;
      int j = line.size()-1;
      while (j > i)
	{
	  swap(line[i],line[j]);
	  --j;
	  ++i;
	}
      cout << line << endl;
      getline (cin, line);
    }
  return 0;
}
