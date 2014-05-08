package databook.config;

import java.util.ResourceBundle;

public class IrodsConfig {
	private static final String BUNDLE_NAME = "databook.config.irods"; //$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle(BUNDLE_NAME);

	private IrodsConfig() {
	}

	public static String getString(String key) {
			return RESOURCE_BUNDLE.getString(key);
	}
	public static int getInt(String key) {
			return Integer.parseInt(RESOURCE_BUNDLE.getString(key)) ;
	}
}
