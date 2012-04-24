import java.util.HashMap;

public class Main
{
   public static void main(String args[])
   {
String S = "tgtagataagtacttacttacgtcga";
String P = "gtacttacttac";
   
BM.BMrun(S, P);

      BioBM bm = new BioBM();
      System.out.println(bm.BMrun("ATGATATTCGATCGAGCG", "TTT"));
   }
}
