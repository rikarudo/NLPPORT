package fact;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import triple.Triple;
import chunk.Chunk;
import dependency.DependencyChunk;
import entity.Entity;

/**
 * This class ...
 * 
 * @author   Ricardo Rodrigues
 * @version  0.9.9
 */
public class FactExtractor {
  private static final String DEFAULT_PROP =
      "resources/config/nlpport.properties";

  private String subjectTag = null;
  private String predicateTag = null;
  private String objectTag = null;

  private String isA = null;
  private String isIn = null;
  private String isPartOf = null;
  private String hasA = null;
  private String partOf = null;
  private String in = null;
  private String trimmings = null;
  private String prepositionalPhrase = null;
  private String verbPhrase = null;
  private String toPreposition = null;

  /**
   * Creates a new ...
   * 
   * @throws InvalidPropertiesFormatException ...
   * @throws IOException ...
   * @throws ParserConfigurationException ...
   * @throws SAXException ...
   */
  public FactExtractor()
      throws InvalidPropertiesFormatException, IOException,
      ParserConfigurationException, SAXException {
    Properties properties = new Properties();
    properties.load(
        this.getClass().getClassLoader().getResourceAsStream(DEFAULT_PROP));

    this.subjectTag = properties.getProperty("subject");
    this.predicateTag = properties.getProperty("predicate");
    this.objectTag = properties.getProperty("object");

    this.isA = properties.getProperty("is_a");
    this.isIn = properties.getProperty("is_in");
    this.isPartOf = properties.getProperty("is_part_of");
    this.hasA = properties.getProperty("has_a");
    this.partOf = properties.getProperty("part_of");
    this.in = properties.getProperty("in");
    this.trimmings = properties.getProperty("trimmings");
    this.prepositionalPhrase = properties.getProperty("prepostional_phrase");
    this.verbPhrase = properties.getProperty("verb_phrase");
    this.toPreposition = properties.getProperty("to_preposition");
  }

  // using ENTITIES ////////////////////////////////////////////////////////////

  /**
   * This method ...
   * 
   * @param  chunks ...
   * @param  entities ...
   * @return ...
   */
  public Triple[] extract(Chunk[] chunks, Entity[] entities) {
    HashSet<Triple> facts = new HashSet<Triple>();

    for (int i = 0; i < chunks.length; i++) {
      for (Entity entity : entities) {
        if (chunks[i].retrieveText().contains(
            entity.getText().replace(" ", "_"))) {
          String subject = chunks[i].retrieveText();
          String predicate = isA;
          String object = new String();

          ArrayList<Chunk> objectChunks = new ArrayList<Chunk>();
          for (int j = i + 1; j < chunks.length; j++) {
            if (!chunks[j].getType().equals(verbPhrase)
                && !chunks[j].retrieveText().equals(toPreposition)) {
              objectChunks.add(chunks[j]);
            }
            else {
              break;
            }
          }

          if (i + 1 < chunks.length) {
            if (chunks[i + 1].retrieveText().equals(partOf)) {
              predicate = isPartOf;
            }
          }

          if (i + 1 < chunks.length) {
            if (chunks[i + 1].retrieveText().equals(in)) {
              predicate = isIn;
            }
          }

          if (objectChunks.size() > 0) {
            for (int k = 0; k < objectChunks.size(); k++) {
              if ((k != 0) && (k != objectChunks.size() - 1)) {
                object += objectChunks.get(k).retrieveText() + " ";
              }
              else {
                if (!objectChunks.get(k).getType().equals(prepositionalPhrase)) {
                  object += objectChunks.get(k).retrieveText() + " ";
                }
              }
            }
            if (object.trim().length() > 0) {
              Triple fact = new Triple(subject, predicate, object.trim());
              if (!facts.contains(fact)) {
                facts.add(fact);
              }
            }
          }

          predicate = isA;
          objectChunks = new ArrayList<Chunk>();
          object = new String();

          int lookback = -1;
          for (int j = i; j >= 0; j--) {
            if (!chunks[j].getType().equals(verbPhrase)) {
              lookback = j;
            }
            else {
              break;
            }
          }

          if (lookback >= 0) {
            for (int j = lookback; j < i; j++) {
              objectChunks.add(chunks[j]);
            }
          }
          if (i - 1 > 0) {
            if (chunks[i - 1].retrieveText().equals(partOf)) {
              predicate = hasA;
            }
          }

          if (objectChunks.size() > 0) {
            for (int k = 0; k < objectChunks.size(); k++) {
              if ((k != 0) && (k != objectChunks.size() - 1)) {
                object += objectChunks.get(k).retrieveText() + " ";
              }
              else {
                if (!objectChunks.get(k).getType().equals(prepositionalPhrase)) {
                  object += objectChunks.get(k).retrieveText() + " ";
                }
              }
            }
            subject = subject.trim();
            object = object.trim();
            ////////////////////////////////////////////////////////////////////
            for (String trim : this.trimmings.split(";")) {
              if (object.endsWith(trim)) {
                object.substring(0, object.length() - trim.length());
                break;
              }
            }
            ////////////////////////////////////////////////////////////////////
            if (object.length() > 0) {
              Triple fact = new Triple(subject, predicate, object);
              if (!facts.contains(fact)) {
                facts.add(fact);
              }
            }
          }
        }
      }
    }
    return facts.toArray(new Triple[facts.size()]);
  }

