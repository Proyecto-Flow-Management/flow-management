package com.proyecto.flowmanagement.ui.views.forms;

import com.proyecto.flowmanagement.backend.persistence.entity.StepDocument;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

@CssImport("./styles/step-document-grid-form.css")
public class DocumentsForm extends VerticalLayout {
    private StepDocument stepDocument;

    VerticalLayout form = new VerticalLayout();
    HorizontalLayout elements = new HorizontalLayout();
    HorizontalLayout actionsLayout = new HorizontalLayout();
    TextField url = new TextField("URL Document");
    public Button save = new Button("Guardar");
    public Button close = new Button("Cancelar");
    public Button delete = new Button("Eliminar");

    public boolean editing;
    public boolean isValid;

    public DocumentsForm(){
        this.editing = false;
        this.isValid = false;

        setClassName("stepDocumentSection");
        configureElements();
        configureForm();
    }

    private void configureElements() {
        delete.setVisible(false);

        this.url.setValue("");
        elements.add(url);
        actionsLayout.add(createButtonsLayout());
    }

    private Component createButtonsLayout() {

        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(buttonClickEvent -> validateAndSave());

        return new HorizontalLayout(save, close, delete);
    }

    private void configureForm() {
        form.add(elements,
                actionsLayout);

        add(form);
    }


    private void validateAndSave() {
        if(isValid())
        {
            stepDocument = new StepDocument();
            stepDocument.setUrl(url.getValue());
            this.isValid = true;
        }
        else {
            MostrarMensajeError("El campo url es obligatorio.");
        }
    }

    private void MostrarMensajeError(String validacionIncompleta) {
        Span mensaje = new Span(validacionIncompleta);
        Notification notification = new Notification(mensaje);
        notification.setDuration(3000);
        notification.setPosition(Notification.Position.MIDDLE);
        notification.open();
    }

    public boolean isValid() {
        boolean result = false;

        if(validateFields())
            result = true;

        return result;
    }

    public boolean validateFields() {
        boolean result = false;

        if(!this.url.getValue().isEmpty()) {
            result = true;
        }
        return result;
    }

    public StepDocument getStepDocument() {
        return this.stepDocument;
    }

    public void setStepDocument(StepDocument stepDocument) {

        this.stepDocument = stepDocument;
        this.url.setValue(stepDocument.getUrl());
    }

    public void setAsDefault() {
        this.url.clear();
    }
}
