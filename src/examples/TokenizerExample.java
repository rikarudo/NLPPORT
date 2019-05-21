package examples;

import java.io.IOException;
import java.util.InvalidPropertiesFormatException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import token.Tokenizer;

public class TokenizerExample {
  public static void main(String[] args) {
    Tokenizer tokenizer = null;
    try {
      tokenizer = new Tokenizer();
    }
    catch (InvalidPropertiesFormatException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    catch (ParserConfigurationException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    catch (SAXException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    String sentence = "Era uma vez um gato maltês, tocava piano e falava"
        + " francês.";
    String[] tokens = tokenizer.tokenize(sentence, true);

    for (int i = 0; i < tokens.length; i++) {
      System.out.println(tokens[i]);
    }
  }
}
