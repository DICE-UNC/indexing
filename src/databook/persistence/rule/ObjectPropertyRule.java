package databook.persistence.rule;

public interface ObjectPropertyRule<T1, T2> {
	void create(T1 e0, String prop, T2 e1, PersistenceContext context);
	/** when e1 == null, delete all properties instances */
	void delete(T1 e0, String prop, T2 e1, PersistenceContext context);
	void modify(T1 e0, String prop, T2 e1, T2 e2, PersistenceContext context);
	void union(T1 e, String prop, T2 o0, T2 o1, PersistenceContext context);
	void diff(T1 e, String prop, T2 o0, T2 o1, PersistenceContext context);
}
