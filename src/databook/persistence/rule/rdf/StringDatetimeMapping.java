package databook.persistence.rule.rdf;

import java.text.ParseException;
import java.util.Date;

import databook.edsl.googql.action;
import databook.utils.ModelUtils;

public class StringDatetimeMapping implements StringObjectMapping<Date> {
	
	public String objectToString(Date obj) {
		return ModelUtils.toRDFDateFormat(obj);
	}
	
	@Override
	public Date stringToObject(String val) {
			return ModelUtils.parseTimeStr(val);
	}

	@Override
	public Date sparqlQueryResultStringToObject(String[] val) {
		return stringToObject(val[0]);
	}

	@Override
	public String objectToRdfString(Date obj) {
		return ModelUtils.databookDatetime(obj);
	}

	@Override
	public Date msgStringToObject(String val) {
		return stringToObject(val);
	}

	@Override
	public String objectToMsgString(Date obj) {
		return objectToString(obj);
	}

	public action query(action a) {
		return a.sel();
	}
}
