import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;

public class GC
{
   public static void main(String[] args) throws java.io.FileNotFoundException
   {
      ArrayList<String> fastaFiles = new ArrayList<String>();
      ArrayList<String> fastaFilesAll = new ArrayList<String>();
      int start, stop;

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
        
         // adjust for missing contig 23         
         if (start >= 23 || stop >= 23)
         {
            stop--;
         }

         for (int i = start-20; i <= stop-20; i++)
         {
            fastaFiles.add(fastaFilesAll.get(i));
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
         for (int i = start-22; i <= stop-22; i++)
         {
            fastaFiles.add(fastaFilesAll.get(i));
         }
      }
      else
      {
         System.err.println("Invalid chromosome type: 'c' or 'f' only");
         return;
      }

      // parse files
      for (int i = 0; i < fastaFiles.size(); i++)
      {
         int count = 0;
         int total = 0;
         double gc = 0.0;
         String c;
         String line = "";
         File thefile = new File(fastaFiles.get(i));
         Scanner sc = new Scanner(thefile);
         sc.nextLine();
         sc.useDelimiter("");

         while (sc.hasNext())
         {
            c = sc.next();
            if (c.equals("C") || c.equals("G"))
            {
               count++;
               total++;
            }
            else if (c.equals("A") || c.equals("T") || c.equals("N"))
            {    
               total++; 
            }
         }

         gc = ((double)count / (double)total) * 100;
         System.out.printf("GC-content of " + fastaFiles.get(i) + " is %.1f\n", gc);
      }
   }
}
