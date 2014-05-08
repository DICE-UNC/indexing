package databook.config;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class IrodsConfig {
	private static final String BUNDLE_NAME = "databook.config.irods"; //$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle(BUNDLE_NAME);

	private IrodsConfig() {
	}

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
	public static int getInt(String key) {
		try {
			return Integer.parseInt(RESOURCE_BUNDLE.getString(key)) ;
		} catch (MissingResourceException e) {
			return -1;
		}
	}
}
