public class overlap {
   public static int overlap(String S, String P) {
      String C = "";
      int[] pi;
        
      C = C.concat(P);
      C = C.concat("$");
      C = C.concat(S);

      pi = computePi(C);
		
      return pi[S.length() + P.length() + 1];
   }
}
