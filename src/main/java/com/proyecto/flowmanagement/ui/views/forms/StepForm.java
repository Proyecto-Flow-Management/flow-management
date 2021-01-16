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
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.shared.Registration;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@org.springframework.stereotype.Component
@PageTitle("CreateStep | Flow Management")
@CssImport("./styles/step-grid-form.css")
public class StepForm extends HorizontalLayout {

    public boolean editing;
    private Step step;
    List<String> stepIdList;

    AlternativeGridForm alternativeGridForm = new AlternativeGridForm();
    DocumentsGridForm documentsGridForm = new DocumentsGridForm();
    OperationGridForm operationGridForm = new OperationGridForm();

    VerticalLayout form = new VerticalLayout();
    HorizontalLayout elements = new HorizontalLayout();
    HorizontalLayout alternativeGridLayout = new HorizontalLayout();
    HorizontalLayout stepDocumentsLayout = new HorizontalLayout();
    HorizontalLayout operationsLayout = new HorizontalLayout();
    HorizontalLayout actionsLayout = new HorizontalLayout();

    TextField text = new TextField("Texto Step");
    TextField textId = new TextField("TextId Step");
    TextField label = new TextField("Label Step");

    public Button save = new Button("Guardar");
    Button delete = new Button("Eliminar");
    public Button close = new Button("Cancelar");


    public StepForm() {

        setSizeFull();

        configureElements();

        configureAlternatives();

        configureDocuments();

        configureOperations();

        configureForm();

        agregarInteractividad();

        editing = false;
    }

    private void configureElements() {
        addClassName("stepSection");

        this.text.setRequired(true);
        this.text.setErrorMessage("Este campo es obligatorio.");
        this.textId.setRequired(true);
        this.textId.setErrorMessage("Este campo es obligatorio.");
        this.label.setRequired(true);
        this.label.setErrorMessage("Este campo es obligatorio.");

        this.text.setValue("");
        this.textId.setValue("");
        this.label.setValue("");

        elements.add(text,textId,label);
        actionsLayout.add(createButtonsLayout());
    }

    private void configureAlternatives() {
        alternativeGridForm.setWidthFull();
        alternativeGridLayout.setWidthFull();
        alternativeGridLayout.add(alternativeGridForm);
    }

    private void configureDocuments() {
        stepDocumentsLayout.setWidthFull();
        stepDocumentsLayout.add(documentsGridForm);
    }

    private void configureOperations() {
        operationsLayout.setWidthFull();
        operationsLayout.add(operationGridForm);
    }

    private void configureForm() {

        form.add(elements,
                operationsLayout,
                alternativeGridLayout,
                stepDocumentsLayout,
                actionsLayout);

        add(form);
    }

    public void setStep(Step step) {
        this.step = step;

        if (step != null)
        {
            this.step = step;
            this.text.setValue(step.getText());
            this.textId.setValue(step.getTextId());
            this.label.setValue(step.getLabel());
            this.operationGridForm.updateOperations(step.getOperations());
            this.alternativeGridForm.updateAlternatives(step.getAlternatives());
            this.documentsGridForm.updateDocuments(step.getStepDocuments());
            editing = true;
            delete.setVisible(true);
        }
        else
        {
            this.text.setValue("");
            this.textId.setValue("");
            this.label.setValue("");
            this.alternativeGridForm.setAsDefault();
            this.operationGridForm.setAsDefault();
            this.documentsGridForm.setAsDefault();

        }
    }

    private Component createButtonsLayout() {

        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(click -> validateAndSave());
        close.addClickListener(click -> fireEvent(new StepForm.CloseEvent(this)));

        return new HorizontalLayout(save, close);
    }

    public Step getStep() {
        return this.step;
    }

    public void agregarInteractividad()
    {
        alternativeGridForm.alternativeForm.save.addClickListener(buttonClickEvent -> addToOperationForm());

    }

    public void setStepIdList (List stepIdList){
        this.stepIdList = stepIdList;
        alternativeGridForm.setStepIdList(this.stepIdList);
    }

    private void addToOperationForm() {
        List<String> ids = new LinkedList<>();

        for (Alternative alternative: this.alternativeGridForm.getAlternatives()) {
            if(alternative.getNextStep() != "")
                ids.add(alternative.getNextStep());
        }

        this.operationGridForm.alternatives = ids;
    }

    private void showNotification(String message){
        Span content = new Span(message);
        Notification notification = new Notification(content);
        notification.setDuration(3000);
        notification.setPosition(Notification.Position.MIDDLE);
        notification.open();
    }

    private void validateAndSave() {
        if(!validateGrids() && !validateFields()){
            showNotification("Algún valor ingresado no es correcto o falta completar campos.");
            showNotification("La grilla de Alternative u Operations se encuentra vacía. Se debe tener al menos un elemento en ambas.");
        }
        else if(!validateGrids()){
            showNotification("La grilla de Alternative u Operations se encuentra vacía. Se debe tener al menos un elemento en ambas.");
        }
        else if (!validateFields()) {
            showNotification("Algún valor ingresado no es correcto o falta completar campos.");
        }
        else {
            this.step = new Step();
            step.setText(text.getValue());
            step.setTextId(textId.getValue());
            step.setLabel(label.getValue());
            step.setOperations(operationGridForm.getOperations());
            step.setAlternatives(alternativeGridForm.getAlternatives());
            step.setStepDocuments(documentsGridForm.getDocuments());
        }
    }

    public boolean isValid() {
        boolean result = false;

        if(validateFields() && validateGrids())
            result = true;

        return result;
    }

    public boolean validateFields(){
        boolean result = false;

        if(!text.getValue().isEmpty() &&
                !textId.getValue().isEmpty() &&
                !label.getValue().isEmpty())
            result = true;

        return result;
    }

    public boolean validateGrids() {
        boolean result = false;

        if (operationGridForm.getOperations().size() > 0 &&
                alternativeGridForm.getAlternatives().size() > 0)
            result = true;

        return result;
    }

    public void clearFields(){
        this.text.setValue("");
        this.textId.setValue("");
        this.label.setValue("");
    }

    public Button getSaveButton() {
        return this.save;
    }

    // Events
    public static abstract class StepFormEvent extends ComponentEvent<StepForm> {
        private Step step;

        protected StepFormEvent(StepForm source, Step step) {
            super(source, false);
            this.step = step;
        }

        public Step getStep() {
            return step;
        }
    }

    public static class SaveEvent extends StepForm.StepFormEvent {
        SaveEvent(StepForm source, Step step) {
            super(source, step);
        }
    }

    public static class DeleteEvent extends StepForm.StepFormEvent {
        DeleteEvent(StepForm source, Step step) {
            super(source, step);
        }

    }

    public static class CloseEvent extends StepForm.StepFormEvent {
        CloseEvent(StepForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}