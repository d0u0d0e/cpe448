package lib;

import java.util.ArrayList;

public class Isoform
{
   public ArrayList<String> mRNA;
   public ArrayList<ArrayList<String>> CDS;

   public Isoform (ArrayList<String> mRNA)
   {
      this.mRNA = mRNA;
      this.CDS = new ArrayList<ArrayList<String>>();
   }
}
