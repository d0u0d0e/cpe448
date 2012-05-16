public class STTest {
   public static void main(String[] args) {
//create empty tree
	System.out.println("Creating empty tree...");
	SuffixTree tree = new SuffixTree();

System.out.println();
//add multiple strings
	System.out.println("Adding 'ATAT' ...");
	tree.addString("ATAT");

        System.out.println("Adding 'TATT' ...");
        tree.addString("TATT");

System.out.println();
//left diversity
        System.out.println("left diversity of root");
        if(tree.root.ldiverse)
           System.out.println("root is left diverse");
        else
           System.out.println("root is not left diverse");

        for(SuffixTree.node nd : tree.root.children.values()) {
           if(nd instanceof SuffixTree.Leaf)
              System.out.println("Leaf: left char = " + nd.prevChar);
           else {
              if(((SuffixTree.Inner)nd).ldiverse)
                 System.out.println("Inner: is left diverse");
              else
                 System.out.println("Inner: not left diverse, left char = " +  nd.prevChar);
           }
        }

System.out.println();
//lca
        SuffixTree.node n1 = tree.root.children.get('A').children.get('A');
        SuffixTree.node n2 = tree.root.children.get('A').children.get('T');
        
        System.out.println("lca of 'ATT$' and 'ATAT$' :");
        System.out.println(tree.lca(n1, n2).label);

System.out.println();
//lce
        System.out.println("lce of 'TATT' and 'TAT' :");
        System.out.println(tree.lce("TATT", "TAT"));

//string matching
        System.out.println("String match for 'ATT' :");
        tree.findMatch(tree.root, "ATT");
   }
}
