/*************************************************************
**                                                          **
**  Sample solution for "Right-Hand Rule"                   **
**       Steven J Zeil                                      **
**       10/29/2005                                         **
**                                                          **
*************************************************************/


#include <algorithm>
#include <fstream>
#include <iomanip>
#include <iostream>
#include <string>
#include <sstream>
#include <map>
#include <vector>


using namespace std;


typedef vector<vector<char> > Maze;

void readMaze (int w, int h, Maze& maze, istream& in)
{
  maze.clear();
  string line;
  getline (in, line);
  for (int i = 0; i < h; ++i)
    {
      getline (in, line);
      maze.push_back (vector<char>(line.begin(), line.end()));
      if (maze.back().size() != w)
	maze.back().resize(w, 'X');
    }
}

bool canSeeGoalFrom (int x0, int y0, int w, int h, const Maze& maze)
{
  bool sawtheGoal = maze[y0][x0] == 'G';
  bool blocked = false;
  for (int x = x0 - 1; x >= 0 && (!blocked) && (!sawtheGoal); --x)
    {
      sawtheGoal = maze[y0][x] == 'G';
      blocked = maze[y0][x] == 'X';
    }
  blocked = false;
  for (int x = x0 + 1; x < w && (!blocked) && (!sawtheGoal); ++x)
    {
      sawtheGoal = maze[y0][x] == 'G';
      blocked = maze[y0][x] == 'X';
    }
  blocked = false;
  for (int y = y0 - 1; y >= 0 && (!blocked) && (!sawtheGoal); --y)
    {
      sawtheGoal = maze[y][x0] == 'G';
      blocked = maze[y][x0] == 'X';
    }
  blocked = false;
  for (int y = y0 + 1; y < h && (!blocked) && (!sawtheGoal); ++y)
    {
      sawtheGoal = maze[y][x0] == 'G';
      blocked = maze[y][x0] == 'X';
    }
  return sawtheGoal;
}


struct Position {
  int x;
  int y;
  int dir;
  // dir indicates which way we are facing:
  //    0 => facing to the right   (adding to x)
  //    1 => facing down           (subtracting from y)
  //    2 => facing to the left    (subtracting from x)
  //    3 => facing up             (adding to y)
  // Incrementing the direction (mod 4) is equivalent to turning right 
  //    90 degrees.  Decrementing turns you left.
  Position (int xx, int yy, int dd) : x(xx), y(yy), dir(dd) {}

  void move(const Maze&);

};


void Position::move(const Maze& maze)
{
  int y1 = y;
  int x1 = x;
  if (dir == 0)
    ++x1;
  else if (dir == 1)
    --y1;
  else if (dir == 2)
    --x1;
  else
    ++y1;
  if (maze[y1][x1] == 'X')
    // Can't go there, turn left instead
    dir = (dir + 7) % 4;
  else
    {
      // We can go there - step forward and turn to the right
      x = x1;
      y = y1;
      dir = (dir + 1) % 4;
    }
}


ostream& operator<< (ostream& out, const Position& p)
{
  static const char* directions = "RDLU";
  out << "(" << p.x << "," << p.y << ")->" << directions[p.dir];
  return out;
}
 

bool reachesGoalFrom (int xE, int yE, int w, int h, const Maze& maze)
{
  int x = xE;
  int y = yE;

  int direction;
  if (xE == 0)
    direction = 0;
  else if (xE == w-1)
    direction = 2;
  else if (yE == 0)
    direction = 3;
  else
    direction = 1;

  Position position (xE, yE, direction);


  bool sawGoal = canSeeGoalFrom(x, y, w, h, maze);
  bool reachedExit = false;
  while (!sawGoal && !reachedExit)
    {
      // cerr << "Move from " << position << flush;
      position.move(maze);
      // cerr << " to " << position << endl;
      sawGoal = canSeeGoalFrom (position.x, position.y, w, h, maze);
      reachedExit = maze[position.y][position.x] == 'E';
    } 
  return sawGoal;
}


void solveMazes (istream& in)
{
  int w, h;

  while ((in >> w >> h) && (w >= 3) && (h >= 3))
    {
      Maze maze;
      readMaze (w, h, maze, in);
      int nEntrances = 0;
      int nReachGoal = 0;
      for (int x = 0; x < w; ++x)
	for (int y = 0; y < h; y += h-1)
	  if (maze[y][x] == 'E')
	    {
	      ++nEntrances;
	      if (reachesGoalFrom(x, y, w, h, maze))
		++nReachGoal;
	    }
      for (int x = 0; x < w; x += w-1)
	for (int y = 1; y < h-1; ++y)
	  if (maze[y][x] == 'E')
	    {
	      ++nEntrances;
	      if (reachesGoalFrom(x, y, w, h, maze))
		++nReachGoal;
	    }

      cout << "The goal would be found from "  << nReachGoal 
	   << " out of " << nEntrances << " entrances." << endl;

    }
}



int main()
{
  solveMazes(cin);
  // ifstream in ("test2.dat");
  // solveMazes(in);
  return 0;
}
