package com.proyecto.flowmanagement.ui.views.list;

import com.proyecto.flowmanagement.backend.persistence.entity.Guide;
import com.proyecto.flowmanagement.backend.service.Impl.GuideGeneratorServiceImp;
import com.proyecto.flowmanagement.backend.service.Impl.GuideServiceImpl;
import com.proyecto.flowmanagement.ui.MainLayout;
import com.proyecto.flowmanagement.ui.views.forms.UploadFileForm;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.internal.Pair;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.StreamResource;
import org.apache.commons.lang3.SerializationUtils;
import org.vaadin.olli.FileDownloadWrapper;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Route(value = "GuideListDeleted", layout = MainLayout.class)
@PageTitle("Guias Eliminadas | Flow Management")
public class GuideListEliminados  extends VerticalLayout implements HasUrlParameter<String> {

    @Override
    public void setParameter(BeforeEvent event
            , @OptionalParameter String parameter) {
        if (parameter != null)
            updateGrid();
    }

    Grid<Guide> grid = new Grid<>(Guide.class);
    GuideServiceImpl guideService;
    GuideGeneratorServiceImp guideGeneratorService;
    Button addGuideButton;
    UploadFileForm uploadFileForm;
    TextField filterText = new TextField();

    public GuideListEliminados(GuideServiceImpl guideService, GuideGeneratorServiceImp guideGeneratorServiceImp) throws IOException, SAXException, ParserConfigurationException {
        this.guideService = guideService;
        this.guideGeneratorService = guideGeneratorServiceImp;
        //test2();
        addClassName("create-guide-view");
        setSizeFull();
        configureGrid();

        filterText.setPlaceholder("Filtrar por nombre...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateListWithValues());

        Div content = new Div(grid);
        content.addClassName("content");
        content.setSizeFull();

        add(getToolBar(), content);
        updateList();
        updateGrid();
    }

    private void updateListWithValues() {
        grid.setItems(guideService.findAll(filterText.getValue()));
    }

    public void updateGrid() {
        grid.setItems(guideService.getAll().stream().filter(aux -> aux.isGuiaPropia() && aux.isEliminada()));
    }

    private HorizontalLayout getToolBar() {
        addGuideButton = new Button("Crear Guia", event -> UI.getCurrent().navigate("CrearGuia"));
        uploadFileForm = new UploadFileForm(guideGeneratorService);
        HorizontalLayout toolbar = new HorizontalLayout(filterText);
        uploadFileForm.importarGuia.addClickListener(buttonClickEvent -> soloImportacion());
        uploadFileForm.cancelarImportacion.addClickListener(buttonClickEvent -> noSoloImportacion());
        uploadFileForm.upload.addSucceededListener(buttonClickEvent -> importar());
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void noSoloImportacion() {
        this.addGuideButton.setVisible(true);
        this.filterText.setVisible(true);
    }

    private void importar() {
        String mensaje = "";

        try {
            this.addGuideButton.setVisible(true);
            this.filterText.setVisible(true);

            if (uploadFileForm.actual != null) {
                guideService.add(uploadFileForm.actual);
                updateGrid();
                mensaje = "Guia importada correctamente";
            } else {
                mensaje = "La guia no pudo ser importada por errores en su sintaxis";
            }

        } catch (Exception ex) {
            mensaje = "La guia no pudo ser importada por errores al transferirse a la base de datos";
        }

        mostrarMensajeError(mensaje);
    }

    private void mostrarMensajeError(String validacionIncompleta) {
        Span mensaje = new Span(validacionIncompleta);
        Notification notification = new Notification(mensaje);
        notification.setDuration(3000);
        notification.setPosition(Notification.Position.MIDDLE);
        notification.open();
    }

    private void soloImportacion() {
        this.addGuideButton.setVisible(false);
        this.filterText.setVisible(false);
    }

    private void updateList() {
        grid.setColumns("name", "label", "mainStep");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.addComponentColumn(guide -> generateEditButton(guide)).setHeader("Editar");
        grid.addComponentColumn(guide -> restaurar(guide)).setHeader("Restaurar");
        grid.addComponentColumn(guide -> generateExportButton(guide)).setHeader("Exportar");
    }

    private Button generateEditButton(Guide guide) {
        Icon icon = new Icon("vaadin", "edit");
        Button botonEditar = new Button("", event -> UI.getCurrent().navigate("CrearGuia/" + guide.getId()));
        botonEditar.setIcon(icon);
        return botonEditar;
    }

    private Button generateDuplicateButton(Guide guide) {
        Icon icon = new Icon("vaadin", "copy");
        Button botonDuplicar = new Button(icon);
        botonDuplicar.addClickListener(e -> {
            duplicateGuide(guide);
        });
        return botonDuplicar;
    }

    private Button restaurar(Guide guide) {
        Icon icon = new Icon("vaadin", "plus");
        Button botonEliminar = new Button(icon);
        botonEliminar.addClickListener(e -> {
            restaurar(guide.getId());
        });
        return botonEliminar;
    }

    private FileDownloadWrapper generateExportButton(Guide guide) {
        Button button = new Button();
        Icon icon = new Icon("vaadin", "download");
        button.setIcon(icon);
        List<Pair<String, byte[]>> files = getFiles(guide);

        FileDownloadWrapper buttonWrapper = new FileDownloadWrapper(
                new StreamResource(guide.getName() + ".zip", () -> {
                    try {
                        return new ByteArrayInputStream(zipFiles(files));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }));
        buttonWrapper.wrapComponent(button);
        return buttonWrapper;
    }

    private List<Pair<String, byte[]>> getFiles(Guide guide) {
        List<Pair<String, byte[]>> files = guideGeneratorService.guidePrints(guide);
        return files;
    }

    public byte[] zipFiles(List<Pair<String, byte[]>> files) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ZipOutputStream zos = new ZipOutputStream(baos)) {

            for (Pair<String, byte[]> file : files) {
                String filename = file.getFirst();
                byte[] content = file.getSecond();
                ZipEntry entry = new ZipEntry(filename);
                entry.setSize(content.length);
                zos.putNextEntry(entry);
                zos.write(content);
                zos.closeEntry();
            }
            zos.finish();
            zos.flush();
            zos.close();
            return baos.toByteArray();
        }
    }

    public void duplicateGuide(Guide guide) {
        Guide newGuide = SerializationUtils.clone(guide);
        newGuide.setName("Copia-" + guide.getName());
        guideService.add(newGuide);
        updateGrid();
        updateList();
    }

    private void configureGrid() {
        grid.addClassName("guide-grid");
        grid.setSizeFull();
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
    }

    private void restaurar(Long id) {
        Guide aux = guideService.getById(id);
        aux.setEliminada(false);
        guideService.update(aux);
        grid.setItems(guideService.getAll().stream().filter(g -> g.isGuiaPropia() && g.isEliminada()));
    }

    private void editGuide(Long id) {
        UI.getCurrent().navigate("EditGuide");
    }
}