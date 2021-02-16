package com.proyecto.flowmanagement.ui.views.forms;

import com.proyecto.flowmanagement.backend.persistence.entity.Guide;
import com.proyecto.flowmanagement.backend.service.Impl.GuideGeneratorServiceImp;
import com.proyecto.flowmanagement.backend.service.Impl.GuideServiceImpl;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.upload.SucceededEvent;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class UploadFileForm extends HorizontalLayout {

    public Button importarGuia = new Button("Importar");
    public HorizontalLayout panelImportar = new HorizontalLayout();
    public Button cancelarImportacion = new Button("Cancelar");
    GuideGeneratorServiceImp guideGeneratorService;
    DocumentBuilderFactory factory = null;
    DocumentBuilder builder = null;
    Document ret = null;
    public Guide actual;
    MemoryBuffer buffer = new MemoryBuffer();
    public Upload upload = new Upload(buffer);

    public UploadFileForm(GuideGeneratorServiceImp guideGeneratorService)
    {
        this.guideGeneratorService = guideGeneratorService;

        configureActions();

        configureElements();

        configureForm();
    }

    private void configureForm()
    {
        add(importarGuia,panelImportar);
    }

    private void configureElements()
    {
        panelImportar.add(upload, cancelarImportacion);
        panelImportar.setVisible(false);
    }

    private void configureActions() {
        importarGuia.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        importarGuia.addClickListener(buttonClickEvent -> mostarImportar());
        cancelarImportacion.addThemeVariants(ButtonVariant.LUMO_ERROR);
        cancelarImportacion.addClickListener(buttonClickEvent -> cancelarImportacion());
        upload.addSucceededListener(succeededEvent -> cargarGuia(succeededEvent));
    }

    private void cargarGuia(SucceededEvent event)
    {
        try {
            factory = DocumentBuilderFactory.newInstance();
            builder = factory.newDocumentBuilder();
            ret = builder.parse(buffer.getInputStream());
            Guide guide = (Guide) guideGeneratorService.importGuide(ret);
            guide.setName(event.getFileName().split("\\.")[0]);
            guide.setLabel(event.getFileName().split("\\.")[0]);
            cancelarImportacion();
            actual = guide;
        } catch (Exception e) {

        }
    }

    private void cancelarImportacion()
    {
        panelImportar.setVisible(false);
        importarGuia.setVisible(true);
    }

    private void mostarImportar()
    {
        panelImportar.setVisible(true);
        importarGuia.setVisible(false);
    }
}
