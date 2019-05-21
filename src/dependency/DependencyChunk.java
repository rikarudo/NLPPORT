package dependency;

import java.util.Arrays;

/**
 * This class ...
 *
 * @author   Ricardo Rodrigues
 * @version  0.9.9.9
 */
public class DependencyChunk implements Comparable<DependencyChunk> {
  private int id = 0;
  private int head = 0;
  private String function = null;
  private CoNLLToken[] tokens = null;

  /**
   * Creates a new ...
   * 
   * @param  id ...
   * @param  function ...
   * @param  head ...
   * @param  tokens ...
   */
  public DependencyChunk(int id, String function, int head,
      CoNLLToken[] tokens) {
    this.id = id;
    this.head = head;
    this.function = function;
    this.tokens = tokens;
  }

  /**
   * This method ...
   * 
   * @return ...
   */
  public int getID() {
    return id;
  }

  /**
   * This method ...
   * 
   * @param  id ...
   */
  public void setID(int id) {
    this.id = id;
  }

  /**
   * This method ...
   * 
   * @return ...
   */
  public int getHead() {
    return head;
  }

  /**
   * This method ...
   * 
   * @param  head ...
   */
  public void setHead(int head) {
    this.head = head;
  }

  /**
   * This method ...
   * 
   * @return ...
   */
  public String getFunction() {
    return function;
  }

  /**
   * This method ...
   * 
   * @param  function ...
   */
  public void setFunction(String function) {
    this.function = function;
  }

  /**
   * This method ...
   * 
   * @return ...
   */
  public CoNLLToken[] getTokens() {
    return tokens;
  }

  /**
   * This method ...
   * 
   * @param  tokens ...
   */
  public void setTokens(CoNLLToken[] tokens) {
    this.tokens = tokens;
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
      for (CoNLLToken token : tokens) {
        text.append(token.getLemma() + " ");
      }
    }
    else {
      for (CoNLLToken token : tokens) {
        text.append(token.getForm() + " ");
      }
    }
    return text.toString().trim();
  }

  /**
   * This method ...
   * 
   * @return ...
   */
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((function == null) ? 0 : function.hashCode());
    result = prime * result + head;
    result = prime * result + id;
    result = prime * result + Arrays.hashCode(tokens);
    return result;
  }

  /**
   * This method ...
   * 
   * @param  obj ...
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
    DependencyChunk other = (DependencyChunk) obj;
    if (function == null) {
      if (other.function != null) {
        return false;
      }
    }
    else if (!function.equals(other.function)) {
      return false;
    }
    if (head != other.head) {
      return false;
    }
    if (id != other.id) {
      return false;
    }
    if (!Arrays.equals(tokens, other.tokens)) {
      return false;
    }
    return true;
  }

  /**
   * This method ...
   * 
   * @return ...
   */
  public DependencyChunk clone() {
    return new DependencyChunk(id, function, head, tokens.clone());
  }

  /**
   * This method ...
   * 
   * @return ...
   */
  public int compareTo(DependencyChunk other) {
    if (id < other.getID()) {
      return -1;
    }
    else if (id > other.getID()) {
      return 1;
    }
    else {
      return 0;
    }
  }

  /**
   * This method ...
   * 
   * @return ...
   */
  public String toString() {
    return "DependencyChunk [id=" + id + ", head=" + head + ", function="
        + function + ", tokens=" + Arrays.toString(tokens) + "]";
  }
}
