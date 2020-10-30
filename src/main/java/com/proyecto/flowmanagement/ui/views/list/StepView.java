package com.proyecto.flowmanagement.ui.views.list;

import com.proyecto.flowmanagement.backend.persistence.entity.Step;
import com.proyecto.flowmanagement.backend.service.Impl.StepServiceImpl;
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
@Route(value = "step", layout = MainLayout.class)
@PageTitle("Step | Flow Management")
public class StepView extends VerticalLayout {
    private final StepForm form;
    Grid<Step> grid = new Grid<>(Step.class);
    TextField filterText = new TextField();

    private StepServiceImpl stepService;

    public StepView(StepServiceImpl stepService) {
        this.stepService = stepService;
        addClassName("list-view");
        setSizeFull();
        configureGrid();

        form = new StepForm();
        form.addListener(StepForm.SaveEvent.class, this::saveContact);
        form.addListener(StepForm.DeleteEvent.class, this::deleteContact);
        form.addListener(StepForm.CloseEvent.class, e -> closeEditor());

        Div content = new Div(grid, form);
        content.addClassName("content");
        content.setSizeFull();

        add(getToolBar(), content);
        updateList();
        closeEditor();
    }

    private void deleteContact(StepForm.DeleteEvent evt) {
        stepService.delete(evt.getStep());
        updateList();
        closeEditor();
    }

    private void saveContact(StepForm.SaveEvent evt) {
        stepService.save(evt.getStep());
        updateList();
        closeEditor();
    }

    private void closeEditor() {
        form.setStep(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private HorizontalLayout getToolBar() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addStepButton = new Button("Add Step", click -> addStep());

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addStepButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void addStep() {
        grid.asSingleSelect().clear();
        editStep(new Step());
    }

    private void updateList() {
        grid.setItems(stepService.getAll());
    }

    private void  configureGrid() {
        grid.addClassName("step-grid");
        grid.setSizeFull();
//        grid.setColumns("name", "label");
//        grid.setColumns("name", "label", "nextStep");

        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(evt -> editStep(evt.getValue()));
    }

    private void editStep(Step step) {
        if(step == null) {
            closeEditor();
        } else {
            form.setStep(step);
            form.setVisible(true);
            addClassName("editing");
        }
    }
}
