package UI;

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
   private final int DIALOG_HEIGHT = 200, DIALOG_WIDTH = 500;

   private Controller controller;
   /*
    * GUI Components
    */
   private Container mPane = null, mOwner = null;
   private JDialog mDialog = null;
   private JTextField mFasta, mGff, mWindow, mSlide;

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

      mFasta = new JTextField(20);
      mGff = new JTextField(20);
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

      mFasta = new JTextField(20);
      mGff = new JTextField(20);
      mWindow = new JTextField(6);
      mSlide = new JTextField(6);
   }

   /**
    * Method for initializing a default Dialog window ready for GC Content
    * input
    */
   public void init() {
      JLabel fastaFileLabel = new JLabel("Select FASTA File:");
      JPanel fastaFileField = prepareInputDataField(mFasta, fastaFileLabel);

      JLabel gffFileLabel = new JLabel("Select GFF File:");
      JPanel gffFileField = prepareInputDataField(mGff, gffFileLabel);
      
      JPanel windowField = prepareWindowSizeField("Window:", mWindow);
      JPanel slideField = prepareWindowSizeField("Slide:", mSlide);

      JPanel nucleotideRangeField = new JPanel();
      nucleotideRangeField.setLayout(new FlowLayout(FlowLayout.LEADING));
      nucleotideRangeField.add(windowField);
      nucleotideRangeField.add(slideField);
      
      mPane.add(fastaFileField);

      mPane.add(gffFileField);

      mPane.add(nucleotideRangeField);
      
      mPane.add(initControls());

      mPane.validate();
   }

   /**
    * Convenience method for constructing a JPanel that contains the JTextField
    * and file browse button used for selecting a file.
    */
   private JPanel prepareInputDataField(JTextField dataField, JLabel label) {
      JPanel dataFileField = new JPanel();

      dataFileField.setLayout(new FlowLayout(FlowLayout.LEADING));

      dataFileField.add(label);
      dataFileField.add(dataField);
      dataFileField.add(prepareBrowseButton(dataField));

      return dataFileField;
   }

   /**
    * Convenience method for creating a file browse button. This is abstracted
    * so that it is not necessarily associated with the file field.
    */
   private JButton prepareBrowseButton(final JTextField fastaField) {
      JButton fileBrowse = new JButton("Browse");

      fileBrowse.addActionListener(new ActionListener() {

         public void actionPerformed(ActionEvent e) {
            JFileChooser chooser = new JFileChooser();
            int returnVal = chooser.showOpenDialog(chooser);
            
            if (returnVal == JFileChooser.CANCEL_OPTION) {
               System.out.println("cancelled");
            }

            else if (returnVal == JFileChooser.APPROVE_OPTION) {
               File fastaFile = chooser.getSelectedFile();
               fastaField.setText(fastaFile.getAbsolutePath());
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
            if (mFasta.getText().equals("") || mGff.getText().equals("")) {
               JOptionPane.showMessageDialog(mOwner,
                "No FASTA or GFF file was selected",
                "Invalid File", JOptionPane.ERROR_MESSAGE);
               return;
            }
            controller.setFastaFile(mFasta.getText());
            controller.setGffFile(mGff.getText());
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
