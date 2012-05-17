package UI;

import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Frame;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.FlowLayout;

import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.BoxLayout;

import java.io.File;

public class InputFilesDialog extends JDialog {
   /*
    * CONSTANTS
    */
   private final int DIALOG_HEIGHT = 230, DIALOG_WIDTH = 500;

   private Controller controller;
   private File[] fastaFiles, gffFiles;
   /*
    * GUI Components
    */
   private Container mPane = null, mOwner = null;
   private JDialog mDialog = null;
   private JTextField mFileRangeStart, mFileRangeEnd, mWindow, mSlide;
   private JComboBox mFileType;
   private String[] comboBoxOptions = {"Select one...", "Fosmid", "Contig"};

   public InputFilesDialog(Controller controller) {
      super();
      
      this.controller = controller;
      this.setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
      this.setResizable(false);
      this.setLocationRelativeTo(null);

      mDialog = this;

      mPane = this.getContentPane();
      mPane.setLayout(new BoxLayout(mPane, BoxLayout.Y_AXIS));
      mPane.setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
      
      setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

      mFileRangeStart = new JTextField(20);
      mFileRangeEnd = new JTextField(20);
   }

   public InputFilesDialog(Frame owner, Controller controller, String title) {
      super(owner, title);

      this.controller = controller;
      this.setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
      this.setResizable(false);
      this.setLocationRelativeTo(null);

      mOwner = owner;
      mDialog = this;

      mPane = this.getContentPane();
      mPane.setLayout(new BoxLayout(mPane, BoxLayout.Y_AXIS));
      mPane.setSize(DIALOG_WIDTH, DIALOG_HEIGHT);

      setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

      mFileRangeStart = new JTextField(20);
      mFileRangeEnd = new JTextField(20);
      mWindow = new JTextField(6);
      mSlide = new JTextField(6);
      mFileType = new JComboBox(comboBoxOptions);
   }

   /**
    * Method for initializing a default Dialog window ready for GC Content
    * input
    */
   public void init() {
      JPanel fileFieldLabel = prepareStandAloneLabel("Select FASTA and GFF files:");
      
      JPanel windowField = prepareWindowSizeField("Window:", mWindow);
      JPanel slideField = prepareWindowSizeField("Slide:", mSlide);

      JPanel nucleotideRangeField = new JPanel();
      nucleotideRangeField.setLayout(new FlowLayout(FlowLayout.LEADING));
      nucleotideRangeField.add(windowField);
      nucleotideRangeField.add(slideField);
      
      mPane.add(fileFieldLabel);
      mPane.add(prepareInputFilesField(mFileRangeStart, "FASTA", true));
      mPane.add(prepareInputFilesField(mFileRangeEnd, "GFF", false));

      mPane.add(nucleotideRangeField);
      
      mPane.add(initControls());

      mPane.validate();
   }

   /**
    * Convenience method for constructing a JPanel that contains the JTextField
    * and file browse button used for selecting a file.
    */
   private JPanel prepareInputDataField(JTextField start, JTextField end, JComboBox options) {
      JPanel dataFileField = new JPanel();

      dataFileField.setLayout(new FlowLayout(FlowLayout.LEADING));
      
      options.setSelectedIndex(0);

      dataFileField.add(new JLabel("Start:"));
      dataFileField.add(start);
      dataFileField.add(new JLabel("End:"));
      dataFileField.add(end);
      dataFileField.add(options);

      return dataFileField;
   }
    private JPanel prepareStandAloneLabel(String s) {
       JPanel holder = new JPanel();
       
       holder.setLayout(new FlowLayout(FlowLayout.LEADING));
       
       holder.add(new JLabel(s));
       
       return holder;
   }
   /**
    * Convenience method for constructing a JPanel that contains the JTextField
    * and file browse button used for selecting a file.
    */
   private JPanel prepareInputFilesField(JTextField fileField, String fieldName, boolean fasta) {
      JPanel dataFileField = new JPanel();

      dataFileField.setLayout(new FlowLayout(FlowLayout.LEADING));
      
      dataFileField.add(new JLabel(fieldName));
      dataFileField.add(fileField);
      dataFileField.add(prepareBrowseButton(fileField, fasta));

      return dataFileField;
   }

