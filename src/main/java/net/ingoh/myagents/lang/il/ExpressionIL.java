package net.ingoh.myagents.lang.il;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = StringLiteralIL.class, name = "StringLiteralIL"),
        @JsonSubTypes.Type(value = VariableIL.class, name = "VariableIL")
})
public abstract class ExpressionIL implements ILNode {}