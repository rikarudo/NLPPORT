package token;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;
import java.util.InvalidPropertiesFormatException;
import java.util.LinkedHashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.InvalidFormatException;
import replacement.Replacement;

/**
 * This class ...
 *
 * @author   Ricardo Rodrigues
 * @version  0.9.9.9
 */
public class Tokenizer {
  private static final String DEFAULT_PROP =
      "resources/config/nlpport.properties";

  private LinkedHashSet<String> abbreviationTargets = null;
  private Pattern[] contractionTargets = null;
  private Pattern[] cliticTargets = null;
  private Replacement[] abbreviations = null;
  private Replacement[] contractions = null;
  private Replacement[] clitics = null;
  private TokenizerME tokenizer = null;

  /**
   * Creates a new <code>Tokenizer</code> object ...
   * 
   * @throws InvalidPropertiesFormatException ...
   * @throws IOException ...
   * @throws ParserConfigurationException ...
   * @throws SAXException ...
   */
  public Tokenizer()
      throws InvalidPropertiesFormatException, IOException,
      ParserConfigurationException, SAXException {
    Properties properties = new Properties();
    properties.load(
        this.getClass().getClassLoader().getResourceAsStream(DEFAULT_PROP));
    this.tokenizer = new TokenizerME(new TokenizerModel(
        this.getClass().getClassLoader().getResourceAsStream(
            properties.getProperty("tokenizer"))));
    this.abbreviations = Replacement.readReplacements(
        this.getClass().getClassLoader().getResourceAsStream(
            properties.getProperty("abbreviations")));
    this.contractions = Replacement.readReplacements(
        this.getClass().getClassLoader().getResourceAsStream(
            properties.getProperty("contractions")));
    this.clitics = Replacement.readReplacements(
        this.getClass().getClassLoader().getResourceAsStream(
            properties.getProperty("clitics")));
    Arrays.sort(this.abbreviations);
    Arrays.sort(this.contractions);
    Arrays.sort(this.clitics);
    this.abbreviationTargets = new LinkedHashSet<String>(
        this.abbreviations.length);
    for (int i = 0; i < this.abbreviations.length; i++) {
      this.abbreviationTargets.add(abbreviations[i].getTarget());
    }
    this.contractionTargets = new Pattern[this.contractions.length];
    for (int i = 0; i < this.contractions.length; i++) {
      this.contractionTargets[i] = Pattern.compile(
          this.contractions[i].getTarget());
    }
    this.cliticTargets = new Pattern[this.clitics.length];
    for (int i = 0; i < this.clitics.length; i++) {
      this.cliticTargets[i] = Pattern.compile(this.clitics[i].getPrefix()
          + this.clitics[i].getTarget());
    }
  }

  /**
   * Creates a new <code>Tokenizer</code> object ...
   * 
   * @param  tokenizerModel ...
   * @param  abbreviations ...
   * @param  contractions ...
   * @param  clitics ...
   * @throws InvalidFormatException ...
   * @throws IOException ...
   * @throws ParserConfigurationException ...
   * @throws SAXException ...
   */
  public Tokenizer(InputStream tokenizerModel, Replacement[] abbreviations,
      Replacement[] contractions, Replacement[] clitics)
          throws InvalidFormatException, IOException,
          ParserConfigurationException, SAXException {
    this.tokenizer = new TokenizerME(new TokenizerModel(tokenizerModel));
    this.abbreviations = abbreviations;
    this.contractions = contractions;
    this.clitics = clitics;
    Arrays.sort(this.contractions);
    Arrays.sort(this.clitics);
    Arrays.sort(this.abbreviations);
    this.abbreviationTargets = new LinkedHashSet<String>(
        this.abbreviations.length);
    for (int i = 0; i < this.abbreviations.length; i++) {
      this.abbreviationTargets.add(abbreviations[i].getTarget());
    }
    this.contractionTargets = new Pattern[this.contractions.length];
    for (int i = 0; i < this.contractions.length; i++) {
      this.contractionTargets[i] = Pattern.compile(
          this.contractions[i].getTarget());
    }
    cliticTargets = new Pattern[this.clitics.length];
    for (int i = 0; i < this.clitics.length; i++) {
      this.cliticTargets[i] = Pattern.compile(this.clitics[i].getPrefix()
          + this.clitics[i].getTarget());
    }
  }

