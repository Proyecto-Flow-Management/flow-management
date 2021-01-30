package com.proyecto.flowmanagement.ui.views.panels;

import com.proyecto.flowmanagement.backend.persistence.entity.Guide;
import com.proyecto.flowmanagement.backend.service.Impl.ValidationServiceImpl;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import java.util.List;

public class ValidatorPanel extends HorizontalLayout {
    HorizontalLayout validatorSecctionLayout = new HorizontalLayout();
    Accordion accordion = new Accordion();
    FormLayout basicInformation = new FormLayout();
    Grid<String> erroresValidacion = new Grid<>();

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
        validatorSecctionLayout = new HorizontalLayout();
        validatorSecctionLayout.add(erroresValidacion);
        erroresValidacion.addColumn(value-> {  return value; }).setHeader("GroupName").setSortable(true);
    }

    public void actualizarGrilla(Guide guide)
    {
        ValidationServiceImpl validationService = new ValidationServiceImpl();

        List<String> errores = validationService.validarGuia(guide);

        this.erroresValidacion.setItems(errores);

        if(errores.size() > 0)
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
}
