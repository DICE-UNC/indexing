package databook.persistence.rule.rdf;

import static databook.utils.ModelUtils.*;
import static databook.utils.ModelUtils.bracket;
import static databook.utils.ModelUtils.databookResource;
import static databook.utils.ModelUtils.databookStatement;
import static databook.utils.ModelUtils.sDiff;

import java.net.URI;
import java.util.Collection;

import databook.local.model.RDFDatabase.Format;
import databook.local.model.RDFDatabase.RDFDatabaseTransaction;
import databook.persistence.rule.ObjectPropertyRule;
import databook.persistence.rule.PersistenceContext;
import databook.persistence.rule.rdf.ruleset.RDFEntity;
import erilex.data.generic.Pair;

public class RDFAbstractCollectionPropertyRule<T extends RDFEntity, D> implements ObjectPropertyRule<T, Collection<D>> {
	
	protected StringObjectMapping<D> som;
	public RDFAbstractCollectionPropertyRule(StringObjectMapping<D> som, boolean cascadeP) {
		super();
		this.som = som;
		this.cascade = cascadeP;
	}
	
	public boolean cascade;
	@Override
	public void create(T e, String prop, Collection<D> c, PersistenceContext context) {
		if(c!=null)
			for(D o: c) {
			if(o != null) {
				if(cascade) {
					context.create(o);
				}
				URI uri = e.getUri();
				RDFDatabaseTransaction trans = context.getRdfTrans();
				trans.add(databookStatement(bracket(uri), databookResource(prop), som.objectToRdfString(o)),
						Format.N3, DATABOOK_MODEL_URI);
			}
		}
	}

	@Override
	public void delete(T e, String prop, Collection<D> c, PersistenceContext context) {
		if(c==null){
			// delete all properties instances
			c = context.<D>getPropertyObjects(e, prop, som);
		}
		for(D o:c) {
			if(o != null) {
				URI uri = e.getUri();
				RDFDatabaseTransaction trans = context.getRdfTrans();
				trans.remove(databookStatement(bracket(uri), databookResource(prop), som.objectToRdfString(o)),
						Format.N3, DATABOOK_MODEL_URI);
				if(cascade) {
					context.delete(o);
				}
			}
		}
	}

	@Override
	public void modify(T e, String prop, Collection<D> o0, Collection<D> o1, PersistenceContext context) {
		if(o1!=null) { 
			if(o0==null){
				o0 = context.<D>getPropertyObjects(e, prop, som);
			}
			Pair<Collection<D>, Collection<D>> ret = sDiff(o0, o1, som);
			delete(e, prop, ret.fst, context);
			create(e, prop, ret.snd, context);
		}
	}


	@Override
	public void union(T e, String prop, Collection<D> o0, Collection<D> o1, PersistenceContext context) {
		if(o1!=null) {
			if(o0==null){
				o0 = context.<D>getPropertyObjects(e, prop, som);
			}
			Pair<Collection<D>, Collection<D>> ret = sDiff(o0, o1, som);
			create(e, prop, ret.snd, context);
		}
	}

	@Override
	public void diff(T e, String prop, Collection<D> o0, Collection<D> o1, PersistenceContext context) {
		if(o1!=null) {
			if(o0==null){
				o0 = context.<D>getPropertyObjects(e, prop, som);
			}
			Pair<Collection<D>, Collection<D>> ret = sDiff(o0, o1, som);
			delete(e, prop, ret.snd, context);

		}
	}


}
