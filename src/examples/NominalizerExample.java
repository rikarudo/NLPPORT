package examples;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;

import javax.xml.parsers.ParserConfigurationException;

import lexicon.LexiconLoadException;

import org.xml.sax.SAXException;

import nominalization.Nominalizer;

public class NominalizerExample {

  public static void main(String[] args)
      throws InvalidPropertiesFormatException, IOException,
      LexiconLoadException, ParserConfigurationException, SAXException {

    BufferedReader verbFile = new BufferedReader(new FileReader(new File(
        "src/resources/verbs/verblist.txt")));
    ArrayList<String> contents = new ArrayList<String>();
    String line = null;
    while ((line = verbFile.readLine()) != null) {
      if (line.length() > 0) {
        contents.add(line);
      }
    }
    verbFile.close();

    String[] verbs = contents.toArray(new String[contents.size()]);
    Nominalizer nominalizer = new Nominalizer();
    String[] nouns = null;
    for (String verb : verbs) {
      nouns = nominalizer.nominalize(verb, "v");
      System.out.println(verb);
      for (String noun : nouns) {
        if (noun.endsWith("or") || noun.endsWith("ante")
            || noun.endsWith("ente") || noun.endsWith("inte")) {
          System.out.println("\t" + noun + " [agente]");
        }
        else {
          System.out.println("\t" + noun);
        }
      }
      System.out.println();
    }

    System.out.println();
    System.out.println("****");
    System.out.println();

    String verb = "agir";
    nouns = nominalizer.nominalize(verb, "v");
    System.out.println(verb);
    for (String noun : nouns) {
      System.out.println("\t" + noun);
    }
  }
}
