package com.proyecto.flowmanagement.ui.views.forms;

import com.proyecto.flowmanagement.backend.persistence.entity.StepDocument;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import java.util.LinkedList;
import java.util.List;

@CssImport("./styles/step-document-grid-form.css")
public class DocumentsForm extends VerticalLayout {
    private StepDocument stepDocument;

    public boolean editing;
    TextField url = new TextField("URL Document");
    public Button save = new Button("Guardar");
    public Button delete = new Button("Eliminar");
    public Button close = new Button("Cancelar");

    public DocumentsForm(){
        setClassName("stepDocumentSection");
        configureElements();
        editing = false;
    }

    private void configureElements() {
        HorizontalLayout elements = new HorizontalLayout();
        save.addClickListener(buttonClickEvent -> save());

        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        this.url.setRequired(true);
        this.url.setErrorMessage("Este campo es requerido para exportar la guía.");

        this.url.setValue("");
        elements.add(url,save,close, delete);
        add(elements);
    }

    private void showNotification(String message){
        Span content = new Span(message);
        Notification notification = new Notification(content);
        notification.setDuration(3000);
        notification.setPosition(Notification.Position.MIDDLE);
        notification.open();
    }

    private void save(){
        if(!emptyForm()){
            stepDocument = new StepDocument();
            stepDocument.setUrl(url.getValue());
        }
        else{
            showNotification("Debes completar el capmo URL Document.");
        }
    }

    public boolean emptyForm(){
        boolean result = false;

        if(this.url.getValue().isEmpty()) {
            result = true;
        }
        return result;
    }

    public List<String> isValid(){
        List <String> valores = validateFields();

        return valores;
    }

    public List<String> validateFields(){
        List<String> valores = new LinkedList<>();

        if(url.getValue().trim() == ""){
            valores.add("El campo Url es obligatorio.");
        }

        return valores;
    }
    public StepDocument getStepDocument() {
        return this.stepDocument;
    }

    public void setStepDocument(StepDocument stepDocument) {
        if (stepDocument != null)
        {
            this.stepDocument = stepDocument;
            this.url.setValue(stepDocument.getUrl());
            editing = true;
            delete.setVisible(true);
        }
        else
        {
            this.url.setValue("");
        }
    }
}
