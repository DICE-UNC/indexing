package databook.listener;

import java.util.Collection;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.irods.jargon.core.connection.IRODSAccount;
import org.irods.jargon.core.exception.JargonException;
import org.irods.jargon.core.pub.DataObjectAO;
import org.irods.jargon.core.pub.IRODSFileSystem;
import org.irods.jargon.core.pub.io.IRODSFile;
import org.irods.jargon.core.pub.io.IRODSFileFactory;
import org.irods.jargon.core.pub.io.IRODSFileInputStream;
import org.irods.jargon.core.pub.io.IRODSFileReader;

import databook.persistence.rule.rdf.ruleset.DataEntity;
import databook.persistence.rule.rdf.ruleset.DataObject;
import databook.persistence.rule.rdf.ruleset.Message;

public class SimpleScheduler implements Scheduler {

	public static final int NUM_THREADS = 2;

	static Log log = LogFactory.getLog("Scheduler");
	ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
	IRODSFileSystem irodsFs;
	IRODSAccount irodsAccount;
	
	public SimpleScheduler () {
		try {
			irodsFs = IRODSFileSystem.instance();
			irodsAccount=IRODSAccount.instance("localhost", 1247, "rods", "rods", "/databook/home/rods","databook", "demoResc");
		} catch (JargonException e) {
			log.error("error", e);
		}
	}

	@Override
	public void submit(final Job j) {
		executor.execute(new Runnable() {

			@Override
			public void run() {
				try {
					System.out.println("run job " + j);
					Message m = j.obj;
					Collection<DataEntity> objs = m.getHasPart();
					if (m.getOperation().equals("retrieve")) {
						for (DataEntity obj : objs) {
							if (obj instanceof DataObject) {
								String path = obj.getLabel();
								if (path != null) {
									//org.irods.jargon.core.pub.DataObjectAO dao = irodsFs.getIRODSAccessObjectFactory().getDataObjectAO(irodsAccount);
									//dao.replicateIrodsDataObject(path, "indexerResource");
									IRODSFileFactory ff = irodsFs
											.getIRODSFileFactory(irodsAccount);
									IRODSFile f = ff.instanceIRODSFile(path);
									IRODSFileReader is = new IRODSFileReader(f, ff);
									
									// TODO replicate file and store the new file handle to rf 

									j.success.call(is);
								} else {
									throw new RuntimeException(
											"No path provided for data object");

								}
							} else {
								throw new RuntimeException(
										"Unsupported data entity type "
												+ obj.getClass());

							}
						}
					} else {
						throw new RuntimeException("Unsupported operation "
								+ m.getOperation());
					}
				} catch (Exception e) {
					j.fail.call(e);
				}

			}
		});

	}

}
