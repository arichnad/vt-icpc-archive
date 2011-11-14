import java.io.*;
import java.util.*;
import java.util.regex.Pattern;


public class crosswords {
  
  
  public class Locations {
    int line;
    int column;
    int length;
    boolean vertical;

    public String toString () {
      return "[(" + line + "," + column + ")@" 
        + length + " " + vertical + "]";
    }
  };
  
  
  public class Placement {
    Locations loc;
    int suppressed;
    
    Placement (Locations locat)
    {
      loc = locat;
      suppressed = 0;
    }

    public String toString () {
      return loc.toString();
    }
  }
  
  
  public class Word {
    String value;
    Vector<Placement> possible;
    int placedAt;
    int numPossible;
    
    public Word() {
      possible = new Vector<Placement>();
    }
    
    public String toString() {
      String s = value + " " + placedAt + " " + numPossible;
      if (placedAt >= 0)
        s = s + " " + possible.get(placedAt);
      return s;
    }
  }
  
  
  public class Template implements Cloneable {
    char[][] chars;
    int width;
    int height;
    
    Template (int w, int h)
    {
      chars = new char[h][w];
      width = w;
      height = h;
    }
    
    public String toString()
    {
      StringBuffer sb = new StringBuffer();
      for (int y = 0; y < height; ++y) {
        for (int x = 0; x < width; ++x) {
          sb.append(chars[y][x]);
        }
        sb.append("\n");
      }
      return sb.toString();
    }
    
    public Object clone() {
      Template r = new Template(width,height);
      for (int y = 0; y < height; ++y) {
        for (int x = 0; x < width; ++x) {
          r.chars[y][x] = chars[y][x];
        }
      }
      return r;
    }
  }
  
  
  public int problemNumber;
  public Scanner inScan;
  public Vector<Locations> locations;
  public int nWords;
  public int nLines;
  public Word[] problem;
  public Template startingLayout;
  
  
  public boolean legal(Locations loc, Template layout, String word)
  // true if it is legal to insert this word at the indicated location
  // given the assignments already reflected in the layout
  {
    boolean OK = true;
    if (loc.vertical) {
      for (int i = 0; OK && i < loc.length; ++i) {
        char c = layout.chars[loc.line+i][loc.column];
        OK = (c == '.') || (c == word.charAt(i));
      }
    } else {
      for (int i = 0; OK && i < loc.length; ++i) {
        char c = layout.chars[loc.line][loc.column+i];
        OK = (c == '.') || (c == word.charAt(i));
      }
    }
    return OK;
  }
  
  
  
  public void place (Locations loc, Template layout, String word)
  // Update the layout by placing the word at the indicated location
  {
    if (loc.vertical) {
      for (int i = 0; i < loc.length; ++i) {
        layout.chars[loc.line+i][loc.column] = word.charAt(i);
      }
    } else {
      for (int i = 0; i < loc.length; ++i) {
        layout.chars[loc.line][loc.column+i] = word.charAt(i);
      }
    }
  }
  
  
  
  public void trimPossibilities (Template layout, 
                                 int numAlreadyPlaced)
  {
    // For all unassigned words, suppress any possible locations
    // that would conflict with the latest change
    for (int i = numAlreadyPlaced; i < problem.length; ++i) {
      Word word = problem[i];
      for (int k = 0; k < word.possible.size(); ++k) {
        if (word.possible.get(k).suppressed <= 0
            && ! legal(word.possible.get(k).loc, layout, word.value)) {
          word.possible.get(k).suppressed = numAlreadyPlaced;
          --word.numPossible;
        }
      }
    }
  }
  
  
  public void restorePossibilities (int numAlreadyPlaced)
  {
    // For all unassigned words, restore any possible locations
    // that were suppressed at this same level of the solution.
    for (int i = numAlreadyPlaced; i < problem.length; ++i) {
      Word word = problem[i];
      for (int k = 0; k < word.possible.size(); ++k) {
        if (word.possible.get(k).suppressed == numAlreadyPlaced) {
          word.possible.get(k).suppressed = 0;
          ++word.numPossible;
        }
      }
    }
  }
  
  
  void swap (Object[] a, int i, int j)
  {
    Object temp = a[i];
    a[i] = a[j];
    a[j] = temp;
  }
  
  
  
