
import java.util.HashMap;

public class TestBM {
    public static void main(String[] args) {
        String pass = "PASSED!";
        String fail = "FAILED!";

        System.out.printf("ComputePi test %s\n", (testComputePi() ? pass: fail));
        System.out.printf("ComputeR test %s\n", (testComputeR() ? pass: fail));
        System.out.printf("GoodSuffix test %s\n", (testGoodSuffix() ? pass: fail));
    }

    public static boolean testComputePi() {
        boolean test1, test2, test3;
        test1 = test2 = test3 = true;
        int[] actual, expected1 = {0,0,1,0,1,0,0,1,2,2,3},
                expected2 = {0,0,1,2,3,4,5,6,7,8,9};
        actual = BM.computePi("AATATCAAAT");
        
        for (int i = 0; i < actual.length; i++)
            if (expected1[i] != actual[i])
                test1 = false;
        
        actual = BM.computePi("T");
        if(actual[0] != 0 || actual[1] != 0)
            test2 = false;
        
        
        actual = BM.computePi("CCCCCCCCCC");
        for (int i = 0; i < actual.length; i++)
            if (expected2[i] != actual[i])
                test3 = false;
        
        return test1 && test2 && test3;
    }
    
    public static boolean testComputeR() {
        boolean test1, test2, test3, test4;
        test1 = test2 = test3 = test4 = true;
        HashMap rval;
        String p1 = "ACAACAT",
               p2 = "ABBABAB",
               p3 = "CAACCCCC",
               p4 = "AAAAAAA";
        
        rval = BM.computeR(p1);
        if (!rval.containsKey('A') || !((Integer)rval.get('A')).equals(5))
            test1 = false;
        if (!rval.containsKey('C') || !((Integer)rval.get('C')).equals(4))
            test1 = false;
        if (!rval.containsKey('T') || !((Integer)rval.get('T')).equals(6))
            test1 = false;
        
        rval = BM.computeR(p2);
        if (!rval.containsKey('A') || !((Integer)rval.get('A')).equals(5))
            test2 = false;
        if (!rval.containsKey('B') || !((Integer)rval.get('B')).equals(6))
            test2 = false;
        
        rval = BM.computeR(p3);
        if (!rval.containsKey('A') || !((Integer)rval.get('A')).equals(2))
            test3 = false;
        if (!rval.containsKey('C') || !((Integer)rval.get('C')).equals(7))
            test3 = false;
        
        rval = BM.computeR(p4);
        if (!rval.containsKey('A') || !((Integer)rval.get('A')).equals(6))
            test4 = false;
        
        return test1 && test2 && test3 && test4;
    }
    
    public static boolean testGoodSuffix() {
        boolean test1, test2, test3, test4;
        test1 = test2 = test3 = test4 = true;
        String p1 = "ACAACAACAT",
               p2 = "ABBABAB",
               p3 = "CAACCACAAC",
               p4 = "AAAAAAA";
        
        int[]  actual,
               expected1 = {10,10,10,10,10,10,10,10,10,10,1},
               expected2 = {5,5,5,5,2,5,4,1},
               expected3 = {6,6,6,6,6,6,6,9,3,5,1},
               expected4 = {1,1,2,3,4,5,6,1};
        
        actual = BM.GoodSuffix(p1);
        for (int i = 0; i < actual.length; i++)
            if (expected1[i] != actual[i])
                test1 = false;
         for (int i = 0; i < actual.length; i++)
            System.out.print(actual[i] + ", ");
         System.out.println();

         actual = BM.GoodSuffix(p2);
         for (int i = 0; i < actual.length; i++)
            if (expected2[i] != actual[i])
                test2 = false;
         for (int i = 0; i < actual.length; i++)
            System.out.print(actual[i] + ", ");
         System.out.println();

        actual = BM.GoodSuffix(p3);
        for (int i = 0; i < actual.length; i++)
            if (expected3[i] != actual[i])
                test3 = false;
         for (int i = 0; i < actual.length; i++)
             System.out.print(actual[i] + ", ");
         System.out.println();

        actual = BM.GoodSuffix(p4);
        for (int i = 0; i < actual.length; i++)
            if (expected4[i] != actual[i])
                test4 = false;
         for (int i = 0; i < actual.length; i++)
             System.out.print(actual[i] + ", ");
         System.out.println();

        return test1 && test2 && test3 && test4;
    }
    
}
