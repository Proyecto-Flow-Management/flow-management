package com.proyecto.flowmanagement.ui.views.panels;

import com.proyecto.flowmanagement.ui.views.grids.OperationGridForm;
import com.proyecto.flowmanagement.ui.views.grids.StepGridForm;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

@CssImport("./styles/operation-panel.css")
public class OperationPanel extends HorizontalLayout {

    public OperationGridForm operationGridForm = new OperationGridForm();
    HorizontalLayout operationSectionLayout = new HorizontalLayout();

    Accordion accordion = new Accordion();
    FormLayout basicInformation = new FormLayout();

    public OperationPanel()
    {
        configureGrid();

        configureElements();

        configureForm();
    }

    private void configureGrid() {
        operationGridForm = new OperationGridForm();
        operationGridForm.setWidthFull();
        operationGridForm = new OperationGridForm();
        operationSectionLayout = new HorizontalLayout();
        operationSectionLayout.add(operationGridForm);
    }

    private void configureElements() {
        basicInformation.setWidthFull();
        operationSectionLayout.setWidthFull();
        operationSectionLayout.setId("step-Layout");
        setWidthFull();
        basicInformation.add(operationSectionLayout);
        accordion.close();
        accordion.add("Operations", basicInformation);
    }

    private void configureForm() {
        add(accordion);
    }
}
