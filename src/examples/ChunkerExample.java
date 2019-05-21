package examples;

import java.io.IOException;
import java.util.InvalidPropertiesFormatException;

import chunk.Chunk;
import chunk.Chunker;

public class ChunkerExample {
  public static void main(String[] args) {
    Chunker chunker = null;
    try {
      chunker = new Chunker();
    }
    catch (InvalidPropertiesFormatException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    String[] tokens = {"O", "rato", "roeu", "a", "rolha", "de", "a", "garrafa",
        "de", "rum", "de", "o", "Rei_da_Rússia", "."};
    String[] tags = {"art", "n", "v-fin", "art", "n", "prp", "art", "n", "prp",
        "n", "prp", "art", "prop", "punc"};
    String[] lemmas = {"o", "rato", "roer", "o", "rolha", "de", "o", "garrafa",
        "de", "rum", "de", "o", "rei_da_rússia", "."};
    String[] chunks = chunker.chunk(tokens, tags);

    for (int i = 0; i < chunks.length; i++) {
      System.out.println(tokens[i] + "\t" + tags[i] + "\t" + chunks[i]);
    }
    System.out.println();


    Chunk[] blocks = chunker.chunkAsBlocks(tokens, tags, lemmas);
    for (Chunk block : blocks) {
      System.out.println(block);
    }
    System.out.println();
  }
}
