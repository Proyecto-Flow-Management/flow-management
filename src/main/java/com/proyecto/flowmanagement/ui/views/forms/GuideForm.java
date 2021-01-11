package com.proyecto.flowmanagement.ui.views.forms;
import com.proyecto.flowmanagement.backend.persistence.entity.Guide;
import com.proyecto.flowmanagement.backend.persistence.entity.Step;
import com.proyecto.flowmanagement.backend.persistence.entity.User;
import com.proyecto.flowmanagement.backend.service.Impl.GuideServiceImpl;
import com.proyecto.flowmanagement.ui.MainLayout;

import com.proyecto.flowmanagement.ui.views.grids.OperationGridForm;
import com.proyecto.flowmanagement.ui.views.grids.StepGridForm;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@org.springframework.stereotype.Component
@Route(value = "CreateGuide", layout = MainLayout.class)
@PageTitle("CreateGuide | Flow Management")
public class GuideForm extends VerticalLayout {


    HorizontalLayout guideLayout;
    HorizontalLayout stepSecctionLayout;
    HorizontalLayout operationSecctionLayout;
    HorizontalLayout actionsLayout;

    Button crearGuia;

    HorizontalLayout layoutGuide;
    TextField name;
    TextField label;

    StepGridForm stepGridForm;
    OperationGridForm operationGridForm;

    public GuideForm(GuideServiceImpl guideService)
    {
        configureGuideElements();

        configureStepElements();

        configureOperationElements();
        
        configureDocumentsElements();

        createForm();
}

    private void configureDocumentsElements() {
    }

    private void configureOperationElements() {
        operationGridForm = new OperationGridForm();
        operationGridForm.setWidthFull();

        operationSecctionLayout = new HorizontalLayout();
        operationSecctionLayout.setWidthFull();

        operationSecctionLayout.add(operationGridForm);
    }

    private void createForm() {
        crearGuia = new Button("Crear Guia");
        crearGuia.addClickListener(buttonClickEvent -> crearGuia());
        HorizontalLayout botonCrear = new HorizontalLayout();

        Span content = new Span("Guia creada y archivo XML generado!");
        Notification notification = new Notification(content);
        notification.setDuration(3000);
        notification.setPosition(Notification.Position.MIDDLE);

        Button button = new Button("Crear Guia");
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        button.addClickListener(event -> notification.open());

        botonCrear.add(button);
        add(guideLayout,stepSecctionLayout,operationSecctionLayout,botonCrear);
    }


    private void crearGuia() {

        Guide newGuide = new Guide();

        newGuide.setName(name.getValue());
        newGuide.setLabel(label.getValue());

        newGuide.setSteps(stepGridForm.getSteps());
        newGuide.setOperations(operationGridForm.getOperations());

        GuideServiceImpl guideService = new GuideServiceImpl();
        guideService.add(newGuide);
    }

    private void configureGuideElements() {
        H1 guidelabel = new H1("Elementos de Guia");
        guidelabel.addClassName("logo");
        add(guidelabel);

        guideLayout = new HorizontalLayout();
        name = new TextField("Nombre Guia");
        label = new TextField("Label Guia");
        guideLayout.add(name,label);
    }

    private void configureStepElements() {
        stepGridForm = new StepGridForm();
        stepGridForm.setWidthFull();
        stepSecctionLayout = new HorizontalLayout();
        stepSecctionLayout.setWidthFull();
        stepSecctionLayout.add(stepGridForm);
    }


}