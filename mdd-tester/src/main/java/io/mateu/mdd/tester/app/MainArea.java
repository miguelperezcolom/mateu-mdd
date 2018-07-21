package io.mateu.mdd.tester.app;

import io.mateu.mdd.core.app.AbstractAction;
import io.mateu.mdd.core.app.AbstractArea;
import io.mateu.mdd.core.app.AbstractModule;
import io.mateu.mdd.core.app.MDDAction;
import io.mateu.mdd.tester.model.entities.basic.BasicFieldsDemoEntity;

import java.util.Arrays;
import java.util.List;

public class MainArea extends AbstractArea {
    public MainArea() {
        super("Main area");
        defaultAction = new MDDAction("Welcome page", BasicFieldsDemoEntity.class);
    }

    @Override
    public List<AbstractModule> buildModules() {
        return Arrays.asList(new DeepMenusModule(), new MainModule());
    }

    @Override
    public boolean isPublicAccess() {
        return true;
    }

}