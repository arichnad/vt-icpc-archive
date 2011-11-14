/*************************************************************
 **                                                         **
 **  Sample solution for "Wordstack"                        **
 **       Steven J Zeil                                     **
 **       10/29/2005                                        **
 **                                                         **
 *************************************************************/


#include <iomanip>
#include <iostream>
#include <set>
#include <vector>
#include <algorithm>


using namespace std;


int bestOffsets[10][10];
int localScores[10][10];



int localScore (const string& w1, const string& w2, int offset)
{
  int score = 0;
  for (int i = 0; i < w1.length(); ++i)
    if (i - offset >= 0 && i - offset < w2.length())
      if (w1[i] == w2[i - offset])
	++score;
  return score;
}




void printSolution(const string* words, 
		   const int* wordOrder, 
		   int n)
{
  int cumulativeOffsets[10];
  cumulativeOffsets[0] = 0;
  for (int i = 1; i < n; ++i)
    cumulativeOffsets[i] = cumulativeOffsets[i-1] + 
      bestOffsets[wordOrder[i]][wordOrder[i-1]];

  cerr << "=======" << endl;
  int minOffset = *(min_element(cumulativeOffsets, 
				cumulativeOffsets+n));

  int offset = 0;
  for (int i = 0; i < n; ++i)
    {
      string w1 = words[wordOrder[i]];
      cerr << string(cumulativeOffsets[i] - minOffset, ' ')
	   << w1 << endl;
    }
  cerr << "=======" << endl;
}




int main()
{
  int n;
  string words[10];
  int wordOrder[10];
  int offsets[10];
  int bestWordOrder[10];


  while ((cin >> n) && (n > 0))
    {
      for (int i = 0; i < n; ++i)
	{
	  string s;
	  cin >> words[i];
	}


      // Pre-compute the best offset for each pair of words
      for (int i = 0; i < n; ++i)
	for (int j = 0; j < i; ++j)
	  {
	    int bestOffset = 0;
	    int bestLocalScore = 0;
	    int wordilen = words[i].length();
	    int wordjlen = words[j].length();
	    for (int k = -wordilen-wordjlen; k < wordilen+wordjlen; ++k)
	      {
		int score = localScore (words[i], words[j], k);
		if (score > bestLocalScore)
		  {
		    bestLocalScore = score;
		    bestOffset = k;
		  }
	      }
	    bestOffsets[i][j] = bestOffsets[j][i] = bestOffset;
	  }
	  
      // Pre-compute the best local (pairwise) score for each pair of words
      for (int i = 0; i < n; ++i)
	for (int j = 0; j < i; ++j)
	  {
	    localScores[i][j] = localScores[j][i]
	      = localScore(words[i], words[j], bestOffsets[i][j]);
	  }
      
      for (int i = 0; i < n; ++i)
	wordOrder[i] = i;
      
      fill (offsets, offsets+n, 0);

      int bestScore = 0;
      int permutations = 0;

      do
	{
	  ++permutations;
	  if (permutations % 100000 == 0)
	    cerr << permutations << endl;

	  int score = 0;
	  for (int i = 1; i < n; ++i)
	    score += localScores[wordOrder[i-1]][wordOrder[i]];

	  if (score > bestScore)
	    {
	      bestScore = score;
	      copy (wordOrder, wordOrder+n, bestWordOrder);
	      // printSolution (words, bestWordOrder, n);
	    }
	} while (next_permutation(wordOrder, wordOrder+n));
      
      
      cout << bestScore << endl;
      printSolution (words, bestWordOrder, n);
    }
  return 0;
}
