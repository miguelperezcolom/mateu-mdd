package io.mateu.fieldProcessors;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ServiceLoader;

import io.mateu.model.Field;

public class FieldProcessors {

    public static List<FieldProcessor> processors = new ArrayList<>();

    static {
        ServiceLoader<FieldProcessor> loader = ServiceLoader
                .load(FieldProcessor.class);
        for (FieldProcessor fieldProcessor : loader) {
            processors.add(fieldProcessor);
        }
        processors.sort(Comparator.comparingInt(FieldProcessor::getOrder));
    }

    public static String process(Field f) {
        FieldProcessor matched = null;
        for (FieldProcessor processor : processors) {
            if (processor.matches(f)) {
                matched = processor;
                break;
            }
        }
        String code = "No FieldProcessor matches " + f.getType().getClassName();
        if (matched != null) {
            code = matched.writeCode(f);
        }
        return code;
    }
}
