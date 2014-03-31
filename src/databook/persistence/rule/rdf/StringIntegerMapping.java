package databook.persistence.rule.rdf;

import databook.edsl.googql.action;
import databook.utils.ModelUtils;

public class StringIntegerMapping implements StringObjectMapping<Integer> {
	
	public String objectToString(Integer obj) {
		return obj.toString();
	}
	
	@Override
	public Integer stringToObject(String val) {
		return Integer.parseInt(val);
	}

	@Override
	public Integer sparqlQueryResultStringToObject(String[] val) {
		return stringToObject(val[0]);
	}

	@Override
	public String objectToRdfString(Integer obj) {
		return ModelUtils.databookInt(objectToString(obj));
	}
	
	@Override
	public action query(action a) {
		return a.sel();
	}

	@Override
	public Integer msgStringToObject(String val) {
		return stringToObject(val);
	}

	@Override
	public String objectToMsgString(Integer obj) {
		return objectToString(obj);
	}

}
