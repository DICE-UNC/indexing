package databook.persistence.rule.rdf;

import static databook.utils.ModelUtils.*;
import static databook.utils.ModelUtils.DATABOOK_RESOURCE_URI_PREFIX;
import static databook.utils.ModelUtils.LABEL_MODEL_URI;
import static databook.utils.ModelUtils.RDFS_URI_PREFIX;
import static databook.utils.ModelUtils.bracket;
import static databook.utils.ModelUtils.databookStatement;
import databook.local.model.RDFDatabase.Format;
import databook.local.model.RDFDatabase.RDFDatabaseTransaction;
import databook.persistence.rule.ObjectPropertyRule;
import databook.persistence.rule.PersistenceContext;
import databook.persistence.rule.rdf.ruleset.RDFEntity;

public class RDFAbstractPropertyRule<T extends RDFEntity, D>
		implements ObjectPropertyRule<T, D, PersistenceContext> {

	public boolean cascade;
	public StringObjectMapping<D> som;

	public static PropertyModelMapper PROP_MODEL = new PropertyModelMapper() {

		public String getPropURI(String prop) {
			String prefix;
			if (prop.equals("label")) {
				prefix = RDFS_URI_PREFIX;
			} else {
				prefix = DATABOOK_RESOURCE_URI_PREFIX;
			}
			return prefix + prop;
		}

		public String getPropModelURI(String prop) {
			String modelUri;
			if (prop.equals("label")) {
				modelUri = LABEL_MODEL_URI;
			} else {
				modelUri = DATABOOK_MODEL_URI;
			}
			return modelUri;

		}
	};

	public RDFAbstractPropertyRule(StringObjectMapping<D> som) {
		this(false, som);
	}

	public RDFAbstractPropertyRule(boolean cascade, StringObjectMapping<D> som) {
		super();
		this.cascade = cascade;
		this.som = som;
	}

	@Override
	public void create(T e, String prop, D i, PersistenceContext context) {
		if (i != null) {
			if (cascade) {
				context.create(i);
			}
			java.net.URI uri = e.getUri();
			RDFDatabaseTransaction trans = context.getRdfTrans();
			trans.add(
					databookStatement(bracket(uri),
							bracket(PROP_MODEL.getPropURI(prop)),
							getDatabookString(i)), 
					Format.N3, PROP_MODEL.getPropModelURI(prop));
		}

	}

	@Override
	public void delete(T e, String prop, D i, PersistenceContext context) {
		if (i == null) {
			i = getUniqueValue(e, prop, context);
			if (i == null) {
				return;
			}
		}
		java.net.URI uri = e.getUri();
		RDFDatabaseTransaction trans = context.getRdfTrans();
		trans.remove(
				databookStatement(bracket(uri),
						bracket(PROP_MODEL.getPropURI(prop)),
						getDatabookString(i)),
				Format.N3, PROP_MODEL.getPropModelURI(prop));

		if (cascade) {
			context.delete(i);
		}
	}

	protected D getUniqueValue(T e, String prop,
			PersistenceContext context) {
		return getUnique(context.getPropertyObjects(e, prop, som));
	}

	protected String getDatabookString(D i) {
		return som.objectToRdfString(i);
	}

	@Override
	public void modify(T e, String prop, D i0, D i1, PersistenceContext context) {
		if(i1!=null) {if (i0 == null) {
			i0 = getUniqueValue(e, prop, context);
		}
		if (i0 != null) {
			delete(e, prop, i0, context);
		}
		create(e, prop, i1, context);
		}
	}

	@Override
	public void union(T e, String prop, D o0, D o1, PersistenceContext context) {
		if(o1 != null) {
			if (o0 == null) {
				o0 = getUniqueValue(e, prop, context);
			}
			if (o0 != null) {
				throw new RuntimeException("union of two "
						+ o0.getClass().getName() + " " + o0 + ", " + o1 + " is not supported");
			}
			create(e, prop, o1, context);
		}
	}

	@Override
	public void diff(T e, String prop, D o0, D o1, PersistenceContext context) {
		if (o1 != null) {
			if (o0 == null) {
				o0 = getUniqueValue(e, prop, context);
			} 
			if(o0 == null) {
				throw new RuntimeException("diff of null value and a non-null value for property "
						+ prop + " is not supported");
			}
			if (!o0.equals(o1)) {
				throw new RuntimeException("diff of two different "
						+ o0.getClass().getName() + " is not supported");
			}
			delete(e, prop, o0, context);
		}

	}

}