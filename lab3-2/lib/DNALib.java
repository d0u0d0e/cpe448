package lib;
import java.lang.StringBuffer; 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Collections;
import java.util.Iterator;

public class DNALib
{
   private int error;
   private String seq;
   public String protein;
   private StringBuffer sequence;
   private ArrayList<Gene> geneList;
   Map<String, aminoAcid> map = new HashMap<String, aminoAcid>(64);
   Map<String, String> pmap = new HashMap<String, String>(64);
	
   class aminoAcid {
      String name;
      ArrayList<Codon> codons;
		
      private aminoAcid(String name, int k){
         this.name = name;
         codons = new ArrayList<Codon>(k);
      }
   }
	
   class Codon {
      String name;
      int count;
		
      private Codon(String seq){
         this.name = seq;
         count = 0;
      }
   }
   
   public DNALib(String seq) {
      this.seq = seq;
      this.sequence = new StringBuffer(seq);
      this.createMap();
      this.protein = toProtein();
      this.counts();
   }

   public DNALib(String seq, ArrayList<Gene> geneList)
   {
      this.seq = seq;
      this.geneList = geneList;
      this.sequence = new StringBuffer(seq);
      this.createMap();
      this.counts();
   }

   // BASIC FUNCTIONALITY
   public double GCContent (int start, int stop)
   {
      int count = 0, total = 0;
      for (int i = start; i <= stop; i++)
      {
         if (i >= seq.length())
            break;
         if (seq.charAt(i) == 'C' || seq.charAt(i) == 'G')
            count++;
         total++;
      }
      return ((double)count / (double)total) * 100;
   } 

