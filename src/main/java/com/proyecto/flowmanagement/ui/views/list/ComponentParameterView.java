package com.proyecto.flowmanagement.ui.views.list;

import com.proyecto.flowmanagement.backend.persistence.entity.Component_Parameter;
import com.proyecto.flowmanagement.backend.service.ComponentParameterService;
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
@Route(value = "componentParameter", layout = MainLayout.class)
@PageTitle("Component Parameter | Flow Management")
public class ComponentParameterView extends VerticalLayout {
    private final ComponentParameterForm form;
    Grid<Component_Parameter> grid = new Grid<>(Component_Parameter.class);
    TextField filterText = new TextField();

    private ComponentParameterService componentParameterService;

    public ComponentParameterView(ComponentParameterService componentParameterService) {
        this.componentParameterService = componentParameterService;
        addClassName("list-view");
        setSizeFull();
        configureGrid();

//        form = new UserForm(rolController.listRol());
        form = new ComponentParameterForm();
        form.addListener(ComponentParameterForm.SaveEvent.class, this::saveContact);
        form.addListener(ComponentParameterForm.DeleteEvent.class, this::deleteContact);
        form.addListener(ComponentParameterForm.CloseEvent.class, e -> closeEditor());

        Div content = new Div(grid, form);
        content.addClassName("content");
        content.setSizeFull();

        add(getToolBar(), content);
        updateList();
        closeEditor();
    }

    private void deleteContact(ComponentParameterForm.DeleteEvent evt) {
        componentParameterService.delete(evt.getComponentParameter());
        updateList();
        closeEditor();
    }

    private void saveContact(ComponentParameterForm.SaveEvent evt) {
        componentParameterService.save(evt.getComponentParameter());
        updateList();
        closeEditor();
    }

    private void closeEditor() {
        form.setComponentParameter(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private HorizontalLayout getToolBar() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addComponentParameterButton = new Button("Add Component Parameter", click -> addComponentParameter());

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addComponentParameterButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void addComponentParameter() {
        grid.asSingleSelect().clear();
        editComponentParameter(new Component_Parameter());
    }

    private void updateList() {
        grid.setItems(componentParameterService.findAll());
    }

    private void  configureGrid() {
        grid.addClassName("componentparameter-grid");
        grid.setSizeFull();
        grid.setColumns("name", "label", "value");

        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(evt -> editComponentParameter(evt.getValue()));
    }

    private void editComponentParameter(Component_Parameter component_parameter) {
        if(component_parameter == null) {
            closeEditor();
        } else {
            form.setComponentParameter(component_parameter);
            form.setVisible(true);
            addClassName("editing");
        }
    }
}
