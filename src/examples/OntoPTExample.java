package examples;

import java.io.IOException;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.status.StatusLogger;
import org.eclipse.rdf4j.repository.RepositoryException;
import org.eclipse.rdf4j.rio.RDFParseException;

import ontopt.OntoPTRepository;

public class OntoPTExample {

  public static void main(String[] args) throws RDFParseException,
  RepositoryException, IOException, NumberFormatException {
    // the following line disables Log4J output to the console
    // (if you don't want it showing)
    StatusLogger.getLogger().setLevel(Level.OFF);

    OntoPTRepository ontoPT = new OntoPTRepository();
    String word = "autor", anotherWord = "responsável";
    String[] relations = ontoPT.getRelationsBetweenElements(word, anotherWord);
    for (String relation : relations) {
      System.out.println(word.toUpperCase() + " : " + relation + " : "
          + anotherWord.toUpperCase());
    }
    System.out.println();
    System.out.println(word.toUpperCase() + " and " + anotherWord.toUpperCase()
    + " are in the same synset: " + String.valueOf(ontoPT.sameSynsetElements(
        word, anotherWord)).toUpperCase());
    System.out.println();

    // it would be better to rank the elements of the synset and define
    // a threshold (but this is just an example)
    System.out.println("synonyms of " + word.toUpperCase() + ":");
    String[] synonyms = ontoPT.getSynsetElements(word);
    for (String synonym : synonyms) {
      System.out.println("* " + synonym);
    }
    System.out.println();

    // the same recommendation as above
    System.out.println("hypernyms of " + word.toUpperCase() + ":");
    String[] hypernyms = ontoPT.getRelatedElements("hiperonimoDe", word);
    for (String hypernym : hypernyms) {
      System.out.println("* " + hypernym);
    }
    System.out.println();

    // the same recommendation as above
    System.out.println("actions said of " + word.toUpperCase() + ":");
    String[] actions = ontoPT.getRelatedElements("dizSeDoQue", word);
    for (String action : actions) {
      System.out.println("* " + action);
    }
    System.out.println();

    System.out.println("all relation types found in Onto.PT:");
    String[] relationTypes = ontoPT.getRelationTypes();
    for (String relationType : relationTypes) {
      System.out.println(": " + relationType + " :");
    }

    ontoPT.close();
  }
}
