package net.ingoh.myagents.lang.il;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = PrintStatementIL.class, name = "PrintStatementIL"),
})
public abstract class StatementIL implements ILNode {}
