public class Definition
{
    /** Source of the definition */
    private String source;
    /** The actual definition */
    private String definition;
    /** The word for the definition */
    private String word;

    /**
     * Creates a definition for a word given a source and the definition.
     * @param source Where the definition is from.
     * @param definition The actual definition itself.
     */
    public Definition(String source, String definition, String word)
    {
        this.source = source;
        this.definition = definition;
        this.word = word;
    }

    /**
     * Getter for the source.
     * @return The source for this definition.
     */
    public String getSource()
    {
        return this.source;
    }

    /**
     * Getter for the definition.
     * @return The actual definition for this definition object.
     */
    public String getDefinition()
    {
        return this.definition;
    }

    /**
     * Getter for the word of the definition.
     * @return The word of this definition.
     */
    public String getWord()
    {
        return this.word;
    }

    /**
     * String representation of the definition containing the source, word, and the definition.
     * @return source - word - definition
     */
    public String toString()
    {
        return this.getSource() + " - " + this.getWord() + " - " + this.getDefinition();
    }
}
