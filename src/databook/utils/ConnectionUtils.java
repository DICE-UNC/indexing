package databook.utils;

import org.irods.jargon.core.connection.IRODSAccount;
import org.irods.jargon.core.exception.JargonException;
import org.irods.jargon.core.pub.IRODSFileSystem;

public class ConnectionUtils {
	public static final String VIVO_ADMIN_IRODS_USERNAME = "rods";
	public static final String VIVO_ADMIN_IRODS_PASSWORD = "rods";
	public static final String VIVO_ADMIN_IRODS_HOST = "localhost";
	public static final int VIVO_ADMIN_IRODS_PORT = 1247;
	public static final String VIVO_ADMIN_IRODS_HOME_DIRECTORY = "/databook/home/rods";
	public static final String VIVO_ADMIN_IRODS_ZONE = "databook";
	public static final String VIVO_ADMIN_IRODS_DEFAULT_RESOURCE = "demoResc";
	public static IRODSFileSystem irodsFs;

	static {
		try {
		  irodsFs = new IRODSFileSystem();
		} catch(JargonException e) {
		  throw new Error(e);
		}
	}
	
	public static IRODSAccount adminAccount() {
	  return
	      new IRODSAccount(VIVO_ADMIN_IRODS_HOST, VIVO_ADMIN_IRODS_PORT, VIVO_ADMIN_IRODS_USERNAME, VIVO_ADMIN_IRODS_PASSWORD, 
			       VIVO_ADMIN_IRODS_HOME_DIRECTORY, VIVO_ADMIN_IRODS_ZONE, VIVO_ADMIN_IRODS_DEFAULT_RESOURCE);
	}
}