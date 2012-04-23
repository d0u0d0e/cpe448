import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;

import java.lang.StringBuffer;
import java.util.StringTokenizer;
import java.util.ArrayList;
import java.lang.Math;
import java.util.Comparator;
import java.util.Collections;

public class Combine
{
   public static void main(String args[]) throws java.io.FileNotFoundException
   {
      ArrayList<String> fastaFiles = new ArrayList<String>();
      ArrayList<String> gffFiles = new ArrayList<String>();
      ArrayList<String> fasta = new ArrayList<String>();
      ArrayList<ArrayList<Gene>> gff = new ArrayList<ArrayList<Gene>>();

      // intialize bio-specific Boyer-Moore's algorithm
      BioBM bm = new BioBM();

      // fasta files
      fastaFiles.add("fasta1");
      fastaFiles.add("fasta2");
      
      // gff files
      gffFiles.add("gff1");
      gffFiles.add("gff2");

      // check file counts
      if (fastaFiles.size() != gffFiles.size())
      {  
         System.out.println("File count does not match\n");
         return;
      }

      // parse files
      for (int i = 0; i < fastaFiles.size(); i++)
      {
         String s = parseFASTA(fastaFiles.get(i));
         fasta.add(s);
         gff.add(parseGFF(gffFiles.get(i), s.length()));   
      }

      // combine files
      for (int i = 0; i < fasta.size()-1; i++)
      {   
         int overlap;
         int maxOverlap = -1;
         int end = 0;
         
         String fasta1 = fasta.get(i);
         String fasta2 = fasta.get(i+1);
         ArrayList<Gene> gff1 = gff.get(i);
         ArrayList<Gene> gff2 = gff.get(i+1);

         while ((overlap = bm.BMrun(fasta1, fasta2.substring(0, end))) >= 0)
         {
            System.out.println((fasta2.substring(0, end)));
            maxOverlap = overlap;
            end++;
            if (end >= fasta2.length())
               break;
         }

    
         System.out.println("Fasta1: " + fasta1);
         System.out.println("Fasta2: " + fasta2);
         System.out.println("Overlap: " + maxOverlap);
         String fastaC = fasta1 + fasta2.substring(fasta1.length()-maxOverlap, fasta2.length()-1);
         System.out.println("Combined: " + fastaC);

      }
   }

   public static String parseFASTA(String fileName) throws java.io.FileNotFoundException
   {
      Scanner sc = new Scanner(new File(fileName));
      String result = "";
      
      sc.nextLine(); // skip header
      
      while (sc.hasNextLine())
      {
         result = result + sc.nextLine();
      }
      return result;     
   }
    
   public static ArrayList<Gene> parseGFF(String fileName, int fastaLength) throws java.io.FileNotFoundException
   {
      ArrayList<Gene> geneList = new ArrayList<Gene>();
      ArrayList<String> idList = new ArrayList<String>();
      Scanner sc = new Scanner(new File(fileName));
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
      max = fastaLength;

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

      return geneList;
   }
}
