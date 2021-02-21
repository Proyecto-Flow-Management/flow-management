package com.proyecto.flowmanagement.ui.views.panels;

import com.proyecto.flowmanagement.backend.persistence.entity.Guide;
import com.proyecto.flowmanagement.backend.service.Impl.GuideGeneratorServiceImp;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.SucceededEvent;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayOutputStream;
import java.util.LinkedList;
import java.util.List;

public class ValidacionesOpcionesPanel extends HorizontalLayout {
    public Guide guia;
    public Button importarxsd = new Button("Importar xsd");
    public HorizontalLayout panelImportar = new HorizontalLayout();
    public Button cancelarImportacion = new Button("Cancelar");
    DocumentBuilderFactory factory = null;
    DocumentBuilder builder = null;
    Document ret = null;
    public Guide actual;
    MemoryBuffer buffer = new MemoryBuffer();
    public Upload upload = new Upload(buffer);
    public ComboBox<String> opciones = new ComboBox<>();
    public Button validar = new Button("Validar");
    public Button validarXML = new Button("validarXML");

    HorizontalLayout validatorSecctionLayout = new HorizontalLayout();
    Accordion accordion = new Accordion();
    FormLayout basicInformation = new FormLayout();

    public ValidacionesOpcionesPanel()
    {
        List<String> opciones = new LinkedList<>();
        opciones.add("Validacion XSD");
        opciones.add("Validacion Integridad");
        this.opciones.setItems(opciones);

        configureForm();
    }

    private void configureForm() {
        opciones.addValueChangeListener(value -> cambioOpcion());
        validatorSecctionLayout.add(opciones,validar,upload,validarXML);
        cancelarImportacion.addThemeVariants(ButtonVariant.LUMO_ERROR);
        cancelarImportacion.addClickListener(buttonClickEvent -> cancelarImportacion());
        upload.addSucceededListener(succeededEvent -> cargaarDocument(succeededEvent));
        basicInformation.setWidthFull();
        validatorSecctionLayout.setWidthFull();
        validatorSecctionLayout.setId("step-Layout");
        setWidthFull();
        basicInformation.add(validatorSecctionLayout);
        accordion.close();
        accordion.add("Opciones de Validacion", basicInformation);
        add(accordion);
        setAsDefaault();
    }

    private void cambioOpcion()
    {
        if(opciones.getValue() == "Validacion XSD")
        {
            mostrarValidacionXSD();
        }
        else if(opciones.getValue() == "Validacion Integridad")
            {
                mostrarValidacionIntegridad();
            }
    }

    public void setAsDefaault()
    {
        this.opciones.setVisible(true);
        this.validar.setVisible(false);
        this.upload.setVisible(false);
        this.validarXML.setVisible(false);
    }

    public void mostrarValidacionIntegridad()
    {
        this.opciones.setVisible(true);
        this.validar.setVisible(true);
        this.upload.setVisible(false);
        this.validarXML.setVisible(false);
    }

    public void mostrarValidacionXSD()
    {
        this.opciones.setVisible(true);
        this.validar.setVisible(false);
        this.upload.setVisible(true);
        this.validarXML.setVisible(true);
    }



    private void cancelarImportacion()
    {
    }

    private void cargaarDocument(SucceededEvent event) {
        try {
            guia = new Guide();

            factory = DocumentBuilderFactory.newInstance();
            builder = factory.newDocumentBuilder();
            ret = builder.parse(buffer.getInputStream());
            guia.setNameXsd(event.getFileName().split("\\.")[0]);
            ByteArrayOutputStream bos=new ByteArrayOutputStream();
            StreamResult result=new StreamResult(bos);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(ret);
            transformer.transform(source, result);
            byte []array=bos.toByteArray();
            guia.setFileXSD(array);
            actual = guia;
        } catch (Exception e) {
            actual = null;
        }
    }
}
