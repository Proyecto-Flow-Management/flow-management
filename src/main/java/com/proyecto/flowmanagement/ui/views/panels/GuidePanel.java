package com.proyecto.flowmanagement.ui.views.panels;

import com.proyecto.flowmanagement.backend.persistence.entity.Guide;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import java.util.List;

public class GuidePanel  extends HorizontalLayout {

    Accordion accordion = new Accordion();
    FormLayout basicInformation = new FormLayout();
    HorizontalLayout basicHorizontal = new HorizontalLayout();

    public TextField name = new TextField("Name");
    public TextField label = new TextField("Label");
    public ComboBox<String> mainStep =new ComboBox<>("Empezar en:");
    
    public GuidePanel()
    {
        configureElements();
        
        configureForm();
    }

    private void configureElements() {
        this.name.setRequired(true);
        this.label.setRequired(true);
        this.label.setRequired(true);

        this.name.setErrorMessage("Campo obligatorio");
        this.label.setErrorMessage("Campo obligatorio");

        basicHorizontal.add(name,label,mainStep);
        basicInformation.add(basicHorizontal);
        accordion.add("Informacion Basica", basicInformation);
    }

    private void configureForm() {
        setSizeFull();
        accordion.setSizeFull();
        add(accordion);
    }

    private void setMainStep(List<String> stepsList)
    {
        this.mainStep.setItems(stepsList);
    }
}
