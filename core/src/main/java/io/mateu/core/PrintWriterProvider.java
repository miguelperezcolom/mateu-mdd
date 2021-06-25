package io.mateu.core;

import java.io.IOException;
import java.io.PrintWriter;

@FunctionalInterface
public interface PrintWriterProvider {

    PrintWriter create(String path) throws IOException;

}
