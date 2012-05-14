package UI;

import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

public class MainWindow extends JFrame {
   private static final String WINDOW_TITLE = "CSC 448 - Bioinformatics";
   private static final String DEFAULT_TEXT = "Output Area";
   private static final int FRAME_WIDTH = 500,
                            FRAME_HEIGHT = 600,
                            OUTPUT_WIDTH = 40,
                            OUTPUT_HEIGHT = 23;

   private static MainWindow mMainFrame = null;
   private JMenuBar mMenuBar;
   private JTextArea mOutput;
   private Controller controller;

   /**
    * This is a singleton constructor for the MainWindow. This means that
    * only one MainWindow is instantiated at a time. Whenever a component
    * wants to get the main window, it'll always get this one.
    */
   public static MainWindow getMainFrame() {
      if (mMainFrame == null) {
         mMainFrame = new MainWindow();
      }

      return mMainFrame;
   }
   
   private MainWindow() {
      super(WINDOW_TITLE);

      setSize(FRAME_WIDTH, FRAME_HEIGHT);
      setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
      //centers the component on the screen
      setLocationRelativeTo(null);
   }

   /**
    * Initialize the Menu Bar for this JFrame and any other relevant
    * components.
    */
   public void init(Controller controller) {
      this.controller = controller;
      mOutput = new JTextArea(OUTPUT_HEIGHT, OUTPUT_WIDTH);
      JPanel outputPanel = prepareOutputPanel(mOutput);
      
      mMenuBar = new JMenuBar();
      mMenuBar.add(initFileMenu());
      mMenuBar.add(initAnalysisMenu());
      mMenuBar.add(initHelpMenu());
      
      mMainFrame.add(outputPanel);

      setJMenuBar(mMenuBar);
      validate();
   }

   /**
    * Initialize menu items that will be present in the 'File' menu, then
    * return the initialized 'File' menu.
    */
   private JMenu initFileMenu() {
      JMenuItem exitProgram = new JMenuItem("Exit");
      exitProgram.addActionListener(new ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent e) {
            System.exit(0);
         }
      });


      JMenu fileMenu = new JMenu("File");
      fileMenu.add(exitProgram);

      return fileMenu;
   }

   /**
    * Initialize menu items that will be present in the 'Analysis' menu, then
    * return the initialized 'Analysis' menu.
    */
   private JMenu initAnalysisMenu() {
      JMenuItem gcContent = new JMenuItem("Lab2-1: GC content");
      JMenuItem dataFiles = new JMenuItem("Lab2-2: DNA Manipulation");
      JMenuItem dnaMerge = new JMenuItem("Lab3-1: DNA Merging");
      JMenuItem dnaRepeats = new JMenuItem("Lab3-2: DNA Repeats/Palindromes");
      
      gcContent.addActionListener(new ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent e) {
             //JOptionPane.showMessageDialog(mMainFrame, "Temporarily disabled.");
             InputDialog gcContentDialog = new InputDialog(mMainFrame, controller, "GC Content Input Parameters");

             gcContentDialog.init();
             gcContentDialog.setVisible(true);
         }
      });

      dataFiles.addActionListener(new ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent e) {
             InputFilesDialog inputFilesDialog = new InputFilesDialog(mMainFrame, controller, "DNA Manipulation Input Files");

             inputFilesDialog.init();
             inputFilesDialog.setVisible(true);
         }
      });
      dnaMerge.addActionListener(new ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent e) {
             DNAMergeDialog dnaMergeDialog = new DNAMergeDialog(mMainFrame, controller, "DNA Merge");

             dnaMergeDialog.init();
             dnaMergeDialog.setVisible(true);
         }
      });
      dnaRepeats.addActionListener(new ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent e) {
             DNARepeatsDialog dnaRepeatsDialog = new DNARepeatsDialog(mMainFrame, controller, "DNA Repeats/Palindromes");

             dnaRepeatsDialog.init();
             dnaRepeatsDialog.setVisible(true);
         }
      });
      JMenu analysisMenu = new JMenu("Analysis");
      
      analysisMenu.add(gcContent);
      analysisMenu.add(dataFiles);
      analysisMenu.add(dnaMerge);
      analysisMenu.add(dnaRepeats);

      return analysisMenu;
   }
   
   private JMenu initHelpMenu() {
       JMenuItem howTo = new JMenuItem("How to");
       JMenu helpMenu = new JMenu("Help");
       
       howTo.addActionListener(new ActionListener() {
           public void actionPerformed(java.awt.event.ActionEvent e) {
               HowToDialog howToD = new HowToDialog(mMainFrame, "How To");
               howToD.init();
               howToD.setVisible(true);
           }
       });
       
       helpMenu.add(howTo);
       return helpMenu;
   }
   
   private JPanel prepareOutputPanel(JTextArea mTextOutput) {
       JPanel panel = new JPanel();
       mTextOutput.setEditable(false);
       mTextOutput.setLineWrap(true);
       //mTextOutput.setText(DEFAULT_TEXT);
       JScrollPane outputPane = new JScrollPane(mTextOutput);
       panel.add(new JLabel(DEFAULT_TEXT));
       panel.add(outputPane);
       return panel;
   }

   public void setOutput(String output) {
       mOutput.append(output);
   }
   /**
    * Convenience method for ensuring the MainWindow is visible and repainted.
    */
   public void showWindow() {
      setVisible(true);
      repaint();
   }
}
