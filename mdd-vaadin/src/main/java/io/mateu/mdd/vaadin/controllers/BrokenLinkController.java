package io.mateu.mdd.vaadin.controllers;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Label;
import io.mateu.mdd.vaadin.components.ComponentWrapper;
import io.mateu.mdd.vaadin.navigation.ViewStack;

public class BrokenLinkController extends Controller {
    public BrokenLinkController(ViewStack stack, String path) {
        super();
        register(stack, path, new ComponentWrapper(VaadinIcons.BUG, "Broken link", new Label("<h1>No route to " + path + "</h1>", ContentMode.HTML), false));
    }
}
