package com.proyecto.flowmanagement.ui.views.forms;

import com.proyecto.flowmanagement.backend.persistence.entity.Alternative;
import com.proyecto.flowmanagement.backend.persistence.entity.Step;
import com.proyecto.flowmanagement.ui.views.grids.AlternativeGridForm;
import com.proyecto.flowmanagement.ui.views.grids.DocumentsGridForm;
import com.proyecto.flowmanagement.ui.views.grids.OperationGridForm;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.shared.Registration;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@org.springframework.stereotype.Component
@PageTitle("CreateStep | Flow Management")
@CssImport("./styles/step-grid-form.css")
public class StepForm extends HorizontalLayout {

//    private Step step = new Step();
    private Step step;

    public AlternativeGridForm alternativeGridForm = new AlternativeGridForm();
    DocumentsGridForm documentsGridForm = new DocumentsGridForm();
    public OperationGridForm operationGridForm = new OperationGridForm();

    VerticalLayout form = new VerticalLayout();
    FormLayout elements = new FormLayout();
    FormLayout textLayout = new FormLayout();
    HorizontalLayout alternativeGridLayout = new HorizontalLayout();
    HorizontalLayout stepDocumentsLayout = new HorizontalLayout();
    HorizontalLayout operationsLayout = new HorizontalLayout();
    HorizontalLayout actionsLayout = new HorizontalLayout();
    TagsDesconocidosForm tagsDesconocidosForm = new TagsDesconocidosForm();
    HorizontalLayout tagsDesconocidosLayout = new HorizontalLayout();
    TextArea text = new TextArea("Text");
    TextField textId = new TextField("StepId");
    TextField label = new TextField("Label");

    public Button save = new Button("Guardar");
    public Button close = new Button("Cancelar");
    public Button delete = new Button("Eliminar");

    public boolean editing;
    public boolean esValido;

    public StepForm() {
        this.editing = false;
        this.esValido = false;

        setSizeFull();

        configureElements();

        configureTagsDesconocidos();

        configureAlternatives();

        configureDocuments();

        configureOperations();

        configureForm();

        agregarInteractividad();
    }

    private void configureTagsDesconocidos() {
        tagsDesconocidosForm.setWidthFull();
        tagsDesconocidosLayout.setWidthFull();
        tagsDesconocidosLayout.add(tagsDesconocidosForm);
    }

    private void configureForm() {
        form.add(elements,textLayout,
                tagsDesconocidosLayout,
                alternativeGridLayout,
                operationsLayout,
                stepDocumentsLayout,
                actionsLayout);

        add(form);
    }

    private void configureOperations() {
        operationsLayout.setWidthFull();
        operationsLayout.add(operationGridForm);
    }

    private void configureDocuments() {
        stepDocumentsLayout.setWidthFull();
        stepDocumentsLayout.add(documentsGridForm);
    }

    private void configureAlternatives() {
        alternativeGridForm.setWidthFull();
        alternativeGridLayout.setWidthFull();
        alternativeGridLayout.add(alternativeGridForm);
    }

    public void agregarInteractividad()
    {
        alternativeGridForm.alternativeForm.save.addClickListener(buttonClickEvent -> addToOperationForm());
        alternativeGridForm.alternativeForm.delete.addClickListener(buttonClickEvent -> addToOperationForm());
    }

    private void addToOperationForm() {
        List<String> ids = new LinkedList<>();

        if(this.alternativeGridForm.getAlternatives() != null)
        {
            for (Alternative alternative: this.alternativeGridForm.getAlternatives()) {
                if(alternative.getNextStep() != "")
                    ids.add(alternative.getNextStep());
            }
        }

        this.operationGridForm.updateAlternativesIds(ids);
    }

    private void configureElements() {
        addClassName("stepSection");
        delete.setVisible(false);

        this.text.setPlaceholder("Descripción de la guía...");

        elements.add(textId,label);
        elements.setResponsiveSteps(
                new FormLayout.ResponsiveStep("25em", 1),
                new FormLayout.ResponsiveStep("32em", 2));
        elements.setWidthFull();
        textLayout.add(text);
        textLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("25em", 1));
        textLayout.setWidthFull();
        actionsLayout.add(createButtonsLayout());
    }

    private Component createButtonsLayout() {

        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(click -> saveStep());

        return new HorizontalLayout(save, close, delete);
    }

    private void saveStep() {
          this.step = new Step();
          step.setText(text.getValue());
          step.setTextId(textId.getValue());
          step.setLabel(label.getValue());
          step.setAlternatives(alternativeGridForm.getAlternatives());
          step.setOperations(operationGridForm.getOperations());
          step.setStepDocuments(documentsGridForm.getDocuments());
          step.setTagsDesconocidos(this.tagsDesconocidosForm.desconocidosText.getValue());

          String validacionIncompleta = step.validacionIncompleta();

          if(!validacionIncompleta.isEmpty())
              MostrarMensajeError(validacionIncompleta);

          this.esValido = validacionIncompleta.isEmpty();
    }

    private void MostrarMensajeError(String validacionIncompleta) {
        Span mensaje = new Span(validacionIncompleta);
        Notification notification = new Notification(mensaje);
        notification.setDuration(3000);
        notification.setPosition(Notification.Position.MIDDLE);
        notification.open();
    }

    public void setStep(Step step) {
        this.step = step;
        this.label.setValue(step.getLabel());
        this.textId.setValue(step.getTextId());
        this.text.setValue(step.getText());
        this.tagsDesconocidosForm.desconocidosText.setValue(step.getTagsDesconocidos());
        this.alternativeGridForm.loadAlternative(step.getAlternatives());
        this.operationGridForm.loadOperations(step.getOperations());
        this.documentsGridForm.loadStepDocuments(step.getStepDocuments());
    }

    public Step getStep() {
        return this.step;
    }
    public Button getSaveButton() {
        return this.save;
    }

    public void setAsDefault() {
        this.label.clear();
        this.textId.clear();
        this.text.clear();
        this.alternativeGridForm.setAsDefault();
        this.operationGridForm.setAsDefault();
        this.documentsGridForm.setAsDefault();
    }
}