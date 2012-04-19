import java.util.Scanner;
import java.io.File;
import java.lang.StringBuffer;
import java.util.StringTokenizer;
import java.util.ArrayList;
import java.lang.String;
import java.lang.Math;
import java.util.Comparator;
import java.util.Collections;

public class Main
{
   public static void main(String[] args) throws java.io.FileNotFoundException
   {
      if (args.length != 2)
      {
         System.out.println("Invalid input.");
         System.out.println("Parameters: <FASTA> <GFF>");
         return;
      }
      
      // FASTA File
      StringBuffer seqBuffer = new StringBuffer();
      File fastaFile = new File(args[0]);
      Scanner sc = new Scanner(fastaFile);
      sc.nextLine();
      sc.useDelimiter("");

      while (sc.hasNextLine())
      {
         String c = sc.next();
         if (!c.equals("\n"))
            seqBuffer = seqBuffer.append(c);
      }
      String seq = seqBuffer.toString();

      // GFF File
      ArrayList<Gene> geneList = new ArrayList<Gene>();
      ArrayList<String> idList = new ArrayList<String>();
      File gffFile = new File(args[1]);
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
      

      // Initialize DNA Libarary
      DNALib lib = new DNALib(seq, geneList);
      
      // Basic Functionality
      //System.out.println("GC-content: " + lib.GCContent(start, stop));
      //System.out.println("Complement: " + lib.complement(start, stop));
      //System.out.println("Reverse: " + lib.reverse(start, stop));
      
      // 2.2. GC-Content Regions
      System.out.println("GC-Content Regions with Overlap (start, stop, GC):");
      lib.overlap(1000, 100);
      System.out.print("\n");

      System.out.println("GC-Content Regions without Overlap (start, stop, GC):");
      lib.nonoverlap(100);
      System.out.print("\n");

      // 2.2.1. Gene Density
      System.out.println("Gene Density: ");
      lib.geneDensity();
      System.out.print("\n");

      // 2.2.2 Gene GC-Content
      System.out.println("Gene GC-Content (gene, start, stop, GC): ");
      lib.geneGC();
      System.out.print("\n");

      // 2.2.3 Gene Size 
      System.out.println("Gene Size (gene, start, stop, size): ");
      lib.geneSize();
      System.out.print("\n");

      // 2.2.4 Codon Frequency
      System.out.println("Codon Frequency (codon, amino acid, frequency, ratio): ");
      lib.codonFrequency();
      System.out.print("\n");

      // Error
      System.out.println("Error:");
      lib.getError();
   }
}
