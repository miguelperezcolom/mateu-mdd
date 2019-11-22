package io.mateu.mdd.vaadinport.vaadin.components.fieldBuilders;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.vaadin.data.*;
import com.vaadin.server.UserError;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import io.mateu.mdd.core.CSS;
import io.mateu.mdd.core.MDD;
import io.mateu.mdd.core.app.AbstractAction;
import io.mateu.mdd.core.data.MDDBinder;
import io.mateu.mdd.core.data.Pair;
import io.mateu.mdd.core.interfaces.AbstractStylist;
import io.mateu.mdd.core.reflection.FieldInterfaced;
import io.mateu.mdd.core.reflection.ReflectionHelper;
import io.mateu.mdd.vaadinport.vaadin.components.app.AbstractMDDExecutionContext;
import io.mateu.mdd.vaadinport.vaadin.components.oldviews.AbstractViewComponent;
import io.mateu.mdd.vaadinport.vaadin.components.oldviews.EditorViewComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class AbstractFieldBuilder {

    public static List<AbstractFieldBuilder> builders = Lists.newArrayList(
            new KPIInlineFieldBuilder()
            , new FareValueFieldBuilder()
            , new JPAUnmodifiableFieldBuilder()
            , new FromDataProviderFieldBuilder()
            , new JPAAuditFieldBuilder()
            , new JPAWizardFieldBuilder()
            , new JPAWeekDaysFieldBuilder()
            , new JPAIFrameFieldBuilder()
            , new JPAURLFieldBuilder()
            , new JPAFileFieldBuilder()
            , new JPALiteralFieldBuilder()
            , new JPASignatureFieldBuilder()
            , new JPACodeFieldBuilder()
            , new JPAHtmlFieldBuilder()
            , new JPAPrimitiveArraysFieldBuilder()
            , new JPAPrimitiveCollectionsFieldBuilder()
            , new JPAFastMoneyFieldBuilder()
            , new JPAMoneyFieldBuilder()
            , new JPATextAreaFieldBuilder()
            , new JPAStringFieldBuilder()
            , new JPATimeFieldBuilder()
            , new JPAIntegerFieldBuilder()
            , new JPALongFieldBuilder()
            , new JPADoubleFieldBuilder()
            , new JPABooleanFieldBuilder()
            , new JPAEnumerationFieldBuilder()
            , new JPAOneToOneFieldBuilder()
            , new JPAManyToOneFieldBuilder()
            , new JPAOneToManyFieldBuilder()
            , new JPADateFieldBuilder()
            , new JPALocalDateFieldBuilder()
            , new JPALocalDateTimeFieldBuilder()
            , new ComponentFieldBuilder()
            , new JPAPOJOFieldBuilder()
    );

    public abstract boolean isSupported(FieldInterfaced field);

    public abstract Component build(FieldInterfaced field, Object object, Layout container, MDDBinder binder, Map<HasValue, List<Validator>> validators, AbstractStylist stylist, Map<FieldInterfaced, Component> allFieldContainers, boolean forSearchFilter, Map<String, List<AbstractAction>> attachedActions);


    public static void applyStyles(AbstractStylist stylist, Object model, Map<FieldInterfaced, Component> containers, Pair<Map<FieldInterfaced, List<String>>, Map<FieldInterfaced, List<String>>> styleChanges) {
        Map<FieldInterfaced, List<String>> remove = styleChanges.getKey();
        Map<FieldInterfaced, List<String>> add = styleChanges.getValue();

        remove.keySet().forEach((f) -> {
            if (containers.containsKey(f)) containers.get(f).removeStyleNames(remove.get(f).toArray(new String[0]));
        });
        add.keySet().forEach((f) -> {
            if (containers.containsKey(f)) containers.get(f).addStyleNames(add.get(f).toArray(new String[0]));
        });

        if (model != null) for (FieldInterfaced f : containers.keySet()) {
            Component c = containers.get(f);
            if (c != null) {
                boolean v = stylist.isVisible(f, model);
                c.setVisible(v);
                if (c.getParent() instanceof HorizontalLayout) {
                    final boolean[] algunoVisible = {false};
                    ((Layout) c.getParent()).getComponentIterator().forEachRemaining(x -> algunoVisible[0] |= x.isVisible());
                    c.getParent().setVisible(algunoVisible[0]);
                }
            }
        }
    }

    public void addErrorHandler(AbstractComponent tf) {
        tf.setErrorHandler(e -> {
            Throwable th = e.getThrowable();
            while (th != null && th.getCause() != null) {
                th = th.getCause();
            }
            e.setThrowable(th);
            tf.setComponentError(new UserError(th.getMessage()));
        });
    }

    public static Binder.Binding completeBinding(Binder.BindingBuilder aux, MDDBinder binder, FieldInterfaced field) {
        return completeBinding(aux, binder, field, null);
    }

    public static Binder.Binding completeBinding(Binder.BindingBuilder aux, MDDBinder binder, FieldInterfaced field, AbstractComponent captionOwner) {
        aux.withValidator(new Validator() {

            boolean initialized = false;
            Object lastValue = null;
            ValidationResult lastResult;

            @Override
            public ValidationResult apply(Object v, ValueContext valueContext) {
                if (valueContext.getHasValue().isPresent()) {
                    if (initialized && (lastValue == v || (v != null && v.equals(lastValue)))) {
                    } else {
                        if (!initialized || lastValue != v || (v != null && !v.equals(lastValue))) {
                            initialized = true;
                            try {
                                Object old = ReflectionHelper.getValue(field, binder.getBean());
                                if (old != v || (v != null && !v.equals(old))) {
                                    ReflectionHelper.setValue(field, binder.getBean(), v);
                                }
                                lastValue = v;
                                return lastResult = ValidationResult.ok();
                            } catch (Exception e) {
                                Throwable th = e;
                                while (th != null && th.getCause() != null) {
                                    th = th.getCause();
                                }
                                th.printStackTrace();
                                return lastResult = ValidationResult.error(th.getMessage() != null?th.getMessage():th.getClass().getSimpleName());
                            }
                        }
                    }
                    return lastResult != null?lastResult:(lastResult = ValidationResult.ok());
                } else return ValidationResult.error("missing HasValue");
            }

            @Override
            public Object apply(Object o, Object o2) {
                return ValidationResult.ok();
            }
        });
        if (captionOwner != null) {
            aux.withValidationStatusHandler(s -> {
               if (s.isError()) captionOwner.setComponentError(new UserError(s.getMessage().orElse("Error")));
               else captionOwner.setComponentError(null);
            });
        }
        Binder.Binding binding = aux.bind(o -> ReflectionHelper.getValue(field, o, String.class.equals(field.getType())?"":null), (o, v) -> {
            /*
            try {
                ReflectionHelper.setValue(field, o, v);
                tf.setComponentError(null);
            } catch (Exception e) {
                Throwable th = e;
                while (th != null && th.getCause() != null) {
                    th = th.getCause();
                }

                BindingValidationStatus<?> status = new BindingValidationStatus<Object>(Result.error(th.getMessage()), (Binder.Binding<?, Object>) tf.getData());
                binder.getValidationStatusHandler().statusChange(new BinderValidationStatus(binder, Arrays.asList(status), Collections.emptyList()));
            }
            */
            binder.update(o); // entra en bucle!!!
        });
        return binding;
    }

    public static void completeBinding(HasValue hv, MDDBinder binder, FieldInterfaced field) {
        Binder.BindingBuilder aux = binder.forField(hv);
        completeBinding(aux, binder, field);
    }

    public void addComponent(Layout container, Component c, List<AbstractAction> attachedActions) {
        if (attachedActions == null || attachedActions.size() == 0) container.addComponent(c);
        else {
            VerticalLayout vl = null;
            if (container instanceof VerticalLayout && container.getComponentCount() == 2 && ((VerticalLayout) container).getComponent(1) instanceof HorizontalLayout) {
                crearBotonera(attachedActions, (Layout) ((VerticalLayout) container).getComponent(1));
            } else {
                vl = new VerticalLayout(c, crearBotonera(attachedActions));
                vl.addStyleName(CSS.NOPADDING);
            }
            vl.addStyleName("contenedorbotoneracampo");
            vl.addStyleName("conbotonera");
            container.addComponent(vl);
        }
    }

    private Component crearBotonera(List<AbstractAction> attachedActions) {
        HorizontalLayout hl = new HorizontalLayout();
        hl.addStyleName(CSS.NOPADDING);
        hl.addStyleName("botoneracampo");
        return crearBotonera(attachedActions, hl);
    }

    private Component crearBotonera(List<AbstractAction> attachedActions, Layout hl) {
        for (AbstractAction a : attachedActions) {
            Component i = null;
            if (true) {
                Button b;
                i = b = new Button(a.getName(), a.getIcon());
                b.addStyleName(ValoTheme.BUTTON_QUIET);
                b.addStyleName(ValoTheme.BUTTON_TINY);
                b.addClickListener(e -> {
                    try {

                        Runnable r = new Runnable() {
                            @Override
                            public void run() {

                                /*
                                boolean needsValidation = AbstractViewComponent.this instanceof EditorViewComponent && a.isValidationNeeded();

                                if (!needsValidation || ((EditorViewComponent)AbstractViewComponent.this).validate()) {

                                    a.run(new AbstractMDDExecutionContext());

                                    if (AbstractViewComponent.this instanceof EditorViewComponent) {

                                        EditorViewComponent evc = (EditorViewComponent) AbstractViewComponent.this;

                                        evc.getBinder().update(evc.getModel());

                                    }

                                }
                                 */
                                a.run(new AbstractMDDExecutionContext());
                            }
                        };

                        if (!Strings.isNullOrEmpty(a.getConfirmationMessage())) {
                            MDD.confirm(a.getConfirmationMessage(), new Runnable() {
                                @Override
                                public void run() {

                                    r.run();

                                    //todo: actualizar vista con los cambios en el modelo

                                }
                            });
                        } else r.run();

                    } catch (Throwable throwable) {
                        MDD.alert(throwable);
                    }
                });

                /*
                if (!Strings.isNullOrEmpty(a.getGroup())) menuItemsByGroup.computeIfAbsent(a.getGroup(), k -> new ArrayList<>()).add(i);
                addMenuItem(a.getId(), i);

                 */

                if (!Strings.isNullOrEmpty(a.getStyle())) i.addStyleName(a.getStyle());

                if (Strings.isNullOrEmpty(a.getGroup())) hl.addComponent(i);

            }
            if (i != null && !Strings.isNullOrEmpty(a.getStyle())) i.addStyleName(a.getStyle());
            i.setVisible(true);
        }
        return hl;
    }
}
