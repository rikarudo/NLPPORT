package ontopt;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.MalformedQueryException;
import org.eclipse.rdf4j.query.QueryEvaluationException;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryException;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFParseException;
import org.eclipse.rdf4j.sail.memory.MemoryStore;

/**
 * This class ...
 *
 * @author   Ricardo Rodrigues
 * @version  0.9.9.9
 */
public class OntoPTRepository {
  private static final String DEFAULT_PROP =
      "resources/config/ontport.properties";

  private Repository repository = null;
  private RepositoryConnection connection = null;
  private String queryPrefix = null;
  private String prefix = null;
  private String synonymOfRelation = null;
  private String[] relationTypeExclusions = null;

  /**
   * Creates a new ...
   * 
   * @throws RDFParseException ...
   * @throws RepositoryException ...
   * @throws IOException ...
   */
  public OntoPTRepository()
      throws RDFParseException, RepositoryException, IOException {
    Properties properties = new Properties();
    properties.load(
        this.getClass().getClassLoader().getResourceAsStream(DEFAULT_PROP));
    this.prefix = properties.getProperty("ontoptPrefix");
    this.synonymOfRelation = properties.getProperty("synonymOfRelation");
    this.relationTypeExclusions = properties.getProperty(
        "relationTypeExclusions").split(";");
    repository = new SailRepository(new MemoryStore());
    repository.init();
    connection = repository.getConnection();
    if (connection.getNamespace(prefix) != null) {
      queryPrefix = new String("PREFIX " + prefix
          + ":<" + connection.getNamespace(prefix) + ">");
    }
    else {
      queryPrefix = new String();
    }
    // copy the OntoPT file from the resources to a temporary location
    // and then load it
    InputStream ontologyInput = 
        this.getClass().getClassLoader().getResourceAsStream(
            properties.getProperty("ontoptOntology"));
    File tempOntology = File.createTempFile(
        this.getClass().getName() + "$", ".rdfs");
    OutputStream ontologyOutput = new FileOutputStream(tempOntology);
    int length = 0;
    byte[] buffer = new byte[4096];
    while ((length = ontologyInput.read(buffer)) > 0) {
      ontologyOutput.write(buffer, 0, length);
    }
    ontologyInput.close();
    ontologyOutput.close();
    this.load(tempOntology, prefix);
  }

  /**
   * Creates a new ...
   * 
   * @param  ontologyFile ...
   * @param  prefix ...
   * @param  synonymOfRelation ...
   * @param  relationTypeExclusions ...
   * @throws RDFParseException ...
   * @throws RepositoryException ...
   * @throws IOException ...
   */
  public OntoPTRepository(File ontologyFile, String prefix,
      String synonymOfRelation, String[] relationTypeExclusions)
          throws RDFParseException, RepositoryException, IOException {
    repository = new SailRepository(new MemoryStore());
    repository.init();
    connection = repository.getConnection();
    if (connection.getNamespace(prefix) != null) {
      queryPrefix = new String("PREFIX " + prefix
          + ":<" + connection.getNamespace(prefix) + ">");
    }
    else {
      queryPrefix = new String();
    }

    this.load(ontologyFile, prefix);
  }


  /**
   * This method ...
   * 
   * @param  ontologyFile ...
   * @param  baseURI ...
   * @throws RDFParseException ...
   * @throws RepositoryException ...
   * @throws IOException ...
   */
  public void load(File ontologyFile, String baseURI)
      throws RDFParseException, RepositoryException, IOException {
    connection.add(ontologyFile, baseURI, RDFFormat.RDFXML);
    queryPrefix = new String("PREFIX " + prefix
        + ":<" + connection.getNamespace(prefix) + ">");
  }

