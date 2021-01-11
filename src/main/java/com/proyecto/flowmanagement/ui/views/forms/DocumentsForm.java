package com.proyecto.flowmanagement.ui.views.forms;

import com.proyecto.flowmanagement.backend.persistence.entity.StepDocument;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

@CssImport("./styles/step-document-grid-form.css")
public class DocumentsForm extends VerticalLayout {
    private StepDocument stepDocument;

    TextField url = new TextField("URL Document");
    public Button save = new Button("Guardar");
    public Button close = new Button("Cancelar");

    public DocumentsForm(){
        setClassName("stepDocumentSection");
        configureElements();
    }

    private void configureElements() {
        HorizontalLayout elements = new HorizontalLayout();
        save.addClickListener(buttonClickEvent -> validateAndSave());
        this.url.setRequired(true);
        this.url.setErrorMessage("Este campo es obligatorio.");

        this.url.setValue("");
        elements.add(url,save,close);
        add(elements);
    }

    private void validateAndSave() {
        if(isValid())
        {
            stepDocument = new StepDocument();
            stepDocument.setUrl(url.getValue());
        }
        else {
            Span content = new Span("Algún valor ingresado no es correcto o falta completar campos.");
            Notification notification = new Notification(content);
            notification.setDuration(3000);
            notification.setPosition(Notification.Position.MIDDLE);
            notification.open();
        }
    }

    private boolean isValid() {
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
    }
}
