package examples;

import dependency.DependencyParser;

public class DependencyParserExample {
  public static void main(String[] args) throws Exception {
    DependencyParser parser = new DependencyParser();

    String[] tokens = {"A", "rápida", "raposa", "castanha", "salta",
        "sobre", "o", "cão", "preguiçoso", "."};
    String[] tags = {"art", "adj", "n", "adj", "v-fin", "prp", "art",
        "n", "adj", "punc"};
    String[] lemmas = {"o", "rápido", "raposa", "castanho", "saltar",
        "sobre", "o", "cão", "preguiçoso", "."};

    String[] depTokens = parser.parseAsCoNLLString(tokens, tags, lemmas);

    for (String dependencyToken : depTokens) {
      System.out.println(dependencyToken);
    }
  }
}
