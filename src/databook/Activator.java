package databook;

import java.io.InputStream;

import org.irods.jargon.core.packinstr.InxVal;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

import databook.listener.Indexer;
import databook.listener.ModelUpdater;
import databook.listener.service.IndexingService;
import databook.listener.service.MessagingService;
import databook.local.model.RDFDatabase;
import databook.local.model.RDFServiceWrapper;

public class Activator implements BundleActivator {
	ServiceRegistration<MessagingService> sr;
	ServiceRegistration<IndexingService> inxSvc;
	Indexer i;
	RDFDatabase database;
	IndexingService ts;

	public void start(BundleContext context) throws Exception {
		/*ModelUpdater s = new ModelUpdater();
		if(sr == null) {
			sr = context.registerService(MessageHandler.class, s, null);
		}
		if(inxSvc == null) {
			inxSvc = context.registerService(IndexingService.class, s, null);
		}*/
		System.out.println("indexing framework started");
	}
	
	public void stop(BundleContext context) throws Exception {
		/*if(sr != null) {
			sr.unregister();
			sr = null;
		}
		
		if(inxSvc != null) {
			inxSvc.unregister();
			inxSvc = null;
		}*/

		System.out.println("indexing framework stopped");
		
	}

}
