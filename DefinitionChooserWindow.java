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
    //component variables
    /** Variable to contain the word data table. */
    private JTable dataTable;
    /** Variable to contain the main file operation menu. */
    private JMenu fileMenu;
    /** Variable to contain the main word operation menu. */
    private JMenu wordMenu;
    /** Variable to contain the about menu. */
    private JMenu aboutMenu;

    //variables to manage the state of the application
    /** Field to keep track of the currently opened file*/
    private String filePath;
    /** Field to keep track of whether the currently opened file is saved */
    private boolean isModified;
    /** Field to keep track of current Word[] */
    private Word[] currentWords;

    /**
     * Main constructor of this window.
     */
    public DefinitionChooserWindow(Word word)
    {
        initUi();
        currentWords = new Word[0];
    }

    /**
     * Initializes the GUI
     */
    private void initUi()
    {
        setTitle("Definition Chooser");
        setSize(400, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        this.setLayout(new BorderLayout());
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
}
