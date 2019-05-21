package dependency;

/**
 * This class ...
 *
 * @author   Ricardo Rodrigues
 * @version  0.9.9.9
 */
public class CoNLLToken implements Comparable<CoNLLToken> {
  private String id = null;
  private String form = null;
  private String lemma = null;
  private String coarseGrainedPOSTag = null;
  private String posTag = null;
  private String features = null;
  private String head = null;
  private String dependencyRelation = null;
  private String projectiveHead = null;
  private String projectiveDependencyRelation = null;

  /**
   * Creates a new ...
   * 
   * @param  id ...
   * @param  form ...
   * @param  lemma ...
   * @param  coarseGrainedPOSTag ...
   * @param  posTag ...
   * @param  features ...
   * @param  head ...
   * @param  dependencyRelation ...
   * @param  projectiveHead ...
   * @param  projectiveDependencyRelation ...
   */
  public CoNLLToken(String id, String form, String lemma,
      String coarseGrainedPOSTag, String posTag, String features, String head,
      String dependencyRelation, String projectiveHead,
      String projectiveDependencyRelation) {
    this.id = id;
    this.form = form;
    this.lemma = lemma;
    this.coarseGrainedPOSTag = coarseGrainedPOSTag;
    this.posTag = posTag;
    this.features = features;
    this.head = head;
    this.dependencyRelation = dependencyRelation;
    this.projectiveHead = projectiveHead;
    this.projectiveDependencyRelation = projectiveDependencyRelation;
  }

