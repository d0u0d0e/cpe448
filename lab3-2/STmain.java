import java.util.Scanner;
import java.io.File;
import java.lang.StringBuffer;
import java.util.StringTokenizer;
import java.util.ArrayList;
import java.lang.String;
import java.lang.Math;
import java.util.Comparator;
import java.util.Collections;

public class STmain
{
   public static void main(String args[]) throws java.io.FileNotFoundException
   {
      // FASTA File
      StringBuffer seqBuffer = new StringBuffer();
      File fastaFile = new File("fasta/contig22.txt");
      Scanner sc = new Scanner(fastaFile);
      sc.nextLine();

      while (sc.hasNext())
      {
         String c = sc.next();
         if (!c.equals("\n"))
            seqBuffer = seqBuffer.append(c);
      }
      String seq = seqBuffer.toString();

      // GFF File
      ArrayList<Gene> geneList = new ArrayList<Gene>();
      ArrayList<String> idList = new ArrayList<String>();
      File gffFile = new File("gff/derecta_dot_contig22.0.gff");
      sc = new Scanner(gffFile);
      String id = "";      
      int startg = 0, stopg = 0;
      while (sc.hasNextLine())
      {
         String line = sc.nextLine();
         StringTokenizer st = new StringTokenizer(line, " \t");
         int count = 0;
         int flag = 0;
         while (st.hasMoreTokens())
         {
            String word = st.nextToken();
            if (count == 2)
            {
               if (word.equals("mRNA"))
               {
                  flag = 1;
               }
               else if (word.equals("CDS"))
               {
                  flag = 2;
               }
            }
            else if (count == 3 && flag > 0)
               startg = Integer.parseInt(word);
            else if (count == 4 && flag > 0)
               stopg = Integer.parseInt(word);
            else if (count == 9 && flag > 0)
            {
               StringTokenizer st2 = new StringTokenizer(word, "\";");
               id = st2.nextToken();
            }
            count++;
         }
         // mRNA
         if (flag == 1)
         { 
            if (!idList.contains(id))
            {
               Gene gene;
               if (startg < stopg)
                  gene = new Gene (id, startg, stopg, true);
               else
                  gene = new Gene (id, stopg, startg, false);

               geneList.add(gene);
               idList.add(id);
            }
            else
            {
               for (Gene g : geneList)
               {
                  if (id.equals(g.id))
                  {
                     if (Math.abs(startg - stopg) > Math.abs(g.start-g.stop))
                     { 
                        Gene ng; 
                        if (startg < stopg)
                        {
                           ng = new Gene(id, startg, stopg, true);
                        }
                        else
                        {
                           ng = new Gene(id, stopg, startg, false);
                        }
                        geneList.set(geneList.indexOf(g), ng);
                     }
                     break;
                  }
               }
            }            
         }
         // CDS
         else if (flag == 2)
         {
            for (Gene g : geneList)
            {
               if (id.equals(g.id))
               {
                  int dup = 0;

                  for (Gene p : g.points)
                  {
                     if (p.start == startg && p.stop == stopg)
                     {
                        dup = 1;
                     }
                  }
                  if (dup == 0)
                  {
                     if (startg < stopg)
                     {
                        g.points.add(new Gene("Exon", startg, stopg, true));
                     }
                     else
                     {
                        g.points.add(new Gene("Exon", stopg, startg, false));
                     }
                  }
               }
            }
         }
      }
      
      // Exons and Introns within Genes

      int min;
      int max;
      
      for (Gene g : geneList)
      {
         Collections.sort(g.points, new Comparator<Gene>()
            {
               public int compare(Gene a, Gene b) {
                  return a.start - b.start;
               }
            });
      }

      for (Gene g : geneList)
      {
         min = g.start;
         max = g.stop;
         ArrayList<Gene> copy = new ArrayList<Gene>(g.points);
         for (Gene p : copy)
         {
            if (min < p.start)
            {
               Gene gene = new Gene("Intron", min, p.start-1, false);
               g.points.add(gene);       
            }
            min = p.stop+1;      
         } 
         if (max > min)
         {
            Gene gene = new Gene("Intron", min, max, false);
            g.points.add(gene);
         }
         Collections.sort(g.points, new Comparator<Gene>()
            {
               public int compare(Gene a, Gene b) {
                  return a.start - b.start;
               }
            });
      }

      // Genes and Intergenic Regions
      min = 1;
      max = seq.length();

      ArrayList<Gene> geneSort = new ArrayList<Gene>(geneList);
      Collections.sort(geneSort, new Comparator<Gene>() 
         {
            public int compare(Gene a, Gene b) {
               return a.start - b.start;
            }
         });

      for (Gene g : geneSort)
      {
         if (min < g.start)
         {
            Gene gene = new Gene("Intergenic", min, g.points.get(0).start-1, false);
            geneList.add(gene);
         }
         min = g.points.get(g.points.size()-1).stop+1;
      }
      if (max > min)
      {
         Gene gene = new Gene("Intergenic", min, max, false);
         geneList.add(gene);
      }  
      
      Collections.sort(geneList, new Comparator<Gene>() 
         {
            public int compare(Gene a, Gene b) {
               return a.start - b.start;
            }
         });
      
      System.out.println("Repeats:");
      //Repeat rep = new Repeat("AATTTAAAAAAACCCAAAAA", 1, 10, 2, geneList);
      Repeat rep = new Repeat(seq, 6, 12, 10, geneList);

      System.out.println("Size, Repeats, Frequency, Expected Frequency, % More than Expected, Sequence, Average Distances, Standard Deviation, Distance to Nearest Gene");
      for (Repeat.Unexpected ue : rep.unexpected)
      {
         System.out.printf("%d, %f, %f, %f, %s, %f, %f, %f\n", ue.size, ue.freq, ue.expectedFreq, ue.percentFreq, ue.s, ue.selfAvgProximity, ue.selfStdProximity, ue.geneProximity);
      }

      System.out.println("\nPalindromes:");
      //Palindrome pal = new Palindrome("AAAGGACCGCTACAGTAACCCCCCGGGGGGATTTATATATATTATTTTGGCCTTT", 2, 12, 0, 5);
      //Palindrome pal = new Palindrome("AAATTT", 1, 10, 0, 10);
      Palindrome pal = new Palindrome(seq, 15, 30, 20, 50);
      System.out.println("Sequence, Reverse Complement of Sequence, Length, Gap, Location 1, Location 2"); 
      for (String s : pal.gapPalindromes)
      {
         for (int i = 0; i < pal.gapLocations1.get(s).size(); i++)
         {
            System.out.printf("%s, %s, %d, %d, %d, %d\n", s, Palindrome.reverseComplement(s), s.length(), Math.abs(pal.gapLocations1.get(s).get(i)-pal.gapLocations2.get(s).get(i)) - s.length(), pal.gapLocations1.get(s).get(i), pal.gapLocations2.get(s).get(i));   
         }
      }
   }
}
