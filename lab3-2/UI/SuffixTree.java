package UI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class SuffixTree {

   public class node {
      public node prev;
      public char prevChar;
      public String label;
      public HashMap<Character, node> children;
   }
	
   class Inner extends node {
      public boolean ldiverse;        //left diversity
                                     //prevChar - if children are not left diverse, character is passed up to parent
      public Inner(String label, node n) {
         this.label = label;
         children = new HashMap<Character, node>();
         prev = n;
      }
   }

   class Leaf extends node {
      HashMap<Integer, Integer> suffixNum;

      public Leaf(String label, int num, node n) {
         this.label = label;
         children = new HashMap<Character, node>();
         suffixNum = new HashMap<Integer, Integer>();
         suffixNum.put(new Integer(stringNum), new Integer(num));
         prev = n;

         leaves.add(this);
      }
   }
   
   ArrayList<Leaf> leaves = new ArrayList<Leaf>();
   Inner root;
   int stringNum;

   //creates empty tree
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

      traverse(root);
   }

 //finds location to insert suffix
   public void insert(String sub, node n, int suffNum, char prevChar) {

      node current = n;
      boolean inserted = false;

      while(!inserted) {
         if(current.children.containsKey(sub.charAt(0))) {  //current node contains character
            node child = current.children.get(sub.charAt(0));
            int i = 0;

            while(i < sub.length() && i < child.label.length() && sub.charAt(i) == child.label.charAt(i))
               i++;

            if(i < sub.length()) {
               if(i == child.label.length()) {              //complete match with inner node
                  if(child.children.containsKey(sub.charAt(i))) {           //follow path down
                     current = child;
                     sub = sub.substring(i);
                  }
                  else {               //remaining substring becomes leaf
                     Leaf l = new Leaf(sub.substring(i), suffNum, current);
                     l.prevChar = prevChar;

                     child.children.put(l.label.charAt(0), l);

                     inserted = true;
                  }
               }
               else { //common sequence splits to 2
                  Inner in = new Inner(sub.substring(0, i), current);        //create new inner node
                  Leaf l = new Leaf(sub.substring(i), suffNum, current);
                  l.prevChar = prevChar;

                  child.label = child.label.substring(i);

                  in.children.put(l.label.charAt(0), l);          // add new leaf and child as child of new inner node
                  in.children.put(child.label.charAt(0), child);

                  current.children.put(sub.charAt(0), in);   //insert inner node as child of prev node

                  inserted = true;
               }
            }
            else {   //complete match, add to list of suffixes
               ((Leaf)child).suffixNum.put(new Integer(stringNum), new Integer(suffNum));
               inserted = true;
            }

         }
         else {       //no path to follow, create leaf
            Leaf l = new Leaf(sub, suffNum, current);
            l.prevChar = prevChar;

            current.children.put(sub.charAt(0), l);

            inserted = true;
         }
      }
   }

   public void traverse(Inner in) {
      for(node n : in.children.values()) {
         if(n instanceof Inner) {
            if(!((Inner)n).ldiverse)
               traverse((Inner)n);

            if(((Inner)n).ldiverse)
               in.ldiverse = true;
         }
      }
	   
      if(!in.ldiverse) {
         Iterator<node> iter = in.children.values().iterator();
	      
         if(iter.hasNext()) {
            in.prevChar = iter.next().prevChar;
	      
            while(iter.hasNext())
               if(in.prevChar != iter.next().prevChar)
                  in.ldiverse = true;
	 }
      }
   }
   
   public node lca(node n1, node n2) {
	   node ancestor = null, prev;
	   ArrayList<node> ancestors = new ArrayList<node>();
	   
	   prev = n1;
	   while(prev != null) {
		   ancestors.add(prev);
		   prev = prev.prev;
	   }
	   
	   prev = n2;
	   while(ancestor == null) {
	      if(ancestors.contains(prev))
		   ancestor = prev;

              prev = prev.prev;

           }
	   
	   return ancestor;
   }
   
   public String lce(String s1, String s2) {
	   node common = lca(findLeaf(s1), findLeaf(s2));
	   StringBuffer ext = new StringBuffer();

	   while(common.label != "root"){
		   ext.insert(0,  common.label);
		   common = common.prev;
	   }
	   
	   return ext.toString();
   }
   
   public node findLeaf(String s){
	   int ndx = 0, i;
	   node n = root;
	   s = s.concat("$");

	   while(ndx < s.length()) {
 	      if(n.children.containsKey(s.charAt(ndx))) {
		   n = n.children.get(s.charAt(ndx));
		   i = 0;
		   while(ndx < s.length() && i < n.label.length() && s.charAt(ndx) == n.label.charAt(i)) {
	              i++;
	              ndx++;
	           }
	      }
              else {
                 return n;
              }
           }

	   return n;
   }

   public void append(node n, String edge, String leaf) {
	  Inner in = new Inner(edge, n);
	  Leaf l = new Leaf(leaf, 0, in);
	  
	  in.children.put(leaf.charAt(0), l);
	  
      if(n.children.containsKey((edge.charAt(0)))) {
    	  //throw error?
      }
      else {
    	  n.children.put(edge.charAt(0), in);
      }
   }
   
   public void split(String edge){
       //how will edge be split?
   }

   public void findMatch(node n, String sub) {
      boolean done = false;
      node current = n;

      while(!done) {
         if(current.children.containsKey(sub.charAt(0))) {  //current node contains character
             node child = current.children.get(sub.charAt(0));
             int i = 0;

             while(i < sub.length() && i < child.label.length() && sub.charAt(i) == child.label.charAt(i))
                i++;

             if(i < sub.length()) {
                if(i == child.label.length()) {              //complete match with inner node
                   if(child.children.containsKey(sub.charAt(i))) {           //follow path down
                      current = child;
                      sub = sub.substring(i);
                   }
                   else {        // no match for remaining string
                      System.out.println("no match");
                      done = true;
                   }
                }
                else { //common sequence splits to 2
                   System.out.println("no match");
                   done = true;
                }
             }
             else {   //complete match, add to list of suffixes
                matchedLeaves(current);
                done = true;
             }

          }
          else {       //no path to follow
             System.out.println("no match");
             done = true;
          }
       }
   }
   
   public void matchedLeaves(node n) {
	   for(node nd : n.children.values()) {
		   if(nd instanceof Inner)
			   matchedLeaves(nd);
		   else if(nd instanceof Leaf) {
                      for(Integer i : ((Leaf)nd).suffixNum.keySet())
		         System.out.println("found: string = " + i + "   suffix # = " + 
((Leaf)nd).suffixNum.get(i));
                   }
	   }
   }
}
