package databook.persistence.rule.rdf;

import java.net.URI;
import java.util.List;

import org.apache.commons.logging.LogFactory;

import databook.edsl.googql.action;
import databook.persistence.rule.rdf.ruleset.RDFEntity;
import databook.utils.ModelUtils;
import static databook.utils.ModelUtils.*;

public final class StringRDFEntityMapping<T extends RDFEntity> implements
		StringObjectMapping<T> {

	public StringRDFEntityMapping() {
	}

	@Override
	public T stringToObject(String val) {
		return null;
	}

	@Override
	public String objectToString(T obj) {
		return obj.getUri().toString();
	}

	@Override
	public T sparqlQueryResultStringToObject(String[] val) {
		try {
			String uri = val[0];
			String typeuri = val[1];
			String pack = "databook.persistence.rule.rdf.ruleset.";
			String clas = pack+typeuri.substring(typeuri.indexOf("#")+1);
			Class<T> c;
			c = (Class<T>) Class.forName(clas);
			T e = c.newInstance();
			e.setUri(new URI(uri));
			e.setTypeUri(new URI(typeuri));
			return e;
		} catch (Exception e1) {
			LogFactory.getLog(this.getClass()).error("error", e1);
			return null;
		}
		
	}

	@Override
	public String objectToRdfString(T obj) {
		return ModelUtils.bracket(objectToString(obj));
	}

	@Override
	public T msgStringToObject(String val) {
		return stringToObject(ModelUtils.databookResourceNoBracket(val));
	}

	@Override
	public String objectToMsgString(T obj) {
		return ModelUtils.extractId(obj.getUri().toString());
	}
	
	public action query(action a) {
		return a.sel().follow(IS_A).sel();
	}
}