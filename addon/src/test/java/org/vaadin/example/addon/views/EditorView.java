package org.vaadin.example.addon.views;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import io.mateu.Mateu;
import org.vaadin.example.addon.MyBuilder;
import org.vaadin.example.addon.model.Person;

@Route("editor")
public class EditorView extends Div {

    public EditorView() {
        add(Mateu.getInstance(MyBuilder.class).getEditorForPerson(() -> new Person(), p -> System.out.println("saved")));
    }
}
