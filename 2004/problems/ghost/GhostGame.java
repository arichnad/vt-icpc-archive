import java.io.*;
import java.util.*;



public class GhostGame {
  public static void main(String[] argv){
    Scanner in = new Scanner(System.in);
    int nPlayers;
    nextScenario:
    while( (nPlayers = in.nextInt()) > 1){
      in.nextLine(); // skip over newline

      WordTree root = new WordTree();
      root.l = '\0';
      String word = in.nextLine().trim();
      while(word.length() > 0){
	if(word.length()>=4)
	  root.appendWord(word, 0);
	word = in.nextLine().trim();
      }

      String prefix = in.nextLine().trim();
      
      System.out.print(prefix+" ");

      root = root.findPrefix(prefix);

      if(root==null){
	System.out.println("Challenge");
	continue;
      }
      
      
      if(root.endsWord){
	System.out.println("Challenge");
	continue;
      }

      root.setOurLevel(1, nPlayers);

      for(WordTree x : root.next.values()){
	if(x.isValidMove(true,0)){
	  System.out.println(x.l);
	  continue nextScenario;
	}
      }

      System.err.println("POSSIBLE");

      for(WordTree x : root.next.values()){
	if(x.isValidMove(false,0)){
	  System.out.println(x.l);
	  continue nextScenario;
	}
      }

      System.out.println("Bluff");
    }
  }

}

class WordTree {
  char l;

  boolean endsWord=false;
  boolean isOurLevel=false;

  static final String spcs = "                                                                                ";

  Hashtable<Character,WordTree> next = new Hashtable<Character,WordTree>();

  void appendWord(String word, int level){
    if(word.length() == 0){
      if(level >= 4)
	endsWord = true;
    }
    else{
      char x = word.charAt(0);
      WordTree nextNode = next.get(x);
      if(nextNode == null){
	nextNode = new WordTree();
	nextNode.l = x;
	next.put(x, nextNode);
      }
      nextNode.appendWord(word.substring(1), level+1);
    }
  }

  WordTree findPrefix(String p){
    if(p.length() == 0)
      return this;
    WordTree nextNode = next.get(p.charAt(0));
    if(nextNode == null)
      return null;
    return nextNode.findPrefix(p.substring(1));
  }

  void setOurLevel(int thisLevel, int nPlayers){
    if(thisLevel == 0){
      isOurLevel = true;
      thisLevel = nPlayers;
    }
    thisLevel--;

    for(WordTree n : next.values())
      n.setOurLevel(thisLevel, nPlayers);
  }

//   boolean isValidMove(boolean guaranteed){
//     if(endsWord){
//       if(isOurLevel)
// 	// we lose
// 	return false;
//       else
// 	// we win
// 	return true;
//     }
    
//     boolean hasFalseChild = false;
//     boolean hasChildren = false;
//     boolean hasAnyTrueChild = false;

//     for(WordTree n : next.values()){
//       hasChildren = true;
//       if(!n.isValidMove(guaranteed))
// 	hasFalseChild = true;
//       else
// 	hasAnyTrueChild = true;
//     }

//     if(hasChildren)
//       return (guaranteed && !hasFalseChild) ||
// 	(!guaranteed && hasAnyTrueChild);
//     else
//       // short word in dictionary with no descendants
//       return false;
//   }    

  boolean isValidMove(boolean guaranteed, int lev){
    System.err.println(spcs.substring(0,lev)+"visit "+l);

    if(endsWord){
      if(isOurLevel){
	// we lose
	System.err.println(spcs.substring(0,lev)+"LOSE");
	return false;
      }
      else{
	// we win
	System.err.println(spcs.substring(0,lev)+"WIN!");
	return true;
      }
    }
    
    boolean hasFalseChild = false;
    boolean hasChildren = false;
    boolean hasAnyTrueChild = false;

    for(WordTree n : next.values()){
      hasChildren = true;
      if(!n.isValidMove(guaranteed,lev+2)){
	System.err.println(spcs.substring(0,lev)+n.l+":false");

	hasFalseChild = true;
      }
      else{
	System.err.println(spcs.substring(0,lev)+n.l+":true");
	hasAnyTrueChild = true;
      }
    }

    if(hasChildren){
      System.err.println(spcs.substring(0,lev)+"RETURN "+((guaranteed && !hasFalseChild) ||
						     (!guaranteed && hasAnyTrueChild)));
    }
    

    if(hasChildren)
      return (guaranteed && !hasFalseChild) ||
	(!guaranteed && hasAnyTrueChild);
    else{
      // short word in dictionary with no descendants
      System.err.println(spcs.substring(0,lev)+"SHORT FALSE");
      return false;
    }
  }    

}


  
	  

	
      
