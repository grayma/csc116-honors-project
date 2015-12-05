public class Definition
{
    /** Source of the definition */
    private String source;
    /** ID of the source of the definition */
    private String id;
    /** The actual definition */
    private String definition;
    /** The word for the definition */
    private String word;

    /**
     * Creates a definition for a word given a source and the definition.
     * @param source Where the definition is from.
     * @param definition The actual definition itself.
     */
    public Definition(String source, String id, String definition, String word)
    {
        this.source = source;
        this.id = id;
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
     * Mutator for source
     */
    public void Source(String source)
    {
        this.source = source;
    }

    /**
     * Getter for the source ID.
     * @return The ID of the source for this definition.
     */
    public String getId()
    {
        return this.id;
    }

    /**
     * Mutator for id
     */
    public void setId(String id)
    {
        this.id = id;
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
     * Mutator for definition
     */
    public void setDefinition(String definition)
    {
        this.definition = definition;
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
     * Mutator for word
     */
    public void setWord(String word)
    {
        this.word = word;
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
