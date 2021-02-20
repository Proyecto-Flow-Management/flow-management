package com.proyecto.flowmanagement.ui.views.panels;

import com.proyecto.flowmanagement.backend.commun.CustomNode;
import com.proyecto.flowmanagement.backend.commun.ValidationDTO;
import com.proyecto.flowmanagement.backend.persistence.entity.Guide;
import com.proyecto.flowmanagement.backend.service.Impl.ValidationServiceImpl;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.provider.hierarchy.TreeData;
import com.vaadin.flow.data.provider.hierarchy.TreeDataProvider;

import java.util.LinkedList;
import java.util.List;

public class ValidatorPanel extends HorizontalLayout {
    HorizontalLayout validatorSecctionLayout = new HorizontalLayout();
    Accordion accordion = new Accordion();
    FormLayout basicInformation = new FormLayout();
    Grid<String> erroresValidacion = new Grid<>();
    Grid<String> erroresValidacionesXML = new Grid<>();

    TreeGrid<ValidationDTO> arbol = new TreeGrid<ValidationDTO>(ValidationDTO.class);
    TreeDataProvider<ValidationDTO> dataProvider = (TreeDataProvider<ValidationDTO>) arbol.getDataProvider();
    TreeData<ValidationDTO> data = dataProvider.getTreeData();

    public ValidatorPanel()
    {
        configureGrid();

        configureForm();

        this.accordion.setVisible(false);
    }

    private void configureForm() {
        basicInformation.setWidthFull();
        validatorSecctionLayout.setWidthFull();
        validatorSecctionLayout.setId("step-Layout");
        setWidthFull();
        basicInformation.add(validatorSecctionLayout);
        accordion.close();
        accordion.add("Errores Guia", basicInformation);
        add(accordion);
    }

    private void configureGrid() {
        erroresValidacionesXML.setVisible(false);
        validatorSecctionLayout = new HorizontalLayout();
        validatorSecctionLayout.add(arbol);
        arbol.removeAllColumns();
        arbol.addColumn("label");
        arbol.addComponentColumn(this::getFoo).setHeader(new Span("Header Component")).setId("someID");
        arbol.setHierarchyColumn("label");
    }

    public void actualizarGrilla(Guide guide)
    {
        ValidationServiceImpl validationService = new ValidationServiceImpl();

        ValidationDTO validacion =validationService.validarGuia(guide);
        actualizarGrid(validacion);

        if(validacion.getValidationDTOList().size() > 0 || validacion.getError().size()>0)
        {
            Span mensaje = new Span("La guia tiene problemas de validacion. Por favor ver seccion de validacion");
            Notification notification = new Notification(mensaje);
            notification.setDuration(3000);
            notification.setPosition(Notification.Position.MIDDLE);
            notification.open();
            this.accordion.setVisible(true);
        }
        else
        {
            Span mensaje = new Span("La Guia esta correcta");
            Notification notification = new Notification(mensaje);
            notification.setDuration(3000);
            notification.setPosition(Notification.Position.MIDDLE);
            notification.open();
            this.accordion.setVisible(false);
        }

    }

    private void actualizarGrid(ValidationDTO validacion)
    {
        data.clear();

        arbol.getDataProvider().refreshAll();

        data.addItem(null,validacion);

        if(validacion.getError().size()>0)
        {
            ValidationDTO error = new ValidationDTO();
            error.setLabel("Errores");
            data.addItem(validacion,error);

            for (String str: validacion.getError())
            {
                ValidationDTO errorStep = new ValidationDTO();
                errorStep.setLabel(str);
                data.addItem(error, errorStep);
            }
        }

        agregarSteps(validacion);

        agregarOperation(validacion);

        arbol.getDataProvider().refreshAll();
    }

    private void agregarOperation(ValidationDTO validacion)
    {
        if(validacion.getValidationDTOListSecond().size()>0)
        {
            ValidationDTO operation = new ValidationDTO();
            operation.setLabel("Operations");
            data.addItem(validacion,operation);

            for (ValidationDTO val : validacion.getValidationDTOList())
            {
                ValidationDTO operationAux = new ValidationDTO();
                operationAux.setLabel(val.getLabel());
                data.addItem(operation,operationAux);
                agregarOperationPuntual(operationAux, val);
            }
        }
    }

