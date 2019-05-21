package dependency;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;

import org.maltparser.concurrent.ConcurrentMaltParserModel;
import org.maltparser.concurrent.ConcurrentMaltParserService;
import org.maltparser.core.exception.MaltChainedException;
import org.xml.sax.SAXException;

/**
 * This class ...
 *
 * @author   Ricardo Rodrigues
 * @version  0.9.9.9
 */
public class DependencyParser {
  private static final String DEFAULT_PROP =
      "resources/config/nlpport.properties";

  private ConcurrentMaltParserModel model = null;

  /**
   * Creates a new <code>DependencyParser</code> object ...
   * 
   * @throws IOException ...
   * @throws InvalidPropertiesFormatException ...
   * @throws MaltChainedException ...
   * @throws SAXException ...
   * @throws ParserConfigurationException ...
   * @throws NumberFormatException ...
   * 
   */
  public DependencyParser()
      throws InvalidPropertiesFormatException, IOException,
      MaltChainedException, NumberFormatException, ParserConfigurationException,
      SAXException {
    Properties properties = new Properties();
    properties.load(
        this.getClass().getClassLoader().getResourceAsStream(DEFAULT_PROP));
    // copy the malt model from the resources to a temporary location
    InputStream modelInput = 
        this.getClass().getClassLoader().getResourceAsStream(
            properties.getProperty("maltModel"));
    File tempModel = File.createTempFile(
        this.getClass().getName() + "$", ".mco");
    OutputStream modelOutput = new FileOutputStream(tempModel);
    int length = 0;
    byte[] buffer = new byte[4096];
    while ((length = modelInput.read(buffer)) > 0) {
      modelOutput.write(buffer, 0, length);
    }
    modelInput.close();
    modelOutput.close();
    // initialize the model
    model = ConcurrentMaltParserService.initializeParserModel(
        tempModel.toURI().toURL());
  }

  /**
   * Creates a new <code>DependencyParser</code> object ...
   *
   * @param  parserModel ...
   * @throws MalformedURLException ...
   * @throws MaltChainedException ...
   */
  public DependencyParser(File parserModel)
      throws MalformedURLException, MaltChainedException {
    model = ConcurrentMaltParserService.initializeParserModel(parserModel);
  }

  /**
   * This method ...
   * 
   * @param  tokens ...
   * @param  tags ...
   * @param  lemmas ...
   * @return ...
   * @throws MaltChainedException ...
   * @throws DependencyParsingException ...
   */
  public String[] parseAsCoNLLString(String[] tokens, String[] tags,
      String[] lemmas)
          throws MaltChainedException, DependencyParsingException {
    if ((tokens.length != lemmas.length) || (tokens.length != tags.length)) {
      throw new DependencyParsingException("tokens.length: " + tokens.length
          + "; lemmas.length: " + lemmas.length + "; tags.length: "
          + tags.length);
    }
    String[] dependencyTokens = new String[tokens.length];
    for (int i = 0; i < dependencyTokens.length; i++) {
      if (tags[i].contains("-")) {
        dependencyTokens[i] = (i + 1) + "\t" + tokens[i] + "\t" + lemmas[i]
            + "\t" + tags[i].substring(0, tags[i].indexOf("-")) + "\t"
            + tags[i] + "\t_";
      }
      else {
        dependencyTokens[i] = (i + 1) + "\t" + tokens[i] + "\t" + lemmas[i]
            + "\t" + tags[i] + "\t" + tags[i] + "\t_";
      }
    }
    dependencyTokens = model.parseTokens(dependencyTokens);
    return dependencyTokens;
  }

  /**
   * This method ...
   * 
   * @param  tokens ...
   * @param  tags ...
   * @param  lemmas ...
   * @return ...
   * @throws MaltChainedException ...
   * @throws DependencyParsingException ...
   */
  public CoNLLToken[] parseAsCoNLLToken(String[] tokens, String[] tags,
      String[] lemmas) throws MaltChainedException, DependencyParsingException {
    String[] dependencyTokens = this.parseAsCoNLLString(tokens, tags, lemmas);
    CoNLLToken[] conllTokens = new CoNLLToken[dependencyTokens.length];
    for (int i = 0; i < conllTokens.length; i++) {
      String[] fields = dependencyTokens[i].split("\\t");
      conllTokens[i] = new CoNLLToken(fields);
    }
    return conllTokens;
  }
}
