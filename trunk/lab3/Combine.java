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
      int minMatch = 1000;
      int offset = -1, overlap = -1;

      // contig files
      fastaFiles.add("fasta/contig20.txt");
      fastaFiles.add("fasta/contig21.txt");
      fastaFiles.add("fasta/contig22.txt");
      //fastaFiles.add("fasta/contig23.txt");
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
      gffFiles.add("gff/derecta_dot_contig20.0.gff");
      gffFiles.add("gff/derecta_dot_contig21.0.gff");
      gffFiles.add("gff/derecta_dot_contig22.0.gff");
      //gffFiles.add("gff/derecta_dot_contig23.0.gff");
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

      // fosmid files
      fastaFiles.add("fasta/Fosmid22.txt");
      fastaFiles.add("fasta/Fosmid23.txt");
      fastaFiles.add("fasta/Fosmid24.txt");
      fastaFiles.add("fasta/Fosmid25.txt");
      fastaFiles.add("fasta/Fosmid26.txt");
      fastaFiles.add("fasta/Fosmid27.txt");
      fastaFiles.add("fasta/Fosmid28.txt");
      fastaFiles.add("fasta/Fosmid29.txt");
      fastaFiles.add("fasta/Fosmid30.txt");
      fastaFiles.add("fasta/Fosmid31.txt");
      fastaFiles.add("fasta/Fosmid32.txt");
      fastaFiles.add("fasta/Fosmid33.txt");
      fastaFiles.add("fasta/Fosmid34.txt");
      fastaFiles.add("fasta/Fosmid35.txt");
      fastaFiles.add("fasta/Fosmid36.txt");
      fastaFiles.add("fasta/Fosmid37.txt");
      fastaFiles.add("fasta/Fosmid38.txt");
      fastaFiles.add("fasta/Fosmid39.txt");
      fastaFiles.add("fasta/Fosmid40.txt");
      fastaFiles.add("fasta/Fosmid41.txt");
      fastaFiles.add("fasta/Fosmid42.txt");
      gffFiles.add("gff/derecta_3Lcontrol_fosmid22a.gff");
      gffFiles.add("gff/derecta_3Lcontrol_fosmid23a.gff");
      gffFiles.add("gff/derecta_3Lcontrol_fosmid24a.gff");
      gffFiles.add("gff/derecta_3Lcontrol_fosmid25a.gff");
      gffFiles.add("gff/derecta_3Lcontrol_fosmid26a.gff");
      gffFiles.add("gff/derecta_3Lcontrol_fosmid27a.gff");
      gffFiles.add("gff/derecta_3Lcontrol_fosmid28a.gff");
      gffFiles.add("gff/derecta_3Lcontrol_fosmid29a.gff");
      gffFiles.add("gff/derecta_3Lcontrol_fosmid30a.gff");
      gffFiles.add("gff/derecta_3Lcontrol_fosmid31a.gff");
      gffFiles.add("gff/derecta_3Lcontrol_fosmid32a.gff");
      gffFiles.add("gff/derecta_3Lcontrol_fosmid33a.gff");
      gffFiles.add("gff/derecta_3Lcontrol_fosmid34a.gff");
      gffFiles.add("gff/derecta_3Lcontrol_fosmid35a.gff");
      gffFiles.add("gff/derecta_3Lcontrol_fosmid36a.gff");
      gffFiles.add("gff/derecta_3Lcontrol_fosmid37a.gff");
      gffFiles.add("gff/derecta_3Lcontrol_fosmid38a.gff");
      gffFiles.add("gff/derecta_3Lcontrol_fosmid39a.gff");
      gffFiles.add("gff/derecta_3Lcontrol_fosmid40a.gff");
      gffFiles.add("gff/derecta_3Lcontrol_fosmid41a.gff");
      gffFiles.add("gff/derecta_3Lcontrol_fosmid42a.gff");

      // check file counts
      if (fastaFiles.size() != gffFiles.size() && fastaFiles.size() > 1)
      {  
         System.err.println("File count does not match\n");
         return;
      }

      // parse files
      System.out.print("Parsing files...");
      for (int i = 0; i < fastaFiles.size(); i++)
      {
         String s = parseFASTA(fastaFiles.get(i));
         fasta.add(s);
         gff.add(parseGFF(gffFiles.get(i), s.length()));   
      }
      System.out.println("done");
      
      // combine files
      String superFASTA = fasta.get(0);
      ArrayList<String> superGFF = new ArrayList<String>();
      ArrayList<String> gff1 = gff.get(0);

      for (int i = 0; i < fasta.size()-1; i++)
      { 
         System.out.print("Merging " + gffFiles.get(i) + "..."); 
         
         if (offset == -1)
            System.out.println("found no overlap");
         else
            System.out.println("found overlap at " + overlap);
        
         overlap = -1;
         offset = -1;
         String fasta2 = fasta.get(i+1);
         ArrayList<String> gff2 = gff.get(i+1);
        
         if (minMatch > fasta2.length()-1)
         {
            System.err.println("Minimum match less than string length");
            return;
         }

         // find largest overlap
         offset = BioOverlap.getOffset(superFASTA, fasta2, minMatch);
         overlap = superFASTA.length() - offset;

         // overlap found
         if (offset != -1)
         {
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
                  // add rest of first GFF file positives
                  for (z = k; z < gff2.size() && parseLine(gff2.get(z)).get(6).charAt(0) == '+'; z++)
                  {
                     superGFF.add(repiece(updateOffset(parseLine(gff2.get(z)), overlap)));
                  }
                  
                  for (x = gff2.size()-1; x > 0 && Integer.parseInt(parseLine(gff2.get(x)).get(3)) <= offset && parseLine(gff2.get(x)).get(6).charAt(0) == '-'; x--)
                  {
                  }
                  k = ++x;
                  
                  for (int l = z; l < x; l++)
                  {
                     superGFF.add(repiece(updateOffset(parseLine(gff2.get(l)), overlap)));
                  }
               }
               // overlaps
               if (start1 >= overlap)
               {
                  if (k < gff2.size())
                  {
                     p2 = parseLine(gff2.get(k));
                     start2 = Integer.parseInt(p2.get(3));
                     stop2 = Integer.parseInt(p2.get(4));

                     // variations found
                     if (start1 != (start2 + overlap) || stop1 != (stop2 + overlap)
                         || (!p1.get(9).equals(p2.get(9))))
                     {
                        // write variations to file
                        outVar.write(p1.get(9) + ", " + p1.get(0) + ", " + p1.get(3) + ", " + p1.get(4) + "\n");
                        p2t = updateOffset(p2, overlap);
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
                           if (Math.abs(start1 - stop1) < Math.abs(start1 - stop2 + overlap))
                           {
                              superGFF.add(repiece(p1));
                           }
                           else
                           {
                              superGFF.add(repiece(updateOffset(p2, overlap)));
                           }
                        }
                        /* gene mismatch, add both */
                        else
                        { 
                           superGFF.add(repiece(p1));
                           superGFF.add(repiece(updateOffset(p2, overlap)));
                        }
                     }
                     // no variation
                     else
                     {
                        superGFF.add(repiece(updateOffset(p2, overlap)));
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
                  superGFF.add(repiece(updateOffset(parseLine(gff2.get(j)), overlap)));
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
      System.out.print("Outputting to files...");
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

      System.out.println("done");
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
