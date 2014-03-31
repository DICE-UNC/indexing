package databook.persistence.rule.rdf;

import static databook.utils.ModelUtils.*;

import java.net.URI;

import databook.local.model.RDFDatabase.Format;
import databook.local.model.RDFDatabase.RDFDatabaseTransaction;
import databook.persistence.rule.ObjectPropertyRule;
import databook.persistence.rule.PersistenceContext;
import databook.persistence.rule.PersistenceException;
import databook.persistence.rule.rdf.ruleset.RDFEntity;

public class RDFURIPropertyRule<T extends RDFEntity> extends RDFAbstractPropertyRule<T, URI>{

	public RDFURIPropertyRule() {
		super(new StringURIMapping());
	}
	
}
