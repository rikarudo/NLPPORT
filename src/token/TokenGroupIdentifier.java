package token;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;

import replacement.Replacement;

import org.xml.sax.SAXException;

/**
 * This class ...
 *
 * @author   Ricardo Rodrigues
 * @version  0.9.9.9
 */
public class TokenGroupIdentifier {
  private static final String DEFAULT_PROP =
      "resources/config/nlpport.properties";

  private String[] groups = null;

  /**
   * Creates a new <code>TokenGroupIdentifier</code> object ...
   * 
   * @throws InvalidPropertiesFormatException ...
   * @throws IOException ...
   * @throws ParserConfigurationException ...
   * @throws SAXException ...
   */
  public TokenGroupIdentifier()
      throws InvalidPropertiesFormatException, IOException,
      ParserConfigurationException, SAXException {
    Properties properties = new Properties();
    properties.load(
        this.getClass().getClassLoader().getResourceAsStream(DEFAULT_PROP));
    Replacement[] tokenGroups = Replacement.readReplacements(
        this.getClass().getClassLoader().getResourceAsStream(
            properties.getProperty("tokenGroups")));
    HashSet<String> tokenGroupSet = new HashSet<String>();
    for (Replacement tokenGroup:tokenGroups) {
      tokenGroupSet.add(tokenGroup.getTarget());
    }
    groups = tokenGroupSet.toArray(new String[tokenGroupSet.size()]);
    Arrays.sort(groups);
  }

  /**
   * Creates a new <code>TokenGroupIdentifier</code> object ...
   * 
   * @param  tokenGroupData ...
   * @throws ParserConfigurationException ...
   * @throws SAXException ...
   * @throws IOException ...
   */
  public TokenGroupIdentifier(InputStream tokenGroupData)
      throws ParserConfigurationException, SAXException, IOException {
    Replacement[] tokenGroups = Replacement.readReplacements(tokenGroupData);
    HashSet<String> tokenGroupSet = new HashSet<String>();
    for (Replacement tokenGroup:tokenGroups) {
      tokenGroupSet.add(tokenGroup.getTarget());
    }
    groups = tokenGroupSet.toArray(new String[tokenGroupSet.size()]);
    Arrays.sort(groups);
  }

  /**
   * This method ...
   * 
   * @param  tokens ...
   * @return ...
   */
  public String[] group(String[] tokens) {
    StringBuffer tokenBuffer = new StringBuffer();
    for (String token : tokens) {
      tokenBuffer.append(token + " ");
    }
    String groupedTokens = tokenBuffer.toString().trim();
    ArrayList<String> matches = new ArrayList<String>();
    Matcher matcher = null;
    for (String pattern : groups) {
      matcher = Pattern.compile(pattern).matcher(groupedTokens);
      while (matcher.find()) {
        if (!matches.contains(escapeMetacharacters(matcher.group()))) {
          matches.add(escapeMetacharacters(matcher.group()));
        }
      }
    }
    for (String match : matches) {
      groupedTokens = groupedTokens.replaceAll("(\\s|^)" + match + "(\\s|$)",
          " " + match.replace(" ", "_") + " ").trim();
    }
    return groupedTokens.split("\\s");
  }

  private String escapeMetacharacters(String text) {
    return text.replace("\\", "\\\\").replace("^", "\\^").replace(
        "$", "\\$").replace(".", "\\.").replace("|", "\\|").replace(
            "?", "\\?").replace("*", "\\*").replace("+", "\\+").replace(
                "(", "\\(").replace(")", "\\)").replace("[", "\\[").replace(
                    "]", "\\]");
  }
}
