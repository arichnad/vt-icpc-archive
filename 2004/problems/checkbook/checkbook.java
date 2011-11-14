
import java.io.*;
import java.util.*;

public class checkbook {

  static int dAsInt(String num){
    String[] x = num.split("\\.");
    return Integer.parseInt(x[0])*100+Integer.parseInt(x[1]);
  }

  public static void main(String[] argv)
    throws IOException{
    int bankStartBalance, bankEndBalance;
    
    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

    String[] field = in.readLine().split("\\s");
    bankStartBalance = dAsInt(field[1]);

    Hashtable<String, Transaction> statement = 
      new Hashtable<String, Transaction>();
    
    field = in.readLine().split("\\s");
    while(!field[0].equals("balance")){
      statement.put(field[0]+"-"+field[1], 
		    new Transaction(field[0], field[1], dAsInt(field[2])));
      field = in.readLine().split("\\s");
    }
    bankEndBalance = dAsInt(field[1]);

    
    try{
      int balance = dAsInt(in.readLine());
      
      while(true){
	field = in.readLine().trim().split("\\s");
	Transaction regTrans = 
	  new Transaction(field[0], field[1], dAsInt(field[2]));
	System.err.println("got in "+regTrans);
	int nextBalance = dAsInt(in.readLine().trim());
	Transaction stateTrans = statement.get(field[0]+"-"+field[1]);
	
	String message = field[0]+" "+field[1];;

	if(stateTrans == null){
	  message += " is not in statement";
	  // this is probably the biggest twist in the problem
	  stateTrans = 
	    new Transaction(field[0], field[1], dAsInt(field[2]));
	  stateTrans.isDummy = true;
	  statement.put(field[0]+"-"+field[1], stateTrans);
	}
	else{
	  if( stateTrans.isDummy )
	    message += " is not in statement";
	}

	if (stateTrans.checked)
	  message += " repeated transaction";
	stateTrans.checked = true;
	if (stateTrans.amount != regTrans.amount && !stateTrans.isDummy){
	  message += " incorrect amount";
	  if(nextBalance == stateTrans.doMath(balance))
	    message += " math uses correct value";
	  else{
	    if(nextBalance != regTrans.doMath(balance))
	      message += " math mistake";
	  }
	}
	else
	  if(nextBalance != regTrans.doMath(balance))
	    message += " math mistake";
	
	if(message.equals(field[0]+" "+field[1]))
	  System.out.println(message+" is correct");
	else
	  System.out.println(message);
	
	balance = nextBalance;


      }
    }
    // ugly, but fast way to exit
    catch(Exception e){ System.err.println("threw "+e);}

    // now check for missing transactions
    for(Transaction t : statement.values()){
      if(!t.checked)
	System.out.println("missed "+
			   (t.type==TranType.CHECK?"check ":"deposit ")+
			   t.number);
    }

  }
	  	  
}
  
    
class Transaction { 
  TranType type;
  int number;
  int amount;
  boolean checked = false;
  boolean isDummy = false; // is fake entry to detect duplicate not in statement

  int doMath(int balance){
    if(type == TranType.CHECK)
      return balance - amount;
    else
      return balance + amount;
  }
  
  Transaction(String tStr, String number, int amount){
    this.type = tStr.equals("check")?TranType.CHECK:TranType.DEPOSIT;
    this.number = Integer.parseInt(number);
    this.amount = amount;
  }
}

enum TranType { CHECK, DEPOSIT }

