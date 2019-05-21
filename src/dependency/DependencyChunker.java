package dependency;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

/**
 * This class ...
 *
 * @author   Ricardo Rodrigues
 * @version  0.9.9.9
 */
public class DependencyChunker {
  private static final String DEFAULT_PROP =
      "resources/config/nlpport.properties";

  private CoNLLToken[] sentence = null;
  private String[] functions = null;
  private String[] exclusions = null;
  private String root = null;

  /**
   * Creates a new ...
   * @param  sentence ...
   * @throws InvalidPropertiesFormatException ...
   * @throws IOException ...
   */
  public DependencyChunker(CoNLLToken[] sentence)
      throws InvalidPropertiesFormatException, IOException {
    this.sentence = sentence.clone();
    Properties properties = new Properties();
    properties.load(
        this.getClass().getClassLoader().getResourceAsStream(DEFAULT_PROP));
    this.functions = 
        properties.getProperty("grammaticalFunctions").split(";");
    this.exclusions = 
        properties.getProperty("grammaticalExclusions").split(";");
    this.root = properties.getProperty("grammaticalRoot");
  }

  /**
   * Creates a new...
   * 
   * @param  sentence ...
   * @param  functions ...
   * @param  exclusions ...
   * @param  root ...
   */
  public DependencyChunker(CoNLLToken[] sentence, String[] functions,
      String[] exclusions, String root) {
    this.sentence = sentence.clone();
    this.functions = functions.clone();
    this.exclusions = exclusions.clone();
    this.root = new String(root);
  }

  /**
   * This method ...
   * 
   * @param  id ...
   * @return ...
   */
  public CoNLLToken retrieve(String id) {
    for (CoNLLToken token : sentence) {
      if (token.getID().equalsIgnoreCase(id)) {
        return token;
      }
    }
    return null;
  }

  /**
   * This method ...
   * 
   * @param  deprel ...
   * @param  head ...
   * @return ...
   */
  public CoNLLToken retrieve(String deprel, String head) {
    for (CoNLLToken token : sentence) {
      if (token.getDependencyRelation().equalsIgnoreCase(deprel)
          && token.getHead().equalsIgnoreCase(head)) {
        return token;
      }
    }
    return null;
  }

  /**
   * This method ...
   * 
   * @return ...
   */
  public CoNLLToken[] retrieveAll() {
    return sentence;
  }

  /**
   * This method ...
   * 
   * @param  head ...
   * @return ...
   */
  public CoNLLToken[] retrieveDependants(CoNLLToken head) {
    return this.retrieveDependants(head, false, false);
  }

  /**
   * This method ...
   * 
   * @param  head ...
   * @param  includeHead ...
   * @return ...
   */
  public CoNLLToken[] retrieveDependants(CoNLLToken head, boolean includeHead) {
    return this.retrieveDependants(head, includeHead, false);
  }

  /**
   * This method ...
   * 
   * @param  head ...
   * @param  includeHead ...
   * @param  stopOnNewRelation ...
   * @return ...
   */
  public CoNLLToken[] retrieveDependants(CoNLLToken head, boolean includeHead,
      boolean stopOnNewRelation) {
    ArrayList<CoNLLToken> dependants = new ArrayList<CoNLLToken>();
    for (CoNLLToken token : sentence) {
      if (token.getHead().equalsIgnoreCase(head.getID())) {
        if (stopOnNewRelation) {
          if (!Arrays.asList(functions).contains(
              token.getDependencyRelation())) {
            if (!Arrays.asList(exclusions).contains(
                token.getDependencyRelation())) {
              dependants.add(token);
              if (this.hasDependants(token)) {
                dependants.addAll(Arrays.asList(retrieveDependants(
                    token, false, stopOnNewRelation)));
              }
            }
          }
        }
        else {
          if (!Arrays.asList(exclusions).contains(
              token.getDependencyRelation())) {
            dependants.add(token);
            if (this.hasDependants(token)) {
              dependants.addAll(Arrays.asList(retrieveDependants(
                  token, false, stopOnNewRelation)));
            }
          }
        }
      }
    }
    if (includeHead) {
      dependants.add(head);
    }
    Collections.sort(dependants);
    return dependants.toArray(new CoNLLToken[dependants.size()]);
  }

