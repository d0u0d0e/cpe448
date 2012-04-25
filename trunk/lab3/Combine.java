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
      fastaFiles.add("fasta/contig20.txt");
      fastaFiles.add("fasta/contig21.txt");
      fastaFiles.add("fasta/contig22.txt");
      fastaFiles.add("fasta/contig24.txt");
      fastaFiles.add("fasta/contig25.txt");
      fastaFiles.add("fasta/contig26.txt");
      fastaFiles.add("fasta/contig27.txt");
      fastaFiles.add("fasta/contig28.txt");
      fastaFiles.add("fasta/contig29.txt");
      fastaFiles.add("fasta/contig30.txt");
      fastaFiles.add("fasta/contig31.txt");
      fastaFiles.add("fasta/contig32.txt");
      fastaFiles.add("fasta/contig33.txt");
      fastaFiles.add("fasta/contig34.txt");
      fastaFiles.add("fasta/contig35.txt");
      fastaFiles.add("fasta/contig36.txt");
      fastaFiles.add("fasta/contig37.txt");
      fastaFiles.add("fasta/contig38.txt");
      
      // MISSING: fastaFiles.add("fasta/contig23.txt");
      
      // gff files
      gffFiles.add("gff/derecta_dot_contig20.0.gff");
      gffFiles.add("gff/derecta_dot_contig21.0.gff");
      gffFiles.add("gff/derecta_dot_contig22.0.gff");
      gffFiles.add("gff/derecta_dot_contig24.0.gff");
      gffFiles.add("gff/derecta_dot_contig25.0.gff");
      gffFiles.add("gff/derecta_dot_contig26.0.gff");
      gffFiles.add("gff/derecta_dot_contig27.0.gff");
      gffFiles.add("gff/derecta_dot_contig28.0.gff");
      gffFiles.add("gff/derecta_dot_contig29.0.gff");
      gffFiles.add("gff/derecta_dot_contig30.0.gff");
      gffFiles.add("gff/derecta_dot_contig31.0.gff");
      gffFiles.add("gff/derecta_dot_contig32.0.gff");
      gffFiles.add("gff/derecta_dot_contig33.0.gff");
      gffFiles.add("gff/derecta_dot_contig34.0.gff");
      gffFiles.add("gff/derecta_dot_contig35a.gff");
      gffFiles.add("gff/derecta_dot_contig36.0.gff");
      gffFiles.add("gff/derecta_dot_contig37.0.gff");
      gffFiles.add("gff/derecta_dot_contig38.0.gff");
      
      // MISSING: gffFiles.add("gff/derecta_dot_contig23.0.gff");
     
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
         System.err.println(gffFiles.get(i)); 
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
         System.out.println(maxOverlap);
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

            int flag = 0;
            int z = 0, x = 0;
            for (int j = 0; j < gff1.size(); j++)
            {
               ArrayList<String> p1 = parseLine(gff1.get(j));
               ArrayList<String> p2, p2t;
               int start1 = Integer.parseInt(p1.get(3));
               int stop1 = Integer.parseInt(p1.get(4));
               int start2, stop2;

               // check for entry to negative orders
               if (flag == 0 && p1.get(6).charAt(0) == '-')
               {
                  flag = 1;
                  System.err.println("flag set");
                  // add rest of first GFF file positives
                  for (z = k; z < gff2.size() && parseLine(gff2.get(z)).get(6).charAt(0) == '+'; z++)
                  {
                     superGFF.add(repiece(updateOffset(parseLine(gff2.get(z)), maxOverlap)));
                  }
                  
                  for (x = gff2.size()-1; x > 0 && Integer.parseInt(parseLine(gff2.get(x)).get(3)) <= offset && parseLine(gff2.get(x)).get(6).charAt(0) == '-'; x--)
                  {
                  }
                  k = ++x;
                  
                  for (int l = z; l < x; l++)
                  {
                     superGFF.add(repiece(updateOffset(parseLine(gff2.get(l)), maxOverlap)));
                  }
               }
               // overlaps
               if (start1 >= maxOverlap)
               {
                  if (k < gff.size())
                  {
                     p2 = parseLine(gff2.get(k));
                     start2 = Integer.parseInt(p2.get(3));
                     stop2 = Integer.parseInt(p2.get(4));

                     // variations found
                     if (start1 != (start2 + maxOverlap) || stop1 != (stop2 + maxOverlap)
                         || (!p1.get(9).equals(p2.get(9))))
                     {
                        // write variations to file
                        outVar.write(p1.get(9) + ", " + p1.get(0) + ", " + p1.get(3) + ", " + p1.get(4) + "\n");
                        p2t = updateOffset(p2, maxOverlap);
                        outVar.write(p2t.get(9) + ", " + p2t.get(0) + ", " + p2t.get(3) + ", " + p2t.get(4) + "\n");
                        
                        // total gene variations and bp
                        if (!(vars.containsKey(p1.get(9))))
                        {
                           vars.put(p1.get(9), 1);
                           varCount++;
                        }
                        if (!(vars.containsKey(p2.get(9))))
                        {
                           vars.put(p2.get(9), 1);
                           varCount++;
                        }
                        bp += Math.abs(Math.abs(start1-stop1) - Math.abs(start2-stop2));
                        
                        
                        // range mismatch only, add highest range
                        if (p1.get(9).equals(p2.get(9)))
                        { 
                           if (Math.abs(start1 - stop1) < Math.abs(start1 - stop2 + maxOverlap))
                           {
                              superGFF.add(repiece(p1));
                           }
                           else
                           {
                              superGFF.add(repiece(updateOffset(p2, maxOverlap)));
                           }
                        }
                        /* gene mismatch, just add p1 */
                        else
                        { 
                           superGFF.add(repiece(p1));
                        }
                     }
                     // no variation
                     else
                     {
                        superGFF.add(repiece(updateOffset(p2, maxOverlap)));
                     }
                     k++;
                  }
                  else
                  {
                     superGFF.add(repiece(p1));
                  }
               }
               // not overlapping
               else
               {
                  superGFF.add(repiece(p1));
               }
            }
         
            // add remaining genes of second GFF file
            if (flag == 0) 
            {
               for (int j = k; j < gff2.size(); j++)
               {
                  superGFF.add(repiece(updateOffset(parseLine(gff2.get(j)), maxOverlap)));
               }
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
         throw new IOException("File not found" + fileName);
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
         throw new IOException("File not found" + fileName);
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
