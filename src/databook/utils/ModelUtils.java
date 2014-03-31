package databook.utils;

import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import databook.persistence.rule.rdf.StringObjectMapping;
import erilex.data.generic.Pair;

public class ModelUtils {

	public static final String RDFS_URI_PREFIX = "http://www.w3.org/2000/01/rdf-schema#";
	public static final String DATABOOK_RESOURCE_URI_PREFIX = "http://localhost/vivo/ontology/databook#";
	public static final String DATABOOK_MODEL_URI = "http://vitro.mannlib.cornell.edu/a/graph/databook-model";
	public static final String LABEL_MODEL_URI = "http://vitro.mannlib.cornell.edu/default/vitro-kb-inf";
	public static final String TRIPLE_QUOTES = "\"\"\"";

	public static final String IS_A = "a";
	public static final String REPLACES_URI = databookResourceNoBracket("replaces");
	public static final String REPLACED_BY_URI = databookResourceNoBracket("replacedBy");
	public static final String SESSION_URI = databookResourceNoBracket("session");
	public static final String ATTRIBUTE_URI = databookResourceNoBracket("attribute");
	public static final String VALUE_URI = databookResourceNoBracket("value");
	public static final String UNIT_URI = databookResourceNoBracket("unit");
    public static final String MOST_SPECIFIC_TYPE_URI = "http://vitro.mannlib.cornell.edu/ns/vitro/0.7#mostSpecificType";
    public static final String DATA_OBJECT_URI = databookResourceNoBracket("DataObject");
    public static final String COLLECTION_URI = databookResourceNoBracket("Collection");
    public static final String LABEL_URI = rdfsResourceNoBracket("label");
    public static final String SESSION_PART_URI = databookResourceNoBracket("sessionPart");
    public static final String LIKED_BY_URI = databookResourceNoBracket("likedBy");
    public static final String DISLIKED_BY_URI = databookResourceNoBracket("dislikedBy");
    public static final String DISCUSSION_URI = databookResourceNoBracket("discussion");
    public static final String METADATA_URI = databookResourceNoBracket("metadata");
    public static final String ACCESS_HISTORY_URI = databookResourceNoBracket("accessHistory");
    public static final String HAS_PART_URI = databookResourceNoBracket("hasPart");
	public static final String CONTRIBUTOR_URI = databookResourceNoBracket("contributor");
	public static final String OWNER_URI = databookResourceNoBracket("owner");
	public static final String CREATED_URI = databookResourceNoBracket("created");
	public static final String SUBMITTED_URI = databookResourceNoBracket("submitted");
	public static final String TITLE_URI = databookResourceNoBracket("title");
	public static final String DESCRIPTION_URI = databookResourceNoBracket("description");
	public static final String PART_OF_URI = databookResourceNoBracket("partOf");
	public static final String RELATED_URI = databookResourceNoBracket("related");
	public static final String ACCESS_URI = databookResourceNoBracket("Access");
	public static final String ACCESS_PERMISSION_URI = databookResourceNoBracket("AccessPermission");
	public static final String AVU_URI = databookResourceNoBracket("AVU");
	public static final String DATA_FORMAT_URI = databookResourceNoBracket("DataFormat");
	public static final String POST_URI = databookResourceNoBracket("Post");
	public static final String WORKFLOW_URI = databookResourceNoBracket("Workflow");
	public static final String DATABOOK_USER_URI = databookResourceNoBracket("DatabookUser");
//	public static final String ACCESS_URI = databookResourceNoBracket("Access");
	public static final String FINISHED_URI = databookResourceNoBracket("finished");
	public static final String ACCESS_PERMISSIONS_URI = databookResourceNoBracket("accessPermissions");
	public static final String DATA_SIZE_URI = databookResourceNoBracket("dataSize");
	public static final String HAS_VERSION_URI = databookResourceNoBracket("hasVersion");
	public static final String ACTION_URI = databookResourceNoBracket("action");
	
