import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

/**
 * MainWindow for definition finder.
 *
 * @author Matthew Gray (mrgray4@ncsu.edu)
 */
public class MainWindow extends JFrame
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
    public MainWindow()
    {
        initUi();
        currentWords = new Word[0];
        newFile();
    }

    /**
     * Initializes the GUI
     */
    private void initUi()
    {
        setTitle("Definition Finder");
        setSize(400, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        /////////////////////////////////////////////////////////////////////////////////////////////
        //JMenuBar - help from https://docs.oracle.com/javase/tutorial/uiswing/components/menu.html//
        /////////////////////////////////////////////////////////////////////////////////////////////

        //Where the unimportant GUI is created:
        JMenuBar menuBar;
        JMenuItem menuItem;

        //Create the menu bar.
        menuBar = new JMenuBar();

        //Build the file menu.
        fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_A);
        fileMenu.getAccessibleContext().setAccessibleDescription("The only menu in this program that has menu items");
        menuBar.add(fileMenu);
        //a group of JMenuItems for the file menu
        menuItem = new JMenuItem(new AbstractAction("New") {
            public void actionPerformed(ActionEvent e) {
                newFile();
            }
        });
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        fileMenu.add(menuItem);
        menuItem = new JMenuItem(new AbstractAction("Save") {
            public void actionPerformed(ActionEvent e) {
                saveFile();            
            }
        });
        menuItem.setMnemonic(KeyEvent.VK_S);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        fileMenu.add(menuItem);
        menuItem = new JMenuItem(new AbstractAction("Open") {
            public void actionPerformed(ActionEvent e) {
                openFile();            
            }
        });
        menuItem.setMnemonic(KeyEvent.VK_P);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        fileMenu.add(menuItem);
        menuItem = new JMenuItem(new AbstractAction("Print") {
            public void actionPerformed(ActionEvent e) {
                print();            
            }
        });
        menuItem.setMnemonic(KeyEvent.VK_P);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));
        fileMenu.add(menuItem);

        //Build the word menu.
        wordMenu = new JMenu("Word");
        wordMenu.setMnemonic(KeyEvent.VK_A);
        wordMenu.getAccessibleContext().setAccessibleDescription("The only menu in this program that has menu items");
        menuBar.add(wordMenu);
        //a group of JMenuItems for the word menu
        menuItem = new JMenuItem(new AbstractAction("Add Word") {
            public void actionPerformed(ActionEvent e) {
                addWord();
            }
        });
        menuItem.setMnemonic(KeyEvent.VK_A);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.ALT_MASK));
        wordMenu.add(menuItem);
        menuItem = new JMenuItem(new AbstractAction("Remove Word") {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = dataTable.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null,
                        "To remove a row, one must select it.",
                        "Error!",
                        JOptionPane.ERROR_MESSAGE,
                        null);
                    return;
                }
                removeWord(selectedRow);
            }
        });
        menuItem.setMnemonic(KeyEvent.VK_R);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.ALT_MASK));
        wordMenu.add(menuItem);
        menuItem = new JMenuItem(new AbstractAction("Get Definitions") {
            public void actionPerformed(ActionEvent e) {
                getDefinitions();
            }
        });
        menuItem.setMnemonic(KeyEvent.VK_G);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, ActionEvent.ALT_MASK));
        wordMenu.add(menuItem);
        menuItem = new JMenuItem(new AbstractAction("View Definitions") {
            public void actionPerformed(ActionEvent e) {
                viewDefinitions();
            }
        });
        menuItem.setMnemonic(KeyEvent.VK_V);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.ALT_MASK));
        wordMenu.add(menuItem);

        this.setJMenuBar(menuBar);

        ///////////////////////////////////////////////////////////////////////////////////////////
        //table - help from https://docs.oracle.com/javase/tutorial/uiswing/components/table.html//
        ///////////////////////////////////////////////////////////////////////////////////////////

        String[] columnNames = {"Word", "Definition"};
        DefaultTableModel model = new DefaultTableModel(null, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
               return false;
            }
        };
        dataTable = new JTable(model);
        JScrollPane tableScroll = new JScrollPane(dataTable);
        dataTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //dataTable.setEnabled(false);

        this.setLayout(new BorderLayout());
        this.add(dataTable.getTableHeader(), BorderLayout.PAGE_START);
        this.add(tableScroll, BorderLayout.CENTER);
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
     * @param args Command line arguments.
     */
    public static void main(String[] args)
    {
        //starts the window events
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainWindow mainWindow = new MainWindow();
                mainWindow.setVisible(true);
            }
        });
    }

    //file operations
    /**
     * Starts a new file programmatically.
     */
    private void newFile()
    {
        this.filePath = "";
        this.isModified = false;
        for (int i = currentWords.length - 1; i >= 0; i--) {
            removeWord(i);
        }
    }

    /**
     * Opens a file into the GUI
     */
    private void openFile()
    {
        try {
            JFileChooser fileChooser = new JFileChooser();
            int option = fileChooser.showOpenDialog(null);
            if (option == JFileChooser.APPROVE_OPTION) {
                newFile();
                File file = fileChooser.getSelectedFile();
                Word[] words = 
                    Word.getWordsFromFile(new ByteArrayInputStream(readContentIntoByteArray(file)));
                addWord(words);
                this.currentWords = words;
                for (int i = 0; i < words.length; i++) {
                    dataTable.setValueAt(words[i].getMainDefinition(), i, 1);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }
    }

    /**
     * Saves the current words
     */
    private void saveFile()
    {
        try {
            JFileChooser fileChooser = new JFileChooser();
            int option = fileChooser.showSaveDialog(null);
            if (option == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                FileOutputStream output = new FileOutputStream(file);
                output.write(Word.getMultipleWordXML(currentWords).getBytes());
                output.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }
    }

    /**
     * Prints the current words
     */
    private void print()
    {
        try {
            this.dataTable.print(); //WOW IT'S THIS EASY???!
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }
    }

    //word operations
    /**
     * Removes a word at this index in the word[]/jtable.
     * @param index the index to remove from.
     */
    private void removeWord(int index)
    {
        //index should never be bigger than the table's length
        ((DefaultTableModel)dataTable.getModel()).removeRow(index);
        Word[] newWords = new Word[currentWords.length - 1];
        int newIndex = 0;
        for (int i = 0; i < currentWords.length; i++) {
            if (i != index) {
                newWords[newIndex++] = currentWords[i];
            }
        }
        currentWords = newWords;
    }

    private void addWord(Word[] words)
    {
        DefaultTableModel model = (DefaultTableModel)(dataTable.getModel());
        for (int i = 0; i < words.length; i++) {
            model.addRow(new Object[] { words[i].getWord(), "" });
        }
        Word[] newWords = new Word[currentWords.length + words.length];
        int index;
        for (index = 0; index < currentWords.length; index++) {
            newWords[index] = currentWords[index];
        }
        for (int i = 0; i < words.length; i++) {
            newWords[index + i] = words[i];
        }
        this.currentWords = newWords;
    }

    /**
     * Adds a word to the JTable.
     */
    private void addWord()
    {
        int n = JOptionPane.showConfirmDialog(
            null,
            "Would you like to input multiple terms (yes) or a single word (no)?",
            "Question",
            JOptionPane.YES_NO_OPTION);
        Word[] words;
        if (n == JOptionPane.YES_OPTION) {
            ArrayList<Word> list = new ArrayList<Word>();
            while (true) {
                String word = getTextualInput("Type in a term to add, leave the TextBox Blank if you are done adding terms.",
                    "Enter Your Terms"); 
                if (word == null || word.equals("")) {
                    words = new Word[list.size()];
                    words = list.toArray(words);
                    break;
                }
                list.add(new Word(word));
            }
        } else if (n == JOptionPane.NO_OPTION) {
            String word = getTextualInput("Type in a term to add, leave the TextBox Blank if you are done adding terms.",
                    "Enter Your Terms");
            words = new Word[] { new Word(word) };
        } else {
            return;
        }
        addWord(words);
    }

    /**
     * Gets the definitions of all the words in the JTable.
     * Prompts if the user wants first definition or their own choice in definition.
     */
    private void getDefinitions()
    {
        int n = JOptionPane.showConfirmDialog(
            null,
            "Would you like to automatically select the first term (yes)" + 
            " or have a choice out of all the terms (no)?",
            "Question",
            JOptionPane.YES_NO_OPTION);
        Word[] words;
        for (int i = 0; i < currentWords.length; i++) {
            String chosenDefinition;
            if (n == JOptionPane.YES_OPTION) {
                chosenDefinition = currentWords[i].getAllDefinitions()[0].getDefinition();
            } else if (n == JOptionPane.NO_OPTION) {
                DefinitionChooserWindow window = DefinitionChooserWindow.chooseDefinition(currentWords[i]);
                chosenDefinition = window.getCurrentVisibleDefinition();
                currentWords[i].getAllDefinitions()[window.getCurrentDefinitionIndex()]
                    .setDefinition(chosenDefinition);
                currentWords[i].setMainDefinition(chosenDefinition);
            } else {
                return;
            }
            dataTable.setValueAt(chosenDefinition, i, 1);
        }
    }

    /**
     * Views the definitions of all the words in the JTable.
     */
    private void viewDefinitions()
    {
        WordViewerWindow window = WordViewerWindow.showWords(this.currentWords);
    }

    //utility functions
    /**
     * Gets whether or not the file has been saved to a path at all.
     * Valid assumption is that it isn't null, set to "" on instantiation.
     * @return True if the file has been saved to a path at all, false otherwise.
     */
    private boolean isSavedAs()
    {
        if (this.filePath.equals("")) {
            return false;
        }
        return true;
    }

    /** 
     * Utility function to prompt for a word.
     * @param question The question to prompt with.
     * @param title The title to prompt in the prompt window with.
     * @return The word if entered, an empty or null String if nothing chosen.
     */
    private static String getTextualInput(String question, String title)
    {
        String word = (String)JOptionPane.showInputDialog(
                        null,
                        question,
                        title,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        null,
                        null);
        return word;
    }

    /**
     * Reads the contents of a File into a byte array.
     * Credit to http://howtodoinjava.com/2014/11/04/how-to-read-file-content-into-byte-array-in-java/
     * @param file The file to read from
     * @return The byte array read in.
     */
    private static byte[] readContentIntoByteArray(File file)
    {
        FileInputStream fileInputStream = null;
        byte[] bFile = new byte[(int) file.length()];
        try
        {
            //convert file into array of bytes
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bFile);
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bFile;
    }
}
