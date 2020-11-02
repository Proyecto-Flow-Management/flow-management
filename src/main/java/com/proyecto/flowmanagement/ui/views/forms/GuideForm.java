package com.proyecto.flowmanagement.ui.views.forms;

import com.proyecto.flowmanagement.backend.persistence.entity.Guide;
import com.proyecto.flowmanagement.backend.persistence.entity.Step;
import com.proyecto.flowmanagement.backend.service.Impl.GuideServiceImpl;
import com.proyecto.flowmanagement.ui.MainLayout;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.Registration;

import java.util.LinkedList;
import java.util.List;

@org.springframework.stereotype.Component
@Route(value = "CreateGuide", layout = MainLayout.class)
@PageTitle("CreateGuide | Flow Management")
public class GuideForm extends VerticalLayout {
    private Guide guide;
    private GuideServiceImpl guideService;

    TextField name = new TextField("Nombre de la Guia");
    TextField label = new TextField("Etiqueta");
    TextField mainStep = new TextField("Paso principal");

    Button save = new Button("Save");
    Button close = new Button("Cancel");

    Binder<Guide> binder = new BeanValidationBinder<>(Guide.class);

    public GuideForm(List<Step> steps, GuideServiceImpl guideService) {
        addClassName("guide-form");
        this.guideService = guideService;

        binder.bindInstanceFields(this);
        generateStyle();

        add(name,label,mainStep, createButtonsLayout());
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
            Guide guideNew = new Guide();

            guideNew.setName(name.getValue());
            guideNew.setLabel(label.getLabel());

            Step stepOne = new Step();
            stepOne.setLabel("Label TEST 1");
            stepOne.setName("Name TEST 1");

            Step stepTwo = new Step();
            stepTwo.setLabel("Label TEST 2");
            stepTwo.setName("Name TEST 2");

            List<Step> stepList = new LinkedList<Step>();
            stepList.add(stepOne);
            stepList.add(stepTwo);

            guideNew.setSteps(stepList);

            guideService.add(guideNew);

            UI.getCurrent().navigate("GuideList");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void generateStyle(){
        setAlignItems(Alignment.CENTER);
        name.setWidth("60%");
        label.setWidth("60%");
        mainStep.setWidth("60%");
    }
}