package databook.persistence.rule;

public interface EntityRule<T> {
	void create(T e, PersistenceContext context);
	void delete(T e, PersistenceContext context);
	void modify(T e0, T e1, PersistenceContext context);
	void union(T e0, T e1, PersistenceContext context);
	void diff(T e0, T e1, PersistenceContext context);
}
