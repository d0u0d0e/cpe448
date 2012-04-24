import java.util.HashMap;

public class Main
{
   public static void main(String args[])
   {
      BM.BMrun("tgtagataagtacttacttacgtcga", "gtacttacttac");
      BM.BMrun("HELLO", "LLO");

      System.out.println(BioBM.BMrun("ATGATATTCGATCGAGCG", "TTT"));
      System.out.println(BioBM.BMrun("HELLO", "ELLO"));
   }
}
