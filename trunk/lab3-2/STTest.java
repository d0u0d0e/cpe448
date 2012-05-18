public class STTest {
   public static void main(String[] args) {
//create empty tree
	System.out.println("Creating empty tree...");
	SuffixTree tree = new SuffixTree();
        System.out.printf("%s : ", "Children of root");
        System.out.println(tree.root.children.keySet());

System.out.println();
//add multiple strings
        System.out.println("Adding 'abcde' ...");
        tree.addString("abcde");
        System.out.printf("%s : ", "Children of root");
        System.out.println(tree.root.children.keySet());

	System.out.println("Adding 'ATAT' ...");
	tree.addString("ATAT");
        System.out.printf("%s : ", "Children of root");
        System.out.println(tree.root.children.keySet());

        System.out.println("Adding 'TATT' ...");
        tree.addString("TATT");
        System.out.printf("%s : ", "Children of root");
        System.out.println(tree.root.children.keySet());

System.out.println();
//left diversity
        System.out.println("Left diversity of root:");
        if(tree.root.ldiverse)
           System.out.println("root is left diverse");
        else
           System.out.println("root is not left diverse");

        for(SuffixTree.node nd : tree.root.children.values()) {
           if(nd instanceof SuffixTree.Leaf)
              System.out.println("Leaf - left char = " + nd.prevChar);
           else {
              if(((SuffixTree.Inner)nd).ldiverse)
                 System.out.println("Inner - is left diverse");
              else
                 System.out.println("Inner - not left diverse, left char = " +  nd.prevChar);
           }
        }

//lca
        SuffixTree.node n1 = tree.findLeaf("TATT");
        SuffixTree.node n2 = tree.findLeaf("TAT");
        
        System.out.printf("%s", "lca of 'TATT' and 'TAT': ");
        System.out.println(tree.lca(n1, n2).label);

        n2 = tree.findLeaf("cde");
        System.out.printf("%s", "lca of 'TATT' and 'cde': ");
        System.out.println(tree.lca(n1, n2).label);

System.out.println();
//lce
        System.out.printf("%s", "lce of 'TATT' and 'TAT': ");
        System.out.println(tree.lce("TATT", "TAT"));

        System.out.printf("%s", "lce of 'TATT' and 'cde': ");
        System.out.println(tree.lce("TATT", "cde"));

System.out.println();
//string matching
        System.out.println("String match for 'TATT':");
        tree.findMatch("TATT");

        System.out.println("\nString match for 'A':"); 
        tree.findMatch("A");


   }
}
