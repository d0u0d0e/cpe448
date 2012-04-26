import java.util.Scanner;
import java.io.File;

public class GC
{
   public static void main(String[] args) throws java.io.FileNotFoundException
   {
      String filename = args[0];
      int start = Integer.parseInt(args[1]) - 1;
      int stop = Integer.parseInt(args[2]) - 1;
      int count = 0;
      int location = 0;
      int total = 0;
      double gc;
      String c;
      String line = "";
      File thefile = new File(filename);
      Scanner sc = new Scanner(thefile);
      sc.nextLine();
      sc.useDelimiter("");

      while (sc.hasNextLine())
      {
         c = sc.next();
         //System.out.print(c);
         if (location >= start && location <= stop)
         {
            if (c.equals("C") || c.equals("G"))
            {
               count++;
            }    
            total++; 
         }
         location++;
      }

      gc = ((double)count / (double)total) * 100;
      System.out.printf("GC-content is %.1f\n", gc);
   }
}
