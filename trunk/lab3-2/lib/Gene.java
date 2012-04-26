package lib;

import java.util.ArrayList;

public class Gene
{
   public String id;
   public ArrayList<Gene> points; 
   public int start;
   public int stop;
   public boolean order; 
   
   public Gene (String id, int start, int stop, boolean order)
   {
      this.id = id;
      points = new ArrayList<Gene>();
      this.start = start;
      this.stop = stop;
      this.order = order;
   } 
}