	public static final String ATTR_PREVIEW = "preview";
	public static final String ATTR_THUMB_PREVIEW = "thumbPreview";
	public static final Object ATTR_DATA_SIZE = "dataSize";
	public static final String ATTR_HAS_VERSION = "preview";
	public static final String ATTR_TITLE = "title";
	public static final String ATTR_DESCRIPTION = "description";
	public static final String ATTR_REPLACES = "replaces";
	public static final String ATTR_REPLACED_BY = "replacedBy";
	public static final String ATTR_RELATED = "related";
	public static final String ATTR_CONTRIBUTOR = "contributor";

    
    public static String uriToAttribute(String uri) {
    	return "data:" + uri.substring(uri.lastIndexOf('#'));
    }

/*	public static DatabookDistributedProperty<?> getProperty(String subjectUri, String predicateUri, DatabookPropertyContext context) {
		// DatabookPropertyContext context = new DatabookPropertyContext(rdfDatabase, rdfTrans, webappDaoFactory, ConnectionUtils.irodsFs, ConnectionUtils.adminAccount());
		if(predicateUri.equals(LIKED_BY_URI) || 
				predicateUri.equals(DISLIKED_BY_URI)) {
			return getDatabookUserDatabookDistributedProperty(subjectUri,
					predicateUri, context);			
		}
		else if(predicateUri.equals(DISCUSSION_URI)) {
			return getPostDatabookDistributedProperty(subjectUri, predicateUri,
					context);
		}
		else if(predicateUri.equals(METADATA_URI)) {
			return getAVUDatabookDistributedProperty(subjectUri, predicateUri,
					context);
		} else if(predicateUri.equals(ACCESS_HISTORY_URI)) {
			return getAccessDatabookDistributedProperty(subjectUri, predicateUri, context);
		} else {
			throw new Error("Unsupported property "+predicateUri);
		}
	}

	public static DatabookDistributedProperty<AVU> getAVUDatabookDistributedProperty(
			String subjectUri, String predicateUri,
			DatabookPropertyContext context) {
		return new LocalOnlyDatabookDistributedObjectProperty<AVU>(subjectUri, predicateUri, true, context) {
			@Override
			public AVU stringToObject(String val) {
				return new AVU(val);
			}
		};
	}

	public static DatabookDistributedProperty<Post> getPostDatabookDistributedProperty(
			String subjectUri, String predicateUri,
			DatabookPropertyContext context) {
		return new LocalOnlyDatabookDistributedObjectProperty<Post>(subjectUri, predicateUri, true, context) {
			@Override
			public Post stringToObject(String val) {
				return new Post(val);
			}
		};
	}

	public static DatabookDistributedProperty<IndividualObject> getPostDatabookDistributedProperty(
			String subjectUri, String predicateUri,
			DatabookPropertyContext context) {
		return new LocalOnlyDatabookDistributedObjectProperty<Post>(subjectUri, predicateUri, true, context) {
			@Override
			public Post stringToObject(String val) {
				return new Post(val);
			}
		};
	}

	public static DatabookDistributedProperty<Access> getAccessDatabookDistributedProperty(
			String subjectUri, String predicateUri,
			DatabookPropertyContext context) {
		return new LocalOnlyDatabookDistributedObjectProperty<Access>(subjectUri, predicateUri, true, context) {
			@Override
			public Access stringToObject(String val) {
				return new Access(val);
			}
		};
	}

	public static DatabookDistributedProperty<DatabookUser> getDatabookUserDatabookDistributedProperty(
			String subjectUri, String predicateUri,
			DatabookPropertyContext context) {
		return new LocalOnlyDatabookDistributedObjectProperty<DatabookUser>(subjectUri, predicateUri, false, context) {

			@Override
			public DatabookUser stringToObject(String val) {
				return new DatabookUser(val);
			}
		};
	}
*/	
	public static String databookResourceNoBracket(String name) {
		return DATABOOK_RESOURCE_URI_PREFIX+name;
	}

	public static String bracket(String uri) {
		return "<"+uri+">";
	}

	public static String bracket(URI uri) {
		return "<"+uri+">";
	}

	public static String databookResource(String name) {
		return "<"+databookResourceNoBracket(name)+">";
	}

	public static String rdfsResource(String name) {
		return "<"+rdfsResourceNoBracket(name)+">";
	}

	public static String rdfsResourceNoBracket(String name) {
		return RDFS_URI_PREFIX+name;
	}

