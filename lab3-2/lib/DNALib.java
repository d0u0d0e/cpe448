package lib;

import java.lang.StringBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Collections;

public class DNALib {

    private int error;
    private String seq;
    private StringBuffer sequence;
    private ArrayList<Gene> geneList;
    Map<String, aminoAcid> map = new HashMap<String, aminoAcid>(64);

    class aminoAcid {

        String name;
        ArrayList<Codon> codons;

        private aminoAcid(String name, int k) {
            this.name = name;
            codons = new ArrayList<Codon>(k);
        }
    }

    class Codon {

        String name;
        int count;

        private Codon(String seq) {
            this.name = seq;
            count = 0;
        }
    }
    
    public DNALib(String seq) {
        this.seq = seq;
    }

    public DNALib(String seq, ArrayList<Gene> geneList) {
        this.seq = seq;
        this.geneList = geneList;
        this.sequence = new StringBuffer(seq);
        this.createMap();
        this.counts();
    }

    // BASIC FUNCTIONALITY
    public double GCContent(int start, int stop) {
        int count = 0, total = 0;
        for (int i = start; i <= stop; i++) {
            if (i >= seq.length()) {
                break;
            }
            if (seq.charAt(i) == 'C' || seq.charAt(i) == 'G') {
                count++;
            }
            total++;
        }
        return ((double) count / (double) total) * 100;
    }

