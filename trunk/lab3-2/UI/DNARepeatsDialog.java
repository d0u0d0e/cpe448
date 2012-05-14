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

public class DNARepeatsDialog extends JDialog {
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
   private JTextField mFileRangeStart, mFileRangeEnd, mWindow, mSlide;
   private JComboBox mFileType;
   private String[] comboBoxOptions = {"Select one...", "Fosmid", "Contig"};

   public DNARepeatsDialog(Controller controller) {
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

   public DNARepeatsDialog(Frame owner, Controller controller, String title) {
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

      mFileRangeStart = new JTextField(6);
      mFileRangeEnd = new JTextField(6);
      mWindow = new JTextField(6);
      mSlide = new JTextField(6);
      mFileType = new JComboBox(comboBoxOptions);
   }

   /**
    * Method for initializing a default Dialog window ready for GC Content
    * input
    */
   public void init() {
      JLabel fileSelectionLb = new JLabel("Set file range and select file type:");
      JPanel fileField = prepareInputDataField(mFileRangeStart, mFileRangeEnd, mFileType);
      
      JPanel windowField = prepareWindowSizeField("Window:", mWindow);
      JPanel slideField = prepareWindowSizeField("Slide:", mSlide);

      JPanel nucleotideRangeField = new JPanel();
      nucleotideRangeField.setLayout(new FlowLayout(FlowLayout.LEADING));
      nucleotideRangeField.add(windowField);
      nucleotideRangeField.add(slideField);
      
      //mPane.add(fileSelectionLb);
      //mPane.add(fileField);

      //mPane.add(nucleotideRangeField);
      
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
            if (mFileRangeStart.getText().equals("") || mFileRangeEnd.getText().equals("")
                    || mFileType.getSelectedIndex() == 0) {
               JOptionPane.showMessageDialog(mOwner,
                "No Range was given, or file type not set.",
                "Invalid File", JOptionPane.ERROR_MESSAGE);
               return;
            }
            if (mFileType.getSelectedIndex() == 2)
                controller.setContig(true);
            else
                controller.setContig(false);
            
            if (!mWindow.getText().equals(""))
                controller.setWindow(Integer.parseInt(mWindow.getText()));
            if (!mSlide.getText().equals(""))
                controller.setSlide(Integer.parseInt(mSlide.getText()));
            try {
                controller.setMainWindowOutput("Calculating...\n");
                controller.doCalculations(Integer.valueOf(mFileRangeStart.getText()),
                        Integer.valueOf(mFileRangeEnd.getText()));
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
