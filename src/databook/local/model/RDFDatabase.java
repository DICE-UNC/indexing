package databook.local.model;

import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import org.skife.csv.CSVReader;

import databook.edsl.googql.Dquery;
import databook.edsl.googql.actionHead;

public interface RDFDatabase {
	public enum Format { N3, RDF_XML }
	public interface RDFDatabaseTransaction {
		
		public void start() throws RDFDatabaseException;
		public void commit() throws RDFDatabaseException;
		public void abort();
		public void add(String rdf, Format format, String model);
		public void remove(String rdf, Format format, String model);

	}
	
	RDFDatabaseTransaction newTransaction();
	String getUniqueValue(String subject, String property);
	List<String> getValues(String subject, String property);
	InputStream describe(String subject, Format format) throws RDFDatabaseException;
	boolean exists(String subject);
	String getEntityTypeUri(String entityUri);
	actionHead<Dquery<Object, Object>, Dquery<Object, Object>, Object, Object> selectQuery();
}
