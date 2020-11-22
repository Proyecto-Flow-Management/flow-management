package com.proyecto.flowmanagement.ui.views.forms;

import com.proyecto.flowmanagement.backend.persistence.entity.Step;
import com.proyecto.flowmanagement.ui.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@org.springframework.stereotype.Component
@Route(value = "EditStep", layout = MainLayout.class)
@PageTitle("EditStep | Flow Management")
public class EditStep extends VerticalLayout {

    private Step step;
    //private GuideServiceImpl guideService;

    TextField label = new TextField("Etiqueta");
    TextField text = new TextField("Instrucciones");

    Button save = new Button("Save");
    Button close = new Button("Cancel");

    Binder<Step> binder = new BeanValidationBinder<>(Step.class);

    public EditStep() {

        addClassName("step-form");

        //this.guideService = guideService;

        binder.bindInstanceFields(this);

        generateStyle();

        add(label,text,createButtonsLayout());

    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        close.addClickListener(event -> UI.getCurrent().navigate("GuideList"));

        save.addClickListener(click -> validateAndSave());

        binder.addStatusChangeListener(evt -> save.setEnabled(binder.isValid()));

        return new HorizontalLayout(save, close);
    }

    private void validateAndSave() {
        try {
            /*guide = guideService.getAll().get(0);
            guide.setLabel(label.getValue());
            guide.setName(name.getValue());
            guideService.update(guide);*/
            UI.getCurrent().navigate("StepList");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void generateStyle(){
        setAlignItems(FlexComponent.Alignment.CENTER);
        label.setWidth("60%");
        text.setWidth("60%");
    }
}
