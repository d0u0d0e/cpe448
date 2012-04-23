import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;

public class Combine
{
   public static void main(String args[]) throws java.io.FileNotFoundException
   {
      ArrayList<String> fasta = new ArrayList<String>();
      ArrayList<String> gff = new ArrayList<String>();
      ArrayList<String> offset = new ArrayList<String>();
      BioBM bm = new BioBM();

      // fasta files
      fasta.add("fasta/contig6.txt");
      fasta.add("fasta/contig7.txt");

      // gff files
      gff.add("gff/derecta_dot_contig6.0.gff");
      gff.add("gff/derecta_dot_contig7.0.gff");

      // check file counts
      if (fasta.size() != gff.size())
      {  
         System.out.println("File count does not match\n");
         return;
      }

      // loop through array of files
      for (int i = 0; i < fasta.size()-1; i++)
      {
         Scanner scF1 = new Scanner(new File(fasta.get(i)));
         Scanner scF2 = new Scanner(new File(fasta.get(i+1)));
         Scanner scG1 = new Scanner(new File(gff.get(i)));
         Scanner scG2 = new Scanner(new File(gff.get(i+1)));
         
         // fasta1
         String fasta1 = "";
         scF1.nextLine();
         while (scF1.hasNextLine())
         {
            fasta1 = fasta1 + scF1.nextLine();
         }
        
         // fasta2
         String fasta2 = "";
         scF2.nextLine();
         while (scF2.hasNextLine())
         {
            fasta2 = fasta2 + scF2.nextLine();
         }
         
         int overlap = bm.BMrun(fasta1, fasta2);

         System.out.println(fasta1);
         System.out.println(fasta2);
         System.out.println("Overlap: " + overlap);
         break;
      }
   }
   
   public static String findOverlap(String s1, String s2)
   {
      return "";      
   }
}
