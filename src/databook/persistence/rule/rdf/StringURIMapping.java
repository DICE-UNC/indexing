package databook.persistence.rule.rdf;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.logging.LogFactory;

import databook.edsl.googql.action;
import databook.utils.ModelUtils;

public class StringURIMapping implements StringObjectMapping<URI> {

	@Override
	public URI stringToObject(String val) {
		try {
			return new URI(val);
		} catch (URISyntaxException e) {
			LogFactory.getLog(StringURIMapping.class).error("error", e);
		}
		return null;
	}

	@Override
	public String objectToString(URI obj) {
		return obj.toString();
	}

	@Override
	public URI sparqlQueryResultStringToObject(String[] val) {
		return stringToObject(val[0]);
	}

	@Override
	public String objectToRdfString(URI obj) {
		return ModelUtils.bracket(objectToString(obj));
	}

	@Override
	public URI msgStringToObject(String val) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String objectToMsgString(URI obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public action query(action a) {
		return a.sel();
	}

}
