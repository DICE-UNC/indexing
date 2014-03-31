package databook.listener;

import java.io.InputStream;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import databook.listener.Indexer;
import databook.listener.service.IndexingService;
import databook.local.model.RDFDatabase;
import databook.local.model.RDFServiceWrapper;

public class Activator implements BundleActivator {
	ServiceReference sr;
	Indexer i;
	RDFDatabase database;
	IndexingService ts;

	public void start(BundleContext context) throws Exception {
		System.out.println("indexing framework started");
	}
	
	public void stop(BundleContext context) throws Exception {
		System.out.println("indexing framework stopped");
	}

}
