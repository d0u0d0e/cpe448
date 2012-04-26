import java.util.HashMap;

public class BM 
{
   public static int BMrun(String S, String P) 
   {
      int s = 0, j = 0, n = S.length(), m = P.length();
      HashMap<Character, Integer> Rval = computeR(P);
      int[] GS = GoodSuffix(P);

      while(s <= (n - m)) {
      j = m;
         while(j > 0 && P.charAt(j-1) == S.charAt(s+j-1))
            j--;

         if (j == 0) {
            //System.out.println(s);
            s = s + GS[0];
         }
         else {
            try
            {
                s += Math.max(GS[j], j - Rval.get(S.charAt(s+j)));
            }
            catch (Exception e)
            {
            	s += Math.max(GS[j], j);
            }
         }
      }
      return s;
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

   public static HashMap<Character, Integer> computeR(String P) 
   {
      HashMap<Character, Integer> Rval = new HashMap<Character, Integer>();
      
      for(int i = 0; i < P.length(); i++)
    	   Rval.put(new Character(P.charAt(i)), new Integer(i));
      
      return Rval;
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

         if(GS[j] > (l - Rpi[l]))
        	 GS[j] = l - Rpi[l];
      }
      
      return GS;
   }
   
}

