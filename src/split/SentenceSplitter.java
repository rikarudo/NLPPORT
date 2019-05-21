package split;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;
import java.util.InvalidPropertiesFormatException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.util.InvalidFormatException;
import replacement.Replacement;

/**
 * This class ...
 *
 * @author   Ricardo Rodrigues
 * @version  0.9.9.9
 */
public class SentenceSplitter {
  private static final String DEFAULT_PROP =
      "resources/config/nlpport.properties";

  private SentenceDetectorME splitter = null;
  private String[] abbreviations = null;

  /**
   * Creates a new <code>SentenceSplitter</code> object ...
   * 
   * @throws InvalidPropertiesFormatException ...
   * @throws IOException ...
   * @throws ParserConfigurationException ...
   * @throws SAXException ...
   */
  public SentenceSplitter()
      throws InvalidPropertiesFormatException, IOException,
      ParserConfigurationException, SAXException {
    Properties properties = new Properties();
    properties.load(
        this.getClass().getClassLoader().getResourceAsStream(DEFAULT_PROP));
    this.splitter = new SentenceDetectorME(new SentenceModel(
        this.getClass().getClassLoader().getResourceAsStream(
            properties.getProperty("sentenceDetector"))));
    Replacement[] abbreviationReplacements = Replacement.readReplacements(
        this.getClass().getClassLoader().getResourceAsStream(
            properties.getProperty("abbreviations")));
    this.abbreviations = new String[abbreviationReplacements.length];
    for (int i = 0; i < this.abbreviations.length; i++) {
      this.abbreviations[i] = abbreviationReplacements[i].getTarget();
    }
  }

  /**
   * Creates a new <code>SentenceSplitter</code> object ...
   * 
   * @param  detectorModel ...
   * @param  abbreviationData ...
   * @throws InvalidFormatException ...
   * @throws IOException ...
   * @throws ParserConfigurationException ...
   * @throws SAXException ...
   */
  public SentenceSplitter(InputStream detectorModel,
      InputStream abbreviationData)
          throws InvalidFormatException, IOException,
          ParserConfigurationException, SAXException {
    this.splitter = new SentenceDetectorME(new SentenceModel(
        detectorModel));
    Replacement[] abbreviationReplacements = 
        Replacement.readReplacements(abbreviationData);
    this.abbreviations = new String[abbreviationReplacements.length];
    for (int i = 0; i < this.abbreviations.length; i++) {
      this.abbreviations[i] = abbreviationReplacements[i].getTarget();
    }
  }

  /**
   * This method ...
   *
   * @param  text ...
   * @return ...
   */
  public String[] split(String text) {
    return this.split(text, false);
  }

  /**
   * This method ...
   *
   * @param  text ...
   * @param  splitOnLineBreaks ...
   * @return ...
   */
  public String[] split(String text, boolean splitOnLineBreaks) {
    if (splitOnLineBreaks) {
      String[] splitText = text.split("(\n\r?)|(\r\n?)");
      String[] sentences = new String[0];
      for (int i = 0; i < splitText.length; i++) {
        sentences = mergeStringArrays(sentences, this.split(splitText[i]));
      }
      return sentences;
    }
    else {
      return this.checkAbbreviations(splitter.sentDetect(text));
    }
  }

  private String[] checkAbbreviations(String[] sentences) {
    ArrayList<String> processedSentences = new ArrayList<String>();
    for (int i = 0; i < sentences.length; i++) {
      if (this.endsWithAnAbbreviation(sentences[i])) {
        if (i + 1 < sentences.length) {
          processedSentences.add(sentences[i] + " " + sentences[i + 1]);
          i++;
        }
      }
      else {
        processedSentences.add(sentences[i]);
      }
    }
    if (sentences.length != processedSentences.size()) {
      return checkAbbreviations(processedSentences.toArray(
          new String[processedSentences.size()]));  
    }
    else {
      return processedSentences.toArray(new String[sentences.length]);
    }
  }

  private boolean endsWithAnAbbreviation(String sentence) {
    for (String abbreviation : abbreviations) {
      if (sentence.toLowerCase().endsWith(" " + abbreviation)) {
        return true;
      }
    }
    return false;
  }

  private String[] mergeStringArrays(String[] oneStringArray,
      String[] anotherStringArray) {
    ArrayList<String> mergedStrings = new ArrayList<String>();
    for (int i = 0; i < oneStringArray.length; i++) {
      if (!mergedStrings.contains(oneStringArray[i])) {
        mergedStrings.add(oneStringArray[i]);
      }
    }
    for (int i = 0; i < anotherStringArray.length; i++) {
      if (!mergedStrings.contains(anotherStringArray[i])) {
        mergedStrings.add(anotherStringArray[i]);
      }
    }
    return mergedStrings.toArray(new String[mergedStrings.size()]);
  }
}
