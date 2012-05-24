import java.util.Scanner;
import java.util.EnumSet;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.StringBuffer;
import java.util.*;

public class EditDistance {
   enum Dir {left, up, upLeft, nil};
   static HashMap<String, Integer> score = new HashMap<String, Integer>(576);

   private static class SubSln {
      public int value;
      public EnumSet<Dir> from;
      public StringBuffer edits, step;

      public SubSln(int value, EnumSet<Dir> from, String edit) {
         edits = new StringBuffer();
         step = new StringBuffer();
         this.value = value;
         this.from = from;
         edits.append(edit);
      }
   };

   private static class Local {
      public int x, y, value;

      public Local(int x, int y, int value) {
         this.x = x;
         this.y = y;
         this.value = value;
      }
   }

   public static void main(String[] args) throws Exception {
      Scanner in = new Scanner(System.in);
      SubSln[][] slns;
      File matrix = new File("blosum62.txt");
      String s1, s2;
      int penalty, ext;
      boolean global;

      System.out.println("Enter two strings: ");
      s1 = in.nextLine();
      s2 = in.nextLine();
      System.out.println("Enter gap penalty cost");
      penalty = in.nextInt();
      System.out.println("Enter extension penalty cost");
      ext = in.nextInt();
      System.out.println("Is this a global alignment? (Y/N)");
      if(in.next().equals("Y"))
         global = true;
      else
         global = false;

      try {
         slns = align(s1, s2, scoreMatrix(matrix), penalty, ext, global);

         if(global)
            System.out.printf("\nBest Cost is:  %d\n", slns[s1.length()][s2.length()].value);
         else {
            ArrayList<Local> list = bestLocal(slns, s1.length()+1, s2.length()+1);

            for(Local loc : list)
               System.out.printf("\nBest cost is:  %d at %d,%d", loc.value, loc.x, loc.y);
         }

         System.out.println();
         printMappings(slns, s1, s2);
         printSlns(slns, s1, s2);

      }
      catch(FileNotFoundException e) {
    	  System.out.println("\nmatrix not found");
      }


   }

   static SubSln[][] align(String s1, String s2, HashMap<String, Integer> score, int penalty, int extension, boolean global) throws Exception {
      SubSln slns[][] = new SubSln [s1.length() + 1][s2.length() + 1];
      SubSln sln, base = new SubSln(0, EnumSet.of(Dir.nil), " ");
      int d1, d2, cost, val, pen, ext;
      StringBuffer key = new StringBuffer("ab");
      String gapext;

      if(global)
         pen = penalty;
      else
         pen = 0;

      slns[0][0] = base;

      //initializing top row
      for(d2 = 1; d2 <= s2.length(); d2++) {
         gapext = slns[0][d2-1].edits.toString();
         ext = 0;

         if(global)
            if(gapext.length() >=6) {
               if(gapext.substring(0, 6).equals("Insert"))
                  ext = extension;
            }

         slns[0][d2] = new SubSln(slns[0][d2-1].value + pen + ext, EnumSet.of(Dir.left), "Insert");
         slns[0][d2].step.append("' ' --- '" + s2.charAt(d2-1)+"' ,");
      }

      for(d1 =1; d1 <= s1.length(); d1++) {
         gapext = slns[d1-1][0].edits.toString();
         ext = 0;
         if(global)
            if(gapext.length() >=6) {
               if(gapext.substring(0, 6).equals("Delete"))
                  ext = extension;
            }

          slns[d1][0] = new SubSln(slns[d1-1][0].value + pen + ext, EnumSet.of(Dir.up), "Delete");
          slns[d1][0].step.append("'" + s1.charAt(d1-1)+"' --- ' ' ,");
          key.setCharAt(0, s1.charAt(d1-1));
          
          //filling out table
          for(d2 = 1; d2 <= s2.length(); d2++) {
              val = -100000;
              key.setCharAt(1, s2.charAt(d2-1));

              cost = score.get(key.toString());

            //Mapping
              if(slns[d1-1][d2-1].value + cost == val) {
                 if(slns[d1][d2] == null) {
                    slns[d1][d2] = new SubSln(slns[d1-1][d2-1].value + cost, EnumSet.of(Dir.upLeft), "Copy");
                    slns[d1][d2].step.append("'" + s1.charAt(d1-1) + "' --- '" + s2.charAt(d2-1) + "' ,");
                 }
                 else {
                    slns[d1][d2].edits.append(",Mapping");
                 }
              }
              else if(slns[d1-1][d2-1].value + cost > val) {
                 slns[d1][d2] = new SubSln(slns[d1-1][d2-1].value+cost, EnumSet.of(Dir.upLeft), "Map");
                 slns[d1][d2].step.append("'" + s1.charAt(d1-1) + "' --- '" + s2.charAt(d2-1) + "' ,");
                 val = slns[d1][d2].value;
              }

            //Insert
              gapext = slns[d1][d2-1].edits.toString();
              ext = 0;
              if(gapext.length() >=6) {
                 if(gapext.substring(0, 6).equals("Insert"))
                    ext = extension;
              }

              if(slns[d1][d2-1].value + pen + ext == val) {
                 if(slns[d1][d2] == null) {
                	slns[d1][d2] = new SubSln(slns[d1][d2-1].value + pen + ext, EnumSet.of(Dir.left), "Insert");
                 }
                 else {
                    slns[d1][d2].step.append("' ' --- '" + s2.charAt(d2-1) + "' ,");
                 }
              }
              else if(slns[d1][d2-1].value + pen + ext > val) {
            	 slns[d1][d2] = new SubSln(slns[d1][d2-1].value + pen + ext, EnumSet.of(Dir.left), "Insert");
                 slns[d1][d2].step.append("' ' --- '" + s2.charAt(d2-1)+"' ,");
                 val = slns[d1][d2].value;
              }


            //Delete
              gapext = slns[d1-1][d2].edits.toString();
              ext = 0;
              if(gapext.length() >=6) {
                 if(gapext.substring(0, 6).equals("Delete"))
                    ext = extension;
              }

              if(slns[d1-1][d2].value + pen + ext == val) {
                  if(slns[d1][d2] == null) {
                 	slns[d1][d2] = new SubSln(slns[d1-1][d2].value + pen + ext, EnumSet.of(Dir.up), "Delete");
                  }
                  else {
                     slns[d1][d2].step.append("'" + s1.charAt(d1-1) + "' --- ' ' ,");
                  }
               }
               else if(slns[d1-1][d2].value + pen + ext > val) {
             	 slns[d1][d2] = new SubSln(slns[d1-1][d2].value + pen + ext, EnumSet.of(Dir.up), "Delete");
                  slns[d1][d2].step.append("'" + s1.charAt(d1-1)+"' --- ' ' ,");
                  val = slns[d1][d2].value;
               }              
          }
      }
      return slns;
   }

