package triple;

/**
 * This class ...
 *
 * @author   Ricardo Rodrigues
 * @version  0.9.9.9
 */
public class Triple {
  private String subject = null;
  private String predicate = null;
  private String object = null;

  /**
   * Creates a new ...
   * 
   * @param  subject ...
   * @param  predicate ...
   * @param  object ...
   */
  public Triple(String subject, String predicate, String object) {
    this.subject = subject;
    this.predicate = predicate;
    this.object = object;
  }

  /**
   * This method ...
   * 
   * @return ...
   */
  public String getSubject() {
    return subject;
  }

  /**
   * This method ...
   * 
   * @param  subject ...
   */
  public void setSubject(String subject) {
    this.subject = subject;
  }

  /**
   * This method ...
   * 
   * @return ...
   */
  public String getPredicate() {
    return predicate;
  }

  /**
   * This method ...
   * 
   * @param  predicate ...
   */
  public void setPredicate(String predicate) {
    this.predicate = predicate;
  }

  /**
   * This method ...
   * 
   * @return ...
   */
  public String getObject() {
    return object;
  }

  /**
   * This method ...
   * 
   * @param  object ...
   */
  public void setObject(String object) {
    this.object = object;
  }

  /**
   * This method ...
   * 
   * @return ...
   */
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((object == null) ? 0 : object.hashCode());
    result = prime * result + ((predicate == null) ? 0 : predicate.hashCode());
    result = prime * result + ((subject == null) ? 0 : subject.hashCode());
    return result;
  }

  /**
   * This method ...
   * 
   *  @param  obj ...
   *  @return ...
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
    Triple other = (Triple) obj;
    if (object == null) {
      if (other.object != null) {
        return false;
      }
    }
    else if (!object.equals(other.object)) {
      return false;
    }
    if (predicate == null) {
      if (other.predicate != null) {
        return false;
      }
    }
    else if (!predicate.equals(other.predicate)) {
      return false;
    }
    if (subject == null) {
      if (other.subject != null) {
        return false;
      }
    }
    else if (!subject.equals(other.subject)) {
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
    return "Triple [subject=" + subject + ", predicate=" + predicate
        + ", object=" + object + "]";
  }
}
