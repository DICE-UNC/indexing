package databook.persistence.rule.rdf;

import static databook.utils.ModelUtils.DATABOOK_MODEL_URI;
import static databook.utils.ModelUtils.DATABOOK_RESOURCE_URI_PREFIX;
import static databook.utils.ModelUtils.LABEL_MODEL_URI;
import static databook.utils.ModelUtils.RDFS_URI_PREFIX;
import static databook.utils.ModelUtils.bracket;

public interface PropertyModelMapper {
	public String getPropURI(String prop);
	public String getPropModelURI(String prop);


}
