/*************************************************************
**                                                          **
**  Sample solution for "Packing Dominoes"                  **
**       Steven J Zeil                                      **
**       10/26/2005                                         **
**                                                          **
*************************************************************/


#include <iostream>
#include <set>
#include <vector>
#include <algorithm>


using namespace std;




struct Point {
  int x;
  int y;

  Point (int xx, int yy) : x(xx), y(yy) {}

  bool isAdjacentTo (const Point& p) const;

  bool operator== (const Point& p) const
  {return x == p.x && y == p.y;}

};

bool Point::isAdjacentTo (const Point& p) const
{
  if (x == p.x && (y == p.y - 1 || y == p.y + 1))
    return true;
  else if (y == p.y && (x == p.x - 1 || x == p.x + 1))
    return true;
  else
    return false;
}


class Domino 
{
  int num1, num2;
  int positionCode;
public:
  Domino (int theNumber1, int theNumber2)
    : num1(theNumber1), num2(theNumber2), positionCode(0)
  {}

  int number1 () const {return num1;}
  int number2 () const {return num2;}

  Point pos1() const;
  Point pos2() const;


  int getPositionCode() const {return positionCode;}
  void setPositionCode(int pc) {positionCode = pc;}

  // True if contained within w*h rectangle 
  bool legallyPositioned ();
    
  bool okWith (const Domino& d) const;
  // true if two dominoes neither overlap nor have adjacent squares with
  //   non-equal numbers

};


struct Problem {
  int setSize;
  int w;
  int h;
  vector<Domino> dominoes;
};
Problem problem;




Point Domino::pos1() const
{
  int k = positionCode / 4;
  return Point (k % problem.w, k / problem.w);
}

Point Domino::pos2() const
{
  int orientation = positionCode % 4;
  int k = positionCode / 4;
  Point p (k % problem.w, k / problem.w);
  if (orientation == 0)
    ++p.y;
  else if (orientation == 1)
    ++p.x;
  else if (orientation == 2)
    --p.y;
  else
    --p.x;
  return p;
}


bool Domino::legallyPositioned ()
{
  Point p1 = pos1();
  if (p1.x < 0 || p1.x >= problem.w)
    return false;
  if (p1.y < 0 || p1.y >= problem.h)
    return false;
  Point p2 = pos2();
  if (p2.x < 0 || p2.x >= problem.w)
    return false;
  if (p2.y < 0 || p2.y >= problem.h)
    return false;
  return true;
}


bool Domino::okWith (const Domino& d) const
{
  Point p11 = pos1();
  Point p12 = pos2();
  Point p21 = d.pos1();
  Point p22 = d.pos2();
  if (p11 == p21 || p11 == p22 || p12 == p21 || p12 == p22)
    return false;
  if (p11.isAdjacentTo(p21) && number1() != d.number1())
    return false;
  if (p11.isAdjacentTo(p22) && number1() != d.number2())
    return false;
  if (p12.isAdjacentTo(p21) && number2() != d.number1())
    return false;
  if (p12.isAdjacentTo(p22) && number2() != d.number2())
    return false;
  return true;
}


bool dominoIsPlacedWell (int dominoNum)
{
  Domino& d = problem.dominoes[dominoNum];
  if (!d.legallyPositioned())
    {
      // cerr << "domino " << dominoNum << " is not legally positioned." << endl;
      return false;
    }
  for (int i = 0; i < dominoNum; ++i)
    if (!d.okWith(problem.dominoes[i]))
    {
      // cerr << "domino " << dominoNum << " clashes with " << i << endl;
      return false;
    }
  return true;
}



int main()
{
  while ((cin >> problem.setSize) && problem.setSize > 0)
    {
      cin >> problem.w >> problem.h;
      problem.dominoes.clear();

      for (int i = 0; i <= problem.setSize; ++i)
	for (int j = i; j <= problem.setSize; ++j)
	{
	  // cerr << "push " << i << " " << problem.dominoes.size() << endl;
	  problem.dominoes.push_back (Domino(i,j));
	}
      int numDominoes = problem.dominoes.size();


      // Iterative backtracking solution
      int numDominoesPlaced = 0;
      int maxPositionCode = 4 * problem.w * problem.h;

      while (numDominoesPlaced >= 0 && numDominoesPlaced < numDominoes)
	{
	  /*
	  for (int i = 0; i < numDominoes; ++i)
	    cerr << problem.dominoes[i].getPositionCode() << " ";
	  cerr << endl;
	  */
	  Domino& d = problem.dominoes[numDominoesPlaced];
	  if (d.getPositionCode() >= maxPositionCode)
	    {
	      d.setPositionCode(0);
	      if (numDominoesPlaced > 0)
		{
		  Domino& d0 = problem.dominoes[numDominoesPlaced-1];
		  d0.setPositionCode(d0.getPositionCode() + 1);
		}
	      --numDominoesPlaced;
	    }
	  else if (dominoIsPlacedWell(numDominoesPlaced)) 
	    {
	      if (numDominoesPlaced < numDominoes)
		{
		  Domino& d0 = problem.dominoes[numDominoesPlaced+1];
		  d0.setPositionCode(0);
		}
	      ++numDominoesPlaced;
	    }
	  else
	    {
	      d.setPositionCode(d.getPositionCode() + 1);
	    }
	}


      // Problem output
      cout << problem.setSize << " " 
	   << problem.w << " " << problem.h << endl;
      if (numDominoesPlaced == numDominoes)
	for (int i = 0; i < numDominoes; ++i)
	  {
	    Domino& d = problem.dominoes[i];
	    cout << d.number1() << " "
		 << d.number2() << " ";
	    Point p1 = d.pos1();
	    Point p2 = d.pos2();
	    cout << p1.x << " " << p1.y << " "
		 << p2.x << " " << p2.y << " "
		 << endl;
	  }
      else
	cout << "No packing is possible" << endl;
    }
  return 0;
}
