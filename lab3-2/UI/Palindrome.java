package UI;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.lang.Math;
import java.util.Arrays;

public class Palindrome
{
   String seq;
   int min, max, minGap, maxGap, total;
   SuffixTree tree;
   ArrayList<String> palindromes;
   ArrayList<String> gapPalindromes;
   HashMap<String, ArrayList<Integer>> locations1;
   HashMap<String, ArrayList<Integer>> locations2;
   HashMap<String, ArrayList<Integer>> gapLocations1;
   HashMap<String, ArrayList<Integer>> gapLocations2;
   HashMap<String, ArrayList<SuffixTree.Leaf>> children;

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
      this.tree.addString(reverseComplement(seq));
      
      for (int i = 0; i < seq.length(); i++)
      {
         if (seq.charAt(i) != 'N')
         {
            total++;
         }
      }
      this.total = total;

      // create palindrome hashmap and locations hashmaps
      this.palindromes = new ArrayList<String>();
      this.locations1 = new HashMap<String, ArrayList<Integer>>();
      this.locations2 = new HashMap<String, ArrayList<Integer>>();
     
      // find palindromes 
      this.children = new HashMap<String, ArrayList<SuffixTree.Leaf>>();
      findPalindromes(tree.root, "");

      for (String s : palindromes)
      {
         ArrayList<SuffixTree.Leaf> c = children.get(s);
         
         for (int i = 0; i < c.size(); i++)
         {
            for (int j = 0; j < c.size(); j++)
            {
               HashMap<Integer, Integer> suffixNum1 = c.get(i).suffixNum;
               HashMap<Integer, Integer> suffixNum2 = c.get(j).suffixNum;
               ArrayList<Integer> keys1 = new ArrayList<Integer>(Arrays.asList(suffixNum1.keySet().toArray(new Integer[0])));
               ArrayList<Integer> keys2 = new ArrayList<Integer>(Arrays.asList(suffixNum2.keySet().toArray(new Integer[0])));
               if (c.get(i) == c.get(j))
               {
                  // perfect palindromes
                  if (keys1.size() == 2)
                  {
                     if (!(locations1.keySet().contains(s)))
                     {
                        locations1.put(s, new ArrayList<Integer>());
                        locations2.put(s, new ArrayList<Integer>());
                     }
                     if (!(locations1.get(s).contains(suffixNum1.get(1))
                          && locations1.get(s).contains(suffixNum1.get(2)))) 
                     {
                        locations1.get(s).add(suffixNum1.get(1));
                        locations2.get(s).add(total-suffixNum2.get(2));
                     }
                  }
               }
               for (int k = 0; k < keys1.size(); k++)
               {
                  for (int l = 0; l < keys2.size(); l++)
                  {
                     if (keys1.get(k) != keys2.get(l))
                     {
                        if (!(locations1.keySet().contains(s)))
                        {
                           locations1.put(s, new ArrayList<Integer>());
                           locations2.put(s, new ArrayList<Integer>());
                        }
                        if (!(locations1.get(s).contains(suffixNum1.get(keys1.get(k)))
                             && locations1.get(s).contains(suffixNum1.get(keys1.get(k))))) 
                        {
                           locations1.get(s).add(suffixNum1.get(keys1.get(k)));
                           locations2.get(s).add(total - suffixNum2.get(keys2.get(l)));
                        }
                     }
                  }
               }
            }
         }
      }
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
            int gap = Math.abs(locations1.get(s).get(i) - locations2.get(s).get(i)) - s.length();
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
            if (!children.keySet().contains(s))
            {
               children.put(s, new ArrayList<SuffixTree.Leaf>());
               palindromes.add(s);
            }
            if (!children.get(s).contains((SuffixTree.Leaf)n))
            {
               children.get(s).add((SuffixTree.Leaf)n);
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


