package databook.persistence.rule;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.commons.collections4.Factory;
import org.apache.commons.collections4.map.LazyMap;

public class RuleRegistry {
	private Map<Class, EntityRule> entityRuleMap;
	private Map<Class, Map<String, Map<Class, ObjectPropertyRule>>> objectPropertyMap;
	
	public RuleRegistry() {
		final Factory f = new Factory() {
			
			@Override
			public Object create() {
				return new HashMap<Class, ObjectPropertyRule>();
			}
		}; 
		final Factory f2 = new Factory() {
			@Override
			public Object create() {
				return LazyMap.lazyMap(new HashMap<String, Map<Class, ObjectPropertyRule>>(), f);
			}
		};
		objectPropertyMap = LazyMap.lazyMap(new HashMap<Class, Map<String, Map<Class, ObjectPropertyRule>>>(), f2);
		entityRuleMap = new HashMap<Class, EntityRule>();
	}
	
	public <T> void registerRule(Class<T> c, EntityRule<T, ?> r) {
		entityRuleMap.put(c, r);
		
	}
	
	public <T,T1> void registerRule(Class<T> c, String prop, Class<T1> c1, ObjectPropertyRule<T,T1,?> r) {
		objectPropertyMap.get(c).get(prop).put(c1, r);
		
	}
	
	public ObjectPropertyRule lookupRule(Object e, String prop, Object o) {
		Class c = e.getClass();
		Class c2;
		Map<String, Map<Class, ObjectPropertyRule>> rm;
		Map<Class, ObjectPropertyRule> rm2;
		ObjectPropertyRule r = null;
		outer: while(!c.equals(Object.class)) {
			 rm=objectPropertyMap.get(c);
			 if(rm != null) {
				rm2 = rm.get(prop);
				if(rm2 != null) {
					if(o == null) {
						Iterator<ObjectPropertyRule> es = rm2.values().iterator();
						if(es.hasNext()) {
							r = es.next();
							break outer;
						}
					} else {
						c2=o.getClass();
						final Map<Class, ObjectPropertyRule> rm2copy = rm2;
						r = searchSuperType(c2, new Function<Class, ObjectPropertyRule>() {
							public ObjectPropertyRule apply(Class c) {
						//		Log.info(RuleRegistry.class, "searching for rule for "+c.getName());
								return rm2copy.get(c);
							}
						});
						if(r !=null) {
							break outer;
						}
						c2=c2.getSuperclass();	
					}
				}
			 }	
			 c = c.getSuperclass();
		}
		if(r == null) {
			Logger.getLogger("Index").severe("error: "+e.getClass().getName()+"::"+prop+"::"+(o==null?"<void>":o.getClass().getName())+" has no rule defined");
		}
		return r;
	}
	
	public EntityRule lookupRule(Object e) {
		Class c = e.getClass();
		EntityRule r = null;
		while(!c.equals(Object.class) && (r=entityRuleMap.get(c)) == null)
			c = c.getSuperclass();
		
		return r;
	}
	
	public interface Function<S,T> {
		public T apply(S param);
	}
	
	public static <T> T searchSuperType(Class c, Function<Class, T> f) {
		T r;
		if(c==null) {
			return null;
		} else if((r=f.apply(c)) != null) {
			return r;
		} else if((r=searchSuperType(c.getSuperclass(), f))!=null){
			return r;
		} else {
			for(Class i:c.getInterfaces()) {
				if((r=searchSuperType(i, f))!=null) {
					return r;
				}
			}
		}
		return null;
		
	}

}