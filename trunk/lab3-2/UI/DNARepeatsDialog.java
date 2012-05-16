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
   private final int DIALOG_HEIGHT = 270, DIALOG_WIDTH = 500;

   private Controller controller;
   /*
    * GUI Components
    */
   private Container mPane = null, mOwner = null;
   private JDialog mDialog = null;
   private JTextField mFileRangeStart, mFileRangeEnd, mMin, mMax, mFold, mMinGap, mMaxGap;
   private JComboBox mFileType;
   private String[] comboBoxOptions = {"Select one...", "Fosmid", "Contig"};
   private JComboBox mMethodOptions;
   private String[] mMethodStrings = {"Select one...", "Repeats", "Palindroms"};

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
      mMin = new JTextField(6);
      mMax = new JTextField(6);
      mFold = new JTextField(6);
      mMinGap = new JTextField(6);
      mMaxGap = new JTextField(6);
      mMethodOptions = new JComboBox(mMethodStrings);
      mFileType = new JComboBox(comboBoxOptions);
   }

   public JTextField getMinGap(){return mMinGap;}
   public JTextField getMaxGap(){return mMaxGap;}
   public JTextField getFold(){return mFold;}
   
   /**
    * Method for initializing a default Dialog window ready for GC Content
    * input
    */
   public void init() {
      String fileInputInfo = "Set file range and select file type:";
      String variablesInfo = "Enter the minimum and maximun repeats/palindroms to look for:";
      JPanel fileField = prepareInputFilesField(mFileRangeStart, mFileRangeEnd, mFileType);

      JPanel inputVariables = prepareInputValuesField(mMin, "Min", mMax, "Max", null, true);
      JPanel palindromValues = prepareInputValuesField(mMinGap, "Min Gap", mMaxGap, "Max Gap", mFold, false);
      JPanel operationOptions = prepareOperationOptions(mMethodOptions);
      
      mPane.add(prepareStandAloneLabel(fileInputInfo));
      mPane.add(fileField);
      mPane.add(prepareStandAloneLabel(variablesInfo));
      mPane.add(inputVariables);
      mPane.add(operationOptions);
      mPane.add(palindromValues);
      mPane.add(initControls());
      mPane.validate();
   }

   /**
    * Convenience method for constructing a JPanel that contains the JTextField
    * and file browse button used for selecting a file.
    */
   private JPanel prepareInputFilesField(JTextField start, JTextField end, JComboBox options) {
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
   private JPanel prepareInputValuesField(JTextField minLength, String minLabel,
           JTextField maxLength, String maxLabel, JTextField kFold, boolean enabled) {
      JPanel aPanel = new JPanel();

      aPanel.setLayout(new FlowLayout(FlowLayout.LEADING));

      aPanel.add(new JLabel(minLabel));
      aPanel.add(minLength);
      aPanel.add(new JLabel(maxLabel));
      aPanel.add(maxLength);
      if(!enabled){
          minLength.setEnabled(false);
          maxLength.setEnabled(false);
          kFold.setEnabled(false);
          aPanel.add(new JLabel("Fold"));
          aPanel.add(kFold);
      }
      
      return aPanel;
   }
   
   private JPanel prepareOperationOptions(JComboBox options) {
       JPanel opField = new JPanel();
       options.addActionListener(new ActionListener(){
           public void actionPerformed(ActionEvent e) {
               JComboBox cb = (JComboBox)e.getSource();
               int i = cb.getSelectedIndex();
               if (i == 2) {
                   mMinGap.setEnabled(true);
                   mMaxGap.setEnabled(true);
                   mFold.setEnabled(false);
               } else if (i == 1) {
                   mMinGap.setEnabled(false);
                   mMaxGap.setEnabled(false);
                   mFold.setEnabled(true);
               } else {
                   mMinGap.setEnabled(false);
                   mMaxGap.setEnabled(false);
                   mFold.setEnabled(false);
               }
           }
       });
       opField.setLayout(new FlowLayout(FlowLayout.LEADING));
       opField.add(new JLabel("Find"));
       opField.add(options);
       
       return opField;
   }
   
   private JPanel prepareStandAloneLabel(String s) {
       JPanel holder = new JPanel();
       
       holder.setLayout(new FlowLayout(FlowLayout.LEADING));
       
       holder.add(new JLabel(s));
       
       return holder;
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
            if (mMin.getText().equals("") || mMax.getText().equals("") || mMethodOptions.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(mOwner,
                "Min or max repeat/palindrom length not set, or Operation not selected.",
                "Invalid input", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (mFileType.getSelectedIndex() == 2)
                controller.setContig(true);
            else
                controller.setContig(false);
            
            try {
            if(mMethodOptions.getSelectedIndex() == 1){
                if (mFold.getText().equals("")) {
                    JOptionPane.showMessageDialog(mOwner,
                    "\'Fold\' field not set.",
                    "Invalid input", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                controller.setMainWindowOutput("Searching for Repeats...");
                controller.findRepeats(Integer.valueOf(mFileRangeStart.getText()),
                        Integer.valueOf(mFileRangeEnd.getText()),
                        Integer.valueOf(mMin.getText()),
                        Integer.valueOf(mMax.getText()),
                        Integer.valueOf(mFold.getText()));
            } else if (mMethodOptions.getSelectedIndex() == 2) {
                if (mMinGap.getText().equals("") || mMaxGap.getText().equals("")) {
                    JOptionPane.showMessageDialog(mOwner,
                    "Min of max gap not set.",
                    "Invalid input", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                controller.setMainWindowOutput("Searching for Palindormes...");
                controller.findPalindromes(Integer.valueOf(mFileRangeStart.getText()),
                        Integer.valueOf(mFileRangeEnd.getText()),
                        Integer.valueOf(mMin.getText()),
                        Integer.valueOf(mMax.getText()),
                        Integer.valueOf(mMinGap.getText()),
                        Integer.valueOf(mMaxGap.getText()));
            }
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
