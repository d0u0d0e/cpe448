import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Palindrome
{
   String seq;
   int min, max, gap;
   SuffixTree tree;
   ArrayList<String> palindromes;

   public Palindrome(String seq, int min, int max, int gap)
   {
      this.seq = seq;
      this.min = min;
      this.max = max;
      this.gap = gap;

      // create tree
      this.tree = new SuffixTree();
      this.tree.addString(seq);
      this.tree.addString(reverseComplement(seq));
      
      // create repeats hashmap and unexpected arraylist
      this.palindromes = new ArrayList<String>();
   }

   public void findPalindromes(SuffixTree.node root, String s)
   {
      for (SuffixTree.node n : root.children.values())
      {
         if (n instanceof SuffixTree.Inner)
         {
            String concat = s + n.label;
            if (concat.length() >= min && concat.length() <= max)
            {
               checkChildren(n, concat);
            }
            findPalindromes(n, concat);
         }
      }
   }

   public void checkChildren(SuffixTree.node root, String s)
   {
      for (SuffixTree.node n : root.children.values())
      {
         if (n instanceof SuffixTree.Inner)
         {
            checkChildren(n, s);
         }
         else
         {
            if (((SuffixTree.Leaf)n).suffixNum.size() == 2)
            {
               palindromes.add(s + n.label);
            }
            else
            {
               System.out.println("debug suffixNum: " + ((SuffixTree.Leaf)n).suffixNum.size());
            }
         }
      }
      return;
   }
   
   public String reverseComplement(String s) 
   {
      StringBuffer seqBuffer = new StringBuffer();
      for (int i = 0; i < s.length(); i++) {
         switch (seq.charAt(i)) {
            case 'A':
               seqBuffer.append("T");
               break;
            case 'T':
               seqBuffer.append("A");
               break;
            case 'G':
               seqBuffer.append("C");
               break;
            case 'C':
               seqBuffer.append("G");
               break;
         }
      }
      return seqBuffer.reverse().toString();
    }
}


