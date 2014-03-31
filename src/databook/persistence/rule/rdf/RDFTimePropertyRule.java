package databook.persistence.rule.rdf;

import static databook.utils.ModelUtils.*;
import databook.local.model.RDFDatabase.Format;
import databook.local.model.RDFDatabase.RDFDatabaseTransaction;
import databook.persistence.rule.ObjectPropertyRule;
import databook.persistence.rule.PersistenceContext;
import databook.persistence.rule.rdf.ruleset.RDFEntity;

import java.net.URI;
import java.util.Collection;
import java.util.Date;

public class RDFTimePropertyRule<T extends RDFEntity> extends RDFAbstractPropertyRule<T, Date>{

	public RDFTimePropertyRule() {
		super(new StringDatetimeMapping());
	}
	
}