  /**
   * This method ...
   * 
   * @param  conllChunks ...
   * @param  entities ...
   * @return ...
   */
  public Triple[] extract(DependencyChunk[] conllChunks, Entity[] entities) {
    return this.extract(conllChunks, entities, false);
  }

  /**
   * This method ...
   * 
   * @param  conllChunks ...
   * @param  entities ...
   * @param  usePredicateLemmas ...
   * @return ...
   */
  public Triple[] extract(DependencyChunk[] conllChunks, Entity[] entities,
      boolean usePredicateLemmas) {
    HashSet<Triple> facts = new HashSet<Triple>();

    for (DependencyChunk conllChunk : conllChunks) {
      if (conllChunk.getFunction().matches(predicateTag)) {
        String predicate = conllChunk.retrieveText(usePredicateLemmas);
        String subject = new String();
        String object = new String();
        for (int i = 0; i < conllChunks.length; i++) {
          if (conllChunks[i].getHead() == conllChunk.getID()) {
            if (conllChunks[i].getFunction().matches(subjectTag)) {
              subject += " " + conllChunks[i].retrieveText();
              subject = subject.trim();
            }
            else if (conllChunks[i].getFunction().matches(objectTag)) {
              object = object.trim() + " " + conllChunks[i].retrieveText();
              object = object.trim();
            }
          }
        }

        if ((object.length() > 0) && (predicate.length() > 0)
            && (object.length() > 0)) {
          for (Entity entity : entities) {
            if (subject.contains(entity.getText().replace(" ", "_"))
                || object.contains(entity.getText().replace(" ", "_"))) {
              Triple fact = new Triple(subject, predicate, object);
              if (!facts.contains(fact)) {
                facts.add(fact);
              }
            }
          }
        }
      }
    }
    return facts.toArray(new Triple[facts.size()]);
  }

  // using PROPER NOUNS ////////////////////////////////////////////////////////

