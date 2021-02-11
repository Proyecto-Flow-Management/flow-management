package com.proyecto.flowmanagement.ui.views.grids;

import com.proyecto.flowmanagement.backend.persistence.entity.Alternative;
import com.proyecto.flowmanagement.backend.persistence.entity.StepDocument;
import com.proyecto.flowmanagement.ui.views.forms.DocumentsForm;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.LinkedList;
import java.util.List;

@CssImport("./styles/general.css")
public class DocumentsGridForm extends HorizontalLayout {

    private Button createDocument;
    DocumentsForm documentsForm;

    StepDocument editing;

    Grid<StepDocument> stepDocumentGrid;
    List<StepDocument> stepDocumentsList;

    VerticalLayout documentSectionLayout = new VerticalLayout();
    Accordion accordion = new Accordion();
    FormLayout basicInformation = new FormLayout();

    public DocumentsGridForm()
    {
        stepDocumentsList = new LinkedList<>();
        setSizeFull();
        configureElements();
    }

    private void configureElements() {
        configureGrid();
        createDocument = new Button("Crear Document", click -> addDocument());
        editing = null;
        documentsForm = new DocumentsForm();
        documentsForm.setVisible(false);

        documentsForm.save.addClickListener(buttonClickEvent -> CreateStepDocument());
        documentsForm.close.addClickListener(buttonClickEvent -> CloseForm());
        documentsForm.delete.addClickListener(buttonClickEvent -> deleteStepDocument());

        HorizontalLayout gridLayout = new HorizontalLayout();
        gridLayout.add(stepDocumentGrid);
        gridLayout.addClassName("gridHorizontalLayout");

        HorizontalLayout stepDocumentFormLayout = new HorizontalLayout();
        stepDocumentFormLayout.add(documentsForm);
        stepDocumentFormLayout.setWidthFull();

        HorizontalLayout createStepDocumentLayout = new HorizontalLayout();
        createStepDocumentLayout.add(createDocument);
        createStepDocumentLayout.setWidthFull();

        basicInformation.setWidthFull();
        documentSectionLayout.setWidthFull();
        documentSectionLayout.setId("step-Layout");
        documentSectionLayout.add(createStepDocumentLayout, stepDocumentFormLayout, gridLayout);
        setWidthFull();
        basicInformation.add(documentSectionLayout);
        accordion.add("Documents", basicInformation);
        accordion.close();
        add(accordion);
    }

    public void deleteStepDocument()
    {
        this.stepDocumentsList.remove(editing);
        CloseForm();
        updateGrid();
    }

    private void CloseForm() {
        this.documentsForm.setVisible(false);
    }

    private void CreateStepDocument() {
        this.stepDocumentGrid.deselectAll();

        if (documentsForm.isValid) {
            StepDocument newStepDocument = documentsForm.getStepDocument();

            if(!documentsForm.editing) {
                stepDocumentsList.add(newStepDocument);
            }
            else
            {
                int index = stepDocumentsList.indexOf(editing);
                this.stepDocumentsList.set(index, newStepDocument);
                documentsForm.editing = false;
            }
            updateGrid();
            documentsForm.setVisible(false);
            closeEditor();
        }
    }

    private void updateGrid() {
        if(stepDocumentsList == null)
            stepDocumentsList = new LinkedList<>();
        stepDocumentGrid.setItems(stepDocumentsList);
    }

    private void addDocument() {
        documentsForm.editing = false;
        stepDocumentGrid.asSingleSelect().clear();
        documentsForm.setAsDefault();
        documentsForm.setVisible(true);
        documentsForm.delete.setVisible(false);
    }

    private void configureGrid() {
        stepDocumentGrid = new Grid<>(StepDocument.class);
        stepDocumentGrid.addClassName("user-grid");
        stepDocumentGrid.setSizeFull();
        stepDocumentGrid.setColumns("url");
        stepDocumentGrid.asSingleSelect().addValueChangeListener(evt -> editStepDocument(evt.getValue()));
    }

    private void editStepDocument(StepDocument stepDocument) {
        if(stepDocument == null) {
            closeEditor();
        } else {
            documentsForm.setStepDocument(stepDocument);
            documentsForm.editing = true;
            documentsForm.setVisible(true);
            editing = stepDocument;
            documentsForm.delete.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        documentsForm.setVisible(false);
        documentsForm.setAsDefault();
        removeClassName("editing");
    }

    public List<StepDocument> getDocuments() {
        return this.stepDocumentsList;
    }

    public void loadStepDocuments(List<StepDocument> stepDocuments)
    {
        this.stepDocumentsList = stepDocuments;
        if(stepDocumentsList == null)
            stepDocumentsList = new LinkedList<>();
        updateGrid();
        this.accordion.close();
    }

    public void setAsDefault() {
        this.stepDocumentsList = new LinkedList<>();
        updateGrid();
    }
}
