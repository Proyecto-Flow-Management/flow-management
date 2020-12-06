package com.proyecto.flowmanagement.ui.views.forms;

import com.proyecto.flowmanagement.backend.persistence.entity.StepDocument;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
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
        save.addClickListener(buttonClickEvent -> validAndSave());
        elements.add(url,save,close);
        add(elements);
    }

    private void validAndSave() {
        if(isValid())
        {
            stepDocument = new StepDocument();
            stepDocument.setUrl(url.getValue());
        }
    }

    private boolean isValid() {
        return true;
    }

    public StepDocument getStepDocument() {
        return this.stepDocument;
    }

    public void setStepDocument(StepDocument stepDocument) {
    }
}
