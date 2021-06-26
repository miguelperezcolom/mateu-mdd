package io.mateu.modelo;

import io.mateu.annotations.MateuBuilder;
import io.mateu.components.FormComponent;

@MateuBuilder
public interface MiRepo {

    FormComponent<MiFormulario> getFormulario();

}
