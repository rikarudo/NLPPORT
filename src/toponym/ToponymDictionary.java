package toponym;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Properties;

/**
 * This class ...
 *
 * @author   Ricardo Rodrigues
 * @version  0.9.9.9
 */
public class ToponymDictionary {
  private static final String DEFAULT_PROP =
      "resources/config/nlpport.properties";
  private static final String VALUE_DELIMITER = ";";
  private static final String POS_SEPARATOR = ":";
  private static final int VALUES_PER_LINE = 4;

  private HashMap<String, HashSet<ToponymDictionaryEntry>> dictionary = null;

  /**
   * Creates a new ...
   * 
   * @throws IOException ...
   * @throws ToponymDictionaryLoadException ... 
   */
  public ToponymDictionary()
      throws IOException, ToponymDictionaryLoadException {
    dictionary = new HashMap<String, HashSet<ToponymDictionaryEntry>>();
    Properties properties = new Properties();
    properties.load(
        this.getClass().getClassLoader().getResourceAsStream(DEFAULT_PROP));
    InputStream demonymData =
        this.getClass().getClassLoader().getResourceAsStream(
            properties.getProperty("toponymDictionary"));
    this.load(demonymData);
  }

  private void load(InputStream demonymData)
      throws IOException, ToponymDictionaryLoadException {
    BufferedReader reader = new BufferedReader(
        new InputStreamReader(demonymData));
    String toponym = null;
    String type = null;
    String demonym = null;
    String partOfSpeech = null;
    String holonym = null;
    String line = null;
    int lineNumber = 0;
    while ((line = reader.readLine()) != null) {
      lineNumber++;
      line = line.trim();
      if (line.length() > 0 && !line.startsWith("#")) {
        String[] values = line.split(VALUE_DELIMITER);
        if ((values.length == VALUES_PER_LINE) && values[2].contains(":")) {
          toponym = values[0].trim();
          type = values[1].trim();
          demonym = values[2].split(POS_SEPARATOR)[0].trim();
          partOfSpeech = values[2].split(POS_SEPARATOR)[1].trim();
          holonym = values[3].trim();
          if (toponym.contains("(") && toponym.contains(")")) {
            toponym = toponym.substring(0, toponym.indexOf("(")).trim();
          }
          if (holonym.contains("(") && holonym.contains(")")) {
            holonym = holonym.substring(0, holonym.indexOf("(")).trim();
          }
          this.add(new ToponymDictionaryEntry(toponym, type, demonym,
              partOfSpeech, holonym));
        }
        else {
          reader.close();
          throw new ToponymDictionaryLoadException(lineNumber + ": " + line);
        }
      }
    }
    reader.close();
  }

  private void add(ToponymDictionaryEntry entry) {
    HashSet<ToponymDictionaryEntry> entrySet = dictionary.get(
        entry.getToponym());
    if (entrySet == null) {
      entrySet = new HashSet<ToponymDictionaryEntry>();
    }
    entrySet.add(entry); 
    dictionary.put(entry.getToponym(), entrySet);
  }

  /**
   * This method ...
   * 
   * @param  demonym ...
   * @return ...
   */
  public String[] retrieveToponyms(String demonym) {
    HashSet<String> toponyms = new HashSet<String>();
    HashSet<ToponymDictionaryEntry> entries =
        new HashSet<ToponymDictionaryEntry>();
    Collection<HashSet<ToponymDictionaryEntry>> entryCollection =
        dictionary.values();
    for (HashSet<ToponymDictionaryEntry> entrySet : entryCollection) {
      entries.addAll(entrySet);
    }
    for (ToponymDictionaryEntry entry : entries) {
      if (entry.getDemonym().equalsIgnoreCase(demonym)) {
        toponyms.add(entry.getToponym());
      }
    }
    return toponyms.toArray(new String[toponyms.size()]);
  }

  /**
   * This method ...
   * 
   * @param  toponym ...
   * @return ...
   */
  public String[] retrieveHolonyms(String toponym) {
    HashSet<String> holonyms = new HashSet<String>();
    HashSet<ToponymDictionaryEntry> entries =
        new HashSet<ToponymDictionaryEntry>();
    Collection<HashSet<ToponymDictionaryEntry>> entryCollection =
        dictionary.values();
    for (HashSet<ToponymDictionaryEntry> entrySet : entryCollection) {
      entries.addAll(entrySet);
    }
    for (ToponymDictionaryEntry entry : entries) {
      if (entry.getToponym().equalsIgnoreCase(toponym)) {
        holonyms.add(entry.getHolonym());
      }
    }
    return holonyms.toArray(new String[holonyms.size()]);
  }

  /**
   * This method ...
   * 
   * @param  toponym ...
   * @return ...
   */
  public String[] retrieveDemonyms(String toponym) {
    HashSet<String> demonyns = new HashSet<String>();
    HashSet<ToponymDictionaryEntry> entries =
        new HashSet<ToponymDictionaryEntry>();
    Collection<HashSet<ToponymDictionaryEntry>> entryCollection =
        dictionary.values();
    for (HashSet<ToponymDictionaryEntry> entrySet : entryCollection) {
      entries.addAll(entrySet);
    }
    for (ToponymDictionaryEntry entry : entries) {
      if (entry.getToponym().equalsIgnoreCase(toponym)) {
        demonyns.add(entry.getDemonym());
      }
    }
    return demonyns.toArray(new String[demonyns.size()]);
  }

  /**
   * This method ...
   * 
   * @param  toponym ...
   * @param  partOfSpeech ...
   * @return ...
   */
  public String[] retrieveDemonyms(String toponym, String partOfSpeech) {
    HashSet<String> demonyns = new HashSet<String>();
    HashSet<ToponymDictionaryEntry> entries =
        new HashSet<ToponymDictionaryEntry>();
    Collection<HashSet<ToponymDictionaryEntry>> entryCollection =
        dictionary.values();
    for (HashSet<ToponymDictionaryEntry> entrySet : entryCollection) {
      entries.addAll(entrySet);
    }
    for (ToponymDictionaryEntry entry : entries) {
      if (entry.getToponym().equalsIgnoreCase(toponym)
          && entry.getPartOfSpeech().equalsIgnoreCase(partOfSpeech)) {
        demonyns.add(entry.getDemonym());
      }
    }
    return demonyns.toArray(new String[demonyns.size()]);
  }
}
