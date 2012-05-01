import java.util.ArrayList;
import java.util.HashMap;

public class SuffixTree {

   public class node {
      public node prev;
      public String label;
      public HashMap<Character, node> children;
   }
	
   class Inner extends node {
      public boolean ldiverse;        //left diversity

      public Inner(String label, node n) {
         this.label = label;
         children = new HashMap<Character, node>();
         prev = n;
      }
   }

   class Leaf extends node {
      ArrayList<Integer> suffixNum;
      char prevChar;

      public Leaf(String label, int num, node n) {
         this.label = label;
         children = new HashMap<Character, node>();
         suffixNum = new ArrayList<Integer>();
         suffixNum.add(0, null);
         suffixNum.add(stringNum, new Integer(num));
         prev = n;
      }
   }
   
   ArrayList<Leaf> leaves = new ArrayList<Leaf>();
   Inner root;
   int stringNum;

   public SuffixTree() {
      root = new Inner("root", null);
      stringNum = 0;
   }


   public void addString(String S) {
      int suffNum = 0;
      String sub = S.concat("$");
      stringNum++;
      char c = '\n';

      for(int i = 0; i < sub.length(); i++) {
         suffNum++;

         if(i > 0)
            c = S.charAt(i - 1);

         insert(sub.substring(i), root, suffNum, c);
      }
   }

//recursively finds location to insert suffix
   public void insert(String sub, node n, int suffNum, char c) {
      if(n.children.containsKey(sub.charAt(0))) {  //current node contains character
         node child = n.children.get(sub.charAt(0));
         int i = 0;

         while(i < sub.length() && i < child.label.length() && sub.charAt(i) == child.label.charAt(i))
            i++;

// i == position of mismatch
         if(i < child.label.length()) {
            String common = sub.substring(0, i);
            Inner in = new Inner(common, n);        //create new inner node

            Leaf l = new Leaf(sub.substring(i), suffNum, n);
            l.prevChar = c;

            child.label = child.label.substring(i);

            in.children.put(l.label.charAt(0), l);          // add new leaf and child as child$
            in.children.put(child.label.charAt(0), child);

            n.children.put(sub.charAt(0), in);   //insert inner node as child of prev node

         }
         else {  // current node does not contain 1st character
            insert(sub.substring(i), child, suffNum, c);
         }

      }
      else {  // suffix is a leaf
         Leaf l = new Leaf(sub, suffNum, n);
         l.prevChar = c;

         n.children.put(sub.charAt(0), l);
      }
   }

   public void repeat() {
   }

   public void palindrome() {
   }
}
