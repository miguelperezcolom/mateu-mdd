package io.mateu.annotationProcessor.parser;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementScanner8;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import io.mateu.model.AccessLevel;
import io.mateu.model.Method;
import io.mateu.model.ParsedClass;

public class ElementParser {

    public ParsedClass parse(ProcessingEnvironment processingEnv, RoundEnvironment roundEnv, TypeElement typeElement) {
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
                "org.example.MiFormulario",
                Lists.newArrayList(),
                parseMethods(processingEnv, roundEnv, typeElement),
                parseTypeArguments(processingEnv, roundEnv, typeElement));
    }

    private List<ParsedClass> parseTypeArguments(ProcessingEnvironment processingEnv, RoundEnvironment roundEnv, TypeElement typeElement) {
        List<ParsedClass> typeArguments = new ArrayList<>();
        typeElement.getTypeParameters().forEach(tp -> {
            TypeElement element = getTypeElement(processingEnv, tp.asType());
            if (element != null) {
                typeArguments.add(new ElementParser().parse(processingEnv, roundEnv, element));
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
            return parse(processingEnv, roundEnv, typeElement);
        } else {
            return new ParsedClass(returnType.toString());
        }
    }

    private TypeElement getTypeElement(ProcessingEnvironment processingEnvironment, TypeMirror returnType) {
        Element element = processingEnvironment.getTypeUtils().asElement(returnType);
        return element != null && element instanceof TypeElement ? (TypeElement) element : null;
    }


}
