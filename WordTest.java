/**
 * Tests for Word class.
 * @author Matthew Gray
 */
public class WordTest
{
    public static void main(String[] args)
    {
        Word word = new Word("computer");
        Word word2 = new Word("word");
        System.out.println(word);
        Definition[] otherDefinitions = word.getAllDefinitions();
        for (int i = 0; i < otherDefinitions.length; i++)
        {
            System.out.println(i + " - " + otherDefinitions[i]);
        }
        System.out.println(Word.getMultipleWordXML(new Word[]{word, word2}));
    }
}