  public boolean solve (int numAlreadyPlaced,
                        Template layout)
  {
    boolean solved = false;
    if (numAlreadyPlaced < problem.length)
    {
      // Try to place another word somewhere
      
      // Find the word with the fewest possible positions remaining.
      //  With luck, we will often find words that "have" to 
      //  be placed in a specific location.
      int fewestPossible = problem[numAlreadyPlaced].numPossible;
      int bestWordIndex = numAlreadyPlaced;
      for (int i = numAlreadyPlaced+1; i < problem.length; ++i)
        if (problem[i].numPossible < fewestPossible) {
          fewestPossible = problem[i].numPossible;
          bestWordIndex = i;
        }
      swap (problem, numAlreadyPlaced, bestWordIndex);
      
      // Try each possibility in turn
      Word word = problem[numAlreadyPlaced];
      
      for (int k = 0; (!solved) && k < word.possible.size(); ++k) {
        if (word.possible.get(k).suppressed <= 0
            && legal(word.possible.get(k).loc, 
                     layout, word.value)) {
          Template newLayout = (Template)layout.clone();
          place(word.possible.get(k).loc, newLayout, word.value);

          ++numAlreadyPlaced;
          word.placedAt = k;

          // System.err.println (word);
          // System.err.println (newLayout);


          trimPossibilities (newLayout, numAlreadyPlaced);
          
          solved = solve (numAlreadyPlaced, newLayout);
          
          if (!solved) {
            word.placedAt = -1;
            restorePossibilities (numAlreadyPlaced);
            --numAlreadyPlaced;
          }
        }
      }
    } else {
      // All words have been assigned. 
      // Have we filled in all the '.'s?
      solved = true;
      for (int y = 0; solved && y < layout.height; ++y)
        for (int x = 0; solved && x < layout.width; ++x)
          solved = layout.chars[y][x] != '.';
      if (solved)
        System.out.print ("\n" + layout);
    }
    return solved;
  }
  
  
  
  public void solve ()
  {
    boolean solved = false;
    System.out.print ("Problem " + problemNumber);
    solved = solve (0, startingLayout);
    if (!solved)     {
      System.out.println(": No layout is possible.");
    }
  }
  
  
  public  crosswords(int nWordsc, int nLinesc, 
                     int problemNumberc, Scanner inScanc)
  {
    nWords = nWordsc;
    nLines = nLinesc;
    problemNumber = problemNumberc;
    inScan = inScanc;
    
    // 1. Read everything in
    
    Vector<String> words = new Vector<String>();
    Pattern wordPattern = Pattern.compile("[a-zA-Z]+");
    for (int i = 0; i < nWords; ++i) {
      String w = inScan.next(wordPattern);
      words.add(w);
    }
    
    inScan.nextLine(); // throw away rest of this line
    
    String line = inScan.nextLine();
    startingLayout = new Template(line.length(), nLines);
    
    for (int y = 0; y < nLines; ++y) {
      if (y > 0)
        line = inScan.nextLine();
      for (int x = 0; x < startingLayout.width; ++x)
        startingLayout.chars[y][x] = line.charAt(x);
    }
    
    
    // 2. Analyze lines to determine possible locations for words
    locations = new Vector<Locations>();
    for (int i = 0; i < nLines; ++i)
      for (int j = 0; j < startingLayout.width; ++j) {
        if (startingLayout.chars[i][j] == '.' &&
            (j == 0 || startingLayout.chars[i][j-1] == '#')) {
          // a horizontal word goes here
          Locations loc = new Locations();
          loc.line = i;
          loc.column = j;
          loc.vertical = false;
          loc.length = 1;
          while (j + loc.length < startingLayout.width
                 && startingLayout.chars[i][j+loc.length] == '.')
            ++loc.length;
          if (loc.length > 1)
            locations.add (loc);
        }
        if (startingLayout.chars[i][j] == '.' &&
            (i== 0 || startingLayout.chars[i-1][j] == '#')) {
          // a vertical word goes here
          Locations loc = new Locations();
          loc.line = i;
          loc.column = j;
          loc.vertical = true;
          loc.length = 1;
          while (i + loc.length < nLines
                 && startingLayout.chars[i+loc.length][j] == '.')
            ++loc.length;
          if (loc.length > 1)
            locations.add (loc);
        }
      }
    
    
    // 3. Associate with each word a list of possible placements within
    //    the puzzle
    problem = new Word[nWords];
    for (int i = 0; i < nWords; ++i) {
      Word word = new Word();
      word.value = words.get(i);
      word.placedAt = -1;
      word.numPossible = 0;
      int wlen = word.value.length();
      for (int j = 0; j < locations.size(); ++j)
      {
        if (locations.get(j).length == wlen) {
          word.possible.add
            (new Placement(locations.get(j)));
          ++word.numPossible;
        }
      }
      problem[i] = word;
      // System.err.println (word);
    }
  }
  
  
  public static void main (String[] argv)
  {
    int problemNumber = 0;
    int nWords, nLines;
    
    Scanner inScan = new Scanner(System.in);
    
    nWords = inScan.nextInt();
    nLines = inScan.nextInt();
    while (nWords > 0 && nLines > 0) {
      ++problemNumber;
      crosswords cw = new crosswords(nWords, nLines, 
                                     problemNumber, inScan);
      cw.solve();
      
      nWords = inScan.nextInt();
      nLines = inScan.nextInt();
    }
  }
  
}
