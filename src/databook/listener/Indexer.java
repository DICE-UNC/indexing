package databook.listener;

import databook.persistence.rule.rdf.ruleset.Messages;

public interface Indexer {
	
	void messages(Messages m) throws Exception;
	
	void setScheduler(Scheduler s);
	
}
