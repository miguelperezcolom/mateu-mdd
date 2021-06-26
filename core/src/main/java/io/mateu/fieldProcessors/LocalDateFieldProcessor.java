package io.mateu.fieldProcessors;

import java.time.LocalDate;

import com.google.auto.service.AutoService;
import io.mateu.model.Field;

@AutoService(FieldProcessor.class)
public class LocalDateFieldProcessor implements FieldProcessor {
    @Override
    public int getOrder() {
        return 100;
    }

    @Override
    public boolean matches(Field field) {
        return LocalDate.class.getName().equals(field.getType().getClassName());
    }

    @Override
    public String writeCode(Field field) {
        String code = "        {\n" +
                "            DatePicker field;\n" +
                "            add(field = new DatePicker(\"" + field.getName() + "\"));\n" +
                "            field.setId(\"" + field.getName() + "\");\n" +
                "            binder.forField(field).bind(\"" + field.getName() + "\");\n" +
                "        }\n";
        return code;
    }
}
