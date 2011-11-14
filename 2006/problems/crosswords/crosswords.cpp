#include <fstream>
#include <iostream>
#include <string>
#include <list>
#include <vector>

using namespace std;


#undef DEBUG


struct Locations {
  int line;
  int column;
  int length;
  bool vertical;
};


vector<Locations> locations;


struct Placement {
  Locations loc;
  int suppressed;

  Placement (const Locations& locat)
    : loc(locat), suppressed(0) {}
};


struct Word {
  string value;
  vector<Placement> possible;
  int placedAt;
  int numPossible;
};

vector<Word> problem;

typedef vector<string> Template;


void print (const Template& layout, ostream& out)
{
  out << "\n";
  for (int i = 0; i < layout.size(); ++i)
    {
      out << layout[i] << endl;
    }
}



bool legal(const Locations& loc, const Template& layout, const string& word)
  // true if it is legal to insert this word at the indicated location
  // given the assignments already reflected in the layout
{
  bool OK = true;
  if (loc.vertical)
    {
      for (int i = 0; OK && i < loc.length; ++i)
	{
	  char c = layout[loc.line+i][loc.column];
	  OK = (c == '.') || (c == word[i]);
	}
    }
  else
    {
      for (int i = 0; OK && i < loc.length; ++i)
	{
	  char c = layout[loc.line][loc.column+i];
	  OK = (c == '.') || (c == word[i]);
	}
    }
  return OK;
}



void place (const Locations& loc, Template& layout, const string& word)
  // Update the layout by placing the word at the indicated location
{
  if (loc.vertical)
    {
      for (int i = 0; i < loc.length; ++i)
	{
	  layout[loc.line+i][loc.column] = word[i];
	}
    }
  else
    {
      for (int i = 0; i < loc.length; ++i)
	{
	  layout[loc.line][loc.column+i] = word[i];
	}
    }

#ifdef DEBUG
  cerr << "\n" << word << " " 
       << ((loc.vertical)? 'V' : 'H')
       << " " << loc.line << " " << loc.column;
  print (layout, cerr);
#endif
}



void trimPossibilities (vector<Word>& problem, 
			const Template& layout, 
			int numAlreadyPlaced)
{
  // For all unassigned words, suppress any possible locations
  // that would conflict with the latest change
  for (int i = numAlreadyPlaced; i < problem.size(); ++i)
    {
      Word& word = problem[i];
      for (int k = 0; k < word.possible.size(); ++k)
	{
	  if (word.possible[k].suppressed <= 0
	      && ! legal(word.possible[k].loc, layout, word.value))
	    {
	      word.possible[k].suppressed = numAlreadyPlaced;
	      --word.numPossible;
	    }
	}
    }
}


void restorePossibilities (vector<Word>& problem, 
			   int numAlreadyPlaced)
{
  // For all unassigned words, restore any possible locations
  // that were suppressed at this same level of the solution.
  for (int i = numAlreadyPlaced; i < problem.size(); ++i)
    {
      Word& word = problem[i];
      for (int k = 0; k < word.possible.size(); ++k)
	{
	  if (word.possible[k].suppressed == numAlreadyPlaced)
	    {
	      word.possible[k].suppressed = 0;
	      ++word.numPossible;
	    }
	}
    }
}











