package databook.listener;

import databook.persistence.rule.rdf.ruleset.Message;

public interface Scheduler {
	public interface Continuation<T> {
		void call(T data);
	}
	public interface Credentials {
		
	}
	
	public class Job<T> {
		Indexer requester;
		Message obj;
		Continuation<T> success;
		Continuation<Throwable> fail;
		Object cred;
		public Job(Indexer requester, Message obj, Continuation<T> success,
				Continuation<Throwable> fail) {
			super();
			this.requester = requester;
			this.obj = obj;
			this.success = success;
			this.fail = fail;
		}
	}
	
	public void submit(Job j);
}
