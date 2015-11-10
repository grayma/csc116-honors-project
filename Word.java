/**
 * Class representing a word and its definition to find.
 * @author Matthew Gray
 */
public class Word
{
    /** The word itself */
    private String word;
    /** The definition of the word */
    private String definition;

    /**
     * Instantiates a word with no definition (in order to get one).
     * @param word The word to create an object of.
     */
    public Word(String word)
    {
        this.word = word;
    }

    /**
     * Instantiates a word with a definition (if we know it for some reason).
     * @param word The word to create an object of.
     * @param definition The definition of the word.
     */
    public Word(String word, String definition)
    {
        this.word = word;
        this.definition = definition;
    }

    /**
     * Gets the definition of a word, sets it if necessary, and returns it.
     * @param overwrite if true, then function will attempt to retrieve from online
     * even if the definition is already set.
     * @return String containing the definition of the word.
     */
    public String getDefinition(boolean overwrite)
    {
        if (overwrite || (definition == null || definition.isEmpty())) {
            //get definition from online service
            //if not able to access internet, then do something
        } else {
            return this.definition;
        }
    }

    /**
     * Sets the definition, overriding whatever may be there.
     * @param definition what to set the object's definition to.
     */
    public void setDefinition(String definition)
    {
        this.definition = definition;
    }
}
