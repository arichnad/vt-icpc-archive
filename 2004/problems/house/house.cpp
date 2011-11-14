#include <iostream>
#include <string>
#include <algorithm>
#include <list>

using namespace std;


struct HorizontalSegment {
  double x1;
  double x2;
  double y;

  HorizontalSegment (double xx1, double xx2, double yy)
    : x1(xx1), x2(xx2), y(yy)
    {
      if (x1 > x2)
	swap(x1, x2);
    }
};

bool operator< (HorizontalSegment a, HorizontalSegment b)
{
  return (a.x2 - a.x1) < (b.x2 - b.x1);
}




typedef list<HorizontalSegment> SegmentList;

class Line {
  double x1, x2, y1, y2;

public:
  Line (double xx1, double yy1, double xx2, double yy2)
    : x1(xx1), y1(yy1), x2(xx2), y2(yy2)
    {}

  double x(double y) const;
};

double Line::x(double y) const
{
  double xx = x1 + ((x2 - x1) / (y2 - y1)) * (y - y1);
  return xx;
}



HorizontalSegment projection (HorizontalSegment ofObstacle,
			      HorizontalSegment fromHouse,
			      double atStreetY)
{
  Line line1 (fromHouse.x1, fromHouse.y, ofObstacle.x2, ofObstacle.y);
  Line line2 (fromHouse.x2, fromHouse.y, ofObstacle.x1, ofObstacle.y);
  return HorizontalSegment(line1.x(atStreetY), line2.x(atStreetY), atStreetY);
}


void removeSegment (SegmentList& street, HorizontalSegment occluded)
{
  SegmentList results;
  for (SegmentList::iterator i = street.begin();
       i != street.end(); i++)
    {
      HorizontalSegment streetSeg = *i;
      if (streetSeg.y != occluded.y)
	// Should not happen in this program
	results.push_back(streetSeg);
      else if (streetSeg.x1 >= occluded.x2 ||
	       streetSeg.x2 <= occluded.x1)
	// segments do not intersect at all
	results.push_back(streetSeg);
      else if (streetSeg.x1 >= occluded.x1
	       && streetSeg.x2 <= occluded.x2)
	// street segment lies entirely within occluded portion
	{}
	// (do nothing)
      else if (occluded.x1 > streetSeg.x1
	       && occluded.x2 < streetSeg.x2)
	{
	  // occluded segment splits the street segment in two
	  results.push_back (HorizontalSegment(streetSeg.x1,
					       occluded.x1, occluded.y));
	  results.push_back (HorizontalSegment(occluded.x2,
					       streetSeg.x2, occluded.y));
	}
      else if (streetSeg.x1 >= occluded.x1 && streetSeg.x1 <= occluded.x2)
	// Occluded segment blocks lower end of street segment
	results.push_back(HorizontalSegment(occluded.x2, streetSeg.x2,
					    occluded.y));
      else if (streetSeg.x2 >= occluded.x1 && streetSeg.x2 <= occluded.x2)
	// Occluded segment blocks higher end of street segment
	results.push_back(HorizontalSegment(streetSeg.x1, occluded.x1,
					    occluded.y));
    }
  street.swap(results);
}


void doHouse(double x1, double x2, double y1)
{
  HorizontalSegment house(x1, x2, y1);

  cin >> x1 >> x2 >> y1;
  SegmentList street;
  street.push_back(HorizontalSegment (x1, x2, y1));
  double streety = y1;

  int nObstacles;
  cin >> nObstacles;
  for (int i = 0; i < nObstacles; i++)
    {
      cin >> x1 >> x2 >> y1;
      HorizontalSegment obstacle(x1, x2, y1);
      if ((y1 - streety) * (y1 - house.y) < 0)
	{ 
	  // obstacle is between house and street
	  HorizontalSegment occluded = projection(obstacle, house, streety);
	  removeSegment(street, occluded);
	}
    }

  if (street.size() > 0)
    {
      SegmentList::iterator maxp = max_element(street.begin(), street.end());
      std::cout.setf(ios::fixed, ios::floatfield);
      std::cout.setf(ios::showpoint);
      std::cout.precision(2);
      double length = maxp->x2 - maxp->x1;
      cout << length << endl;
    }
  else
    cout << "No View" << endl;
}



int main()
{
  double x1, x2, y1;
  cin >> x1 >> x2 >> y1;
  while (x1 != 0.0 || x2 != 0.0 ||y1 != 0.0)
    {
      doHouse(x1, x2, y1);
      cin >> x1 >> x2 >> y1;
    }
  return 0;
}
