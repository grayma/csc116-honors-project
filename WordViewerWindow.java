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
public class WordViewerWindow extends JPanel
{
    /** Word for this application */
    private Word[] words;
    /** Definition currently being viewed */
    private int currentIndex;

    /** Definition text area */
    private JTextArea definitionTextArea;
    /** Previous definition button */
    private JButton previousButton;
    /** Next definition button */
    private JButton nextButton;
    /** Show definition button */
    private JButton showButton;
    /** Label for progress through definition */
    private JLabel progressLabel;

    /**
     * Main constructor of this window.
     */
    public WordViewerWindow(Word[] words)
    {
        this.words = words;
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
        progressLabel = new JLabel("Viewing Word (" + this.words[0].getWord() + ") 1/" 
            + this.words.length,
            SwingConstants.CENTER);
        this.add(progressLabel, BorderLayout.NORTH);
        //setup and add definition text area
        definitionTextArea = new JTextArea("");
        definitionTextArea.setEnabled(false);
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
        showButton = new JButton(new AbstractAction("Show") {
            public void actionPerformed(ActionEvent e) {
                showDefinition();
            }
        });
        bottomButtonPanel.add(previousButton, BorderLayout.WEST);
        bottomButtonPanel.add(nextButton, BorderLayout.EAST);
        bottomButtonPanel.add(showButton, BorderLayout.CENTER);
        this.add(bottomButtonPanel, BorderLayout.SOUTH);
        //this.add(dataTable, BorderLayout.CENTER);
    }

    /**
     * Entry-point to this window.
     * @param words Words to choose
     */
    public static WordViewerWindow showWords(Word[] paramWords)
    {
        WordViewerWindow window = new WordViewerWindow(paramWords);
        JOptionPane.showMessageDialog(null,window,
            "Definition Chooser",JOptionPane.PLAIN_MESSAGE);
        return window;
    }

    /**
     * Used when the reader wants to view the next definition
     */
    private void next()
    {
        if (currentIndex == words.length - 1)
            currentIndex = 0;
        else
            currentIndex++;
        progressLabel.setText("Viewing Word (" + this.words[currentIndex].getWord() + ") "
            + (currentIndex + 1) + "/" + this.words.length);
        definitionTextArea.setText("");
    }

    /**
     * Used when the reader wants to view the previous definition
     */
    private void previous()
    {
        if (currentIndex == 0)
            currentIndex = this.words.length - 1;
        else
            currentIndex++;
        progressLabel.setText("Viewing Word (" + this.words[currentIndex].getWord() + ") "
            + (currentIndex + 1) + "/" + this.words.length);
        definitionTextArea.setText("");
    }

    /**
     * Used when the reader wants to view a definition
     */
    private void showDefinition()
    {
        definitionTextArea.setText(words[currentIndex].getMainDefinition());
    }
}
