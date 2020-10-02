package io.mateu.mdd.vaadin.controllers.firstLevel;

import com.vaadin.ui.Component;
import io.mateu.mdd.core.ui.MDDUIAccessor;
import io.mateu.mdd.vaadin.controllers.BrokenLinkController;
import io.mateu.mdd.vaadin.controllers.Controller;
import io.mateu.mdd.vaadin.navigation.View;
import io.mateu.mdd.vaadin.navigation.ViewStack;
import io.mateu.util.notification.Notifier;

public class HomeController extends Controller {

    public HomeController(ViewStack stack) {
        try {
            if (MDDUIAccessor.getCurrentUser() != null) {
                new PrivateController(stack, "");
            } else {
                new PublicController(stack, "");
            }
        } catch (Throwable throwable) {
            Notifier.alert(throwable);
        }
    }

    @Override
    public void apply(ViewStack stack, String path, String step, String cleanStep, String remaining) throws Throwable {

        if (!"".equals(step)) {
            Controller controller = null;
            if ("private".equals(step)) {
                controller = new PrivateController(stack, path + "/" + step);
            } else if ("public".equals(step)) {
                controller = new PublicController(stack, path + "/" + step);
            } else {
                controller = new BrokenLinkController(stack, path + "/" + step);
            }

            controller.next(stack, path, step, remaining);
        }

    }
}
