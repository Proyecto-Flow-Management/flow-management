package com.proyecto.flowmanagement.ui.views.forms;
import com.proyecto.flowmanagement.backend.persistence.entity.Guide;
import com.proyecto.flowmanagement.backend.persistence.entity.Step;
import com.proyecto.flowmanagement.backend.persistence.entity.User;
import com.proyecto.flowmanagement.backend.service.Impl.GuideServiceImpl;
import com.proyecto.flowmanagement.ui.MainLayout;

import com.proyecto.flowmanagement.ui.views.grids.OperationGridForm;
import com.proyecto.flowmanagement.ui.views.grids.StepGridForm;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Section;
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
        add(guideLayout,stepSecctionLayout,operationSecctionLayout);
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
        name = new TextField("Nombre");
        label = new TextField("Etiqueta");
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