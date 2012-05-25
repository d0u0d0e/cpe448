package UI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.io.*;
import java.util.StringTokenizer;
import java.util.ArrayList;
import java.lang.Math;
import java.util.Comparator;
import java.util.Collections;
import lib.BioOverlap;
import lib.Isoform;
import lib.DNALib;
import lib.EditDistance;
import lib.EditDistance.SubSln;

public class Combine
{
   public Combine () {
       
   }
           
   public void combineFiles(File[] fastaFiles, File[] gffFiles, int existance, int extension) throws IOException
   {
      ArrayList<String> fasta = new ArrayList<String>();
      ArrayList<ArrayList<Isoform>> gff = new ArrayList<ArrayList<Isoform>>();
      ArrayList<String> combinedFASTAS = new ArrayList<String>();
      ArrayList<ArrayList<Isoform>> combinedGFFS = new ArrayList<ArrayList<Isoform>>();
      HashMap<String, Integer> vars = new HashMap<String, Integer>();
      
      int minMatch = 1000;
      int bp = 0, varCount = 0;
      int offset = -1, overlap = -1, split = 1;
      File matrix = new File("blosum62.txt");
      // parse files
      for (int i = 0; i < fastaFiles.length; i++)
      {
         String s = parseFASTA(fastaFiles[i]);
         fasta.add(s);
         gff.add(parseGFF(gffFiles[i]));   
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
         if (offset != -1)
            System.out.println(fastaFiles[i+1].getName() + ": found overlap");
         else
            System.out.println(fastaFiles[i+1].getName() + ": no overlap");
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
               if ((iso.mRNA.get(6).charAt(0) == '+' && Integer.parseInt(iso.mRNA.get(3)) >= overlap) ||
                   (iso.mRNA.get(6).charAt(0) == '-' && Integer.parseInt(iso.mRNA.get(4)) >= overlap) )
               {
                  // matching isoform found between files
                  if ((idx = findIsoform(iso, gff2)) != -1)
                  {
                     int k;
                     Isoform found = updateOffset(gff2.get(idx), overlap);
                     
                     for (k = 0; k < iso.CDS.size() && k < found.CDS.size(); k++)
                     {
                        // variations: different CDS values
                        if (difference(found.CDS.get(k), iso.CDS.get(k)) != 0)
                        {
                           if (!(vars.containsKey(iso.CDS.get(k).get(11))))
                           {
                              vars.put(iso.CDS.get(k).get(11), 1);
                              varCount++;
                           }
                           bp += difference(found.CDS.get(k), iso.CDS.get(k));
                           outVar.write(iso.CDS.get(k).get(11) + ", CDS, " + iso.CDS.get(k).get(0) + ", " + iso.CDS.get(k).get(3) + ", " + iso.CDS.get(k).get(4) + "\n");
                           outVar.write(found.CDS.get(k).get(11) + ", CDS, " + found.CDS.get(k).get(0) + ", " + found.CDS.get(k).get(3) + ", " + found.CDS.get(k).get(4) + "\n\n");
                           
                           // get protein sequence of the two variations
                           DNALib lib1, lib2;
                           if (Integer.parseInt(iso.CDS.get(k).get(3)) < Integer.parseInt(iso.CDS.get(k).get(4)))
                              lib1 = new DNALib(superFASTA.substring(Integer.parseInt(iso.CDS.get(k).get(3)), Integer.parseInt(iso.CDS.get(k).get(4))));
                           else
                              lib1 = new DNALib(superFASTA.substring(Integer.parseInt(iso.CDS.get(k).get(4)), Integer.parseInt(iso.CDS.get(k).get(3))));
                           if (Integer.parseInt(found.CDS.get(k).get(3)) < Integer.parseInt(found.CDS.get(k).get(4)))
                              lib2 = new DNALib(superFASTA.substring(Integer.parseInt(found.CDS.get(k).get(3)), Integer.parseInt(found.CDS.get(k).get(4))));
                           else
                              lib2 = new DNALib(superFASTA.substring(Integer.parseInt(found.CDS.get(k).get(4)), Integer.parseInt(found.CDS.get(k).get(3))));

                           String p1 = lib1.protein;
                           String p2 = lib2.protein;
                           String ref = "MKLRCFNQAQTSHSWHPRSVFRVVSSCPPDRKKRPSPASWQICIGHLLT"+
                                   "NYLGMFKMDSADFWQQARAPFGLQTALHQYSSPPNQQPISHHALHHSVPQENEQ"+
                                   "LAGVQPEGPAAGSGVVGNNSSMAISGTNSTVGSKAESSHIPQQHSEQQSYGSDS"+
                                   "FRGTQSPQLSSHHLLFNAAAAAAAAVHLKSTAMQNNLSPIGDQVQNNLRNYGQG"+
                                   "SLNALCGIKPKQEMDAKTPLQPLDECPHPLVQAQAQSQYGGDYYDVADPQAREI"+
                                   "SEGRALIIGLGAPSTVSTDDAQSSAPSHQLAGTGRALQTHQCKPMSPGTVGSSN"+
                                   "LGAGRRSAPTTISKTFSQGQQSPQHSTTPSGGSTTPDIKYNNDKMANEIQLQLS"+
                                   "RSSSAAAISERTLEECWSTLQRLFMHKSAMQQIQQQIPRVGLGTHGVTGSANLG"+
                                   "GSITPSSDTKPHQCQQCMKSFSSNHQLVQHIRVHTGEKPYKCSYCDRRFKQLSH"+
                                   "VQQHTRLHTGERPYKCHLPDCGRAFIQLSNLQQHLRNHDAQVERAKNRPFHCNI"+
                                   "CGKGFATESSLRTHTSKELQLHLGVLQQHAALIGGPNATSCPVCHKLFLGTEAL"+
                                   "VDHMKHVHKEKSPPPGGSASSQFSELNQIVTGNGNGTGSSNEQIATQCTTESNS"+
                                   "HQATVGSSLIDSFLGKRRTANHPCPVCGKHYVNEGSLRKHLACHAENSQLTNSL"+
                                   "RMWPCSVCQAVFTHENGLLTHMESMRMDPKHQFAAQYVLSRAAAEQRERESLLA"+
                                   "VTLAASSGASTRIGIADAGNVLPTGAHNSDGSNSKCPSPSANSECSSNGRLSSS"+
                                   "TTSDQDQDIDHGLSENENSNQNNIGSSTNNNNNCTSNNNASSHKMAELRLPGTG"+
                                   "QYTMDAELHVANRMSLMAAAAAAVAASRPQDGVDTSAVPSAAVQAAVVNLAAAM"+
                                   "RMNNSSNGATPYQQHHADHTQAHQTHILQHAHPHHHQQQQAPQHQQQQLSHLLV"+
                                   "HTHPSNSNSRSQSSPNINVPSLQNESTAASIAMNMNVHMMRGSLDPDPSLGGMH"+
                                   "GIEVLHQHQQQHHHHHHHHTSNYPQHAVTPTNQHTHPHPQTHQTAHHHTSPETA"+
                                   "LRMHQAEAILRSHTEAAFRLATGSGPDTGVKCEADQNQLNSNNAGNAGGNSGGN"+
                                   "SQQHRFHASSNHQENQRPSDS";

                           //System.out.println(p1); 

                           SubSln[][] sln1 = null;
                           SubSln[][] sln2 = null;
                           int p1cost = 0, p2cost = 0;
                           // run editdistance against reference sequence for each protein sequence
                           if (existance != Integer.MAX_VALUE && extension != Integer.MAX_VALUE) {
                               try {
                                   sln1 = EditDistance.align(ref, p1, EditDistance.scoreMatrix(matrix), existance, extension, false);
                                   sln2 = EditDistance.align(ref, p2, EditDistance.scoreMatrix(matrix), existance, extension, false);
                               } catch (Exception e) {System.err.println(e);}
                           
                           for(SubSln[] arr: sln1){
                               for(SubSln sol: arr) {
                                   if(sol.value > p1cost)
                                       p1cost = sol.value;
                               }
                           }
                           for(SubSln[] arr: sln2){
                               for(SubSln sol: arr) {
                                   if(sol.value > p2cost)
                                       p2cost = sol.value;
                               }
                           }
                           // if lib2 is better, enter below if statement. otherwise do nothing   
                           if (p2cost > p1cost)
                           {
                              iso.CDS.set(k, found.CDS.get(k));
                           }
                           }
                        }
                     }
                     // variations: missing CDS
                     for (int x = k; x < iso.CDS.size(); x++)
                     {
                        outVar.write(iso.CDS.get(x).get(11) + ", CDS, " + iso.CDS.get(x).get(0) + ", " + iso.CDS.get(x).get(3) + ", " + iso.CDS.get(x).get(4) + "\n");
                        outVar.write(found.CDS.get(0).get(11) + ", CDS, " + gffFiles[i+1].getName() + " missing, missing\n\n");
                        if (!(vars.containsKey(iso.CDS.get(x).get(11))))
                        {
                           vars.put(iso.CDS.get(x).get(11), 1);
                           varCount++;
                        }
                     }          
                     for (int x = k; x < found.CDS.size(); x++)
                     {  
                        iso.CDS.add(found.CDS.get(x));
                        outVar.write(iso.CDS.get(0).get(11) + ", CDS, " + gffFiles[i+1].getName()  + ",  missing, missing\n\n");
                        outVar.write(found.CDS.get(x).get(11) + ", CDS, " + found.CDS.get(x).get(0) + ", " + found.CDS.get(x).get(3) + ", " + found.CDS.get(x).get(4) + "\n");
                        if (!(vars.containsKey(found.CDS.get(x).get(11))))
                        {
                           vars.put(found.CDS.get(x).get(11), 1);
                           varCount++;
                        }
                     }

                     gff2.remove(found);
                  }
                  else
                  {
                     outVar.write(iso.mRNA.get(11) + ", mRNA, " + iso.mRNA.get(0) + ", " + iso.mRNA.get(3) + ", " + iso.mRNA.get(4) + "\n");
                     outVar.write(iso.mRNA.get(11) + ", mRNA, " + gffFiles[i+1].getName() + ", missing, missing\n\n");
                     if (!(vars.containsKey(iso.mRNA.get(11))))
                     {
                        vars.put(iso.mRNA.get(11), 1);
                        varCount++;
                     }
                  }
               }
            }
            // go through remaining second gff file
            for (int j = 0; j < gff2.size(); j++)
            {
               superGFF.add(updateOffset(gff2.get(j), overlap));   
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
      outVar.write("Total isoforms with variation: " + Integer.toString(varCount) + "\n");
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

   public static String parseFASTA(File fileName) throws IOException
   {
      Scanner sc;
      try
      {
         sc = new Scanner(fileName);
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
    
   public static ArrayList<Isoform> parseGFF(File fileName) throws IOException
   {
      ArrayList<Isoform> isoList = new ArrayList<Isoform>();
      ArrayList<String> parsedLine = new ArrayList<String>();
      Scanner sc;
      Isoform iso = null;

      try
      {
         sc = new Scanner(fileName);
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
