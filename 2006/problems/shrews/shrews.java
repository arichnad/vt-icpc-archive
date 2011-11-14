import java.io.*;
import java.util.*;
import java.util.regex.Pattern;


public class shrews {
  
  

  public class Shrew implements Comparable {
    public String name;
    public String genes;

    public boolean couldBeDescendedFrom (Shrew mother, Shrew father,
                                  String dominance)
    {
      for (int i = 0; i < dominance.length(); ++i)
      {
        if (dominance.charAt(i) == 'D')
          if (genes.charAt(i) == '1')
          {
            if (mother.genes.charAt(i) == '0' && 
                father.genes.charAt(i) == '0')
              return false;
          }
          else
          {
            if (mother.genes.charAt(i) == '1' || 
                father.genes.charAt(i) == '1')
              return false;
          }
        else
          if (genes.charAt(i) == '1')
          {
            if (mother.genes.charAt(i) == '0' || 
                father.genes.charAt(i) == '0')
              return false;
          }
          else
          {
            if (mother.genes.charAt(i) == '1' && 
                father.genes.charAt(i) == '1')
              return false;
          }
      }
      return true;
    }

    public int compareTo (Object obj)
    {
      Shrew s = (Shrew)obj;
      return name.compareTo(s.name);
    }

  }



  public void solve ()
  {
    Pattern wordPattern = Pattern.compile("[a-zA-Z]+");
    Pattern genesPattern = Pattern.compile("[01]+");
    Scanner sc = new Scanner(System.in);

    String dominance = sc.next(wordPattern);
    Vector<Shrew> females = new Vector<Shrew>();
    Vector<Shrew> males = new Vector<Shrew>();

    while (sc.hasNext(wordPattern)) {
      Shrew shrew = new Shrew();
      shrew.name = sc.next(wordPattern);
      String gender = sc.next(wordPattern);
      shrew.genes =  sc.next(genesPattern);
      if (gender.equals("M"))
        males.add(shrew);
      else
        females.add(shrew);
    }

    Collections.sort (females);
    Collections.sort (males);

    sc.next("\\*\\*\\*");
  
    while (sc.hasNext(wordPattern)) {
      Shrew juvenile = new Shrew();
      juvenile.name = sc.next(wordPattern);
      juvenile.genes =  sc.next(genesPattern);

      boolean firstPair = true;
      System.out.print (juvenile.name + " by ");
      for (int f = 0; f < females.size(); ++f)
        for (int m = 0; m < males.size(); ++m)
        {
          if (juvenile.couldBeDescendedFrom(females.get(f), males.get(m), 
                                            dominance))
	      {
            if (!firstPair)
              System.out.print(" or ");
            firstPair = false;
            System.out.print (females.get(f).name
                              + "-" + males.get(m).name);
	      }
	  }
      System.out.println();
    }

  }

  public static void main (String[] argv)
  {
    shrews s = new shrews();
    s.solve();
  }

}

