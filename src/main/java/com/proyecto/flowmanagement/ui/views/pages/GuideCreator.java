package com.proyecto.flowmanagement.ui.views.pages;

import com.proyecto.flowmanagement.backend.persistence.entity.Alternative;
import com.proyecto.flowmanagement.backend.persistence.entity.Guide;
import com.proyecto.flowmanagement.backend.persistence.entity.Operation;
import com.proyecto.flowmanagement.backend.persistence.entity.Step;
import com.proyecto.flowmanagement.backend.service.Impl.GuideServiceImpl;
import com.proyecto.flowmanagement.backend.service.Impl.ValidationServiceImpl;
import com.proyecto.flowmanagement.ui.MainLayout;
import com.proyecto.flowmanagement.ui.views.grids.OperationGridForm;
import com.proyecto.flowmanagement.ui.views.panels.*;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@org.springframework.stereotype.Component
@Route(value = "CrearGuia", layout = MainLayout.class)
@PageTitle("Crear Guia | Flow Management")
public class GuideCreator extends VerticalLayout {

    HorizontalLayout actualPanelLaayout;
    HorizontalLayout detailsPanelLayout;
    HorizontalLayout stepPanelLayout;
    HorizontalLayout operationPanelLayout;
    HorizontalLayout guidePanelLayout;
    HorizontalLayout actionsLayout;

    ActualGuidePanel actualGuidePanel;
    OperationGridForm operationGridForm;
    DetailsPanel detailsPanel;
    StepPanel stepPanel;
    GuidePanel guidePanel;
    GuideServiceImpl servicio = new GuideServiceImpl();

    public Button save = new Button("Guardar");
    public Button validar = new Button("Validar");

    Guide raiz;
    Guide editado;

    public GuideCreator()
    {
        setSizeFull();
        
        configureActualGuidePanel();

        configureActions();

        configureElements();

        configureDetailsPanel();

        configureGuidePanel();

        configureStepPanel();

        configureOperatorPanel();

        configureActions();

        configureInteractivitie();

        configureForm();
    }

    private void configureActions() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        validar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClickListener(buttonClickEvent -> guardarGuias());
        validar.addClickListener(buttonClickEvent -> validarGuia());

