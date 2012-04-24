import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.io.*;
import java.util.StringTokenizer;
import java.util.ArrayList;
import java.lang.Math;

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
      FileWriter fstreamVar = new FileWriter("variations.csv");
      BufferedWriter outVar = new BufferedWriter(fstreamVar);
      HashMap<String, Integer> vars = new HashMap<String, Integer>();

      int bp = 0, varCount = 0;
      int minMatch = 300;
      int splits = 1;

      // intialize bio-specific Boyer-Moore's algorithm
      BioBM bm = new BioBM();

      // fasta files
      fastaFiles.add("fasta/contig1.txt");
      fastaFiles.add("fasta/contig3.txt");
      fastaFiles.add("fasta/contig5.txt");
      fastaFiles.add("fasta/contig6.txt");
      fastaFiles.add("fasta/contig7.txt");
      fastaFiles.add("fasta/contig8.txt");
      fastaFiles.add("fasta/contig20.txt");
      fastaFiles.add("fasta/contig21.txt");
      
      // gff files
      gffFiles.add("gff/derecta_dot_contig1.0.gff");
      gffFiles.add("gff/derecta_dot_contig3.0.gff");
      gffFiles.add("gff/derecta_dot_contig5.0.gff");
      gffFiles.add("gff/derecta_dot_contig6.0.gff");
      gffFiles.add("gff/derecta_dot_contig7.0.gff");
      gffFiles.add("gff/derecta_dot_contig8.0.gff");
      gffFiles.add("gff/derecta_dot_contig20.0.gff");
      gffFiles.add("gff/derecta_dot_contig21.0.gff");

      // check file counts
      if (fastaFiles.size() != gffFiles.size() && fastaFiles.size() > 1)
      {  
         System.err.println("File count does not match\n");
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
      ArrayList<String> gff1 = gff.get(0);

      for (int i = 0; i < fasta.size()-1; i++)
      {   
         int overlap;
         int maxOverlap = -1;
         int offset;
         int end = minMatch;
         
         String fasta2 = fasta.get(i+1);
         ArrayList<String> gff2 = gff.get(i+1);
        
         if (end > fasta2.length()-1)
         {
            System.err.println("Minimum match less than string length");
            return;
         }

         // find largest overlap
         while ((overlap = bm.BMrun(superFASTA, fasta2.substring(0, end))) >= 0)
         {
            maxOverlap = overlap;
            end++;
            if (end >= fasta2.length()) 
               break;
         }
         
         // overlap found
         if (maxOverlap != -1)
         {
            //////////////////////////////
            // MINMATCH QUALITY CHECK, REMOVE LATER
            if (!fasta2.startsWith(superFASTA.substring(maxOverlap, superFASTA.length()-1)))
            {
               System.err.println("Overlap not actually found at the end, try higher minMatch value");
               return;
            }
            // MINMATCH QUALITY CHECK, REMOVE LATER
            //////////////////////////////
            
            offset = superFASTA.length() - maxOverlap;
            superFASTA = superFASTA + fasta2.substring(offset, fasta2.length());

            // combine GFF files
            int k = 0; /* second gff file */

            for (int j = 0; j < gff1.size(); j++)
            {
               ArrayList<String> p1 = parseLine(gff1.get(j));
               ArrayList<String> p2, p2t;
               int flag = 0;
               int start1 = Integer.parseInt(p1.get(3));
               int stop1 = Integer.parseInt(p1.get(4));
               int start2, stop2;

               // positive order overlaps
               if (p1.get(6).charAt(0) == '+' && start1 >= maxOverlap)
               {
                  p2 = parseLine(gff2.get(k));
                  start2 = Integer.parseInt(p2.get(3));
                  stop2 = Integer.parseInt(p2.get(4));
                  
                  // variation found
                  if (start1 != (start2 + maxOverlap) || 
                      stop1 != (stop2 + maxOverlap))
                  {
                     // write variations to file
                     System.err.println("j: " + j + ", k: " + k );
                     outVar.write(p1.get(9) + ", " + p1.get(0) + ", " + p1.get(3) + ", " + p1.get(4) + "\n");
                     p2t = updateOffset(p2, maxOverlap);
                     outVar.write(p2t.get(9) + ", " + p2t.get(0) + ", " + p2t.get(3) + ", " + p2t.get(4) + "\n");
   
                     // total gene variations and bp
                     if (!(vars.containsKey(p1.get(9))))
                     {
                        vars.put(p1.get(9), 1);
                        varCount++;
                     }
                     bp += Math.abs(Math.abs(start1-stop1) - Math.abs(start2-stop2));

                     // add highest range 
                     if (Math.abs(start1 - stop1) < 
                         Math.abs(start2 - stop2 + maxOverlap))
                     {
                        superGFF.add(repiece(p1));
                     }
                     else
                     {
                        superGFF.add(repiece(updateOffset(p2, maxOverlap)));
                     }
                  }
                  // no variation
                  else
                  {
                     superGFF.add(repiece(updateOffset(p2, maxOverlap)));
                  }
                  k++; 
               }
               // negatives order overlaps
               else if (p1.get(6).charAt(0) == '-' && stop1 >= maxOverlap)
               {
                  if (flag == 0)
                  {
                     flag = 1;
                     // add rest of first GFF file positives
                     while (parseLine(gff2.get(k)).get(6).charAt(0) == '+')
                     {
                        superGFF.add(repiece(updateOffset(parseLine(gff2.get(k)), maxOverlap)));
                        k++;
                     }
                  } 
                  p2 = parseLine(gff2.get(k));
                  start2 = Integer.parseInt(p2.get(3));
                  stop2 = Integer.parseInt(p2.get(4));

                  // variations found
                  if (start1 != (start2 + maxOverlap) || 
                      stop1 != (stop2 + maxOverlap))
                  {
                     // write variations to file
                     System.err.println("j: " + j + ", k: " + k );
                     outVar.write(p1.get(9) + ", " + p1.get(0) + ", " + p1.get(3) + ", " + p1.get(4) + "\n");
                     p2t = updateOffset(p2, maxOverlap);
                     outVar.write(p2t.get(9) + ", " + p2t.get(0) + ", " + p2t.get(3) + ", " + p2t.get(4) + "\n");
                     
                     // total gene variations and bp
                     if (!(vars.containsKey(p1.get(9))))
                     {
                        vars.put(p1.get(9), 1);
                        varCount++;
                     }
                     bp += Math.abs(Math.abs(start1-stop1) - Math.abs(start2-stop2));

                     // add highest range 
                     if (Math.abs(start1 - stop1) < 
                         Math.abs(start1 - stop2 + maxOverlap))
                     {
                        superGFF.add(repiece(p1));
                     }
                     else
                     {
                        superGFF.add(repiece(updateOffset(p2, maxOverlap)));
                     }
                  }
                  // no variation
                  else
                  {
                     superGFF.add(repiece(updateOffset(p2, maxOverlap)));
                  }
                  k++;
               }
               // not overlapping
               else
               {
                  if (p1.get(6).charAt(0) == '-' && flag == 0)
                  {
                     flag = 1;
                     // add rest of first GFF file positives
                     while (k < gff2.size() && parseLine(gff2.get(k)).get(6).charAt(0) == '+')
                     {
                        superGFF.add(repiece(updateOffset(parseLine(gff2.get(k)), maxOverlap)));
                        k++;
                     }
                  }
                  superGFF.add(repiece(p1));
               }
            }
            // add remaining genes of second GFF file
            for (int j = k; j < gff2.size(); j++)
            {
               superGFF.add(repiece(updateOffset(parseLine(gff2.get(j)), maxOverlap)));
            }
            gff1 = superGFF;
            superGFF = new ArrayList<String>();
         }
         // overlap not found
         else
         {
            combinedFASTAS.add(superFASTA);
            combinedGFFS.add(gff1); 
            superGFF = new ArrayList<String>();
            superFASTA = fasta.get(i+1);
            gff1 = gff.get(i+1); 
         }
      }
      // add remaining
      combinedFASTAS.add(superFASTA);
      combinedGFFS.add(gff1); 
      
      // output to file
      for (int i = 0; i < combinedFASTAS.size(); i++)
      { 
         String outname = "superFASTA" + Integer.toString(i+1) + ".txt";
         FileWriter fstream = new FileWriter(outname);
         BufferedWriter out = new BufferedWriter(fstream);
         String s = combinedFASTAS.get(i);
         out.write("header\n");
         // line break every 50 chars
         for (int j = 0; j < s.length(); j+=50)
         {
            if (j + 50 > s.length()-1)
            {
               out.write(s.substring(j, s.length()-1) + "\n");
            }
            else
            {
               out.write(s.substring(j, j + 50) + "\n");
            }
         }
         out.close();
      }
      for (int i = 0; i < combinedGFFS.size(); i++)
      { 
         String outname2 = "superGFF" + Integer.toString(i+1) + ".txt";
         FileWriter fstream2 = new FileWriter(outname2);
         BufferedWriter out2 = new BufferedWriter(fstream2);
         for (String s : combinedGFFS.get(i))
         {
            out2.write(s + "\n");
         }
         out2.close();
      }

      // output variation totals
      outVar.write("Total genes with variation: " + Integer.toString(varCount) + "\n");
      outVar.write("Total differed bp: " + Integer.toString(bp));
      outVar.close();
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

   public static ArrayList<String> updateOffset(ArrayList<String> l, int offset)
   {
      ArrayList<String> temp = new ArrayList<String>(l);
      int start = Integer.parseInt(l.get(3));
      int stop = Integer.parseInt(l.get(4));
      
      temp.set(3, Integer.toString(start + offset));
      temp.set(4, Integer.toString(stop + offset));
      
      return temp;
   }

   public static String repiece(ArrayList<String> l)
   {
      return l.get(0) + "\t.\t" + l.get(2) + "\t" + l.get(3) + "\t" + l.get(4) + "\t.\t" + l.get(6) + "\t.\tgene_id \"" + l.get(9) + "\"; transcript_id \"" + l.get(11) + "\";";
   }
}
