import java.io.*;
import java.util.*;
import java.util.regex.Pattern;


public class zonedout {
  
  
  public Pattern wordPattern = Pattern.compile("[a-zA-Z]+");

  public int numBoxes;
  public int numClerks;


  public class Clerk {
    public BitSet addsMarks;
    public BitSet erases;
    public BitSet distributesTo;
    public BitSet logbook;

    public String toString()
      {
	  StringBuffer buf = new StringBuffer();
	  buf.append("clerk adds {");
	  buf.append(bitSetToString(addsMarks));
	  buf.append("}, erases {");
	  buf.append(bitSetToString(erases));
	  buf.append("}, sends to {");
	  buf.append(bitSetToString(distributesTo));
	  buf.append("}");
	  return buf.toString();
      }
  };


  Clerk[] clerks;


  public class Event {
    public int clerkNum;
    public BitSet form;

    public Event (int clerk, BitSet aForm)
    {
      clerkNum = clerk;
      form = aForm;
    }

  }


  BitSet readBitSet (BufferedReader in)
  {
    BitSet b = new BitSet();
    try {
      String line = in.readLine();
      Scanner sc = new Scanner (line);
      while (sc.hasNextInt()) {
        int j = sc.nextInt();
        b.set(j);
      }
    } catch (IOException ex) {System.err.println (ex);}
    return b;
  }


  String bitSetToString (BitSet b)
  {
    boolean firstTime = true;
    StringBuffer buf = new StringBuffer();
    for (int i = 0; i < b.size(); ++i) {
      if (b.get(i)) {
        if (!firstTime) {
          buf.append (' ');
        }
        buf.append ("" + i);
        firstTime = false;
      }
    }
    return buf.toString();
  }



  public void solve (BufferedReader in)
  {
    LinkedList<Event> q = new LinkedList<Event>();
    q.addLast (new Event(0, new BitSet()));

    while (!q.isEmpty()) {
      Event event = q.getFirst();
      q.removeFirst();
      BitSet form = event.form;
      Clerk clerk = clerks[event.clerkNum];
      BitSet nextForm = (BitSet)form.clone();
      if (clerk.logbook != null)
	  nextForm.or (clerk.logbook);
      nextForm.or (clerk.addsMarks);
      nextForm.andNot (clerk.erases);
      if (clerk.logbook == null
	  || !nextForm.equals(clerk.logbook)) {
        clerk.logbook = nextForm;
        for (int i = 0; i < numClerks; ++i) {
          if (clerk.distributesTo.get(i)) {
            q.addLast (new Event(i, nextForm));
          }
        }
        System.err.println("clerk " + event.clerkNum
                           + " sends " + bitSetToString(nextForm));
      }
    }
    System.out.println (bitSetToString(clerks[0].logbook));
  }
  
  
  public  zonedout (BufferedReader in, int nBoxes, int nClerks)
  {
    numClerks = nClerks;
    numBoxes = nBoxes;
    clerks = new Clerk[numClerks];

    for (int i = 0; i < numClerks; ++i)
    {
      clerks[i] = new Clerk();
      clerks[i].addsMarks = readBitSet(in);
      clerks[i].erases = readBitSet(in);
      clerks[i].distributesTo = readBitSet(in);
      clerks[i].logbook = null;
	System.err.println (i + ": " + clerks[i]);
    }
    
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

    try {
      while (ok) {
        ++problemNumber;
        String line = in.readLine();
	System.err.println (line);
        Scanner sc = new Scanner(line);
        int nBoxes = sc.nextInt();
        int nClerks = sc.nextInt();
	System.err.println (nBoxes + ":" + nClerks);
        if (nBoxes > 0 && nClerks > 0) {
          zonedout zone = new zonedout(in, nBoxes, nClerks);
          zone.solve(in);
        } else {
          ok = false;
        }
      }
    } catch (IOException ex) {System.err.println (ex);}
  }
  
}

