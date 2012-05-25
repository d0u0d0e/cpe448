package UI;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//package lab4.UI;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;
import java.util.StringTokenizer;
import lib.Gene;
import lib.DNALib;

/**
 *
 * @author Ricardo
 */
public class Controller {

    private static final String FILE_EXTENSION = ".csv";
    private MainWindow gui;
    private File[] fastaFileArr;
    private File[] gffFileArr;
    private int window;
    private int slide;
    private boolean contig;
    private ArrayList<String> fastaContigsAll;
    private ArrayList<String> gffContigsAll;
    private ArrayList<String> fastaFosmidsAll;
    private ArrayList<String> gffFosmidsAll;
    private ArrayList<String> fastaFiles;
    private ArrayList<String> gffFiles;

    public Controller() {
        window = 1000;
        slide = 100;
    }

    public void init() {
        gui = MainWindow.getMainFrame();
        gui.init(this);
        gui.showWindow();
        initFiles();
    }

    public void initFiles() {
        fastaContigsAll = new ArrayList<String>();
        gffContigsAll = new ArrayList<String>();
        fastaFosmidsAll = new ArrayList<String>();
        gffFosmidsAll = new ArrayList<String>();

        // contig files 20-38
        fastaContigsAll.add("fasta/contig20.txt");
        fastaContigsAll.add("fasta/contig21.txt");
        fastaContigsAll.add("fasta/contig22.txt");
        //MISSING: fastaFiles.add("fasta/contig23.txt");
        fastaContigsAll.add("fasta/contig24.txt");
        fastaContigsAll.add("fasta/contig25.txt");
        fastaContigsAll.add("fasta/contig26.txt");
        fastaContigsAll.add("fasta/contig27.txt");
        fastaContigsAll.add("fasta/contig28.txt");
        fastaContigsAll.add("fasta/contig29.txt");
        fastaContigsAll.add("fasta/contig30.txt");
        fastaContigsAll.add("fasta/contig31.txt");
        fastaContigsAll.add("fasta/contig32.txt");
        fastaContigsAll.add("fasta/contig33.txt");
        fastaContigsAll.add("fasta/contig34.txt");
        fastaContigsAll.add("fasta/contig35.txt");
        fastaContigsAll.add("fasta/contig36.txt");
        fastaContigsAll.add("fasta/contig37.txt");
        fastaContigsAll.add("fasta/contig38.txt");
        gffContigsAll.add("gff/derecta_dot_contig20.0.gff");
        gffContigsAll.add("gff/derecta_dot_contig21.0.gff");
        gffContigsAll.add("gff/derecta_dot_contig22.0.gff");
        //MISSING: gffFilesAll.add("gff/derecta_dot_contig23.0.gff");
        gffContigsAll.add("gff/derecta_dot_contig24.0.gff");
        gffContigsAll.add("gff/derecta_dot_contig25.0.gff");
        gffContigsAll.add("gff/derecta_dot_contig26.0.gff");
        gffContigsAll.add("gff/derecta_dot_contig27.0.gff");
        gffContigsAll.add("gff/derecta_dot_contig28.0.gff");
        gffContigsAll.add("gff/derecta_dot_contig29.0.gff");
        gffContigsAll.add("gff/derecta_dot_contig30.0.gff");
        gffContigsAll.add("gff/derecta_dot_contig31.0.gff");
        gffContigsAll.add("gff/derecta_dot_contig32.0.gff");
        gffContigsAll.add("gff/derecta_dot_contig33.0.gff");
        gffContigsAll.add("gff/derecta_dot_contig34.0.gff");
        gffContigsAll.add("gff/derecta_dot_contig35a.gff");
        gffContigsAll.add("gff/derecta_dot_contig36.0.gff");
        gffContigsAll.add("gff/derecta_dot_contig37.0.gff");
        gffContigsAll.add("gff/derecta_dot_contig38.0.gff");

        // fosmid files 22-42
        fastaFosmidsAll.add("fasta/Fosmid22.txt");
        fastaFosmidsAll.add("fasta/Fosmid23.txt");
        fastaFosmidsAll.add("fasta/Fosmid24.txt");
        fastaFosmidsAll.add("fasta/Fosmid25.txt");
        fastaFosmidsAll.add("fasta/Fosmid26.txt");
        fastaFosmidsAll.add("fasta/Fosmid27.txt");
        fastaFosmidsAll.add("fasta/Fosmid28.txt");
        fastaFosmidsAll.add("fasta/Fosmid29.txt");
        fastaFosmidsAll.add("fasta/Fosmid30.txt");
        fastaFosmidsAll.add("fasta/Fosmid31.txt");
        fastaFosmidsAll.add("fasta/Fosmid32.txt");
        fastaFosmidsAll.add("fasta/Fosmid33.txt");
        fastaFosmidsAll.add("fasta/Fosmid34.txt");
        fastaFosmidsAll.add("fasta/Fosmid35.txt");
        fastaFosmidsAll.add("fasta/Fosmid36.txt");
        fastaFosmidsAll.add("fasta/Fosmid37.txt");
        fastaFosmidsAll.add("fasta/Fosmid38.txt");
        fastaFosmidsAll.add("fasta/Fosmid39.txt");
        fastaFosmidsAll.add("fasta/Fosmid40.txt");
        fastaFosmidsAll.add("fasta/Fosmid41.txt");
        fastaFosmidsAll.add("fasta/Fosmid42.txt");
        gffFosmidsAll.add("gff/derecta_3Lcontrol_fosmid22a.gff");
        gffFosmidsAll.add("gff/derecta_3Lcontrol_fosmid23a.gff");
        gffFosmidsAll.add("gff/derecta_3Lcontrol_fosmid24a.gff");
        gffFosmidsAll.add("gff/derecta_3Lcontrol_fosmid25a.gff");
        gffFosmidsAll.add("gff/derecta_3Lcontrol_fosmid26a.gff");
        gffFosmidsAll.add("gff/derecta_3Lcontrol_fosmid27a.gff");
        gffFosmidsAll.add("gff/derecta_3Lcontrol_fosmid28a.gff");
        gffFosmidsAll.add("gff/derecta_3Lcontrol_fosmid29a.gff");
        gffFosmidsAll.add("gff/derecta_3Lcontrol_fosmid30a.gff");
        gffFosmidsAll.add("gff/derecta_3Lcontrol_fosmid31a.gff");
        gffFosmidsAll.add("gff/derecta_3Lcontrol_fosmid32a.gff");
        gffFosmidsAll.add("gff/derecta_3Lcontrol_fosmid33a.gff");
        gffFosmidsAll.add("gff/derecta_3Lcontrol_fosmid34a.gff");
        gffFosmidsAll.add("gff/derecta_3Lcontrol_fosmid35a.gff");
        gffFosmidsAll.add("gff/derecta_3Lcontrol_fosmid36a.gff");
        gffFosmidsAll.add("gff/derecta_3Lcontrol_fosmid37a.gff");
        gffFosmidsAll.add("gff/derecta_3Lcontrol_fosmid38a.gff");
        gffFosmidsAll.add("gff/derecta_3Lcontrol_fosmid39a.gff");
        gffFosmidsAll.add("gff/derecta_3Lcontrol_fosmid40a.gff");
        gffFosmidsAll.add("gff/derecta_3Lcontrol_fosmid41a.gff");
        gffFosmidsAll.add("gff/derecta_3Lcontrol_fosmid42a.gff");
    }