  /**
   * This method ...
   *
   * @param  sentence ...
   * @param  expandTokens ...
   * @return ...
   */
  public String[] tokenize(String sentence, boolean expandTokens) {
    String[] tokens = tokenizer.tokenize(sentence);
    for (int i = 0; i < tokens.length; i++) {
      if (expandTokens) {
        for (int j = 0; j < contractions.length; j++) {
          if (contractionTargets[j].matcher(
              tokens[i].toLowerCase()).matches()) {
            if (this.isCapitalized(tokens[i], true)) {
              tokens[i] = this.capitalize(contractions[j].getReplacement(),
                  true);
            }
            else if (this.isCapitalized(tokens[i])) {
              tokens[i] = this.capitalize(contractions[j].getReplacement());
            }
            else {
              tokens[i] = contractions[j].getReplacement();
            }
          }
        }

        for (int j = 0; j < clitics.length; j++) {
          if (cliticTargets[j].matcher(tokens[i].toLowerCase()).matches()) {
            tokens[i] = tokens[i].substring(0,
                tokens[i].length() - clitics[j].getTarget().length())
                + clitics[j].getReplacement();
            break;
          }
        }
      }

      // check for overlooked cases
      if (tokens[i].contains(".") && !tokens[i].matches(".*\\d\\.\\d.*")
          && !abbreviationTargets.contains(tokens[i].toLowerCase())) {
        tokens[i] = tokens[i].replace(".", " . ");
      }
      if (tokens[i].contains(",") && !tokens[i].matches(".*\\d\\,\\d.*")) {
        tokens[i] = tokens[i].replace(",", " , ");
      }
      if (tokens[i].contains(":") && !tokens[i].matches(".*\\d\\:\\d.*")) {
        tokens[i] = tokens[i].replace(":", " : ");
      }
      if (tokens[i].contains("/") && !tokens[i].matches(".*\\d\\/\\d.*")) {
        tokens[i] = tokens[i].replace("/", " / ");
      }
      if (tokens[i].contains("'")) {
        tokens[i] = tokens[i].replace("'", " ' ");
      }
      if (tokens[i].contains("\"")) {
        tokens[i] = tokens[i].replace("\"", " \" ");
      }
      if (tokens[i].contains("«")) {
        tokens[i] = tokens[i].replace("«", " « ");
      }
      if (tokens[i].contains("»")) {
        tokens[i] = tokens[i].replace("»", " » ");
      }
      if (tokens[i].contains(";")) {
        tokens[i] = tokens[i].replace(";", " ; ");
      }
      if (tokens[i].contains("!")) {
        tokens[i] = tokens[i].replace("!", " ! ");
      }
      if (tokens[i].contains("?")) {
        tokens[i] = tokens[i].replace("?", " ? ");
      }
      if (tokens[i].contains("(")) {
        tokens[i] = tokens[i].replace("(", " ( ");
      }
      if (tokens[i].contains(")")) {
        tokens[i] = tokens[i].replace(")", " ) ");
      }
      if (tokens[i].contains("[")) {
        tokens[i] = tokens[i].replace("[", " [ ");
      }
      if (tokens[i].contains("]")) {
        tokens[i] = tokens[i].replace("]", " ] ");
      }
      if (tokens[i].contains("{")) {
        tokens[i] = tokens[i].replace("{", " { ");
      }
      if (tokens[i].contains("}")) {
        tokens[i] = tokens[i].replace("}", " } ");
      }
    }

    // create tokenized sentence based on split tokens
    StringBuffer tokenizedSentence = new StringBuffer();
    for (int i = 0; i < tokens.length; i++) {
      tokenizedSentence.append(tokens[i].replaceAll("\\s+", " ").trim() + " ");
    }

    // put back together abbreviations (and composed tokens with abbreviations)
    // that may have been broken apart
    boolean repeat = true;
    while (repeat) {
      tokens = tokenizedSentence.toString().trim().split("\\s");
      tokenizedSentence = new StringBuffer();
      repeat = false;

      for (int i = 0; i < tokens.length; i++) {
        if (i + 1 < tokens.length) {
          if (abbreviationTargets.contains(tokens[i].toLowerCase().substring(
              tokens[i].lastIndexOf("_") + 1) + tokens[i + 1].toLowerCase())) {
            tokenizedSentence.append(tokens[i] + tokens[i + 1] + " ");
            repeat = true;
            i++;
          }
          else if (abbreviationTargets.contains(
              tokens[i].toLowerCase().substring(tokens[i].lastIndexOf("_") + 1))
              && tokens[i + 1].startsWith("_")) {
            tokenizedSentence.append(tokens[i] + tokens[i + 1] + " ");
            repeat = true;
            i++;
          }
          else {
            tokenizedSentence.append(tokens[i] + " ");
          }
        }
        else {
          tokenizedSentence.append(tokens[i] + " ");
        }
      }
    }

    return tokenizedSentence.toString().trim().split("\\s");
  }