	public static String databookInt(String name) {
		return "\""+name+"\"^^<http://www.w3.org/2001/XMLSchema#integer>";
	}

	public static String databookDecimal(String name) {
		return "\""+name+"\"^^<http://www.w3.org/2001/XMLSchema#decimal>";
	}
	public static String databookString(String name) {
		return TRIPLE_QUOTES+name+TRIPLE_QUOTES+"^^<http://www.w3.org/2001/XMLSchema#string>";
	}

	public static String databookString(String name, String locale) {
		return TRIPLE_QUOTES+name+TRIPLE_QUOTES+"@"+locale;
	}

	public static String toRDFDateFormat(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");  
		return sdf.format(date);
	}
	public static Date parseRDFDateFormat(String source) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");  
			return sdf.parse(source);
	} catch (ParseException e) {
		throw new Error(e);
	}
	}
	public static String databookDatetime(Date date) {
		return "\""+toRDFDateFormat(date)+"\"^^<http://www.w3.org/2001/XMLSchema#dateTime>";
	}

	public static String databookAnonymousResource(String... po) {
		StringBuilder sb = new StringBuilder("[ ");
		sb.append(po[0]);
		sb.append(" ");
		sb.append(po[1]);
		sb.append(" ");
		for(int i=2;i<po.length;i+=2) {
			sb.append("; ");
			sb.append(po[i]);
			sb.append(" ");
			sb.append(po[i+1]);
			sb.append(" ");			
		}
		sb.append("]");
		return sb.toString();
	}

	public static String databookStatement(String s, String... po) {
		StringBuilder sb = new StringBuilder(s);
		sb.append(" ");
		sb.append(po[0]);
		sb.append(" ");
		sb.append(po[1]);
		sb.append(" ");
		for(int i =2;i<po.length;i+=2) {
			sb.append("; ");
			sb.append(po[i]);
			sb.append(" ");
			sb.append(po[i+1]);
			sb.append(" ");
		}
		sb.append(". ");
		return sb.toString();
	}

	public static String databookData(String value, String type) {
		if(type.equals("integer")) {
			return databookInt(value);
		} else {
			return databookString(value);
		}
	}

	public static void validateUri(String uri) {
	  try {
	    new URI(uri);
	  } catch(Exception e){
	    throw new Error(e);
	  }
	}
	
	public static Date parseTimeStr(String timeStr) {
		try {
			return new SimpleDateFormat("yyyy MM dd HH:mm:ss").parse(timeStr);
		} catch (java.text.ParseException e) {
			throw new Error("wrong time format '" + timeStr + "'");
		}
	}


	/*public static Entity<?> uriToPrototype(String subjectUri, DatabookContext context) {
		WebappDaoFactory webAppDaoFactory = context.webappDaoFactory;
		IndividualDao individualDao = webAppDaoFactory.getIndividualDao();
		Individual subject = individualDao.getIndividualByURI(subjectUri);
		String typeUri = subject.getObjectPropertyStatements(MOST_SPECIFIC_TYPE_URI).get(0).getObjectURI();
		if(typeUri.equals(DATA_OBJECT_URI)) {
			return DataObject.prototype;
		} else if(typeUri.equals(COLLECTION_URI)) {
			return Collection.prototype;
		} else if(typeUri.equals(ACCESS_URI)) {
			return Access.prototype;
		} else if(typeUri.equals(DATABOOK_USER_URI)) {
			return DatabookUser.prototype;
		} else {
			throw new UnsupportedEntityUriException(subjectUri);
		}

	}*/

	private static long ctime = 0;
	private static int counter = 0;
	public static synchronized String generateUniqueUri(String typeUri) {
		long time = new Date().getTime();
		if(time != ctime) {
			counter = 1;
			ctime = time;
		} else {
			counter++;
		}
		return typeUri + "_" + time + "_" + counter;
	}

	/*public static Entity<?> getEntity(DatabookContext context, String dataEntityUri) {
		String dataEntityType = context.rdfDatabase.getEntityTypeUri(dataEntityUri);
		Entity<?> dataEntity;
		if(dataEntityType.equals(ModelUtils.COLLECTION_URI)) {
			dataEntity = Collection.prototype.get(dataEntityUri, context);
		} else if(dataEntityType.equals(ModelUtils.DATA_OBJECT_URI)) {
			dataEntity = DataObject.prototype.get(dataEntityUri, context);
		} else if(dataEntityType.equals(ModelUtils.ACTION_URI)) {
			dataEntity = Action.prototype.get(dataEntityUri, context);
		} else {
			throw new Error("Unsupported object type  " + dataEntityType);
			
		}
		return dataEntity;

	}*/

