/*************************************************************
**                                                          **
**  Solution checker for "Packing Dominoes"                 **
**       Steven J Zeil                                      **
**       11/11/2005                                         **
**                                                          **
**  Run as:                                                 **
**   checkDominoes2 contestantOutput judgesOutput resultFile**
**                                                          **
*************************************************************/


#include <iostream>
#include <fstream>
#include <set>
#include <vector>
#include <algorithm>


using namespace std;




struct Point {
  int x;
  int y;

  Point (int xx, int yy) : x(xx), y(yy) {}
  Point(): x(0), y(0) {}

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

ostream& operator<< (ostream& out, Point p)
{
  out << "(" << p.x << "," << p.y << ")";
  return out;
}




class Domino 
{
  int num1, num2;
  Point position1, position2;
public:
  Domino (int theNumber1, int theNumber2)
    : num1(theNumber1), num2(theNumber2)
  {}

  int number1 () const {return num1;}
  int number2 () const {return num2;}

  Point pos1() const;
  Point pos2() const;


  int getPositionCode() const;
  void setPositionCode(int pc);

  void setPosition (Point p1, Point p2) {position1 = p1; position2 = p2;}

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

  int points2PositionCode (Point p1, Point p2) const;

  Point positionCode2Point1 (int positionCode) const;
  Point positionCode2Point2 (int positionCode) const;


};

int Problem::points2PositionCode (Point p1, Point p2) const
{
  int k = p1.x + w * p1.y;
  k = 4 * k;
  if (p2.y == p1.y + 1)
    k += 0;
  else if (p2.x == p1.x + 1)
    k += 1;
  else if (p2.y == p1.y - 1)
    k += 2;
  else
    k += 3;
  return k;
}

Point Problem::positionCode2Point1 (int positionCode) const
{
  int k = positionCode / 4;
  return Point (k % w, k / w);
}

  
Point Problem::positionCode2Point2 (int positionCode) const
{
  Point p = positionCode2Point1(positionCode);
  int orientation = positionCode % 4;
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


Problem problem;




Point Domino::pos1() const
{
  return position1;
}

Point Domino::pos2() const
{
  return position2;
}

int Domino::getPositionCode() const
{
  return problem.points2PositionCode(position1, position2);
}

void Domino::setPositionCode(int pc)
{
  position1 = problem.positionCode2Point1(pc);
  position2 = problem.positionCode2Point2(pc);
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


ostream& operator<< (ostream& out, Domino d)
{
  out << "domino [" << d.number1() << "|" << d.number2() << "]@"
      << d.pos1() << ":" << d.pos2();
  return out;
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


#define check(x,msg) if (OK && !(x)) {OK = false; result = msg; cout << msg << endl;}

int main(int argc, char** argv)
{
  char* contestantFile = (argv[1]);
  char* judgesFile = (argv[2]);
  char* resultFile = (argv[3]);

  ifstream contestantIn (contestantFile);
  ifstream judgesIn (judgesFile);

  bool OK = true;
  string result = "accepted";

  while (OK && (judgesIn >> problem.setSize) && problem.setSize > 0)
    {
      judgesIn >> problem.w >> problem.h;

      int k;
      check ((contestantIn >> k),
	     "formatting error or premature end of contestant output");
      check (k==problem.setSize, "wrong output");

      if (OK) {
	check ((contestantIn >> k),
	       "formatting error or premature end of contestant output");
	check (k==problem.w, "wrong output");
      }

      if (OK) {
	check ((contestantIn >> k),
	       "formatting error or premature end of contestant output");
	check (k==problem.h, "wrong output");
      }

      const string notPossible = "No packing is possible";
      string line;
      if (OK)
	{
	  getline (judgesIn, line);
	  getline (judgesIn, line);
	  if (line.find(notPossible) != string::npos)
	    {
	      getline (contestantIn, line);
	      getline (contestantIn, line);
	      check (line.find(notPossible) != string::npos,
		     "wrong answer - no packing possible");
	    }
	  else
	    {
	      problem.dominoes.clear();
	      for (int i = 0; OK && i <= problem.setSize; ++i)
		for (int j = i; OK && j <= problem.setSize; ++j)
		  {
		    if (i != 0 || j != 0)
		      getline (judgesIn, line);

		    int n1, n2, x1, y1, x2, y2;
		    check (contestantIn >> n1 >> n2, 
			   "wrong output or format error");
		    check ((n1 == i) && (n2 == j), 
			   "wrong output");
		    
		    if (OK) {
		      Domino d(i,j);
		      check (contestantIn >> x1 >> y1 >> x2 >> y2,
			     "format error");
		      d.setPosition(Point(x1, y1), Point(x2, y2));
		      check (d.legallyPositioned(),
			     "domino lies outside the packing area");
		      for (int k = 0; k < problem.dominoes.size(); ++k)
			check(d.okWith(problem.dominoes[k]),
			      "dominoes conflict ");
		      problem.dominoes.push_back (d);
		    }
		  }
	    }
	}
    }

  ofstream resultOut (resultFile);
  resultOut << "<?xml version=\"1.0\"?>" << endl;
  if (OK)
    {
      resultOut << "<result outcome=\"accepted\" security=\""
	    << resultFile
	    << "\">"  << "</result>"
	    << endl;
    } 
  else
    {
      resultOut << "<result outcome=\"invalid\" security=\""
		<< resultFile
		<< "\">" << result << "</result>"
		<< endl;
    }
  return 0;
}
