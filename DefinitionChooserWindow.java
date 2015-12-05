import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * Window for choosing a definition.
 *
 * @author Matthew Gray (mrgray4@ncsu.edu)
 */
public class DefinitionChooserWindow extends JPanel
{
    /** Word for this application */
    private Word currentWord;
    /** Definition currently being viewed */
    private int currentIndex;

    /** Definition text area */
    private JTextArea definitionTextArea;
    /** Previous definition button */
    private JButton previousButton;
    /** Next definition button */
    private JButton nextButton;
    /** Label for progress through definition */
    private JLabel progressLabel;

    /**
     * Main constructor of this window.
     */
    public DefinitionChooserWindow(Word word)
    {
        this.currentWord = word;
        this.currentIndex = 0;
        initUi();
    }

    /**
     * Initializes the GUI
     */
    private void initUi()
    {
        setSize(670, 330);

        //set layout and add progress label
        this.setLayout(new BorderLayout());
        progressLabel = new JLabel("Viewing Definition 1/" 
            + this.currentWord.getAllDefinitions().length,
            SwingConstants.CENTER);
        this.add(progressLabel, BorderLayout.NORTH);
        //setup and add definition text area
        definitionTextArea = new JTextArea(currentWord.getDefinitions());
        JScrollPane scroll = new JScrollPane(definitionTextArea);
        this.add(scroll, BorderLayout.CENTER);
        //setup and add previous, next, and choose definition buttons
        JPanel bottomButtonPanel = new JPanel(new BorderLayout());
        previousButton = new JButton(new AbstractAction("Previous") {
            public void actionPerformed(ActionEvent e) {
                previous();
            }
        });
        nextButton = new JButton(new AbstractAction("Next") {
            public void actionPerformed(ActionEvent e) {
                next();
            }
        });
        bottomButtonPanel.add(previousButton, BorderLayout.WEST);
        bottomButtonPanel.add(nextButton, BorderLayout.EAST);
        this.add(bottomButtonPanel, BorderLayout.SOUTH);
        //this.add(dataTable, BorderLayout.CENTER);
    }

    /**
     * Entry-point to program.
     * @param word Word to choose
     */
    public static String chooseDefinition(Word paramWord)
    {
        DefinitionChooserWindow window = new DefinitionChooserWindow(paramWord);
        JOptionPane.showMessageDialog(null,window,
            "Definition Chooser",JOptionPane.PLAIN_MESSAGE);
        return window.getCurrentVisibleDefinition();
    }

    /**
     * Used when the reader wants to view the next definition
     */
    private void next()
    {
        if (currentIndex == currentWord.getAllDefinitions().length - 1)
            currentIndex = 0;
        else
            currentIndex++;
        progressLabel.setText("Viewing Definition " + (currentIndex + 1) + "/" 
            + this.currentWord.getAllDefinitions().length);
        definitionTextArea.setText(this.currentWord.getAllDefinitions()[currentIndex].getDefinition());
    }

    /**
     * Used when the reader wants to view the previous definition
     */
    private void previous()
    {
        if (currentIndex == 0)
            currentIndex = this.currentWord.getAllDefinitions().length - 1;
        else
            currentIndex++;
        progressLabel.setText("Viewing Definition " + (currentIndex + 1) + "/" 
            + this.currentWord.getAllDefinitions().length);
        definitionTextArea.setText(this.currentWord.getAllDefinitions()[currentIndex].getDefinition());
    }

    /**
     * Gets the current defintion viewed by this window.
     * @return The current definition.
     */
    public String getCurrentVisibleDefinition()
    {
        return this.definitionTextArea.getText();
    }
}
