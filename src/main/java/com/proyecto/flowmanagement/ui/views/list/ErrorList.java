package com.proyecto.flowmanagement.ui.views.list;

import com.proyecto.flowmanagement.backend.persistence.entity.Errores;
import com.proyecto.flowmanagement.backend.persistence.entity.User;
import com.proyecto.flowmanagement.backend.service.Impl.ErroresServiceImpl;
import com.proyecto.flowmanagement.ui.MainLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.List;

//@Component
@Route(value = "ErrorList", layout = MainLayout.class)
@PageTitle("Errores | Flow Management")
public class ErrorList  extends VerticalLayout {

    ErroresServiceImpl erroresService;

    Grid<Errores> grid = new Grid<>(Errores.class);

    public ErrorList(ErroresServiceImpl erroresService)
    {
        this.erroresService = erroresService;
        setWidthFull();

        configureGrid();
        add(grid);
    }

    private void configureGrid()
    {
        grid.removeAllColumns();
        grid.setColumns("error");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        List<Errores> erroresList = erroresService.getAll();
        grid.setItems(erroresList);
    }
}
