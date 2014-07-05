package databook.persistence.rule;

public interface ObjectPropertyRule<T1, T2, PersCxt> {
	void create(T1 e0, String prop, T2 e1, PersCxt context);
	/** when e1 == null, delete all properties instances */
	void delete(T1 e0, String prop, T2 e1, PersCxt context);
	void modify(T1 e0, String prop, T2 e1, T2 e2, PersCxt context);
	void union(T1 e, String prop, T2 o0, T2 o1, PersCxt context);
	void diff(T1 e, String prop, T2 o0, T2 o1, PersCxt context);
}
