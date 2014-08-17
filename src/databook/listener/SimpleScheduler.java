package databook.listener;

import java.net.URI;
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
import org.irods.jargon.core.pub.IRODSAccessObjectFactory;
import org.irods.jargon.core.pub.IRODSFileSystem;
import org.irods.jargon.core.pub.IRODSGenQueryExecutor;
import org.irods.jargon.core.pub.io.IRODSFile;
import org.irods.jargon.core.pub.io.IRODSFileFactory;
import org.irods.jargon.core.pub.io.IRODSFileInputStream;
import org.irods.jargon.core.pub.io.IRODSFileReader;
import org.irods.jargon.core.query.GenQueryBuilderException;
import org.irods.jargon.core.query.IRODSGenQueryBuilder;
import org.irods.jargon.core.query.IRODSGenQueryFromBuilder;
import org.irods.jargon.core.query.IRODSQueryResultRow;
import org.irods.jargon.core.query.IRODSQueryResultSetInterface;
import org.irods.jargon.core.query.JargonQueryException;
import org.irods.jargon.core.query.QueryConditionOperators;
import org.irods.jargon.core.query.RodsGenQueryEnum;

import databook.config.IrodsConfig;
import databook.persistence.rule.rdf.ruleset.DataEntity;
import databook.persistence.rule.rdf.ruleset.DataObject;
import databook.persistence.rule.rdf.ruleset.Message;

public class SimpleScheduler implements Scheduler {

	public static final int NUM_THREADS = IrodsConfig.getInt("numThreads");
	public static final boolean REQUIRE_CREDENTIALS = Boolean.parseBoolean(IrodsConfig.getString("requireCredentials"));

	static Log log = LogFactory.getLog("Scheduler"); //$NON-NLS-1$
	ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
	IRODSFileSystem irodsFs;
	IRODSAccount irodsAccount;

