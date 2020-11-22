package com.proyecto.flowmanagement.ui.views.forms;

import com.proyecto.flowmanagement.backend.persistence.entity.Alternative;
import com.proyecto.flowmanagement.backend.persistence.entity.Guide;
import com.proyecto.flowmanagement.backend.persistence.entity.Operation;
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
            CreateTest();
            //binder.writeBean(guide);
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

    public void CreateTest(){

        Guide guideNew = new Guide();

        guideNew.setName(name.getValue());
        guideNew.setLabel(label.getValue());
        guideNew.setMainStep(mainStep.getValue());

        List<Step> steps = new LinkedList<Step>();

        Step stepOne = new Step();

        List<Alternative> stepAlternative = new LinkedList<Alternative>();
        List<Operation> stepOperation = new LinkedList<Operation>();

        //List<Alternative> guideAlternative = new LinkedList<Alternative>();
        List<Operation> guideOperation = new LinkedList<Operation>();

        //Alternative alternative1Guide = new Alternative();
        //Alternative alternative2Guide = new Alternative();

        Operation operation1Guide = new Operation();
        Operation operation2Guide = new Operation();

        //alternative1Guide.setLabel("Label Alternative 1 Guide");
        //alternative1Guide.setName("Name Alternative 1 Guide");
        //alternative2Guide.setLabel("Label Alternative 2 Guide");
        //alternative2Guide.setName("Name Alternative 2 Guide");
        //guideAlternative.add(alternative1Guide);
        //guideAlternative.add(alternative2Guide);

        operation1Guide.setLabel("Label Operation 1 Guide");
        operation1Guide.setName("Name Operation 1 Guide");
        operation2Guide.setLabel("Label Operation 2 Guide");
        operation2Guide.setName("Name Operation 2 Guide");

        guideOperation.add(operation1Guide);
        guideOperation.add(operation2Guide);

        Alternative alternative1Step1 = new Alternative();
        Alternative alternative2Step1 = new Alternative();

        Operation operation1Step1 = new Operation();
        Operation operation2Step1 = new Operation();

        alternative1Step1.setLabel("Label Alternative 1 Step 1");
        alternative1Step1.setName("Name Alternative 1 Step 1");
        alternative2Step1.setLabel("Label Alternative 2 Step 1");
        alternative2Step1.setName("Name Alternative 2 Step 1");

        stepAlternative.add(alternative1Step1);
        stepAlternative.add(alternative2Step1);
        stepOne.setAlternatives(stepAlternative);

        operation1Step1.setLabel("Label Operation 1 Step 1");
        operation1Step1.setName("Name Operation 1 Step 1");
        operation2Step1.setLabel("Label Operation 2 Step 1");
        operation2Step1.setName("Name Operation 2 Step 1");

        stepOperation.add(operation1Step1);
        stepOperation.add(operation2Step1);
        stepOne.setOperations(stepOperation);

        stepOne.setLabel("Label TEST 1");
        stepOne.setText("Name TEST 1");

        steps.add(stepOne);

        guideNew.setSteps(steps);

        guideService.add(guideNew);
    }
}