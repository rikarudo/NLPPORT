package examples;

import dependency.CoNLLToken;
import dependency.DependencyChunk;
import dependency.DependencyChunker;
import dependency.DependencyParser;

public class DependencyChunkerExample {
  public static void main(String[] args) throws Exception {
    DependencyParser parser = new DependencyParser();

    String[] tokens = {"A", "rápida", "raposa", "castanha", "salta",
        "sobre", "o", "cão", "preguiçoso", "."};
    String[] tags = {"art", "adj", "n", "adj", "v-fin", "prp", "art",
        "n", "adj", "punc"};
    String[] lemmas = {"o", "rápido", "raposa", "castanho", "saltar",
        "sobre", "o", "cão", "preguiçoso", "."};

    System.out.println("DEPENDENCY TOKENS:");
    CoNLLToken[] depTokens = parser.parseAsCoNLLToken(tokens, tags,
        lemmas);
    for (CoNLLToken depToken : depTokens) {
      System.out.println(depToken);
    }
    System.out.println();

    System.out.println("DEPENDENCY TOKENS BY GROUP:");
    DependencyChunker depChunker = new DependencyChunker(depTokens);
    CoNLLToken[] conllTokens = depChunker.retrieveRootDependants(
        true, true);
    for (CoNLLToken conllToken : conllTokens) {
      CoNLLToken[] depTokensByGroup = null;
      if (conllToken.getDependencyRelation().equals("ROOT")) {
        depTokensByGroup = depChunker.retrieveDependants(
            conllToken, true, true);
      }
      else {
        depTokensByGroup = depChunker.retrieveDependants(
            conllToken, true, false);
      }
      System.out.println("{");
      for (CoNLLToken depToken : depTokensByGroup) {
        System.out.println("  " + depToken);
      }
      System.out.println("}");
    }
    System.out.println();

    System.out.println("DEPENDENCY CHUNKS:");
    DependencyChunk[] depChunks = depChunker.parseRootChunks();
    for (DependencyChunk depChunk : depChunks) {
      System.out.println(depChunk);
    }
  }
}
