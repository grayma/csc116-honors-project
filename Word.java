//file and internet io
import java.net.URL;
import java.net.HttpURLConnection;
import java.net.UnknownHostException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.util.Scanner;
//xml io
import javax.xml.parsers.DocumentBuilder; //document builder
import javax.xml.parsers.DocumentBuilderFactory; //...factory
import org.w3c.dom.*; //contains Document and Element etc
import javax.xml.transform.*; //this and 3 below are xml to string
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import javax.xml.xpath.*; //xpath for removing empty text nodes

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
    private Definition[] otherDefinitions;
    /** Service URL for requesting definition */
    private static final String SERVICE_URL = 
        "http://services.aonaware.com//DictService/DictService.asmx/Define?word=";

    //xml indicies
    //definition indicies
    /** Location of the word itself */
    private static final int XML_WORD_LOC = 0;
    /** Location of the dictionary branch node */
    private static final int XML_DICTIONARY_BRANCH_LOC = 1;
    /** Location of the textual definition */
    private static final int XML_DEFINITION_LOC = 2;
    //dictionary indicies
    /** Location of the dictionary ID */
    private static final int XML_DICTIONARY_ID_LOC = 0;
    /** Location of the dictionary title */
    private static final int XML_DICTIONARY_NAME_LOC = 1;

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
        this(word);
        this.definition = definition;
    }

    /**
     * Gets the definitions of a word, sets the main one if necessary, and returns it.
     * @param overwrite if true, then function will attempt to retrieve from online
     * even if the definition is already set.
     * @return String containing the main definition of the word.
     */
    public String getDefinitions(ByteArrayInputStream inputStream, boolean overwrite)
    {
        if (overwrite || (definition == null || definition.isEmpty())) {
            //get definition from online service
            //if not able to access internet, then do something
            try {
                //now to parse the XML
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(inputStream);
                removeEmptyTextNodes(doc);
                NodeList list = doc.getElementsByTagName("Definition");
                int numDefinitions = list.getLength(); 
                otherDefinitions = new Definition[numDefinitions];
                for (int i = 0; i < numDefinitions; i++)
                {
                    Node node = list.item(i);
                    NodeList children = node.getChildNodes(); //gets each child node of the definition
                    //get all data
                    String definition = children.item(XML_DEFINITION_LOC).getTextContent();
                    String word = children.item(XML_WORD_LOC).getTextContent();
                    NodeList dictionaryNode = children.item(XML_DICTIONARY_BRANCH_LOC).getChildNodes();
                    String dictionaryId = dictionaryNode.item(XML_DICTIONARY_ID_LOC).getTextContent();
                    String dictionaryName = dictionaryNode.item(XML_DICTIONARY_NAME_LOC).getTextContent();
                    //now we've got all the data lets add it to the array
                    otherDefinitions[i] = new Definition(dictionaryName, dictionaryId, definition, word);
                }
                if (otherDefinitions.length == 0) {
                    otherDefinitions = new Definition[] {new Definition("nil", "nil", "NO DEFINITION FOUND", word)};
                }
                this.setMainDefinition(otherDefinitions[0].getDefinition());
                return otherDefinitions[0].getDefinition();
            } catch (Exception ex) {
                ex.printStackTrace();
                return "ERROR! " + ex;
            }
        } else {
            return this.definition;
        }
    }

    /**
     * Gets a ByteArrayInputStream to parse a word from the definition API.
     * @return ByteArrayInputStream containing definition XML, null if trouble
     * parsing or getting the definition.
     */
    public ByteArrayInputStream getStreamFromOnlineXML()
    {
        try {
            //download the xml definition page of the word
            String urlSafeWord = URLEncoder.encode(this.word, "UTF-8");
            InputStream response = new URL(SERVICE_URL + urlSafeWord).openStream();
            Scanner responseReader = new Scanner(response).useDelimiter("\\A");
            if (responseReader.hasNext()) {
                //gets a clean version of xml the document can parse
                String xml = responseReader.next();
                response.close();
                xml = xml.trim().replaceFirst("^([\\W]+)<","<");
                ByteArrayInputStream stream = new ByteArrayInputStream(xml.getBytes("utf-8"));
                return stream;
            } else {
                return null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Gets the definitions of a word, sets the main one if necessary, and returns it.
     * @return String containint the definition of the word.
     */
    public String getDefinitions()
    {
        return this.getDefinitions(getStreamFromOnlineXML(), false);
    }

    /**
     * Gets all definitions possible for this term.
     * @return Definition array containing all definitions.  Returns null if trouble getting definitions.
     */
    public Definition[] getAllDefinitions()
    {
        if (this.otherDefinitions != null) {
            return this.otherDefinitions;
        } else {
            getDefinitions();
            return this.otherDefinitions;
        }
    }

    /**
     * Gets XML string for this definition so that it may be printed.
     * @return XML node that can be saved of this word.
     */
    public Element getWordXMLNode(Document doc)
    {
        try {
            Element rootWordElement = doc.createElement("WordDefinitions");
            rootWordElement.setAttribute("word", this.word);
            rootWordElement.setAttribute("mainDefinition", this.getMainDefinition());
            if (this.otherDefinitions == null) {
                this.getDefinitions();
            }
            for (int i = 0; i < this.otherDefinitions.length; i++) {
                //main definition node
                Element definition = doc.createElement("Definition");

                //word
                Element word = doc.createElement("Word");
                word.setTextContent(otherDefinitions[i].getWord());

                //dictionary
                Element dictionary = doc.createElement("Dictionary");
                Element id = doc.createElement("Id");
                id.setTextContent(otherDefinitions[i].getId());
                Element source = doc.createElement("Name");
                source.setTextContent(otherDefinitions[i].getSource());
                dictionary.appendChild(id);
                dictionary.appendChild(source);

                //definition
                Element wordDefinition = doc.createElement("WordDefinition");
                wordDefinition.setTextContent(otherDefinitions[i].getDefinition());

                //append all the children
                definition.appendChild(word);
                definition.appendChild(dictionary);
                definition.appendChild(wordDefinition);
                rootWordElement.appendChild(definition);

            }
            return rootWordElement;
        } catch(Exception ex) {
           ex.printStackTrace();
           return null;
        }
    }

    /**
     * Gets the string of an XML document containing multiple words for saving purposes.
     * @param words The words to save.
     * @return String containing XML containing all words.
     */
    public static String getMultipleWordXML(Word[] words)
    {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.newDocument();
            Element root = doc.createElement("Root");
            for (int i = 0; i < words.length; i++) {
                root.appendChild(words[i].getWordXMLNode(doc));
            }
            doc.appendChild(root);
            return getStringFromDocument(doc);
            
        } catch(Exception ex) {
           ex.printStackTrace();
           return null;
        }
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
     * Gets the main definition.
     * @return The main definition.
     */
    public String getMainDefinition()
    {
        return this.definition;
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

    /**
     * Converts an XML document to a string.
     * From Zaz Gmy on this StackOverflow post:
     * http://stackoverflow.com/questions/10356258/how-do-i-convert-a-org-w3c-dom-document-object-to-a-string
     * @param doc The document to convert to a String.
     */
    private static String getStringFromDocument(Document doc)
    {
        try {
           DOMSource domSource = new DOMSource(doc);
           StringWriter writer = new StringWriter();
           StreamResult result = new StreamResult(writer);
           TransformerFactory tf = TransformerFactory.newInstance();
           Transformer transformer = tf.newTransformer();
           transformer.transform(domSource, result);
           return writer.toString();
        } catch(TransformerException ex) {
           ex.printStackTrace();
           return null;
        }
    } 

    /**
     * Removes empty #text nodes from a document.
     * From James Murty on this StackOverflow post:
     * http://stackoverflow.com/questions/978810/how-to-strip-whitespace-only-text-nodes-from-a-dom-before-serialization
     * @param doc The document to remove empty text nodes from.
     */
    private static void removeEmptyTextNodes(Document doc)
    {
        try {
            XPathFactory xpathFactory = XPathFactory.newInstance();
            // XPath to find empty text nodes.
            XPathExpression xpathExp = xpathFactory.newXPath().compile(
                    "//text()[normalize-space(.) = '']");  
            NodeList emptyTextNodes = (NodeList) 
                    xpathExp.evaluate(doc, XPathConstants.NODESET);

            // Remove each empty text node from document.
            for (int i = 0; i < emptyTextNodes.getLength(); i++) {
                Node emptyTextNode = emptyTextNodes.item(i);
                emptyTextNode.getParentNode().removeChild(emptyTextNode);
            }
        } catch (Exception ex) {
           ex.printStackTrace();           
        }
    }
}

