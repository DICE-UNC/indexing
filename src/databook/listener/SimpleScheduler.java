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

import databook.config.IrodsConfig;
import databook.persistence.rule.rdf.ruleset.DataEntity;
import databook.persistence.rule.rdf.ruleset.DataObject;
import databook.persistence.rule.rdf.ruleset.Message;

public class SimpleScheduler implements Scheduler {

	public static final int NUM_THREADS = 2;

	static Log log = LogFactory.getLog("Scheduler"); //$NON-NLS-1$
	ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
	IRODSFileSystem irodsFs;
	IRODSAccount irodsAccount;
	
	public SimpleScheduler () {
		try {
			irodsFs = IRODSFileSystem.instance();
			irodsAccount=IRODSAccount.instance(IrodsConfig.getString("irods.host"), 1247, IrodsConfig.getString("irods.user"), IrodsConfig.getString("irods.password"), IrodsConfig.getString("irods.home"),IrodsConfig.getString("irods.zone"), IrodsConfig.getString("irods.defaultResource")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
		} catch (JargonException e) {
			log.error("error", e); //$NON-NLS-1$
		}
	}

	@Override
	public void submit(final Job j) {
		executor.execute(new Runnable() {

			@Override
			public void run() {
				try {
					System.out.println("run job " + j); //$NON-NLS-1$
					Message m = j.obj;
					Collection<DataEntity> objs = m.getHasPart();
					if (m.getOperation().equals("retrieve")) { //$NON-NLS-1$
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
											"No path provided for data object"); //$NON-NLS-1$

								}
							} else {
								throw new RuntimeException(
										"Unsupported data entity type " //$NON-NLS-1$
												+ obj.getClass());

							}
						}
					} else if (m.getOperation().equals("accessObject")) { //$NON-NLS-1$
						for (DataEntity obj : objs) {
							if (obj instanceof DataObject) {
								String path = obj.getLabel();
								if (path != null) {
									org.irods.jargon.core.pub.DataObjectAO dao = irodsFs.getIRODSAccessObjectFactory().getDataObjectAO(irodsAccount);

									j.success.call(dao);
								} else {
									throw new RuntimeException(
											"No path provided for data object"); //$NON-NLS-1$

								}
							} else {
								throw new RuntimeException(
										"Unsupported data entity type " //$NON-NLS-1$
												+ obj.getClass());

							}
						}
						
						
					} else {
						throw new RuntimeException("Unsupported operation " //$NON-NLS-1$
								+ m.getOperation());
					}
				} catch (Exception e) {
					j.fail.call(e);
				}

			}
		});

	}

}