    String complement(int start, int stop) {
        StringBuffer seqBuffer = new StringBuffer();
        for (int i = start; i < stop; i++) {
            switch (seq.charAt(i)) {
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
        return seqBuffer.toString();
    }

    String reverse(int start, int stop) {
        String comp = complement(start, stop);
        StringBuffer buff = new StringBuffer(comp);
        return buff.reverse().toString();
    }

    private void mapAdd(String name, aminoAcid aa) {
        this.map.put(name, aa);

        for (int i = 0; i < aa.codons.size(); i++) {
            map.put(aa.codons.get(i).name, aa);
        }
    }

    //should amino acids be labeled by 1 or 3 letter name?
    private void createMap() {
        aminoAcid aa = new aminoAcid("Alanine", 4);
        aa.codons.add(new Codon("GCT"));
        aa.codons.add(new Codon("GCC"));
        aa.codons.add(new Codon("GCA"));
        aa.codons.add(new Codon("GCG"));
        mapAdd("A", aa);

        aa = new aminoAcid("Arginine", 6);
        aa.codons.add(new Codon("CGT"));
        aa.codons.add(new Codon("CGC"));
        aa.codons.add(new Codon("CGA"));
        aa.codons.add(new Codon("CGG"));
        aa.codons.add(new Codon("AGA"));
        aa.codons.add(new Codon("AGG"));
        mapAdd("R", aa);

        aa = new aminoAcid("Asparagine", 2);
        aa.codons.add(new Codon("AAT"));
        aa.codons.add(new Codon("AAC"));
        mapAdd("N", aa);

        aa = new aminoAcid("Aspartic acid", 2);
        aa.codons.add(new Codon("GAT"));
        aa.codons.add(new Codon("GAC"));
        mapAdd("D", aa);

        aa = new aminoAcid("Cysteine", 2);
        aa.codons.add(new Codon("TGT"));
        aa.codons.add(new Codon("TGC"));
        mapAdd("C", aa);

        aa = new aminoAcid("Glutamic acid", 2);
        aa.codons.add(new Codon("GAA"));
        aa.codons.add(new Codon("GAG"));
        mapAdd("E", aa);

        aa = new aminoAcid("Glutamine", 2);
        aa.codons.add(new Codon("CAA"));
        aa.codons.add(new Codon("CAG"));
        mapAdd("Q", aa);

        aa = new aminoAcid("Glycine", 4);
        aa.codons.add(new Codon("GGT"));
        aa.codons.add(new Codon("GGC"));
        aa.codons.add(new Codon("GGA"));
        aa.codons.add(new Codon("GGG"));
        mapAdd("G", aa);

        aa = new aminoAcid("Histidine", 2);
        aa.codons.add(new Codon("CAT"));
        aa.codons.add(new Codon("CAC"));
        mapAdd("H", aa);

        aa = new aminoAcid("Isoleucine", 3);
        aa.codons.add(new Codon("ATC"));
        aa.codons.add(new Codon("ATT"));
        aa.codons.add(new Codon("ATA"));
        mapAdd("I", aa);

        aa = new aminoAcid("Leucine", 6);
        aa.codons.add(new Codon("TTA"));
        aa.codons.add(new Codon("TTG"));
        aa.codons.add(new Codon("CTT"));
        aa.codons.add(new Codon("CTC"));
        aa.codons.add(new Codon("CTA"));
        aa.codons.add(new Codon("CTG"));
        mapAdd("L", aa);

        aa = new aminoAcid("Lysine", 2);
        aa.codons.add(new Codon("AAA"));
        aa.codons.add(new Codon("AAG"));
        mapAdd("K", aa);

        aa = new aminoAcid("Methionine", 1);
        aa.codons.add(new Codon("ATG"));
        mapAdd("M", aa);

        aa = new aminoAcid("Phenylalanine", 2);
        aa.codons.add(new Codon("TTT"));
        aa.codons.add(new Codon("TTC"));
        mapAdd("F", aa);

        aa = new aminoAcid("Proline", 4);
        aa.codons.add(new Codon("CCT"));
        aa.codons.add(new Codon("CCC"));
        aa.codons.add(new Codon("CCA"));
        aa.codons.add(new Codon("CCG"));
        mapAdd("P", aa);

        aa = new aminoAcid("Serine", 6);
        aa.codons.add(new Codon("TCT"));
        aa.codons.add(new Codon("TCC"));
        aa.codons.add(new Codon("TCA"));
        aa.codons.add(new Codon("TCG"));
        aa.codons.add(new Codon("AGT"));
        aa.codons.add(new Codon("AGC"));
        mapAdd("S", aa);

        aa = new aminoAcid("Threonine", 4);
        aa.codons.add(new Codon("ACT"));
        aa.codons.add(new Codon("ACC"));
        aa.codons.add(new Codon("ACA"));
        aa.codons.add(new Codon("ACG"));
        mapAdd("T", aa);

        aa = new aminoAcid("Tryptophan", 1);
        aa.codons.add(new Codon("TGG"));
        mapAdd("W", aa);

        aa = new aminoAcid("Tyrosine", 2);
        aa.codons.add(new Codon("TAT"));
        aa.codons.add(new Codon("TAC"));
        mapAdd("Y", aa);

        aa = new aminoAcid("Valine", 4);
        aa.codons.add(new Codon("GTT"));
        aa.codons.add(new Codon("GTC"));
        aa.codons.add(new Codon("GTA"));
        aa.codons.add(new Codon("GTG"));
        mapAdd("V", aa);

        aa = new aminoAcid("Stop", 3);
        aa.codons.add(new Codon("TAA"));
        aa.codons.add(new Codon("TGA"));
        aa.codons.add(new Codon("TAG"));
        mapAdd("Z", aa);
    }

    private void counts() {
        int start = 0, end = 3;
        String sub;

        while (end <= sequence.length()) {
            sub = sequence.substring(start, end);
            try {
                map.get(sub).codons.get(0).count++;
            } catch (Exception e) {
                this.error++;
            }
            start += 3;
            end += 3;
        }
    }

    public double frequencyOptimalCodons(ArrayList<String> optimal) {
        aminoAcid aa;
        double sum = 0, opt = 0;

        for (int i = 0; i < optimal.size(); i++) {
            aa = map.get(optimal.get(i));

            for (Codon c : aa.codons) {
                sum += c.count;

                if (c.name.equals(optimal.get(i))) {
                    opt += c.count;
                }
            }
        }

        return opt / sum;
    }

    public double rcsu(String codon) {
        double sum = 0, count = 0;
        DNALib.aminoAcid aa = map.get(codon);

        for (int i = 0; i < aa.codons.size(); i++) {
            sum += aa.codons.get(i).count;

            if (aa.codons.get(i).name.equals(codon)) {
                count = aa.codons.get(i).count;
            }
        }

        return (count) / (sum / aa.codons.size());
    }

    public double CAI(String codon) {
        double max = 0, cai = 1;
        String maxC = "ATG";

        for (aminoAcid aa : map.values()) {
            for (Codon c : aa.codons) {
                if (c.count > max) {
                    max = c.count;
                    maxC = c.name;
                }
            }
        }

        for (aminoAcid aa : map.values()) {
            for (Codon c : aa.codons) {
                cai *= rcsu(c.name) / rcsu(maxC);
            }
        }

        return nRoot(59, cai, 1);
    }

    public double effectiveNumberCodons() {
        double Sa, F2, F3, F4, F6, sum;
        double c2, c3, c4, c6;
        F2 = F3 = F4 = F6 = 0;
        c2 = c3 = c4 = c6 = 0;

        for (aminoAcid aa : map.values()) {
            sum = 0;
            Sa = 0;

            switch (aa.codons.size()) {
                case (2):
                    for (Codon c : aa.codons) {
                        sum += c.count;
                    }

                    if (sum > 0) {
                        c2++;
                    }

                    if (sum > 1) {
                        for (Codon c : aa.codons) {
                            Sa += (c.count / sum) * (c.count / sum);
                        }

                        F2 += (sum * Sa - 1) / (sum - 1);
                    }
                    break;
                case (3):
                    for (Codon c : aa.codons) {
                        sum += c.count;
                    }

                    if (sum > 0) {
                        c3++;
                    }

                    if (sum > 1) {
                        for (Codon c : aa.codons) {
                            Sa += (c.count / sum) * (c.count / sum);
                        }

                        F3 += (sum * Sa - 1) / (sum - 1);
                    }
                    break;
                case (4):
                    for (Codon c : aa.codons) {
                        sum += c.count;
                    }

                    if (sum > 0) {
                        c4++;
                    }
                    if (sum > 1) {
                        for (Codon c : aa.codons) {
                            Sa += (c.count / sum) * (c.count / sum);
                        }

                        F4 += (sum * Sa - 1) / (sum - 1);
                    }
                    break;
                case (6):
                    for (Codon c : aa.codons) {
                        sum += c.count;
                    }

                    if (sum > 0) {
                        c6++;
                    }

                    if (sum > 1) {
                        for (Codon c : aa.codons) {
                            Sa += (c.count / sum) * (c.count / sum);
                        }

                        F6 += (sum * Sa - 1) / (sum - 1);
                    }
                    break;
            }
        }

        if (c2 == 0) {
            c2 = 1;
        }
        if (c3 == 0) {
            c3 = 1;
        }
        if (c4 == 0) {
            c4 = 1;
        }
        if (c6 == 0) {
            c6 = 1;
        }

        F2 = F2 / c2;
        F3 = F3 / c3;
        F4 = F4 / c4;
        F6 = F6 / c6;

        if (F2 == 0) {
            F2 = 1;
        }
        if (F3 == 0) {
            F3 = 1;
        }
        if (F4 == 0) {
            F4 = 1;
        }
        if (F6 == 0) {
            F6 = 1;
        }

        return 2 + 9 / F2 + 1 / F3 + 5 / F4 + 3 / F6;
    }

    public double scaledChi2(String amino) {
        double chi = 0, E = 0, total = 0;
        aminoAcid aa = map.get(amino);

        for (Codon c : aa.codons) {
            E += c.count;
        }

        // E == num occurences of amino acid in seq
        for (Codon c : aa.codons) {
            chi += (((double) c.count - E) * ((double) c.count - E) / E);
        }

        for (DNALib.aminoAcid aA : map.values()) {
            if (aA.codons.size() != 1) {
                for (int i = 0; i < aA.codons.size(); i++) {
                    total += aA.codons.get(i).count;
                }
            }
        }

        return chi / total;
    }

    public double enthropy() {
        int A, T, C, G;
        int L = sequence.length();
        A = T = C = G = 0;

        for (int i = 0; i < L; i++) {
            char c = sequence.charAt(i);

            switch (c) {
                case 'A':
                    A++;
                    break;
                case 'T':
                    T++;
                    break;
                case 'C':
                    C++;
                    break;
                case 'G':
                    G++;
                    break;
            }
        }

        return -(A / L * log2(A / L) + C / L * log2(C / L) + T / L * log2(T / L) + G / L * log2(G / L));
    }

    public double codonDensity() {
        return (double) sequence.length() / 3;
    }

    public double codonDensity(String codon) {
        aminoAcid aa = map.get(codon);
        double num = 0;

        for (Codon c : aa.codons) {
            if (c.name.equals(codon)) {
                num = c.count;
            }
        }

        return num / sequence.length();
    }
    /*	
    
    public double geneDensity(){	
        int genes = 0;
    
        for(both strands)
            if(gene)
                genes++;
        
        return genes/L;
    }
     */

//supporting methods
    public static double log2(double num) {
        return Math.log(num) / Math.log(2);
    }

    public static double nRoot(int n, double num, double epsilon) {
        //if you weren't sure, epsilon is the precision
        int ctr = 0;
        double root = 1;
        if (n <= 0) {
            return Double.NaN;
        }
        if (num == 0) //this step is just to reduce the needed iterations
        {
            return 0;
        }
        while ((Math.abs(Math.pow(root, n) - num) > epsilon) && (ctr++ < 1000)) //checks if the number is good enough
        {
            root = ((1.0 / n) * (((n - 1.0) * root) + (num / Math.pow(root, n - 1.0))));
        }
        return root;
    }

    //counts instances of substring.
    public static int count(final String string, final String substring) {
        int count = 0;
        int idx = 0;

        while ((idx = string.indexOf(substring, idx)) != -1) {
            idx++;
            count++;
        }

        return count;
    }

    // IN-DEPTH ANALYSIS
    public String overlap(int window, int slide) {
        StringBuffer buf = new StringBuffer();
        int end;
        for (int i = 0; i < seq.length(); i += slide) {
            if ((end = i + window - 1) >= seq.length()) {
                end = seq.length() - 1;
            }
            //System.out.printf("%d, %d, %.1f\n", i + 1, end + 1, GCContent(i, end));
            buf.append((i + 1) + ", " + (end + 1) + ", " + String.format("%.1f", GCContent(i, end)) + "\n");
        }
        System.out.print(buf);
        return buf.toString();
    }

    public String nonoverlap(int window) {
        StringBuffer buf = new StringBuffer();
        int end;
        for (int i = 0; i < seq.length(); i += window) {
            if ((end = i + window - 1) >= seq.length()) {
                end = seq.length() - 1;
            }
            //System.out.printf("%d, %d, %.1f\n", i + 1, end + 1, GCContent(i, end));
            buf.append((i + 1) + ", " + (end + 1) + ", " + String.format("%.1f", GCContent(i, end)) + "\n");
        }
        System.out.print(buf);
        return buf.toString();
    }

    public String geneDensity() {
        int density = 0;
        for (Gene g : geneList) {
            if (!g.id.equals("Intergenic")) {
                density++;
            }
        }
        System.out.println(density);
        return String.valueOf(density);
    }

    public String geneGC() {
        StringBuffer buf = new StringBuffer();
        for (Gene g : geneList) {
            if (!g.id.equals("Intergenic")) {
                for (Gene p : g.points) {
                    buf.append(String.format("%s (%s), %d, %d, %.1f\n", g.id, p.id, p.start, p.stop, GCContent(p.start - 1, p.stop - 1)));
                }
            } else {
                buf.append(String.format("%s, %d, %d, %.1f\n", g.id, g.start, g.stop, GCContent(g.start - 1, g.stop - 1)));
            }
        }
        System.out.print(buf);
        return buf.toString();
    }

    public String geneSize() {
        StringBuffer buf = new StringBuffer();
        for (Gene g : geneList) {
            if (!g.id.equals("Intergenic")) {
                for (Gene p : g.points) {
                    buf.append(String.format("%s (%s), %d, %d, %d\n", g.id, p.id, p.start, p.stop, p.stop - p.start + 1));
                }
            } else {
                buf.append(String.format("%s, %d, %d, %d\n", g.id, g.start, g.stop, g.stop - g.start + 1));
            }
        }
        System.out.print(buf);
        return buf.toString();
    }

    public String codonFrequency() {
        StringBuffer buf = new StringBuffer();
        HashMap<String, Integer> freq = new HashMap<String, Integer>();
      HashMap<String, Integer> amino = new HashMap<String, Integer>();
      for (Gene g : geneList)
      {
         if (!g.id.equals("Intergenic"))
         {
            String geneSeq = "";
            if (g.order)
               geneSeq = seq.substring(g.start-1, (g.stop - 1) + 1);
            else
               geneSeq = reverse(g.start-1, g.stop - 1);
            for (int i = 0; i < geneSeq.length()-2; i+=3)
            {
               String codon = geneSeq.substring(i, i+3);
               if (freq.containsKey(codon))
               {
                  freq.put(codon, freq.get(codon) + 1);
               }
               else
               {
                  freq.put(codon, 1);
               }
            } 
         }
      }

      for (String s : freq.keySet())
      {
         String a = map.get(s).name;
         if (amino.containsKey(a))
         {
            amino.put(a, amino.get(a) + freq.get(s));
         }   
         else
         {
            amino.put(a, freq.get(s));
         }
      }

      for (String s : freq.keySet())
      {
         buf.append(String.format("%s, %s, %d, %.2f\n", s, map.get(s).name,
                 freq.get(s), (double)freq.get(s) / (double)amino.get(map.get(s).name)));
      }
        System.out.print(buf);
        return buf.toString();
    }

    public void getError() {
        System.out.println(this.error);
    }
}
