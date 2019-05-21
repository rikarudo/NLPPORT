package entity;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.InvalidFormatException;
import opennlp.tools.util.Span;

/**
 * This class ...
 *
 * @author   Ricardo Rodrigues
 * @version  0.9.9.9
 */
public class EntityFinder {
  private static final String DEFAULT_PROP =
      "resources/config/nlpport.properties";

  private NameFinderME finder = null;

  /**
   * Creates a new ...
   * 
   * @throws InvalidPropertiesFormatException ...
   * @throws IOException ...
   */
  public EntityFinder() throws InvalidPropertiesFormatException, IOException {
    Properties properties = new Properties();
    properties.load(
        this.getClass().getClassLoader().getResourceAsStream(DEFAULT_PROP));
    this.finder = new NameFinderME(new TokenNameFinderModel(
        this.getClass().getClassLoader().getResourceAsStream(
            properties.getProperty("entityFinder"))));
  }

  /**
   * Creates a new ...
   * 
   * @param  finderModel ...
   * @throws InvalidFormatException ...
   * @throws IOException ...
   */
  public EntityFinder(InputStream finderModel)
      throws InvalidFormatException, IOException {
    this.finder = new NameFinderME(new TokenNameFinderModel(finderModel));
  }

  /**
   * This method ...
   * 
   * @param  tokens ...
   * @return ...
   */
  public Entity[] find(String[] tokens) {
    Span[] spans = NameFinderME.dropOverlappingSpans(finder.find(tokens));
    ArrayList<Entity> entities = new ArrayList<Entity>();
    for (Span span: spans) {
      String text = new String();
      for (int i = span.getStart(); i < span.getEnd(); i++) {
        text += tokens[i] + " ";
      }
      text = text.trim();
      String type = span.getType();
      String[] spanTokens = new String[span.getEnd() - span.getStart()];
      for (int i = 0; i < spanTokens.length; i++) {
        spanTokens[i] = tokens[i + span.getStart()];
      }
      entities.add(new Entity(text, type, spanTokens));
    }
    return entities.toArray(new Entity[entities.size()]);
  }

  /**
   * This method ...
   * 
   * @param  tokens ...
   * @return ...
   */
  public String bindEntityTokens(String[] tokens) {
    Entity[] entities = this.find(tokens);
    String sentence = new String();
    for (String token : tokens) {
      sentence = sentence.concat(token);
      sentence = sentence.concat(" ");
    }
    sentence = sentence.trim();
    for (Entity entity : entities) {
      if (entity.getTokens().length > 0) {
        sentence = sentence.replaceAll("\\b" + Pattern.quote(entity.getText())
        + "\\b", Matcher.quoteReplacement(
            entity.getText().replaceAll("\\s", "_")));
      }
    }
    return sentence;
  }

  /**
   * This method ...
   * 
   * @param  tokens ...
   * @return ...
   */
  public String annotate(String[] tokens) {
    Entity[] entities = this.find(tokens);
    Arrays.sort(entities);
    String sentence = new String();
    for (String token : tokens) {
      sentence = sentence.concat(token);
      sentence = sentence.concat(" ");
    }
    sentence = sentence.trim();
    for (Entity entity : entities) {
      if (entity.getTokens().length > 0) {
        sentence = sentence.replaceAll("\\b" + Pattern.quote(entity.getText())
        + "\\b", Matcher.quoteReplacement(
            entity.toString().replaceAll("\\s", "_")));
      }
    }
    return sentence.replaceAll("_", " ");
  }
}

// http://opennlp.apache.org/documentation/1.5.3/manual/opennlp.html
// http://opennlp.apache.org/documentation/1.5.3/apidocs/opennlp-tools/opennlp/tools/namefind/RegexNameFinder.html

/*
   abstract
   artprod
   event
   numeric
   organization
   person
   place
   thing
   time
 */
/*
   cat pt-ner.train | grep -oh "<START:\w*>" *
 */
