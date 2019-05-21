package triple;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

/**
 * This class ...
 *
 * @author   Ricardo Rodrigues
 * @version  0.9.9.9
 */
public class TripleStore {
  // indices
  private HashMap<String, HashSet<Triple>> subjectMap = null;
  private HashMap<String, HashSet<Triple>> predicateMap = null;
  private HashMap<String, HashSet<Triple>> objectMap = null;
  // properties
  private HashSet<String> symmetricPredicates = null;
  private HashMap<String, String> inversePredicates = null;

  /**
   * Creates a new ...
   */
  public TripleStore() {
    this(16); // default HashMap initial capacity
  }

  /**
   * Creates a new ...
   * 
   * @param  tripleStream ...
   * @throws IOException ...
   * @throws TripleLoadException ...
   */
  public TripleStore(InputStream tripleStream)
      throws IOException, TripleLoadException {
    this(16);
    this.load(tripleStream);
  }

  /**
   * Creates a new ...
   * 
   * @param  tripleStream ...
   * @param  symmetricPredicates ...
   * @throws IOException ...
   * @throws TripleLoadException ...
   */
  public TripleStore(InputStream tripleStream,
      HashSet<String> symmetricPredicates)
          throws IOException, TripleLoadException {
    this(16, symmetricPredicates, new HashMap<String, String>());
    this.load(tripleStream);
  }

  /**
   * Creates a new ...
   * 
   * @param  tripleStream ...
   * @param  inversePredicates ...
   * @throws IOException ...
   * @throws TripleLoadException ...
   */
  public TripleStore(InputStream tripleStream, 
      HashMap<String, String> inversePredicates)
          throws IOException, TripleLoadException {
    this(16, new HashSet<String>(), inversePredicates);
    this.load(tripleStream);
  }

  /**
   * Creates a new ...
   * 
   * @param  tripleStream ...
   * @param  symmetricPredicates ...
   * @param  inversePredicates ...
   * @throws IOException ...
   * @throws TripleLoadException ...
   */
  public TripleStore(InputStream tripleStream,
      HashSet<String> symmetricPredicates,
      HashMap<String, String> inversePredicates)
          throws IOException, TripleLoadException {
    this(16, symmetricPredicates, inversePredicates);
    this.load(tripleStream);
  }


  /**
   * Creates a new ...
   * 
   * @param  initialCapacity ...
   */
  public TripleStore(int initialCapacity) {
    this(initialCapacity, new HashSet<String>(), new HashMap<String, String>());
  }

  /**
   * Creates a new ...
   * 
   * @param  initialCapacity ...
   * @param  symmetricPredicates ...
   */
  public TripleStore(int initialCapacity, HashSet<String> symmetricPredicates) {
    this(initialCapacity, symmetricPredicates, new HashMap<String, String>());
  }

  /**
   * Creates a new ...
   * 
   * @param  initialCapacity ...
   * @param  inversePredicates ...
   */
  public TripleStore(int initialCapacity,
      HashMap<String, String> inversePredicates) {
    this(initialCapacity, new HashSet<String>(), inversePredicates);
  }

  /**
   * Creates a new ...
   * 
   * @param  initialCapacity ...
   * @param  symmetricPredicates ...
   * @param  inversePredicates ...
   */
  public TripleStore(int initialCapacity, HashSet<String> symmetricPredicates,
      HashMap<String, String> inversePredicates) {
    this.subjectMap = new HashMap<String, HashSet<Triple>>(initialCapacity);
    this.predicateMap = new HashMap<String, HashSet<Triple>>(initialCapacity);
    this.objectMap = new HashMap<String, HashSet<Triple>>(initialCapacity);
    this.symmetricPredicates = symmetricPredicates;
    this.inversePredicates = inversePredicates;
  }

  /**
   * This method ...
   * 
   * @param  triple ...
   */
  public void add(Triple triple) {
    HashSet<Triple> tripleSet = null;
    // subject
    tripleSet = subjectMap.get(triple.getSubject());
    if (tripleSet == null) {
      tripleSet = new HashSet<Triple>();
    }
    tripleSet.add(triple); 
    subjectMap.put(triple.getSubject(), tripleSet);
    // predicate
    tripleSet = predicateMap.get(triple.getPredicate());
    if (tripleSet == null) {
      tripleSet = new HashSet<Triple>();
    }
    tripleSet.add(triple); 
    predicateMap.put(triple.getPredicate(), tripleSet);
    // object
    tripleSet = objectMap.get(triple.getObject());
    if (tripleSet == null) {
      tripleSet = new HashSet<Triple>();
    }
    tripleSet.add(triple); 
    objectMap.put(triple.getObject(), tripleSet);
  }