  /**
   * This method ...
   * 
   * @param  fields ...
   * @throws ArrayIndexOutOfBoundsException ...
   */
  public CoNLLToken(String[] fields) throws ArrayIndexOutOfBoundsException {
    this(fields[0], fields[1], fields[2], fields[3], fields[4], fields[5],
        fields[6], fields[7], fields[8], fields[9]);
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
  public String getForm() {
    return form;
  }

  /**
   * This method ...
   * 
   * @param  form ...
   */
  public void setForm(String form) {
    this.form = form;
  }

  /**
   * This method ...
   * 
   * @return ...
   */
  public String getLemma() {
    return lemma;
  }

  /**
   * This method ...
   * 
   * @param  lemma ...
   */
  public void setLemma(String lemma) {
    this.lemma = lemma;
  }

  /**
   * This method ...
   * 
   * @return ...
   */
  public String getCoarseGrainedPOSTag() {
    return coarseGrainedPOSTag;
  }

  /**
   * This method ...
   * 
   * @param  coarseGrainedPOSTag ...
   */
  public void setCoarseGrainedPOSTag(String coarseGrainedPOSTag) {
    this.coarseGrainedPOSTag = coarseGrainedPOSTag;
  }

  /**
   * This method ...
   * 
   * @return ...
   */
  public String getPOSTag() {
    return posTag;
  }

  /**
   * This method ...
   * 
   * @param  posTag ...
   */
  public void setPostag(String posTag) {
    this.posTag = posTag;
  }

  /**
   * This method ...
   * 
   * @return ...
   */
  public String getFeatures() {
    return features;
  }

  /**
   * This method ...
   * 
   * @param  features ...
   */
  public void setFeatures(String features) {
    this.features = features;
  }

  /**
   * This method ...
   * 
   * @return ...
   */
  public String getHead() {
    return head;
  }

  /**
   * This method ...
   * 
   * @param  head ...
   */
  public void setHead(String head) {
    this.head = head;
  }

  /**
   * This method ...
   * 
   * @return ...
   */
  public String getDependencyRelation() {
    return dependencyRelation;
  }

  /**
   * This method ...
   * 
   * @param  dependencyRelation ...
   */
  public void setDependencyRelation(String dependencyRelation) {
    this.dependencyRelation = dependencyRelation;
  }

  /**
   * This method ...
   * 
   * @return ...
   */
  public String getProjectiveHead() {
    return projectiveHead;
  }

  /**
   * This method ...
   * 
   * @param  projectiveHead ...
   */
  public void setProjectiveHead(String projectiveHead) {
    this.projectiveHead = projectiveHead;
  }

  /**
   * This method ...
   * 
   * @return ...
   */
  public String getProjectiveDependencyRelation() {
    return projectiveDependencyRelation;
  }

  /**
   * This method ...
   * 
   * @param  projectiveDependencyRelation ...
   */
  public void setProjectiveDependencyRelation(
      String projectiveDependencyRelation) {
    this.projectiveDependencyRelation = projectiveDependencyRelation;
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
        + ((coarseGrainedPOSTag == null) ? 0 : coarseGrainedPOSTag.hashCode());
    result = prime * result
        + ((dependencyRelation == null) ? 0 : dependencyRelation.hashCode());
    result = prime * result
        + ((features == null) ? 0 : features.hashCode());
    result = prime * result
        + ((form == null) ? 0 : form.hashCode());
    result = prime * result
        + ((head == null) ? 0 : head.hashCode());
    result = prime * result
        + ((id == null) ? 0 : id.hashCode());
    result = prime * result
        + ((lemma == null) ? 0 : lemma.hashCode());
    result = prime * result
        + ((projectiveDependencyRelation == null)
            ? 0 : projectiveDependencyRelation.hashCode());
    result = prime * result
        + ((projectiveHead == null) ? 0 : projectiveHead.hashCode());
    result = prime * result
        + ((posTag == null) ? 0 : posTag.hashCode());
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
    CoNLLToken other = (CoNLLToken) obj;
    if (coarseGrainedPOSTag == null) {
      if (other.coarseGrainedPOSTag != null) {
        return false;
      }
    }
    else if (!coarseGrainedPOSTag.equals(other.coarseGrainedPOSTag)) {
      return false;
    }
    if (dependencyRelation == null) {
      if (other.dependencyRelation != null) {
        return false;
      }
    }
    else if (!dependencyRelation.equals(other.dependencyRelation)) {
      return false;
    }
    if (features == null) {
      if (other.features != null) {
        return false;
      }
    }
    else if (!features.equals(other.features)) {
      return false;
    }
    if (form == null) {
      if (other.form != null) {
        return false;
      }
    }
    else if (!form.equals(other.form)) {
      return false;
    }
    if (head == null) {
      if (other.head != null) {
        return false;
      }
    }
    else if (!head.equals(other.head)) {
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
    if (lemma == null) {
      if (other.lemma != null) {
        return false;
      }
    }
    else if (!lemma.equals(other.lemma)) {
      return false;
    }
    if (projectiveDependencyRelation == null) {
      if (other.projectiveDependencyRelation != null) {
        return false;
      }
    }
    else if (!projectiveDependencyRelation.equals(
        other.projectiveDependencyRelation)) {
      return false;
    }
    if (projectiveHead == null) {
      if (other.projectiveHead != null) {
        return false;
      }
    }
    else if (!projectiveHead.equals(other.projectiveHead)) {
      return false;
    }
    if (posTag == null) {
      if (other.posTag != null) {
        return false;
      }
    }
    else if (!posTag.equals(other.posTag)) {
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
    return id + "\t" + form + "\t" + lemma + "\t" + coarseGrainedPOSTag + "\t"
        + posTag + "\t" + features + "\t" + head + "\t" + dependencyRelation
        + "\t" + projectiveHead  + "\t" + projectiveDependencyRelation;
  }

  /**
   * This method ...
   * 
   * @return ...
   */
  public CoNLLToken clone() {
    return new CoNLLToken(id, form, lemma, coarseGrainedPOSTag, posTag,
        features, head, dependencyRelation, projectiveHead,
        projectiveDependencyRelation);
  }

  /**
   * This method ...
   * 
   * @return ...
   */
  public int compareTo(CoNLLToken other) {
    if (Integer.parseInt(id) < Integer.parseInt(other.getID())) {
      return -1;
    }
    else if (Integer.parseInt(id) > Integer.parseInt(other.getID())) {
      return 1;
    }
    else {
      return 0;
    }
  }
}
