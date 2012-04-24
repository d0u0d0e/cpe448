import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.lang.StringBuffer;
import java.util.StringTokenizer;
import java.util.ArrayList;
import java.lang.Math;
import java.util.Comparator;
import java.util.Collections;

public class Combine
{
   public static void main(String args[]) throws IOException
   {
      ArrayList<String> fastaFiles = new ArrayList<String>();
      ArrayList<String> gffFiles = new ArrayList<String>();
      ArrayList<String> fasta = new ArrayList<String>();
      ArrayList<ArrayList<String>> gff = new ArrayList<ArrayList<String>>();
      ArrayList<String> combinedFASTAS = new ArrayList<String>();
      ArrayList<ArrayList<String>> combinedGFFS = new ArrayList<ArrayList<String>>();
      int minMatch = 3;

      // intialize bio-specific Boyer-Moore's algorithm
      BioBM bm = new BioBM();

      // fasta files
      /*fastaFiles.add("fasta1");
      fastaFiles.add("fasta2");
      fastaFiles.add("fasta3");
      fastaFiles.add("fasta4");
      fastaFiles.add("fasta5");
      */
      // gff files
      /*gffFiles.add("gff1");
      gffFiles.add("gff2");
      gffFiles.add("gff3");
      gffFiles.add("gff4");
      gffFiles.add("gff5");
*/
      fastaFiles.add("fasta/contig6.txt");
      fastaFiles.add("fasta/contig7.txt");
      gffFiles.add("gff/derecta_dot_contig6.0.gff");
      gffFiles.add("gff/derecta_dot_contig7.0.gff");

      // check file counts
      if (fastaFiles.size() != gffFiles.size() && fastaFiles.size() > 1)
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
      String superFASTA = fasta.get(0);
      ArrayList<String> superGFF = new ArrayList<String>();

      for (int i = 0; i < fasta.size()-1; i++)
      {   
         int overlap;
         int maxOverlap = -1;
         int offset;
         int end = minMatch;
         
         String fasta2 = fasta.get(i+1);
         ArrayList<String> gff1 = gff.get(i);
         ArrayList<String> gff2 = gff.get(i+1);
        
         if (end > fasta2.length()-1)
         {
            System.out.println("Minimum match less than string length");
            return;
         }

         while ((overlap = bm.BMrun(superFASTA, fasta2.substring(0, end))) >= 0)
         {
            maxOverlap = overlap;
            end++;
            if (end >= fasta2.length()) 
               break;
         }
         System.out.println(maxOverlap);
         if (maxOverlap != -1)
         {
            //////////////////////////////
            // QUALITY CHECK, REMOVE LATER
            if (!fasta2.startsWith(superFASTA.substring(maxOverlap, superFASTA.length()-1)))
            {
               System.out.println("Overlap not actually found at the end");
               return;
            }
            // QUALITY CHECK, REMOVE LATER
            //////////////////////////////
            
            offset = superFASTA.length() - maxOverlap;
            superFASTA = superFASTA + fasta2.substring(offset, fasta2.length());

            int k = 0;

            for (int j = 0; j < gff1.size(); j++)
            {
               ArrayList<String> p1 = parseLine(gff1.get(j));
               ArrayList<String> p2;
               int flag = 0;
               int start1 = Integer.parseInt(p1.get(3));
               int stop1 = Integer.parseInt(p1.get(4));
               int start2, stop2;
               if (p1.get(6).charAt(0) == '+' && start1 >= maxOverlap)
               {
                  p2 = parseLine(gff2.get(k));
                  start2 = Integer.parseInt(p2.get(3));
                  stop2 = Integer.parseInt(p2.get(4));
                  if (!(start1 == (start2 + offset) && 
                        stop1 == (stop2 + offset) &&
                        p1.get(9).equals(p2.get(9)) && 
                        p1.get(11).equals(p2.get(11)))) 
                  {
                     System.out.println("p1: " + p1);
                     System.out.println("p2: " + p2);

                     if (Math.abs(start1 - stop1) < 
                         Math.abs(start2 - stop2 + offset))
                     {
                        superGFF.add(repiece(p1));
                     }
                     else
                     {
                        superGFF.add(updateOffset(p2, offset));
                     }
                  }
                  else
                  {
                     superGFF.add(updateOffset(p2, offset));
                  }
                  k++; 
               }
               else if (p1.get(6).charAt(0) == '-' && stop1 >= maxOverlap)
               {
                  if (flag == 0)
                  {
                     flag = 1;
                     while (parseLine(gff2.get(k)).get(6).charAt(0) == '+')
                     {
                        superGFF.add(updateOffset(parseLine(gff2.get(k)), offset));
                        k++;
                     }
                  } 
                  p2 = parseLine(gff2.get(k));
                  start2 = Integer.parseInt(p2.get(3));
                  stop2 = Integer.parseInt(p2.get(4));
                  if (!(start1 == (start2 + offset) && 
                        stop1 == (stop2 + offset) &&
                        p1.get(9).equals(p2.get(9)) && 
                        p1.get(11).equals(p2.get(11)))) 
                  {
                     System.out.println("p1: " + p1);
                     System.out.println("p2: " + p2);

                     if (Math.abs(start1 - stop1) < 
                         Math.abs(start1 - stop2 + offset))
                     {
                        superGFF.add(repiece(p1));
                     }
                     else
                     {
                        superGFF.add(updateOffset(p2, offset));
                     }
                  }
                  else
                  {
                     superGFF.add(updateOffset(p2, offset));
                  }
                  k++;
               }
               else
               {
                  superGFF.add(repiece(p1));
               }
            }
            for (int j = k; i < gff2.size(); i++)
            {
               superGFF.add(updateOffset(parseLine(gff2.get(i)), offset));
            }
         }
         else
         {
            combinedFASTAS.add(superFASTA);
            combinedGFFS.add(superGFF); 
            superGFF = new ArrayList<String>();
            superFASTA = fasta.get(i+1); 
         }
      }
      combinedFASTAS.add(superFASTA);
      combinedGFFS.add(superGFF); 
      
      // output test
      for (String s : combinedFASTAS)
      {
         //System.out.println("Fasta: " + s);
      }

      for (ArrayList<String> l : combinedGFFS)
      {
         for (String s : l)
         {
            System.out.println(s);
         }
      }
   }

