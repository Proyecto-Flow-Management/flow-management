package com.proyecto.flowmanagement.ui.views.list;

import com.proyecto.flowmanagement.backend.persistence.entity.Operation_Type;
import com.proyecto.flowmanagement.backend.service.OperationTypeService;
import com.proyecto.flowmanagement.ui.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.stereotype.Component;

@Component
@Route(value = "operationType", layout = MainLayout.class)
@PageTitle("Operation Type | Flow Management")
public class OperationTypeView extends VerticalLayout {

    private final OperationTypeForm form;
    Grid<Operation_Type> grid = new Grid<>(Operation_Type.class);
    TextField filterText = new TextField();

    private OperationTypeService operationTypeService;

    public OperationTypeView(OperationTypeService operationTypeService) {
        this.operationTypeService = operationTypeService;
        addClassName("list-view");
        setSizeFull();
        configureGrid();

//        form = new UserForm(rolController.listRol());
        form = new OperationTypeForm();
        form.addListener(OperationTypeForm.SaveEvent.class, this::saveContact);
        form.addListener(OperationTypeForm.DeleteEvent.class, this::deleteContact);
        form.addListener(OperationTypeForm.CloseEvent.class, e -> closeEditor());

        Div content = new Div(grid, form);
        content.addClassName("content");
        content.setSizeFull();

        add(getToolBar(), content);
        updateList();
        closeEditor();
    }

    private void deleteContact(OperationTypeForm.DeleteEvent evt) {
        operationTypeService.delete(evt.getOperationType());
        updateList();
        closeEditor();
    }

    private void saveContact(OperationTypeForm.SaveEvent evt) {
        operationTypeService.save(evt.getOperationType());
        updateList();
        closeEditor();
    }

    private void closeEditor() {
        form.setOperationType(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private HorizontalLayout getToolBar() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addOperationTypeButton = new Button("Add Operation Type", click -> addOperationType());

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addOperationTypeButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void addOperationType() {
        grid.asSingleSelect().clear();
        editOperationType(new Operation_Type());
    }

    private void updateList() {
        grid.setItems(operationTypeService.findAll());
    }

    private void  configureGrid() {
        grid.addClassName("operationtype-grid");
        grid.setSizeFull();
        grid.setColumns("name", "label");

        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(evt -> editOperationType(evt.getValue()));
    }

    private void editOperationType(Operation_Type operation_type) {
        if(operation_type == null) {
            closeEditor();
        } else {
            form.setOperationType(operation_type);
            form.setVisible(true);
            addClassName("editing");
        }
    }

}
