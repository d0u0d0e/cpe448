package lib;
import java.util.Arrays;
import java.util.HashMap;

public class TestBM {
    public static void main(String[] args) {
        String pass = "PASSED!";
        String fail = "FAILED!";
        boolean passed;
        StringBuffer bufErr = new StringBuffer();

        System.out.printf("ComputePi test %s\n", ((passed = testComputePi(bufErr)) ? pass: fail));
        if (!passed)
            System.out.println(bufErr);
        System.out.printf("ComputeR test %s\n", ((passed = testComputeR(bufErr)) ? pass: fail));
        System.out.printf("Boyer Moore test %s\n",
                ((passed = testBMrun(bufErr)) ? pass: fail));
        if (!passed)
            System.out.println(bufErr);
        System.out.printf("finding String Overlap test %s\n",
                ((passed = testGetOffset(bufErr)) ? pass: fail));
        if (!passed)
            System.out.println(bufErr);
    }

    public static boolean testComputePi(StringBuffer buf) {
        boolean test1, test2, test3;
        test1 = test2 = test3 = true;
        int[] actual, expected1 = {0,0,1,0,1,0,0,1,2,2,3},
                expected2 = {0,0},
                expected3 = {0,0,1,2,3,4,5,6,7,8,9};
        String p1 = "AATATCAAAT",
                p2 = "T",
                p3 = "CCCCCCCCCC";
        
        buf.delete(0, buf.length());
        actual = BM.computePi(p1);
        
        for (int i = 0; i < actual.length; i++)
            if (expected1[i] != actual[i])
                test1 = false;
        if (!test1)
            buf.append("Input: " + p1 + "\nExpected Output: " + Arrays.toString(expected1)
                    + "\nActual Output:   " + Arrays.toString(actual) + "\n");
            
        actual = BM.computePi(p2);
        if(actual[0] != 0 || actual[1] != 0)
            test2 = false;
        if (!test2)
            buf.append("Input: " + p2 + "\nExpected Output: " + Arrays.toString(expected2)
                    + "\nActual Output:   " + Arrays.toString(actual) + "\n");
        
        actual = BM.computePi(p3);
        for (int i = 0; i < actual.length; i++)
            if (expected3[i] != actual[i])
                test3 = false;
        if (!test3)
            buf.append("Input: " + p3 + "\nExpected Output: " + Arrays.toString(expected3)
                    + "\nActual Output:   " + Arrays.toString(actual) + "\n");
        
        return test1 && test2 && test3;
    }
    
    public static boolean testComputeR(StringBuffer buf) {
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
    
    public static boolean testGoodSuffix(StringBuffer buf) {
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
        
        buf.delete(0, buf.length());
        
        actual = BM.GoodSuffix(p1);
        for (int i = 0; i < actual.length; i++)
            if (expected1[i] != actual[i])
                test1 = false;
        if (!test1)
            buf.append("Input: " + p1 + "\nExpected Output: " + Arrays.toString(expected1)
                    + "\nActual Output:   " + Arrays.toString(actual) + "\n");
        
        actual = BM.GoodSuffix(p2);
        for (int i = 0; i < actual.length; i++)
            if (expected2[i] != actual[i])
                test2 = false;
        if (!test2)
            buf.append("Input: " + p2 + "\nExpected Output: " + Arrays.toString(expected2)
                    + "\nActual Output:   " + Arrays.toString(actual) + "\n");
        
        actual = BM.GoodSuffix(p3);
        for (int i = 0; i < actual.length; i++)
            if (expected3[i] != actual[i])
                test3 = false;
        if (!test3)
            buf.append("Input: " + p3 + "\nExpected Output: " + Arrays.toString(expected3)
                    + "\nActual Output:   " + Arrays.toString(actual) + "\n");

        actual = BM.GoodSuffix(p4);
        for (int i = 0; i < actual.length; i++)
            if (expected4[i] != actual[i])
                test4 = false;
        if (!test4)
            buf.append("Input: " + p4 + "\nExpected Output: " + Arrays.toString(expected4)
                    + "\nActual Output:   " + Arrays.toString(actual) + "\n");

        return test1 && test2 && test3 && test4;
    }
    
    public static boolean testBMrun(StringBuffer buf){
        boolean test1,test2,test3;
        int pos;
        String  s1 = "ACGATATCAGCAGTGTGTTTGACGAT",
                p1 = "TTGACGAT",
                s2 = "AAAAAAAA",
                p2 = "BB",
                s3 = "ACGAGAGTAAG",
                p3 = "TAAGCTG";
        
        test1 = test2 = test3 = true;
        buf.delete(0, buf.length());
        
        if ((pos = BM.BMrun(s1,p1)) != s1.length() - 1)
            test1 = false;
        if (!test1)
            buf.append("\nTest1:\nExpected: " + (s1.length()-1) + "\nActual: " + pos);
        
        if ((pos = BM.BMrun(s2,p2)) != s2.length())
            test2 = false;
        if (!test2)
            buf.append("\nTest2:\nExpected: " + (s2.length()) + "\nActual: " + pos);
        
        if ((pos = BM.BMrun(s3,p3)) != s3.length() - 4)
            test3 = false;
        if (!test3)
            buf.append("\nTest3:\nExpected: " + (s3.length()-4) + "\nActual: " + pos);
        
        return test1 && test2 && test3;
    }
    
    public static boolean testGetOffset(StringBuffer buf){
        boolean test1,test2,test3,test4,test5;
        int pos;
        String  s1 = "ACGATATCAGCAGTGTGTTTGACGAT",
                p1 = "TTGACGAT",
                s2 = "AAAAAAAA",
                p2 = "BB",
                s3 = "ACGAGNANGTAAG",
                p3 = "TAAGCTG";
        
        test1 = test2 = test3 = test4 = test5 = true;
        buf.delete(0, buf.length());
        
        if ((pos = BioOverlap.getOffset(s1,p1, 4)) != p1.length())
            test1 = false;
        if (!test1)
            buf.append("\nTest1:\nExpected: " + p1.length() + "\nActual: " + pos);
        if ((pos = BioOverlap.getOffset(s1,p1, 14)) != -1)
            test2 = false;
        if (!test2)
            buf.append("\nTest2:\nExpected: -1\nActual: " + pos);
        if ((pos = BioOverlap.getOffset(s2,p2, 0)) != 0)
            test3 = false;
        if (!test3)
            buf.append("\nTest3:\nExpected: 0\nActual: " + pos);
        if ((pos = BioOverlap.getOffset(s2,s2, 6)) != s2.length())
            test4 = false;
        if (!test4)
            buf.append("\nTest4:\nExpected: " + s2.length() + "\nActual: " + pos);
        if ((pos = BioOverlap.getOffset(s3,p3, 1)) != 4)
            test5 = false;
        if (!test5)
            buf.append("\nTest5:\nExpected: 4\nActual: " + pos);
        
        return test1 && test2 && test3 && test4 && test5;
    }
    
}
