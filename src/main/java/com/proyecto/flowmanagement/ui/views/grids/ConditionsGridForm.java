package com.proyecto.flowmanagement.ui.views.grids;

import com.proyecto.flowmanagement.backend.persistence.entity.BinaryCondition;
import com.proyecto.flowmanagement.backend.persistence.entity.UnaryCondition;
import com.proyecto.flowmanagement.ui.views.forms.BinaryConditionForm;
import com.proyecto.flowmanagement.ui.views.forms.UnaryConditionForm;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.ArrayList;
import java.util.List;

@CssImport("./styles/general.css")
public class ConditionsGridForm extends VerticalLayout {

    UnaryConditionForm unaryConditionForm;
    BinaryConditionForm binaryConditionForm;

    Button addUnaryCondition = new Button("Agregar Condicion");
    Button addBinaryCondition = new Button("Agregar Condicion Binaria");

    List<UnaryCondition> unaryConditionList = new ArrayList<>();
    List<BinaryCondition> binaryConditionList = new ArrayList<>();

    Grid<UnaryCondition> unaryConditionGrid;
    Grid<BinaryCondition> binaryConditionGrid;

    public ConditionsGridForm()
    {
        configureElements();
    }

    private void configureElements() {
        configureGrid();
        addUnaryCondition = new Button("Crear Condicion Unaria", click -> addUnaryCondition());
        addBinaryCondition = new Button("Crear Condicion Binaria", click -> addBinaryCondition());

        unaryConditionForm = new UnaryConditionForm();
        unaryConditionForm.setVisible(false);
        unaryConditionForm.save.addClickListener(buttonClickEvent -> CreateUnaryCondition());
        unaryConditionForm.close.addClickListener(buttonClickEvent -> CloseUnaryForm());
        
        binaryConditionForm = new BinaryConditionForm();
        binaryConditionForm.setVisible(false);
        binaryConditionForm.save.addClickListener(buttonClickEvent -> CreateBinaryCondition());
        binaryConditionForm.close.addClickListener(buttonClickEvent -> CloseBinaryForm());

        updateUnaryGrid();

        HorizontalLayout unaryGridLayout = new HorizontalLayout();
        unaryGridLayout.add(unaryConditionGrid);
        unaryConditionGrid.setWidthFull();
        unaryGridLayout.addClassName("gridHorizontalLayout");

        HorizontalLayout binaryGridLayout = new HorizontalLayout();
        binaryGridLayout.add(binaryConditionGrid);
        binaryGridLayout.addClassName("gridHorizontalLayout");

        HorizontalLayout unaryConditionFormLayout = new HorizontalLayout();
        unaryConditionFormLayout.add(unaryConditionForm);
        unaryConditionFormLayout.setWidthFull();    
        
        HorizontalLayout binaryConditionFormLayout = new HorizontalLayout();
        binaryConditionFormLayout.add(binaryConditionForm);
        binaryConditionFormLayout.setWidthFull();

        HorizontalLayout createUnaryLayout = new HorizontalLayout();
        createUnaryLayout.add(addUnaryCondition);
        createUnaryLayout.setWidthFull();

        HorizontalLayout createBinaryLayout = new HorizontalLayout();
        createBinaryLayout.add(addBinaryCondition);
        createBinaryLayout.setWidthFull();

        VerticalLayout binaryLayout = new VerticalLayout();
        binaryLayout.add(createBinaryLayout);
        binaryLayout.add(binaryConditionFormLayout);
        binaryLayout.add(binaryGridLayout);

        add(createUnaryLayout,
                unaryConditionFormLayout,
                unaryGridLayout,
                binaryLayout,
                createBinaryLayout,
                binaryConditionFormLayout,
                binaryGridLayout
        );
    }

    private void configureGrid() {
        unaryConditionGrid = new Grid<>(UnaryCondition.class);
        unaryConditionGrid.setColumns("operationName","conditionParameter");
        binaryConditionGrid = new Grid<>(BinaryCondition.class);
        binaryConditionGrid.setColumns("operator","conditions");
    }

    private void addUnaryCondition() {
        unaryConditionGrid.asSingleSelect().clear();
        editUnaryCondition(new UnaryCondition());
    }
    
    private void addBinaryCondition() {
        binaryConditionGrid.asSingleSelect().clear();
        editBinaryCondition(new BinaryCondition());
    }

    private void CloseUnaryForm() {
        this.unaryConditionForm.setVisible(false);
    }
    
    private void CloseBinaryForm() {
        this.binaryConditionForm.setVisible(false);
    }
    

    private void CreateUnaryCondition() {
        UnaryCondition newUnaryCondition = unaryConditionForm.getUnaryCondition();
        unaryConditionList.add(newUnaryCondition);
        updateUnaryGrid();
        unaryConditionForm.setVisible(false);
    }
    
    private void CreateBinaryCondition() {
        BinaryCondition newBinaryCondition = binaryConditionForm.getBinaryCondition();
        List<UnaryCondition> unaryConditionsList = new ArrayList<>(unaryConditionGrid.getSelectedItems());
        newBinaryCondition.setConditions(unaryConditionsList);
        binaryConditionList.add(newBinaryCondition);
        updateBinaryGrid();
        binaryConditionForm.setVisible(false);
    }

    private void updateUnaryGrid() {
//        unaryConditionGrid.getDataProvider().refreshAll();
        unaryConditionGrid.setItems(unaryConditionList);
    }


    private void updateBinaryGrid() {
        binaryConditionGrid.setItems(binaryConditionList);
    }

    private void editUnaryCondition(UnaryCondition unaryCondition) {
        if(unaryCondition == null) {
            closeUnaryEditor();
        } else {
            unaryConditionForm.setUnaryCondition(unaryCondition);
            unaryConditionForm.setVisible(true);
            addClassName("editing");
        }
    }    
    
    private void editBinaryCondition(BinaryCondition binaryCondition) {
        if(binaryCondition == null) {
            closeBinaryEditor();
        } else {
            binaryConditionForm.setBinaryCondition(binaryCondition);
            binaryConditionForm.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeUnaryEditor() {
        unaryConditionForm.setUnaryCondition(null);
        unaryConditionForm.setVisible(false);
        removeClassName("editing");
    }

    private void closeBinaryEditor() {
        binaryConditionForm.setBinaryCondition(null);
        binaryConditionForm.setVisible(false);
        removeClassName("editing");
    }

    public List<UnaryCondition> getUnaryConditions() {
        return this.unaryConditionList;
    }
    public List<BinaryCondition> getBinaryConditions() {
        return this.binaryConditionList;
    }
}
