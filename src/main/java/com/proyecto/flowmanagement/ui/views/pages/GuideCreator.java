package com.proyecto.flowmanagement.ui.views.pages;

import com.proyecto.flowmanagement.backend.persistence.entity.Alternative;
import com.proyecto.flowmanagement.backend.persistence.entity.Guide;
import com.proyecto.flowmanagement.backend.persistence.entity.Operation;
import com.proyecto.flowmanagement.backend.persistence.entity.Step;
import com.proyecto.flowmanagement.backend.service.Impl.*;
import com.proyecto.flowmanagement.backend.persistence.entity.*;
import com.proyecto.flowmanagement.backend.service.Impl.GuideServiceImpl;
import com.proyecto.flowmanagement.ui.MainLayout;
import com.proyecto.flowmanagement.ui.views.grids.OperationGridForm;
import com.proyecto.flowmanagement.ui.views.panels.*;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import io.swagger.v3.oas.models.media.XML;
import org.apache.commons.lang3.SerializationUtils;
import org.hibernate.internal.util.xml.XmlDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.olli.FileDownloadWrapper;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;


import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

//@org.springframework.stereotype.Component
@Route(value = "CrearGuia", layout = MainLayout.class)
@PageTitle("Crear Guia | Flow Management")
public class GuideCreator extends VerticalLayout implements HasUrlParameter<String> {


    @Override
    public void setParameter(BeforeEvent event
            , @OptionalParameter String parameter) {
        if(parameter!=null)
            configureEditing(Long.parseLong(parameter));
        else
            setAsDefault();
    }

    boolean editing;
    boolean flag = true;
    GuideServiceImpl guideService;
    GuideGeneratorServiceImp guideGeneratorServiceImp;

    HorizontalLayout actualPanelLaayout;
    HorizontalLayout detailsPanelLayout;
    HorizontalLayout stepPanelLayout;
    HorizontalLayout validatorPanelLayout;
    HorizontalLayout operationPanelLayout;
    HorizontalLayout guidePanelLayout;
    HorizontalLayout importExportLayout;
    HorizontalLayout actionsLayout;
    HorizontalLayout axsPanelLayout;

    ActualGuidePanel actualGuidePanel;
    OperationGridForm operationGridForm;
    DetailsPanel detailsPanel;
    StepPanel stepPanel;
    ValidatorPanel validatorPanel;
    GuidePanel guidePanel;
    ImportExportPanel importExportPanel;
    ValidacionesOpcionesPanel validacionesOpcionesPanel;

    List<Guide> guideList = new LinkedList<>();
    List<Guide> systemGuideList = new LinkedList<>();

    public Button save = new Button("Guardar");

    Guide raiz;
    Guide editado;

    public GuideCreator(@Autowired GuideServiceImpl guideService,@Autowired GuideGeneratorServiceImp guideGeneratorServiceImp)
    {
        this.guideService = guideService;
        this.guideGeneratorServiceImp = guideGeneratorServiceImp;

        setSizeFull();

        configureActualGuidePanel();

        configureElements();

        configureDetailsPanel();

        configureGuidePanel();

        configureStepPanel();

        configureImportExport();

        configureOperatorPanel();

        configureValidatorPanel();

        configureImportXSD();

        configureActions();

        configureInteractivitie();

        configureForm();
    }

    private void configureImportXSD()
    {
        axsPanelLayout = new HorizontalLayout();
        axsPanelLayout.setWidthFull();
        validacionesOpcionesPanel = new ValidacionesOpcionesPanel();
        axsPanelLayout.add(validacionesOpcionesPanel);
    }

    private void configureEditing(long id) {
        raiz = guideService.getById(id);
        raiz.editing = true;
        flag = false;
        cargarValorGrilla(raiz);
        actualGuidePanel.actualizarItems(raiz);
        actualGuidePanel.setVisible(raiz.getGuides() != null && raiz.getGuides().size() > 0);
        editing = true;
        flag = true;
    }

    private void setAsDefault() {
        raiz = new Guide();
        raiz.editing = true;
        cargarValorGrilla(raiz);
        editing = false;
    }

    private void configureValidatorPanel() {
        validatorPanelLayout = new HorizontalLayout();
        validatorPanelLayout.setWidthFull();
        validatorPanel = new ValidatorPanel();
        validatorPanelLayout.add(validatorPanel);
    }