    public int setFilesInRange(int start, int stop) {
        fastaFiles = new ArrayList<String>();
        gffFiles = new ArrayList<String>();

        if (contig) {
            if (start < 20 || start < 20
                    || stop > 38 || stop > 38
                    || start > stop) {

                System.err.println("Invalid range, 20-38 only");
                return -1;
            }

            if (start <= 23 && stop >= 23) {
                stop--;
            }

            for (int i = start - 20; i <= stop - 20; i++) {
                fastaFiles.add(fastaContigsAll.get(i));
                gffFiles.add(gffContigsAll.get(i));
            }
        } else {
            if (start < 22 || start < 22
                    || stop > 42 || stop > 42
                    || start > stop) {
                System.err.println("Invalid range, 22-42 only");
                return -1;
            }

            for (int i = start - 22; i <= stop - 22; i++) {
                fastaFiles.add(fastaFosmidsAll.get(i));
                gffFiles.add(gffFosmidsAll.get(i));
            }
        }

        return 1;
    }

    public void setContig(boolean contig) {
        this.contig = contig;
    }

    public void setFastaFile(File[] fastaFiles) {
        this.fastaFileArr = fastaFiles;
    }

    public void setGffFile(File[] gffFiles) {
        this.gffFileArr = gffFiles;
    }

    public void setWindow(int window) {
        this.window = window;
    }

    public void setSlide(int slide) {
        this.slide = slide;
    }

    public String readFasta(File fastaFile) throws java.io.FileNotFoundException {
        StringBuffer seqBuffer = new StringBuffer();
        Scanner sc = new Scanner(fastaFile);
        sc.nextLine();

        while (sc.hasNext()) {
            String c = sc.next();
            if (!c.equals("\n")) {
                seqBuffer = seqBuffer.append(c);
            }
        }
        return seqBuffer.toString();
    }

