package examples;

import java.io.IOException;
import java.util.InvalidPropertiesFormatException;

import pos.POSTagger;

public class PoSTaggerExample {
  public static void main(String[] args)  {
    POSTagger tagger = null;
    try {
      tagger = new POSTagger();
    }
    catch (InvalidPropertiesFormatException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    String[] tokens = {"Era", "uma", "vez", "um", "gato", "maltês", ",", 
        "tocava", "piano", "e", "falava", "francês", "."};
    String[] tags = tagger.tag(tokens);

    for (int i = 0; i < tokens.length; i++) {
      System.out.println(tokens[i] + "#" + tags[i]);
    }
  }
}