  /**
   * This method ...
   * 
   * @param  includeHead ...
   * @param  stopOnNewRelation ...
   * @return ...
   */
  public CoNLLToken[] retrieveRootDependants(boolean includeHead,
      boolean stopOnNewRelation) {
    ArrayList<CoNLLToken> dependants = new ArrayList<CoNLLToken>();
    CoNLLToken head = null;
    for (CoNLLToken token : sentence) {
      if (token.getDependencyRelation().equals(root)) {
        head = token.clone();
      }
    }
    if (head != null) {
      for (CoNLLToken token : sentence) {
        if (token.getHead().equalsIgnoreCase(head.getID())) {
          if (stopOnNewRelation) {
            if (!Arrays.asList(functions).contains(
                token.getDependencyRelation())) {
              if (!Arrays.asList(exclusions).contains(
                  token.getDependencyRelation())) {
                dependants.add(token);
                if (this.hasDependants(token)) {
                  dependants.addAll(Arrays.asList(retrieveDependants(
                      token, false, stopOnNewRelation)));
                }
              }
            }
            else {
              dependants.add(token);
            }
          }
          else {
            if (!Arrays.asList(exclusions).contains(
                token.getDependencyRelation())) {
              dependants.add(token);
              if (this.hasDependants(token)) {
                dependants.addAll(Arrays.asList(retrieveDependants(
                    token, false, stopOnNewRelation)));
              }
            }
          }
        }
      }
      if (includeHead) {
        dependants.add(head);
      }
    }
    Collections.sort(dependants);
    return dependants.toArray(new CoNLLToken[dependants.size()]);
  }

  /**
   * This method ...
   * 
   * @param  head ...
   * @return ...
   */
  public boolean hasDependants(CoNLLToken head) {
    for (CoNLLToken token : sentence) {
      if (token.getHead().equalsIgnoreCase(head.getID())) {
        return true;
      }
    }
    return false;
  }

