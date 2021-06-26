package io.mateu.annotationProcessor.parser;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.*;
import javax.lang.model.util.ElementScanner8;
import javax.lang.model.util.SimpleTypeVisitor8;

import java.lang.annotation.Annotation;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import io.mateu.model.AccessLevel;
import io.mateu.model.Field;
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
        String className = typeElement.getQualifiedName().toString();
        boolean found = cache.containsKey(className);
        ParsedClass parsedClass = cache.computeIfAbsent(className, key -> new ParsedClass(key));
        if (!found && !isBasic(className)) {
            parsedClass.setAnnotations(typeElement.getAnnotationMirrors().stream().map(m -> {
                try {
                    Class<? extends Annotation> annotationClass = (Class<? extends Annotation>) Class.forName(m.getAnnotationType().toString());
                    Annotation annotation = typeElement.getAnnotation(annotationClass);
                    return annotation;
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                return null;
            }).filter(x -> x != null).collect(Collectors.toList()));
            parsedClass.setFields(parseFields(typeElement));
            parsedClass.setMethods(parseMethods(typeElement));
            parsedClass.setTypeArguments(generics != null ? generics.stream().map(type -> parse(type, null)).collect(Collectors.toList()) : parseTypeArguments(typeElement));
        }
        return parsedClass;
    }

    private boolean isBasic(String className) {
        boolean basic = Class.class.getName().equals(className);
        basic = basic || Object.class.getName().equals(className);
        basic = basic || LocalDate.class.getName().equals(className);
        basic = basic || String.class.getName().equals(className);
        basic = basic || int.class.getName().equals(className);
        basic = basic || double.class.getName().equals(className);
        basic = basic || boolean.class.getName().equals(className);
        return basic;
    }

    private ParsedClass parse(TypeMirror returnType) {
        TypeElement typeElement = getTypeElement(returnType);
        if (typeElement != null) {
            List<TypeElement> generics = extractGenerics(returnType);
            return parse(typeElement, generics);
        } else {
            String className = returnType.toString();
            ParsedClass parsedClass = cache.computeIfAbsent(className, key -> new ParsedClass(key));
            return parsedClass;
        }
    }

    private List<ParsedClass> parseTypeArguments(TypeElement typeElement) {
        List<ParsedClass> typeArguments = new ArrayList<>();
        typeElement.getTypeParameters().forEach(tp -> {
            TypeElement element = getTypeElement(tp.asType());
            if (element != null) {
                typeArguments.add(parse(element, null));
            }
        });
        return typeArguments.stream().filter(a -> a != null).collect(Collectors.toList());
    }


    private List<Method> parseMethods(TypeElement typeElement) {
        List<Method> methods = new ArrayList<>();
        new ElementScanner8<List<Method>, TypeElement>() {

            @Override
            public List<Method> visitExecutable(ExecutableElement e, TypeElement typeElement) {
                methods.add(createMethod(e));
                return methods;
            }
        }.visit(typeElement);
        return methods.stream().filter(m -> m != null).collect(Collectors.toList());
    }

    private Method createMethod(ExecutableElement ee) {
        return new Method(
                parse(ee.getReturnType())
                , ee.getSimpleName().toString()
                , Lists.newArrayList()
                , AccessLevel.Public
        );
    }

    private List<Field> parseFields(TypeElement typeElement) {
        List<Field> fields = new ArrayList<>();
        new ElementScanner8<List<Field>, TypeElement>() {

            @Override
            public List<Field> visitVariable(VariableElement e, TypeElement typeElement) {
                fields.add(createField(e));
                return fields;
            }
        }.visit(typeElement);
        return fields.stream().filter(f -> f != null).collect(Collectors.toList());
    }

    private Field createField(VariableElement e) {
        System.out.println("creating field " + e.getEnclosingElement().getSimpleName().toString() + "." + e.getSimpleName().toString());
        return new Field(
                parse(e.asType())
                , e.getSimpleName().toString()
                , Lists.newArrayList()
        );
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

        return generics.stream().filter(g -> g != null).collect(Collectors.toList());
    }

    private TypeElement getTypeElement(TypeMirror returnType) {
        Element element = processingEnv.getTypeUtils().asElement(returnType);
        return element != null && element instanceof TypeElement ? (TypeElement) element : null;
    }


}