  /**
   * This method ...
   * 
   * @param  triple ...
   */
  public void remove(Triple triple) {
    HashSet<Triple> tripleSet = null;
    // subject
    tripleSet = subjectMap.get(triple.getSubject());
    if (tripleSet != null) {
      tripleSet.remove(triple);
      if (tripleSet.size() > 0) {
        subjectMap.put(triple.getSubject(), tripleSet);
      }
      else {
        subjectMap.remove(triple.getSubject());
      }
    }
    // predicate
    tripleSet = predicateMap.get(triple.getPredicate());
    if (tripleSet != null) {
      tripleSet.remove(triple);
      if (tripleSet.size() > 0) {
        predicateMap.put(triple.getPredicate(), tripleSet);
      }
      else {
        predicateMap.remove(triple.getPredicate());
      }
    }
    // object
    tripleSet = objectMap.get(triple.getObject());
    if (tripleSet != null) {
      tripleSet.remove(triple);
      if (tripleSet.size() > 0) {
        objectMap.put(triple.getObject(), tripleSet);
      }
      else {
        objectMap.remove(triple.getObject());
      }
    }
  }

  /**
   * This method ...
   * 
   * @param  subject ...
   * @return ...
   */
  public Triple[] querySubject(String subject) {
    HashSet<Triple> subjectTripleList = new HashSet<Triple>();
    if (subjectMap.containsKey(subject)) {
      subjectTripleList = subjectMap.get(subject);
    }
    return subjectTripleList.toArray(new Triple[subjectTripleList.size()]);
  }

  /**
   * This method ...
   * 
   * @param  predicate ...
   * @return ...
   */
  public Triple[] queryPredicate(String predicate) {
    HashSet<Triple> predicateTripleList = new HashSet<Triple>();
    if (predicateMap.containsKey(predicate)) {
      predicateTripleList = predicateMap.get(predicate);
    }
    return predicateTripleList.toArray(new Triple[predicateTripleList.size()]);
  }

  /**
   * This method ...
   * 
   * @param  object ...
   * @return ...
   */
  public Triple[] queryObject(String object) {
    HashSet<Triple> objectTripleList = new HashSet<Triple>();
    if (objectMap.containsKey(object)) {
      objectTripleList = objectMap.get(object);
    }
    return objectTripleList.toArray(new Triple[objectTripleList.size()]);
  }

  /**
   * This method ...
   * 
   * @param  subject ...
   * @param  predicate ...
   * @return ...
   */
  public Triple[] querySubjectPredicate(String subject, String predicate) {
    // subject
    ArrayList<Triple> subjectTripleList = new ArrayList<Triple>(
        Arrays.asList(this.querySubject(subject)));
    // predicate
    ArrayList<Triple> predicateTripleList = new ArrayList<Triple>(
        Arrays.asList(this.queryPredicate(predicate)));
    // intersection
    ArrayList<Triple> commonTriples = new ArrayList<Triple>(subjectTripleList);
    commonTriples.retainAll(predicateTripleList);
    return commonTriples.toArray(new Triple[commonTriples.size()]);
  }

  /**
   * This method ...
   * 
   * @param  subject ...
   * @param  object ...
   * @return ...
   */
  public Triple[] querySubjectObject(String subject, String object) {
    // subject
    ArrayList<Triple> subjectTripleList = new ArrayList<Triple>(
        Arrays.asList(this.querySubject(subject)));
    // object
    ArrayList<Triple> objectTripleList = new ArrayList<Triple>(
        Arrays.asList(this.queryObject(object)));
    // intersection
    ArrayList<Triple> commonTriples = new ArrayList<Triple>(subjectTripleList);
    commonTriples.retainAll(objectTripleList);
    return commonTriples.toArray(new Triple[commonTriples.size()]);
  }

  /**
   * This method ...
   * 
   * @param  predicate ...
   * @param  object ...
   * @return ...
   */
  public Triple[] queryPredicateObject(String predicate, String object) {
    // predicate
    ArrayList<Triple> predicateTripleList = new ArrayList<Triple>(
        Arrays.asList(this.queryPredicate(predicate)));
    // object
    ArrayList<Triple> objectTripleList = new ArrayList<Triple>(
        Arrays.asList(this.queryObject(object)));
    // intersection
    ArrayList<Triple> commonTriples = new ArrayList<Triple>(
        predicateTripleList);
    commonTriples.retainAll(objectTripleList);
    return commonTriples.toArray(new Triple[commonTriples.size()]);
  }

  /**
   * This method ...
   * 
   * @param  subject ...
   * @param  predicate ...
   * @param  object ...
   * @return ...
   */
  public Triple[] querySubjectPredicateObject(String subject, String predicate,
      String object) {
    // subject
    ArrayList<Triple> subjectTripleList = new ArrayList<Triple>(
        Arrays.asList(this.querySubject(subject)));
    // predicate
    ArrayList<Triple> predicateTripleList = new ArrayList<Triple>(
        Arrays.asList(this.queryPredicate(predicate)));
    // object
    ArrayList<Triple> objectTripleList = new ArrayList<Triple>(
        Arrays.asList(this.queryObject(object)));
    // intersection
    ArrayList<Triple> commonTriples = new ArrayList<Triple>(
        subjectTripleList);
    commonTriples.retainAll(predicateTripleList);
    commonTriples.retainAll(objectTripleList);
    return commonTriples.toArray(new Triple[commonTriples.size()]);
  }

