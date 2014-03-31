package databook.persistence.rule.rdf;

import static databook.utils.ModelUtils.*;

import java.net.URI;

import databook.local.model.RDFDatabase.Format;
import databook.local.model.RDFDatabase.RDFDatabaseTransaction;
import databook.persistence.rule.ObjectPropertyRule;
import databook.persistence.rule.PersistenceContext;
import databook.persistence.rule.rdf.ruleset.RDFEntity;

public class RDFIntegerPropertyRule<T extends RDFEntity> extends RDFAbstractPropertyRule<T, Integer>{

	public RDFIntegerPropertyRule() {
		super(new StringIntegerMapping());
	}

//	@Override
//	public void union(T e, String prop, Integer o0, Integer o1,
//			PersistenceContext context) {
//		modify(e, prop, o0, o0+o1, context);
//	}
//
//	@Override
//	public void diff(T e, String prop, Integer o0, Integer o1,
//			PersistenceContext context) {
//		modify(e, prop, o0, o0-o1, context);
//	}

}
