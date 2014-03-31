package databook.persistence.rule.rdf;

import java.util.List;

import databook.edsl.googql.Dquery;
import databook.edsl.googql.action;

public interface StringObjectMapping<T> {
	public T stringToObject(String val);
	public String objectToString(T obj);
	public action query(action a);
	public T sparqlQueryResultStringToObject(String[] val);
	public String objectToRdfString(T obj);
	public T msgStringToObject(String val);
	public String objectToMsgString(T obj);
	
}