   public static String parseFASTA(String fileName) throws IOException
   {
      Scanner sc;
      try
      {
         sc = new Scanner(new File(fileName));
      }
      catch (IOException e)
      {
         throw new IOException("File not found");
      }
      String result = "";
      
      sc.nextLine(); // skip header
      
      while (sc.hasNextLine())
      {
         result = result + sc.nextLine();
      }
      return result;     
   }
    
   public static ArrayList<String> parseGFF(String fileName, int fastaLength) throws IOException
   {
      ArrayList<String> lines = new ArrayList<String>();
      Scanner sc;
      try
      {
         sc = new Scanner(new File(fileName));
      }
      catch (IOException e)
      {
         throw new IOException("File not found");
      }
      while (sc.hasNextLine())
      {
         String line = sc.nextLine();
         lines.add(line);
      }
      return lines;
   }

   public static ArrayList<String> parseLine(String s)
   {
      ArrayList<String> pieces = new ArrayList<String>();
      StringTokenizer st = new StringTokenizer(s, " \t\";");

      while (st.hasMoreTokens())
      {
         pieces.add(st.nextToken());
      }
      return pieces;
   }

   public static String updateOffset(ArrayList<String> l, int offset)
   {
      ArrayList<String> temp = new ArrayList<String>(l);
      int start = Integer.parseInt(l.get(3));
      int stop = Integer.parseInt(l.get(4));
      
      temp.set(3, Integer.toString(start + offset));
      temp.set(4, Integer.toString(stop + offset));
      
      return repiece(temp);
   }

   public static String repiece(ArrayList<String> l)
   {
      return l.get(0) + "   .  " + l.get(2) + "  " + l.get(3) + "  " + l.get(4) + " .  " + l.get(6) + "  .  gene_id \"" + l.get(9) + "\"; transcript_id \"" + l.get(11) + "\";";
   }
}
