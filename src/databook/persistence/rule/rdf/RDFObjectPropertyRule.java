package databook.persistence.rule.rdf;

import databook.persistence.rule.rdf.ruleset.RDFEntity;

public class RDFObjectPropertyRule<T extends RDFEntity, D extends RDFEntity> extends RDFAbstractPropertyRule<T, D> {
	
	public RDFObjectPropertyRule(boolean cascade) {
		super(cascade, new StringRDFEntityMapping<D>());
	}


}
