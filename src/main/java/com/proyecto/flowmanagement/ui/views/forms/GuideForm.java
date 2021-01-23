package com.proyecto.flowmanagement.ui.views.forms;

import com.proyecto.flowmanagement.backend.persistence.entity.Guide;
import com.proyecto.flowmanagement.backend.persistence.entity.Step;
import com.proyecto.flowmanagement.backend.service.Impl.GuideServiceImpl;
import com.proyecto.flowmanagement.ui.MainLayout;
import com.proyecto.flowmanagement.ui.views.grids.OperationGridForm;
import com.proyecto.flowmanagement.ui.views.grids.StepGridForm;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@org.springframework.stereotype.Component
@Route(value = "CreateGuide", layout = MainLayout.class)
@PageTitle("CreateGuide | Flow Management")
public class GuideForm extends VerticalLayout {
    private Guide guide;

    List <String> createGuides;
    List<String> guideNameList = new LinkedList<>();

    HorizontalLayout guideLayout;
    HorizontalLayout stepSecctionLayout;
    HorizontalLayout operationSecctionLayout;
    HorizontalLayout actionsLayout;

    Button crearGuia;

    HorizontalLayout layoutGuide;
    TextField name;
    TextField label;
    ComboBox<String> mainStep;

    StepGridForm stepGridForm;
    OperationGridForm operationGridForm;

    public GuideForm(GuideServiceImpl guideService)
    {
        configureGuideElements();

        configureStepElements();

        configureOperationElements();
        
        configureDocumentsElements();

        createForm();
}

    private void configureDocumentsElements() {
    }

    private void configureOperationElements() {
        operationGridForm = new OperationGridForm();
        operationGridForm.setWidthFull();

        operationSecctionLayout = new HorizontalLayout();
        operationSecctionLayout.setWidthFull();

        operationSecctionLayout.add(operationGridForm);
    }

    private void createForm() {
        crearGuia = new Button("Crear Guia");
//        crearGuia.addClickListener(buttonClickEvent -> crearGuia());
        crearGuia.addClickListener(buttonClickEvent -> save());
        HorizontalLayout botonCrear = new HorizontalLayout();

        Span content = new Span("Guia creada y archivo XML generado!");
        Notification notification = new Notification(content);
        notification.setDuration(3000);
        notification.setPosition(Notification.Position.MIDDLE);

        Button button = new Button("Crear Guia");
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        button.addClickListener(event -> notification.open());

        botonCrear.add(button);
//        botonCrear.add(crearGuia);
        add(guideLayout,stepSecctionLayout,operationSecctionLayout,botonCrear);
    }

    private void showNotification(String message){
        Span content = new Span(message);
        Notification notification = new Notification(content);
        notification.setDuration(3000);
        notification.setPosition(Notification.Position.MIDDLE);
        notification.open();
    }

    public void save(){
        if(!emptyForm()){
            guide.setLabel(label.getValue());
            guide.setName(name.getValue());
            guide.setSteps(stepGridForm.getSteps());
            guide.setOperations(operationGridForm.getOperations());
            createGuides = stepGridForm.getCreateGuides();
        }
    }

    public boolean emptyForm(){
        boolean result = false;

        if(name.getValue().isEmpty() &&
                mainStep.isEmpty() &&
                label.getValue().isEmpty())
            result = true;

        return result;
    }

    public List<String> isValid(){
        List <String> valoresFields = validateFields();
        List <String> valoresGrids = validateGrids();

        List<String> valores = Stream.concat(valoresFields.stream(), valoresGrids.stream())
                .collect(Collectors.toList());

        return valores;
    }

    public List<String> validateFields(){
        List<String> valores = new LinkedList<>();

        if(name.getValue().trim() == ""){
            valores.add("El campo Name es obligatorio.");
        }
        if(mainStep.isEmpty()){
            valores.add("El campo mainStep es obligatorio.");
        }
        if(label.getValue().trim() == ""){
            valores.add("El campo Label es obligatorio.");
        }

        return valores;
    }

    public List<String> validateGrids() {
        List<String> valores = new LinkedList<>();

        if (stepGridForm.getSteps().size() == 0){
            valores.add("Se debe tener al menos 1 step");
        }

        return valores;
    }

    public void setGuideNameList (List guideNameList){
        this.guideNameList = guideNameList;
        stepGridForm.setGuideNameList(this.guideNameList);
    }


    private void crearGuia() {

        Guide newGuide = new Guide();

        newGuide.setName(name.getValue());
        newGuide.setLabel(label.getValue());
        newGuide.setMainStep(mainStep.getValue());

        newGuide.setSteps(stepGridForm.getSteps());
        newGuide.setOperations(operationGridForm.getOperations());

        GuideServiceImpl guideService = new GuideServiceImpl();
        guideService.add(newGuide);
    }

    private void configureGuideElements() {
        H1 guidelabel = new H1("Elementos de Guia");
        guidelabel.addClassName("logo");
        add(guidelabel);

        guideLayout = new HorizontalLayout();
        name = new TextField("Nombre Guia");
        label = new TextField("Label Guia");
        name.setRequired(true);
        name.setErrorMessage("Este campo es obligatorio.");
        label.setRequired(true);
        label.setErrorMessage("Este campo es obligatorio.");
        mainStep = new ComboBox<>("Empezar en:");
        mainStep.setPlaceholder("Id Step Principal");
        mainStep.setRequired(true);
        mainStep.setErrorMessage("Debes seleccionar un Step Principal.");

        guideLayout.add(name,label,mainStep);
    }

    private void configureStepElements() {
        stepGridForm = new StepGridForm();
        stepGridForm.setWidthFull();
        stepGridForm.getStepGrid().asSingleSelect().addValueChangeListener(evt -> updateComboMainStep());
        stepGridForm.getSaveButton().addClickListener(evt -> updateComboMainStep());
        stepGridForm.setGuideNameList(this.guideNameList);
        stepSecctionLayout = new HorizontalLayout();
        stepSecctionLayout.setWidthFull();
        stepSecctionLayout.add(stepGridForm);
    }

    private void updateComboMainStep() {
        List<String> mainStepIds = new LinkedList<>();
        for (Step step :stepGridForm.getSteps()){
            mainStepIds.add(step.getTextId());
        }
        mainStep.setItems(mainStepIds);
    }
}