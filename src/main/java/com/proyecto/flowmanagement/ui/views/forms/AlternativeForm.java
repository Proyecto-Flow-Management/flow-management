package com.proyecto.flowmanagement.ui.views.forms;

import com.proyecto.flowmanagement.backend.persistence.entity.Alternative;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.shared.Registration;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@CssImport("./styles/alternative-form.css")
public class AlternativeForm extends VerticalLayout {

    public boolean editing;
    private Alternative alternative;
    private String createGuide;
    List<String> stepIdList;
    List<String> guideNameList;

    ConditionForm conditionForm = new ConditionForm();

    TextField label = new TextField("Label Alternative");
    ComboBox transitionCombo = new ComboBox("Transición");
    TextField guideNameText = new TextField("guideName");
    TextField stepIdText = new TextField("stepId");

    public Button save = new Button("Guardar");
    Button delete = new Button("Eliminar");
    public Button close = new Button("Cancelar");


    public AlternativeForm() {
        setClassName("alternativeSection");
        configureElements();
        editing = false;
    }

    public void configureElements() {

        save.addClickListener(buttonClickEvent -> save());
        transitionCombo.addValueChangeListener(event -> showTransitionFields());

        VerticalLayout form = new VerticalLayout();

        HorizontalLayout elements = new HorizontalLayout();

        HorizontalLayout conditionsLayout = new HorizontalLayout();

        HorizontalLayout actionsLayout = new HorizontalLayout();

        this.createGuide = "";
        this.label.setRequired(true);
        this.label.setErrorMessage("Este campo es requerido para exportar la guía.");
        this.guideNameText.setRequired(true);
        this.guideNameText.setErrorMessage("Este campo es requerido para exportar la guía.");
        this.stepIdText.setRequired(true);
        this.stepIdText.setErrorMessage("Este campo es requerido para exportar la guía.");
        this.transitionCombo.setItems("guideName","stepId");
        this.transitionCombo.setValue("guideName");

        elements.add(label, transitionCombo, stepIdText, guideNameText);

        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        actionsLayout.add(save,close,delete);

        conditionsLayout.setWidthFull();

        conditionsLayout.add(conditionForm);

        form.add(elements,conditionsLayout, actionsLayout);

        add(form);
    }

    private void showTransitionFields() {
        if (this.transitionCombo.getValue() == "guideName"){
            this.guideNameText.setVisible(true);
            this.stepIdText.setVisible(false);
        }
        else if (this.transitionCombo.getValue() == "stepId"){
            this.guideNameText.setVisible(false);
            this.stepIdText.setVisible(true);
        }
    }


    private void showNotification(String message){
        Span content = new Span(message);
        Notification notification = new Notification(content);
        notification.setDuration(3000);
        notification.setPosition(Notification.Position.MIDDLE);
        notification.open();
    }

    private void save(){
        if (!emptyForm()){
            this.alternative = new Alternative();
            alternative.setLabel(this.label.getValue());
            if (this.transitionCombo.getValue() == "guideName") {
                alternative.setGuideName(this.guideNameText.getValue());
                createGuide = this.guideNameText.getValue();
            }
            else if (this.transitionCombo.getValue() == "stepId"){
                alternative.setNextStep(this.stepIdText.getValue());
            }
            if (this.transitionCombo.getValue() == "stepId" && this.stepIdText.getValue().trim() != "") {
                if (!stepIdList.contains(this.stepIdText.getValue().trim())){
                    showNotification("Recuerda crear el step: " + this.stepIdText.getValue().trim());
                }
            }
            if (this.transitionCombo.getValue() == "guideName" && this.guideNameText.getValue().trim() != "") {
                if (!guideNameList.contains(this.guideNameText.getValue().trim())){
                    showNotification("Recuerda crear la guía: " + this.guideNameText.getValue().trim());
                }
            }
        }
        else{
            showNotification("Debes completar al menos un campo. (Label Alternative, guideName o stepId)");
        }
    }

