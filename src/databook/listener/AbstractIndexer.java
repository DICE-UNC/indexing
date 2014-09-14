package databook.listener;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import databook.listener.service.IndexingService;
import databook.persistence.rule.EntityRule;
import databook.persistence.rule.RuleRegistry;
import databook.persistence.rule.rdf.ruleset.*;

public class AbstractIndexer implements Indexer {

	protected IndexingService indexingService;
	public static Log log = LogFactory.getLog(AbstractIndexer.class);

	public void setIndexingService(IndexingService is) {
		this.indexingService = is;
	}

	public void startup() {
		indexingService.regIndexer(this);
	}

	public void shutdown() {
		indexingService.unregIndexer(this);
	}

	protected RuleRegistry ruleRegistry = new RuleRegistry();

	public void messages(Messages ms) {
		try {
			// System.out.println("messages received " + ms);

			for (Message m : ms.getMessages()) {
				if (m.getOperation().equals("create")) {
					for (DataEntity o : m.getHasPart()) {

						EntityRule r = ruleRegistry.lookupRule(o);
						r.create(o, null);
					}

				} else if (m.getOperation().equals("delete")) {
					for (DataEntity o : m.getHasPart()) {
						EntityRule r = ruleRegistry.lookupRule(o);
						r.delete(o, null);

					}
				} else if (m.getOperation().equals("modify")) {
					final DataEntity o0 = m.getHasPart().get(0);
					final DataEntity o1 = m.getHasPart().get(1);
					EntityRule r = ruleRegistry.lookupRule(o0);
					r.modify(o0, o1, null);

				} else if (m.getOperation().equals("union")) {
					DataEntity o0 = m.getHasPart().get(0);
					DataEntity o1 = m.getHasPart().get(1);
					EntityRule r = ruleRegistry.lookupRule(o0);
					r.union(o0, o1, null);

				}

				else if (m.getOperation().equals("diff")) {
					DataEntity o0 = m.getHasPart().get(0);
					DataEntity o1 = m.getHasPart().get(1);
					EntityRule r = ruleRegistry.lookupRule(o0);
					r.diff(o0, o1, null);
				}
			}
		} catch (Exception e) {
			log.error("error", e);
		}
	}
	protected Scheduler scheduler;

	public void setScheduler(Scheduler s) {
		this.scheduler = s;
	}

}
