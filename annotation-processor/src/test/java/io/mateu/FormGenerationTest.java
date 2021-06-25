package io.mateu;

import javax.tools.JavaFileObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;
import io.mateu.annotationProcessor.MateuBuilderAnnotationProcessor;
import io.mateu.annotations.MateuBuilder;
import io.mateu.components.FormComponent;
import org.junit.Test;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.Compiler.javac;

public class FormGenerationTest {

    @Test
    public void test1() throws IOException, URISyntaxException {
        URL source = getResourceURL("./src/test/java/",
                "io/mateu/modelo/MiRepo.java");
        Compilation compilation = javac()
                .withProcessors(new MateuBuilderAnnotationProcessor())
                .withClasspath(buildClassPath())
                .compile(JavaFileObjects.forResource(source));
        assertThat(compilation).succeededWithoutWarnings();

        for (JavaFileObject generatedFile : compilation.generatedFiles()) {
            String code = readFromInputStream(generatedFile.openInputStream());
            System.out.println(code);
        }

        for (JavaFileObject generatedFile : compilation.generatedSourceFiles()) {
            String code = readFromInputStream(generatedFile.openInputStream());
            System.out.println(code);
        }
    }

    private Iterable<File> buildClassPath() throws URISyntaxException {
        List<File> locations = new ArrayList<>();
        locations.add(new File(FormGenerationTest.class.getProtectionDomain().getCodeSource().getLocation().toURI()));
        locations.add(new File(MateuBuilder.class.getProtectionDomain().getCodeSource().getLocation().toURI()));
        locations.add(new File(FormComponent.class.getProtectionDomain().getCodeSource().getLocation().toURI()));
        locations.add(new File(HorizontalLayout.class.getProtectionDomain().getCodeSource().getLocation().toURI()));
        return locations;
    }

    private URL getResourceURL(String path, String uri) throws MalformedURLException {
        return new File(path + uri).toURI().toURL();
    }

    private String readFromInputStream(InputStream inputStream)
            throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br
                     = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }
}
