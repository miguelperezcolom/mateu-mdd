package io.mateu.model;

import java.lang.annotation.Annotation;
import java.util.List;

public class Method extends Annotated {

    private final String name;
    private final AccessLevel accessLevel;
    private final ParsedClass returnType;
    private final List<Parameter> parameters;

    public Method(ParsedClass returnType, String name, List<Annotation> annotations, AccessLevel accessLevel, List<Parameter> parameters) {
        super(annotations);
        this.returnType = returnType;
        this.name = name;
        this.accessLevel = accessLevel;
        this.parameters = parameters;
    }

    public String getName() {
        return name;
    }

    public boolean isPublic() {
        return AccessLevel.Public == accessLevel;
    }

    public ParsedClass getReturnType() {
        return returnType;
    }

    public AccessLevel getAccessLevel() {
        return accessLevel;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }
}
