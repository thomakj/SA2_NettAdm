package asTwo;

import java.util.Iterator;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.ReasonerRegistry;
import com.hp.hpl.jena.shared.JenaException;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.util.PrintUtil;
import com.hp.hpl.jena.vocabulary.OWL;

public class Jena2 {
	
	public String[] objectArray = {"X5178","X3125","X4912","X5982","X1234","X6742"};
	
	public Model schema;
	public Reasoner reasoner;
	public InfModel m;
	
	public Jena2(){
		schema = FileManager.get().loadModel("sematicweb-2013.owl");
		reasoner = ReasonerRegistry.getOWLReasoner();
		m = createModel(reasoner, schema);
		runStatements(objectArray, m);
		
	}
	
	private void printStatements(InfModel m, Resource res, Property p, Resource o) {
		for(StmtIterator i = m.listStatements(res, p, o); i.hasNext();){
			Statement stmt = i.nextStatement();
			String snmpOid = stmt.getResource().getLocalName();
//			System.out.println(" - " + PrintUtil.print(stmt));
//          System.out.println(snmpOid); 
            
            if(isSnmpOid(snmpOid)){
            	sendSnmpReq(snmpOid);
            }
		}
	}
	
	private void sendSnmpReq(String snmpOid) {
//		System.out.println(snmpOid + " oid før opprettelse av exe");
		new ExecuteSnmp(snmpOid);
		
	}

	private void runStatements(String[] objectArray, InfModel m){
		for (int i = 0; i < objectArray.length; i++) {			
			String part = "http://www.item.ntnu.no/fag/ttm4128/sematicweb-2013#";
			String tot = part.concat(objectArray[i]);
			Resource res = m.getResource(tot);
			
			printStatements(m, res, OWL.equivalentClass, null);
		}
	}
	
	private boolean isSnmpOid(String snmpOid){
		if(snmpOid == null || snmpOid.startsWith("X")){
			return false;
		}
		return true;
	}
	
	public InfModel createModel(Reasoner reasoner, Model schema){
		reasoner.bindSchema(schema);
		InfModel im = ModelFactory.createInfModel(reasoner, schema);
		return im;		
	}
	
	public static void main(String[] args) {
		new Jena2();
		
		
	}

		

}
