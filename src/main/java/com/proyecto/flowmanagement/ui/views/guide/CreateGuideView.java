package com.proyecto.flowmanagement.ui.views.guide;

import com.proyecto.flowmanagement.backend.persistence.entity.Guide;
import com.proyecto.flowmanagement.backend.persistence.entity.Rol;
import com.proyecto.flowmanagement.backend.persistence.entity.Step;
import com.proyecto.flowmanagement.backend.service.Impl.GuideServiceImpl;
import com.proyecto.flowmanagement.backend.service.Impl.StepServiceImpl;
import com.proyecto.flowmanagement.ui.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Route(value = "create_guide", layout = MainLayout.class)
@PageTitle("Crear Guia | Flow Management")
public class CreateGuideView extends VerticalLayout {

    private final GuideForm form;
    Grid<Guide> grid = new Grid<>(Guide.class);

    GuideServiceImpl guideService;
    StepServiceImpl stepService;

    public CreateGuideView(GuideServiceImpl guideService, StepServiceImpl stepService) {
        this.guideService = guideService;
        this.stepService = stepService;
        addClassName("create-guide-view");
        setSizeFull();
        configureGrid();


        form = new GuideForm(stepService.getAll());
        form.addListener(GuideForm.SaveEvent.class, this::saveGuide);
        form.addListener(GuideForm.DeleteEvent.class, this::deleteGuide);
        form.addListener(GuideForm.CloseEvent.class, e -> closeEditor());

        Div content = new Div(grid, form);
        content.addClassName("content");
        content.setSizeFull();

        add(getToolBar(), content);
        updateList();
        closeEditor();
    }

    private void deleteGuide(GuideForm.DeleteEvent evt) {
        guideService.delete(evt.getGuide());
        updateList();
        closeEditor();
    }

    private void saveGuide(GuideForm.SaveEvent evt) {
        guideService.add(evt.getGuide());
        updateList();
        closeEditor();
    }

    private void closeEditor() {
        form.setGuide(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private HorizontalLayout getToolBar() {
        /*filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());*/

        Button addGuideButton = new Button("Crear Guia", click -> addGuide());

        HorizontalLayout toolbar = new HorizontalLayout(addGuideButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void addGuide() {
        grid.asSingleSelect().clear();
        editGuide(new Guide());
    }

    private void updateList() {
        grid.setItems(guideService.getAll());
    }

    private void  configureGrid() {
        grid.addClassName("guide-grid");
        grid.setSizeFull();
        grid.setColumns("name","mainStep");
        grid.addColumn(guide-> {
            Set<Step> steps = guide.getSteps();
            String txt = "";
            for (Step step:steps
                 ) {
                if (txt == ""){
                    txt = txt + step.getName();
                }
                else{
                    txt = txt + " ,";
                    txt = txt + step.getName();
                }
            }
            return steps == null ? "-" : txt;
        }).setHeader("Steps").setSortable(true);

        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(evt -> editGuide(evt.getValue()));

    }

    private void editGuide(Guide guide) {
        if(guide == null) {
            closeEditor();
        } else {
            form.setGuide(guide);
            form.setVisible(true);
            addClassName("editing");
        }
    }

}