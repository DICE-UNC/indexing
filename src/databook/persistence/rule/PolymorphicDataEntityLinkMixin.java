package databook.persistence.rule;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonSubTypes.*;

import databook.persistence.rule.rdf.ruleset.*;
import static databook.utils.ModelUtils.*;
	  
public interface PolymorphicDataEntityLinkMixin {
	@JsonProperty("hasPart")
	@JsonTypeInfo(  
		    use = JsonTypeInfo.Id.NAME,  
		    include = JsonTypeInfo.As.PROPERTY,  
		    property = "type")  
	@JsonSubTypes({  
		    @Type(value = DataObject.class, name = "DataObject"),  
		    @Type(value = Collection.class, name = "Collection"),
		    @Type(value = Message.class, name = "Message"),
		    @Type(value = Messages.class, name = "Messages"),
		    @Type(value = User.class, name = "User"),
		    @Type(value = Access.class, name = "Access"),
		    @Type(value = Session.class, name = "Session"),
		    @Type(value = Post.class, name = "Post")})
			
	DataEntity getDataEntity();
	
	
}