   static void printSlns(SubSln[][] slns, String s1, String s2) throws Exception {
      int d1, d2;

      System.out.printf("      ");
      for(d2 = 0; d2 < s2.length(); d2++) {
         System.out.printf("            (%c) ", s2.charAt(d2));
      }

      for(d1 = 0; d1 <= s1.length(); d1++) {
         System.out.printf("\r\n%s ",  d1 == 0 ? "   " : "(" + s1.charAt(d1-1) + ")");
         for(d2 = 0; d2 <= s2.length(); d2++) {                    //outputs total edit values
            System.out.printf("%d              ", slns[d1][d2].value);
         }

         System.out.print("\r\n   ");
         for(d2 = 0; d2 <= s2.length(); d2++) {   //row to output edits
            System.out.printf("%"+5+"s          ", slns[d1][d2].edits.toString());
         }
      }
      System.out.println();
   }
   
   static void printMappings(SubSln[][] slns, String s1, String s2) {
      int d1 = s1.length(), d2 = s2.length(), stp;
      ArrayList<String> edits = new ArrayList<String>();
      Iterator iter;
      Dir dir;

      while(d1 != 0 || d2 != 0) {
         stp = slns[d1][d2].step.indexOf(",");
 
         if(stp > 0)
            edits.add(slns[d1][d2].step.substring(0, stp));
         else
            edits.add(slns[d1][d2].step.toString());

         iter = slns[d1][d2].from.iterator();
         dir = (Dir)iter.next();
         if(dir.equals(Dir.left))
            d2--;
         else if(dir.equals(Dir.up))
               d1--; 
         else if(dir.equals(Dir.upLeft)) {
            d1--;
            d2--;
         }
      }

      System.out.println("\nMappings are:");

      for(int i = edits.size()-1; i >= 0; i--)
         System.out.println(edits.get(i));

   }
   
   public static HashMap<String, Integer> scoreMatrix(File matrix) throws FileNotFoundException {
	  Scanner valScan, Fscan = new Scanner(matrix);
	  StringBuffer key = new StringBuffer("ab");
	  String S = Fscan.nextLine();
	  String vals = Fscan.nextLine();
          valScan = new Scanner(vals);

     for(int i = 0; i < S.length(); i++) {
    	 key.setCharAt(0, S.charAt(i));
    	 
    	 for(int j = 0; j < S.length(); j++) {
    		 key.setCharAt(1, S.charAt(j));
    		 
    		 if(!valScan.hasNext()) {
    			vals = Fscan.nextLine();
    			valScan = new Scanner(vals);
    		 } 
	    		 
    		 score.put(key.toString(), valScan.nextInt());
    	 }
     }
	  
	 return score;
   }

   public static ArrayList<Local> bestLocal(SubSln[][] slns, int s1len, int s2len) {
      int x = 0, y = 0, max = 0;
      ArrayList<Local> list = new ArrayList<Local>();

      for(int i = 0; i < s1len; i++) {
         for(int j = 0; j < s2len; j++) {
            if(slns[i][j].value > max) {
               max = slns[i][j].value;
               x = i;
               y = j;
            }
         }
      }

      for(int i = 0; i < s1len; i++) {
         for(int j = 0; j < s2len; j++) {
            if(slns[i][j].value == max) {
               list.add(new Local(i, j, max));
            }
         }
      }


      return list;
   }
}
