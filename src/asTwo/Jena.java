package asTwo;

import sun.org.mozilla.javascript.internal.regexp.SubString;

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
import com.hp.hpl.jena.util.PrintUtil;
import com.hp.hpl.jena.vocabulary.OWL;

public class Jena {

	public Jena() {
		InfModel infmodel = createModel();
		Resource res = createResource(infmodel);
		System.out.println(printStatements(infmodel, res, OWL.equivalentClass, null));
	}

	public String printStatements(Model model, Resource res, Property pro, Resource reso) {
		for (StmtIterator i = model.listStatements(res,pro,reso); i.hasNext(); ) {
			Statement stmt = i.nextStatement();
			if(PrintUtil.print(stmt).contains("snmp")) {
				return PrintUtil.print(stmt).substring(PrintUtil.print(stmt).indexOf("snmp"), PrintUtil.print(stmt).length()-1);
			} else {
				System.out.println(PrintUtil.print(stmt));
			}

		}
		return null;
	}

	private Resource createResource(InfModel infmodel) {
		//String[] cnmpObjects = {"X5178","X3125","X4912","X5982","X1234","X6742"};
		return infmodel.getResource("http://www.item.ntnu.no/fag/ttm4128/sematicweb-2013#X3125");
	}

	public InfModel createModel() {

		System.out.println("Starting application");

		Model schema = FileManager.get().loadModel("sematicweb-2013.owl");
		Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
		reasoner = reasoner.bindSchema(schema);
		InfModel infmodel = ModelFactory.createInfModel(reasoner, schema);

		return infmodel;
	}

	public static void main(String[] args) {
		new Jena();
	}

}
