import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.ReasonerRegistry;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.vocabulary.OWL;

public class Jena2 {
	
//	public String[] objectArray = {"X5178","X3125","X4912","X5982","X1234","X6742"};
	
	public Model schema = FileManager.get().loadModel("sematicweb-2013.owl");
	public Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
	public InfModel m = createModel(reasoner, schema);
	
	
	public String getMibOid(String cnmpId){
		/*
		 * Generates a resource from OWL where the url+cnmpId is located and returns the snmpOid that corresponds to the cnmpId
		 * 
		 *  @param cnmpId	id that should be translated
		 *  @return 		String 
		 */
		String part = "http://www.item.ntnu.no/fag/ttm4128/sematicweb-2013#";
		String tot = part.concat(cnmpId.toUpperCase());
		Resource res = m.getResource(tot);
		
		return translateCnmpToSnmp(m, res, OWL.equivalentClass, null);
	}
	
	private String translateCnmpToSnmp(InfModel m, Resource res, Property p, Resource o) {
		/*
		 * Iterates over the Model to find snmpOid's and returns the snmpOid or null
		 * 
		 * @param m		is the Model
		 * @param res	is the resource constructed from OWL
		 * @param p		is the property that is wanted in the statement
		 * @return 		String
		 */
		for(StmtIterator i = m.listStatements(res, p, o); i.hasNext();){
			Statement stmt = i.nextStatement();
			String snmpOid = stmt.getResource().getLocalName();
            if(isSnmpOid(snmpOid)){
            	return snmpOid;
            }
		}
		return null;
	}
	
	private boolean isSnmpOid(String snmpOid){
		/*
		 * Checks if the input is a valid snmpOid and returns true or false
		 * 
		 * @param snmpOid	value to be checked
		 * @return			boolean
		 */
		if(snmpOid == null || snmpOid.startsWith("X")){
			return false;
		}
		return true;
	}
	
	public InfModel createModel(Reasoner reasoner, Model schema){
		/*
		 * Creates a InfModel based on a Reasoner and a Model and returns it 
		 * 
		 * @param reasoner	Model based on an OWL file
		 * @param schema	OWL reasoner
		 * @return 			InfModel
		 */
		reasoner.bindSchema(schema);
		InfModel im = ModelFactory.createInfModel(reasoner, schema);
		return im;		
	}
}
