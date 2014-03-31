package databook.persistence.rule.rdf;

import java.text.NumberFormat;

import databook.edsl.googql.action;
import databook.utils.ModelUtils;

public class StringDoubleMapping implements StringObjectMapping<Double> {
	
	private NumberFormat nf;
	
	public StringDoubleMapping() {
		nf = NumberFormat.getNumberInstance();
		nf.setGroupingUsed(false);
	}
	
	public String objectToString(Double obj) {
		return nf.format(obj);
	}
	
	@Override
	public Double stringToObject(String val) {
		return Double.parseDouble(val);
	}

	@Override
	public Double sparqlQueryResultStringToObject(String[] val) {
		return stringToObject(val[0]);
	}

	@Override
	public String objectToRdfString(Double obj) {
		return ModelUtils.databookDecimal(objectToString(obj));
	}

	@Override
	public Double msgStringToObject(String val) {
		return stringToObject(val);
	}

	@Override
	public String objectToMsgString(Double obj) {
		return objectToString(obj);
	}

	@Override
	public action query(action a) {
		return a.sel();
	}

}
