/*************************************************************
**                                                          **
**  Sample solution for "Extrusion"                         **
**       Steven J Zeil                                      **
**       10/19/2005                                         **
**                                                          **
*************************************************************/


#include <iostream>
#include <iomanip>
#include <vector>
#include <algorithm>


using namespace std;




struct Point {
  double x;
  double y;

  Point (double xx, double yy) : x(xx), y(yy) {}
};



int main()
{
  int N;
  while ((cin >> N) && N >= 3)
    {
      // read the vertices
      vector<Point> vertices;
      double x, y;
      for (int i = 0; i < N; ++i)
	{
	  cin >> x >> y;
	  vertices.push_back (Point(x,y));
	}
      vertices.push_back (vertices[0]);


      // Compute the cross-section area
      //   Possible approaches:
      //     1) the calculation below (which is easily derived from 
      //                               Green's theorem)
      //     2) apply Green's theorem directly (Green's theorem relates
      //            the area inside a curve to an integral taken along the
      //            perimeter of the curve)
      //     3) Decompose the area into two lists of triangles - 
      //           A) a list of trangles that, if added to the polygon,
      //                 would make it convex
      //           B) a list of trangles making up the interior of 
      //                 the convex polygon
      //        Then subtract the sum of the areas in list A from the sum
      //          of the areas of triangles in list B
      double area = 0.0;
      for (int i = 0; i < N; ++i)
	{
	  int j = i + 1;
	  area += vertices[i].x * vertices[j].y 
                - vertices[j].x * vertices[i].y;
	}
      area = area / 2.0;
      area = (area < 0.0) ? -area : area;

      // Compute the bar length
      double metalVolume;
      cin >> metalVolume;
      double length = metalVolume / area;

      cout << "BAR LENGTH: " << fixed << setprecision(2) << length << endl;
    }
  return 0;
}
