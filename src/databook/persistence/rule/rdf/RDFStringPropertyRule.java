package databook.persistence.rule.rdf;

import static databook.utils.ModelUtils.*;
import databook.persistence.rule.ObjectPropertyRule;
import databook.persistence.rule.PersistenceContext;
import databook.persistence.rule.PersistenceException;
import databook.persistence.rule.rdf.ruleset.RDFEntity;

public class RDFStringPropertyRule<T extends RDFEntity> extends RDFAbstractPropertyRule<T, String>{

	public RDFStringPropertyRule() {
		super(new StringStringMapping());
	}
	
}
