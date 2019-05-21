package fact;

/**
 * This class ...
 * 
 * @author   Ricardo Rodrigues
 * @version  0.9.9.9
 */
public class Fact {
  private String id = null;
  private String subject = null;
  private String predicate = null;
  private String object = null;
  private String sentenceID = null;
  private String documentID = null;

  /**
   * Creates a new ...
   * 
   * @param  id ...
   * @param  subject ...
   * @param  predicate ...
   * @param  object ...
   * @param  sentenceID ...
   * @param  documentID ...
   */
  public Fact(String id, String subject, String predicate, String object,
      String sentenceID, String documentID) {  
    this.id = id;
    this.subject = subject;
    this.predicate = predicate;
    this.object = object;
    this.sentenceID = sentenceID;
    this.documentID = documentID;
  }

  /**
   * This method ...
   * 
   * @return ...
   */
  public String getID() {
    return id;
  }

  /**
   * This method ...
   * 
   * @param  id ...
   */
  public void setID(String id) {
    this.id = id;
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
  public String getSentenceID() {
    return sentenceID;
  }

  /**
   * This method ...
   * 
   * @param  sentenceID ...
   */
  public void setSentenceID(String sentenceID) {
    this.sentenceID = sentenceID;
  }

  /**
   * This method ...
   * 
   * @return ...
   */
  public String getDocumentID() {
    return documentID;
  }

  /**
   * This method ...
   * 
   * @param  documentID ...
   */
  public void setDocumentID(String documentID) {
    this.documentID = documentID;
  }

  /**
   * This method ...
   * 
   * @return ...
   */
  public String getFullText() {
    return this.getSubject() + " " + this.getPredicate() + " "
        + this.getObject();
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
        + ((documentID == null) ? 0 : documentID.hashCode());
    result = prime * result
        + ((object == null) ? 0 : object.hashCode());
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result
        + ((predicate == null) ? 0 : predicate.hashCode());
    result = prime * result
        + ((sentenceID == null) ? 0 : sentenceID.hashCode());
    result = prime * result
        + ((subject == null) ? 0 : subject.hashCode());
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
    Fact other = (Fact) obj;
    if (documentID == null) {
      if (other.documentID != null) {
        return false;
      }
    }
    else if (!documentID.equals(other.documentID)) {
      return false;
    }
    if (object == null) {
      if (other.object != null) {
        return false;
      }
    }
    else if (!object.equals(other.object)) {
      return false;
    }
    if (id == null) {
      if (other.id != null) {
        return false;
      }
    }
    else if (!id.equals(other.id)) {
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
    if (sentenceID == null) {
      if (other.sentenceID != null) {
        return false;
      }
    }
    else if (!sentenceID.equals(other.sentenceID)) {
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
    return "Fact [id=" + id + ", subject=" + subject + ", predicate="
        + predicate + ", object=" + object + ", sentenceID=" + sentenceID
        + ", documentID=" + documentID + "]";
  }
}
