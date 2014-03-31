package databook.listener.service;

import databook.listener.Indexer;

public interface IndexingService {
	public void regIndexer(Indexer i);

	public void unregIndexer(Indexer i);

}
