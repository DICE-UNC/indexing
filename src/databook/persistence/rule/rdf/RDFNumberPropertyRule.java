package databook.persistence.rule.rdf;

import databook.persistence.rule.rdf.ruleset.RDFEntity;

public class RDFNumberPropertyRule<T extends RDFEntity> extends RDFAbstractPropertyRule<T, Double>{

	public RDFNumberPropertyRule() {
			super(new StringDoubleMapping());
	}

}
