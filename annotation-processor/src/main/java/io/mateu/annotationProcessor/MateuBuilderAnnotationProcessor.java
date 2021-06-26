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
import java.util.Comparator;
import java.util.ServiceLoader;
import java.util.Set;

import com.google.auto.service.AutoService;
import io.mateu.annotationProcessor.parser.ElementParser;
import io.mateu.core.MateuClassProcessor;
import io.mateu.fieldProcessors.FieldProcessor;

import static io.mateu.fieldProcessors.FieldProcessors.processors;

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

        setupProcessors();

        for (TypeElement annotation : annotations) {
            Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(annotation);

            ElementParser elementParser = new ElementParser(processingEnv, roundEnv);

            for (Element e : annotatedElements) {
                if (e instanceof TypeElement) {
                    TypeElement typeElement = (TypeElement) e;
                    String simpleClassName = typeElement.getSimpleName().toString();

                    System.out.println("MateuBuilderAnnotationProcessor running on " + simpleClassName);

                    try {
                        new MateuClassProcessor(path -> {
                            JavaFileObject builderFile = processingEnv.getFiler().createSourceFile(path);
                            PrintWriter out = new PrintWriter(builderFile.openWriter());
                            return out;
                        }).process(elementParser.parse(typeElement, null));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }

        return true;
    }

    private void setupProcessors() {
        ServiceLoader<FieldProcessor> loader = ServiceLoader
                .load(FieldProcessor.class, getClass().getClassLoader());
        for (FieldProcessor fieldProcessor : loader) {
            processors.add(fieldProcessor);
        }
        processors.sort(Comparator.comparingInt(FieldProcessor::getOrder));
    }

}
