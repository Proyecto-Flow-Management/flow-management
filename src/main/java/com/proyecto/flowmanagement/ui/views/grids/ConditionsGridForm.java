package com.proyecto.flowmanagement.ui.views.grids;

import com.proyecto.flowmanagement.backend.persistence.entity.Alternative;
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
import java.util.LinkedList;
import java.util.List;

@CssImport("./styles/general.css")
public class ConditionsGridForm extends VerticalLayout {

    UnaryConditionForm unaryConditionForm;
    BinaryConditionForm binaryConditionForm;

    BinaryCondition editingBinary;
    UnaryCondition editUnary;

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

    public ConditionsGridForm(Alternative alternative)
    {

        if(alternative != null)
        {
            unaryConditionList = alternative.getConditions();
            binaryConditionList = alternative.getBinaryConditions();
        }
        else
        {
            this.unaryConditionList = new LinkedList<>();
            this.binaryConditionList = new LinkedList<>();
        }

        configureElements();

        updateUnaryGrid();
        updateBinaryGrid();
    }

    private void configureElements() {
        configureGrid();
        addUnaryCondition = new Button("Crear Condicion Unaria", click -> addUnaryCondition());
        addBinaryCondition = new Button("Crear Condicion Binaria", click -> addBinaryCondition());

        unaryConditionForm = new UnaryConditionForm();
        unaryConditionForm.setVisible(false);
        unaryConditionForm.save.addClickListener(buttonClickEvent -> CreateOrSaveUnaryCondition());
        unaryConditionForm.delete.addClickListener(buttonClickEvent -> EliminarUnary());
        unaryConditionForm.close.addClickListener(buttonClickEvent -> closeUnaryEditor());
        
        binaryConditionForm = new BinaryConditionForm();
        binaryConditionForm.setVisible(false);
        binaryConditionForm.save.addClickListener(buttonClickEvent -> CreateOrSaveBinaryCondition());
        binaryConditionForm.delete.addClickListener(buttonClickEvent -> EliminarBinary());
        binaryConditionForm.close.addClickListener(buttonClickEvent -> closeBinaryEditor());

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
        unaryConditionGrid.setColumns("operationName");
        unaryConditionGrid.asSingleSelect().addValueChangeListener(evt -> editUnaryCondition(evt.getValue()));
        binaryConditionGrid = new Grid<>(BinaryCondition.class);
        binaryConditionGrid.setColumns("operator");
        binaryConditionGrid.asSingleSelect().addValueChangeListener(evt -> editBinaryCondition(evt.getValue()));
    }

    private void editUnaryCondition(UnaryCondition unaryCondition) {
        this.editUnary = unaryCondition;
        unaryConditionForm.setVisible(true);
        if(unaryCondition != null) {
            unaryConditionForm.setUnaryCondition(unaryCondition);
            addClassName("editing");
        }
    }

    private void editBinaryCondition(BinaryCondition binaryCondition) {
        this.editingBinary = binaryCondition;
        binaryConditionForm.setVisible(true);
        if(binaryCondition != null) {
            binaryConditionForm.setBinaryCondition(binaryCondition);
            addClassName("editing");
        }
    }

    private void addUnaryCondition() {
        unaryConditionForm.setUnaryCondition(null);
        unaryConditionGrid.asSingleSelect().clear();
        editUnaryCondition(null);
    }
    
    private void addBinaryCondition() {
        binaryConditionForm.setBinaryCondition(null);
        binaryConditionGrid.asSingleSelect().clear();
        editBinaryCondition(null);
    }

    private void EliminarUnary() {
        unaryConditionList.remove(editUnary);
        updateUnaryGrid();
        closeUnaryEditor();
    }

    private void EliminarBinary() {
        binaryConditionList.remove(editingBinary);
        updateBinaryGrid();
        closeBinaryEditor();
    }

    private void CreateOrSaveUnaryCondition() {

        UnaryCondition newUnaryCondition = unaryConditionForm.getUnaryCondition();

        if(unaryConditionForm.editing)
        {
            int index = unaryConditionList.indexOf(editUnary);
            unaryConditionList.set(index, newUnaryCondition);
            updateUnaryGrid();
        }
        else
        {
            unaryConditionList.add(newUnaryCondition);
            updateUnaryGrid();
            closeUnaryEditor();
        }

        unaryConditionForm.setVisible(false);
    }
    
    private void CreateOrSaveBinaryCondition() {

        BinaryCondition newBinaryCondition = binaryConditionForm.getBinaryCondition();

        if(binaryConditionForm.editing)
        {
            int index = binaryConditionList.indexOf(editingBinary);
            binaryConditionList.set(index, newBinaryCondition);
            updateBinaryGrid();
        }
        else
        {
            binaryConditionList.add(newBinaryCondition);
            updateBinaryGrid();
            closeBinaryEditor();
        }

        binaryConditionForm.setVisible(false);

    }

    private void updateUnaryGrid() {
        unaryConditionGrid.setItems(unaryConditionList);
    }


    private void updateBinaryGrid() {
        binaryConditionGrid.setItems(binaryConditionList);
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
