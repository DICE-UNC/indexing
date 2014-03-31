package databook.persistence.rule;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonSubTypes.*;

import databook.persistence.rule.rdf.ruleset.Collection;
import databook.persistence.rule.rdf.ruleset.DataEntity;
import databook.persistence.rule.rdf.ruleset.DataObject;
import databook.persistence.rule.rdf.ruleset.Message;
import databook.persistence.rule.rdf.ruleset.Messages;
import static databook.utils.ModelUtils.*;
	  
public interface PolymorphicDataEntityMixin {
	@JsonProperty("hasPart")
	@JsonTypeInfo(  
		    use = JsonTypeInfo.Id.NAME,  
		    include = JsonTypeInfo.As.PROPERTY,  
		    property = "type")  
	@JsonSubTypes({  
		    @Type(value = DataObject.class, name = "DataObject"),  
		    @Type(value = Collection.class, name = "Collection"),
		    @Type(value = Message.class, name = "Message"),
		    @Type(value = Messages.class, name = "Messages")})  
	java.util.List<DataEntity> getHasPart();
}
