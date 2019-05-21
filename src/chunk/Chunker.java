package chunk;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;
import opennlp.tools.util.InvalidFormatException;
import opennlp.tools.util.Span;

/**
 * This class ...
 *
 * @author   Ricardo Rodrigues
 * @version  0.9.9.9
 */
public class Chunker {
  private static final String DEFAULT_PROP =
      "resources/config/nlpport.properties";

  private ChunkerME chunker = null;

  /**
   * Creates a new ...
   * 
   * @throws InvalidPropertiesFormatException ...
   * @throws IOException ...
   */
  public Chunker() throws InvalidPropertiesFormatException, IOException {
    Properties properties = new Properties();
    properties.load(
        this.getClass().getClassLoader().getResourceAsStream(DEFAULT_PROP));
    this.chunker = new ChunkerME(new ChunkerModel(
        this.getClass().getClassLoader().getResourceAsStream(
            properties.getProperty("chunker"))));
  }

  /**
   * Creates a new ...
   * 
   * @param  chunkerModel ...
   * @throws InvalidFormatException ...
   * @throws IOException ...
   */
  public Chunker(InputStream chunkerModel)
      throws InvalidFormatException, IOException {
    this.chunker = new ChunkerME(new ChunkerModel(chunkerModel));
  }

  /**
   * This method ...
   * 
   * @param  tokens ...
   * @param  tags ...
   * @return ...
   */
  public String[] chunk(String[] tokens, String[] tags) {
    return chunker.chunk(tokens, tags);
  }

  /**
   * This method ...
   * 
   * @param  tokens ...
   * @param  tags ...
   * @param  lemmas ...
   * @return ...
   */
  public Chunk[] chunkAsBlocks(String[] tokens, String[] tags,
      String[] lemmas) {
    Span[] spans = chunker.chunkAsSpans(tokens, tags);
    ArrayList<Chunk> chunks = new ArrayList<Chunk>();
    for (Span span: spans) {
      String type = span.getType();
      String[] spanTokens = new String[span.getEnd() - span.getStart()];
      String[] spanTags = new String[span.getEnd() - span.getStart()];
      String[] spanLemmas = new String[span.getEnd() - span.getStart()];
      for (int i = 0; i < spanTokens.length; i++) {
        spanTokens[i] = tokens[i + span.getStart()];
        spanTags[i] = tags[i + span.getStart()];
        spanLemmas[i] = lemmas[i + span.getStart()];
      }
      chunks.add(new Chunk(type, spanTokens, spanTags, spanLemmas));
    }
    return chunks.toArray(new Chunk[chunks.size()]);
  }
}