/*	public static DatabookDistributedProperty<?, ?> getProperty(Entity<?> dataEntity, String propertyName, DatabookContext context) {
	DatabookDistributedProperty<?, ?> p;
	if(propertyName.equals(ATTR_DATA_SIZE)) {
		p = (DatabookDistributedProperty<?, ?>) ((DataObject) dataEntity).dataSize(context);
	} else if(propertyName.equals(ATTR_HAS_VERSION)) {
		p = (DatabookDistributedProperty<?, ?>) ((IndividualObject<?>) dataEntity).hasVersion(context);
	} else if(propertyName.equals(ATTR_TITLE)) {
		p = (DatabookDistributedProperty<?, ?>) ((DataEntity<?>) dataEntity).title(context);
	} else if(propertyName.equals(ATTR_DESCRIPTION)) {
		p = (DatabookDistributedProperty<?, ?>) ((DataEntity<?>) dataEntity).description(context);
	} else if(propertyName.equals(ATTR_RELATED)) {
		p = (DatabookDistributedProperty<?, ?>) ((DataEntity<?>) dataEntity).related(context);
	} else if(propertyName.equals(ATTR_REPLACES)) {
		p = (DatabookDistributedProperty<?, ?>) ((IndividualObject<?>) dataEntity).replaces(context);
	} else if(propertyName.equals(ATTR_REPLACED_BY)) {
		p = (DatabookDistributedProperty<?, ?>) ((IndividualObject<?>) dataEntity).replaces(context);
	} else if(propertyName.equals(ATTR_CONTRIBUTOR)) {
		p = (DatabookDistributedProperty<?, ?>) ((DataEntity<?>) dataEntity).contributor(context);
	} else {
		throw new Error("Unsupported property name " + propertyName);
	}
	return p;
	}*/

	public static String extractId(String uri) {
		return uri.substring(uri.lastIndexOf('#')+1);
	}
	
	public static <D> HashMap<String, D> collectionToHashMap(java.util.Collection<D> o, StringObjectMapping<D> som) {
		HashMap<String, D> m = new HashMap<String, D>();
		for(D d : o) {
			m.put(som.objectToString(d), d);
		}
		return m;
	}
	public static <D> Pair<Collection<D>, Collection<D>> sDiff(Collection<D> o0, Collection<D> o1, StringObjectMapping<D> som) {
		if(o0 == null) {
			Collection<D> emp = Collections.emptyList();
			if(o1 == null) {
				return new Pair<Collection<D>, Collection<D>>(emp, emp);
			} else {
				return new Pair<Collection<D>, Collection<D>>(emp, o1);
			}
		} else if(o1 == null) {
			Collection<D> emp = Collections.emptyList();
			return new Pair<Collection<D>, Collection<D>>(o0, emp);
		}
		HashMap<String, D> m0 = collectionToHashMap(o0, som);
		HashMap<String, D> m1 = collectionToHashMap(o1, som);
		List<D> toDelete = new ArrayList<D>();
		List<D> toCreate = new ArrayList<D>();
		for(D d0 : o0) {
			D d1;
				
			if((d1=m1.get(som.objectToString(d0)))!=null) {
				// do not cascade modification
				// modification of d0 should be encoded in separate messages
				// context.modify(d0, d1);
			} else {
				toDelete.add(d0);
			}
		}
		for(D d1 : o1) {
			if(m0.get(som.objectToString(d1))==null) {
				toCreate.add(d1);
			}
		}
		return new Pair<Collection<D>, Collection<D>>(toDelete, toCreate);
	}
	public static <T> T getUnique(Collection<T> c) {
		if(c.isEmpty()) {
			return null;
		} else {
			return c.iterator().next();
		}
	}


}
