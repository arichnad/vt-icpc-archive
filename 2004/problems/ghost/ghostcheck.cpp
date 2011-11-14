#include <string>
#include <iostream>
#include <set>

using namespace std;


void countWinnersAndLosers (const set<string>& vocab, string seq,
			    int numPlayers,
			    int& numWinners, int& numLosers)
{
  numWinners = numLosers = 0;
  if (vocab.count(seq) > 0)
    numLosers = 1;
  else
    {
      for (set<string>::const_iterator w = vocab.begin();
	   w != vocab.end() && (numWinners == 0 || numLosers == 0); w++)
	{
	  if (w->size() > seq.size()
	      && w->substr(0, seq.size()) == seq)
	    {
	      int remainingChars = w->size() - seq.size();
	      if (remainingChars % numPlayers == 0)
		++numLosers;
	      else{
		++numWinners;
		cout << "Winner " << w->substr(0,w->size()) << endl;
	      }
	    }
	}
    }
}


// Main game routine
void ghost (int numPlayers, const set<string>& vocab, string seq)
{
  if (vocab.count(seq) > 0)
    // If previous player has completed a word, challenge
    cout << "Challenge" << endl;
  else
    {
      // Collect all characters representing a possible extension
      set<char> possibleExtensions;
      for (set<string>::const_iterator w = vocab.begin();
	   w != vocab.end(); w++)
	{
	  if (w->size() > seq.size()
	      && w->substr(0, seq.size()) == seq)
	    {
	      char extension = (*w)[seq.size()];
	      possibleExtensions.insert (extension);
	    }
	}

      if (possibleExtensions.size() == 0)
	{
	  // If there are no possible extensions, challenge!
	  cout << "Challenge" << endl;
	  return;
	}

      string possibleWinner = "";
      string guaranteedWinner = "";
      int possibleCount = 0;
      int guaranteedCount = 0;
      for (set<char>::iterator c = possibleExtensions.begin();
	   c != possibleExtensions.end(); c++)
	{
          // Check each extension
	  char extension = *c;
	  int numWinners;
	  int numLosers;
	  // Count the numer of words that can be formed from
	  // the sequence and this extension, differentiating between
          // losers (words that would bw finished by the computer player)
	  // and winners (words that would be completed by another
	  // player.)
	  countWinnersAndLosers (vocab, seq + extension,
				 numPlayers, numWinners, numLosers);
	  if (numLosers == 0)
	    {
	      // We can force a win
	      guaranteedWinner = extension;
	      ++guaranteedCount;
	      cerr << "Guaranteed win #" <<guaranteedCount << ": " << extension << endl;
	    }
	  else if (numWinners > 0)
	    {
	      // Not guaranteed to be a winner or a loser - 
	      // remember this and keep looking 
	      possibleWinner = extension;
	      ++possibleCount;
	      cerr << "Posssible win #" << possibleCount << ": "<< extension << endl;
	    }
	}
      if (guaranteedWinner != "")
	cout << guaranteedWinner << endl;
      else if (possibleWinner != "")
	cout << possibleWinner << endl;
      else
	cout << "Bluff" << endl;
    }
}


int main()
{
  int numPlayers;

  cin >> numPlayers;
  while (numPlayers >= 2)
    {
      set<string> vocabulary;
      string word;
      getline (cin, word); // discard EOL after numPlayers
      getline (cin, word);
      while (word.size() > 0)
	{
	  if (word.size() > 3)  // Only 4-letter or longer words 
	    vocabulary.insert(word);
	  getline (cin, word);
	}


      // Need to remove all words that are extensions of other words
      set<string> goodwords;
      for (set<string>::iterator p = vocabulary.begin();
	   p != vocabulary.end(); p++)
	{
	  string word = *p;
	  bool OK = true;
	  for (int i = 4; OK && i < word.size(); i++) 
	    {
	      if (vocabulary.count(word.substr(0,i)) > 0)
		OK = false;
	    }
	  if (OK)
	    goodwords.insert (word);
	  else
	    cerr << "Removed " << word << endl;
	}
      
      string sequence;
      getline (cin, sequence);

      cout << sequence << " ";
      ghost (numPlayers, goodwords, sequence);
      cin >> numPlayers;
    }
  return 0;
}