	public SimpleScheduler() {
		try {
			irodsFs = IRODSFileSystem.instance();
			irodsAccount = IRODSAccount
					.instance(
							IrodsConfig.getString("irods.host"), IrodsConfig.getInt("irods.port"), IrodsConfig.getString("irods.user"), IrodsConfig.getString("irods.password"), IrodsConfig.getString("irods.home"), IrodsConfig.getString("irods.zone"), IrodsConfig.getString("irods.defaultResource")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
		} catch (JargonException e) {
			log.error("error", e); //$NON-NLS-1$
		}
	}

	private String getPath(DataObject obj, IRODSAccount iaccount) {

		try {

			URI uri = obj.getUri();
			if (uri == null) {
				return obj.getLabel();
			}
			IRODSAccessObjectFactory accessObjectFactory;
			accessObjectFactory = irodsFs.getIRODSAccessObjectFactory();
			IRODSGenQueryExecutor irodsGenQueryExecutor = accessObjectFactory
					.getIRODSGenQueryExecutor(iaccount);

			IRODSGenQueryFromBuilder query = new IRODSGenQueryBuilder(false,
					null)
					.addSelectAsGenQueryValue(RodsGenQueryEnum.COL_DATA_NAME)
					.addSelectAsGenQueryValue(RodsGenQueryEnum.COL_COLL_NAME)
					.addConditionAsGenQueryField(
							RodsGenQueryEnum.COL_META_DATA_ATTR_NAME,
							QueryConditionOperators.EQUAL, "data:id")
					.addConditionAsGenQueryField(
							RodsGenQueryEnum.COL_META_DATA_ATTR_VALUE,
							QueryConditionOperators.EQUAL, uri.toString())
					.exportIRODSQueryFromBuilder(1);

			IRODSQueryResultSetInterface resultSet = irodsGenQueryExecutor
					.executeIRODSQuery(query, 0);
			IRODSQueryResultRow result = resultSet.getFirstResult();

			return result.getColumn(RodsGenQueryEnum.COL_COLL_NAME.getName())
					+ "/"
					+ result.getColumn(RodsGenQueryEnum.COL_DATA_NAME.getName());
		} catch (Exception e) {
			log.error("error", e);
		}
		return null;
	}

	private String getPath(databook.persistence.rule.rdf.ruleset.Collection obj, IRODSAccount iaccount) {

		try {

			URI uri = obj.getUri();
			if (uri == null) {
				return obj.getLabel();
			}
			IRODSAccessObjectFactory accessObjectFactory;
			accessObjectFactory = irodsFs.getIRODSAccessObjectFactory();
			IRODSGenQueryExecutor irodsGenQueryExecutor = accessObjectFactory
					.getIRODSGenQueryExecutor(iaccount);

			IRODSGenQueryFromBuilder query = new IRODSGenQueryBuilder(false,
					null)
					.addSelectAsGenQueryValue(RodsGenQueryEnum.COL_COLL_NAME)
					.addConditionAsGenQueryField(
							RodsGenQueryEnum.COL_META_DATA_ATTR_NAME,
							QueryConditionOperators.EQUAL, "data:id")
					.addConditionAsGenQueryField(
							RodsGenQueryEnum.COL_META_DATA_ATTR_VALUE,
							QueryConditionOperators.EQUAL, uri.toString())
					.exportIRODSQueryFromBuilder(1);

			IRODSQueryResultSetInterface resultSet = irodsGenQueryExecutor
					.executeIRODSQuery(query, 0);
			IRODSQueryResultRow result = resultSet.getFirstResult();

			return result.getColumn(RodsGenQueryEnum.COL_COLL_NAME.getName());
		} catch (Exception e) {
			log.error("error", e);
		}
		return null;
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
					IRODSAccount iaccount = getIRODSAccount(j);
					if (m.getOperation().equals("retrieve") || m.getOperation().equals("reader")) { //$NON-NLS-1$
						for (DataEntity obj : objs) {
							if (obj instanceof DataObject) {
								String path = getPath((DataObject) obj, iaccount);
								if (path != null) {
									// org.irods.jargon.core.pub.DataObjectAO
									// dao =
									// irodsFs.getIRODSAccessObjectFactory().getDataObjectAO(irodsAccount);
									// dao.replicateIrodsDataObject(path,
									// "indexerResource");
									IRODSFileFactory ff = irodsFs
											.getIRODSFileFactory(iaccount);
									IRODSFile f = ff.instanceIRODSFile(path);
									IRODSFileReader is = new IRODSFileReader(f,
											ff);

									// TODO replicate file and store the new
									// file handle to rf

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
					} else if (m.getOperation().equals("inputStream")) { //$NON-NLS-1$
						for (DataEntity obj : objs) {
							if (obj instanceof DataObject) {
								String path = getPath((DataObject) obj, iaccount);
								if (path != null) {
									// org.irods.jargon.core.pub.DataObjectAO
									// dao =
									// irodsFs.getIRODSAccessObjectFactory().getDataObjectAO(irodsAccount);
									// dao.replicateIrodsDataObject(path,
									// "indexerResource");
									IRODSFileFactory ff = irodsFs
											.getIRODSFileFactory(
													iaccount);
									IRODSFileInputStream is = ff
											.instanceIRODSFileInputStream(path);

									// TODO replicate file and store the new
									// file handle to rf

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
								String path = getPath((DataObject) obj, iaccount);
								if (path != null) {
									org.irods.jargon.core.pub.DataObjectAO dao = irodsFs
											.getIRODSAccessObjectFactory()
											.getDataObjectAO(iaccount);

									j.success.call(dao);
								} else {
									throw new RuntimeException(
											"No path provided for data object"); //$NON-NLS-1$

								}
							} else if (obj instanceof databook.persistence.rule.rdf.ruleset.Collection) {
									String path = getPath((databook.persistence.rule.rdf.ruleset.Collection) obj, iaccount);
									if (path != null) {
										org.irods.jargon.core.pub.CollectionAO dao = irodsFs
												.getIRODSAccessObjectFactory()
												.getCollectionAO(iaccount);

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
	
	private IRODSAccount getIRODSAccount(final Job j) {
		IRODSAccount iaccount;
		if(REQUIRE_CREDENTIALS) {
			iaccount = (IRODSAccount) j.cred;
			if(iaccount == null) {
				throw new java.lang.SecurityException("No credentials");
			}
		} else {
			iaccount = irodsAccount;
		}
		return iaccount;
	}
	
}
