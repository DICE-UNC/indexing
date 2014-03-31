package databook.persistence.rule.rdf;

import static databook.utils.ModelUtils.*;

import java.net.URI;
import java.util.*;

import databook.local.model.RDFDatabase.Format;
import databook.local.model.RDFDatabase.RDFDatabaseTransaction;
import databook.persistence.rule.ObjectPropertyRule;
import databook.persistence.rule.PersistenceContext;
import databook.persistence.rule.rdf.ruleset.DataEntity;
import databook.persistence.rule.rdf.ruleset.RDFEntity;
import erilex.data.generic.Pair;

public class RDFCollectionPropertyRule<T extends RDFEntity, D extends RDFEntity> extends RDFAbstractCollectionPropertyRule<T, D> {
	

	public RDFCollectionPropertyRule(boolean cascade) {
		this(new StringRDFEntityMapping<D>(), cascade);
	}
	
	public RDFCollectionPropertyRule(StringObjectMapping<D> som, boolean cascade) {
		super(som, cascade);
	}


}
