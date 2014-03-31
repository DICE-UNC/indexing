package databook.local.model;

import java.io.InputStream;


public interface RDFServiceWrapper {

	InputStream sparqlSelectQuery(String anyString) throws Exception;

	Object getRdfServiceImpl();

}