  /**
   * This method ...
   * 
   * @param  query ...
   * @return ...
   * @throws RepositoryException ...
   * @throws MalformedQueryException ...
   * @throws QueryEvaluationException ...
   */
  public BindingSet[] query(String query)
      throws RepositoryException, MalformedQueryException,
      QueryEvaluationException {
    TupleQuery tQuery = connection.prepareTupleQuery(
        QueryLanguage.SPARQL, queryPrefix + "\n" + query);
    TupleQueryResult tqResult = tQuery.evaluate();
    ArrayList<BindingSet> bindingSets = new ArrayList<BindingSet>();
    while (tqResult.hasNext()) {
      bindingSets.add(tqResult.next());
    }
    return bindingSets.toArray(new BindingSet[bindingSets.size()]);
  }

  /**
   * This method ...
   * 
   * @throws RepositoryException ...
   */
  public void commit() throws RepositoryException {
    connection.commit();
  }

  /**
   * This method ...
   * 
   * @throws RepositoryException ...
   */
  public void close() throws RepositoryException {
    connection.close();
    repository.shutDown();
  }

  /**
   * This method ...
   * 
   * @return ...
   */
  public String getPrefix() {
    return this.prefix;
  }

  /**
   * This method ...
   * 
   * @param  lexicalForm ...
   * @return ...
   * @throws RepositoryException ...
   * @throws MalformedQueryException ...
   * @throws QueryEvaluationException ...
   */
  public String[] getSynsetElements(String lexicalForm)
      throws RepositoryException, MalformedQueryException,
      QueryEvaluationException {
    String query = "SELECT ?element\n"
        + "WHERE {\n"
        + "  ?synset " + prefix + ":formaLexical \"" + lexicalForm + "\" .\n"
        + "  ?synset " + prefix + ":formaLexical ?element .\n"
        + "}";
    BindingSet[] sets = this.query(query);
    ArrayList<String> synonyms = new ArrayList<String>();
    for (BindingSet set : sets) {
      synonyms.add(set.getValue("element").stringValue());
    }
    return synonyms.toArray(new String[synonyms.size()]);
  }

  /**
   * 
   * @param  lexicalFormA ...
   * @param  lexicalFormB ...
   * @return ...
   * @throws RepositoryException ...
   * @throws MalformedQueryException ...
   * @throws QueryEvaluationException ...
   */
  public boolean sameSynsetElements(String lexicalFormA, String lexicalFormB)
      throws RepositoryException, MalformedQueryException,
      QueryEvaluationException {
    ArrayList<String> elements = new ArrayList<String>(Arrays.asList(
        this.getSynsetElements(lexicalFormA)));
    if (elements.contains(lexicalFormB)) {
      return true;
    }
    else {
      return false;
    }
  }

  /**
   * This method ...
   * 
   * @param  relationName ...
   * @param  lexicalForm ...
   * @return ...
   * @throws RepositoryException ...
   * @throws MalformedQueryException ...
   * @throws QueryEvaluationException ...
   */
  public String[] getRelatedElements(String relationName, String lexicalForm)
      throws RepositoryException, MalformedQueryException,
      QueryEvaluationException {
    ArrayList<String> elements = new ArrayList<String>();
    if (relationName.equals(synonymOfRelation)) {
      String[] synsetElements = this.getSynsetElements(lexicalForm);
      for (String element : synsetElements) {
        elements.add(element);
      }
    }
    String query = "SELECT DISTINCT ?element\n"
        + "WHERE {\n"
        + "  ?synsetA " + prefix + ":formaLexical \"" + lexicalForm + "\" .\n"
        + "  ?synsetA " + prefix + ":" + relationName + " ?synsetB .\n"
        + "  ?synsetB " + prefix + ":formaLexical ?element .\n"
        + "}";
    BindingSet[] sets = this.query(query);
    for (BindingSet set : sets) {
      elements.add(set.getValue("element").stringValue());
    }
    return elements.toArray(new String[elements.size()]);
  }

