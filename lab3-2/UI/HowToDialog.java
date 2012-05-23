/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import java.awt.Adjustable;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Frame;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;

/**
 *
 * @author Ricardo
 */
public class HowToDialog extends JDialog {
   private static final String HEADER = "<div align=\"center\"><h1>How To</h1></div>";
   private static final String FORMAT_START = "<div style=\"width:360px; "
           + "word-wrap: break-word; font-size:16\">";
   private static final String FORMAT_END = "</div>";
   private static final String LAB2_1_HOW_TO_TEXT =
           "<HR><h2>Lab 2-1: DNA Analysis: GC Percent</h2><p>" +
           "To calculate the GC content of a DNA strand, under the <b>Analysis</b> "+
           "menu select the <b>Lab2-1: GC Content</b> option. A window will "+
           "pop up prompting you for a FASTA file and a range of the strand "+
           "for which the GC content is to be calculated. Click on the <b>Browse</b> " +
           "button to select a FASTA file. The range is optional. If left "+
           "blank, the range would be from the begining of the strand to the "+
           "end of the strand.</p>" +
           "<p><b>Output:</b><br />"+
           "The output will be displayed in the main window.</p><br />";
   private static final String LAB2_2_HOW_TO_TEXT = 
           "<HR><h2>Lab 2-2: DNA Analysis: DNA Manipulation, Codon Usage Bias, and Gene Density</h2><p>"+
           "To begin, under the <b>Analysis</b> menu select the <b>Lab2-2: DNA " +
           "Manipulation</b> option. A window will pop up prompting you for 2 files, " +
           "a slide value, and window size. You can select one or multiple FASTA files "+
           "along with their corresponding GFF files. "+
           "The <b>Window</b> and <b>Slide</b> options are " +
           "optional. Their default values are 1000 and 100 respectively.</p>"+
           "<p><b>Output:</b><br />"+
           "The output will go to separate files which names will be listed in the " +
           "output box of the main window. The files will be writen to the folder " +
           "from which the program was run.</p><br />";
   private static final String LAB3_1_HOW_TO_TEXT =
           "<HR><h2>Lab 3-1: DNA Merging</h2><p>" +
           "To merge DNA FASTA files, under the <b>Analysis</b> menu select the " +
           "<b>Lab3-1: DNA Merging</b> option. A window will pop up prompting " +
           "you for FASTA and GFF files. Select the FASTA files that you would "+
           "like to combine along with the corresponding GFF files and click "+
           "<b>Okay</b>.</p><p>"+
           "<b>Output:</b><br />"+
           "The output will go to separate files which will be writen to the folder " +
           "from which the program was run.</p><br />";
   private static final String LAB3_2_HOW_TO_TEXT =
           "<HR><h2>Lab 3-2: Repeat Search and Palindrome Discovery</h2><p>" +
           "To find repeats/palindromes, under the <b>Analysis</b> menu select the " +
           "<b>Lab3-2: DNA Repeats</b> option. A window will pop up prompting " +
           "you for FASTA and GFF files (among other things). Select the FASTA" +
           "files along with the corresponding GFF files you would like to find repeats/" +
           "palindromes for. Enter the minimum and maximum length of the repeat/"+
           "palindrome. Select what you would like to search for in the files " +
           "(repeats or palindromes). For repeats enter the \'fold\' value. For " +
           "Palindromes enter the min and max gap. Click <b>Okay</b>.</p>" +
           "<p><b>Output:</b><br />"+
           "The output will go to a separate file which will be writen to the folder " +
           "from which the program was run.</p><br />";
   private static final int DIALOG_WIDTH = 500,
                            DIALOG_HEIGHT = 400;

   private JEditorPane mOutput;
   private Container mPane = null, mOwner = null;
   private JDialog mDialog = null;

   public HowToDialog(Frame owner, String title) {
      super(owner, title);

      this.setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
      this.setResizable(false);
      this.setLocationRelativeTo(null);

      mOwner = owner;
      mDialog = this;

      mDialog.setResizable(true);
      mPane = this.getContentPane();
      mPane.setLayout(new BoxLayout(mPane, BoxLayout.Y_AXIS));
      mPane.setSize(DIALOG_WIDTH, DIALOG_HEIGHT);

      setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

   }

   /**
    * Method for initializing a default Dialog window ready for GC Content
    * input
    */
   public void init() {
      mOutput = new JEditorPane();
      JPanel outputPanel = prepareOutputPanel(mOutput);
      
      mPane.add(outputPanel);
   }

   private JPanel prepareOutputPanel(JEditorPane mTextOutput) {
       JPanel panel = new JPanel();
       panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
       mTextOutput.setEditable(false);
       mTextOutput.setLayout(new FlowLayout());
       mTextOutput.setContentType("text/html");
       mTextOutput.setText("<html>" + HEADER + FORMAT_START
               + LAB2_1_HOW_TO_TEXT + LAB2_2_HOW_TO_TEXT + LAB3_1_HOW_TO_TEXT + LAB3_2_HOW_TO_TEXT
               + FORMAT_END + "</html>");
       mTextOutput.setCaretPosition(0);
       JScrollPane outputPane = new JScrollPane(mTextOutput);
       outputPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
       outputPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
       panel.add(outputPane);
       return panel;
   }
}
