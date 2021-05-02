package io.mateu.mdd.annotationProcessor;

import com.google.auto.service.AutoService;
import com.google.common.base.Strings;
import com.sun.tools.javac.code.Symbol;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

@SupportedAnnotationTypes({"io.mateu.mdd.annotations.MateuBuilder"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class MateuBuilderAnnotationProcessor  extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (TypeElement annotation : annotations) {
            Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(annotation);

            for (Element e : annotatedElements) {
                TypeElement typeElement = (TypeElement) e;
                String className = typeElement.getQualifiedName().toString();
                String simpleClassName = typeElement.getSimpleName().toString();

                System.out.println("MateuBuilderAnnotationProcessor running on " + simpleClassName);

                String generatedFullClassName = className + "Impl";
                String pkgName = generatedFullClassName.substring(0, generatedFullClassName.lastIndexOf("."));
                String generatedClassName = generatedFullClassName.substring(generatedFullClassName.lastIndexOf(".") + 1);

                JavaFileObject builderFile = null;
                try {
                    builderFile = processingEnv.getFiler().createSourceFile(generatedFullClassName);
                    try (PrintWriter out = new PrintWriter(builderFile.openWriter())) {
                        // writing generated file to out …

                        out.println("package " + pkgName + ";");
                        out.println("import " + className + ";");

                        out.println("");


                        out.println();

                        out.println("public class " + generatedClassName + " implements " + simpleClassName + " {");

                        for (Element enclosedElement : typeElement.getEnclosedElements()) {
                            System.out.println(enclosedElement.getClass().getName());
                            if (enclosedElement instanceof Symbol.MethodSymbol) {
                                Symbol.MethodSymbol methodSymbol = (Symbol.MethodSymbol) enclosedElement;

                                out.println("  public " + methodSymbol.getReturnType().toString() + " " + methodSymbol.getSimpleName() + "() {");
                                out.println("    return null;");
                                out.println("  }");

                            }
                        }

                        out.println("");
                        out.println("}");
                    }



                } catch (IOException ex) {
                    ex.printStackTrace();
                }


            }
        }

        return true;
    }
}
