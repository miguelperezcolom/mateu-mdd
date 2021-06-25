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
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import io.mateu.model.AccessLevel;
import io.mateu.model.Method;
import io.mateu.model.ParsedClass;

public class ElementParser {

    public ParsedClass parse(ProcessingEnvironment processingEnv, RoundEnvironment roundEnv, TypeElement typeElement, List<TypeElement> generics) {
        //todo: implementar
        return new ParsedClass(typeElement.getAnnotationMirrors().stream().map(m -> {
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
                parseMethods(processingEnv, roundEnv, typeElement),
                generics != null ? generics.stream().map(type -> parse(processingEnv, roundEnv, type, null)).collect(Collectors.toList()) : parseTypeArguments(processingEnv, roundEnv, typeElement));
    }

    private List<ParsedClass> parseTypeArguments(ProcessingEnvironment processingEnv, RoundEnvironment roundEnv, TypeElement typeElement) {
        List<ParsedClass> typeArguments = new ArrayList<>();
        typeElement.getTypeParameters().forEach(tp -> {
            TypeElement element = getTypeElement(processingEnv, tp.asType());
            if (element != null) {
                typeArguments.add(new ElementParser().parse(processingEnv, roundEnv, element, null));
            }
        });
        return typeArguments;
    }


    private List<Method> parseMethods(ProcessingEnvironment processingEnv, RoundEnvironment roundEnv, TypeElement typeElement) {
        List<Method> methods = new ElementScanner8<List<Method>, TypeElement>() {

            private List<Method> methods = new ArrayList<>();

            @Override
            public List<Method> visitExecutable(ExecutableElement e, TypeElement typeElement) {
                methods.add(createMethod(processingEnv, roundEnv, e));
                return methods;
            }
        }.visit(typeElement);
        return methods;
    }

    private Method createMethod(ProcessingEnvironment processingEnv, RoundEnvironment roundEnv, ExecutableElement ee) {
        return new Method(
                getParsedClass(processingEnv, roundEnv, ee.getReturnType())
                , ee.getSimpleName().toString()
                , Lists.newArrayList()
                , AccessLevel.Public
        );
    }

    private ParsedClass getParsedClass(ProcessingEnvironment processingEnv, RoundEnvironment roundEnv, TypeMirror returnType) {
        TypeElement typeElement = getTypeElement(processingEnv, returnType);
        if (typeElement != null) {
            List<TypeElement> generics = extractGenerics(processingEnv, roundEnv, returnType);
            return parse(processingEnv, roundEnv, typeElement, generics);
        } else {
            return new ParsedClass(returnType.toString());
        }
    }

    private List<TypeElement> extractGenerics(ProcessingEnvironment processingEnv, RoundEnvironment roundEnv, TypeMirror returnType) {
        List<TypeElement> generics = new ArrayList<>();
        returnType.accept(new SimpleTypeVisitor8<Void, Void>()
        {
            @Override
            public Void visitDeclared(DeclaredType declaredType, Void v)
            {
                List<? extends TypeMirror> typeArguments = declaredType.getTypeArguments();
                typeArguments.forEach(type -> {
                    generics.add(getTypeElement(processingEnv, type));
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

    private TypeElement getTypeElement(ProcessingEnvironment processingEnvironment, TypeMirror returnType) {
        Element element = processingEnvironment.getTypeUtils().asElement(returnType);
        return element != null && element instanceof TypeElement ? (TypeElement) element : null;
    }


}
