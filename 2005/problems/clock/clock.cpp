/*************************************************************
**                                                          **
**  Sample solution for "Context-Free Clock"                **
**       Steven J Zeil                                      **
**       10/25/2005                                         **
**                                                          **
*************************************************************/


#include <iostream>
#include <iomanip>
#include <algorithm>
#include <cmath>

using namespace std;

static const double halfDay = 12.0 * 60.0 * 60.0;

struct Time {
  long h;
  long m;
  long s;

  Time() : h(0L), m(0L), s(0L) {}

  Time (long h0, long m0, long s0): h(h0), m(m0), s(s0) {}

  Time (long linearT);

  long linearTime() const
    // Express a time as the number of seconds since 12:00:00
  {
    return (h * 60L + m) * 60L + s;
  }

  double angle() const;
};


Time::Time (long linearT)
{
  h = linearT / 3600L;
  linearT -= h * 3600L;
  m = linearT / 60L;
  linearT -= m * 60L;
  s = linearT;
  while (h > 23)
    h -= 24;
}

double Time::angle() const
{
  double hourHandAngle = 360.0 * (double)linearTime() / halfDay;
  double minuteHandAngle = 360.0 * (double)linearTime() / (halfDay/12.0);
  double angleBetween = minuteHandAngle - hourHandAngle;
  while (angleBetween >= 360.0)
    angleBetween -= 360.0;
  while (angleBetween < 0.0)
    angleBetween += 360.0;
  return angleBetween;
}


int main()
{
  double a;
  Time t0;
  char c;

  while ((cin >> a >> t0.h >> c >> t0.m >> c >> t0.s)
	 && a >= 0) {

    /*
      ah (angle of hour hand)
      am (angle of minute hand)
      a(t)  (angle from hour hand to minute hand at time t, 
             t measured in seconds since 12:00:00)

      H: number of seconds in 12 hours = 12 * 60 * 60;
      t: any time of day

      ah = 360*t / h = t / 120;
      am = 12*360*t / h = t/10;

      a(t) = am - ah = t/10 - t/120
                     = 11*t / 120
           [a(t) may be greater than 360]

      a(t) = a + k*360   [a is the measured angle]

      a + k*360 = 11*t / 120
      t = 120*(a + k*360) / 11
    */
    
    // Compute the earliest time of the day in which we could see that
    // angle.
    double ta = 120.0 * a / 11.0;

    // The angle repeats every h/11 seconds. Look for the first repetition
    // that is equal to or after the supplied time.
    long lt0 = t0.linearTime();
    double delta = halfDay/11.0;
    while (ta < (double)lt0)
      ta += delta;

    Time t((long)(ta+0.001));
    cout << setfill('0') << setw(2) << t.h << ":"
      << setw(2) << t.m << ":"
	 << setw(2) << t.s << endl;
    }

  return 0;
}
