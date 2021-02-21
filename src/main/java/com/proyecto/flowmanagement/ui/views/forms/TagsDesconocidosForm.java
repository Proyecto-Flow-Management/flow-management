package com.proyecto.flowmanagement.ui.views.forms;

import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;

public class TagsDesconocidosForm   extends HorizontalLayout {

    VerticalLayout desconocidosLayout = new VerticalLayout();
    Button validarDesconocido = new Button("Validar");
    VerticalLayout contenedorDesconocidos = new VerticalLayout();
    Div mensajesError = new Div();
    public TextArea desconocidosText = new TextArea("Desconocidos");
    Accordion desconocidosAccordion = new Accordion();

    public TagsDesconocidosForm()
    {
        desconocidosLayout.setWidthFull();
        mensajesError.setClassName("error");
        mensajesError.setVisible(false);
        validarDesconocido.addClickListener(buttonClickEvent -> validarDesconocido());
        contenedorDesconocidos.setMinWidth("60%");
        contenedorDesconocidos.setMaxWidth("80%");
        desconocidosText.setWidthFull();
        mensajesError.setWidthFull();
        contenedorDesconocidos.add(validarDesconocido, desconocidosText, mensajesError);
        desconocidosLayout.add(contenedorDesconocidos);
        desconocidosAccordion.add("Componentes desconocidos", desconocidosLayout);
        desconocidosAccordion.close();
        add(desconocidosAccordion);
    }

    private void validarDesconocido(){
        this.mensajesError.setVisible(true);
        try {
            SAXParserFactory.newInstance().newSAXParser().getXMLReader().parse(new InputSource(new StringReader(this.desconocidosText.getValue())));
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            setMensajesError(ex.getMessage());
        }
    }

    public void setMensajesError(String mensajesError){
        this.mensajesError.setText(mensajesError);
        this.mensajesError.setVisible(true);
    }

    public void setMensajesOK(String mensajesError){
        this.mensajesError.setText("El XML esta creado de manera correcta");
        this.mensajesError.setVisible(true);
    }

    public void setDesconocidosText(String text){
        this.desconocidosText.setValue(text);
    }
}
