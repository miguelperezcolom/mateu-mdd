package io.mateu.annotationProcessor.parser;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.*;
import javax.lang.model.util.ElementScanner8;
import javax.lang.model.util.SimpleTypeVisitor8;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import io.mateu.model.AccessLevel;
import io.mateu.model.Method;
import io.mateu.model.ParsedClass;

public class ElementParser {

    private final ProcessingEnvironment processingEnv;
    private final RoundEnvironment roundEnv;

    Map<String, ParsedClass> cache = new HashMap<>();

    public ElementParser(ProcessingEnvironment processingEnv, RoundEnvironment roundEnv) {
        this.processingEnv = processingEnv;
        this.roundEnv = roundEnv;
    }

    public ParsedClass parse(TypeElement typeElement, List<TypeElement> generics) {
        //todo: implementar
        ParsedClass parsedClass = cache.get(typeElement.getQualifiedName().toString());
        if (parsedClass == null) {
            parsedClass = new ParsedClass(cache, typeElement.getAnnotationMirrors().stream().map(m -> {
                try {
                    Class<? extends Annotation> annotationClass = (Class<? extends Annotation>) Class.forName(m.getAnnotationType().toString());
                    Annotation annotation = typeElement.getAnnotation(annotationClass);
                    return annotation;
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                return null;
            }).filter(x -> x != null).collect(Collectors.toList()),
                    typeElement.getQualifiedName().toString(),
                    Lists.newArrayList(),
                    parseMethods(typeElement),
                    () -> generics != null ? generics.stream().map(type -> parse(type, null)).collect(Collectors.toList()) : parseTypeArguments(typeElement));
        }
        return parsedClass;
    }

    private List<ParsedClass> parseTypeArguments(TypeElement typeElement) {
        List<ParsedClass> typeArguments = new ArrayList<>();
        typeElement.getTypeParameters().forEach(tp -> {
            TypeElement element = getTypeElement(tp.asType());
            if (element != null) {
                typeArguments.add(parse(element, null));
            }
        });
        return typeArguments;
    }


    private List<Method> parseMethods(TypeElement typeElement) {
        List<Method> methods = new ElementScanner8<List<Method>, TypeElement>() {

            private List<Method> methods = new ArrayList<>();

            @Override
            public List<Method> visitExecutable(ExecutableElement e, TypeElement typeElement) {
                methods.add(createMethod(e));
                return methods;
            }
        }.visit(typeElement);
        return methods;
    }

    private Method createMethod(ExecutableElement ee) {
        return new Method(
                getParsedClass(ee.getReturnType())
                , ee.getSimpleName().toString()
                , Lists.newArrayList()
                , AccessLevel.Public
        );
    }

    private ParsedClass getParsedClass(TypeMirror returnType) {
        TypeElement typeElement = getTypeElement(returnType);
        if (typeElement != null && !Class.class.getName().equals(typeElement.getQualifiedName().toString())) {
            List<TypeElement> generics = extractGenerics(returnType);
            return parse(typeElement, generics);
        } else {
            ParsedClass parsedClass = cache.get(returnType.toString());
            if (parsedClass == null) {
                parsedClass = new ParsedClass(cache, returnType.toString());
            }
            return parsedClass;
        }
    }

    private List<TypeElement> extractGenerics(TypeMirror returnType) {
        List<TypeElement> generics = new ArrayList<>();
        returnType.accept(new SimpleTypeVisitor8<Void, Void>()
        {
            @Override
            public Void visitDeclared(DeclaredType declaredType, Void v)
            {
                List<? extends TypeMirror> typeArguments = declaredType.getTypeArguments();
                typeArguments.forEach(type -> {
                    generics.add(getTypeElement(type));
                });
                return null;
            }
            @Override
            public Void visitPrimitive(PrimitiveType primitiveType, Void v)
            {
                return null;
            }
            @Override
            public Void visitArray(ArrayType arrayType, Void v)
            {
                return null;
            }
            @Override
            public Void visitTypeVariable(TypeVariable typeVariable, Void v)
            {
                return null;
            }
            @Override
            public Void visitError(ErrorType errorType, Void v)
            {
                return null;
            }
            @Override
            protected Void defaultAction(TypeMirror typeMirror, Void v)
            {
                return null;
            }
        }, null);

        return generics;
    }

    private TypeElement getTypeElement(TypeMirror returnType) {
        Element element = processingEnv.getTypeUtils().asElement(returnType);
        return element != null && element instanceof TypeElement ? (TypeElement) element : null;
    }


}
