/** Code generated by EriLex */
package databook.edsl.map;
public abstract class DmapValue<t,Eta> extends erilex.data.generic.Tree<erilex.tree.ASTValueData> {
	public DmapValue(
		final erilex.tree.ASTValueData name, 
		final erilex.data.generic.Tree<erilex.tree.ASTValueData>... sub) {
		super(name, 
			sub);
	}
	public java.lang.Object run() {
		return this.accept(new Devaluate()
			, 
			new java.lang.String());
	}
	public abstract java.lang.Object accept(
		final Visitor v, 
		final java.lang.String key);
}
