#include <iostream>
#include <string>
#include <set>
#include <map>
#include <queue>
#include <algorithm>

using namespace std;


struct Road {
  string house1;
  string house2;
  double length;

  Road (string h1, string h2, double len)
    : house1(h1), house2(h2), length(len)
  {
    if (h1 > h2)
      swap(house1, house2);
  }

  bool operator< (const Road& r) const
  {
    return house1 < r.house1
      || (house1 == r.house1 && house2 < r.house2)
      || (house1 == r.house1 && house2 == r.house2 && length < r.length);
  }

  bool operator== (const Road& r) const
  {
    return house1 == r.house1 && house2 == r.house2 && length == r.length;
  }

};

bool compareByLength (Road r1, Road r2) {return r1.length > r2.length;}

struct CompareByLength {
  bool operator() (Road r1, Road r2) {return r1.length > r2.length;}
};


typedef string House;

typedef multimap<House, Road> Graph;
typedef pair<Graph::const_iterator, Graph::const_iterator> OutEdges; 

typedef priority_queue<Road, vector<Road>, CompareByLength> PriorityQueue;


void addRoads (PriorityQueue& pq, const Graph& g, House h)
{
  OutEdges newRoads = g.equal_range(h);
  while (newRoads.first != newRoads.second)
    {
      const Graph::value_type& house_road = *(newRoads.first);
      pq.push(house_road.second);
      ++newRoads.first;
    }
}
 

void findMinSpanTree (
   const Graph& g,
   string firstHouse,
   int numHouses,
   set<Road>& spanTree)
{      // Prim's Algorithm
  PriorityQueue  pq;
  spanTree.clear();
  set<House> connectedHouses;

  // Initializethe priority queue with the roads from an arbitrary house
  connectedHouses.insert (firstHouse);
  addRoads (pq, g, firstHouse);

  while (!pq.empty() && connectedHouses.size() < numHouses)
    {
      // Get the shortest road for one of the connected houses
      Road shortestRoad = pq.top();
      pq.pop();
      
      // If at least one house on that road is not yet connected...
      if (connectedHouses.count(shortestRoad.house1) == 0
	  || connectedHouses.count(shortestRoad.house2) == 0)
	{
	  // Add that road to our span tree
	  spanTree.insert(shortestRoad);

	  // Mark the new house as connected
	  string newConnection
	    = (connectedHouses.count(shortestRoad.house1) == 0) 
	    ? shortestRoad.house1 : shortestRoad.house2;
	  connectedHouses.insert(newConnection);

	  // and add all roads to that house into the priority queue
	  addRoads (pq, g, newConnection);
	}
    }
  if (connectedHouses.size() != numHouses)
    spanTree.clear(); // Could not find a spanning tree
}



int main ()
{
  double maxCable;
  int nHouses, nRoads;
  House h1, h2;
  House firstHouse;
  double length;
  Graph g;

  cin >> maxCable;
  cin >> nHouses;
  cin >> firstHouse;
  for (int i = 1; i < nHouses; ++i)
    cin >> h1; // Oddly enough, we don't need to save these
               // - input is redundant

  cin >> nRoads;
  for (int i = 0; i < nRoads; ++i)
    {
      cin >> h1 >> h2 >> length;
      if (h1 != h2)
	{
	  Road road(h1, h2, length);
	  g.insert(Graph::value_type(h1, road));      
	  g.insert(Graph::value_type(h2, road));
	}
    }

  set<Road> minSpanTree;
  findMinSpanTree (g, firstHouse, nHouses, minSpanTree);
  
  if (minSpanTree.size() != nHouses-1)
    cout << "Not enough cable" << endl; // Not enough in the whole world!
  else 
    {
      double total = 0.0;
      for (set<Road>::iterator i = minSpanTree.begin();
	   i != minSpanTree.end(); i++)
	total += i->length;
      if (total <= maxCable)
	{
	  cout.setf(ios::fixed, ios::floatfield);
	  cout.setf(ios::showpoint);
	  cout.precision(1);
	  cout << "Need " << total << " miles of cable" << endl;
	}
      else
	cout << "Not enough cable" << endl;
    }
  return 0;
}
