package databook.persistence.rule;

import databook.persistence.rule.rdf.ruleset.RDFEntity;

public class TranscientPropertyRule<T extends RDFEntity, D, PersCtx> implements ObjectPropertyRule<T, D, PersCtx> {

	@Override
	public void create(T e0, String prop, D e1, PersCtx context) {
		
	}

	@Override
	public void delete(T e0, String prop, D e1, PersCtx context) {
		
	}

	@Override
	public void modify(T e0, String prop, D e1, D e2, PersCtx context) {
		
	}

	@Override
	public void union(T e, String prop, D o0, D o1, PersCtx context) {
		
	}

	@Override
	public void diff(T e, String prop, D o0, D o1, PersCtx context) {
		
	}

}
