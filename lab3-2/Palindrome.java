import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Palindrome
{
   String seq;
   int min, max, minGap, maxGap;
   SuffixTree tree;
   ArrayList<String> palindromes;
   HashMap<String, ArrayList<Integer>> locations1;
   HashMap<String, ArrayList<Integer>> locations2;

   public Palindrome(String seq, int min, int max, int minGap, int maxGap)
   {
      this.seq = seq;
      this.min = min;
      this.max = max;
      this.minGap = minGap;
      this.maxGap = maxGap;

      // create tree
      this.tree = new SuffixTree();
      this.tree.addString(seq);
      System.out.println("Adding: " + seq);
      this.tree.addString(reverseComplement(seq));
      System.out.println("Adding: " + reverseComplement(seq));
      
      // create palindrome hashmap and locations hashmaps
      this.palindromes = new ArrayList<String>();
      this.locations1 = new HashMap<String, ArrayList<Integer>>();
      this.locations2 = new HashMap<String, ArrayList<Integer>>();
     
      // find palindromes 
      findPalindromes(tree.root, "");
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
               if (!(locations1.keySet().contains(s + n.label)))
               {
                  locations1.put(s + n.label, new ArrayList<Integer>());
                  locations2.put(s + n.label, new ArrayList<Integer>());
               }
               palindromes.add(s + n.label);
               locations1.get(s + n.label).add(((SuffixTree.Leaf)n).suffixNum.get(1));
               locations2.get(s + n.label).add(((SuffixTree.Leaf)n).suffixNum.get(2));
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


