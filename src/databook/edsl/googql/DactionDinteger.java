/** Code generated by EriLex */
package databook.edsl.googql;
public class DactionDinteger<__t,__E> extends Daction<__t,__E> {
	Daction<__t,__E> i0;
	public DactionDinteger(
		final Daction<__t,__E> i0) {
		super(new erilex.tree.ASTValueData(
				"action", 
				"action")
			, 
			new erilex.data.generic.Tree(
				new erilex.tree.ASTValueData(
					"integer", 
					"integer")), 
			i0);
		this.i0=i0;
	}
	public java.lang.Object accept(
		final Visitor v, 
		final java.util.Stack<String> nodes, 
		final java.lang.Integer counter, 
		final java.util.Map<String,Class> selects) {
		return v.visit(nodes, 
			counter, 
			selects, 
			this);
	}
	public static <__t,__E> DactionDinteger<__t,__E> integer(
		final Daction<__t,__E> i0) {
		return new DactionDinteger<__t,__E>(
			i0);
	}
}
