package com.proyecto.flowmanagement.ui.views.panels;

import com.proyecto.flowmanagement.backend.persistence.entity.Guide;
import com.proyecto.flowmanagement.backend.service.Impl.GuideGeneratorServiceImp;
import com.proyecto.flowmanagement.backend.service.Impl.GuideServiceImpl;
import com.proyecto.flowmanagement.ui.views.forms.UploadFileForm;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class ImportExportPanel  extends HorizontalLayout {

    Accordion accordion = new Accordion();
    FormLayout basicInformation = new FormLayout();
    HorizontalLayout basicHorizontal = new HorizontalLayout();
    ComboBox<String> options = new ComboBox<>("Referencia");
    public ComboBox<Guide> guides = new ComboBox<>("Referencia");
    public UploadFileForm uploadFileForm;
    Button cancel = new Button("Cancel");
    public Button importarExistente = new Button("Importar");
    public boolean isRight;
    GuideGeneratorServiceImp guideGeneratorService;
    GuideServiceImpl guideService;

    public ImportExportPanel(GuideGeneratorServiceImp guideGeneratorService, GuideServiceImpl guideService)
    {
        this.guideService = guideService;

        this.guideGeneratorService = guideGeneratorService;

        configureElements();
    }

    private void configureElements()
    {
        List<String> opciones = new LinkedList<>();
        opciones.add("Nueva Guia");
        opciones.add("Guia existente");
        guides.setItems(guideService.getAll().stream().filter(o -> o.isGuiaPropia()).collect(Collectors.toList()));
        options.setItems(opciones);
        cancel.addClickListener(buttonClickEvent -> setAsDefault());
        options.addValueChangeListener(comboBoxStringComponentValueChangeEvent -> slectItem());

        uploadFileForm = new UploadFileForm(this.guideGeneratorService);
        basicHorizontal.add(options,uploadFileForm,guides,importarExistente,cancel);
        uploadFileForm.cancelarImportacion.addClickListener(buttonClickEvent -> setAsDefault());
        basicHorizontal.setWidthFull();
        setWidthFull();
        basicHorizontal.setClassName("campos-layout");
        basicInformation.add(basicHorizontal);
        basicInformation.setClassName("basic-information-layout");
        basicInformation.setWidthFull();
        accordion.add("Importar-Exportar", basicInformation);
        accordion.close();
        add(accordion);

        setAsDefault();
    }

    private void slectItem()
    {
        String value = options.getValue();

        if(value == "Nueva Guia")
        {
            this.options.setVisible(false);
            this.cancel.setVisible(false);
            this.uploadFileForm.setVisible(true);
            this.uploadFileForm.importarGuia.setVisible(false);
            this.uploadFileForm.panelImportar.setVisible(true);
            this.uploadFileForm.cancelarImportacion.setVisible(true);
            this.uploadFileForm.upload.setVisible(true);
            this.guides.setVisible(false);
            importarExistente.setVisible(false);
            cancel.setVisible(false);
        }
        else if(!value.isEmpty())
        {
            this.uploadFileForm.importarGuia.setVisible(false);
            this.uploadFileForm.panelImportar.setVisible(false);
            this.cancel.setVisible(true);
            this.uploadFileForm.cancelarImportacion.setVisible(false);
            importarExistente.setVisible(true);
            this.options.setVisible(false);
            this.uploadFileForm.setVisible(false);
            this.guides.setVisible(true);
            cancel.setVisible(true);
        }
    }

    public void setAsDefault()
    {
        options.setValue("");
        importarExistente.setVisible(false);
        cancel.setVisible(false);
        this.options.setVisible(true);
        this.uploadFileForm.setVisible(false);
        this.guides.setVisible(false);
        this.accordion.close();
    }

}
