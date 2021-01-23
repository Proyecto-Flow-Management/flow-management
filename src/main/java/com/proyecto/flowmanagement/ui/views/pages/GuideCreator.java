package com.proyecto.flowmanagement.ui.views.pages;

import com.proyecto.flowmanagement.backend.persistence.entity.Guide;
import com.proyecto.flowmanagement.backend.persistence.entity.Operation;
import com.proyecto.flowmanagement.backend.persistence.entity.Step;
import com.proyecto.flowmanagement.ui.MainLayout;
import com.proyecto.flowmanagement.ui.views.grids.OperationGridForm;
import com.proyecto.flowmanagement.ui.views.panels.DetailsPanel;
import com.proyecto.flowmanagement.ui.views.panels.GuidePanel;
import com.proyecto.flowmanagement.ui.views.panels.OperationPanel;
import com.proyecto.flowmanagement.ui.views.panels.StepPanel;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.LinkedList;
import java.util.List;

@org.springframework.stereotype.Component
@Route(value = "CrearGuia", layout = MainLayout.class)
@PageTitle("Crear Guia | Flow Management")
public class GuideCreator extends VerticalLayout {

    HorizontalLayout detailsPanelLayout;
    HorizontalLayout stepPanelLayout;
    HorizontalLayout operationPanelLayout;
    HorizontalLayout guidePanelLayout;

    OperationGridForm operationGridForm;
    DetailsPanel detailsPanel;
    StepPanel stepPanel;
    GuidePanel guidePanel;

    public Button save = new Button("Guardar");
    public Button validar = new Button("Validar");

    List<Guide> guideList = new LinkedList<>();
    Guide actual;

    public GuideCreator()
    {
        actual = new Guide();

        guideList.add(actual);

        setSizeFull();
        
        configureActualGuidePanel();

        configureActions();

        configureElements();

        configureDetailsPanel();

        configureGuidePanel();

        configureStepPanel();

        configureOperatorPanel();

        configureInteractivitie();

        configureForm();
    }

    private void configureActions() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        validar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClickListener(buttonClickEvent -> guardarGuias());
    }

    private void guardarGuias() {
        actualizarGuiaActual();
        Guide firstGuide = guideList.get(0);
        guideList.remove(firstGuide);
        firstGuide.setGuides(guideList);
    }

    private void configureActualGuidePanel()
    {
        detailsPanelLayout = new HorizontalLayout();
        detailsPanelLayout.setWidthFull();
        detailsPanel = new DetailsPanel();
        detailsPanelLayout.add(detailsPanel);
        detailsPanelLayout.setVisible(false);

    }

    private void configureElements() {

    }

    private void configureGuidePanel() {
        guidePanelLayout = new HorizontalLayout();
        guidePanelLayout.setWidthFull();
        guidePanel = new GuidePanel();
        guidePanelLayout.add(guidePanel);
    }

    private void configureDetailsPanel() {
        detailsPanelLayout = new HorizontalLayout();
        detailsPanelLayout.setWidthFull();
        detailsPanel = new DetailsPanel();
        detailsPanelLayout.add(detailsPanel);
    }

    private void configureStepPanel() {
        stepPanelLayout = new HorizontalLayout();
        stepPanelLayout.setWidthFull();
        stepPanel = new StepPanel();
        stepPanelLayout.add(stepPanel);
    }

    private void configureOperatorPanel() {
        operationGridForm = new OperationGridForm();
        operationGridForm.setWidthFull();

        operationPanelLayout = new HorizontalLayout();
        operationPanelLayout.setWidthFull();

        operationPanelLayout.add(operationGridForm);
    }

    private void configureInteractivitie() {
        actualizacionDeDetalles();
        //Actualizar Steps
        // Alternativs
        //Actuaalizar resumen
    }

    private void configureForm() {
        add(detailsPanelLayout,detailsPanel,guidePanelLayout,stepPanelLayout,operationPanelLayout);
    }

    private void actualizacionDeDetalles() {
        stepPanel.stepGridForm.stepForm.save.addClickListener( buttonClickEvent ->  actualizarGuiaActual());
        stepPanel.stepGridForm.stepForm.delete.addClickListener( buttonClickEvent ->  actualizarGuiaActual());
        guidePanel.name.addValueChangeListener(addListener ->  actualizarGuiaActual());
    }

    private void actualizarGuiaActual()
    {
        // Elementos Guia
        Guide newGuide = new Guide();
        newGuide.setName(guidePanel.name.getValue());
        newGuide.setLabel(guidePanel.label.getValue());

        if(guidePanel.mainStep.getValue() != null)
            newGuide.setMainStep(guidePanel.mainStep.getValue());
        else
            newGuide.setMainStep("");

        //Elementos de Steps
        List<Step> stepList = stepPanel.stepGridForm.getSteps();
        newGuide.setSteps(stepList);

        //ElementosOperations
        List<Operation> operationsList = operationGridForm.getOperations();
        newGuide.setOperations(operationsList);

        int index = guideList.indexOf(actual);
        guideList.set(index,newGuide);

        detailsPanel.updateGridDetails(guideList);
    }

}





