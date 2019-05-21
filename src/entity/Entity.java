package entity;

import java.util.Arrays;

/**
 * This class ...
 *
 * @author   Ricardo Rodrigues
 * @version  0.9.9.9
 */
public class Entity implements Comparable<Entity> {
  private String text = null;
  private String type = null;
  private String[] tokens = null;

  /**
   * Creates a new ...
   * @param  text ...
   * @param  type ...
   * @param  tokens ...
   */
  public Entity(String text, String type, String[] tokens) {
    this.text = text;
    this.type = type;
    this.tokens = tokens;
  }

  /**
   * This method ...
   * 
   * @return ...
   */
  public String getText() {
    return text;
  }

  /**
   * This method ...
   * 
   * @param  text ...
   */
  public void setText(String text) {
    this.text = text;
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
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((text == null) ? 0 : text.hashCode());
    result = prime * result + Arrays.hashCode(tokens);
    result = prime * result + ((type == null) ? 0 : type.hashCode());
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
    Entity other = (Entity) obj;
    if (text == null) {
      if (other.text != null) {
        return false;
      }
    }
    else if (!text.equals(other.text)) {
      return false;
    }
    if (!Arrays.equals(tokens, other.tokens)) {
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
    return true;
  }

  /**
   * This method ...
   * 
   * @return ...
   */
  public String toString() {
    return new String("<START:" + type + "> " + text + " <END>");
  }

  /**
   * This method ...
   * 
   * @param  other ...
   * @return ...
   */
  public int compareTo(Entity other) {
    int totalLength = this.getText().length() * 100
        + this.getTokens().length * 10 + this.getType().length();
    int otherTotalLength = other.getText().length() * 100
        + other.getTokens().length * 10 + other.getType().length();
    if (totalLength < otherTotalLength) {
      return 1;
    }
    else if (totalLength > otherTotalLength) {
      return -1;
    }
    else {
      return 0;
    }
  }
}
