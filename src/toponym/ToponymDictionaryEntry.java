package toponym;

/**
 * This class ...
 *
 * @author   Ricardo Rodrigues
 * @version  0.9.9.9
 */
public class ToponymDictionaryEntry {
  private String toponym = null;
  private String type = null;
  private String demonym = null;
  private String partOfSpeech = null;
  private String holonym = null;

  /**
   * Creates a new ...
   * 
   * @param  toponym ...
   * @param  type ...
   * @param  demonym ...
   * @param  partOfSpeech ...
   * @param  holonym ...
   */
  public ToponymDictionaryEntry(String toponym, String type, String demonym,
      String partOfSpeech, String holonym) {
    this.toponym = toponym;
    this.type = type;
    this.demonym = demonym;
    this.partOfSpeech = partOfSpeech;
    this.holonym = holonym;
  }

  /**
   * This method ...
   * 
   * @return ...
   */
  public String getToponym() {
    return toponym;
  }

  /**
   * This method ...
   * 
   * @param  toponym ...
   */
  public void setToponym(String toponym) {
    this.toponym = toponym;
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
  public String getDemonym() {
    return demonym;
  }

  /**
   * This method ...
   * 
   * @param  demonym ...
   */
  public void setDemonym(String demonym) {
    this.demonym = demonym;
  }

  /**
   * This method ...
   * 
   * @return ...
   */
  public String getPartOfSpeech() {
    return partOfSpeech;
  }

  /**
   * This method ...
   * 
   * @param  partOfSpeech ...
   */
  public void setPartOfSpeech(String partOfSpeech) {
    this.partOfSpeech = partOfSpeech;
  }

  /**
   * This method ...
   * 
   * @return ...
   */
  public String getHolonym() {
    return holonym;
  }

  /**
   * This method ...
   * 
   * @param  holonym ...
   */
  public void setHolonym(String holonym) {
    this.holonym = holonym;
  }

  /**
   * This method ...
   * 
   * @return ...
   */
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result
        + ((toponym == null) ? 0 : toponym.hashCode());
    result = prime * result + ((type == null) ? 0 : type.hashCode());
    result = prime * result
        + ((partOfSpeech == null) ? 0 : partOfSpeech.hashCode());
    result = prime * result
        + ((holonym == null) ? 0 : holonym.hashCode());
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
    ToponymDictionaryEntry other = (ToponymDictionaryEntry) obj;
    if (toponym == null) {
      if (other.toponym != null) {
        return false;
      }
    }
    else if (!toponym.equals(other.toponym)) {
      return false;
    }
    if (type == null) {
      if (other.type != null) {
        return false;
      }
    }
    else if (!type.equals(other.type)) {
      return false;
    }
    if (demonym == null) {
      if (other.demonym != null) {
        return false;
      }
    }
    else if (!demonym.equals(other.demonym)) {
      return false;
    }
    if (partOfSpeech == null) {
      if (other.partOfSpeech != null) {
        return false;
      }
    }
    else if (!partOfSpeech.equals(other.partOfSpeech)) {
      return false;
    }
    if (holonym == null) {
      if (other.holonym != null) {
        return false;
      }
    }
    else if (!holonym.equals(other.holonym)) {
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
    return new String(toponym + ":" + type + ":" + demonym + ":"
        + partOfSpeech);
  }
}
