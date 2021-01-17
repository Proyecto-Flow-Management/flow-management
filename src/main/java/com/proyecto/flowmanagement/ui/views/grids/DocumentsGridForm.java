package com.proyecto.flowmanagement.ui.views.grids;

import com.proyecto.flowmanagement.backend.persistence.entity.Alternative;
import com.proyecto.flowmanagement.backend.persistence.entity.StepDocument;
import com.proyecto.flowmanagement.backend.persistence.entity.UnaryCondition;
import com.proyecto.flowmanagement.ui.views.forms.DocumentsForm;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.LinkedList;
import java.util.List;

@CssImport("./styles/general.css")
public class DocumentsGridForm extends VerticalLayout {

    private Button createDocument;
    DocumentsForm documentsForm;
    StepDocument editStepDocument;

    Grid<StepDocument> stepDocumentGrid;
    List<StepDocument> stepDocumentsList;

    public DocumentsGridForm()
    {
        setSizeFull();
        configureElements();
    }

    private void configureElements() {
        stepDocumentsList = new LinkedList<>();
        configureGrid();
        createDocument = new Button("Crear Document", click -> addDocument());

        documentsForm = new DocumentsForm();
        documentsForm.setVisible(false);
        documentsForm.save.addClickListener(buttonClickEvent -> CreateorSaveStepDocument());
        documentsForm.close.addClickListener(buttonClickEvent -> CloseForm());

        HorizontalLayout gridLayout = new HorizontalLayout();
        gridLayout.add(stepDocumentGrid);
        gridLayout.addClassName("gridHorizontalLayout");

        HorizontalLayout stepDocumentFormLayout = new HorizontalLayout();
        stepDocumentFormLayout.add(documentsForm);
        stepDocumentFormLayout.setWidthFull();

        HorizontalLayout createStepDocumentLayout = new HorizontalLayout();
        createStepDocumentLayout.add(createDocument);
        createStepDocumentLayout.setWidthFull();

        add(createStepDocumentLayout, stepDocumentFormLayout, gridLayout);
    }

    private void CloseForm() {
        this.documentsForm.setVisible(false);
    }


    private void CreateorSaveStepDocument() {
        if (documentsForm.isValid()) {
            StepDocument newStepDocument = documentsForm.getStepDocument();

            if (documentsForm.editing) {
                int index = stepDocumentsList.indexOf(editStepDocument);
                stepDocumentsList.set(index, newStepDocument);
                updateGrid();
            } else {
                stepDocumentsList.add(newStepDocument);
                updateGrid();
            }
            closeEditor();
        }
    }

    private void updateGrid() {
        if (stepDocumentsList != null) {
            stepDocumentGrid.setItems(stepDocumentsList);
        }
        else{
            stepDocumentGrid.setItems(new LinkedList<>());
        }
    }

    public void updateDocuments(List documentsList){
        this.stepDocumentsList = documentsList;
        updateGrid();
    }

    private void addDocument() {
        stepDocumentGrid.asSingleSelect().clear();
        editStepDocument(null);
    }

    private void editStepDocument(StepDocument stepDocument) {
        documentsForm.setVisible(true);

        if(stepDocument != null) {
            this.editStepDocument = stepDocument;
            documentsForm.setStepDocument(stepDocument);
            addClassName("editing");
        }
        else
        {
            documentsForm.setStepDocument(null);
        }
    }

    public void setAsDefault() {
        this.stepDocumentsList = new LinkedList<StepDocument>();
        this.stepDocumentGrid.setItems(stepDocumentsList);
    }

    private void closeEditor() {
        documentsForm.setStepDocument(null);
        documentsForm.setVisible(false);
        removeClassName("editing");
    }

private void configureGrid() {  
    stepDocumentGrid = new Grid<>(StepDocument.class);
    stepDocumentGrid.addClassName("user-grid");
    stepDocumentGrid.setSizeFull();
    stepDocumentGrid.setColumns("url");
    stepDocumentGrid.asSingleSelect().addValueChangeListener(evt -> editStepDocument(evt.getValue()));
    }

    public List<StepDocument> getDocuments() {
        return this.stepDocumentsList;
    }
}
