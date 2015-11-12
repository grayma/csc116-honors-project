//file and internet io
import java.net.URL;
import java.net.HttpURLConnection;
import java.net.UnknownHostException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.util.Scanner;
//xml io
import javax.xml.parsers.DocumentBuilder; //document builder
import javax.xml.parsers.DocumentBuilderFactory; //...factory
import org.w3c.dom.*; //contains Document and Element etc

/**
 * Class representing a word and 
 * its definition to find.
 * @author Matthew Gray
 */
public class Word
{
    /** The word itself */
    private String word;
    /** The definition of the word */
    private String definition;
    /** Other definitions of the word */
    private String[] otherDefinitions;
    /** Service URL for requesting definition */
    private static final String SERVICE_URL = 
        "http://services.aonaware.com//DictService/DictService.asmx/Define?word=";

    /**
     * Instantiates a word with no definition (in order to get one).
     * @param word The word to create an object of.
     */
    public Word(String word)
    {
        /* added url encoding so this is unnecessary
        if (word.contains(" ")) {
            throw new IllegalArgumentException("Cannot contain spaces " +
                    "or be more than one word.");
        }*/
        this.word = word;
    }

    /**
     * Instantiates a word with a definition (if we know it for some reason).
     * @param word The word to create an object of.
     * @param definition The definition of the word.
     */
    public Word(String word, String definition)
    {
        this(word);
        this.definition = definition;
    }

    /**
     * Gets the definitions of a word, sets the main one if necessary, and returns it.
     * @param overwrite if true, then function will attempt to retrieve from online
     * even if the definition is already set.
     * @return String containing the definition of the word.
     */
    public String getDefinitions(boolean overwrite)
    {
        if (overwrite || (definition == null || definition.isEmpty())) {
            //get definition from online service
            //if not able to access internet, then do something
            try {
                //download the xml definition page of the word
                String urlSafeWord = URLEncoder.encode(this.word, "UTF-8");
                InputStream response = new URL(SERVICE_URL + urlSafeWord).openStream();
                Scanner responseReader = new Scanner(response).useDelimiter("\\A");
                if (responseReader.hasNext()) {
                    //now to parse the XML
                    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                    DocumentBuilder db = dbf.newDocumentBuilder();
                    //gets a clean version of xml the document can parse
                    String xml = responseReader.next();
                    response.close();
                    xml = xml.trim().replaceFirst("^([\\W]+)<","<");
                    Document doc = db.parse(new ByteArrayInputStream(xml.getBytes("utf-8")));
                    //now parse the xml in the document
                    return "doc parsed"; 
                } else {
                    return "ERROR!  Definition not found.";
                }
            } catch (UnknownHostException uEx) {
                return "ERROR! Definition service unavailable (potentially no internet).";
            } catch (MalformedURLException mEx) {
                return "ERROR! Malformed URL.";  
            } catch (Exception ex) {
                return "ERROR! " + ex.toString();
            }
        } else {
            return this.definition;
        }
    }

    /**
     * Gets the definitions of a word, sets the main one if necessary, and returns it.
     * @return String containint the definition of the word.
     */
    public String getDefinitions()
    {
        return this.getDefinitions(false);
    }

    /**
     * Sets the definition, overriding whatever may be there.
     * @param definition what to set the object's definition to.
     */
    public void setMainDefinition(String definition)
    {
        this.definition = definition;
    }

    /**
     * Gets the word itself.
     * @return the string representing the word of this object.
     */
    public String getWord()
    {
        return this.word;
    }

    /**
     * Returns a String representing this word and definition pair.
     * @return the string representing this word and its definition.
     */
    public String toString()
    {
        return this.getWord() + " - " + this.getDefinitions();
    }

    /**
     * Comparison of this object with another object, potentially another word.
     * If the definition of either word isn't set, then the comparison will be false.
     * This was chosen to conserve resources/time when doing potential large batch
     * comparisons.
     * @param o the other object to compare to.
     * @return whether this word is equal to another word, true if so, false if otherwise.
     */
    public boolean equals(Object o)
    {
        if (o instanceof Word) {
            Word otherWord = (Word)o;
            if (this.getWord().equals(otherWord.getWord())) {
                if (this.definition.equals(otherWord.getDefinitions())) {
                    return true;
                }
            }
        }
        return false;
    }
}
