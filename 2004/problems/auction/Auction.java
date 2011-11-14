/*
 * Auction.java
 *
 */
import java.util.*;
import java.io.*;
import java.text.*;


public class Auction{
    
    /** Creates a new instance of Auction */
    public Auction() {
    }
    
    public static int ConvertTime(String Time) {
        StringTokenizer tok = new StringTokenizer(Time.trim(),":");
      	int hours = Integer.parseInt(tok.nextToken());
      	int minutes = Integer.parseInt(tok.nextToken());
      	int seconds = Integer.parseInt(tok.nextToken());
        return hours*3600 + minutes*60 + seconds;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        String buff;  // stirng buffer for reads
	StringTokenizer tok; // tokenizer for reads
	BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        
        // get info on items for sale
        HashMap items = new HashMap();
      	int numItems = Integer.parseInt(in.readLine().trim());
        for (int i=0; i < numItems; i++) {  // get all the items
            tok = new StringTokenizer(in.readLine().trim());
            int itemNum = Integer.parseInt(tok.nextToken());
            double minBid = Double.parseDouble(tok.nextToken());
            item thisitem = new item(itemNum,minBid,ConvertTime(tok.nextToken()));
            items.put(new Integer(itemNum),thisitem);
        }
        
        // get info on the bidders
        HashMap bidders = new HashMap();
      	int numbidders = Integer.parseInt(in.readLine().trim());
        for (int i=0; i < numbidders; i++) {  // get all the items
            tok = new StringTokenizer(in.readLine().trim());
            int idnum = Integer.parseInt(tok.nextToken());
            double cash = Double.parseDouble(tok.nextToken());
            bidders.put(new Integer(idnum),new bidder(cash,idnum));
        }
        
        // allocate the bids
      	int numbids = Integer.parseInt(in.readLine().trim());
        for (int i=0;i<numbids;i++) {
            tok = new StringTokenizer(in.readLine().trim());
            int itemnum = Integer.parseInt(tok.nextToken());
            int bidder = Integer.parseInt(tok.nextToken());
            double amount = Double.parseDouble(tok.nextToken());
            int timestamp = ConvertTime(tok.nextToken());
            ((item)items.get(new Integer(itemnum))).addBid(new bid(timestamp,bidder,amount));
        }
       
        // find out who won, and print
        item thisitem = null;
        ArrayList sortItem = new ArrayList();
        for (Iterator iter = items.keySet().iterator(); iter.hasNext(); ) {
           sortItem.add(items.get(iter.next()));
        }
        Collections.sort(sortItem);  // the auctions are resolved in time order 
        for (Iterator it = sortItem.iterator(); it.hasNext(); ) {
            thisitem = (item)it.next();
            Collections.sort( thisitem.bids );
            // note  late bids and min bids already thrown out.
            Collections.sort(thisitem.getBids());
            Iterator bidlist = thisitem.getBids().iterator();
            while (bidlist.hasNext() && thisitem.winner == null ) {
                bid thisbid = (bid)bidlist.next();
                // get the bidder
                bidder abidder;
                abidder = ((bidder)bidders.get(new Integer(thisbid.bidderId)));
                if (abidder.bal >= thisbid.bidamount ) { // i.e. has enough cash
                    abidder.bal -= thisbid.bidamount;
                    thisitem.winner = thisbid;
                }
            }
            if (thisitem.winner != null) {
                NumberFormat nf = NumberFormat.getInstance();
                nf.setMinimumFractionDigits(2);
                nf.setMaximumFractionDigits(2);
                System.out.println("Item "+thisitem.id+" Bidder "+thisitem.winner.bidderId+" Price "+ nf.format(thisitem.winner.bidamount));
            }
            else {
                System.out.println("Item "+thisitem.id+" Reserve not met.");
            }
            
        }
    
    }
    
}


/*
 * bid.java
 *
 */
class bid implements Comparable {
    
    public int timestamp;
    public int bidderId;
    public double bidamount;
    
    
    /** Creates a new instance of bid */
    public bid(int ts,int id,double amount) {
        timestamp = ts;
        bidderId = id;
        bidamount = amount;
    }
    
    
    public boolean equals(Object o) {
      if (!(o instanceof bid)) {
          return false;
      }
      bid b = (bid)o;
      return (((timestamp == b.timestamp)  && (b.bidderId == bidderId)) && (b.bidamount == b.bidamount));
    }
    
    public int hashCode() {
       return timestamp;
    }
    
    public int compareTo(Object o) {
        bid b = (bid)o;
        if (bidamount > b.bidamount) return -1;
        if (bidamount < b.bidamount) return 1;
        // same bid, check timestamps
        if (timestamp > b.timestamp) return -1;
        if (timestamp < b.timestamp ) return 1;
        return 0;
    }
    
}

/*
 * item.java
 *
 */

class item implements Comparable {
    public double minbid;
    public int id;
    public int endtime;
    public bid winner = null;
    public ArrayList bids;    
    
    public ArrayList getBids() {return bids;}
    
    /** Creates a new instance of item */
    public item(int idnum, double min, int time) {
        id = idnum;
        minbid=min;
        endtime = time;
        bids = new ArrayList();
        winner = null;
    }
    
    public boolean equals(Object o) {
      if (!(o instanceof item)) {
          return false;
      }
      item i = (item)o;
      return id == i.id;
    }
    
    public int hashCode() {
       return id;
    }

    public int compareTo(Object o) {
        item i = (item)o;
        return (endtime > i.endtime ? 1  :( endtime < i.endtime ? -1 : 0) );
    }    
    
    public void addBid(bid b) {
// only take bids that meet minimum, and are before the end of auction        
       if ((b.timestamp <= endtime) && (b.bidamount >= minbid)) {  
           bids.add(b);
       }
    }
}


/*
 * bidder.java
 *
 */
class bidder {
    
    public double bal;
    public int id;
        
    /** Creates a new instance of bidder */
    public bidder(double balance, int idnumber) {
        bal = balance;
        id = idnumber;
    }
}
