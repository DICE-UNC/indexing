/** Code generated by EriLex */
package databook.edsl.googql;
public abstract class Visitor {
	public abstract java.lang.Object visit(
		final java.util.Stack<String> nodes, 
		final java.lang.Integer counter, 
		final java.util.Map<String,Class> selects, 
		final DactionDtime d);
	public abstract java.lang.Object visit(
		final java.util.Stack<String> nodes, 
		final java.lang.Integer counter, 
		final java.util.Map<String,Class> selects, 
		final Dactionuri d);
	public abstract java.lang.Object visit(
		final java.util.Stack<String> nodes, 
		final java.lang.Integer counter, 
		final java.util.Map<String,Class> selects, 
		final DactionDsel d);
	public abstract java.lang.Object visit(
		final java.util.Stack<String> nodes, 
		final java.lang.Integer counter, 
		final java.util.Map<String,Class> selects, 
		final DactionDfollow d);
	public abstract java.lang.Object visit(
		final java.util.Stack<String> nodes, 
		final java.lang.Integer counter, 
		final java.util.Map<String,Class> selects, 
		final DactionDmatch d);
	public abstract java.lang.Object visit(
		final java.util.Stack<String> nodes, 
		final java.lang.Integer counter, 
		final java.util.Map<String,Class> selects, 
		final DactionDnumber d);
	public abstract java.lang.Object visit(
		final java.util.Stack<String> nodes, 
		final java.lang.Integer counter, 
		final java.util.Map<String,Class> selects, 
		final DactionDback d);
	public abstract java.lang.Object visit(
		final java.util.Stack<String> nodes, 
		final java.lang.Integer counter, 
		final java.util.Map<String,Class> selects, 
		final DactionDend d);
	public abstract java.lang.Object visit(
		final java.util.Stack<String> nodes, 
		final java.lang.Integer counter, 
		final java.util.Map<String,Class> selects, 
		final DactionDstring d);
	public abstract java.lang.Object visit(
		final java.util.Stack<String> nodes, 
		final java.lang.Integer counter, 
		final java.util.Map<String,Class> selects, 
		final DactionDinteger d);
	public abstract java.lang.Object visit(
		final java.util.Stack<String> nodes, 
		final java.lang.Integer counter, 
		final java.util.Map<String,Class> selects, 
		final DactionHeadDnode d);
	public abstract java.lang.Object visit(
		final java.util.Stack<String> nodes, 
		final java.lang.Integer counter, 
		final java.util.Map<String,Class> selects, 
		final DmatchTailDwith d);
	public abstract java.lang.Object visit(
		final java.util.Stack<String> nodes, 
		final java.lang.Integer counter, 
		final java.util.Map<String,Class> selects, 
		final DqueryDuse d);
}
