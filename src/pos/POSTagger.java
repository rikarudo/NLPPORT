package pos;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.InvalidPropertiesFormatException;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.util.InvalidFormatException;

/**
 * This class ...
 *
 * @author   Ricardo Rodrigues
 * @version  0.9.9.9
 */
public class POSTagger {
  private static final String DEFAULT_PROP =
      "resources/config/nlpport.properties";

  private POSTaggerME tagger = null;

  /**
   * Creates a new <code>POSTagger</code> object ...
   * 
   * @throws InvalidPropertiesFormatException ...
   * @throws IOException ...
   */
  public POSTagger() throws InvalidPropertiesFormatException, IOException {
    Properties properties = new Properties();
    properties.load(
        this.getClass().getClassLoader().getResourceAsStream(DEFAULT_PROP));
    this.tagger = new POSTaggerME(new POSModel(
        this.getClass().getClassLoader().getResourceAsStream(
            properties.getProperty("posTagger"))));
  }

  /**
   * Creates a new <code>POSTagger</code> object ...
   * 
   * @param  posModel ...
   * @throws InvalidFormatException ...
   * @throws IOException ...
   */
  public POSTagger(InputStream posModel)
      throws InvalidFormatException, IOException {
    this.tagger = new POSTaggerME(new POSModel(posModel));
  }

  /**
   * This method ...
   * 
   * @param  tokens ...
   * @return ...
   */
  public String[] tag(String[] tokens) {
    return tagger.tag(tokens);
  }

  /**
   * This method ...
   * 
   * @param  tokens ...
   * @return ...
   */
  public TaggedToken[] tagTokens(String[] tokens) {
    String[] tags = tagger.tag(tokens);
    TaggedToken[] taggedTokens = new TaggedToken[tags.length];
    for (int i = 0; i < tags.length; i++) {
      taggedTokens[i] = new TaggedToken(tokens[i], tags[i]);
    }
    return taggedTokens;
  }
}
