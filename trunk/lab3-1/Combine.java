import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.io.*;
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
      ArrayList<String> fastaFilesAll = new ArrayList<String>();
      ArrayList<String> gffFiles = new ArrayList<String>();
      ArrayList<String> gffFilesAll = new ArrayList<String>();
      ArrayList<String> fasta = new ArrayList<String>();
      ArrayList<ArrayList<Isoform>> gff = new ArrayList<ArrayList<Isoform>>();
      ArrayList<String> combinedFASTAS = new ArrayList<String>();
      ArrayList<ArrayList<Isoform>> combinedGFFS = new ArrayList<ArrayList<Isoform>>();
      HashMap<String, Integer> vars = new HashMap<String, Integer>();
      
      int minMatch = 1000;
      int bp = 0, varCount = 0;
      int offset = -1, overlap = -1, split = 1;
      int start = 0, stop = 0;

      if (args.length != 3)
      {
         System.err.println("Not enough arguments: <chromosome> <start> <stop>");
         return;
      }
      
      if (args[0].charAt(0) == 'c' ||
          args[0].charAt(0) == 'C')
      {
         start = Integer.parseInt(args[1]);
         stop = Integer.parseInt(args[2]);

         if (start < 20 || start < 20 ||
             stop > 38 || stop > 38 ||
             start > stop)
         {
            System.err.println("Invalid range, 20-38 only");
            return;
         }
         
         // contig files 20-38
         fastaFilesAll.add("fasta/contig20.txt");
         fastaFilesAll.add("fasta/contig21.txt");
         fastaFilesAll.add("fasta/contig22.txt");
         //MISSING: fastaFiles.add("fasta/contig23.txt");
         fastaFilesAll.add("fasta/contig24.txt");
         fastaFilesAll.add("fasta/contig25.txt");
         fastaFilesAll.add("fasta/contig26.txt");
         fastaFilesAll.add("fasta/contig27.txt");
         fastaFilesAll.add("fasta/contig28.txt");
         fastaFilesAll.add("fasta/contig29.txt");
         fastaFilesAll.add("fasta/contig30.txt");
         fastaFilesAll.add("fasta/contig31.txt");
         fastaFilesAll.add("fasta/contig32.txt");
         fastaFilesAll.add("fasta/contig33.txt");
         fastaFilesAll.add("fasta/contig34.txt");
         fastaFilesAll.add("fasta/contig35.txt");
         fastaFilesAll.add("fasta/contig36.txt");
         fastaFilesAll.add("fasta/contig37.txt");
         fastaFilesAll.add("fasta/contig38.txt");
         gffFilesAll.add("gff/derecta_dot_contig20.0.gff");
         gffFilesAll.add("gff/derecta_dot_contig21.0.gff");
         gffFilesAll.add("gff/derecta_dot_contig22.0.gff");
         //MISSING: gffFilesAll.add("gff/derecta_dot_contig23.0.gff");
         gffFilesAll.add("gff/derecta_dot_contig24.0.gff");
         gffFilesAll.add("gff/derecta_dot_contig25.0.gff");
         gffFilesAll.add("gff/derecta_dot_contig26.0.gff");
         gffFilesAll.add("gff/derecta_dot_contig27.0.gff");
         gffFilesAll.add("gff/derecta_dot_contig28.0.gff");
         gffFilesAll.add("gff/derecta_dot_contig29.0.gff");
         gffFilesAll.add("gff/derecta_dot_contig30.0.gff");
         gffFilesAll.add("gff/derecta_dot_contig31.0.gff");
         gffFilesAll.add("gff/derecta_dot_contig32.0.gff");
         gffFilesAll.add("gff/derecta_dot_contig33.0.gff");
         gffFilesAll.add("gff/derecta_dot_contig34.0.gff");
         gffFilesAll.add("gff/derecta_dot_contig35a.gff");
         gffFilesAll.add("gff/derecta_dot_contig36.0.gff");
         gffFilesAll.add("gff/derecta_dot_contig37.0.gff");
         gffFilesAll.add("gff/derecta_dot_contig38.0.gff");
        
         // adjust for missing contig 23
         System.out.println("Merging contig files " + Integer.toString(start) +
                            " to " + Integer.toString(stop) + "...");
         
         if (start >= 23 || stop >= 23)
         {
            stop--;
         }

         for (int i = start-20; i <= stop-20; i++)
         {
            fastaFiles.add(fastaFilesAll.get(i));
            gffFiles.add(gffFilesAll.get(i));
         }
      }

      else if (args[0].charAt(0) == 'f' ||
               args[0].charAt(0) == 'F')
      {
         start = Integer.parseInt(args[1]);
         stop = Integer.parseInt(args[2]);

         if (start < 22 || start < 22 ||
             stop > 42 || stop > 42 ||
             start > stop)
         {
            System.err.println("Invalid range, 22-42 only");
            return;
         }
 
         // fosmid files 22-42
         fastaFilesAll.add("fasta/Fosmid22.txt");
         fastaFilesAll.add("fasta/Fosmid23.txt");
         fastaFilesAll.add("fasta/Fosmid24.txt");
         fastaFilesAll.add("fasta/Fosmid25.txt");
         fastaFilesAll.add("fasta/Fosmid26.txt");
         fastaFilesAll.add("fasta/Fosmid27.txt");
         fastaFilesAll.add("fasta/Fosmid28.txt");
         fastaFilesAll.add("fasta/Fosmid29.txt");
         fastaFilesAll.add("fasta/Fosmid30.txt");
         fastaFilesAll.add("fasta/Fosmid31.txt");
         fastaFilesAll.add("fasta/Fosmid32.txt");
         fastaFilesAll.add("fasta/Fosmid33.txt");
         fastaFilesAll.add("fasta/Fosmid34.txt");
         fastaFilesAll.add("fasta/Fosmid35.txt");
         fastaFilesAll.add("fasta/Fosmid36.txt");
         fastaFilesAll.add("fasta/Fosmid37.txt");
         fastaFilesAll.add("fasta/Fosmid38.txt");
         fastaFilesAll.add("fasta/Fosmid39.txt");
         fastaFilesAll.add("fasta/Fosmid40.txt");
         fastaFilesAll.add("fasta/Fosmid41.txt");
         fastaFilesAll.add("fasta/Fosmid42.txt");
         gffFilesAll.add("gff/derecta_3Lcontrol_fosmid22a.gff");
         gffFilesAll.add("gff/derecta_3Lcontrol_fosmid23a.gff");
         gffFilesAll.add("gff/derecta_3Lcontrol_fosmid24a.gff");
         gffFilesAll.add("gff/derecta_3Lcontrol_fosmid25a.gff");
         gffFilesAll.add("gff/derecta_3Lcontrol_fosmid26a.gff");
         gffFilesAll.add("gff/derecta_3Lcontrol_fosmid27a.gff");
         gffFilesAll.add("gff/derecta_3Lcontrol_fosmid28a.gff");
         gffFilesAll.add("gff/derecta_3Lcontrol_fosmid29a.gff");
         gffFilesAll.add("gff/derecta_3Lcontrol_fosmid30a.gff");
         gffFilesAll.add("gff/derecta_3Lcontrol_fosmid31a.gff");
         gffFilesAll.add("gff/derecta_3Lcontrol_fosmid32a.gff");
         gffFilesAll.add("gff/derecta_3Lcontrol_fosmid33a.gff");
         gffFilesAll.add("gff/derecta_3Lcontrol_fosmid34a.gff");
         gffFilesAll.add("gff/derecta_3Lcontrol_fosmid35a.gff");
         gffFilesAll.add("gff/derecta_3Lcontrol_fosmid36a.gff");
         gffFilesAll.add("gff/derecta_3Lcontrol_fosmid37a.gff");
         gffFilesAll.add("gff/derecta_3Lcontrol_fosmid38a.gff");
         gffFilesAll.add("gff/derecta_3Lcontrol_fosmid39a.gff");
         gffFilesAll.add("gff/derecta_3Lcontrol_fosmid40a.gff");
         gffFilesAll.add("gff/derecta_3Lcontrol_fosmid41a.gff");
         gffFilesAll.add("gff/derecta_3Lcontrol_fosmid42a.gff");

         for (int i = start-22; i <= stop-22; i++)
         {
            fastaFiles.add(fastaFilesAll.get(i));
            gffFiles.add(gffFilesAll.get(i));
         }
                  
         System.out.println("Merging fosmid files " + Integer.toString(start) +
                            " to " + Integer.toString(stop) + "...");
      }
      else
      {
         System.err.println("Invalid chromosome type: 'c' or 'f' only");
         return;
      }

      // parse files
      for (int i = 0; i < fastaFiles.size(); i++)
      {
         String s = parseFASTA(fastaFiles.get(i));
         fasta.add(s);
         gff.add(parseGFF(gffFiles.get(i)));   
      }
      
      // combine files
      String superFASTA = fasta.get(0);
      ArrayList<Isoform> superGFF = gff.get(0);
      FileWriter fstreamVar = new FileWriter("variations" + Integer.toString(split) + ".csv");
      BufferedWriter outVar = new BufferedWriter(fstreamVar);
      
      for (int i = 0; i < fasta.size()-1; i++)
      {
         overlap = -1;
         offset = -1;
         String fasta2 = fasta.get(i+1);
         ArrayList<Isoform> gff2 = gff.get(i+1);
         
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
            
            // combine gff files
            // go through first gff file
            for (int j = 0; j < superGFF.size(); j++)
            {
               int idx;
               Isoform iso = superGFF.get(j);

               // isoform within overlapping region
               if (iso.mRNA.get(6).charAt(0) == '+' && Integer.parseInt(iso.mRNA.get(3)) >= overlap ||
                   iso.mRNA.get(6).charAt(0) == '-' && Integer.parseInt(iso.mRNA.get(4)) >= overlap)
               {
                  // matching isoform found between files
                  if ((idx = findIsoform(iso, gff2)) != -1)
                  {
                     int k;
                     Isoform found = updateOffset(gff2.get(idx), offset);
                     
                     for (k = 0; k < iso.CDS.size() || k < found.CDS.size(); k++)
                     {
                        // variations: different CDS values
                        if (isHigher(found.CDS.get(k), iso.CDS.get(k)))
                        {
                           if (!(vars.containsKey(iso.CDS.get(k).get(11))))
                           {
                              vars.put(iso.CDS.get(k).get(11), 1);
                              varCount++;
                           }
                           bp += difference(found.CDS.get(k), iso.CDS.get(k));
                           iso.CDS.set(k, found.CDS.get(k));
                           outVar.write(iso.CDS.get(k).get(11) + ", " + iso.CDS.get(k).get(0) + ", " + iso.CDS.get(k).get(3) + ", " + iso.CDS.get(k).get(4) + "\n");
                           outVar.write(found.CDS.get(k).get(11) + ", " + found.CDS.get(k).get(0) + ", " + found.CDS.get(k).get(3) + ", " + found.CDS.get(k).get(4) + "\n");
                        }
                     }
                     // variations: missing CDS
                     for (int x = k; x < iso.CDS.size(); x++)
                     {
                        outVar.write(iso.CDS.get(x).get(11) + ", " + iso.CDS.get(x).get(0) + ", " + iso.CDS.get(x).get(3) + ", " + iso.CDS.get(x).get(4) + "\n");
                        outVar.write(found.CDS.get(0).get(11) + ", missing, missing, missing\n");
                     }          
                     for (int x = k; x < found.CDS.size(); x++)
                     {
                        iso.CDS.add(found.CDS.get(x));
                        outVar.write(found.CDS.get(x).get(11) + ", " + found.CDS.get(x).get(0) + ", " + found.CDS.get(x).get(3) + ", " + found.CDS.get(x).get(4) + "\n");
                        outVar.write(iso.CDS.get(0).get(11) + ", missing, missing, missing\n");
                     }

                     gff2.remove(found);
                  }
               }
            }
            // go through remaining second gff file
            for (int j = 0; j < gff2.size(); j++)
            {
               superGFF.add(updateOffset(gff2.get(j), offset));   
            }
         }
         // overlap not found
         else
         {
            combinedFASTAS.add(superFASTA);
            superFASTA = fasta.get(i+1);
      
            combinedGFFS.add(superGFF);
            superGFF = gff.get(i+1);

            outVar.write("Total genes with variation: " + Integer.toString(varCount) + "\n");
            outVar.write("Total differed bp: " + Integer.toString(bp));
            outVar.close();
            split++; 
            fstreamVar = new FileWriter("variations" + Integer.toString(split) + ".csv");
            outVar = new BufferedWriter(fstreamVar);
            bp = 0;
            varCount = 0;
            vars = new HashMap<String, Integer>();
         }
      }
      // add remaining
      combinedFASTAS.add(superFASTA);
      combinedGFFS.add(superGFF);
      outVar.write("Total genes with variation: " + Integer.toString(varCount) + "\n");
      outVar.write("Total differed bp: " + Integer.toString(bp));
      outVar.close(); 


      // output FASTAS to file
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

      // output GFFS to file
      for (int i = 0; i < combinedGFFS.size(); i++)
      { 
         String outname2 = "superGFF" + Integer.toString(i+1) + ".txt";
         FileWriter fstream2 = new FileWriter(outname2);
         BufferedWriter out2 = new BufferedWriter(fstream2);
         
         // sort results
         Collections.sort(combinedGFFS.get(i), new Comparator<Isoform>()
            {
               public int compare(Isoform a, Isoform b) 
               {
                  char aOrder = a.mRNA.get(6).charAt(0);
                  char bOrder = b.mRNA.get(6).charAt(0);
                  int aStart = Integer.parseInt(a.mRNA.get(3));
                  int aStop = Integer.parseInt(a.mRNA.get(4));
                  int bStart = Integer.parseInt(b.mRNA.get(3));
                  int bStop = Integer.parseInt(b.mRNA.get(4));

                  if (aOrder != bOrder)
                  {
                     if (aOrder == '+')
                     {
                        return -1;
                     }
                     return 1;
                  }
                  else
                  {
                     if (aOrder == '+') 
                     {
                        return aStart - bStart;         
                     }
                     else
                     {
                        return bStart - aStart;         
                     }
                  }
               }
            });
         for (Isoform iso : combinedGFFS.get(i))
         {
            out2.write(repiece(iso.mRNA));
            for (ArrayList<String> cds : iso.CDS)
            {
               out2.write(repiece(cds));
            }
         }
         out2.close();
      }

   }    
      /*
      String superFASTA = fasta.get(0);
      ArrayList<String> superGFF = new ArrayList<String>();
      ArrayList<String> gff1 = gff.get(0);
      FileWriter fstreamVar = new FileWriter("variations" + Integer.toString(split) + ".csv");
      BufferedWriter outVar = new BufferedWriter(fstreamVar);
      
      for (int i = 0; i < fasta.size()-1; i++)
      { 
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
            int k = 0; //second gff file

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
                        // gene mismatch, add both
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
            outVar.write("Total genes with variation: " + Integer.toString(varCount) + "\n");
            outVar.write("Total differed bp: " + Integer.toString(bp));
            outVar.close();
            split++; 
            fstreamVar = new FileWriter("variations" + Integer.toString(split) + ".csv");
            outVar = new BufferedWriter(fstreamVar);
            bp = 0;
            varCount = 0;
            vars = new HashMap<String, Integer>();
         }
      }

      // add remaining
      combinedFASTAS.add(superFASTA);
      combinedGFFS.add(gff1); 
      outVar.write("Total genes with variation: " + Integer.toString(varCount) + "\n");
      outVar.write("Total differed bp: " + Integer.toString(bp));
      outVar.close(); 

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
   }*/

   public static boolean isHigher(ArrayList<String> c1, ArrayList<String> c2)
   {  
      if (Math.abs(Integer.parseInt(c1.get(3)) - Integer.parseInt(c1.get(4))) > 
          Math.abs(Integer.parseInt(c2.get(3)) - Integer.parseInt(c2.get(4))))
      {
         return true;
      }
      return false;
   }

   public static int difference(ArrayList<String> c1, ArrayList<String> c2)
   {  
      return Math.abs(Math.abs(Integer.parseInt(c1.get(3)) - Integer.parseInt(c1.get(4))) - 
                      Math.abs(Integer.parseInt(c2.get(3)) - Integer.parseInt(c2.get(4))));
   }

   public static int findIsoform(Isoform iso, ArrayList<Isoform> isoList)
   {  
      for (int i = 0; i < isoList.size(); i++)
      {
         if (isoList.get(i).mRNA.get(11).equals(iso.mRNA.get(11)))
         {
            return i;
         }
      }
      return -1;
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
    
   public static ArrayList<Isoform> parseGFF(String fileName) throws IOException
   {
      ArrayList<Isoform> isoList = new ArrayList<Isoform>();
      ArrayList<String> parsedLine = new ArrayList<String>();
      Scanner sc;
      Isoform iso = null;

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
         parsedLine = parseLine(line);

         if (parsedLine.get(2).equals("mRNA"))
         {
            if (iso != null)
            {
               isoList.add(iso);
            }
            iso = new Isoform(parsedLine);      
         }
         else if (parsedLine.get(2).equals("CDS"))
         {
            iso.CDS.add(parsedLine);
         }
      }

      return isoList;
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

   public static Isoform updateOffset(Isoform iso, int offset)
   {
      iso.mRNA.set(3, Integer.toString(Integer.parseInt(iso.mRNA.get(3)) + offset));
      iso.mRNA.set(4, Integer.toString(Integer.parseInt(iso.mRNA.get(4)) + offset));

      for (int i = 0; i < iso.CDS.size(); i++)
      { 
         iso.CDS.get(i).set(3, Integer.toString(Integer.parseInt(iso.CDS.get(i).get(3)) + offset));
         iso.CDS.get(i).set(4, Integer.toString(Integer.parseInt(iso.CDS.get(i).get(4)) + offset));
      } 
      return iso;
   }

   public static String repiece(ArrayList<String> l)
   {
      return l.get(0) + "\t.\t" + l.get(2) + "\t" + l.get(3) + "\t" + l.get(4) + "\t.\t" + l.get(6) + "\t.\tgene_id \"" + l.get(9) + "\"; transcript_id \"" + l.get(11) + "\";\n";
   }
}
