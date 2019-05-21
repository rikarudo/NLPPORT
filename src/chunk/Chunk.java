package chunk;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class ...
 *
 * @author   Ricardo Rodrigues
 * @version  0.9.9.9
 */
public class Chunk {
  private String type = null;
  private String[] tokens = null;
  private String[] tags = null;
  private String[] lemmas = null;

  /**
   * Creates a new ...
   * 
   * @param  type ...
   * @param  tokens ...
   * @param  tags ...
   * @param  lemmas ...
   */
  public Chunk(String type, String[] tokens, String[] tags, String[] lemmas) {
    this.type = type;
    this.tokens = tokens;
    this.tags = tags;
    this.lemmas = lemmas;
  }

  /**
   * This method ...
   * 
   * @return ...
   */
  public String getType() {
    return type;
  }

  /**
   * This method ...
   * 
   * @param  type ...
   */
  public void setType(String type) {
    this.type = type;
  }

  /**
   * This method ...
   * 
   * @return ...
   */
  public String[] getTokens() {
    return tokens;
  }

  /**
   * This method ...
   * 
   * @param  tokens ...
   */
  public void setTokens(String[] tokens) {
    this.tokens = tokens;
  }

  /**
   * This method ...
   * 
   * @return ...
   */
  public String[] getTags() {
    return tags;
  }

  /**
   * This method ...
   * 
   * @param  tags ...
   */
  public void setTags(String[] tags) {
    this.tags = tags;
  }

  /**
   * This method ...
   * 
   * @return ...
   */
  public String[] getLemmas() {
    return lemmas;
  }

  /**
   * This method ...
   * 
   * @param  lemmas ...
   */
  public void setLemmas(String[] lemmas) {
    this.lemmas = lemmas;
  }


  /**
   * This method ...
   * 
   * @return ...
   */
  public String retrieveText() {
    return this.retrieveText(false);
  }

  /**
   * This method ...
   * 
   * @param  useLemmas ...
   * @return ...
   */
  public String retrieveText(boolean useLemmas) {
    StringBuffer text = new StringBuffer();
    if (useLemmas) {
      for (String lemma : lemmas) {
        text.append(lemma + " ");
      }
    }
    else {
      for (String token : tokens) {
        text.append(token + " ");
      }
    }
    return text.toString().trim();
  }

  /**
   * This method ...
   * 
   * @param  other ...
   * @param  type ...
   * @return ...
   */
  public Chunk merge(Chunk other, String type) {    
    ArrayList<String> tokens = new ArrayList<String>(Arrays.asList(
        this.getTokens()));
    tokens.addAll(Arrays.asList(other.getTokens()));
    ArrayList<String> tags = new ArrayList<String>(Arrays.asList(
        this.getTags()));
    tags.addAll(Arrays.asList(other.getTags()));
    ArrayList<String> lemmas = new ArrayList<String>(Arrays.asList(
        this.getLemmas()));
    lemmas.addAll(Arrays.asList(other.getLemmas()));
    return new Chunk(type, tokens.toArray(new String[tokens.size()]),
        tags.toArray(new String[tags.size()]),
        lemmas.toArray(new String[lemmas.size()]));
  }

  /**
   * This method ...
   * 
   * @return ...
   */
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((type == null) ? 0 : type.hashCode());
    result = prime * result + Arrays.hashCode(tokens);
    result = prime * result + Arrays.hashCode(tags);
    result = prime * result + Arrays.hashCode(lemmas);
    return result;
  }

  /**
   * This method ...
   * 
   * @return ...
   */
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    Chunk other = (Chunk) obj;
    if (type == null) {
      if (other.type != null) {
        return false;
      }
    }
    else if (!type.equals(other.type)) {
      return false;
    }
    if (!Arrays.equals(tokens, other.tokens)) {
      return false;
    }
    if (!Arrays.equals(tags, other.tags)) {
      return false;
    }
    if (!Arrays.equals(lemmas, other.lemmas)) {
      return false;
    }
    return true;
  }

  /**
   * This method ...
   * 
   * @return ...
   */
  public String toString() {
    return "Chunk [type=" + type + ", tokens=" + Arrays.toString(tokens)
    + ", tags=" + Arrays.toString(tags) + ", lemmas="
    + Arrays.toString(lemmas) + "]";
  }
}
