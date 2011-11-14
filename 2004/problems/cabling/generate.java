import java.io.*;
import java.util.*;

public class generate {
  static final int XDIM = 15;
  static final int YDIM = 15;
  public static void main (String[] argv){

    System.out.println("100.0");
    System.out.println(XDIM*YDIM);
    for(int x=0;x<XDIM;x++)
      for(int y=0;y<YDIM;y++)
	System.out.println(new Character((char)('A'+x)).toString()+
			   new Character((char)('A'+y)).toString());

    System.out.println(YDIM*(XDIM-1)+XDIM*(YDIM-1));

    for(int x=0;x<XDIM-1;x++)
      for(int y=0;y<YDIM;y++){
	System.out.print(new Character((char)('A'+x)).toString()+
			   new Character((char)('A'+y)).toString()+" "+
			   new Character((char)('A'+x+1)).toString()+
			   new Character((char)('A'+y)).toString()+" ")	  ;
	System.out.printf("%.1f\n", Math.random()*50);
      }

    for(int y=0;y<YDIM-1;y++)
      for(int x=0;x<XDIM;x++){
	System.out.print(new Character((char)('A'+x)).toString()+
			   new Character((char)('A'+y)).toString()+" "+
			   new Character((char)('A'+x)).toString()+
			   new Character((char)('A'+y+1)).toString()+" ")	  ;
	System.out.printf("%.1f\n", Math.random()*50);
      }

  }
}

	
