package io.mateu.annotationProcessor;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;
import java.io.PrintWriter;
import java.util.Set;

import com.google.auto.service.AutoService;
import io.mateu.annotationProcessor.parser.ElementParser;
import io.mateu.core.MateuClassProcessor;

@SupportedAnnotationTypes({"io.mateu.annotations.MateuBuilder"})
@AutoService(Processor.class)
public class MateuBuilderAnnotationProcessor  extends AbstractProcessor {

    @Override
    public SourceVersion getSupportedSourceVersion() {
        SupportedSourceVersion ssv = this.getClass().getAnnotation(SupportedSourceVersion.class);
        SourceVersion sv = null;
        if (ssv == null) {
            return SourceVersion.latest();
        } else {
            return ssv.value();
        }
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (TypeElement annotation : annotations) {
            Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(annotation);

            ElementParser elementParser = new ElementParser(processingEnv, roundEnv);

            for (Element e : annotatedElements) {
                if (e instanceof TypeElement) {
                    TypeElement typeElement = (TypeElement) e;
                    String simpleClassName = typeElement.getSimpleName().toString();

                    System.out.println("MateuBuilderAnnotationProcessor running on " + simpleClassName);

                    new MateuClassProcessor(path -> {
                        JavaFileObject builderFile = processingEnv.getFiler().createSourceFile(path);
                        PrintWriter out = new PrintWriter(builderFile.openWriter());
                        return out;
                    }).process(elementParser.parse(typeElement, null));
                }
            }
        }

        return true;
    }

}
