import java.io.*;
import java.util.*;

public class HubDriving {
  
  public static void main(String[] argv){
    Scanner in = new Scanner(System.in);
    int nCases = in.nextInt();
    for(int i=0;i<nCases;i++)
      new HubDriving(in);
  }

  int n;

  HubDriving(Scanner in){
    n = in.nextInt();
    int m = in.nextInt();

    Node[] nodes = new Node[n];

    for(int i=0;i<n;i++)
      nodes[i] = new Node(i);
    for(int i=0;i<m;i++){
      int a = in.nextInt()-1;
      int b = in.nextInt()-1;
      int d = in.nextInt();
      new Edge(nodes[a], nodes[b], d);
    }

    int[][] pathcost = new int[n][];
    
    for(int i=0;i<n;i++)
      pathcost[i] = getShortestPaths(i, nodes);
      

    int minCost=Integer.MAX_VALUE, minA=0, minB=0;

    for(int a=0;a<n;a++)
      for(int b=a+1;b<n;b++){
	int pairCost = calcCosts(nodes, pathcost[a], pathcost[b],
				 a, b, false);
	if(pairCost < minCost){
	  minCost = pairCost;
	  minA= a;
	  minB= b;
	}
      }
     
    calcCosts(nodes, pathcost[minA], pathcost[minB], minA, minB, true);
  }

  /** 
      algorithm:

      - each node is assigned to one of two hubs, A & B
      - the cost from node to hub is charged for each trip
        for a total of 2 * (N-1) * costToHub
      - for both hubs, this is
        2(N-1)(\sum_A costToA(i) + \sum_B costToB(i))
      - each node on the other side of A-B crosses that link
        twice for each node on the first side, so add in
	2 AB |B| (N-|B|)
      - because we are minimizing a simple sum, for each given 
        |B|, the optimization of which nodes are in B can be
	greedy
      - the optimization is made based on how much is saved moving from
        A to B
  */
      
  int calcCosts(Node[] nodes, int[] aCosts, int[] bCosts, 
		int a, int b, boolean printing){

    //System.err.println("hubs "+a+" "+b);

    for(int i=0;i<n;i++){
      if(i==a || i==b)
	nodes[i].cost = Integer.MIN_VALUE/2;
      else
	nodes[i].cost = aCosts[i]-bCosts[i];
    }
    // shallow copy so we can sort
    Node[] swapOrder = nodes.clone();
    Arrays.sort(swapOrder);

    for(int i=0;i<n;i++){
      //System.err.println("   swap "+swapOrder[i]+" saves "+swapOrder[i].cost);
    }

    int AB = aCosts[b];
    // initialize with no nodes in B
    int backboneCost = 2*AB*1*(n-1);
    int edgeCost = 0;
    for(int i=0;i<nodes.length;i++)
      if(i!=a && i!=b)
	edgeCost+=2*(n-1)*aCosts[i];
    
    int minCost = edgeCost + backboneCost;
    int minSwapCount = 0;

    //System.err.println(" swap=0 cost "+minCost);

    for(int swap=1;swap<=n-2;swap++){
      backboneCost = 2*AB*(swap+1)*(n-(swap+1));
      edgeCost-=2*(n-1)*swapOrder[n-swap].cost;

      int newCost = backboneCost + edgeCost;
      //System.err.println(" swap="+swap+" cost "+newCost);

      if(newCost < minCost){
	minCost = newCost;
	minSwapCount = swap;
      }
    }

    if(printing){
      int[] assign = new int[n];
      for(int i=0;i<n;i++)
	assign[i] = a+1;
      assign[a] = 0;
      assign[b] = 0;

      //System.err.println("printing\na="+a);
      //System.err.println("b="+b);

      for(int i=1;i<=minSwapCount;i++){
	assign[swapOrder[n-i].name] = b+1;
	//System.err.println(swapOrder[n-i].name+" to b");
      }
      
      for(int i=0;i<n;i++)
	System.out.print(assign[i]+" ");
      System.out.println();
    }

    return minCost;
  }
    
   
  int[] getShortestPaths(int source, Node[] nodes){
    int[] pathcost = new int[n];

    for(int i=0;i<n;i++)
      nodes[i].cost = Integer.MAX_VALUE;;

    TreeSet<Node> costs = new TreeSet<Node>();
    nodes[source].cost = 0;
    costs.add(nodes[source]);
    
    while(costs.size()>0){
      Node n = costs.first();
      costs.remove(n);
      
      pathcost[n.name] = n.cost;

      for(Edge e : n.edges){
	Node other = e.a==n?e.b:e.a;
	if(n.cost+e.weight < other.cost){
	  costs.remove(other);
	  other.cost = n.cost+e.weight;
	  costs.add(other);
	}
      }
    }

    return pathcost;
  }


    
      
    
}

class Node implements Comparable<Node>{
  Integer name;
  int cost;
  HashSet<Edge> edges = new HashSet<Edge>();

  Node(Integer name){
    this.name = name;
  }

  // consistent with equals for TreeSet
  public int compareTo(Node o){
    if(cost!=o.cost)
      return cost - o.cost;
    else
      return name.compareTo(o.name);
  }

  public String toString(){
    return "Node:"+name;
  }

  public int hashCode(){
    return name.hashCode();
  }
}

class Edge implements Comparable<Edge> {
  Node a;
  Node b;
  int weight;

  Edge(Node a, Node b, int weight){
    this.a = a;
    this.b = b;
    this.weight = weight;
    a.edges.add(this);
    b.edges.add(this);
  }

  public int compareTo(Edge o){
    return weight - o.weight;
  }

  public String toString(){
    return a.name+"--"+b.name+"("+weight+")";
  }
}
