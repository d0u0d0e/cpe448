public class overlap {
   public static int overlap(String S, String P, int min) {
      String C = "";
      int[] pi;
      int ol;
        
      C = C.concat(P);
      C = C.concat("$");
      C = C.concat(S);

      pi = computePi(C);

      ol = pi[S.length() + P.length() + 1];

      if(ol >= min)
         return ol;
      else
         return -1;
   }

   public static int[] computePi(String pattern) {
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
}