    public void readGFF(File gffFile, ArrayList<Gene> geneList, ArrayList<String> idList)
            throws java.io.FileNotFoundException {
        Scanner sc;

        // GFF File
        sc = new Scanner(gffFile);
        String id = "";
        int startg = 0, stopg = 0;
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            StringTokenizer st = new StringTokenizer(line, " \t");
            int count = 0;
            int flag = 0;
            while (st.hasMoreTokens()) {
                String word = st.nextToken();
                if (count == 2) {
                    if (word.equals("mRNA")) {
                        flag = 1;
                    } else if (word.equals("CDS")) {
                        flag = 2;
                    }
                } else if (count == 3 && flag > 0) {
                    startg = Integer.parseInt(word);
                } else if (count == 4 && flag > 0) {
                    stopg = Integer.parseInt(word);
                } else if (count == 9 && flag > 0) {
                    StringTokenizer st2 = new StringTokenizer(word, "\";");
                    id = st2.nextToken();
                }
                count++;
            }
            // mRNA
            if (flag == 1) {
                if (!idList.contains(id)) {
                    Gene gene;
                    if (startg < stopg) {
                        gene = new Gene(id, startg, stopg, true);
                    } else {
                        gene = new Gene(id, stopg, startg, false);
                    }

                    geneList.add(gene);
                    idList.add(id);
                } else {
                    for (Gene g : geneList) {
                        if (id.equals(g.id)) {
                            if (Math.abs(startg - stopg) > Math.abs(g.start - g.stop)) {
                                Gene ng;
                                if (startg < stopg) {
                                    ng = new Gene(id, startg, stopg, true);
                                } else {
                                    ng = new Gene(id, stopg, startg, false);
                                }
                                geneList.set(geneList.indexOf(g), ng);
                            }
                            break;
                        }
                    }
                }
            } // CDS
            else if (flag == 2) {
                for (Gene g : geneList) {
                    if (id.equals(g.id)) {
                        int dup = 0;

                        for (Gene p : g.points) {
                            if (p.start == startg && p.stop == stopg) {
                                dup = 1;
                            }
                        }
                        if (dup == 0) {
                            if (startg < stopg) {
                                g.points.add(new Gene("Exon", startg, stopg, true));
                            } else {
                                g.points.add(new Gene("Exon", stopg, startg, false));
                            }
                        }
                    }
                }
            }
        }
    }
    public void createGeneList(String seq, ArrayList<Gene> geneList) {        
        // Exons and Introns within Genes

            int min;
            int max;

            for (Gene g : geneList) {
                Collections.sort(g.points, new Comparator<Gene>() {

                    public int compare(Gene a, Gene b) {
                        return a.start - b.start;
                    }
                });
            }

            for (Gene g : geneList) {
                min = g.start;
                max = g.stop;
                ArrayList<Gene> copy = new ArrayList<Gene>(g.points);
                for (Gene p : copy) {
                    if (min < p.start) {
                        Gene gene = new Gene("Intron", min, p.start - 1, false);
                        g.points.add(gene);
                    }
                    min = p.stop + 1;
                }
                if (max > min) {
                    Gene gene = new Gene("Intron", min, max, false);
                    g.points.add(gene);
                }
                Collections.sort(g.points, new Comparator<Gene>() {

                    public int compare(Gene a, Gene b) {
                        return a.start - b.start;
                    }
                });
            }

            // Genes and Intergenic Regions
            min = 1;
            max = seq.length();

            ArrayList<Gene> geneSort = new ArrayList<Gene>(geneList);
            Collections.sort(geneSort, new Comparator<Gene>() {

                public int compare(Gene a, Gene b) {
                    return a.start - b.start;
                }
            });

            for (Gene g : geneSort) {
                if (min < g.start) {
                    Gene gene = new Gene("Intergenic", min, g.points.get(0).start - 1, false);
                    geneList.add(gene);
                }
                min = g.points.get(g.points.size() - 1).stop + 1;
            }
            if (max > min) {
                Gene gene = new Gene("Intergenic", min, max, false);
                geneList.add(gene);
            }

            Collections.sort(geneList, new Comparator<Gene>() {

                public int compare(Gene a, Gene b) {
                    return a.start - b.start;
                }
            });
        
    }

    public void doCalculations() throws java.io.FileNotFoundException {
        ArrayList<Gene> geneList;
        ArrayList<String> idList;
        StringBuffer[] buf = new StringBuffer[6];
        DNALib lib;
        String seq;

        for (int i = 0; i < buf.length;i++)
            buf[i] = new StringBuffer();

        for (int i = 0; i < fastaFileArr.length; i++) {
            idList = new ArrayList<String>();
            geneList = new ArrayList<Gene>();
            seq = readFasta(fastaFileArr[i]);
            readGFF(gffFileArr[i], geneList, idList);

            createGeneList(seq, geneList);
            


            // Initialize DNA Libarary
            //System.out.println(geneList.);
            lib = new DNALib(seq, geneList);
            //buf[5].append(lib.codonFrequency());

            // Basic Functionality
            //System.out.println("GC-content: " + lib.GCContent(start, stop));
            //System.out.println("Complement: " + lib.complement(start, stop));
            //System.out.println("Reverse: " + lib.reverse(start, stop));
            buf[0].append("\nResults for file: " + fastaFileArr[i].getName() + "\n");
            buf[0].append(lib.overlap(window, slide));
            buf[0].append("\n");
            buf[1].append("\nResults for file: " + fastaFileArr[i].getName() + "\n");
            buf[1].append(lib.nonoverlap(window));
            buf[1].append("\n");
            buf[2].append("\nResults for file: " + fastaFileArr[i].getName() + "\n");
            buf[2].append(lib.geneDensity());
            buf[2].append("\n");
            buf[3].append("\nResults for file: " + fastaFileArr[i].getName() + "\n");
            buf[3].append(lib.geneGC());
            buf[3].append("\n");
            buf[4].append("\nResults for file: " + fastaFileArr[i].getName() + "\n");
            buf[4].append(lib.geneSize());
            buf[4].append("\n");
            buf[5].append("\nResults for file: " + fastaFileArr[i].getName() + "\n");
            buf[5].append(lib.codonFrequency());
            buf[5].append("\n");
            lib.getError();
        }
        try {
            String dirName = "results";
            String path = dirName + System.getProperty("path.separator");
            FileWriter fstream;
            BufferedWriter out;

            // 2.2. GC-Content Regions
            fstream = new FileWriter("GCContOverlap" + FILE_EXTENSION);
            out = new BufferedWriter(fstream);

            System.out.println("GC-Content Regions with Overlap (start, stop, GC):");
            out.write(buf[0].toString());
            out.close();
            System.out.print("\n");
            setMainWindowOutput("File: GCContOverlap" + FILE_EXTENSION + " writen. "
                    + "Format: (start, stop, GC)\n");

            fstream = new FileWriter("GCContNonOverLap" + FILE_EXTENSION);
            out = new BufferedWriter(fstream);

            System.out.println("GC-Content Regions without Overlap (start, stop, GC):");
            out.write(buf[1].toString());
            out.close();
            System.out.print("\n");
            setMainWindowOutput("File: GCContNonOverlap" + FILE_EXTENSION + " writen. "
                    + "Format: (start, stop, GC)\n");

            // 2.2.1. Gene Density
            fstream = new FileWriter("GeneDensity" + FILE_EXTENSION);
            out = new BufferedWriter(fstream);

            System.out.println("Gene Density: ");
            out.write(buf[2].toString());
            out.close();
            System.out.print("\n");
            setMainWindowOutput("File: GeneDensity" + FILE_EXTENSION + " writen.\n");

            // 2.2.2 Gene GC-Content
            fstream = new FileWriter("GeneGCCont" + FILE_EXTENSION);
            out = new BufferedWriter(fstream);

            System.out.println("Gene GC-Content (gene, start, stop, GC): ");
            out.write(buf[3].toString());
            out.close();
            System.out.print("\n");
            setMainWindowOutput("File: GeneGCCont" + FILE_EXTENSION + " writen. "
                    + "Format: (gene, start, stop, GC)\n");

            // 2.2.3 Gene Size 
            fstream = new FileWriter("GeneSize" + FILE_EXTENSION);
            out = new BufferedWriter(fstream);

            System.out.println("Gene Size (gene, start, stop, size): ");
            out.write(buf[4].toString());
            out.close();
            System.out.print("\n");
            setMainWindowOutput("File: GeneSize" + FILE_EXTENSION + " writen. "
                    + "Format: (gene, start, stop, size)\n");

            // 2.2.4 Codon Frequency
            fstream = new FileWriter("CodonFrequency" + FILE_EXTENSION);
            out = new BufferedWriter(fstream);

            System.out.println("Codon Frequency (codon, amino acid, frequency, ratio): ");
            out.write(buf[5].toString());
            out.close();
            System.out.print("\n");
            setMainWindowOutput("File: CodonFrequency" + FILE_EXTENSION + " writen. "
                    + "Format: (codon, amino acid, frequency, ratio)\n");

            setMainWindowOutput("Done.\n");
        } catch (Exception e) {//Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }
    }

    public void computeGC(String fileName, int start, int end) throws java.io.FileNotFoundException {
        String seq = readFasta(new File(fileName));
        DNALib lib = new DNALib(seq);
        if (end > seq.length()) {
            end = seq.length();
        }
        setMainWindowOutput(String.format("\nThe GC content of file %s from "
                + "position %d to %d is: %.1f\n", fileName, start,
                end, lib.GCContent(start, end)));
    }

    public void combineFiles(int existancePenalty, int extensionPenalty) {
        setMainWindowOutput("Merging files...\n");
        Combine c = new Combine();
        try {
            c.combineFiles(fastaFileArr, gffFileArr, existancePenalty, extensionPenalty);
        }
        catch (Exception e) {
            System.err.println("ERROR: " + e.getMessage());
        }
        setMainWindowOutput("Merging complete.\n");
    }
    
    public void findRepeats(int minLeng, int maxLeng,
            int kfold) throws java.io.FileNotFoundException {
        ArrayList<Gene> geneList;
        ArrayList<String> idList;
        String seq;
        Repeat r;
        StringBuffer buf = new StringBuffer();

        for (int i = 0; i < fastaFileArr.length; i++) {
            idList = new ArrayList<String>();
            geneList = new ArrayList<Gene>();
            seq = readFasta(fastaFileArr[i]);
            readGFF(gffFileArr[i], geneList, idList);

            createGeneList(seq, geneList);
            r = new Repeat(seq, minLeng, maxLeng, kfold, geneList);
            buf.append("Output for file " + fastaFileArr[i].getName() + "\n");
            buf.append("Size, Repeats, Frequency, Expected Frequency, % More than Expected, Sequence, Average Repeat Distances, Stdev Repeat Distances, Average Gene Distance, Stdev Gene Distances\n");
      for (Repeat.Unexpected ue : r.unexpected)
      {
         buf.append(String.format("%d, %d, %f, %f, %f, %s, %f, %f, %f, %f\n", ue.size, ue.repeat, ue.freq, ue.expectedFreq, ue.percentFreq, ue.s, ue.selfAvgProximity, ue.selfStdProximity, ue.geneAvgProximity, ue.geneStdProximity));
      }
      buf.append("\n");
        }
        try {
            FileWriter fstream;
            BufferedWriter out;

            // 2.2. GC-Content Regions
            fstream = new FileWriter("Repeats" + FILE_EXTENSION);
            out = new BufferedWriter(fstream);
            
            out.write(buf.toString());
            out.close();
        }
        catch(Exception e) {
            System.err.println(e);
        }
        setMainWindowOutput("\nDone. Output was written to Repeats.csv\n");

    }
    
    public void findPalindromes(int minLeng, int maxLeng,
            int minGap, int maxGap) throws java.io.FileNotFoundException {
        String seq;
        Palindrome p;
        StringBuffer buf = new StringBuffer();

        for (int ndx = 0; ndx < fastaFileArr.length; ndx++) {
            seq = readFasta(fastaFileArr[ndx]);
            p = new Palindrome(seq, minLeng, maxLeng, minGap, maxGap);
            
            buf.append("Output for file " + fastaFileArr[ndx].getName() + "\n");
            buf.append("Sequence, Reverse Complement of Sequence, Length, Gap, Location 1, Location 2\n"); 
      for (String s : p.gapPalindromes)
      {
         for (int i = 0; i < p.gapLocations1.get(s).size(); i++)
         {
            buf.append(String.format("%s, %s, %d, %d, %d, %d\n", s, Palindrome.reverseComplement(s), s.length(), Math.abs(p.gapLocations1.get(s).get(i)-p.gapLocations2.get(s).get(i)) - s.length(), p.gapLocations1.get(s).get(i), p.gapLocations2.get(s).get(i)));   
         }
      }
      buf.append("\n");
        }
        try {
            FileWriter fstream;
            BufferedWriter out;

            // 2.2. GC-Content Regions
            fstream = new FileWriter("Palindromes" + FILE_EXTENSION);
            out = new BufferedWriter(fstream);
            
            out.write(buf.toString());
            out.close();
        }
        catch(Exception e) {
            System.err.println(e);
        }
        
        setMainWindowOutput("\nDone. Output was written to Palindromes.csv\n");
    }
    
    public void setMainWindowOutput(String text) {
        gui.setOutput(text);
    }
}
