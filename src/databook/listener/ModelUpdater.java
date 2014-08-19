package databook.listener;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import databook.listener.service.IndexingService;
import databook.listener.service.MessagingService;
import databook.persistence.rule.PolymorphicDataEntityLinkMixin;
import databook.persistence.rule.PolymorphicDataEntityMixin;
import databook.persistence.rule.rdf.ruleset.DataEntity;
import databook.persistence.rule.rdf.ruleset.DataEntityLink;
import databook.persistence.rule.rdf.ruleset.Message;
import databook.persistence.rule.rdf.ruleset.Messages;

public class ModelUpdater implements MessagingService, IndexingService {

	private static final Log log = LogFactory.getLog(ModelUpdater.class);
	private List<Indexer> indexers = new ArrayList<Indexer>();
	private Scheduler s = new SimpleScheduler();

	public void regIndexer(Indexer i) {
		if (!indexers.contains(i)) {
			indexers.add(i);
		}
		i.setScheduler(s);
	}

	public void unregIndexer(Indexer i) {
		i.setScheduler(null);
		indexers.remove(i);
	}

	public static String genAVUId(String objId, String attribute, String value, String unit) {
		return URLEncoder.encode(objId + "/" + attribute + "/" + value + "/" + unit);
	}

	public void update(String messages) {
		log.info("Executing update:\n===start===\n" + messages + "\n===end===\n");

		ObjectMapper om = new ObjectMapper();
		om.addMixInAnnotations(DataEntity.class,PolymorphicDataEntityMixin.class);
		om.addMixInAnnotations(Message.class, PolymorphicDataEntityMixin.class);
		om.addMixInAnnotations(DataEntityLink.class, PolymorphicDataEntityLinkMixin.class);

		Messages ms;
		try {
			ms = om.readValue(messages, Messages.class);
		} catch (JsonParseException e) {
			log.error("error", e);
			return;
		} catch (JsonMappingException e) {
			log.error("error", e);
			return;
		} catch (IOException e) {
			log.error("error", e);
			return;
		}

		for (Indexer i : indexers) {
			try {
				i.messages(ms);
				log.info("Model updated ( id = " + i + ")");
			} catch (Throwable e) {
				log.error("error", e);
			}
		}

	}

	@Override
	public void handle(String message) {
		System.out.println("Executing update:\n===start===\n" + message + "\n===end===\n");
		log.info("Received AMQP message'" + message + "'");
		update(message);

	}
}
