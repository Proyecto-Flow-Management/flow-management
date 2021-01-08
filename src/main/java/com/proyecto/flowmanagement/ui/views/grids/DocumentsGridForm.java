package com.proyecto.flowmanagement.ui.views.grids;

import com.proyecto.flowmanagement.backend.persistence.entity.StepDocument;
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

    Grid<StepDocument> stepDocumentGrid;
    List<StepDocument> stepDocumentsList;

    public DocumentsGridForm()
    {
        stepDocumentsList = new LinkedList<>();
        setSizeFull();
        configureElements();
    }

    private void configureElements() {
        configureGrid();
        createDocument = new Button("Crear Document", click -> addDocument());

        documentsForm = new DocumentsForm();
        documentsForm.setVisible(false);

        documentsForm.save.addClickListener(buttonClickEvent -> CreateStepDocument());
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

    private void CreateStepDocument() {
        StepDocument newStepDocument = documentsForm.getStepDocument();
        stepDocumentsList.add(newStepDocument);
        updateGrid();
        documentsForm.setVisible(false);
    }

    private void updateGrid() {
        stepDocumentGrid.setItems(stepDocumentsList);
    }

    private void addDocument() {
        stepDocumentGrid.asSingleSelect().clear();
        editStepDocument(new StepDocument());
    }

    private void editStepDocument(StepDocument stepDocument) {
        if(stepDocument == null) {
            closeEditor();
        } else {
            documentsForm.setStepDocument(stepDocument);
            documentsForm.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
//        documentsForm.setStepDocument(null);
        documentsForm.setVisible(false);
        removeClassName("editing");
    }

private void configureGrid() {  
    stepDocumentGrid = new Grid<>(StepDocument.class);
    stepDocumentGrid.addClassName("user-grid");
    stepDocumentGrid.setSizeFull();
    stepDocumentGrid.setColumns("url");
    stepDocumentGrid.asSingleSelect().addValueChangeListener(evt -> editAlternative(evt.getValue()));
    }

    private void editAlternative(StepDocument value) {
    }

    public List<StepDocument> getDocuments() {
        return this.stepDocumentsList;
    }
}
