package examples;

import java.io.IOException;
import java.util.InvalidPropertiesFormatException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import split.SentenceSplitter;

public class SentenceSplitterExample {
  public static void main(String[] args)
      throws InvalidPropertiesFormatException, IOException,
      ParserConfigurationException, SAXException {
    SentenceSplitter splitter = new SentenceSplitter();
    String text = "Era uma vez um gato maltês, tocava piano e falava "
        + "francês. O rato roeu a rolha da garrafa de rum do Rei da "
        + "Rússia. A rápida raposa castanha salta sobre o cão preguiçoso.";
    String[] sentences = splitter.split(text);
    for (String sentence : sentences) {
      System.out.println(sentence);
    }
  }
}
