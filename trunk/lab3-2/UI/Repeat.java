package UI;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.lang.Math;
import lib.Gene;

public class Repeat
{
   String seq;
   int min, max, total, fold;
   SuffixTree tree;
   HashMap<Character, Double> probability;
   HashMap<String, Integer> repeats;
   ArrayList<Unexpected> unexpected;
   HashMap<String, ArrayList<Integer>> locations;

   public class Unexpected
   {
      String s;
      int size;
      int repeat;
      double selfAvgProximity;
      double selfStdProximity;
      double geneAvgProximity;
      double geneStdProximity;
      double freq;
      double expectedFreq;
      double percentFreq;

      public Unexpected(String s, int size, int repeat, double selfAvgProximity, double selfStdProximity, double geneAvgProximity, double geneStdProximity, double freq, double expectedFreq, double percentFreq)
      {
         this.s = s;
         this.size = size;
         this.repeat = repeat;
         this.selfAvgProximity = selfAvgProximity;
         this.selfStdProximity = selfStdProximity;
         this.geneAvgProximity = geneAvgProximity;
         this.geneStdProximity = geneStdProximity;
         this.freq = freq;
         this.expectedFreq = expectedFreq;
         this.percentFreq = percentFreq;
      }
   }

   public Repeat(String seq, int min, int max, int fold, ArrayList<Gene> geneList)
   {
      this.seq = seq;
      this.min = min;
      this.max = max;
      this.fold = fold;

      // get counts
      int total = 0;
      double A = 0.0, C = 0.0, T = 0.0, G = 0.0; 

      for (int i = 0; i < seq.length(); i++)
      {
         if (seq.charAt(i) != 'N')
            total++;
         if (seq.charAt(i) == 'A')
            A++;
         else if (seq.charAt(i) == 'T')
            T++;
         else if (seq.charAt(i) == 'C')
            C++;
         else if (seq.charAt(i) == 'G')
            G++;
      }
      this.total = total;

      // create probability hashmap and insert percentages
      this.probability = new HashMap<Character, Double>();
      this.probability.put('A', A / total);
      this.probability.put('T', T / total);
      this.probability.put('G', G / total);
      this.probability.put('C', C / total);
      
      // create tree
      this.tree = new SuffixTree();
      this.tree.addString(seq);

      // create repeats hashmap and unexpected arraylist
      this.repeats = new HashMap<String, Integer>();
      this.locations = new HashMap<String, ArrayList<Integer>>();
      this.unexpected = new ArrayList<Unexpected>();
      
      // find repeats
      findRepeats(tree.root, "");
      for (String s : repeats.keySet())
      {
         int repeat = repeats.get(s);
         try {
         double p = probability.get(s.charAt(0));
         
         
         for (int i = 1; i < s.length(); i++)
         {
            p *= probability.get(s.charAt(i));
         }
         
         if ((p * total * fold) <= repeat)
         {
            double selfAvgProximity = getSelfAvgProximity(s);  
            double selfStdProximity = getSelfStdProximity(s, selfAvgProximity);  
            double geneAvgProximity = getGeneAvgProximity(s, geneList);  
            double geneStdProximity = getGeneStdProximity(s, geneList, geneAvgProximity);  
            double freq = 1.0 / (total / (double)repeat);
            double expectedFreq = 1.0 / ((double)total / (p * total));
            double percentFreq = freq / expectedFreq * 100;
            Unexpected ue = new Unexpected(s, s.length(), repeat, selfAvgProximity, selfStdProximity, geneAvgProximity, geneStdProximity, freq, expectedFreq, percentFreq);
            unexpected.add(ue);
         }
         }
         catch (Exception e) {
             System.err.println(s.charAt(0));
         }
      }

      Collections.sort(unexpected, new Comparator<Unexpected>()
         {
            public int compare(Unexpected a, Unexpected b)
            {
               if (a.size != b.size)
               {
                  return a.size - b.size;
               }
               if (a.freq > b.freq)
               {
                  return 1; 
               }
               else if (a.freq < b.freq)
               {
                  return -1;
               }
               if (a.expectedFreq > b.expectedFreq)
               {
                  return 1; 
               }
               else if (a.expectedFreq < b.expectedFreq)
               {
                  return -1;
               }
               if (a.percentFreq > b.percentFreq)
               {
                  return 1;
               }
               if (a.percentFreq < b.expectedFreq)
               {
                  return -1;
               }
               return a.s.compareTo(b.s);
            }
         });
   }

   public double getSelfStdProximity(String s, double mean)
   {
      double sumDev = 0.0;         
      ArrayList<Integer> locs = locations.get(s);
      Collections.sort(locs, new Comparator<Integer>()
         {
            public int compare(Integer a, Integer b)
            {
               return b - a;
            }
         });
      for (int i = 0; i < locs.size() - 1; i++)
      {
         int l = locs.get(i) - locs.get(i+1) - s.length();
         sumDev += (l - mean) * (l - mean);
      }
      return Math.sqrt(sumDev / (locs.size() - 1));
   }

   public double getSelfAvgProximity(String s)
   {
      double sum = 0.0;
      ArrayList<Integer> locs = locations.get(s);
      Collections.sort(locs, new Comparator<Integer>()
         {
            public int compare(Integer a, Integer b)
            {
               return b - a;
            }
         });
      for (int i = 0; i < locs.size() - 1; i++)
      {
         sum += locs.get(i) - locs.get(i+1) - s.length();
      }

      return sum / (locs.size()-1);
   }

   public double getGeneStdProximity(String s, ArrayList<Gene> geneList, double mean)
   {
      double sumDev = 0.0;
      ArrayList<Integer> locs = locations.get(s);
      for (Integer i : locs)
      {
         int min = 0;
         for (Gene g : geneList)
         {
            if (!g.id.equals("Intergenic"))
            {
               int start = Math.abs(g.points.get(0).start - i);
               if (min == 0)
               {
                  min = start;
               }
               else if (min > start)
               {
                  min = start;
               }
            }
         }  
         sumDev += (min - mean) * (min - mean); 
      } 
      return Math.sqrt(sumDev / locs.size());
   }

   public double getGeneAvgProximity(String s, ArrayList<Gene> geneList)
   {
      double sum = 0.0;
      ArrayList<Integer> locs = locations.get(s);
      
      for (Integer i : locs)
      {
         int min = 0;
         for (Gene g : geneList)
         {
            if (!g.id.equals("Intergenic"))
            {
               int start;
               if (g.points.get(0).start > i)
               {
                  start = g.points.get(0).start - i - s.length();
               }
               else
               {
                  start = i - g.points.get(0).start;
               }
               
               if (min == 0)
               {
                  min = start;
               }
               else if (min > start)
               {
                  min = start;
               }
            }
         }   
         sum += min;
      } 
      return sum / locs.size();
   }
   
   public void findRepeats(SuffixTree.node root, String s)
   {
      for (SuffixTree.node n : root.children.values())
      {
         if (n instanceof SuffixTree.Inner)
         {
            String concat = s + n.label;
            if (concat.length() >= min && concat.length() <= max)
            {
               locations.put(concat, new ArrayList<Integer>());
               this.repeats.put(concat, countChildren(concat, n));
            }
            findRepeats(n, concat);
         }
      }
   }

   public int countChildren(String s, SuffixTree.node root)
   {
      int count = 0;

      for (SuffixTree.node n : root.children.values())
      {
         if (n instanceof SuffixTree.Inner)
         {
            count += countChildren(s, n);
         }
         else
         {
            count++;
            locations.get(s).add(((SuffixTree.Leaf)n).suffixNum.get(1));
         }
      }
      return count;
   }
}


