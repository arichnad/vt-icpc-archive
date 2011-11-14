import java.io.*;
import java.util.*;

public class auctionsrus {

  static int dAsInt(String num){
    String[] x = num.split("\\.");
    return Integer.parseInt(x[0])*100+Integer.parseInt(x[1]);
  }

  static String intAsD(int x){
    return String.format("%d.%02d", x/100, x%100);
  }

  static int timeAsInt(String num){
    String[] x = num.split(":");
    return Integer.parseInt(x[0])*60*60+Integer.parseInt(x[1])*60+Integer.parseInt(x[2]);
  }
  

  public static void main(String[] argv)
    throws IOException {


        
    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    
    Hashtable<Integer,PriorityQueue<Bid>> itemBids = 
      new Hashtable<Integer,PriorityQueue<Bid>>();

    PriorityQueue<Item> items = new PriorityQueue<Item>();

    Hashtable<Integer,Bidder> bidders = new Hashtable<Integer,Bidder>();

    int numItems = Integer.parseInt(in.readLine().trim());
    for(int i=0;i<numItems;i++){
      String[] fields = in.readLine().split("\\s");
      items.add(new Item(Integer.parseInt(fields[0]), dAsInt(fields[1]), timeAsInt(fields[2])));
      itemBids.put(Integer.parseInt(fields[0]), new PriorityQueue<Bid>());
    }

    int numBidders = Integer.parseInt(in.readLine().trim());
    for(int i=0;i<numBidders;i++){
      String[] fields = in.readLine().split("\\s");
      int n =Integer.parseInt(fields[0]);
      bidders.put(n,new Bidder(n, dAsInt(fields[1])));
    }

    int numBids = Integer.parseInt(in.readLine().trim());
    for(int i=0;i<numBids;i++){
      String[] fields = in.readLine().split("\\s");
      int item =Integer.parseInt(fields[0]);
      int bidder =Integer.parseInt(fields[1]);
      int amount = dAsInt(fields[2]);
      int time = timeAsInt(fields[3]);
      PriorityQueue<Bid> q = itemBids.get(item);
      q.add(new Bid(bidder, amount, time));
    }

    while(items.size()>0){
      Item i = items.poll();
      System.err.println("checking on item "+i);
      if(!findWinningBid(i, itemBids.get(i.n), bidders, -1))
	System.out.println("Item "+i.n+" Reserve not met.");
    }
  }

  static boolean findWinningBid(Item i, PriorityQueue<Bid> q, 
				Hashtable<Integer,Bidder> bidders, int leadingBid){
    if(q.size() == 0)
      return false;

    Bid b = q.poll();

    System.err.println("considering bid "+b);

    if(b.time > i.time)
      return false;

    boolean myBid;

    if(bidders.get(b.bidder).balance >= b.amount &&
       b.amount > leadingBid &&
       b.amount >= i.reserve){
      leadingBid = b.amount;
      myBid = true;
    }
    else
      myBid = false;

    if(findWinningBid(i, q, bidders, leadingBid))
      return true;

    if(myBid){
      System.out.println("Item "+i.n+" Bidder "+b.bidder+" Price "+intAsD(leadingBid));
      bidders.get(b.bidder).balance -= b.amount;
      return true;
    }

    return false;
  }
}

    
      

    

      

    
      
  class Item implements Comparable<Item>{
    int n;
    int time;
    int reserve;

    Item(int n, int reserve, int time){
      this.n = n;
      this.time = time;
      this.reserve = reserve;
    }
    
    public int compareTo(Item o){
      return time - o.time;
    }

    public String toString(){
      return "item "+n+" closes at "+time+" with reserve "+ reserve;
    }

  }

  class Bid implements Comparable<Bid>{
    int bidder;
    int amount;
    int time;

    Bid(int bidder, int amount, int time){
      this.bidder = bidder;
      this.amount = amount;
      this.time = time;
    }

    public int compareTo(Bid o){
      return amount - o.amount;
    }

    public String toString(){
      return "from "+bidder+" for "+amount+" at "+time;
    }
  }

  class Bidder{
    int n;
    int balance;

    Bidder(int n, int balance){
      this.n = n;
      this.balance = balance;
    }
  }

