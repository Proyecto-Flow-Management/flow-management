package com.proyecto.flowmanagement.ui.views.list;

import com.proyecto.flowmanagement.backend.persistence.entity.Guide;
import com.proyecto.flowmanagement.backend.service.Impl.GuideGeneratorServiceImp;
import com.proyecto.flowmanagement.backend.service.Impl.GuideServiceImpl;
import com.proyecto.flowmanagement.backend.service.Impl.MockTestServiceImpl;
import com.proyecto.flowmanagement.backend.service.Impl.StepServiceImpl;
import com.proyecto.flowmanagement.ui.MainLayout;
import com.proyecto.flowmanagement.ui.views.forms.GuideForm;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamRegistration;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.stereotype.Component;
import org.vaadin.stefan.LazyDownloadButton;

import java.io.ByteArrayInputStream;
import java.util.List;

@Component
@Route(value = "GuideList", layout = MainLayout.class)
@PageTitle("Crear Guia | Flow Management")
public class GuideList extends VerticalLayout {

    Grid<Guide> grid = new Grid<>(Guide.class);
    GuideServiceImpl guideService;

    public GuideList(GuideServiceImpl guideService) {
        this.guideService = guideService;
        //test();
        addClassName("create-guide-view");
        setSizeFull();
        configureGrid();

        Div content = new Div(grid);
        content.addClassName("content");
        content.setSizeFull();

        add(getToolBar(), content);
        updateList();
        grid.setItems(guideService.getAll());
    }

    private HorizontalLayout getToolBar() {
        Button addGuideButton = new Button("Crear Guia", event -> UI.getCurrent().navigate("CreateGuide"));
        HorizontalLayout toolbar = new HorizontalLayout(addGuideButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }


    public void test(){
        GuideGeneratorServiceImp test = new GuideGeneratorServiceImp();
        MockTestServiceImpl mock = new MockTestServiceImpl();
        List<Guide> myGuide = guideService.getAll();
        if (!myGuide.isEmpty())
        {
            test.GuidePrint(myGuide.get(0));
        }
    }

    private void updateList() {
        grid.setColumns("name", "label", "mainStep");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.addComponentColumn(guide ->{
                    Icon icon = new Icon("vaadin", "edit");
                    Button botonEditar = new Button("", event -> UI.getCurrent().navigate("CrearGuia/" + guide.getId()));
                    botonEditar.setIcon(icon);
                    return  botonEditar;
                }).setHeader("Editar");

        grid.addComponentColumn(guide ->{
                    Icon icon = new Icon("vaadin", "trash");
                    Button botonEliminar = new Button(icon);
                    botonEliminar.addClickListener(e -> {
                        deleteGuide(guide.getId());
                    });
                    return  botonEliminar;
        }).setHeader("Eliminar");

        grid.addComponentColumn(guide ->{
                    Icon icon = new Icon("vaadin", "external-link");
//                    Button botonEliminar = new Button(icon);
                    LazyDownloadButton downloadButton = new LazyDownloadButton("",
                            () -> guide.getName() + ".txt",
                            () -> new ByteArrayInputStream(guide.getName().getBytes()) // ... create the input stream here
                    );
                    downloadButton.setIcon(icon);

//                    botonEliminar.addClickListener(e -> {
//                        export(guide.getId());
//                    });
                    return  downloadButton;
        }).setHeader("Exportar");
    }

    private void  configureGrid() {
        grid.addClassName("guide-grid");
        grid.setSizeFull();
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
    }

    private void deleteGuide(Long id)
    {
        guideService.delete(id);
        grid.setItems(guideService.getAll());
    }

    private void editGuide(Long id)
    {
        UI.getCurrent().navigate("EditGuide");
    }


    private void export(Long id)
    {
        guideService.delete(id);
    }

}