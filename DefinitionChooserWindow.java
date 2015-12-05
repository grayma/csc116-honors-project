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
public class DefinitionChooserWindow extends JFrame
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
    /** Choose definition button */
    private JButton chooseButton;
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
        setTitle("Definition Chooser");
        setSize(670, 330);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        //set layout and add progress label
        this.setLayout(new BorderLayout());
        progressLabel = new JLabel("Viewing Definition 1/" 
            + this.currentWord.getAllDefinitions().length,
            SwingConstants.CENTER);
        this.add(progressLabel, BorderLayout.NORTH);
        //setup and add definition text area
        definitionTextArea = new JTextArea(currentWord.getDefinitions());
        this.add(definitionTextArea, BorderLayout.CENTER);
        //setup and add previous, next, and choose definition buttons
        JPanel bottomButtonPanel = new JPanel(new BorderLayout());
        previousButton = new JButton("Previous Definition (CTRL-P)");
        nextButton = new JButton("Next Definition (CTRL-N)");
        chooseButton = new JButton("Choose Definition (CTRL-E)");
        bottomButtonPanel.add(previousButton, BorderLayout.WEST);
        bottomButtonPanel.add(nextButton, BorderLayout.EAST);
        bottomButtonPanel.add(chooseButton, BorderLayout.CENTER);
        this.add(bottomButtonPanel, BorderLayout.SOUTH);
        //this.add(dataTable, BorderLayout.CENTER);
    }

    /**
     * Overloaded from JFrame, manages creation of layout.
     * @param arg JComponent argument to create layout with.
     */
    private void createLayout(JComponent arg)
    {
        //gets the frames content pane and sets it to a grouplayout style
        Container pane = getContentPane();
        GroupLayout groupLayout = new GroupLayout(pane);
        pane.setLayout(groupLayout);

        groupLayout.setAutoCreateContainerGaps(true); //automatically manage gaps between containers
        groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup()
                .addComponent(arg)); //manage horizontal addition of components
        groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
                .addComponent(arg)); //manage vertical layout of components
    }

    /**
     * Entry-point to program.
     * @param word Word to choose
     */
    public static Definition chooseDefinition(Word paramWord)
    {
        DefinitionChooserWindow window = new DefinitionChooserWindow(paramWord);
        window.setVisible(true);
        return null;
    }

    /**
     * Used when the reader wants to choose the current definition
     */
    private void choose()
    {

    }

    /**
     * Used when the reader wants to view the next definition
     */
    private void next()
    {

    }

    /*
     * Used when the reader wants to view the previous definition
     */
    private void previous()
    {

    }
}
