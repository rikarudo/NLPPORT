package nominalization;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import replacement.Replacement;
import lexicon.Lexicon;
import lexicon.LexiconLoadException;

/**
 * This class ...
 *
 * @author   Ricardo Rodrigues
 * @version  0.9.9.9
 */
public class Nominalizer {
  private static final String DEFAULT_PROP =
      "resources/config/nlpport.properties";

  private Lexicon lexicon = null;
  private Pattern[] suffixExceptions = null;
  private Pattern[] suffixTargets = null;
  private Pattern[] suffixTags = null;
  private Replacement[] suffixes = null;
  private String nounTag = null;

  /**
   * Creates a new ...
   * 
   * @throws InvalidPropertiesFormatException ...
   * @throws IOException ...
   * @throws LexiconLoadException ...
   * @throws ParserConfigurationException ...
   * @throws SAXException ...
   */
  public Nominalizer() throws InvalidPropertiesFormatException, IOException,
  LexiconLoadException, ParserConfigurationException, SAXException {
    Properties properties = new Properties();
    properties.load(
        this.getClass().getClassLoader().getResourceAsStream(DEFAULT_PROP));
    InputStream lexiconData =
        this.getClass().getClassLoader().getResourceAsStream(
            properties.getProperty("lexicon"));
    InputStream suffixData =
        this.getClass().getClassLoader().getResourceAsStream(
            properties.getProperty("nominalizationSuffixes"));
    String nounTag = properties.getProperty("nounTag");
    this.initialize(lexiconData, suffixData, nounTag);
  }

  /**
   * Creates a new ...
   * 
   * @param  lexiconData ...
   * @param  suffixData ...
   * @param  nounTag ...
   * @throws IOException ...
   * @throws LexiconLoadException ...
   * @throws ParserConfigurationException ...
   * @throws SAXException ...
   */
  public Nominalizer(InputStream lexiconData, InputStream suffixData,
      String nounTag)
          throws IOException, LexiconLoadException,
          ParserConfigurationException, SAXException {
    this.initialize(lexiconData, suffixData, nounTag);
  }

  private void initialize(InputStream lexiconData, InputStream suffixData,
      String nounTag)
          throws IOException, LexiconLoadException,
          ParserConfigurationException, SAXException {
    this.lexicon = new Lexicon();
    this.lexicon.load(lexiconData);
    this.suffixes = Replacement.readReplacements(suffixData);
    this.nounTag = new String(nounTag);
    Arrays.sort(this.suffixes);
    suffixExceptions = new Pattern[this.suffixes.length];
    suffixTargets = new Pattern[this.suffixes.length];
    suffixTags = new Pattern[this.suffixes.length];
    for (int i = 0; i < suffixes.length; i++) {
      suffixExceptions[i] = Pattern.compile(suffixes[i].getExceptions());
      suffixTargets[i] = Pattern.compile(suffixes[i].getPrefix()
          +  suffixes[i].getTarget() + suffixes[i].getSuffix());
      suffixTags[i] = Pattern.compile(suffixes[i].getTag());
    }
  }

  /**
   * 
   * @param  lemma ...
   * @param  tag ...
   * @return ...
   */
  public String[] nominalize(String lemma, String tag) {
    ArrayList<String> nouns = new ArrayList<String>();
    String noun = null;
    for (int i = 0; i < suffixes.length; i++) {
      noun = lemma.toLowerCase();
      if (suffixTargets[i].matcher(noun).matches()
          && suffixTags[i].matcher(tag.toLowerCase()).matches()
          && !suffixExceptions[i].matcher(noun).matches()) {
        noun = noun.substring(0,
            noun.length() - suffixes[i].getTarget().length())
            + suffixes[i].getReplacement();
        if (lexicon.contains(noun)) {
          ArrayList<String> pos = new ArrayList<String>(Arrays.asList(
              lexicon.retrievePartsOfSpeech(noun)));
          if (pos.contains(nounTag)) {
            nouns.add(noun);
          }
        }
      }
    }
    return nouns.toArray(new String[nouns.size()]);
  }
}

/*
http://en.wikipedia.org/wiki/Nominalization
http://en.wiktionary.org/wiki/nominalizer
 */