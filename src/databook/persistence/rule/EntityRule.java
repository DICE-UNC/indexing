package databook.persistence.rule;

public interface EntityRule<T, PersCtx> {
	void create(T e, PersCtx context);
	void delete(T e, PersCtx context);
	void modify(T e0, T e1, PersCtx context);
	void union(T e0, T e1, PersCtx context);
	void diff(T e0, T e1, PersCtx context);
}
