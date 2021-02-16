package com.proyecto.flowmanagement.ui.views.panels;

import com.proyecto.flowmanagement.backend.service.Impl.GuideGeneratorServiceImp;
import com.proyecto.flowmanagement.ui.views.forms.UploadFileForm;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class ImportExportPanel  extends HorizontalLayout {

    Accordion accordion = new Accordion();
    FormLayout basicInformation = new FormLayout();
    HorizontalLayout basicHorizontal = new HorizontalLayout();
    public UploadFileForm uploadFileForm;
    GuideGeneratorServiceImp guideGeneratorService;

    public ImportExportPanel(GuideGeneratorServiceImp guideGeneratorService)
    {
        this.guideGeneratorService = guideGeneratorService;

        configureElements();
    }

    private void configureElements()
    {
        uploadFileForm = new UploadFileForm(this.guideGeneratorService);
        basicHorizontal.add(uploadFileForm);
        basicHorizontal.setClassName("campos-layout");
        basicInformation.add(basicHorizontal);
        basicInformation.setClassName("basic-information-layout");
        accordion.add("Importar-Exportar", basicInformation);
        accordion.close();
        add(accordion);
    }

}