        actionsLayout = new HorizontalLayout();
        actionsLayout.add(save,validar);
    }


    private void guardarGuias()
    {
        actualizarGuiaActual();
        servicio.add(raiz);
    }

    private void configureActualGuidePanel()
    {
        actualPanelLaayout = new HorizontalLayout();
        actualPanelLaayout.setWidthFull();
        actualGuidePanel = new ActualGuidePanel();
        actualPanelLaayout.add(actualGuidePanel);
        actualPanelLaayout.setVisible(false);
    }

    private void configureElements() {
        editado = new Guide();
        raiz = new Guide();
        raiz.editing = true;
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
        actualizacionAlternativs();
        actualizacionCrecionStep();
        actualizacionEliminarStep();
        actualizacionConditionsAlternatives();
        cambioGuia();
        //Actualizar Steps
        // Alternativs
        //Actuaalizar resumen
    }

    private void actualizacionConditionsAlternatives() {
        stepPanel.stepGridForm.stepForm.alternativeGridForm.alternativeForm.conditionForm.unaryConditionForm.save.addClickListener(buttonClickEvent -> actualizarGuiaActual());
        stepPanel.stepGridForm.stepForm.alternativeGridForm.alternativeForm.conditionForm.binaryConditionForm.save.addClickListener(buttonClickEvent -> actualizarGuiaActual());
        stepPanel.stepGridForm.stepForm.alternativeGridForm.alternativeForm.conditionForm.eliminarBinary.addClickListener(buttonClickEvent -> actualizarGuiaActual());
        stepPanel.stepGridForm.stepForm.alternativeGridForm.alternativeForm.conditionForm.eliminarUnary.addClickListener(buttonClickEvent -> actualizarGuiaActual());
    }

    private void actualizacionEliminarStep() {
        stepPanel.stepGridForm.stepForm.delete.addClickListener(buttonClickEvent -> cargarMainStepsPorEliminacion());
    }

    private void actualizacionCrecionStep()
    {
        stepPanel.stepGridForm.stepForm.save.addClickListener(buttonClickEvent -> cargarMainSteps());
    }

    private void cargarMainSteps() {

        if(stepPanel.stepGridForm.stepForm.esValido)
        {
            guidePanel.actualizarSteps(stepPanel.stepGridForm.stepList);
        }
    }

    private void cargarMainStepsPorEliminacion() {
        guidePanel.actualizarSteps(stepPanel.stepGridForm.stepList);
    }

    private void cambioGuia()
    {
        actualGuidePanel.actualGuide.addValueChangeListener(event -> cargarGuia());
    }

    private void cargarGuia() {
        Guide valor = actualGuidePanel.actualGuide.getValue();

        raiz.quitarEdicion();

        if(valor == raiz)
            raiz.editing = true;
        else
            raiz.setearParaEditar(valor);

        if(valor != null)
        {
            editado = new Guide();
            cargarValorGrilla(valor);
        }

        editado.editing = true;
    }

    private void cargarValorGrilla(Guide guide)
    {
        actualizarGuiaActual();

        this.stepPanel.stepGridForm.stepList = guide.getSteps();
        this.stepPanel.stepGridForm.updateGrid();

        this.operationGridForm.operationList = guide.getOperations();
        this.operationGridForm.updateGrid();

        this.guidePanel.actualizarAtributos(guide);
        this.guidePanel.name.setValue(guide.getName());
    }

    private void actualizacionAlternativs()
    {
        stepPanel.stepGridForm.stepForm.alternativeGridForm.alternativeForm.save.addClickListener(buttonClickEvent -> nuevoStep());
    }

    private void nuevoStep() {

        if(stepPanel.stepGridForm.stepForm.alternativeGridForm.alternativeForm.isValid)
        {
            Alternative myAlternative = stepPanel.stepGridForm.stepForm.alternativeGridForm.alternativeForm.getAlternative();

            if(myAlternative.getNewStep())
            {
                List<Step> stepsActuales = stepPanel.stepGridForm.getSteps();

                boolean noExiste = stepsActuales.stream().filter(aux -> aux.getTextId().equals(myAlternative.getNextStep())).count() == 0;

                if(noExiste)
                {
                    Step nuevo = new Step();
                    nuevo.setText(myAlternative.getNextStep());
                    nuevo.setLabel(myAlternative.getNextStep());
                    nuevo.setTextId(myAlternative.getNextStep());
                    this.stepPanel.stepGridForm.stepList.add(nuevo);
                    this.stepPanel.stepGridForm.updateGrid();
                    actualizarGuiaActual();
                }
            }
            else if( myAlternative.getGuideName() != null ||  !myAlternative.getGuideName().isEmpty())
            {
                Guide newGuide = new Guide();
                newGuide.setName(myAlternative.getGuideName());

                if(editado.getGuides()== null)
                    editado.setGuides(new LinkedList<>());

                editado.addGuide(newGuide);

                actualizarGuiaActual();
            }
        }
    }

    private void actualizarListaDeGuias(Guide actual)
    {
        if(actual.getGuides().size() > 1)
        {
            actualGuidePanel.setVisible(true);
            actualGuidePanel.setItems(actual.getGuides());
            actualGuidePanel.actualGuide.setValue(actual);
        }
        else
        {
            actualGuidePanel.setVisible(false);
        }
    }

    private void configureForm() {
        add(actualPanelLaayout,detailsPanelLayout,detailsPanel,guidePanelLayout,stepPanelLayout,operationPanelLayout,actionsLayout);
    }

    private void actualizacionDeDetalles() {
        stepPanel.stepGridForm.stepForm.save.addClickListener( buttonClickEvent ->  actualizarGuiaActual());
        stepPanel.stepGridForm.stepForm.delete.addClickListener( buttonClickEvent ->  actualizarGuiaActual());
        guidePanel.name.addValueChangeListener(addListener ->  actualizarGuiaActual());
    }

    private void actualizarGuiaActual()
    {
        editado.setName(guidePanel.name.getValue());
        editado.setLabel(guidePanel.label.getValue());

        if(guidePanel.mainStep.getValue() != null)
            editado.setMainStep(guidePanel.mainStep.getValue());
        else
            editado.setMainStep("");

        //Elementos de Steps
        List<Step> stepList = stepPanel.stepGridForm.getSteps();
        editado.setSteps(stepList);

        //ElementosOperations
        List<Operation> operationsList = operationGridForm.getOperations();
        editado.setOperations(operationsList);

        if(raiz.editing)
            raiz = editado;
        else
         raiz.setGuide(editado);

        detailsPanel.updateGridDetails(raiz);

        if(raiz.getGuides() != null && raiz.getGuides().size() > 0)
            mostrarOpcionesDeGuias();
        else
            actualPanelLaayout.setVisible(false);
    }

    private void mostrarOpcionesDeGuias()
    {
        actualPanelLaayout.setVisible(true);
        actualGuidePanel.actualizarItems(raiz);
    }

    private boolean validarGuia()
    {
        actualizarGuiaActual();
        ValidationServiceImpl servicioValidacion = new ValidationServiceImpl();
        List<String> mensajes = servicioValidacion.validarGuia(raiz);
        return true;
    }

    private void mostrarMensajeError(String mensajeValidacion) {
        Span mensaje = new Span(mensajeValidacion);
        Notification notification = new Notification(mensaje);
        notification.setDuration(3000);
        notification.setPosition(Notification.Position.MIDDLE);
        notification.open();
    }
}





