package com.proyecto.flowmanagement.ui.views.panels;

import com.proyecto.flowmanagement.backend.persistence.entity.Guide;
import com.proyecto.flowmanagement.ui.views.grids.StepGridForm;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import java.util.List;

public class ActualGuidePanel extends HorizontalLayout {

    HorizontalLayout actualGuideLayout = new HorizontalLayout();
    Accordion accordion = new Accordion();
    FormLayout basicInformation = new FormLayout();
    public ComboBox<Guide> actualGuide =new ComboBox<>("Guia Actual");

    public ActualGuidePanel()
    {
        configureElements();
    }

    private void configureElements()
    {
        setWidthFull();
        basicInformation.setWidthFull();
        actualGuideLayout.setWidthFull();
        actualGuideLayout.setId("step-Layout");
        setWidthFull();
        basicInformation.add(actualGuide);
        accordion.close();
        accordion.add("Guia Actual", basicInformation);
        add(accordion);
    }

    public void setItems(List<Guide> guideList)
    {
        Guide actual = actualGuide.getValue();
        this.actualGuide.setItems(guideList);
        this.actualGuide.setValue(actual);
    }

}
