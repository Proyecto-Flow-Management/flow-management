package com.proyecto.flowmanagement.ui.views.forms;

import com.proyecto.flowmanagement.backend.persistence.entity.Alternative;
import com.proyecto.flowmanagement.backend.persistence.entity.Guide;
import com.proyecto.flowmanagement.backend.persistence.entity.Operation;
import com.proyecto.flowmanagement.backend.persistence.entity.Step;
import com.proyecto.flowmanagement.backend.service.Impl.GuideServiceImpl;
import com.proyecto.flowmanagement.backend.service.Impl.OperationServiceImpl;
import com.proyecto.flowmanagement.backend.service.Impl.StepServiceImpl;
import com.proyecto.flowmanagement.ui.MainLayout;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@org.springframework.stereotype.Component
@Route(value = "CreateGuide", layout = MainLayout.class)
@PageTitle("CreateGuide | Flow Management")
public class GuideForm extends VerticalLayout {

    private final StepForm stepForm;
    private final OperationForm operationForm;
    private final AlternativeForm alternativeForm;
    private Guide guide;
    private GuideServiceImpl guideService;
    private StepServiceImpl stepService;
    private OperationServiceImpl operationService;

    TextField name = new TextField("Nombre de la Guia");
    TextField label = new TextField("Etiqueta");
    TextField mainStep = new TextField("Paso principal");

    List<Step> stepList = new ArrayList<Step>();
    List<Operation> operationList = new ArrayList<Operation>();
    List<Alternative> alternativeList = new ArrayList<Alternative>();

    Grid<Step> gridStep = new Grid<>(Step.class);
    Grid<Operation> gridOperation = new Grid<>(Operation.class);
    Grid<Alternative> gridAlternative = new Grid<>(Alternative.class);
    Div messageDiv = new Div();
    Div messageDiv2 = new Div();
    Div messageDiv3 = new Div();

    Button save = new Button("Guardar");
    Button close = new Button("Cancelar");

    Binder<Guide> binder = new BeanValidationBinder<>(Guide.class);

    public GuideForm(List<Step> steps, GuideServiceImpl guideService, StepServiceImpl stepService, OperationServiceImpl operationService) {
        addClassName("guide-form");
        this.guideService = guideService;
        this.stepService = stepService;
        this.operationService = operationService;

        setSizeFull();
        configureGrids();

        stepForm = new StepForm();
        stepForm.addListener(StepForm.SaveEvent.class, this::saveStep);
        stepForm.addListener(StepForm.DeleteEvent.class, this::deleteStep);
        stepForm.addListener(StepForm.CloseEvent.class, e -> closeEditorStep());
        stepForm.setVisible(false);

        operationForm = new OperationForm();
        operationForm.addListener(OperationForm.SaveEvent.class, this::saveOperation);
        operationForm.addListener(OperationForm.DeleteEvent.class, this::deleteOperation);
        operationForm.addListener(OperationForm.CloseEvent.class, e -> closeEditorOperation());
        operationForm.setVisible(false);

        alternativeForm = new AlternativeForm();
        alternativeForm.addListener(AlternativeForm.SaveEvent.class, this::saveAlternative);
        alternativeForm.addListener(AlternativeForm.DeleteEvent.class, this::deleteAlternative);
        alternativeForm.addListener(AlternativeForm.CloseEvent.class, e -> closeEditorAlternative());
        alternativeForm.setVisible(false);

        HorizontalLayout contentFields = new HorizontalLayout(name,label,mainStep);

//        Div contentStep = new Div(gridStep, stepForm);
        HorizontalLayout contentStep = new HorizontalLayout(gridStep,stepForm);
        contentStep.addClassName("content");
        contentStep.setSizeFull();
        HorizontalLayout contentOperation = new HorizontalLayout(gridOperation, operationForm);
        contentOperation.addClassName("contentOperation");
        contentOperation.setSizeFull();
        HorizontalLayout contentAlternative = new HorizontalLayout(gridAlternative, alternativeForm);
        contentAlternative.addClassName("contentAlternative");
        contentAlternative.setSizeFull();

        updateList();
//        gridStep.setItems(stepService.getAll());
        gridStep.setItems(stepList);
//        gridOperation.setItems(operationService.getAll());
        gridOperation.setItems(operationList);
        gridAlternative.setItems(alternativeList);

        binder.bindInstanceFields(this);
        generateStyle();

        HorizontalLayout toolbarStep = getToolBarStep();
        toolbarStep.setAlignItems(Alignment.START);

        HorizontalLayout createButtons = createButtonsLayout();
        createButtons.setAlignItems(Alignment.CENTER);
        add(contentFields, toolbarStep, contentStep, messageDiv, getToolBarOperation(), contentOperation, messageDiv2, getToolBarAlternative(), contentAlternative, messageDiv3, createButtons);
//        add(name,label,mainStep, toolbarStep, contentStep, messageDiv, getToolBarOperation(), contentOperation, messageDiv2, contentAlternative, messageDiv3, createButtons);
//        add(name,label,mainStep,new H3("Steps:"), getToolBarStep(), contentStep, messageDiv , new H3("Operations"), contentOperation, messageDiv2, createButtonsLayout());
    }