  /**
   * This method ...
   * 
   * @return ...
   */
  public DependencyChunk[] parseChunks() {
    // the chunk's id is dependent on the index of the token found
    // at its root: the first chunk corresponds to the index of the head token
    // that was found first, the second chunk to the second head token
    // and so on, as the tokens are processed sequentially
    ArrayList<DependencyChunk> chunkList = new ArrayList<DependencyChunk>();
    int chunkID = 0;
    for (CoNLLToken token : sentence) {
      ArrayList<CoNLLToken> tokenList = new ArrayList<CoNLLToken>();
      for (String grammaticalFunction : functions) {
        if (token.getDependencyRelation().equalsIgnoreCase(
            grammaticalFunction)) {
          CoNLLToken[] dependants = this.retrieveDependants(token, true, true);
          for (CoNLLToken dependant : dependants) {
            tokenList.add(dependant);
          }
          if (tokenList.size() > 0) {
            chunkID++;
            // defaults the chunk's head to zero (0)
            chunkList.add(new DependencyChunk(chunkID,
                token.getDependencyRelation(), 0, tokenList.toArray(
                    new CoNLLToken[tokenList.size()])));
          }
          break;
        }
      }
    }

    ////////////////////////////////////////////////////////////////////////////

    // try to assign the correct head to each chunk
    DependencyChunk[] chunks = chunkList.toArray(
        new DependencyChunk[chunkList.size()]);
    for (CoNLLToken token : sentence) {
      for (String grammaticalFunction : functions) {
        if (token.getDependencyRelation().equalsIgnoreCase(
            grammaticalFunction)) {
          String headID = token.getHead();
          for (CoNLLToken headToken : sentence) {
            if (headToken.getID().equals(headID)) {
              for (int i = 0; i < chunks.length; i++) {
                ArrayList<CoNLLToken> tokenList = new ArrayList<CoNLLToken>(
                    Arrays.asList(chunks[i].getTokens()));
                if (tokenList.contains(token)) {
                  for (int j = 0; j < chunks.length; j++) {
                    ArrayList<CoNLLToken> headTokenList = 
                        new ArrayList<CoNLLToken>(
                            Arrays.asList(chunks[j].getTokens()));
                    if (headTokenList.contains(headToken)
                        && !tokenList.contains(headToken)) {
                      chunks[i].setHead(chunks[j].getID());
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
    return chunks;
  }

  /**
   * This method ...
   * 
   * @return ...
   */
  public DependencyChunk[] parseRootChunks() {
    // the chunk's id is dependent on the index of the token found
    // at its root: the first chunk corresponds to the index of the head token
    // that was found first, the second chunk to the second head token
    // and so on, as the tokens are processed sequentially
    ArrayList<DependencyChunk> chunkList = new ArrayList<DependencyChunk>();
    int chunkID = 0;
    CoNLLToken[] rootDependants = this.retrieveRootDependants(true, true);
    for (CoNLLToken rootDependant : rootDependants) {
      ArrayList<CoNLLToken> tokenList = new ArrayList<CoNLLToken>();
      CoNLLToken[] dependants = null;
      if (rootDependant.getDependencyRelation().equals(root)) {
        dependants = this.retrieveDependants(rootDependant, true, true);
      }
      else {
        dependants = this.retrieveDependants(rootDependant, true, false);
      }

      for (CoNLLToken dependant : dependants) {
        tokenList.add(dependant);
      }
      if (tokenList.size() > 0) {
        chunkID++;
        // defaults the chunk's head to zero (0)
        chunkList.add(new DependencyChunk(chunkID,
            rootDependant.getDependencyRelation(), 0, tokenList.toArray(
                new CoNLLToken[tokenList.size()])));
      }
    }

    ////////////////////////////////////////////////////////////////////////////

    // try to assign the correct head to each chunk
    DependencyChunk[] chunks = chunkList.toArray(
        new DependencyChunk[chunkList.size()]);
    for (CoNLLToken token : sentence) {
      for (String grammaticalFunction : functions) {
        if (token.getDependencyRelation().equalsIgnoreCase(
            grammaticalFunction)) {
          String headID = token.getHead();
          for (CoNLLToken headToken : sentence) {
            if (headToken.getID().equals(headID)) {
              for (int i = 0; i < chunks.length; i++) {
                ArrayList<CoNLLToken> tokenList = new ArrayList<CoNLLToken>(
                    Arrays.asList(chunks[i].getTokens()));
                if (tokenList.contains(token)) {
                  for (int j = 0; j < chunks.length; j++) {
                    ArrayList<CoNLLToken> headTokenList = 
                        new ArrayList<CoNLLToken>(
                            Arrays.asList(chunks[j].getTokens()));
                    if (headTokenList.contains(headToken)
                        && !tokenList.contains(headToken)) {
                      chunks[i].setHead(chunks[j].getID());
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
    return chunks;
  }
}

/*
adj: Adjective
adv: Adverb
art: Article
n: Noun
num: Numeral
pron: Pronoun
prop: ProperNoun
prp: Preposition
punc: Punctuation
v: Verb

ROOT: Root
>S: ComplementizerDependent
ACC: DirectObject
ADVL: AdjunctAdverbial
ADVO: ObjectRelatedArgumentAdverbial
ADVS: SubjectRelatedArgumentAdverbial
AUX: AuxiliaryVerb
DAT: DativeObject
FOC: FocusMarker
MV: MainVerb
NPHR: TopNodeNounPhrase
OC: ObjectComplement
P: Predicator
PASS: PassiveAdjunct
PAUX: PredicateAuxiliaryVerb
PIV: PrepositionalObject
PMV: PredicateMainVerb
PRED: AdjunctPredicative
S<: StatementPredicative
SC: SubjectComplement
SUBJ: Subject
TOP: TopicConstituent
VOC: VocativeAdjunct
 */
