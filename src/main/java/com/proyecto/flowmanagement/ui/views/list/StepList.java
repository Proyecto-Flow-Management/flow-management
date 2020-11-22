package com.proyecto.flowmanagement.ui.views.list;

import com.proyecto.flowmanagement.backend.persistence.entity.Step;
import com.proyecto.flowmanagement.backend.service.Impl.StepServiceImpl;
import com.proyecto.flowmanagement.ui.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Route(value = "StepList", layout = MainLayout.class)
@PageTitle("Listar Steps | Flow Management")
public class StepList extends VerticalLayout {

    Grid<Step> grid = new Grid<>(Step.class);
    StepServiceImpl stepService;

    public StepList(StepServiceImpl stepService) {
        this.stepService = stepService;

        addClassName("create-step-view");
        setSizeFull();
        configureGrid();

        Div content = new Div(grid);
        content.addClassName("content");
        content.setSizeFull();

        add(getToolBar(), content);
        updateList();
        grid.setItems(stepService.getAll());
    }

    private HorizontalLayout getToolBar() {
        Button addStepButton = new Button("Crear Step", event -> UI.getCurrent().navigate("CreateStep"));
        HorizontalLayout toolbar = new HorizontalLayout(addStepButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void updateList() {
        grid.setColumns("label", "text");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.addComponentColumn(step ->{
            Icon icon = new Icon("vaadin", "edit");
            Button botonEliminar = new Button(icon);
            icon.addClickListener(e -> {
                editStep(step.getId());
            });
            return  botonEliminar;
        }).setHeader("Editar");

        grid.addComponentColumn(step ->{
            Icon icon = new Icon("vaadin", "trash");
            Button botonEliminar = new Button(icon);
            botonEliminar.addClickListener(e -> {
                deleteStep(step.getId());
            });
            return  botonEliminar;
        }).setHeader("Eliminar");

        grid.addComponentColumn(step ->{
            Icon icon = new Icon("vaadin", "external-link");
            Button botonEliminar = new Button(icon);
            botonEliminar.addClickListener(e -> {
                export(step.getId());
            });
            return  botonEliminar;
        }).setHeader("Exportar");
    }

    private void  configureGrid() {
        grid.addClassName("step-grid");
        grid.setSizeFull();
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
    }

    private void deleteStep(Long id)
    {
        stepService.delete(id);
        grid.setItems(stepService.getAll());
    }

    private void editStep(Long id)
    {
        UI.getCurrent().navigate("EditStep");
    }


    private void export(Long id)
    {

    }
}