   /**
    * Convenience method for creating a file browse button. This is abstracted
    * so that it is not necessarily associated with the fasta file field.
    */
   private JButton prepareBrowseButton(final JTextField fastaField, final boolean fasta) {
      JButton fileBrowse = new JButton("Browse");

      fileBrowse.addActionListener(new ActionListener() {

         public void actionPerformed(ActionEvent e) {
            JFileChooser chooser = new JFileChooser();
            chooser.setMultiSelectionEnabled(true);
            int returnVal = chooser.showOpenDialog(chooser);
            
            if (returnVal == JFileChooser.CANCEL_OPTION) {
               System.out.println("cancelled");
            }

            else if (returnVal == JFileChooser.APPROVE_OPTION) {
               File[] files = chooser.getSelectedFiles();
               if (fasta)
                   fastaFiles = files;
               else
                   gffFiles = files;
               fastaField.setText(files.length + " files selected.");
            }

            else {
               System.out.println("Encountered Unknown Error");
               System.exit(0);
            }
         }
      });

      return fileBrowse;
   }
    
    /**
    * Convenience method for creating JTextFields for the window size and slide
    * parameters. This is abstracted so that it may be called
    * for both the window and the slide text fields.
    */
   private JPanel prepareWindowSizeField(String labelPrefix, JTextField windowSize) {
      JPanel windowSizePanel = new JPanel();

      windowSizePanel.setLayout(new FlowLayout());

      windowSizePanel.add(new JLabel(labelPrefix));
      windowSizePanel.add(windowSize);

      return windowSizePanel;
   }

   /**
    * Convenience method for creating a JPanel containing an okay and cancel
    * button at the bottom of the Dialog window. If cancel is pressed then the
    * dialog window is disposed and nothing happens. If okay is pressed then
    * the appropriate input parameters should be grabbed and handled
    * appropriately. Some slight error checking is also implemented.
    */
   public JPanel initControls() {
      JPanel dialogControls = new JPanel();

      dialogControls.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

      dialogControls.add(createOkayButton());
      dialogControls.add(createCancelButton());

      dialogControls.setAlignmentX(Component.CENTER_ALIGNMENT);

      return dialogControls;
   }

   /**
    * Convenience method for creating the okay button at the bottom of the
    * Dialog window. Anything that should be added to the okay button's
    * functionality should probably be added here, or separated into an
    * ActionListener and added as an ActionListener for the button.
    */
   private JButton createOkayButton() {
      JButton okayButton = new JButton("Okay");

      okayButton.addActionListener(new ActionListener(){
         public void actionPerformed(ActionEvent e) {
            if (fastaFiles == null || gffFiles == null) {
               JOptionPane.showMessageDialog(mOwner,
                "No FASTA files of GFF files were set.",
                "Invalid File", JOptionPane.ERROR_MESSAGE);
               return;
            }
            controller.setFastaFile(fastaFiles);
            controller.setGffFile(gffFiles);
            
            if (!mWindow.getText().equals(""))
                controller.setWindow(Integer.parseInt(mWindow.getText()));
            if (!mSlide.getText().equals(""))
                controller.setSlide(Integer.parseInt(mSlide.getText()));
            try {
                controller.setMainWindowOutput("Calculating...\n");
                controller.doCalculations();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(InputFilesDialog.class.getName()).log(Level.SEVERE, null, ex);
            }
            dispose();
         }
      });

      return okayButton;
   }

   /**
    * Convenience method for creating the cancel button at the bottom of the
    * Dialog window. This currently just disposes of the dialog window, though
    * some more complex behavior may be desired.
    */
   private JButton createCancelButton() {
      JButton cancelButton = new JButton("Cancel");

      cancelButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            dispose();
            return;
         }
      });

      return cancelButton;
   }
}
