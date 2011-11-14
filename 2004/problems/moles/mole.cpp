#include <string>
#include <iostream>
#include <set>
#include <vector>
#include <algorithm>

using namespace std;

/********************************************************/


struct Coord {
  int x;
  int y;

  Coord (): x(0), y(0) {}
  Coord (int xx, int yy): x(xx), y(yy) {}
};

bool operator== (const Coord& p1, const Coord& p2)
{
  return p1.x == p2.x && p1.y == p2.y;
}

bool operator< (const Coord& p1, const Coord& p2)
{
  return p1.y < p2.y ||
    (p1.y == p2.y && p1.x < p2.x);
}

ostream& operator<< (ostream& out, const Coord& c)
{
  out << "(" << c.x << "," << c.y << ")";
  return out;
}

/********************************************************/

class Mole {
  int W, H, N, T;

  bool legal(Coord c) const
  {
    return c.x >= 0 && c.y >= 0
      && c.x <= W && c.y <= H;
  }

  void eliminate (set<Coord>& locations, Coord c);
  // Remove from locations the point c and all points adjacent to it

  void addIfLegal (set<Coord>& locations, Coord c);

  void expand (set<Coord>& locations, Coord c);
  // Add to locations all points adjacent to c

  void print (const set<Coord>& locations);

public:
  Mole (int w, int h, int n, int t)
    : W(w), H(h), N(n), T(t)
  {}

  void simulate ();
};


void Mole::simulate ()
{
  // Read the terrier positions
  vector<vector<Coord> > terriers;
  for (int i = 0; i < N; i++)
    {
      vector<Coord> terrier;
      int x, y;
      for (int t = 0; t < T; t++)
	{
	  cin >> x >> y;
	  terrier.push_back(Coord(x,y));
	}
      terriers.push_back(terrier);
    }

  // Initially, the mole could be anywhere
  set<Coord> moleLocations;
  for (int y = 0; y <= H; y++)
    for (int x = 0; x <= W; x++)
      moleLocations.insert(Coord(x,y));
  
  // Run through the possible times
  for (int t = 0; t < T; ++t)
    {
      // Mole cannot be near a terrier
      for (int i = 0; i < N; i++)
	eliminate (moleLocations, terriers[i][t]);
      
      set<Coord> newMoleLocations = moleLocations;
      // Mole can move...
      for (set<Coord>::iterator pos = moleLocations.begin();
	   pos != moleLocations.end(); pos++)
	expand (newMoleLocations, *pos);

      // ...but not next to a terrier
      for (int i = 0; i < N; i++)
	eliminate (newMoleLocations, terriers[i][t]);
      
      moleLocations.swap(newMoleLocations);
    }
  
  if (moleLocations.size() == 0)
    {
      cout << "No possible locations" << endl;
    }
  else
    {
      print (moleLocations);
    }
}

void Mole::print (const set<Coord>& locations)
{
  // Print the possible positions. Note that the operator< on Coord
  // was chosen so that the points would be kept in the desired order
  int cnt = 0;
  for (set<Coord>::iterator pos = locations.begin();
       pos != locations.end(); pos++)
    {
      if (cnt > 0)
	cout << " ";
      cout << *pos;
      cnt++;
      if (cnt == 8)
	{
	  cout << endl;
	  cnt = 0;
	}
    }
  if (cnt != 0)
    cout << endl;
}


void Mole::eliminate (set<Coord>& locations, Coord c)
// Remove from locations the point c and all points adjacent to it
{
  locations.erase (c);
  locations.erase (Coord(c.x-1, c.y));
  locations.erase (Coord(c.x+1, c.y));
  locations.erase (Coord(c.x, c.y-1));
  locations.erase (Coord(c.x, c.y+1));
}



void Mole::addIfLegal (set<Coord>& locations, Coord c)
{
  if (legal(c))
    locations.insert(c);
}

void Mole::expand (set<Coord>& locations, Coord c)
// Add to locations all points adjacent to c
{
  addIfLegal(locations, Coord(c.x-1, c.y));
  addIfLegal(locations, Coord(c.x+1, c.y));
  addIfLegal(locations, Coord(c.x, c.y-1));
  addIfLegal(locations, Coord(c.x, c.y+1));
}



/********************************************************/




int main()
{
  int W, H, N, T;
  cin >> W >> H >> N >> T;
  int count = 0;
  while (W > 0 && H > 0) {
    ++count;
    cout << "Observation Set " << count << endl;
    Mole mole(W, H, N, T);
    mole.simulate();
    cin >> W >> H >> N >> T;
  }
  return 0;
}
