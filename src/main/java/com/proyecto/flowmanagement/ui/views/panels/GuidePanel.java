package com.proyecto.flowmanagement.ui.views.panels;

import com.proyecto.flowmanagement.backend.persistence.entity.Guide;
import com.proyecto.flowmanagement.backend.persistence.entity.Step;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

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

    public void actualizarAtributos(Guide guide)
    {
        this.mainStep.setItems(new LinkedList<>());

        if(guide.getSteps() != null )
        {
            List<String> steps = guide.getSteps().stream().map(m -> m.getTextId()).collect(Collectors.toList());
            if(guide.getMainStep()!= null && steps.contains(guide.getMainStep()))
                this.mainStep.setItems(steps);
        }

        if(guide.getName() !=null)
             this.name.setValue(guide.getName());
        else
            this.name.setValue("");


        if(guide.getLabel() !=null)
            this.label.setValue(guide.getLabel());
        else
            this.label.setValue("");
    }

    public void actualizarSteps(List<Step> stepList) {
        String actualValue = mainStep.getValue();

        List<String> steps = stepList.stream().map(m -> m.getTextId()).collect(Collectors.toList());

        this.mainStep.setItems(steps);

        if(stepList.contains(actualValue))
            mainStep.setValue(actualValue);

    }
}
