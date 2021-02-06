package com.proyecto.flowmanagement.ui.views.panels;

import com.proyecto.flowmanagement.ui.views.grids.StepGridForm;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.ui.Window;

@CssImport("./styles/step-panel.css")
public class StepPanel  extends HorizontalLayout {

    public StepGridForm stepGridForm = new StepGridForm();
    HorizontalLayout stepSecctionLayout = new HorizontalLayout();

    Accordion accordion = new Accordion();
    FormLayout basicInformation = new FormLayout();

    public StepPanel()
    {
        configureGrid();

        configureElements();

        configureForm();
    }

    private void configureGrid() {
        stepGridForm = new StepGridForm();
        stepSecctionLayout = new HorizontalLayout();
        stepSecctionLayout.add(stepGridForm);
    }

    private void configureElements() {
        basicInformation.setWidthFull();
        stepSecctionLayout.setWidthFull();
        stepSecctionLayout.setId("step-Layout");
        setWidthFull();
        basicInformation.add(stepSecctionLayout);
        accordion.close();
        accordion.add("Steps", basicInformation);
    }

    private void configureForm() {
        add(accordion);
    }
}
