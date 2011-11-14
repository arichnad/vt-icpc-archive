/*
 * check.java
 *
 * Created on November 1, 2004, 7:00 PM
 */
import java.util.*;
import java.io.*;

/**
 *
 */
public class check {
    
    /** Creates a new instance of check */
    public check() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        
        double StatementStart;
        double StatementEnd;
        double balance;
        
        String buff;  // string buffer for reads
	StringTokenizer tok; // tokenizer for reads
	BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        
        HashMap checks = new HashMap();
        HashMap deposits = new HashMap();
        
        // get initial balance
        tok = new StringTokenizer(in.readLine().trim());
        buff = tok.nextToken();  // ignore "Balance"
        StatementStart = Double.parseDouble(tok.nextToken());
        // get transactions until we get to final balance
        do {
            tok = new StringTokenizer(in.readLine().trim());  // get next line
            buff = tok.nextToken();  // get type
            if (buff.equalsIgnoreCase("BALANCE")) {  // this is the final balance
                StatementEnd = Double.parseDouble(tok.nextToken());  // just get final balance
            }
            else {  // either a check or deposit
               String idnum = tok.nextToken();  // get id #
               double val = Double.parseDouble(tok.nextToken());  // get value
               if (buff.equalsIgnoreCase("CHECK") ) {  // this is a check
                   checks.put(idnum, new entry(val));
               }
               else {  // must be a deposit
                   deposits.put(idnum, new entry(val));
               }
            } 
        } while (!buff.equalsIgnoreCase("BALANCE"));
        // now get register start balance
        balance = Double.parseDouble(in.readLine().trim());
        String thisline = in.readLine(); 
        while (thisline != null) {
            tok = new StringTokenizer(thisline.trim());
            String type = tok.nextToken();
            String idnum = tok.nextToken();
            double val = Double.parseDouble(tok.nextToken());
            double newbalance = Double.parseDouble(in.readLine().trim()); 
            double direction = (type.equalsIgnoreCase("CHECK") ? -1.0 : 1.0);
            String initialMsg = ""+type+" "+idnum;
            String outMsg = initialMsg;
            entry en = (entry)( (direction < 0.0 ) ? checks.get(idnum) : deposits.get(idnum));
            if (en == null) {  // not in the list
               outMsg += " is not in statement";
            }
            else { // mark as touched
                en.examined = true;
            }
            String marker = (String) ((direction < 0.0)  ? checks.get("r"+idnum) : deposits.get("r"+idnum));
            if (marker != null ) {
                outMsg += " repeated transaction";
            }
            if (en != null && en.value != val) {
                outMsg += " incorrect amount";
                if (en != null && newbalance == balance + direction * en.value ) {  // only do this check if amount is incorrect
                    outMsg += " math uses correct value";
                }
            }
            if ((en == null && newbalance != balance+direction*val ) || (en != null && newbalance != balance + direction*en.value && newbalance != balance + direction*val)) {
                outMsg += " math mistake";            
            }
            if (outMsg.equals(initialMsg)) {  // no problems reported
                outMsg += " is correct";
            }
            System.out.println(outMsg);
            // record that we've looked at this one.
            if (direction < 0.0 ) {  // i.e. was a check
                checks.put("r"+idnum,"washere");
            }
            else {  // must be a deposit
                deposits.put("r"+idnum,"washere");
            }
            // set balance for next time
            balance = newbalance;
            thisline = in.readLine(); // get next transaction
        }
        // need to print out unused entries in statement
        for (Iterator ch = checks.keySet().iterator();ch.hasNext();) {
            String thisKey = (String)ch.next();
            if (!thisKey.startsWith("r") && !((entry)checks.get(thisKey)).examined ) {
                System.out.println("missed check "+thisKey);
            }        
        }
        for (Iterator dp = deposits.keySet().iterator();dp.hasNext();) {
            String thisKey = (String)dp.next();
            if (!thisKey.startsWith("r") && !((entry)deposits.get(thisKey)).examined ) {
                System.out.println("missed deposit "+thisKey);
            }        
        }

    }

}
/*
 * entry.java
 *
 * Created on November 1, 2004, 8:18 PM
 */

/**
 *
 */
 class entry {
    public double value;   // value of the the entry 
    public boolean examined;  // has this entry been examined yet?
/** Creates a new instance of entry */
    public entry (double val) {
        value=val;
        examined=false;
    }  
}
