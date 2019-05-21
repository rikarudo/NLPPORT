package ontology;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Properties;

import rank.WordRanking;
import rank.WordRankingLoadException;
import triple.Triple;
import triple.TripleLoadException;
import triple.TripleStore;

/**
 * This class ...
 *
 * @author   Ricardo Rodrigues
 * @version  0.9.9.9
 */
public class LexicalOntology {
  private static final String DEFAULT_PROP =
      "resources/config/nlpport.properties";

  private TripleStore store = null;
  private WordRanking ranking = null;
  private HashMap<String, String> inverseRelations = null;

  /**
   * Creates a new ...
   * 
   * @throws TripleLoadException ...
   * @throws IOException ...
   * @throws NumberFormatException ...
   * @throws WordRankingLoadException ...
   */
  public LexicalOntology()
      throws NumberFormatException, TripleLoadException, IOException,
      WordRankingLoadException {
    this(null);
  }

  /**
   * Creates a new ...
   * 
   * @param  inverseRelations ...
   * @throws TripleLoadException ...
   * @throws IOException ...
   * @throws NumberFormatException ...
   * @throws WordRankingLoadException ...
   */
  public LexicalOntology(HashMap<String, String> inverseRelations)
      throws TripleLoadException, IOException, NumberFormatException,
      WordRankingLoadException {
    Properties properties = new Properties();
    properties.load(
        this.getClass().getClassLoader().getResourceAsStream(DEFAULT_PROP));
    InputStream ontologyData =
        this.getClass().getClassLoader().getResourceAsStream(
            properties.getProperty("lexicalRelations"));
    InputStream wordRankingData =
        this.getClass().getClassLoader().getResourceAsStream(
            properties.getProperty("wordRanking"));
    this.ranking = new WordRanking(wordRankingData);
    if (inverseRelations != null) {
      this.inverseRelations = inverseRelations;
    }
    else {
      this.inverseRelations = new HashMap<String, String>();
      String[] lexInvRelations = 
          properties.getProperty("lexicalInverseRelations").split(";");
      for (String lexInvRelation : lexInvRelations) {
        if (lexInvRelation.contains(":") && (lexInvRelation.indexOf(":") > 0)
            && (lexInvRelation.indexOf(":") < lexInvRelation.length() - 1)) {
          this.inverseRelations.put(
              lexInvRelation.substring(0, lexInvRelation.indexOf(":")),
              lexInvRelation.substring(lexInvRelation.indexOf(":") + 1));
        }
      }
    }
    this.store = new TripleStore(ontologyData, this.inverseRelations);
  }

  /**
   * This method ...
   * 
   * @param  relation ...
   * @param  word ...
   * @return ...
   */
  public String[] retrieveRelatedWords(String word, String relation) {
    HashSet<String> relatedWords = new HashSet<String>();
    Triple[] triples = store.querySubject(word);
    for (Triple triple : triples) {
      if (triple.getPredicate().equalsIgnoreCase(relation)) {
        relatedWords.add(triple.getObject());
      }
    }
    return ranking.rank(relatedWords.toArray(new String[relatedWords.size()]));
  }

  /**
   * This method ...
   * 
   * @return ...
   */
  public HashMap<String, String> getInverseRelations() {
    return inverseRelations;
  }

  /**
   * This method ...
   * 
   * @param  inverseRelations ...
   */
  public void setInverseRelations(HashMap<String, String> inverseRelations) {
    this.inverseRelations = inverseRelations;
  }
}
