import java.util.ArrayList;

public class DNALibTest {
   public static void main(String[] args) {
      boolean passed = true;
      String sequence = "ATGTACGTCACGTACTGA";
                       // M  Y  V  T  Y  #
      DNALib dna = new DNALib(sequence, null);

      System.out.println("Test sequence: " + sequence);

      String comp = "TACATGCAGTGCATGACT";
      if(!dna.complement(0, sequence.length()).equals(comp))
         System.out.println("complement method failed");

      String rev = "TCAGTACGTGACGTACAT";
      if(!dna.reverse(0, sequence.length()).equals(rev)) {
         System.out.println("reverse method failed");
         passed = false;
      }

      if(dna.GCContent(0, sequence.length()) != (double)8/18 * 100) {
         System.out.println("GCContent method failed");
         passed = false;
      }

      System.out.println();
      System.out.println("Histogram for Tyrosine (Y)");
      System.out.println("Expected results:");
      DNALib.aminoAcid aa = dna.map.get("Y");
      int total = 0;
      for(int i = 0; i < aa.codons.size(); i++)
    	  total += aa.codons.get(i).count;
      
      System.out.println("TAT   " + aa.codons.get(0).count + "   " + (double)aa.codons.get(0).count / total);
      System.out.println("TAC   " + aa.codons.get(1).count + "   " + (double)aa.codons.get(1).count / total);
      
      System.out.println();
      System.out.println("Actual results:");
      dna.histogram("Y");
      System.out.println();

      ArrayList<String> list = new ArrayList<String>();
      list.add("TAC");
      if(dna.frequencyOptimalCodons(list) != (double)1)
         System.out.println("frequencyOptimalCodons method failed");

      if(dna.rcsu("TAC") != (double)2) {
         System.out.println("RCSU method failed");
         passed = false;
      }

      if(dna.CAI("TAC") != (double)1) {
         System.out.println("CAI method failed");
         passed = false;
      }

      if(dna.effectiveNumberCodons() != (double)20) {
         System.out.println("effectiveNumberCodons method failed");
         passed = false;
      }

      if(dna.scaledChi2("Y") != (double)2/5) {
    	  System.out.println("scaledChi2 method failed");
          passed = false;
      }

      //A = 5  T = 5  C = 4  G = 4
      double enthropy = -((double)5/18 * Math.log((double)5/18)/Math.log(2) + (double)5/18 * Math.log((double)5/18)/Math.log(2)
    		         + (double)4/18 * Math.log((double)4/18)/Math.log(2) + (double)4/18*Math.log((double)4/18)/Math.log(2));

      if(dna.enthropy() != enthropy) {
    	  System.out.println("enthropy method failed");
          passed = false;
      }

      if(passed)
         System.out.println("All tests have passed");

   }
}