    private void configureActions() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        validacionesOpcionesPanel.validar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClickListener(buttonClickEvent -> guardarGuias());
        validacionesOpcionesPanel.validar.addClickListener(buttonClickEvent -> validarGuia());
        guidePanel.eliminarGuia.addClickListener(buttonClickEvent -> deleteGuide());
        actionsLayout = new HorizontalLayout();
        actionsLayout.add(save);
    }

    private void deleteGuide()
    {
        raiz.eliminarGuia();

        if(raiz != null)
        {
            raiz.editing = true;

                editado = raiz;
            guidePanel.eliminarGuia.setVisible(false);

            cargarValorGrilla(raiz);
        }

    }


    private void guardarGuias()
    {
        if (!guidePanel.name.getValue().trim().isEmpty()) {
            actualizarGuiaActual();
            raiz.setGuiaPropia(true);

            if (!editing)
                guideService.add(raiz);
            else
                guideService.update(raiz);

            UI.getCurrent().navigate("GuideList/" +1);
        }
        else{
            mostrarMensajeError("La guía debe tener un nombre al menos!");
        }
    }

    private void configureActualGuidePanel()
    {
        actualPanelLaayout = new HorizontalLayout();
        actualPanelLaayout.setWidthFull();
        actualGuidePanel = new ActualGuidePanel();
        actualPanelLaayout.add(actualGuidePanel);
        actualPanelLaayout.setVisible(false);
    }

    private void configureElements() {
        editado = new Guide();
        raiz = new Guide();
        raiz.editing = true;


    }

    private void actualizarListaGuidesExistentes()
    {
        this.guideList = raiz.getGuides();
        this.systemGuideList = guideService.getAll().stream().filter(aux -> aux.isGuiaPropia()).collect(Collectors.toList());
        this.stepPanel.stepGridForm.stepForm.alternativeGridForm.alternativeForm.guideList = this.guideList;
        this.stepPanel.stepGridForm.stepForm.alternativeGridForm.alternativeForm.systemGuideList = this.systemGuideList;
    }

    private void configureGuidePanel() {
        guidePanelLayout = new HorizontalLayout();
        guidePanelLayout.setWidthFull();
        guidePanel = new GuidePanel();
        guidePanelLayout.add(guidePanel);
    }

    private void configureImportExport() {
        importExportLayout = new HorizontalLayout();
        importExportLayout.setWidthFull();
        importExportPanel = new ImportExportPanel(guideGeneratorServiceImp, guideService);
        importExportLayout.add(importExportPanel);
    }

    private void configureDetailsPanel() {
        detailsPanelLayout = new HorizontalLayout();
        detailsPanelLayout.setWidthFull();
        detailsPanel = new DetailsPanel();
        detailsPanelLayout.add(detailsPanel);
    }

    private void configureStepPanel() {
        stepPanelLayout = new HorizontalLayout();
        stepPanelLayout.setWidthFull();
        stepPanel = new StepPanel();
        stepPanelLayout.add(stepPanel);
        stepPanel.stepGridForm.stepForm.alternativeGridForm.createAlternative.addClickListener( buttonClickEvent ->  actualizarListaGuidesExistentes());
    }

    private void configureOperatorPanel() {
        operationGridForm = new OperationGridForm();
        operationGridForm.setWidthFull();

        operationPanelLayout = new HorizontalLayout();
        operationPanelLayout.setWidthFull();

        operationPanelLayout.add(operationGridForm);
    }

    private void configureInteractivitie() {
        actualizacionDeDetalles();
        actualizacionAlternativs();
        actualizacionCrecionStep();
        actualizacionEliminarStep();
        actualizacionConditionsAlternatives();
        cambioGuia();
        actualizacionOperations();
        cambioLabelYempezarEn();
        importacionGuia();
        configurarValidacionXSD();
        //Actualizar Steps
        // Alternativs
        //Actuaalizar resumen
    }

    private void configurarValidacionXSD()
    {
        validacionesOpcionesPanel.validarXML.addClickListener(buttonClickEvent -> generarValidacion());
    }

    private void generarValidacion()
    {
        List<String> errorsList = new LinkedList<>();

        errorsList = iterar(raiz);

        if(errorsList.size() > 0)
        {
            this.validatorPanel.mostrarErroresXSD(errorsList);
            this.validatorPanel.accordion.setVisible(true);
            mostrarMensajeError("Existen errores");
        }
        else
        {
            mostrarMensajeError("La guia se valida correctamente contra el XSD");
        }
    }

    public List<String> iterar(Guide guia)
    {
        List<String> errores = new LinkedList<>();

        errores.addAll(validarXDSMessage(guideGeneratorServiceImp.GuidePrint(guia), guia.getName()));

        for(Guide aux : guia.getGuides()){
            errores.addAll(iterar(aux));
        }

        return errores;
    }

    public List<String> validarXDSMessage( byte[] byteArray, String nombreGuia)
    {
        List<String> retorno = new LinkedList<>();

        byte[]  validacionXSD = validacionesOpcionesPanel.actual.getFileXSD();

        try {
            File xsd = new File("xsd.xsd");
            File xml = new File("xsd.xml");

            FileOutputStream fos = null;

            fos = new FileOutputStream(xsd);
            fos.write(validacionXSD);

            fos = new FileOutputStream(xml);
            fos.write(byteArray);

            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(xsd);
            Validator validator = schema.newValidator();
            final List<SAXParseException> exceptions = new LinkedList<SAXParseException>();
            validator.setErrorHandler(new ErrorHandler()
            {
                @Override
                public void warning(SAXParseException exception) throws SAXException
                {
                    exceptions.add(exception);
                }

                @Override
                public void fatalError(SAXParseException exception) throws SAXException
                {
                    exceptions.add(exception);
                }

                @Override
                public void error(SAXParseException exception) throws SAXException
                {
                    exceptions.add(exception);
                }
            });
            validator.validate(new StreamSource(xml));
            if(exceptions.size()>0)
            {
                retorno = exceptions.stream().map(e -> e.getMessage()).collect(Collectors.toList());

                retorno.replaceAll(e -> "Guia " + nombreGuia + ":      " + e);
            }
            return retorno;

        } catch (Exception e) {
            return retorno;
        }
    }

    private void importacionGuia()
    {
        importExportPanel.uploadFileForm.upload.addSucceededListener(succeededEvent -> addImportedGuide());
        importExportPanel.importarExistente.addClickListener(buttonClickEvent -> importarExistente());
    }

    private void importarExistente() {
        if(importExportPanel.guides !=null)
        {
            Guide importedGuide = importExportPanel.guides.getValue();
            if(this.guideList == null)
                guideList = new LinkedList<>();

            guideList.add(SerializationUtils.clone(importedGuide));

            if(importedGuide != null)
            {
                editado.addGuide(importedGuide);
                actualizarGuiaActual();
            }

            importExportPanel.setAsDefault();

            this.stepPanel.stepGridForm.stepForm.alternativeGridForm.alternativeForm.guideList = this.guideList;
        }
        else{
            mostrarMensajeError("Debe elegir una guia para importar");
        }
    }

    private void addImportedGuide()
    {
        Guide importedGuide = importExportPanel.uploadFileForm.actual;
        importedGuide.setGuiaPropia(false);
        if(this.guideList == null)
            guideList = new LinkedList<>();
        guideList.add(importedGuide);

        if(importedGuide != null)
        {
            editado.addGuide(importedGuide);
            actualizarGuiaActual();
        }

        importExportPanel.setAsDefault();

        this.stepPanel.stepGridForm.stepForm.alternativeGridForm.alternativeForm.guideList = this.guideList;
    }

    private void cambioLabelYempezarEn()
    {
        guidePanel.label.addValueChangeListener(event -> actualizarLabel());
        guidePanel.mainStep.addValueChangeListener(event -> actualizarStep());
        guidePanel.tagsDesconocidosForm.desconocidosText.addValueChangeListener(event -> actualizarTagsDesconocidos());
    }

    private void actualizarTagsDesconocidos() {
        editado.setTagsDesconocidos(guidePanel.tagsDesconocidosForm.desconocidosText.getValue());
    }

    private void actualizarStep()
    {
        editado.setMainStep(guidePanel.mainStep.getValue());
    }

    private void actualizarLabel()
    {
        editado.setLabel(guidePanel.label.getValue());
    }

    private void actualizacionOperations()
    {
        operationGridForm.operationForm.save.addClickListener(buttonClickEvent -> actualizarOperationsIdsGuia());
        stepPanel.stepGridForm.stepForm.operationGridForm.operationForm.save.addClickListener(buttonClickEvent -> actualizarOperaationsSteps());
        stepPanel.stepGridForm.stepForm.alternativeGridForm.alternativeForm.save.addClickListener(buttonClickEvent -> actualizarAlternativesIds());
        stepPanel.stepGridForm.stepForm.operationGridForm.createOperation.addClickListener(buttonClickEvent -> actualizarListAlternatives());
    }

    private void actualizarAlternativesIds()
    {
        stepPanel.stepGridForm.stepForm.operationGridForm.createOperation.addClickListener( buttonClickEvent ->  actualizarListAlternatives());
    }

    private void actualizarListAlternatives()
    {
       List<String> alternativesIds = stepPanel.stepGridForm.stepForm.getStep().getAlternatives().stream().filter(a -> a.getNextStep() != null && a.getNextStep() != "").map(al -> al.getNextStep()).collect(Collectors.toList());
       stepPanel.stepGridForm.stepForm.operationGridForm.operationForm.alternativeIdsForm.updateElements(alternativesIds);
    }

    private void actualizarOperaationsSteps()
    {
        if(stepPanel.stepGridForm.stepForm.operationGridForm.operationForm.isValid)
        {
            List<String> operationsList = stepPanel.stepGridForm.stepForm.operationGridForm.getOperations().stream().map(o -> o.getName()).collect(Collectors.toList());
            stepPanel.stepGridForm.stepForm.operationGridForm.updateOperationsIds(operationsList);
        }
    }

    private void actualizarOperationsIdsGuia()
    {
        actualizarGuiaActual();
        List<String> operationsList = operationGridForm.getOperations().stream().map(o -> o.getName()).collect(Collectors.toList());
        operationGridForm.operations = operationsList;
    }

    private void actualizacionConditionsAlternatives() {
        stepPanel.stepGridForm.stepForm.alternativeGridForm.alternativeForm.conditionForm.unaryConditionForm.save.addClickListener(buttonClickEvent -> actualizarGuiaActual());
        stepPanel.stepGridForm.stepForm.alternativeGridForm.alternativeForm.conditionForm.binaryConditionForm.save.addClickListener(buttonClickEvent -> actualizarGuiaActual());
        stepPanel.stepGridForm.stepForm.alternativeGridForm.alternativeForm.conditionForm.eliminarBinary.addClickListener(buttonClickEvent -> actualizarGuiaActual());
        stepPanel.stepGridForm.stepForm.alternativeGridForm.alternativeForm.conditionForm.eliminarUnary.addClickListener(buttonClickEvent -> actualizarGuiaActual());
    }

    private void actualizacionEliminarStep() {
        stepPanel.stepGridForm.stepForm.delete.addClickListener(buttonClickEvent -> cargarMainStepsPorEliminacion());
    }

    private void actualizacionCrecionStep()
    {
        stepPanel.stepGridForm.stepForm.save.addClickListener(buttonClickEvent -> cargarMainSteps());
    }

    private void cargarMainSteps() {

        if(stepPanel.stepGridForm.stepForm.esValido)
        {
            guidePanel.actualizarSteps(stepPanel.stepGridForm.stepList);
        }
    }

    private void cargarMainStepsPorEliminacion() {
        guidePanel.actualizarSteps(stepPanel.stepGridForm.stepList);
    }

    private void cambioGuia()
    {
        actualGuidePanel.actualGuide.addValueChangeListener(event -> cargarGuia());
    }

    private void cargarGuia() {

        Guide valor = actualGuidePanel.actualGuide.getValue();

        if(valor != null)
        {
            raiz.quitarEdicion();

            if(valor == raiz)
            {
                raiz.editing = true;
                editado = raiz;
                guidePanel.eliminarGuia.setVisible(false);
            }
            else
            {
                editado = valor;
                raiz.setearParaEditar(editado);
                guidePanel.eliminarGuia.setVisible(true);
            }

            cargarValorGrilla(valor);
        }
    }

    private void cargarValorGrilla(Guide guide)
    {
        this.stepPanel.stepGridForm.stepList = guide.getSteps();
        this.stepPanel.stepGridForm.updateGrid();

        this.operationGridForm.operationList = guide.getOperations();
        this.operationGridForm.updateGrid();

        this.guidePanel.actualizarAtributos(guide);

        if(guide.getName() != null)
             this.guidePanel.name.setValue(guide.getName());
        else
            this.guidePanel.name.setValue("");
    }

    private void actualizacionAlternativs()
    {
        stepPanel.stepGridForm.stepForm.alternativeGridForm.alternativeForm.save.addClickListener(buttonClickEvent -> nuevoStep());
    }

    private Guide getGuideByName(String name){
        List<Guide> listGuide = guideService.getAll();

        for (Guide guide:
             listGuide) {
            if (name.equals(guide.getName())){
                return guide;
            }
        }
        return null;
    }

    private Guide duplicateGuide(Guide guide){
        Guide newGuide =  SerializationUtils.clone(guide);
        newGuide.setName(guide.getName());
        return newGuide;
    }

    private void nuevoStep() {

        if(stepPanel.stepGridForm.stepForm.alternativeGridForm.alternativeForm.isValid)
        {
            Alternative myAlternative = stepPanel.stepGridForm.stepForm.alternativeGridForm.alternativeForm.getAlternative();

            if(myAlternative.getNewStep())
            {
                List<Step> stepsActuales = stepPanel.stepGridForm.getSteps();

                boolean noExiste = stepsActuales.stream().filter(aux -> aux.getTextId().equals(myAlternative.getNextStep())).count() == 0;

                if(noExiste)
                {
                    Step nuevo = new Step();
                    nuevo.setText(myAlternative.getNextStep());
                    nuevo.setLabel(myAlternative.getNextStep());
                    nuevo.setTextId(myAlternative.getNextStep());
                    this.stepPanel.stepGridForm.stepList.add(nuevo);
                    this.stepPanel.stepGridForm.updateGrid();
                }
            }
            else if(myAlternative.isSystemGuide() && (myAlternative.getGuideName() != null ||  !myAlternative.getGuideName().isEmpty())){
                Guide guide = getGuideByName(myAlternative.getGuideName());
                if (guide != null){
                    Guide newGuide = duplicateGuide(guide);

                    if(editado.getGuides()== null)
                        editado.setGuides(new LinkedList<>());

                    editado.addGuide(newGuide);
                }
            }
            else if(myAlternative.isNewGuide() && (myAlternative.getGuideName() != null ||  !myAlternative.getGuideName().isEmpty()))
            {
                Guide newGuide = new Guide();
                newGuide.setName(myAlternative.getGuideName());

                if(editado.getGuides()== null)
                    editado.setGuides(new LinkedList<>());

                editado.addGuide(newGuide);
            }
            actualizarGuiaActual();
        }
    }

    private void actualizarListaDeGuias(Guide actual)
    {
        if(actual.getGuides().size() > 1)
        {
            actualGuidePanel.setVisible(true);
            actualGuidePanel.setItems(actual.getGuides());
            actualGuidePanel.actualGuide.setValue(actual);
        }
        else
        {
            actualGuidePanel.setVisible(false);
        }
    }

    private void configureForm() {
        add(actualPanelLaayout,
                detailsPanelLayout,
                guidePanelLayout,
                stepPanelLayout,
                operationPanelLayout,
                importExportLayout,
                axsPanelLayout,
                validatorPanelLayout,
                actionsLayout);
    }

    private void actualizacionDeDetalles() {
        stepPanel.stepGridForm.stepForm.save.addClickListener( buttonClickEvent ->  actualizarGuiaActual());
        stepPanel.stepGridForm.stepForm.delete.addClickListener( buttonClickEvent ->  actualizarGuiaActual());
        guidePanel.name.addValueChangeListener(addListener ->  actualizarGuiaActual());
    }

    private void actualizarGuiaActual()
    {
        editado.setName(guidePanel.name.getValue());
        editado.setLabel(guidePanel.label.getValue());

        if(guidePanel.mainStep.getValue() != null)
            editado.setMainStep(guidePanel.mainStep.getValue());
        else
            editado.setMainStep("");

        //Elementos de Steps
        List<Step> stepList = stepPanel.stepGridForm.getSteps();
        editado.setSteps(stepList);

        //ElementosOperations
        List<Operation> operationsList = operationGridForm.getOperations();
        editado.setOperations(operationsList);

        if(raiz.editing)
        {
            if(flag)
            {
                Long id = raiz.getId();
                List<Guide> guideList = editado.getGuides();
                raiz = editado;
                raiz.editing = true;
                raiz.setGuides(guideList);
                raiz.setId(id);
            }
            else
                {
                    Long id = raiz.getId();
                    List<Guide> guideList = raiz.getGuides();
                    raiz = editado;
                    raiz.editing = true;
                    raiz.setGuides(guideList);
                    raiz.setId(id);
                }
        }
        else
         raiz.setGuide(editado);

        detailsPanel.updateGridDetails(raiz);

        if(raiz.getGuides() != null && raiz.getGuides().size() > 0)
            mostrarOpcionesDeGuias();
        else
        {
            actualPanelLaayout.setVisible(false);
            actualGuidePanel.setVisible(false);
        }
    }

    private void mostrarOpcionesDeGuias()
    {
        actualPanelLaayout.setVisible(true);
        actualGuidePanel.setVisible(true);
        actualGuidePanel.actualizarItems(raiz);
    }

    private void validarGuia()
    {
        actualizarGuiaActual();

        this.validatorPanel.actualizarGrilla(raiz);
    }

    private void mostrarMensajeError(String mensajeValidacion) {
        Span mensaje = new Span(mensajeValidacion);
        Notification notification = new Notification(mensaje);
        notification.setDuration(3000);
        notification.setPosition(Notification.Position.MIDDLE);
        notification.open();
    }
}