    public boolean emptyForm(){
        boolean result = false;

        if(this.label.getValue().isEmpty() &&
                (((this.transitionCombo.getValue() == "guideName" && this.guideNameText.getValue().isEmpty()))
                || (this.transitionCombo.getValue() == "stepId" && this.stepIdText.getValue().isEmpty())))
            result = true;

        return result;
    }


    public void setAlternative(Alternative alternative) {
        if (alternative != null)
        {
            this.alternative = alternative;
            if (this.alternative.getGuideName()!=null){
                this.transitionCombo.setValue("guideName");
                this.createGuide = this.alternative.getGuideName();
                this.guideNameText.setValue(alternative.getGuideName());
                this.guideNameText.setVisible(true);
                this.stepIdText.setVisible(false);
            }
            else if (this.alternative.getNextStep()!=null){
                this.transitionCombo.setValue("stepId");
                this.stepIdText.setValue(alternative.getNextStep());
                this.stepIdText.setVisible(true);
                this.guideNameText.setVisible(false);
            }
            this.label.setValue(alternative.getLabel());
            this.conditionForm.setConditions(alternative);
            editing = true;
            delete.setVisible(true);
        }
        else
        {
            this.guideNameText.setValue("");
            this.stepIdText.setValue("");
            this.label.setValue("");
            this.createGuide = "";
            this.conditionForm.setAsDefault();
        }
    }

    public String getCreateGuide(){
        return this.createGuide;
    }



    public List<String> isValid(){
        List <String> valoresFields = validateFields();
        List <String> valoresTransition = validateTransition();

        List<String> valores = Stream.concat(valoresFields.stream(), valoresTransition.stream())
                .collect(Collectors.toList());

        return valores;
    }

    public List<String> validateFields(){
        List<String> valores = new LinkedList<>();

        if(label.getValue().trim() == ""){
            valores.add("El campo Label es obligatorio.");
        }
        if (this.transitionCombo.getValue() == "guideName" && this.guideNameText.getValue().trim() == ""){
            valores.add("El campo guideName es obligatorio.");
        }
        if (this.transitionCombo.getValue() == "stepId" && this.stepIdText.getValue().trim() == ""){
            valores.add("El campo stepId es obligatorio.");
        }

        return valores;
    }

    public List<String> validateTransition(){
        List<String> valores = new LinkedList<>();

        if (this.transitionCombo.getValue() == "guideName" && this.guideNameText.getValue().trim() != ""){
            if (!guideNameList.contains(this.guideNameText.getValue().trim())){
                valores.add("Se debe crear la guía: " + this.guideNameText.getValue().trim());
            }
        }
        if (this.transitionCombo.getValue() == "stepId" && this.stepIdText.getValue().trim() != "") {
            if (!stepIdList.contains(this.stepIdText.getValue().trim())){
                valores.add("Se debe crear el step: " + this.stepIdText.getValue().trim());
            }
        }

        return valores;
    }

    public void setStepIdList(List<String> stepIdList){
        this.stepIdList = stepIdList;
    }

    public void setGuideNameList(List<String> guideNameList){
        this.guideNameList = guideNameList;
    }

    public Alternative getAlternative()
    {
        this.alternative.setConditions(conditionForm.getUnaryConditions());
        this.alternative.setBinaryConditions(conditionForm.getBinaryConditions());
        return this.alternative;
    }

    // Events
    public static abstract class AlternativeFormEvent extends ComponentEvent<AlternativeForm> {
        private Alternative alternative;

        protected AlternativeFormEvent(AlternativeForm source, Alternative alternative) {
            super(source, false);
            this.alternative = alternative;
        }

        public Alternative getAlternative() {
            return alternative;
        }
    }

    public static class SaveEvent extends AlternativeForm.AlternativeFormEvent {
        SaveEvent(AlternativeForm source, Alternative alternative) {
            super(source, alternative);
        }
    }

    public static class DeleteEvent extends AlternativeForm.AlternativeFormEvent {
        DeleteEvent(AlternativeForm source, Alternative alternative) {
            super(source, alternative);
        }

    }

    public static class CloseEvent extends AlternativeForm.AlternativeFormEvent {
        CloseEvent(AlternativeForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
