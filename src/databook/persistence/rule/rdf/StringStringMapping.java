package databook.persistence.rule.rdf;

import java.util.List;

import databook.edsl.googql.Dquery;
import databook.edsl.googql.action;
import databook.utils.ModelUtils;

public class StringStringMapping implements StringObjectMapping<String> {
	
	public String objectToString(String obj) {
		return obj;
	}
	
	@Override
	public String stringToObject(String val) {
		return val;
	}

	@Override
	public String sparqlQueryResultStringToObject(String[] val) {
		return stringToObject(val[0]);
	}

	@Override
	public String objectToRdfString(String obj) {
		return ModelUtils.databookString(objectToString(obj));
	}

	@Override
	public String msgStringToObject(String val) {
		return val;
	}

	@Override
	public String objectToMsgString(String obj) {
		return obj;
	}
	@Override
	public action query(action a) {
		return a.sel();
	}

}
