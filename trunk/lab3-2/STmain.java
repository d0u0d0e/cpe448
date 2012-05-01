import java.util.Scanner;

public class Main
{
   public static void main(String args[])
   {
      SuffixTree tree = new SuffixTree();
      Scanner scan = new Scanner(System.in);

      System.out.println("Enter string:");
      String S = scan.nextLine();
      tree.addString(S);

      for(SuffixTree.node n : tree.root.children.values()) {
         if(n instanceof SuffixTree.Leaf)
            System.out.println(tree.root.label + " : " + n.label);
         else {
            for(SuffixTree.node m : n.children.values())
               System.out.println(n.label + " : " + m.label);
         }
      }
   }
}


