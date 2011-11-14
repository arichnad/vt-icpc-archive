/*
 * ghost.java
 *
 * Created on November 6, 2004, 7:05 PM
 */

import java.io.*;
import java.util.*;

/**
 *
 * @author  Cary
 */
public class ghost {
    
    public static void main(String[] args) throws Exception {
        String buff;  // stirng buffer for reads
	StringTokenizer tok; // tokenizer for reads
	BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        int NumPlayers;
        treeNode thisTree;

        NumPlayers = Integer.parseInt(in.readLine().trim());
        while (NumPlayers >= 2 ) {    // loop through all data sets
            thisTree = new treeNode("");
            buff = in.readLine().trim().toLowerCase();
            while (buff.length()> 0) {
                if (buff.length()>3) {  // possible valid word
                    thisTree.addString(buff);
                }
                buff = in.readLine().trim().toLowerCase();  // get the next one
            }
            String initial = in.readLine().trim().toLowerCase();  // get the word so far
            System.out.print(initial);
            treeNode subTree = thisTree.getSubTree(initial);  // only things that start with the initial String are interesting.
            if (subTree == null || subTree.getIsWord()) { System.out.println(" Challenge");}  // already a word or not a word
            else {
                HashMap options = new HashMap();
                for (Iterator it = subTree.Extensions.keySet().iterator(); it.hasNext(); ) {
                   String option = (String)it.next();
                   options.put( (new Integer( ((treeNode)subTree.Extensions.get(option)).nodeEval(NumPlayers,0) ) ),option);
                }
                if (options.containsKey(new Integer(1))) {  // has a force win
                    System.out.println(" "+((String)options.get(new Integer(1))));
                }
                else  if (options.containsKey(new Integer(0))) {  // has an indeterminate play
                    System.out.println(" "+((String)options.get(new Integer(0))));
                }
                else {  // is gonna lose, better fake it
                    System.out.println(" Bluff");
                }
            }            
            NumPlayers = Integer.parseInt(in.readLine().trim());
        }
    }
}


class treeNode {
    
    public boolean isword;
    public String base;
    public HashMap Extensions;
    
    public void setIsWord(boolean word) {isword = word;}
    public void setBase(String baseString) {base = baseString;}
    public boolean getIsWord() {return isword;}
    public String getBase() {return base;}
    
    public treeNode(String baseStr) {
        setBase(baseStr);
        Extensions = new HashMap();
    }
    
    // -1 play is bad 0 = mixed 1 = forces other player loss
    public int nodeEval(int numPlayers, int turnNum) {
        int sum = 0;
        int targetsize = Extensions.size();
        if (isword ) { 
            if (turnNum == 0 ) {  // ends as a word on players turn . . . bad choice
                sum -= 1;
            }
            else {
                sum += 1;
            }
            return sum;
        }
        for (Iterator it = Extensions.keySet().iterator(); it.hasNext();) {
            treeNode node = (treeNode)Extensions.get(it.next());
            sum += node.nodeEval(numPlayers, ((turnNum+1) % numPlayers));
        }
        if (targetsize == sum) {return 1; }  // all force other player loss
        if (targetsize == -sum) {return -1;}  // all force player loss 
        return 0;
    }

    // returns the subtree that starts with the string toMatch, or null if no such tree exists
    public treeNode getSubTree(String toMatch) {
        if (base.equals(toMatch)) {
            return this;
        }
        else {
            String theChar = String.valueOf(toMatch.charAt(base.length()));
            treeNode nextNode = (treeNode)Extensions.get(theChar);
            if (nextNode == null) { return null; }
            return nextNode.getSubTree(toMatch);
        }
    }
    
    // go recursively down the tree to add the new string.
    public void addString(String toAdd) {
        if (toAdd.equals(base)) {  // if the entire string is in the base
            isword = true;  // its a word 
            return;  // we're done
        }
        String theChar = String.valueOf(toAdd.charAt(base.length()));
        treeNode newNode = (treeNode)Extensions.get(theChar);
        if ( newNode == null ){
            newNode = new treeNode(toAdd.substring(0,base.length()+1) );
            Extensions.put(theChar, newNode);
        }
        newNode.addString(toAdd);
        return;
    }
}




