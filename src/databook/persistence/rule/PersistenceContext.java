package databook.persistence.rule;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.irods.jargon.core.connection.IRODSAccount;
import org.irods.jargon.core.pub.IRODSFileSystem;

import databook.edsl.googql.Dquery;
import databook.edsl.googql.action;
import databook.local.model.RDFDatabase;
import databook.local.model.RDFDatabase.RDFDatabaseTransaction;
import databook.local.model.RDFDatabaseException;
import databook.persistence.rule.rdf.RDFAbstractPropertyRule;
import databook.persistence.rule.rdf.StringObjectMapping;
import databook.persistence.rule.rdf.ruleset.AVU;
import databook.persistence.rule.rdf.ruleset.DataEntity;
import databook.persistence.rule.rdf.ruleset.RDFEntity;
import databook.utils.ModelUtils;

public class PersistenceContext {
	
	public static final Log log = LogFactory.getLog(PersistenceContext.class);
	private RDFDatabase rdfDb;
	private RDFDatabaseTransaction rdfTrans;
	private RuleRegistry rr;
	private IRODSFileSystem irodsFs;
	private IRODSAccount account;

	public PersistenceContext(RDFDatabase rdfDatabase, RDFDatabaseTransaction rdfTrans, RuleRegistry rr,
			IRODSFileSystem irodsFs, IRODSAccount account) {
		this.setRdfDb(rdfDatabase);
		this.setRuleRegistry(rr);
		this.setIrodsFs(irodsFs);
		this.setRdfTrans(rdfTrans);
		this.setIrodsAccount(account);
	}

	
	
	public void startTrans() {
		setRdfTrans(getRdfDb().newTransaction());
	}
	public void commit() throws RDFDatabaseException {
		getRdfTrans().commit();
	}
	public void abort() {
		getRdfTrans().abort();
	}
	
	public void create(RDFEntity e, String prop, Object o) {
		ObjectPropertyRule r = getRuleRegistry().lookupRule(e, prop, o);
		if(r!=null) {
		r.create(e, prop, o, this);
		}
	}
	
	public void create(Object e) {
		EntityRule r = getRuleRegistry().lookupRule(e);
		if(r!=null) {
		r.create(e, this);
		}
	}
	
	public void delete(Object e, String prop, Object o) {
		ObjectPropertyRule r = getRuleRegistry().lookupRule(e, prop, o);
		if(r!=null) {
			r.delete(e, prop, o, this);
		}
	}
	
	public void delete(Object e) {
		EntityRule r = getRuleRegistry().lookupRule(e);
		if(r!=null) {
			r.delete(e, this);
		}
	}
	
	public void modify(Object e, String prop, Object o0, Object o1) {
		ObjectPropertyRule r = getRuleRegistry().lookupRule(e, prop, o0);
		if(r!=null) {
			r.modify(e, prop, o0, o1, this);
		}
	}
	
	public void modify(Object e, Object e1) {
		EntityRule r = getRuleRegistry().lookupRule(e);
		if(r!=null) {
			r.modify(e, e1, this);
		}
	}
	public void union(Object e, String prop, Object o0, Object o1) {
		ObjectPropertyRule r = getRuleRegistry().lookupRule(e, prop, o0);
		if(r!=null) {
			r.union(e, prop, o0, o1, this);
		}
	}
	
	public void diff(Object e, String prop, Object o0, Object o1) {
		ObjectPropertyRule r = getRuleRegistry().lookupRule(e, prop, o1);
		if(r!=null) {
			r.diff(e, prop, o0, o1, this);
		}
	}
	List<Runnable> postProcList = new ArrayList<Runnable>();
	public void appendToPostProcList(Runnable postProc) {
		postProcList.add(postProc);
	}
	public List<Runnable> getPostProcList() {
		return postProcList;
	}
	
	public URI lookupURIByDataEntityAndAVU(DataEntity d, AVU avu) {
			try {
				List<String[]> list = ((List<String[]>) getRdfDb().selectQuery()
						.node(d.getUri()).follow(ModelUtils.METADATA_URI)
						.match(ModelUtils.ATTRIBUTE_URI).with(avu.getAttribute())
				  		.match(ModelUtils.VALUE_URI).with(avu.getValue())
				  		.match(ModelUtils.UNIT_URI).with(avu.getUnit())
				        .uri().end().run());
				
				if(list.isEmpty()) {
					return null;
				} else {
					return new URI( list.get(0)[0]);
				}
			} catch (URISyntaxException e) {
				log.error("error", e);
			}
			return null;
	}
	
	public <D> Collection<D> getPropertyObjects(RDFEntity d, String prop, StringObjectMapping<D> som) {
		String propUri = RDFAbstractPropertyRule.PROP_MODEL.getPropURI(prop);
		action<Dquery<Object, Object>, Dquery<Object, Object>, Object, Object> t = getRdfDb().selectQuery()
			.node(d.getUri()).follow(propUri);
		t = som.query(t);
		List<String[]> l = (List<String[]>) t.end().run(); 
		List<D> lo = new ArrayList<D>();
		for(String[] uritypeuri : l) {
			D i = som.sparqlQueryResultStringToObject(uritypeuri);
			lo.add(i);
		}
		return lo;
		
	}



	public RDFDatabase getRdfDb() {
		return rdfDb;
	}



	public void setRdfDb(RDFDatabase rdfDb) {
		this.rdfDb = rdfDb;
	}



	public RDFDatabaseTransaction getRdfTrans() {
		return rdfTrans;
	}



	public void setRdfTrans(RDFDatabaseTransaction rdfTrans) {
		this.rdfTrans = rdfTrans;
	}



	public RuleRegistry getRuleRegistry() {
		return rr;
	}



	public void setRuleRegistry(RuleRegistry rr) {
		this.rr = rr;
	}



	public IRODSFileSystem getIrodsFs() {
		return irodsFs;
	}



	public void setIrodsFs(IRODSFileSystem irodsFs) {
		this.irodsFs = irodsFs;
	}



	public IRODSAccount getIrodsAccount() {
		return account;
	}



	public void setIrodsAccount(IRODSAccount account) {
		this.account = account;
	}

}
