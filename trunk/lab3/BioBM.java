import java.util.HashMap;

public class BioBM {

   public static int BMrun(String S, String P) {
      int s = 0, j = 0, n = S.length(), m = P.length();
      int[] GS = GoodSuffix(P);

      j = m;
      while(s <= (n - m)) {
         //System.out.println("s: " + s + ", j: " + j);
         while(j > 0 && P.charAt(j-1) == S.charAt(s+j-1))
       	    j--;

         if (j == 0) {
            return s;
         }
	      else {
	         //s = GS[j];
            s++;
	      }
      }
      //System.out.println("Not Found? s: " +s);
      return -1;
   }

   
   static public int[] computePi(String pattern) {
      int[] pi = new int[pattern.length()+1];
      int nextChr, pfxLen;
      pfxLen = 0; // Prefix we've matched against ourselves so far
      for (nextChr = 1; nextChr < pattern.length(); nextChr++) {
         while (pattern.charAt(pfxLen) != pattern.charAt(nextChr) && pfxLen > 0)
            pfxLen = pi[pfxLen];
            
         if (pattern.charAt(pfxLen) == pattern.charAt(nextChr))
            pfxLen++;

         pi[nextChr+1] = pfxLen; // nextChr+1 is match count
      }

      return pi;
   }

   public static int[] GoodSuffix(String P) {
      int[] GS = new int[P.length() + 1], pi, Rpi;
      StringBuffer revP = new StringBuffer(P);
      
      pi = computePi(P); 

      revP = revP.reverse();
      Rpi = computePi(revP.toString());

      for(int j = 0; j < P.length() + 1; j++)
    	  GS[j] = P.length() - pi[P.length()];

      int j;
      for(int l = 1; l <= P.length(); l++) {
         j = P.length() - Rpi[l];

         if(GS[j] > l - Rpi[l])
        	 GS[j] = l - Rpi[l];
      }
      
      return GS;
   }
   
}

