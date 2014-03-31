package databook.listener;

import java.util.Collection;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.irods.jargon.core.connection.IRODSAccount;
import org.irods.jargon.core.exception.JargonException;
import org.irods.jargon.core.pub.IRODSFileSystem;
import org.irods.jargon.core.pub.io.IRODSFile;
import org.irods.jargon.core.pub.io.IRODSFileFactory;

import databook.persistence.rule.rdf.ruleset.DataEntity;
import databook.persistence.rule.rdf.ruleset.DataObject;
import databook.persistence.rule.rdf.ruleset.Message;

public class SimpleScheduler implements Scheduler {

	public static final int NUM_THREADS = 2;

	Executor executor = Executors.newFixedThreadPool(NUM_THREADS);
	IRODSFileSystem irodsFs;
	IRODSAccount irodsAccount;

	@Override
	public void submit(final Job j) {
		executor.execute(new Runnable() {

			@Override
			public void run() {
				try {
					Message m = j.obj;
					Collection<DataEntity> objs = m.getHasPart();
					if (m.getOperation().equals("retrieve")) {
						for (DataEntity obj : objs) {
							if (obj instanceof DataObject) {
								String path = obj.getLabel();
								if (path != null) {
									IRODSFileFactory ff = irodsFs
											.getIRODSFileFactory(irodsAccount);
									IRODSFile f = ff.instanceIRODSFile(path);
									IRODSFile rf = null;
									// TODO replicate file and store the new file handle to rf 

									j.success.call(rf);
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