  /**
   * 
   * @param  lexicalFormA ...
   * @param  lexicalFormB ...
   * @return ...
   * @throws RepositoryException ...
   * @throws MalformedQueryException ...
   * @throws QueryEvaluationException ...
   */
  public String[] getRelationsBetweenElements(String lexicalFormA,
      String lexicalFormB)
          throws RepositoryException, MalformedQueryException,
          QueryEvaluationException {
    ArrayList<String> relations = new ArrayList<String>();
    if (this.sameSynsetElements(lexicalFormA, lexicalFormB)) {
      relations.add(synonymOfRelation);
    }
    String query = "SELECT DISTINCT ?relation\n"
        + "WHERE {\n"
        + "  ?lexicalFormA " + prefix + ":formaLexical \"" + lexicalFormA
        + "\" .\n"
        + "  ?lexicalFormA ?relation ?lexicalFormB .\n"
        + "  ?lexicalFormB " + prefix + ":formaLexical \"" + lexicalFormB
        + "\" .\n"
        + "}";

    String relation = null;
    BindingSet[] sets = this.query(query);
    for (BindingSet set : sets) {
      relation = set.getValue("relation").stringValue().substring(
          set.getValue("relation").stringValue().indexOf("#") + 1);
      relations.add(relation);
    }
    return relations.toArray(new String[relations.size()]);
  }

  /**
   * This method ...
   * 
   * @return ...
   * @throws RepositoryException ...
   * @throws MalformedQueryException ...
   * @throws QueryEvaluationException ...
   */
  public String[] getRelationTypes()
      throws RepositoryException, MalformedQueryException,
      QueryEvaluationException {
    String query = "SELECT DISTINCT ?relation\n"
        + "WHERE {\n"
        + "  ?synsetA ?relation ?synsetB .\n"
        + "}";

    ArrayList<String> relations = new ArrayList<String>();
    relations.add(synonymOfRelation);
    ArrayList<String> relationExclusions = new ArrayList<String>(
        Arrays.asList(relationTypeExclusions));
    String relation = null;
    BindingSet[] sets = this.query(query);
    for (BindingSet set : sets) {
      relation = set.getValue("relation").stringValue().substring(
          set.getValue("relation").stringValue().indexOf("#") + 1);
      if (!relationExclusions.contains(relation)) {
        relations.add(relation);
      }
    }
    String[] relationTypes = relations.toArray(new String[relations.size()]);
    Arrays.sort(relationTypes);
    return relationTypes;
  }
}

/*
accaoNaoDaManeira
accaoPara
accaoQueCausa
accaoResultadoDe
antonimoAdjDe
antonimoAdvDe
antonimoNDe
antonimoVDe
causadorDaAccao
causadorDe
causadorDeAlgoComPropriedade
contem
contidoEm
contidoEmAlgoComPropriedade
definicao
devidoAEstado
devidoAQualidade
dizSeDoQue
dizSeSobre
estadoDe
estadoDoQueÉ
fazSeCom
fazSeComAlgoComPropriedade
feitoDe
feitoPeloQueÉ
finalidadeDaAccao
finalidadeDe
finalidadeDeAlgoComPropriedade
hiperonimoDe
hiponimoDe
inclui
incluiAlgoComPropriedade
localOrigemDe
maneiraComPropriedade
maneiraPorMeioDe
maneiraSem
maneiraSemAccao
materialDe
meioParaManeira
membroDe
membroDeAlgoComPropriedade
naoHaNaManeira
originarioDe
parteDe
parteDeAlgoComPropriedade
produtorDe
produtorDeAlgoComPropriedade
produzidoPor
produzidoPorAlgoComPropriedade
propriedadeDaManeira
propriedadeDeAlgoMembroDe
propriedadeDeAlgoParteDe
propriedadeDeAlgoProdutorDe
propriedadeDeAlgoProduzidoPor
propriedadeDeAlgoQueCausa
propriedadeDeAlgoQueContem
propriedadeDeAlgoQueInclui
propriedadeDeAlgoQueTemParte
propriedadeDeAlgoResultadoDe
propriedadeDoQueServePara
propriedadeDoQueServeParaAccao
qualidadeDe
qualidadeDoQueÉ
referidoPeloQueÉ
resultadoDaAccao
resultadoDe
resultadoDeAlgoComPropriedade
servePara
serveParaAccao
[sinonimoDe]
temComoParteAlgoComPropriedade
temEstado
temParte
temQualidade
 */