  /**
   * This method ...
   * 
   * @return ...
   */
  public HashSet<String> getSymmetricPredicates() {
    return symmetricPredicates;
  }

  /**
   * This method ...
   * 
   * @param symmetricPredicates ...
   */
  public void setSymmetricPredicates(HashSet<String> symmetricPredicates) {
    this.symmetricPredicates = symmetricPredicates;
  }

  /**
   * This method ...
   * 
   * @return ...
   */
  public HashMap<String, String> getInversePredicates() {
    return inversePredicates;
  }

  /**
   * This method ...
   * 
   * @param  inversePredicates ...
   */
  public void setInversePredicates(HashMap<String, String> inversePredicates) {
    this.inversePredicates = inversePredicates;
  }

  /**
   * This method ...
   * 
   * @param  tripleInput ...
   * @throws IOException ...
   * @throws TripleLoadException ...
   */
  public void load(InputStream tripleInput)
      throws IOException, TripleLoadException  {
    BufferedReader reader = new BufferedReader(
        new InputStreamReader(tripleInput));
    ArrayList<String> tripleLines = new ArrayList<String>();
    String line = null;
    while ((line = reader.readLine()) != null) {
      if (line.length() > 0);
      tripleLines.add(line);
    }
    reader.close();
    String[] elements = null;
    String subject = null;
    String predicate = null;
    String object = null;
    for (String tripleLine : tripleLines) {
      elements = tripleLine.split("\\s");
      if (elements.length != 3) {
        throw new TripleLoadException("Triple parsing error: there sould be "
            + "three elements (subject, predicate, object) in each line.");
      }
      else {
        subject = elements[0];
        predicate = elements[1];
        object = elements[2];
        Triple triple = new Triple(subject, predicate,  object);
        this.add(triple);
        if (symmetricPredicates.contains(predicate)) {
          triple = new Triple(object, predicate, subject);
          this.add(triple);
        }
        if (inversePredicates.containsKey(predicate)) {
          triple = new Triple(object, inversePredicates.get(predicate),
              subject);
          this.add(triple);
        }
      }
    }
  }

  /**
   * This method ...
   * 
   * @return ...
   */
  public Triple[] retrieveAllTriples() {
    HashSet<Triple> allTriples = new HashSet<Triple>();
    for (HashSet<Triple> tripleSet : subjectMap.values()) {
      for (Triple triple : tripleSet) {
        allTriples.add(triple);
      }
    }
    for (HashSet<Triple> tripleSet : predicateMap.values()) {
      for (Triple triple : tripleSet) {
        allTriples.add(triple);
      }
    }
    for (HashSet<Triple> tripleSet : objectMap.values()) {
      for (Triple triple : tripleSet) {
        allTriples.add(triple);
      }
    }
    return allTriples.toArray(new Triple[allTriples.size()]);
  }

  /**
   * This method ...
   * 
   * @param  triples ...
   * @return ...
   */
  public String[] retrieveSubjects(Triple[] triples) {
    ArrayList<String> subjects = new ArrayList<String>();
    for (Triple triple : triples) {
      subjects.add(triple.getSubject());
    }
    return subjects.toArray(new String[subjects.size()]);
  }

  /**
   * This method ...
   * 
   * @param  triples ...
   * @return ...
   */
  public String[] retrievePredicates(Triple[] triples) {
    ArrayList<String> predicates = new ArrayList<String>();
    for (Triple triple : triples) {
      predicates.add(triple.getPredicate());
    }
    return predicates.toArray(new String[predicates.size()]);
  }

  /**
   * This method ...
   * 
   * @param  triples ...
   * @return ...
   */
  public String[] retrieveObjects(Triple[] triples) {
    ArrayList<String> objects = new ArrayList<String>();
    for (Triple triple : triples) {
      objects.add(triple.getObject());
    }
    return objects.toArray(new String[objects.size()]);
  }

  /**
   * This method ...
   * 
   * @param  tripleData ...
   * @throws IOException ...
   */
  public void save(OutputStream tripleData) throws IOException {
    Triple[] triples = this.retrieveAllTriples();
    for (Triple triple : triples) {
      tripleData.write(new String(triple.getSubject()
          + " " + triple.getPredicate() + " " + triple.getObject()
          + "\n").getBytes());
    }
  }

  /**
   * This method ...
   * 
   * @return ...
   */
  public int size() {
    return this.retrieveAllTriples().length;
  }
}
