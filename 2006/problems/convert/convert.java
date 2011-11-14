import java.io.*;
import java.util.*;
import java.util.regex.Pattern;


public class convert {
  
  
  public boolean emptyProblem;
  public Pattern wordPattern = Pattern.compile("[a-zA-Z]+");

  public class MeasurementSystem {

    public MeasurementSystem(BufferedReader in)
    {
      try {
        String line = in.readLine();
        emptyProblem = true;
        if (line.length() > 0) {
          emptyProblem = false;
          unitNames = line.split(" +");
          conversion = new double[unitNames.length][unitNames.length];
          for (int i = 0; i < unitNames.length; ++i) {
            for (int j = 0; j < unitNames.length; ++j) {
              conversion[i][j] = -1.0;
            }
            conversion[i][i] = 1.0;
          }
          for (int i = 0; i < unitNames.length-1; ++i) {
            line = in.readLine();
            Scanner sc = new Scanner(line);
            double d1 = sc.nextDouble ();
            String u1 = sc.next (wordPattern);
            String u2 = sc.next ("=");
            double d2 = sc.nextDouble ();
            u2 = sc.next (wordPattern);

            int i1 = findUnit(u1);
            int i2 = findUnit(u2);
            conversion[i1][i2] = d2 / d1;
            conversion[i2][i1] = d1 / d2;
          }

          // Fill in the remaining parts of the conversion matrix
          for (int pass = 0; pass < unitNames.length; ++pass) {
            for (int i = 0; i < unitNames.length; ++i) {
              for (int j = 0; j < unitNames.length; ++j) {
                if (conversion[i][j] < 0.0) {
                  for (int k = 0; k < unitNames.length; ++k) {
                    if (k != i && k != j
                        && conversion[i][k] >= 0.0
                        && conversion[k][j] >= 0.0) {
                      conversion[i][j] = 
                        conversion[i][k] * conversion[k][j];
                    }
                  }
                }
              }
            }
          }

          for (int i = 0; i < unitNames.length; ++i) {
            System.err.print (unitNames[i] + " ");
          }
          System.err.println();
          for (int j = 0; j < unitNames.length; ++j) {
            for (int i = 0; i < unitNames.length; ++i) {
              System.err.print (conversion[i][j] + " ");
            }
            System.err.println();
          }

        }
      } catch (Exception ex) {}
    }


    double reduceToLowest(double[] measurement)
    {
      double sum = 0.0;
      for (int i = 0; i < unitNames.length; ++i) {
        sum += measurement[i] * conversion[i][unitNames.length-1];
      }
      return sum;
    }


    int[] normalize(double measure)
    {
      int[] measurement = new int[unitNames.length];
      for (int i = 0; i < unitNames.length-1; ++i) {
        measurement[i] = 
          (int) (measure / conversion[unitNames.length-1][i]);
        measure -= measurement[i] * conversion[unitNames.length-1][i];
      }
      measurement[unitNames.length-1] = (int)(measure + 0.5);
      return measurement;
    }


    int findUnit (String unitname)
    {
      int pos = -1;
      for (int i = 0; pos < 0 && i < unitNames.length; ++i)
        if (unitname.equals(unitNames[i]))
          pos = i;
      return pos;
    }


    String[] unitNames;
    
    double[][] conversion;
  }  


  MeasurementSystem system1;
  MeasurementSystem system2;
  double conversion;

  public void solve (BufferedReader in)
  {
      try {
        String line = in.readLine();
        while (line.length() > 0) {
          double[] measurement = new double[system1.unitNames.length];
          Arrays.fill (measurement, 0.0);
          Scanner sc = new Scanner(line);
          while (sc.hasNextDouble()) {
            double d1 = sc.nextDouble ();
            String u1 = sc.next (wordPattern);
            int i1 = system1.findUnit(u1);
            measurement[i1] = d1;
          }
          double measure = system1.reduceToLowest(measurement);
          int[] converted = system2.normalize(measure);
          
          for (int i = 0; i < converted.length; ++i) {
            if (i > 0)
              System.out.print (" ");
            System.out.print (converted[i] + " " + system2.unitNames[i]);
          }
          System.out.println();

          line = in.readLine();
        }
      } catch (Exception ex) {}
  }
  
  
  public  convert (BufferedReader in)
  {
    system2 = null;
    system1 = new MeasurementSystem(in);
    if (!emptyProblem)
      system2 = new MeasurementSystem(in);

    try {
      String line = in.readLine();

      Scanner sc = new Scanner(line);
      double d1 = sc.nextDouble ();
      String u1 = sc.next (wordPattern);
      String u2 = sc.next ("=");
      double d2 = sc.nextDouble ();
      u2 = sc.next (wordPattern);
      
      int i1 = system1.findUnit(u1);
      int i2 = system2.findUnit(u2);
      
      d1 = d1 * system1.conversion[i1][system1.unitNames.length-1];
      d2 = d2 * system1.conversion[i2][system2.unitNames.length-1];
      conversion = d2 / d1;
    } catch (Exception ex) {}
  }
  
  
  public static void main (String[] argv)
  {
    BufferedReader in = null;
    if (argv.length > 0) {
      // Command line option for easier debugging
      System.err.println ("Loading data from " + argv[0]);
      try {
        in = new BufferedReader (new FileReader (argv[0]));
      } catch (IOException ex) {
        System.err.println ("Problem opening " + argv[0] + ":\n" + ex);
        System.exit(1);
      }
    } else {
      in = new BufferedReader
          (new InputStreamReader(System.in));
    }


    int problemNumber = 0;

    boolean ok = true;

    while (ok) {
      ++problemNumber;
      convert cnv = new convert(in);
      if (cnv.emptyProblem) {
        ok = false;
      } else {
        cnv.solve(in);
      }
    }
  }
  
}

