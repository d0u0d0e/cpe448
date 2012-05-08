import java.util.ArrayList;

public class maiN {
   public static void main(String[] args) {
      String sequence = "ATACTGCGATCATACTACGGCGTCGGATATAGCGCGATAAATTTGGCGCGGCGCTACGACGTACGATACGCTAGTCGTAGCGGGCGCGTTTTCGCGCTCGCGCCATATATATAC";
      DNALib dna = new 
DNALib(sequence, null);

// I L R S Y Y G V G # S A I N L A R R Y D V R Y A S R S G R V F A L A P Y I Y

      String comp = "TATGACGCTAGTATGATGCCGCAGCCTATATCGCGCTATTTAAACCGCGCCGCGATGCTGCATGCTATGCGATCAGCATCGCCCGCGCAAAAGCGCGAGCGCGGTATATATATG";
      if(!dna.complement(0, sequence.length()).equals(comp))
         throw new AssertionError("complement method failed");

      String rev = "GTATATATATGGCGCGAGCGCGAAAACGCGCCCGCTACGACTAGCGTATCGTACGTCGTAGCGCCGCGCCAAATTTATCGCGCTATATCCGACGCCGTAGTATGATCGCAGTAT";
      if(!dna.reverse(0, sequence.length()).equals(rev))
         throw new AssertionError("reverse method failed");

      if(dna.GCContent(0, sequence.length()) != (double)62/114 * 100)
         throw new AssertionError("GCContent method failed");

      System.out.println("Histogram for Arginine (R)");
      System.out.println("Expected results:");
      System.out.println("CGT");

      System.out.println("Actual results:");
      dna.histogram("R");
/*
      ArrayList list = new ArrayList();
      list.add("ATG");
      list.add("TTT");
      System.out.println(dna.frequencyOptimalCodons(list));

      System.out.println(dna.rcsu("TTT"));
      System.out.println(dna.CAI("TTT"));
      System.out.println(dna.effectiveNumberCodons());
      System.out.println(dna.scaledChi2("R"));

      System.out.println(dna.enthropy());
      //gene density
*/

      System.out.println("All tests have passed");
   }
}
