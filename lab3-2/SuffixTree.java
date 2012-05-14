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
      ArrayList<Integer> suffixNum;

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
   }

 //recursively finds location to insert suffix
   public void insert(String sub, node n, int suffNum, char c) {
      if(n.children.containsKey(sub.charAt(0))) {  //current node contains character
         node child = n.children.get(sub.charAt(0));
         int i = 0;

         while(i < sub.length() && i < child.label.length() && sub.charAt(i) == child.label.charAt(i))
            i++;

// i == position of mismatch
         if(i < sub.length()) { //insert branch
            if(child.children.containsKey((sub.charAt(i)))) {
               insert(sub.substring(i), child, suffNum, c);
            }
        	else {
        	   if(child instanceof Inner) {
        	      insert(sub.substring(i), child, suffNum, c);
        	   }
        	   else {
                  String common = sub.substring(0, i);
                  Inner in = new Inner(common, n);        //create new inner node

                  Leaf l = new Leaf(sub.substring(i), suffNum, n);
                  l.prevChar = c;

                  child.label = child.label.substring(i);

                  in.children.put(l.label.charAt(0), l);          // add new leaf and child as child$
                  in.children.put(child.label.charAt(0), child);

                  n.children.put(sub.charAt(0), in);   //insert inner node as child of prev node
        	   }
        	}
         }
         else {  // complete match
        	 ((Leaf)child).suffixNum.add(suffNum);
         }
      }
      else {  // suffix is a leaf
         Leaf l = new Leaf(sub, suffNum, n);
         l.prevChar = c;

         n.children.put(sub.charAt(0), l);
      }
      
      traverse(root);
   }
   
   public void traverse(Inner in) {
	   for(node n : in.children.values()) {
		   if(n instanceof Inner) {
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
	   
	   prev = n1.prev;
	   while(prev != null) {
		   ancestors.add(prev);
		   prev = prev.prev;
	   }
	   
	   prev = n2.prev;
	   while(ancestor == null)
		   if(ancestors.contains(prev))
			   ancestor = prev;
	   
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
	   
	   while(ndx < s.length()) {
		   if(!n.children.containsKey(s.charAt(ndx))) {
			   return n;
		   }
		   else {
			   n = n.children.get(s.charAt(ndx));
			   i = 0;
			   while(ndx < s.length() && ndx < n.label.length() && s.charAt(i) == n.label.charAt(i)) {
		            i++;
		            ndx++;
			   }
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

   public void findMatch(node n, String s) {
      if(n.children.containsKey(s.charAt(0))) {  //current node contains character
         node child = n.children.get(s.charAt(0));
         int i = 0;

         while(i < s.length() && i < child.label.length() && s.charAt(i) == child.label.charAt(i))
            i++;

// i == position of mismatch
         if(i < child.label.length()) {
            System.out.println("no match found");
         }
         else {  //string is completely matched
            matchedLeaves(child);
         }
      }
      else {  // no match
         System.out.println("no match found");
      }   
   }
   
   public void matchedLeaves(node n) {
	   for(node nd : n.children.values()) {
		   if(nd instanceof Inner)
			   matchedLeaves(nd);
		   else if(nd instanceof Leaf)
		      System.out.println();
	   }
   }
}