  /**
   * This method ...
   * 
   * @param  chunks ...
   * @param  properNouns ...
   * @return ...
   */
  public Triple[] extract(Chunk[] chunks, String[] properNouns) {
    HashSet<Triple> facts = new HashSet<Triple>();

    for (int i = 0; i < chunks.length; i++) {
      for (String properNoun : properNouns) {
        if (chunks[i].retrieveText().contains(
            properNoun.replace(" ", "_"))) {
          String subject = chunks[i].retrieveText();
          String predicate = isA;
          String object = new String();

          ArrayList<Chunk> objectChunks = new ArrayList<Chunk>();
          for (int j = i + 1; j < chunks.length; j++) {
            if (!chunks[j].getType().equals(verbPhrase)
                && !chunks[j].retrieveText().equals(toPreposition)) {
              objectChunks.add(chunks[j]);
            }
            else {
              break;
            }
          }

          if (i + 1 < chunks.length) {
            if (chunks[i + 1].retrieveText().equals(partOf)) {
              predicate = isPartOf;
            }
          }

          if (i + 1 < chunks.length) {
            if (chunks[i + 1].retrieveText().equals(in)) {
              predicate = isIn;
            }
          }

          if (objectChunks.size() > 0) {
            for (int k = 0; k < objectChunks.size(); k++) {
              if ((k != 0) && (k != objectChunks.size() - 1)) {
                object += objectChunks.get(k).retrieveText() + " ";
              }
              else {
                if (!objectChunks.get(k).getType().equals(prepositionalPhrase)) {
                  object += objectChunks.get(k).retrieveText() + " ";
                }
              }
            }
            subject = subject.trim();
            object = object.trim();
            ////////////////////////////////////////////////////////////////////
            for (String trim : this.trimmings.split(";")) {
              if (object.endsWith(trim)) {
                object.substring(0, object.length() - trim.length());
                break;
              }
            }
            ////////////////////////////////////////////////////////////////////
            if (object.length() > 0) {
              Triple fact = new Triple(subject, predicate, object);
              if (!facts.contains(fact)) {
                facts.add(fact);
              }
            }
          }

          predicate = isA;
          objectChunks = new ArrayList<Chunk>();
          object = new String();

          int lookback = -1;
          for (int j = i; j >= 0; j--) {
            if (!chunks[j].getType().equals(verbPhrase)) {
              lookback = j;
            }
            else {
              break;
            }
          }

          if (lookback >= 0) {
            for (int j = lookback; j < i; j++) {
              objectChunks.add(chunks[j]);
            }
          }
          if (i - 1 > 0) {
            if (chunks[i - 1].retrieveText().equals(partOf)) {
              predicate = hasA;
            }
          }

          if (objectChunks.size() > 0) {
            for (int k = 0; k < objectChunks.size(); k++) {
              if ((k != 0) && (k != objectChunks.size() - 1)) {
                object += objectChunks.get(k).retrieveText() + " ";
              }
              else {
                if (!objectChunks.get(k).getType().equals(prepositionalPhrase)) {
                  object += objectChunks.get(k).retrieveText() + " ";
                }
              }
            }
            if (object.trim().length() > 0) {
              Triple fact = new Triple(subject, predicate, object.trim());
              if (!facts.contains(fact)) {
                facts.add(fact);
              }
            }
          }
        }
      }
    }
    return facts.toArray(new Triple[facts.size()]);
  }

  /**
   * This method ...
   * 
   * @param  conllChunks ...
   * @param  properNouns ...
   * @return ...
   */
  public Triple[] extract(DependencyChunk[] conllChunks, String[] properNouns) {
    return this.extract(conllChunks, properNouns, false);
  }

  /**
   * This method...
   * 
   * @param  conllChunks ...
   * @param  properNouns ...
   * @param  usePredicateLemmas ...
   * @return ...
   */
  public Triple[] extract(DependencyChunk[] conllChunks, String[] properNouns,
      boolean usePredicateLemmas) {
    HashSet<Triple> facts = new HashSet<Triple>();

    for (DependencyChunk conllChunk : conllChunks) {
      if (conllChunk.getFunction().matches(predicateTag)) {
        String predicate = conllChunk.retrieveText(usePredicateLemmas);
        String subject = new String();
        String object = new String();
        for (int i = 0; i < conllChunks.length; i++) {
          if (conllChunks[i].getHead() == conllChunk.getID()) {
            if (conllChunks[i].getFunction().matches(subjectTag)) {
              subject += " " + conllChunks[i].retrieveText();
              subject = subject.trim();
            }
            else if (conllChunks[i].getFunction().matches(objectTag)) {
              object = object.trim() + " " + conllChunks[i].retrieveText();
              object = object.trim();
            }
          }
        }

        if ((object.length() > 0) && (predicate.length() > 0)
            && (object.length() > 0)) {
          for (String properNoun : properNouns) {
            if (subject.contains(properNoun.replace(" ", "_"))
                || object.contains(properNoun.replace(" ", "_"))) {
              Triple fact = new Triple(subject, predicate, object);
              if (!facts.contains(fact)) {
                facts.add(fact);
              }
            }
          }
        }
      }
    }
    return facts.toArray(new Triple[facts.size()]);
  }


  // A USAR OS CHUNKS COM QUALQUER CONTEÚDO ////////////////////////////////////