    private void agregarOperationPuntual(ValidationDTO operationAux, ValidationDTO val)
    {
        if(val.getError().size()>0)
        {
            ValidationDTO error = new ValidationDTO();
            error.setLabel("Errores");
            data.addItem(operationAux,error);

            for (String str: val.getError())
            {
                ValidationDTO errorStep = new ValidationDTO();
                errorStep.setLabel(str);
                data.addItem(error, errorStep);
            }
        }
    }

    private void agregarSteps(ValidationDTO validacion)
    {
        if(validacion.getValidationDTOList().size()>0)
        {
            ValidationDTO steps = new ValidationDTO();
            steps.setLabel("Steps");
            data.addItem(validacion,steps); 

            for (ValidationDTO val : validacion.getValidationDTOList())
            {
                ValidationDTO stepsAux = new ValidationDTO();
                stepsAux.setLabel(val.getLabel());
                data.addItem(steps,stepsAux);
                agregarStepsPuntual(stepsAux, val);
            }
        }
    }

    private void agregarStepsPuntual(ValidationDTO steps, ValidationDTO val)
    {
        if(val.getError().size()>0)
        {
            ValidationDTO error = new ValidationDTO();
            error.setLabel("Errores");
            data.addItem(steps,error);

            for (String str: val.getError())
            {
                ValidationDTO errorStep = new ValidationDTO();
                errorStep.setLabel(str);
                data.addItem(error, errorStep);
            }
        }

        if(val.getValidationDTOListSecond().size()>0)
        {
            ValidationDTO operation = new ValidationDTO();
            operation.setLabel("Operations");
            data.addItem(val,operation);

            for (ValidationDTO opAux : val.getValidationDTOList())
            {
                ValidationDTO operationAux = new ValidationDTO();
                operationAux.setLabel(opAux.getLabel());
                data.addItem(operation,operationAux);
                agregarOperationPuntual(operationAux, opAux);
            }
        }

        if(val.getValidationDTOList().size()>0)
        {
            ValidationDTO alternative = new ValidationDTO();
            steps.setLabel("Alternative");
            data.addItem(steps,alternative);

            for (ValidationDTO alt : val.getValidationDTOList())
            {
                agregarAlternativsPuntuales(alternative, alt);
            }
        }
    }

    private void agregarAlternativsPuntuales(ValidationDTO alternative, ValidationDTO alt)
    {

        if(alt.getError().size()>0)
        {
            ValidationDTO error = new ValidationDTO();
            error.setLabel("Errores");
            data.addItem(alternative,error);

            for (String str: alt.getError())
            {
                ValidationDTO errorStep = new ValidationDTO();
                errorStep.setLabel(str);
                data.addItem(error, errorStep);
            }
        }

        if(alt.getValidationDTOList().size()>0)
        {
            ValidationDTO alternativePuntual = new ValidationDTO();
            alternativePuntual.setLabel("Alternative");
            data.addItem(alternative,alternativePuntual);

            for (ValidationDTO alter : alt.getValidationDTOList())
            {
                agregarAlternativsPuntuales(alternativePuntual, alter);
            }
        }
    }


    private void agregarConditions(ValidationDTO condiciones)
    {
        if(condiciones.getError().size()>0)
        {
            ValidationDTO error = new ValidationDTO();
            error.setLabel("Errores");
            data.addItem(condiciones,error);

            for (String str: condiciones.getError())
            {
                ValidationDTO errorCondition= new ValidationDTO();
                errorCondition.setLabel(str);

                data.addItem(error, errorCondition);
            }
        }

        for (ValidationDTO cond: condiciones.getValidationDTOList())
        {
            data.addItem(condiciones, cond);
            agregarConditions(cond);
        }
    }


    public Component getFoo(ValidationDTO s) {
        Div div = new Div();
        div.setText(s.getLabel());
        return div;
    }
}