    private void  configureGrids() {
        gridStep.addClassName("step-grid");
        gridStep.setSizeFull();
        gridStep.setSelectionMode(Grid.SelectionMode.MULTI);

        gridStep.asMultiSelect().addValueChangeListener(event -> {
            String selectedSteps = "";
            for (Step step : gridStep.getSelectedItems())
                selectedSteps += step.getText() + ", ";
            String message = String.format("Seleccionados actualmente: %s", selectedSteps);
//            String message = String.format("Seleccionados actualmente: %s", event.getValue());
            messageDiv.setText(message);
        });
        gridStep.getColumns().forEach(col -> col.setAutoWidth(true));

        gridOperation.addClassName("operation-grid");
        gridOperation.setSizeFull();
        gridOperation.setSelectionMode(Grid.SelectionMode.MULTI);

        gridOperation.asMultiSelect().addValueChangeListener(event -> {
            String selectedOperations = "";
            for (Operation operation : gridOperation.getSelectedItems())
                selectedOperations += operation.getName() + ", ";
            String message = String.format("Seleccionados actualmente: %s", selectedOperations);
//            String message = String.format("Seleccionados actualmente: %s", event.getValue());
            messageDiv2.setText(message);
        });
        gridOperation.getColumns().forEach(col -> col.setAutoWidth(true));

        gridAlternative.addClassName("alternative-grid");
        gridAlternative.setSizeFull();
        gridAlternative.setSelectionMode(Grid.SelectionMode.MULTI);

        gridAlternative.asMultiSelect().addValueChangeListener(event -> {
            String selectedAlternatives = "";
            for (Alternative alternative : gridAlternative.getSelectedItems())
                selectedAlternatives += alternative.getName() + ", ";
            String message = String.format("Seleccionados actualmente: %s", selectedAlternatives);
//            String message = String.format("Seleccionados actualmente: %s", event.getValue());
            messageDiv3.setText(message);
        });
        gridAlternative.getColumns().forEach(col -> col.setAutoWidth(true));
    }

    private void addStep() {
//        gridStep.asSingleSelect().clear();
        gridStep.asMultiSelect().clear();
        editStep(new Step());
    }

    private void editStep(Step step) {
        if(step == null) {
            closeEditorStep();
        } else {
            stepForm.setStep(step);
            stepForm.setVisible(true);
            addClassName("editing");
        }
    }

    private void deleteStep(StepForm.DeleteEvent evt) {
//        userService.delete(evt.getUser());
        updateList();
        closeEditorStep();
    }

    private void saveStep(StepForm.SaveEvent evt) {
        stepList.add(evt.getStep());
        gridStep.setItems(stepList);
//        gridStep.setItems(new Step());
//        gridStep.setItems(evt.getStep());
        gridStep.getDataProvider().refreshAll();
//        updateList();
        closeEditorStep();
    }

    private void closeEditorStep() {
        stepForm.setStep(null);
        stepForm.setVisible(false);
        removeClassName("editing");
    }