void solve (vector<Word>& problem,
	    int numAlreadyPlaced,
	    Template layout, 
	    bool& solved, ostream& out)
{
  if (numAlreadyPlaced < problem.size())
    {
      // Try to place another word somewhere
      
      // Find the word with the fewest possible positions remaining.
      //  With luck, we will often find words that "have" to be placed
      //  in a specific location.
      int fewestPossible = problem[numAlreadyPlaced].numPossible;
      int bestWordIndex = numAlreadyPlaced;
      for (int i = numAlreadyPlaced+1; i < problem.size(); ++i)
	if (problem[i].numPossible < fewestPossible)
	  {
	    fewestPossible = problem[i].numPossible;
	    bestWordIndex = i;
	  }


#ifdef DEBUG
      if (fewestPossible == 0) 
	{
	  cerr << "Cannot place " << problem[bestWordIndex].value << endl;
	}
#endif

      swap (problem[numAlreadyPlaced], problem[bestWordIndex]);

      // Try each possibility in turn
      Word& word = problem[numAlreadyPlaced];

      for (int k = 0; (!solved) && k < word.possible.size(); ++k)
	{
	  if (word.possible[k].suppressed <= 0
	      && legal(word.possible[k].loc, layout, word.value))
	    {
	      Template newLayout = layout;
	      place(word.possible[k].loc, newLayout, word.value);
	      ++numAlreadyPlaced;
	      word.placedAt = k;
	      trimPossibilities (problem, newLayout, numAlreadyPlaced);
	      
	      solve (problem, numAlreadyPlaced, newLayout, solved, out);
	      
	      if (!solved)
		{
		  word.placedAt = -1;
		  restorePossibilities (problem, numAlreadyPlaced);
		  --numAlreadyPlaced;
		}
	    }
	}
    }
  else
    { // All words have been assigned. Have we filled in all the '.'s?
      solved = true;
      for (int i = 0; solved && i < layout.size(); ++i)
	solved = (layout[i].find('.') == string::npos);
      if (solved)
	print (layout, out);
    }
}



void solve (int problemNumber,
	    vector<Word>& problem,
	    const Template& layout,
	    ostream& out)
{
  bool solved = false;
  cout << "Problem " << problemNumber << flush;
  solve (problem, 0, layout, solved, out);
  if (!solved)
    {
      cout << ": No layout is possible." << endl;
    }
}


int main (int argc, char** argv)
{

  // ifstream in ("crosswords.in");
  istream& in = cin;
  // ofstream out ("crosswords.out");
  ostream& out = cout;

  int problemNumber = 0;
  int nWords, nLines;

  while (in >> nWords && nWords > 0 && in >> nLines && nLines > 0)
    {
      ++problemNumber;


      // 1. Read everything in

      vector<string> words;
      Template lines;
      
      for (int i = 0; i < nWords; ++i)
	{
	  string w;
	  in >> w;
	  words.push_back(w);
	}

      cerr << "Last word in puzzle was " << words.back() << endl;

      string line;
      getline(in, line); // throw away rest of this line
      for (int i = 0; i < nLines; ++i)
	{
	  getline(in, line);
	  lines.push_back (line);
	}

      int width = lines[0].length();



      // 2. Analyze lines to determine possible locations for words
      for (int i = 0; i < nLines; ++i)
	for (int j = 0; j < width; ++j)
	  {
	    if (lines[i][j] == '.' &&
		(j == 0 || lines[i][j-1] == '#'))
	      {
		// a horizontal word goes here
		Locations loc;
		loc.line = i;
		loc.column = j;
		loc.vertical = false;
		loc.length = 1;
		while (j + loc.length < width && lines[i][j+loc.length] == '.')
		  ++loc.length;
		if (loc.length > 1)
		  locations.push_back (loc);
	      }
	    if (lines[i][j] == '.' &&
		(i== 0 || lines[i-1][j] == '#'))
	      {
		// a vertical word goes here
		Locations loc;
		loc.line = i;
		loc.column = j;
		loc.vertical = true;
		loc.length = 1;
		while (i + loc.length < nLines
		       && lines[i+loc.length][j] == '.')
		  ++loc.length;
		if (loc.length > 1)
		  locations.push_back (loc);
	      }
	  }
#ifdef DEBUG
      cerr << locations.size() << " locations found" << endl;
#endif


      // 3. Associate with each word a list of possible placements within
      //    the puzzle
      for (int i = 0; i < nWords; ++i)
	{
	  Word word;
	  word.value = words[i];
	  word.placedAt = -1;
	  word.numPossible = 0;
	  int wlen = word.value.length();
	  for (int j = 0; j < locations.size(); ++j)
	    {
	      if (locations[j].length == wlen)
		{
		  word.possible.push_back (Placement(locations[j]));
		  ++word.numPossible;
	      }
	    }
#ifdef DEBUG
	  cerr << word.value << " can be placed at any of " 
	       << word.numPossible  << " positions" << endl;
#endif
	  problem.push_back(word);
	}

      // 4. Solve the problem

      solve (problemNumber, problem, lines, out);

      problem.clear();
      locations.clear();
    }
  return 0;
}
