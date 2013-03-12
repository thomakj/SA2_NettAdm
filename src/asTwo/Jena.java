package asTwo;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.shared.JenaException;

public class Jena {
	
	
	public static void main(String[] args) {
		OntModel m = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM, null);
		
		try {
			m.read("sematicweb-2013.owl");
		} catch (JenaException e) {
			System.out.println("ERROR");
			e.printStackTrace();
			System.exit(0);
		}
		
		
	}
		

}
