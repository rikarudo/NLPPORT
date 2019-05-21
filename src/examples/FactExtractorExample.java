package examples;

import java.io.IOException;
import java.util.HashSet;

import javax.xml.parsers.ParserConfigurationException;

import lemma.LemmatizeException;
import lemma.Lemmatizer;

import org.maltparser.core.exception.MaltChainedException;
import org.xml.sax.SAXException;

import pos.POSTagger;
import rank.WordRankingLoadException;
import token.Tokenizer;
import chunk.Chunk;
import chunk.Chunker;
import dependency.CoNLLToken;
import dependency.DependencyChunk;
import dependency.DependencyChunker;
import dependency.DependencyParser;
import dependency.DependencyParsingException;
import dictionary.DictionaryLoadException;
import entity.Entity;
import entity.EntityFinder;
import fact.FactExtractor;
import triple.Triple;

public class FactExtractorExample {
  public static void main(String[] args)
      throws LemmatizeException, DictionaryLoadException,
      WordRankingLoadException, MaltChainedException,
      DependencyParsingException {
    try {
      FactExtractor extractor = new FactExtractor();

      String sentence =
          "Mel_Blanc, o homem que deu a sua voz a o coelho mais famoso de o "
              + "mundo, Bugs_Bunny, era alérgico a cenouras.";

      Tokenizer tokenizer = new Tokenizer();
      POSTagger tagger = new POSTagger();
      Chunker chunker = new Chunker();
      EntityFinder finder = new EntityFinder();
      Lemmatizer lemmatizer = new Lemmatizer();
      DependencyParser parser = new DependencyParser();

      System.out.println("-------------------------------------------------");
      System.out.println("S: " + sentence);
      System.out.println("-------------------------------------------------");

      String[] tokens = tokenizer.tokenize(sentence, false);
      Entity[] entities = finder.find(tokens);
      tokens = tokenizer.tokenize(finder.bindEntityTokens(tokens), true);
      String[] tags = tagger.tag(tokens);
      String[] lemmas = lemmatizer.lemmatize(tokens, tags);
      Chunk[] chunks = chunker.chunkAsBlocks(tokens, tags, lemmas);
      CoNLLToken[] depTokens = parser.parseAsCoNLLToken(tokens, tags, lemmas);
      DependencyChunker dependency = new DependencyChunker(depTokens);
      DependencyChunk[] depChunks = dependency.parseRootChunks();

      HashSet<String> properNouns = new HashSet<String>();
      for (int i = 0; i < tokens.length; i++) {
        if (tags[i].equals("prop")) {
          properNouns.add(tokens[i]);
        }
      }

      Triple[] facts = extractor.extract(chunks, entities);
      for (Triple fact : facts) {
        System.out.println("A: " + fact);
        System.out.println("[A: " + finder.annotate((fact.getSubject()
            + " " + fact.getPredicate() + " "
            + fact.getObject()).split("\\s")) + "]");
      }
      System.out.println();

      facts = extractor.extract(depChunks, entities, true);
      for (Triple fact : facts) {
        System.out.println("B: " + fact);
        System.out.println("[B: " + finder.annotate((fact.getSubject()
            + " " + fact.getPredicate() + " "
            + fact.getObject()).split("\\s")) + "]");
      }
      System.out.println();

      facts = extractor.extract(chunks, properNouns.toArray(
          new String[properNouns.size()]));
      for (Triple fact : facts) {
        System.out.println("C: " + fact);
        System.out.println("[C: " + finder.annotate((fact.getSubject()
            + " " + fact.getPredicate() + " "
            + fact.getObject()).split("\\s")) + "]");
      }
      System.out.println();

      facts = extractor.extract(depChunks, properNouns.toArray(
          new String[properNouns.size()]), true);
      for (Triple fact : facts) {
        System.out.println("D: " + fact);
        System.out.println("[D: " + finder.annotate((fact.getSubject()
            + " " + fact.getPredicate() + " "
            + fact.getObject()).split("\\s")) + "]");
      }
      System.out.println();

      facts = extractor.extract(chunks);
      for (Triple fact : facts) {
        System.out.println("E: " + fact);
        System.out.println("[E: " + finder.annotate((fact.getSubject()
            + " " + fact.getPredicate() + " "
            + fact.getObject()).split("\\s")) + "]");
      }
      System.out.println();

      facts = extractor.extract(depChunks, true);
      for (Triple fact : facts) {
        System.out.println("F: " + fact);
        System.out.println("[F: " + finder.annotate((fact.getSubject()
            + " " + fact.getPredicate() + " "
            + fact.getObject()).split("\\s")) + "]");
      }
      System.out.println();
    }
    catch (IOException | ParserConfigurationException | SAXException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
