package io.mateu.mdd.vaadinport.vaadin.components.fields;

import com.vaadin.data.*;
import com.vaadin.data.validator.BeanValidator;
import com.vaadin.server.UserError;
import com.vaadin.shared.ui.ErrorLevel;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import io.mateu.mdd.core.annotations.DataProvider;
import io.mateu.mdd.core.annotations.UseLinkToListView;
import io.mateu.mdd.core.annotations.UseRadioButtons;
import io.mateu.mdd.core.interfaces.AbstractStylist;
import io.mateu.mdd.core.reflection.FieldInterfaced;
import io.mateu.mdd.core.reflection.ReflectionHelper;
import io.mateu.mdd.core.util.Helper;
import io.mateu.mdd.vaadinport.vaadin.MyUI;
import io.mateu.mdd.vaadinport.vaadin.components.dataProviders.JPQLListDataProvider;
import io.mateu.mdd.core.data.MDDBinder;

import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JPAManyToOneFieldBuilder extends JPAFieldBuilder {


    public boolean isSupported(FieldInterfaced field) {
        return field.isAnnotationPresent(ManyToOne.class);
    }

    public void build(FieldInterfaced field, Object object, Layout container, MDDBinder binder, Map<HasValue, List<Validator>> validators, AbstractStylist stylist, Map<FieldInterfaced, Component> allFieldContainers) {

        Component tf = null;
        HasValue hv = null;


        if (field.isAnnotationPresent(UseLinkToListView.class)) {

            HorizontalLayout wrap = new HorizontalLayout();
            wrap.setSpacing(true);
            wrap.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);

            TextField l = new TextField();
            l.setEnabled(false);
            l.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
            wrap.addComponent(l);


            hv = new AbstractField() {

                Object v = null;

                @Override
                protected void doSetValue(Object o) {
                    v = o;
                    if (o != null) l.setValue(o.toString());
                    else l.setValue("");
                }

                @Override
                public Object getValue() {
                    return v;
                }
            };

            Button b = new Button("Select");
            b.addStyleName(ValoTheme.BUTTON_LINK);
            b.addClickListener(e -> MyUI.get().getNavegador().go(field.getName()));
            wrap.addComponent(b);
            container.addComponent(wrap);

            tf = wrap;

            l.setRequiredIndicatorVisible(field.isAnnotationPresent(NotNull.class));

        } else if (field.isAnnotationPresent(UseRadioButtons.class)) {

            RadioButtonGroup rbg;
            container.addComponent(tf = rbg = new RadioButtonGroup());

            hv = rbg;

            //AbstractBackendDataProvider
            //FetchItemsCallback
            //newItemProvider

            if (field.isAnnotationPresent(DataProvider.class)) {

                try {

                    DataProvider a = field.getAnnotation(DataProvider.class);

                    ((HasDataProvider)tf).setDataProvider(a.dataProvider().newInstance());

                    rbg.setItemCaptionGenerator(a.itemCaptionGenerator().newInstance());

                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

            } else {

                try {
                    Helper.notransact((em) -> rbg.setDataProvider(new JPQLListDataProvider(em, field.getType())));
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }

                FieldInterfaced fName = ReflectionHelper.getNameField(field.getType());
                if (fName != null) rbg.setItemCaptionGenerator((i) -> {
                    try {
                        return "" + ReflectionHelper.getValue(fName, i);
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    return "Error";
                });

            }

            rbg.setRequiredIndicatorVisible(field.isAnnotationPresent(NotNull.class));

        } else {

            ComboBox cb;
            container.addComponent(tf = cb = new ComboBox());

            if (allFieldContainers.size() == 0) cb.focus();

            hv = cb;


            //AbstractBackendDataProvider
            //FetchItemsCallback
            //newItemProvider

            if (field.isAnnotationPresent(DataProvider.class)) {

                try {

                    DataProvider a = field.getAnnotation(DataProvider.class);

                    cb.setDataProvider(a.dataProvider().newInstance());

                    cb.setItemCaptionGenerator(a.itemCaptionGenerator().newInstance());

                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

            } else {

                try {
                    Helper.notransact((em) -> cb.setDataProvider(new JPQLListDataProvider(em, field.getType())));
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }

                FieldInterfaced fName = ReflectionHelper.getNameField(field.getType());
                if (fName != null) cb.setItemCaptionGenerator((i) -> {
                    try {
                        return "" + ReflectionHelper.getValue(fName, i);
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    return "Error";
                });

            }

            cb.setRequiredIndicatorVisible(field.isAnnotationPresent(NotNull.class));

        }





        allFieldContainers.put(field, tf);

        tf.setCaption(Helper.capitalize(field.getName()));

         AbstractComponent c = (AbstractComponent) tf;

        validators.put(hv, new ArrayList<>());

        HasValue finalHv = hv;
        hv.addValueChangeListener(new HasValue.ValueChangeListener<String>() {
            @Override
            public void valueChange(HasValue.ValueChangeEvent<String> valueChangeEvent) {
                    ValidationResult result = null;
                for (Validator v : validators.get(finalHv)) {
                    result = v.apply(valueChangeEvent.getValue(), new ValueContext(c));
                    if (result.isError()) break;
                }
                if (result != null && result.isError()) {
                    c.setComponentError(new UserError(result.getErrorMessage()));
                } else {
                    c.setComponentError(null);
                }
            }
        });


        if (field.isAnnotationPresent(NotNull.class)) validators.get(tf).add(new Validator() {
            @Override
            public ValidationResult apply(Object o, ValueContext valueContext) {
                ValidationResult r = null;
                if (o == null) r = ValidationResult.create("Required field", ErrorLevel.ERROR);
                else r = ValidationResult.ok();
                return r;
            }

            @Override
            public Object apply(Object o, Object o2) {
                return null;
            }
        });

        BeanValidator bv = new BeanValidator(field.getDeclaringClass(), field.getName());

        validators.get(hv).add(new Validator() {

            @Override
            public ValidationResult apply(Object o, ValueContext valueContext) {
                return bv.apply(o, valueContext);
            }

            @Override
            public Object apply(Object o, Object o2) {
                return null;
            }
        });

        addValidators(validators.get(hv));

        /*
        tf.setDescription();
        tf.setPlaceholder();
        */

        bind(binder, hv, field);
    }

    public Object convert(String s) {
        return s;
    }

    public void addValidators(List<Validator> validators) {
    }

    protected void bind(MDDBinder binder, HasValue tf, FieldInterfaced field) {
        binder.bindManyToOne(tf, field.getName());
    }

}