    private HorizontalLayout getToolBarStep() {
        Button addStepButton = new Button("Crear Step", click -> addStep());

        HorizontalLayout toolbar = new HorizontalLayout(addStepButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void addOperation() {
        gridOperation.asMultiSelect().clear();
        editOperation(new Operation());
    }

    private void editOperation(Operation operation) {
        if(operation == null) {
            closeEditorOperation();
        } else {
            operationForm.setOperation(operation);
            operationForm.setVisible(true);
            addClassName("editing");
        }
    }

    private void deleteOperation(OperationForm.DeleteEvent evt) {
//        userService.delete(evt.getUser());
        updateList();
        closeEditorOperation();
    }

    private void saveOperation(OperationForm.SaveEvent evt) {
        operationList.add(evt.getOperation());
        gridOperation.setItems(operationList);
        gridOperation.getDataProvider().refreshAll();
//        updateList();
        closeEditorOperation();
    }

    private void closeEditorOperation() {
        operationForm.setOperation(null);
        operationForm.setVisible(false);
        removeClassName("editing");
    }

    private HorizontalLayout getToolBarOperation() {
        Button addOperationButton = new Button("Crear Operation", click -> addOperation());

        HorizontalLayout toolbar = new HorizontalLayout(addOperationButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void addAlternative() {
        gridAlternative.asMultiSelect().clear();
        editAlternative(new Alternative());
    }

    private void editAlternative(Alternative alternative) {
        if(alternative == null) {
            closeEditorAlternative();
        } else {
            alternativeForm.setAlternative(alternative);
            alternativeForm.setVisible(true);
            addClassName("editing");
        }
    }

    private void deleteAlternative(AlternativeForm.DeleteEvent evt) {
//        userService.delete(evt.getUser());
        updateList();
        closeEditorAlternative();
    }

    private void saveAlternative(AlternativeForm.SaveEvent evt) {
        alternativeList.add(evt.getAlternative());
        gridAlternative.setItems(alternativeList);
        gridAlternative.getDataProvider().refreshAll();
//        updateList();
        closeEditorAlternative();
    }

    private void closeEditorAlternative() {
        alternativeForm.setAlternative(null);
        alternativeForm.setVisible(false);
        removeClassName("editing");
    }

    private HorizontalLayout getToolBarAlternative() {
        Button addAlternativeButton = new Button("Crear Alternative", click -> addAlternative());

        HorizontalLayout toolbar = new HorizontalLayout(addAlternativeButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void updateList() {
        gridStep.setColumns("text", "label");
        gridOperation.setColumns("name", "label");
        gridAlternative.setColumns("name", "label");
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        close.addClickListener(event -> UI.getCurrent().navigate("GuideList"));

        save.addClickListener(click -> validateAndSave());

        binder.addStatusChangeListener(evt -> save.setEnabled(binder.isValid()));

        return new HorizontalLayout(save, close);
    }

    private void validateAndSave() {
        try {
            CreateTest();
            UI.getCurrent().navigate("GuideList");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void generateStyle(){
//        setAlignItems(Alignment.CENTER);
        name.setWidth("60%");
        label.setWidth("60%");
        mainStep.setWidth("60%");
    }

    public void CreateTest(){

        Guide guideNew = new Guide();

        guideNew.setName(name.getValue());
        guideNew.setLabel(label.getLabel());

        List<Step> steps = new LinkedList<Step>();

        Step stepOne = new Step();

        List<Alternative> stepAlternative = new LinkedList<Alternative>();
        List<Operation> stepOperation = new LinkedList<Operation>();

        List<Alternative> guideAlternative = new LinkedList<Alternative>();
        List<Operation> guideOperation = new LinkedList<Operation>();

        Alternative alternative1Guide = new Alternative();
        Alternative alternative2Guide = new Alternative();

        Operation operation1Guide = new Operation();
        Operation operation2Guide = new Operation();

        alternative1Guide.setLabel("Label Alternative 1 Guide");
        alternative1Guide.setName("Name Alternative 1 Guide");
        alternative2Guide.setLabel("Label Alternative 2 Guide");
        alternative2Guide.setName("Name Alternative 2 Guide");
        guideAlternative.add(alternative1Guide);
        guideAlternative.add(alternative2Guide);
        guideNew.setAlternative(guideAlternative);

        operation1Guide.setLabel("Label Operation 1 Guide");
        operation1Guide.setName("Name Operation 1 Guide");
        operation2Guide.setLabel("Label Operation 2 Guide");
        operation2Guide.setName("Name Operation 2 Guide");

        guideOperation.add(operation1Guide);
        guideOperation.add(operation2Guide);
        guideNew.setOperation(guideOperation); 

        Alternative alternative1Step1 = new Alternative();
        Alternative alternative2Step1 = new Alternative();

        Operation operation1Step1 = new Operation();
        Operation operation2Step1 = new Operation();

        alternative1Step1.setLabel("Label Alternative 1 Step 1");
        alternative1Step1.setName("Name Alternative 1 Step 1");
        alternative2Step1.setLabel("Label Alternative 2 Step 1");
        alternative2Step1.setName("Name Alternative 2 Step 1");

        stepAlternative.add(alternative1Step1);
        stepAlternative.add(alternative2Step1);
        stepOne.setAlternative(stepAlternative);

        operation1Step1.setLabel("Label Operation 1 Step 1");
        operation1Step1.setName("Name Operation 1 Step 1");
        operation2Step1.setLabel("Label Operation 2 Step 1");
        operation2Step1.setName("Name Operation 2 Step 1");

        stepOperation.add(operation1Step1);
        stepOperation.add(operation2Step1);
        stepOne.setOperation(stepOperation);

        stepOne.setLabel("Label TEST 1");
        stepOne.setText("Name TEST 1");

        steps.add(stepOne);

        guideNew.setSteps(steps);

        guideService.add(guideNew);
    }
}