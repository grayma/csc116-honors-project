import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * MainWindow for definition finder.
 *
 * @author Matthew Gray (mrgray4@ncsu.edu)
 */
public class MainWindow extends JFrame
{
    public MainWindow()
    {
        initUi();
    }

    private void initUi()
    {
        setTitle("Definition Finder");
        setSize(400, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        /////////////////////////////////////////////////////////////////////////////////////////////
        //JMenuBar - help from https://docs.oracle.com/javase/tutorial/uiswing/components/menu.html//
        /////////////////////////////////////////////////////////////////////////////////////////////

        //Where the GUI is created:
        JMenuBar menuBar;
        JMenu menu;
        JMenuItem menuItem;

        //Create the menu bar.
        menuBar = new JMenuBar();

        //Build the first menu.
        menu = new JMenu("Word Actions");
        menu.setMnemonic(KeyEvent.VK_A);
        menu.getAccessibleContext().setAccessibleDescription("The only menu in this program that has menu items");
        menuBar.add(menu);

        //a group of JMenuItems
        menuItem = new JMenuItem("Add Word", KeyEvent.VK_A);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.ALT_MASK));
        menu.add(menuItem);
        menuItem = new JMenuItem("Remove Word", KeyEvent.VK_R);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.ALT_MASK));
        menu.add(menuItem);
        menuItem = new JMenuItem("Get Definitions", KeyEvent.VK_G);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, ActionEvent.ALT_MASK));
        menu.add(menuItem);

        //Build second menu in the menu bar.
        menu = new JMenu("About Application");
        menu.setMnemonic(KeyEvent.VK_N);
        menuBar.add(menu);

        this.setJMenuBar(menuBar);

        ///////////////////////////////////////////////////////////////////////////////////////////
        //table - help from https://docs.oracle.com/javase/tutorial/uiswing/components/table.html//
        ///////////////////////////////////////////////////////////////////////////////////////////

        String[] columnNames = {"Word", "Defniition"};

        Object[][] data = {{"word1", "definition1"}, {"word2", "definition2"}, {"word3", "definition3"}};

        JTable table = new JTable(data, columnNames);

        this.setLayout(new BorderLayout());
        this.add(table.getTableHeader(), BorderLayout.PAGE_START);
        this.add(table, BorderLayout.CENTER);

    }

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
}