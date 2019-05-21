package examples;

import java.io.IOException;

import toponym.ToponymDictionary;
import toponym.ToponymDictionaryLoadException;

public class ToponymDictionaryExample {
  public static void main(String[] args)
      throws IOException, ToponymDictionaryLoadException {
    ToponymDictionary dict = new ToponymDictionary();

    String word = "Coimbra";
    String[] demonyms = dict.retrieveDemonyms(word);

    System.out.println("Inhabitants of <" + word + "> are called:");
    for (String demonym : demonyms) {
      System.out.println("* <" + demonym + ">");
    }
    System.out.println();

    word = "coimbrão";
    String[] toponyms = dict.retrieveToponyms(word);

    System.out.println("A(n) <" + word + "> comes from:");
    for (String toponym : toponyms) {
      System.out.println("* <" + toponym + ">");
    }
  }
}
