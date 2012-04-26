package UI;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//package lab4.UI;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;
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
    private File fastaFile;
    private File gffFile;
    private int window;
    private int slide;

    
    public Controller() {
        window = 1000;
        slide = 100;
    }
    
    public void init() {
      gui = MainWindow.getMainFrame();
      gui.init(this);
      gui.showWindow();
    }
    
    public void setFastaFile(String fastaFileName) {
        this.fastaFile = new File(fastaFileName);
    }
    
    public void setGffFile(String gffFileName) {
        this.gffFile = new File(gffFileName);
    }
    
    public void setWindow(int window) {
        this.window = window;
    }
    
    public void setSlide(int slide) {
        this.slide = slide;
    }
    
    public void doCalculations() throws java.io.FileNotFoundException {
        // FASTA File
        StringBuffer seqBuffer = new StringBuffer();
        Scanner sc = new Scanner(fastaFile);
        sc.nextLine();
        sc.useDelimiter("");

        while (sc.hasNextLine()) {
            String c = sc.next();
            if (!c.equals("\n")) {
                seqBuffer = seqBuffer.append(c);
            }
        }
        String seq = seqBuffer.toString();

        // GFF File
        ArrayList<Gene> geneList = new ArrayList<Gene>();
        ArrayList<String> idList = new ArrayList<String>();
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


        // Initialize DNA Libarary
        DNALib lib = new DNALib(seq, geneList);

        // Basic Functionality
        //System.out.println("GC-content: " + lib.GCContent(start, stop));
        //System.out.println("Complement: " + lib.complement(start, stop));
        //System.out.println("Reverse: " + lib.reverse(start, stop));

        try{
            String dirName = "results";
            String path = dirName + System.getProperty("path.separator");
            File dir = new File(dirName);
            //dir.mkdir();
            FileWriter fstream;
            BufferedWriter out;

            // 2.2. GC-Content Regions
            fstream = new FileWriter("GCContOverlap" + FILE_EXTENSION);
            out = new BufferedWriter(fstream);
            
            System.out.println("GC-Content Regions with Overlap (start, stop, GC):");
            out.write(lib.overlap(window, slide));
            out.close();
            System.out.print("\n");
            gui.setOutput("File: GCContOverlap" + FILE_EXTENSION + " writen. " +
                    "Format: (start, stop, GC)\n");

            fstream = new FileWriter("GCContNonOverLap" + FILE_EXTENSION);
            out = new BufferedWriter(fstream);
            
            System.out.println("GC-Content Regions without Overlap (start, stop, GC):");
            out.write(lib.nonoverlap(window));
            out.close();
            System.out.print("\n");
            gui.setOutput("File: GCContNonOverlap" + FILE_EXTENSION + " writen. " +
                    "Format: (start, stop, GC)\n");

            // 2.2.1. Gene Density
            fstream = new FileWriter("GeneDensity" + FILE_EXTENSION);
            out = new BufferedWriter(fstream);
            
            System.out.println("Gene Density: ");
            out.write(lib.geneDensity());
            out.close();
            System.out.print("\n");
            gui.setOutput("File: GeneDensity" + FILE_EXTENSION + " writen.\n");

            // 2.2.2 Gene GC-Content
            fstream = new FileWriter("GeneGCCont" + FILE_EXTENSION);
            out = new BufferedWriter(fstream);
            
            System.out.println("Gene GC-Content (gene, start, stop, GC): ");
            out.write(lib.geneGC());
            out.close();
            System.out.print("\n");
            gui.setOutput("File: GeneGCCont" + FILE_EXTENSION + " writen. " +
                    "Format: (gene, start, stop, GC)\n");

            // 2.2.3 Gene Size 
            fstream = new FileWriter("GeneSize" + FILE_EXTENSION);
            out = new BufferedWriter(fstream);
            
            System.out.println("Gene Size (gene, start, stop, size): ");
            out.write(lib.geneSize());
            out.close();
            System.out.print("\n");
            gui.setOutput("File: GeneSize" + FILE_EXTENSION + " writen. " +
                    "Format: (gene, start, stop, size)\n");

            // 2.2.4 Codon Frequency
            fstream = new FileWriter("CodonFrequency" + FILE_EXTENSION);
            out = new BufferedWriter(fstream);
            
            System.out.println("Codon Frequency (codon, amino acid, frequency, ratio): ");
            out.write(lib.codonFrequency());
            out.close();
            System.out.print("\n");
            gui.setOutput("File: CodonFrequency" + FILE_EXTENSION + " writen. " +
                    "Format: (codon, amino acid, frequency, ratio)\n");

            // Error
            System.out.println("Error:");
            lib.getError();
            gui.setOutput("Done.\n");
        }catch (Exception e){//Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }
    }

    public void setMainWindowOutput(String text) {
        gui.setOutput(text);
    }
}
