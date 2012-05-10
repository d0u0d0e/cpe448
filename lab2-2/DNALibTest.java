import java.util.ArrayList;

public class DNALibTest {
   public static void main(String[] args) {
      String sequence = "ATACTGCGATCATACTACGGCGTCGGATATAGCGCGATAAATTTGGCGCGGCGCTACGACGTACGATACGCTAGTCGTAGCGGGCGCGTTTTCGCGCTCGCGCCATATATATAC";
                       // I  L  R  S  Y  Y  G  V  G  Y  S  A  I  N  L  A  R  R  Y  D  V  R  Y  A  S  R  S  G  R  V  F  A  L  A  P  Y  I  Y
      DNALib dna = new
DNALib(sequence, null);

// a = 25 t = 27 c = 30 g = 32

      String comp = "TATGACGCTAGTATGATGCCGCAGCCTATATCGCGCTATTTAAACCGCGCCGCGATGCTGCATGCTATGCGATCAGCATCGCCCGCGCAAAAGCGCGAGCGCGGTATATATATG";
      if(!dna.complement(0, sequence.length()).equals(comp))
         throw new AssertionError("complement method failed");

      String rev = "GTATATATATGGCGCGAGCGCGAAAACGCGCCCGCTACGACTAGCGTATCGTACGTCGTAGCGCCGCGCCAAATTTATCGCGCTATATCCGACGCCGTAGTATGATCGCAGTAT";
      if(!dna.reverse(0, sequence.length()).equals(rev))
         throw new AssertionError("reverse method failed");

      if(dna.GCContent(0, sequence.length()) != (double)62/114 * 100)
         throw new AssertionError("GCContent method failed");

      System.out.println("Histogram for Isoleucine (I)");
      System.out.println("Expected results:");
      DNALib.aminoAcid aa = dna.map.get("I");
      int total = 0;
      for(int i = 0; i < aa.codons.size(); i++)
    	  total += aa.codons.get(i).count;
      
      System.out.println("ATC   " + aa.codons.get(0).count + "   " + (double)aa.codons.get(0).count / total);
      System.out.println("ATT   " + aa.codons.get(1).count + "   " + (double)aa.codons.get(1).count / total);
      System.out.println("ATA   " + aa.codons.get(2).count + "   " + (double)aa.codons.get(2).count / total);
      
      System.out.println();
      System.out.println("Actual results:");
      dna.histogram("I");
      System.out.println();


      ArrayList<String> list = new ArrayList<String>();
      list.add("TTG");
      list.add("ATA");
      if(dna.frequencyOptimalCodons(list) != (double)4/6)
         throw new AssertionError("frequencyOptimalCodons method failed");

      if(dna.rcsu("TTG") != (double)1/((double)3/6))
         throw new AssertionError("RCSU method failed");
      
//      System.out.println(dna.CAI("TTT"));
//      System.out.println(dna.effectiveNumberCodons());

      if(dna.scaledChi2("I") != (double)6/38)
    	  throw new AssertionError("scaledChi2 method failed");

      //A = 25  T = 27  C = 30  G = 32
      double enthropy = -((double)25/114 * Math.log((double)25/114)/Math.log(2) + (double)27/114 * Math.log((double)27/114)/Math.log(2)
    		                + (double)30/114 * Math.log((double)30/114)/Math.log(2) + (double)32/114*Math.log((double)32/114)/Math.log(2));

      if(dna.enthropy() != enthropy)
    	  throw new AssertionError("enthropy method failed");




      System.out.println("All tests have passed");
   }
}
