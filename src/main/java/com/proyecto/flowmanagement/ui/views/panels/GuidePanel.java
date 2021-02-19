package com.proyecto.flowmanagement.ui.views.panels;

import com.proyecto.flowmanagement.backend.persistence.entity.Guide;
import com.proyecto.flowmanagement.backend.persistence.entity.Step;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@CssImport("./styles/guide-panel-form.css")
public class GuidePanel  extends HorizontalLayout {

    Accordion accordion = new Accordion();
    HorizontalLayout basicHorizontal = new HorizontalLayout();
    Accordion desconocidosAccordion = new Accordion();
    TextArea desconocidosText = new TextArea("Desconocidos");
    public Button eliminarGuia = new Button("Eliminar");

    public TextField name = new TextField("Name");
    public TextField label = new TextField("Label");
    public ComboBox<String> mainStep =new ComboBox<>("Empezar en:");
    
    public GuidePanel()
    {
        configureElements();
        
        configureForm();
    }

    private void configureElements() {
        eliminarGuia.addThemeVariants(ButtonVariant.LUMO_ERROR);
        eliminarGuia.addClassName("delete-button");
        eliminarGuia.setVisible(false);

        basicHorizontal.add(name,label,mainStep,eliminarGuia);
        basicHorizontal.setVerticalComponentAlignment(Alignment.BASELINE, name);
        basicHorizontal.setVerticalComponentAlignment(Alignment.BASELINE, label);
        basicHorizontal.setVerticalComponentAlignment(Alignment.BASELINE, mainStep);
        basicHorizontal.setVerticalComponentAlignment(Alignment.BASELINE, eliminarGuia);
        basicHorizontal.setClassName("campos-layout");
        basicHorizontal.setWidthFull();
        desconocidosAccordion.setWidthFull();
        desconocidosAccordion.close();
        desconocidosAccordion.add("Componentes desconocidos", desconocidosText);
        desconocidosText.setMinWidth("60%");
        desconocidosText.setMaxWidth("80%");
        desconocidosText.setClassName("campos-layout");
        VerticalLayout layoutVertical = new VerticalLayout();
        layoutVertical.setWidthFull();
        layoutVertical.add(basicHorizontal,desconocidosAccordion);
        name.setMinWidth("25%");
        name.setMaxWidth("40%");
        label.setMinWidth("25%");
        label.setMaxWidth("40%");
//        accordion.add("Informacion Basica", basicHorizontal);
        accordion.add("Informacion Basica", layoutVertical);
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
        String mainStepAux = guide.getMainStep();
        if(mainStepAux == null)
            mainStepAux = "";

        this.mainStep.setItems(new LinkedList<>());

        if(guide.getSteps() != null )
        {
            guide.setMainStep(mainStepAux);
            List<String> steps = guide.getSteps().stream().map(m -> m.getTextId()).collect(Collectors.toList());
            if(guide.getMainStep()!= null && steps.contains(guide.getMainStep())) {
                this.mainStep.setItems(steps);
                this.mainStep.setValue(mainStepAux);
            }
            else if (guide.getMainStep()!= null) {
                if (guide.getMainStep().equals("") && steps.size()>0) {
                    this.mainStep.setItems(steps);
                }
            }
        }

        if(guide.getLabel() !=null)
            this.label.setValue(guide.getLabel());
        else
            this.label.setValue("");

        if(guide.getName() !=null)
            this.name.setValue(guide.getName());
        else
            this.name.setValue("");
    }
 
    public void actualizarSteps(List<Step> stepList) {
        String actualValue = mainStep.getValue();

        List<String> steps = stepList.stream().map(m -> m.getTextId()).collect(Collectors.toList());
        this.mainStep.setItems(steps);

        if(steps.contains(actualValue))
        {
            mainStep.setValue(actualValue);
        }
        else
            mainStep.setValue("");

    }
}
