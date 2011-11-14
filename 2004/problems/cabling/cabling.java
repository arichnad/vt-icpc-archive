import java.io.*;
import java.util.*;

public class cabling {
  static double totalLength = 0;
  static PriorityQueue<Edge> edgeHeap = new PriorityQueue<Edge>();
  static Hashtable<String, Node> houses = new Hashtable<String, Node>();

  public static void main(String[] argv)
  throws IOException{
    
    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    
    double cableLength = Double.parseDouble(in.readLine().trim());

    int nHouses = Integer.parseInt(in.readLine().trim());
    for(int i=0;i<nHouses;i++){
      String houseName = in.readLine().trim();
      houses.put(houseName, new Node(houseName));
    }
    int nRoads = Integer.parseInt(in.readLine().trim());
    double minWeight = Double.MAX_VALUE;
    Edge minEdge=null;
    for(int i=0;i<nRoads;i++){
      String[] fields = in.readLine().split("\\s");
      double weight = Double.parseDouble(fields[2]);
      Node a = houses.get(fields[0]);
      Node b = houses.get(fields[1]);
      Edge e = new Edge(a, b, weight);
      if (weight < minWeight){
	minWeight = weight;
	minEdge = e;
      }
      a.edges.add(e);
      b.edges.add(e);
    }

    System.err.println("houses is "+houses);

    addEdge(minEdge);

    while(houses.size() > 0){
      Edge e = edgeHeap.remove();
      if(houses.containsKey(e.a.name) || houses.containsKey(e.b.name))
	addEdge(e);
    }
    
    if (totalLength > cableLength)
      System.out.println("Not enough cable");
    else
      System.out.printf("Need %.1f miles of cable\n", totalLength);
  }

  static void addEdge(Edge e){
    System.err.println("addedge "+e.a.name+"-"+e.b.name+" "+e.weight);
    totalLength+=e.weight;
    if(houses.containsKey(e.a.name)){
      System.err.println("reached node "+e.a.name);
      for(Edge i:e.a.edges)
	edgeHeap.add(i);
      houses.remove(e.a.name);
    }
    if(houses.containsKey(e.b.name)){
      System.err.println("reached node "+e.b.name);
      for(Edge i:e.b.edges)
	edgeHeap.add(i);
      houses.remove(e.b.name);
    }

  }


}

class Node {
  String name;
  boolean isIn = false;
  HashSet<Edge> edges = new HashSet<Edge>();
  Node(String name){
    this.name = name;
  }
  public String toString(){
    return "Node:"+name;
  }
}

class Edge implements Comparable<Edge> {
  Node a;
  Node b;
  Double weight;

  Edge(Node a, Node b, double weight){
    this.a = a;
    this.b = b;
    this.weight = weight;
  }

  public int compareTo(Edge o){
    return weight.compareTo(o.weight);
  }

  public String toString(){
    return a.name+"--"+b.name+"("+weight+")";
  }
}

  
