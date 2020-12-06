package com.proyecto.flowmanagement.ui.views.forms;

import com.proyecto.flowmanagement.backend.persistence.entity.Step;
import com.proyecto.flowmanagement.backend.persistence.entity.StepDocument;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.shared.Registration;

import java.util.ArrayList;
import java.util.List;

@org.springframework.stereotype.Component
@PageTitle("CreateStep | Flow Management")
public class StepForm extends VerticalLayout {
    private final StepDocumentForm stepDocumentForm;
    private Step step;

    List<StepDocument> stepDocumentList = new ArrayList<StepDocument>();

    HorizontalLayout toolbarStepDocument;
    HorizontalLayout contentFields;
    HorizontalLayout createButtons;

    TextField text = new TextField("Nombre");
    TextField label = new TextField("Etiqueta");

    Button save = new Button("Guardar");
    Button delete = new Button("Borrar");
    Button close = new Button("Cancelar");

    Grid<StepDocument> gridStepDocument = new Grid<>(StepDocument.class);
    Div messageDiv = new Div();

    Binder<Step> binder = new BeanValidationBinder<>(Step.class);

    public StepForm() {
        addClassName("step-form");

        setSizeFull();
        configureGrid();

        stepDocumentForm = new StepDocumentForm();
        configureStepDocumentForm();

        contentFields = new HorizontalLayout(text,label);
        HorizontalLayout contentStepDocument = new HorizontalLayout(stepDocumentForm, gridStepDocument);
        toolbarStepDocument = getToolBarStepDocument();
        contentStepDocument.addClassName("contentStep");
        contentStepDocument.setSizeFull();

        updateList();
        gridStepDocument.setItems(stepDocumentList);

        binder.bindInstanceFields(this);
        createButtons = createButtonsLayout();

        add(contentFields, toolbarStepDocument, contentStepDocument, messageDiv, createButtons);
    }

    public void setStep(Step step) {
        this.step = step;
        binder.readBean(step);
    }

    public void configureStepDocumentForm(){
        stepDocumentForm.addListener(StepDocumentForm.SaveEvent.class, this::saveStepDocument);
        stepDocumentForm.addListener(StepDocumentForm.DeleteEvent.class, this::deleteStepDocument);
        stepDocumentForm.addListener(StepDocumentForm.CloseEvent.class, e -> closeEditorStepDocument());
        stepDocumentForm.setVisible(false);
    }

    private void updateList() {
        gridStepDocument.setColumns("url");
    }

    private void  configureGrid() {
        gridStepDocument.addClassName("stepdocument-grid");
        gridStepDocument.setSizeFull();
        gridStepDocument.setSelectionMode(Grid.SelectionMode.MULTI);

        gridStepDocument.asMultiSelect().addValueChangeListener(event -> {
            String selectedSteps = "";
            for (StepDocument stepDocument : gridStepDocument.getSelectedItems())
                selectedSteps += stepDocument.getUrl() + ", ";
            String message = String.format("Seleccionados actualmente: %s", selectedSteps);
//            String message = String.format("Seleccionados actualmente: %s", event.getValue());
            messageDiv.setText(message);
        });
        gridStepDocument.getColumns().forEach(col -> col.setAutoWidth(true));
    }

    private HorizontalLayout getToolBarStepDocument() {
        Button addStepDocumentButton = new Button("Crear Step Document", click -> addStepDocument());

        HorizontalLayout toolbar = new HorizontalLayout(addStepDocumentButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }


    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(click -> validateAndSave());
        delete.addClickListener(click -> fireEvent(new StepForm.DeleteEvent(this, step)));
        close.addClickListener(click -> fireEvent(new StepForm.CloseEvent(this)));

        binder.addStatusChangeListener(evt -> save.setEnabled(binder.isValid()));

        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(step);
            fireEvent(new StepForm.SaveEvent(this, step));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    private void addStepDocument() {
        gridStepDocument.asMultiSelect().clear();
        editStepDocument(new StepDocument());
    }

    private void editStepDocument(StepDocument stepDocument) {
        if(step == null) {
            closeEditorStepDocument();
        } else {
            stepDocumentForm.setStepDocument(stepDocument);
            stepDocumentForm.setVisible(true);
            gridStepDocument.setVisible(false);
            contentFields.setVisible(false);
            setVisibleEditStepDocument(false);
            addClassName("editing");
        }
    }


    private void deleteStepDocument(StepDocumentForm.DeleteEvent evt) {
        updateList();
        closeEditorStepDocument();
    }

    private void saveStepDocument(StepDocumentForm.SaveEvent evt) {
        stepDocumentList.add(evt.getStepDocument());
        gridStepDocument.setItems(stepDocumentList);
        gridStepDocument.getDataProvider().refreshAll();
        closeEditorStepDocument();
        gridStepDocument.setVisible(true);
        contentFields.setVisible(true);
        setVisibleEditStepDocument(true);
    }

    private void closeEditorStepDocument() {
        stepDocumentForm.setStepDocument(null);
        stepDocumentForm.setVisible(false);
        gridStepDocument.setVisible(true);
        contentFields.setVisible(true);
        setVisibleEditStepDocument(true);
        removeClassName("editing");
    }

    private void setVisibleEditStepDocument(Boolean bool){
        toolbarStepDocument.setVisible(bool);
        createButtons.setVisible(bool);
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