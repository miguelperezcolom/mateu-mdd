package io.mateu.fieldProcessors;

import com.google.auto.service.AutoService;
import io.mateu.model.Field;

@AutoService(FieldProcessor.class)
public class IntFieldProcessor implements FieldProcessor {
    @Override
    public int getOrder() {
        return 100;
    }

    @Override
    public boolean matches(Field field) {
        return int.class.getName().equals(field.getType().getClassName());
    }

    @Override
    public String writeCode(Field field) {
        String code = "        {\n" +
                "            IntegerField field;\n" +
                "            add(field = new IntegerField(\"" + field.getName() + "\"));\n" +
                "            field.setId(\"" + field.getName() + "\");\n" +
                "            binder.forField(field).bind(\"" + field.getName() + "\");\n" +
                "        }\n";
        return code;
    }
}
