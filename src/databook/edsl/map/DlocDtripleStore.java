/** Code generated by EriLex */
package databook.edsl.map;
public class DlocDtripleStore<__t,__E> extends Dloc<__t,__E> {
	Dloc<__t,__E> i0;
	public DlocDtripleStore(
		final Dloc<__t,__E> i0) {
		super(new erilex.tree.ASTValueData(
				"loc", 
				"loc")
			, 
			new erilex.data.generic.Tree(
				new erilex.tree.ASTValueData(
					"tripleStore", 
					"tripleStore")), 
			i0);
		this.i0=i0;
	}
	public java.lang.Object accept(
		final Visitor v, 
		final java.lang.String key) {
		return v.visit(key, 
			this);
	}
	public static <__t,__E> DlocDtripleStore<__t,__E> tripleStore(
		final Dloc<__t,__E> i0) {
		return new DlocDtripleStore<__t,__E>(
			i0);
	}
}
