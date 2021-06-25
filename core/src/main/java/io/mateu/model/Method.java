package io.mateu.model;

import java.lang.annotation.Annotation;
import java.util.List;

public class Method extends Annotated {

    private final String name;
    private final AccessLevel accessLevel;
    private final ParsedClass returnType;

    public Method(ParsedClass returnType, String name, List<Annotation> annotations, AccessLevel accessLevel) {
        super(annotations);
        this.returnType = returnType;
        this.name = name;
        this.accessLevel = accessLevel;
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
}