   String complement (int start, int stop)
   {
      StringBuffer seqBuffer = new StringBuffer();
      for (int i = start; i < stop; i++)
      {
         switch(seq.charAt(i))
         {
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

   String reverse (int start, int stop)
   {
      String comp = complement(start, stop);
      StringBuffer buff = new StringBuffer(comp);
      return buff.reverse().toString();
   }

   private void mapAdd(String name, aminoAcid aa) {
      this.map.put(name, aa);
      for(int i = 0; i < aa.codons.size(); i++){
         this.pmap.put(aa.codons.get(i).name, name);	
         map.put(aa.codons.get(i).name, aa);
      }
   }
	
	private void createMap(){
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
      aminoAcid aa;
	
      while(end <= sequence.length()) {
         sub = sequence.substring(start, end);
         aa = map.get(sub);

         try {
            for(int i = 0; i < aa.codons.size(); i++)
               if(aa.codons.get(i).name.equals(sub))
                  aa.codons.get(i).count++;
         }
         catch (Exception e) {
            this.error++;
         }

         start += 3;
	 end += 3;
      }
   }

   public String toProtein()
   {
      int start = 0, end = 3;
      String sub;
      String protein = "";

      while(end <= seq.length()) 
      {
         sub = seq.substring(start, end);
         protein = protein + this.pmap.get(sub);
         start += 3;
	      end += 3;
      }
      return protein;
   }

   public void histogram(String amino){
      int total = 0;
      aminoAcid aa = map.get(amino);
      for(int i = 0; i < aa.codons.size(); i++)
         total += aa.codons.get(i).count;

      for(int i = 0; i < aa.codons.size(); i++)
         System.out.println(aa.codons.get(i).name + "   " + aa.codons.get(i).count + "   "
                            + (double)aa.codons.get(i).count / total);

   }


   public double frequencyOptimalCodons(ArrayList<String> optimal){
	aminoAcid aa;
	double sum = 0, opt = 0;
		
	for(int i = 0; i < optimal.size(); i++) {
	   aa = map.get(optimal.get(i));
		   
	   for(int j = 0; j < aa.codons.size(); j++) {
	      sum += aa.codons.get(j).count;
	      
	      if(aa.codons.get(j).name.equals(optimal.get(i)))
	         opt += aa.codons.get(j).count;
	   }
	}
        return opt / sum;
   }

	
   public double rcsu(String codon){
	double sum = 0, count = 0;
	DNALib.aminoAcid aa = map.get(codon);
	   
	for(int i = 0; i < aa.codons.size(); i++) {
	   sum += aa.codons.get(i).count;
		   
	   if(aa.codons.get(i).name.equals(codon))
	   count = aa.codons.get(i).count;
	}
	   
      return (count)/(sum / aa.codons.size());
   }
	

   public double CAI(String codon){
	double max = 0, cai = 1;
		
	for(aminoAcid aa : map.values())
	   for(Codon c : aa.codons)
	      if(c.count > max)
	         max = rcsu(c.name);
		
	for(aminoAcid aa : map.values())
           if(!aa.name.equals("Methionine") && !aa.name.equals("Tryptophane") && !aa.name.equals("Stop"))
	      for(Codon c : aa.codons)
	         cai *= rcsu(c.name) / max;
		 
	 return nRoot(59, cai, 0.0000000001);
   }

	
   public double effectiveNumberCodons() {
      double F2, F3, F4, F6, Sa, Fval;
      int sum;
      F2 = F3 = F4 = F6 = 0;
      Iterator iter = map.keySet().iterator();
      while(iter.hasNext()) {
         String s = (String)iter.next();

         if(s.length() == 1) {
            aminoAcid aa = map.get(s);

         sum = 0;
         Sa = 0;
         for(int i = 0; i < aa.codons.size(); i++) {
            sum += aa.codons.get(i).count;
         }

         if(sum > 1) {
            for(int i = 0; i < aa.codons.size(); i++)
               Sa += (aa.codons.get(i).count / sum) * (aa.codons.get(i).count / sum);

            Fval = (sum * Sa - 1) / (sum - 1);
         }
         else {
            Fval = 1;
         }

         switch(aa.codons.size()) {
            case(2):
               F2 += Fval;
               break;
            case(3):
               if(!aa.name.equals("Stop"))
                  F3 += Fval;
               break;
            case(4):
               F4 += Fval;
               break;
            case(6):
               F6 += Fval;
               break;
         }
        }
      }

         F2 = F2 / 9;
         F4 = F4 / 5;
         F6 = F6 / 3;
	         
         return 2 + 9/F2 + 1/F3 + 5/F4 + 3/F6;
   }
	
   public double scaledChi2(String amino){
	double chi = 0, E = 0, total = seq.length()/3;
	aminoAcid aa = map.get(amino);
		
	for(Codon c : aa.codons)
		E += c.count;

	E = (double)E/aa.codons.size();

	// E == expected number of codons w/o bias
	for(Codon c : aa.codons){
	   chi += (((double)c.count - E) * ((double)c.count - E)) / E;
	}

	total -= map.get("M").codons.get(0).count;
	total -= map.get("W").codons.get(0).count;

	return chi/total;
   }
	
   public double enthropy(){
      double A, T, C, G;
      int L = sequence.length();
      A = T = C = G = 0;

        for(int i = 0; i < L; i++){
           char c = sequence.charAt(i);
        	
           switch(c){
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
        
        return -(A/L*log2(A/L) + C/L*log2(C/L) + T/L*log2(T/L) + G/L*log2(G/L));	
   }
	

//supporting methods
	public static double log2(double num){
		return Math.log(num)/Math.log(2);
	}
	
	public static double nRoot(int n, double num, double epsilon)
	{
                    //if you weren't sure, epsilon is the precision
		int ctr = 0;
		double root = 1;
		if(n <= 0)
			return Double.NaN;
		if(num == 0) //this step is just to reduce the needed iterations
			return 0;
		while((Math.abs(Math.pow(root, n) - num) > epsilon) && (ctr++ < 1000)) //checks if the number is good enough
		{
			root = ((1.0/n)*(((n-1.0)*root)+(num/Math.pow(root, n-1.0))));
		}
		return root;
	}
	
	//counts instances of substring.
	public static int count(final String string, final String substring)
	  {
	     int count = 0;
	     int idx = 0;

	     while ((idx = string.indexOf(substring, idx)) != -1)
	     {
	        idx++;
	        count++;
	     }

	     return count;
	  }

   // IN-DEPTH ANALYSIS
   public String overlap(int window, int slide) {
      StringBuffer buf = new StringBuffer();
      int end;
      for (int i = 0; i < seq.length(); i+=slide)
      {
         if ((end = i + window - 1) >= seq.length())
            end = seq.length() - 1;
         buf.append(String.format("%d, %d, %.1f\n", i + 1, end + 1, GCContent(i, end)));
      }
      System.out.print(buf);
      return buf.toString();
   }
   
   public String nonoverlap(int window) {
      StringBuffer buf = new StringBuffer();
      int end;
      for (int i = 0; i < seq.length(); i+=window)
      { 
         if ((end = i + window - 1) >= seq.length())
            end = seq.length() - 1;
         buf.append(String.format("%d, %d, %.1f\n", i + 1, end + 1, GCContent(i, end)));
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
      for (Gene g : geneList)
      {
         if (!g.id.equals("Intergenic"))
         {
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
      try {
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
      
      } catch (Exception e) {
          System.out.println(e);
      }
      
      System.out.print(buf);
      return buf.toString();

   }

   public void getError()
   {
      System.out.println(this.error);
   }   
}