  /**
   * This method ...
   * 
   * @param  chunks ...
   * @return ...
   */
  public Triple[] extract(Chunk[] chunks) {
    HashSet<Triple> facts = new HashSet<Triple>();

    for (int i = 0; i < chunks.length; i++) {
      String subject = chunks[i].retrieveText();
      String predicate = isA;
      String object = new String();

      ArrayList<Chunk> objectChunks = new ArrayList<Chunk>();
      for (int j = i + 1; j < chunks.length; j++) {
        if (!chunks[j].getType().equals(verbPhrase)
            && !chunks[j].retrieveText().equals(toPreposition)) {
          objectChunks.add(chunks[j]);
        }
        else {
          break;
        }
      }

      if (i + 1 < chunks.length) {
        if (chunks[i + 1].retrieveText().equals(partOf)) {
          predicate = isPartOf;
        }
      }

      if (i + 1 < chunks.length) {
        if (chunks[i + 1].retrieveText().equals(in)) {
          predicate = isIn;
        }
      }

      if (objectChunks.size() > 0) {
        for (int k = 0; k < objectChunks.size(); k++) {
          if ((k != 0) && (k != objectChunks.size() - 1)) {
            object += objectChunks.get(k).retrieveText() + " ";
          }
          else {
            if (!objectChunks.get(k).getType().equals(prepositionalPhrase)) {
              object += objectChunks.get(k).retrieveText() + " ";
            }
          }
        }
        subject = subject.trim();
        object = object.trim();
        ////////////////////////////////////////////////////////////////////
        for (String trim : this.trimmings.split(";")) {
          if (object.endsWith(trim)) {
            object.substring(0, object.length() - trim.length());
            break;
          }
        }
        ////////////////////////////////////////////////////////////////////
        if (object.length() > 0) {
          Triple fact = new Triple(subject, predicate, object);
          if (!facts.contains(fact)) {
            facts.add(fact);
          }
        }
      }

      predicate = isA;
      objectChunks = new ArrayList<Chunk>();
      object = new String();

      int lookback = -1;
      for (int j = i; j >= 0; j--) {
        if (!chunks[j].getType().equals(verbPhrase)) {
          lookback = j;
        }
        else {
          break;
        }
      }

      if (lookback >= 0) {
        for (int j = lookback; j < i; j++) {
          objectChunks.add(chunks[j]);
        }
      }
      if (i - 1 > 0) {
        if (chunks[i - 1].retrieveText().equals(partOf)) {
          predicate = hasA;
        }
      }

      if (objectChunks.size() > 0) {
        for (int k = 0; k < objectChunks.size(); k++) {
          if ((k != 0) && (k != objectChunks.size() - 1)) {
            object += objectChunks.get(k).retrieveText() + " ";
          }
          else {
            if (!objectChunks.get(k).getType().equals(prepositionalPhrase)) {
              object += objectChunks.get(k).retrieveText() + " ";
            }
          }
        }
        if (object.trim().length() > 0) {
          Triple fact = new Triple(subject, predicate, object.trim());
          if (!facts.contains(fact)) {
            facts.add(fact);
          }
        }
      }
    }
    return facts.toArray(new Triple[facts.size()]);
  }

  /**
   * This method ...
   * 
   * @param  conllChunks ...
   * @return ...
   */
  public Triple[] extract(DependencyChunk[] conllChunks) {
    return this.extract(conllChunks, false);
  }

  /**
   * This method...
   * 
   * @param  conllChunks ...
   * @param  usePredicateLemmas ...
   * @return ...
   */
  public Triple[] extract(DependencyChunk[] conllChunks,
      boolean usePredicateLemmas) {
    HashSet<Triple> facts = new HashSet<Triple>();

    for (DependencyChunk conllChunk : conllChunks) {
      if (conllChunk.getFunction().matches(predicateTag)) {
        String predicate = conllChunk.retrieveText(usePredicateLemmas);
        String subject = new String();
        String object = new String();
        for (int i = 0; i < conllChunks.length; i++) {
          if (conllChunks[i].getHead() == conllChunk.getID()) {
            if (conllChunks[i].getFunction().matches(subjectTag)) {
              subject += " " + conllChunks[i].retrieveText();
              subject = subject.trim();
            }
            else if (conllChunks[i].getFunction().matches(objectTag)) {
              object = object.trim() + " " + conllChunks[i].retrieveText();
              object = object.trim();
            }
          }
        }

        if ((object.length() > 0) && (predicate.length() > 0)
            && (object.length() > 0)) {
          Triple fact = new Triple(subject, predicate, object);
          if (!facts.contains(fact)) {
            facts.add(fact);
          }
        }
      }
    }
    return facts.toArray(new Triple[facts.size()]);
  }
}
