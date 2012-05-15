import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.lang.Math;

public class Palindrome
{
   String seq;
   int min, max, minGap, maxGap;
   SuffixTree tree;
   ArrayList<String> palindromes;
   ArrayList<String> gapPalindromes;
   HashMap<String, ArrayList<Integer>> locations1;
   HashMap<String, ArrayList<Integer>> locations2;
   HashMap<String, ArrayList<Integer>> gapLocations1;
   HashMap<String, ArrayList<Integer>> gapLocations2;

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

      // gaps
      this.gapPalindromes = new ArrayList<String>();
      this.gapLocations1 = new HashMap<String, ArrayList<Integer>>();
      this.gapLocations2 = new HashMap<String, ArrayList<Integer>>();
      findGapPalindromes();
   }

   public void findGapPalindromes()
   {
      for (String s : palindromes)
      {
         for (int i = 0; i < locations1.get(s).size(); i++)
         {
            int gap = Math.abs(locations1.get(s).get(i) - locations2.get(s).get(i));
            if (gap >= minGap && gap <= maxGap)
            {
               if (!(gapLocations1.keySet().contains(s)))
               {
                  gapLocations1.put(s, new ArrayList<Integer>());
                  gapLocations2.put(s, new ArrayList<Integer>());
                  gapPalindromes.add(s);
               }
               gapLocations1.get(s).add(locations1.get(s).get(i)); 
               gapLocations2.get(s).add(locations2.get(s).get(i)); 
            }
         }
      }
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
               String p = s + n.label;
               p = p.substring(0, p.length()-1);
               if (!(locations1.keySet().contains(s + n.label)))
               {
                  locations1.put(p, new ArrayList<Integer>());
                  locations2.put(p, new ArrayList<Integer>());
               }
               palindromes.add(p);
               locations1.get(p).add(((SuffixTree.Leaf)n).suffixNum.get(1));
               locations2.get(p).add(((SuffixTree.Leaf)n).suffixNum.get(2));
            }
         }
      }
      return;
   }
   
   public static String reverseComplement(String s) 
   {
      StringBuffer seqBuffer = new StringBuffer();
      for (int i = 0; i < s.length(); i++) {
         switch (s.charAt(i)) {
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


