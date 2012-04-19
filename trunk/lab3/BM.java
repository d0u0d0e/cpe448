import java.util.HashMap;

public class BM {

  public static void BMrun(String S, String P) {
       int s = 0, j, n = S.length(), m = P.length();
       HashMap<Character, Integer> Rval = computeR(P);
       int[] GS = GoodSuffix(P);
       
       j = m - 1;
       while(s <= (n - m)) {          
          while(j > 0 && P.charAt(j) == S.charAt(s+j)) {
              j -= 1;
   
              if(j == 0) {
                  System.out.println(s);
                  s = s + GS[0];
              }
              else {
                 try {
                     s = Math.max(GS[j], j - Rval.get(S.charAt(s+j)));
                 }
                 catch(Exception e) {
                     s = Math.max(GS[j], j);
                 }
              }
          }
       }
  }
  
  static public int[] computePi(char[] pattern) {
     int[] pi = new int[pattern.length+1];
     int nextChr, pfxLen;

     pfxLen = 0; // Prefix we've matched against ourselves so far
     for (nextChr = 1; nextChr < pattern.length; nextChr++) {
        while (pattern[pfxLen] != pattern[nextChr] && pfxLen > 0)
           pfxLen = pi[pfxLen];

        if (pattern[pfxLen] == pattern[nextChr])
           pfxLen++;

        pi[nextChr+1] = pfxLen; // nextChr+1 is match count
     }

     return pi;
  }

  public static HashMap<Character, Integer> computeR(String P) {
      HashMap<Character, Integer> Rval = new HashMap<Character, Integer>();
     Character c;
     
     for(int i = 0; i < P.length(); i++) {
         c = new Character(P.charAt(i));
         
         Rval.put(c, new Integer(i));
     }
     
     return Rval;
  }

  public static int[] GoodSuffix(String P) {
      int[] GS = new int[P.length() + 1], pi, Rpi;
     char[] revP = new char[P.length() + 1];
     
     pi = computePi(P.toCharArray()); // is toCharArray() allowed?
     for(int i = 1; i <= P.length(); i++)
        revP[i] = P.charAt(P.length() - i);
System.out.println(revP);
     Rpi = computePi(revP);

     //not sure about this part
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
