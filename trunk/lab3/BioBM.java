import java.util.HashMap;

public class BioBM 
{
   public static int BMrun(String S, String P) {
      int s, j = 0, n = S.length(), m = P.length();
      int[] GS;

      if(n > m)
    	  s = n - m;
      else
    	  s = 0;
      	      
      while(s+j < n ) {
         j = 0;
         GS = GoodSuffix(P);

      try {
         while(j < P.length() && P.charAt(j) == S.charAt(s+j)) {
       	    System.out.printf("P = %c, S = %c\n", P.charAt(j), S.charAt(s+j));
            j++;
         }
      }
      catch (Exception e) {
      }
	      
         if (s+j == n) {
            return s;
         }
         else {
            s++;
            P = P.substring(0, P.length() - 1);
          //   s += Math.max(GS[j], j);
          // P = P.substring(0, P.length() - Math.max(GS[j], j));
         }
      }
      return -1;
   }

/*
   public static int BMrun(String S, String P) 
   {
      int s = 0, j = 0, n = S.length(), m = P.length();
      int[] GS = GoodSuffix(P);


      while(s <= (n - m)) {
         j = m;
         while(j > 0 && nEqual(P.charAt(j-1), S.charAt(s+j-1)))
       	    j--;

         if (j == 0) {
            return s;
         }
	      else {
	         s += GS[j];
	      }
      }
      return -1;
   }
*/
   static public boolean nEqual(char a, char b)
   {
      if (a == b || a == 'N' || b == 'N')
      {
         return true;
      }

      return false;
   }
   
   static public int[] computePi(String pattern) 
   {
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

   public static int[] GoodSuffix(String P) 
   {
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

