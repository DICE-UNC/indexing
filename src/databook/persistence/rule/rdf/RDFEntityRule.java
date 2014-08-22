package databook.persistence.rule.rdf;

import static databook.utils.ModelUtils.DATABOOK_MODEL_URI;
import static databook.utils.ModelUtils.IS_A;
import static databook.utils.ModelUtils.*;
import static databook.utils.ModelUtils.databookStatement;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.net.URI;

import databook.local.model.RDFDatabase.Format;
import databook.local.model.RDFDatabase.RDFDatabaseTransaction;
import databook.persistence.rule.EntityRule;
import databook.persistence.rule.PersistenceContext;
import databook.persistence.rule.rdf.ruleset.RDFEntity;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class RDFEntityRule<T extends RDFEntity> implements EntityRule<T, PersistenceContext> {
	private static final Log log = LogFactory.getLog(RDFEntityRule.class);

// utilities
	public static void createAllProperties(RDFEntity e, PersistenceContext context) {
		try {
			// log.info("***creating object " + e);
			BeanInfo info = Introspector.getBeanInfo(e.getClass());
			for(PropertyDescriptor pd : info.getPropertyDescriptors()) {
				String prop = pd.getName();
				Object o = pd.getReadMethod().invoke(e);
				// log.info("***creating property " + prop + " = " + o);
				context.create(e, prop, o);

			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	public static void deleteAllProperties(RDFEntity e, PersistenceContext context) {
		try {
			BeanInfo info = Introspector.getBeanInfo(e.getClass());
			for(PropertyDescriptor pd : info.getPropertyDescriptors()) {
				String prop = pd.getName();
				// set obj to null to indicate delete all
				// Object o = pd.getReadMethod().invoke(e);
				context.delete(e, prop, null);

			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	public static boolean isPrimitiveType(Class type) {
		return type.equals(Integer.class) ||
				type.equals(Boolean.class) ||
				type.equals(Double.class) ||
				type.equals(String.class);
	}

	
	@Override
	public void create(T e, PersistenceContext context) {
		java.net.URI uri = e.getUri();
		RDFDatabaseTransaction trans = context.getRdfTrans();
		trans.add(databookStatement(bracket(uri), IS_A, getType(e)),
				Format.N3, DATABOOK_MODEL_URI);
		createAllProperties(e, context);
		
	}

	public String getType(T e) {
		URI uri = e.getTypeUri();
		return uri!=null?uri.toString():databookResource(e.getClass().getSimpleName());
	}

	@Override
	public void delete(T e, PersistenceContext context) {
		java.net.URI uri = e.getUri();
		deleteAllProperties(e, context);
		RDFDatabaseTransaction trans = context.getRdfTrans();
		trans.remove(databookStatement(bracket(uri), IS_A, getType(e)),
				Format.N3, DATABOOK_MODEL_URI);
	}

	@Override
	public void modify(T e0, T e1, PersistenceContext context) {
		try {
			BeanInfo info = Introspector.getBeanInfo(e0.getClass());
			for(PropertyDescriptor pd : info.getPropertyDescriptors()) {
				String prop = pd.getName();
				Object o0 = pd.getReadMethod().invoke(e0);
				Object o1 = pd.getReadMethod().invoke(e1);
				context.modify(e0, prop, o0, o1);
			}
		} catch (Exception e2) {
			e2.printStackTrace();
		}
	}

	@Override
	public void union(T e0, T e1, PersistenceContext context) {
		try {
			BeanInfo info = Introspector.getBeanInfo(e0.getClass());
			for(PropertyDescriptor pd : info.getPropertyDescriptors()) {
				String prop = pd.getName();
				Object o0 = pd.getReadMethod().invoke(e0);
				Object o1 = pd.getReadMethod().invoke(e1);
				Class type = pd.getPropertyType();
				context.union(e0, prop, o0, o1);
			}
		} catch (Exception e2) {
			e2.printStackTrace();
		}

	}


	@Override
	public void diff(T e0, T e1, PersistenceContext context) {
		try {
			BeanInfo info = Introspector.getBeanInfo(e0.getClass());
			for(PropertyDescriptor pd : info.getPropertyDescriptors()) {
				String prop = pd.getName();
				Object o0 = pd.getReadMethod().invoke(e0);
				Object o1 = pd.getReadMethod().invoke(e1);
				Class type = pd.getPropertyType();
				context.diff(e0, prop, o0, o1);
			}
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		
	}

}
