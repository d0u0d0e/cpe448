import java.util.HashMap;

public class Main
{
   public static void main(String args[])
   {
      BM bm = new BM();
      
      int[] res = bm.GoodSuffix("LLO");
      for (int i : res)
         System.out.print(i + ", ");
      System.out.println("");
      bm.BMrun("BYEHELLO", "HELLO");
   }
}