  /**
   * This method ...
   *
   * @param  sentence ...
   * @return ...
   */
  public String[] tokenize(String sentence) {
    return this.tokenize(sentence, false);
  }

  /**
   * This method ...
   * 
   * @param  tokens ...
   * @return ...
   */
  public String groupTokens(String[] tokens) {
    StringBuffer sentence = new StringBuffer();
    for (String token : tokens) {
      sentence.append(token + " ");
    }
    return sentence.toString().trim();
  }

  /**
   * This method ...
   * 
   * @param  tokens ...
   * @param  tokenGroups ...
   * @return ...
   */
  public String groupTokens(String[] tokens, String[] tokenGroups) {
    String sentence = this.groupTokens(tokens);
    for (String tokenGroup : tokenGroups) {
      if (tokenGroup.length() > 0) {
        sentence = sentence.replaceAll("\\b" + Pattern.quote(tokenGroup)
        + "\\b", Matcher.quoteReplacement(
            tokenGroup.replaceAll("\\s", "_")));
      }
    }
    return sentence;
  }

  private boolean isCapitalized(String word) {
    return this.isCapitalized(word, false);
  }

  private boolean isCapitalized(String word, boolean all) {
    if (all) {
      if (word.length() > 0) {
        for (int i = 0; i < word.length(); i++) {
          if (!Character.isUpperCase(word.charAt(i))) {
            return false;
          }
        }
        return true;
      }
      else {
        return false;
      }
    }
    else {
      if (word.length() > 0) {
        if (!Character.isUpperCase(word.charAt(0))) {
          return false;
        }
        return true;
      }
      else {
        return false;
      }
    }
  }

  private String capitalize(String word, boolean all) {
    if (all) {
      return word.toUpperCase();
    }
    else {
      if (word.length() > 0) {
        String[] elements = word.split("\\s");
        String capitalizedWord = new String();
        for (String element : elements) {
          String capitalizedElement = element.substring(0, 1).toUpperCase();
          if (element.length() > 1) {
            capitalizedElement = capitalizedElement.concat(
                element.substring(1));
          }
          capitalizedWord = capitalizedWord.concat(" " + capitalizedElement);
        }
        return capitalizedWord.trim();
      }
      else {
        return word;
      }
    }
  }

  private String capitalize(String word) {
    return this.capitalize(word, false);
  }
}
