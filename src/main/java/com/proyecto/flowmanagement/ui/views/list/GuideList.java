package com.proyecto.flowmanagement.ui.views.list;

import com.proyecto.flowmanagement.backend.persistence.entity.Guide;
import com.proyecto.flowmanagement.backend.service.Impl.GuideGeneratorServiceImp;
import com.proyecto.flowmanagement.backend.service.Impl.GuideServiceImpl;
import com.proyecto.flowmanagement.backend.service.Impl.MockTestServiceImpl;
import com.proyecto.flowmanagement.backend.service.Impl.StepServiceImpl;
import com.proyecto.flowmanagement.ui.MainLayout;
import com.proyecto.flowmanagement.ui.views.forms.GuideForm;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.internal.Pair;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamRegistration;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinSession;
import org.apache.commons.lang3.SerializationUtils;
import org.springframework.stereotype.Component;
import org.vaadin.olli.FileDownloadWrapper;
import org.vaadin.stefan.LazyDownloadButton;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Component
@Route(value = "GuideList", layout = MainLayout.class)
@PageTitle("Crear Guia | Flow Management")
public class GuideList extends VerticalLayout {

    Grid<Guide> grid = new Grid<>(Guide.class);
    GuideServiceImpl guideService;

    public GuideList(GuideServiceImpl guideService) {
        this.guideService = guideService;
        //test();
        addClassName("create-guide-view");
        setSizeFull();
        configureGrid();

        Div content = new Div(grid);
        content.addClassName("content");
        content.setSizeFull();

        add(getToolBar(), content);
        updateList();
        grid.setItems(guideService.getAll());
    }

    private void updateGrid(){
        grid.setItems(guideService.getAll());
    }

    private HorizontalLayout getToolBar() {
        Button addGuideButton = new Button("Crear Guia", event -> UI.getCurrent().navigate("CreateGuide"));
        HorizontalLayout toolbar = new HorizontalLayout(addGuideButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }


    public void test(){
        GuideGeneratorServiceImp test = new GuideGeneratorServiceImp();
        MockTestServiceImpl mock = new MockTestServiceImpl();
        List<Guide> myGuide = guideService.getAll();
        if (!myGuide.isEmpty())
        {
            test.GuidePrint(myGuide.get(0));
        }
    }

    private void updateList() {
        grid.setColumns("name", "label", "mainStep");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.addComponentColumn(guide -> generateEditButton(guide)).setHeader("Editar");
        grid.addComponentColumn(guide -> generateDuplicateButton(guide)).setHeader("Duplicar");
        grid.addComponentColumn(guide -> generateDeleteButton(guide)).setHeader("Eliminar");
        grid.addComponentColumn(guide -> generateExportButton(guide)).setHeader("Exportar");
    }

    private Button generateEditButton(Guide guide){
        Icon icon = new Icon("vaadin", "edit");
        Button botonEditar = new Button("", event -> UI.getCurrent().navigate("CrearGuia/" + guide.getId()));
        botonEditar.setIcon(icon);
        return  botonEditar;
    }

    private Button generateDuplicateButton(Guide guide){
        Icon icon = new Icon("vaadin", "copy");
        Button botonDuplicar = new Button(icon);
        botonDuplicar.addClickListener(e -> {
            duplicateGuide(guide);
        });
        return  botonDuplicar;
    }

    private Button generateDeleteButton(Guide guide){
        Icon icon = new Icon("vaadin", "trash");
        Button botonEliminar = new Button(icon);
        botonEliminar.addClickListener(e -> {
            deleteGuide(guide.getId());
        });
        return  botonEliminar;
    }

    private FileDownloadWrapper generateExportButton(Guide guide) {
        Button button = new Button();
        Icon icon = new Icon("vaadin", "download");
        button.setIcon(icon);
        List<Pair<String, String>> files = getFiles(guide);

        FileDownloadWrapper buttonWrapper = new FileDownloadWrapper(
                new StreamResource(guide.getName() + ".zip", () -> {
                    try {
                        return new ByteArrayInputStream(zipFiles(files));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                return null;}));
//        FileDownloadWrapper buttonWrapper = new FileDownloadWrapper(
//                new StreamResource(guide.getName() + ".txt", () -> new ByteArrayInputStream(guide.getName().getBytes())));
        buttonWrapper.wrapComponent(button);
        return buttonWrapper;
    }

    private List<Pair<String, String>> getFiles(Guide guide){
        List<Pair<String, String>> files = new LinkedList<>();
        files.add(new Pair<>("archivo1.txt","contendido archivo 1, guia: " + guide.getName()));
        files.add(new Pair<>("archivo2.txt","contendido archivo 2, guia: " + guide.getName()));
        files.add(new Pair<>("archivo3.txt","contendido archivo 2, guia: " + guide.getName()));
        return files;
    }

    public byte[] zipFiles(List<Pair<String, String>> files) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ZipOutputStream  zos = new ZipOutputStream(baos)) {

            for (Pair<String, String> file : files) {
                String filename = file.getFirst();
                byte[] content = file.getSecond().getBytes();
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

    private void duplicateGuide(Guide guide){
        Guide newGuide =  SerializationUtils.clone(guide);
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

    private void deleteGuide(Long id)
    {
        guideService.delete(id);
        grid.setItems(guideService.getAll());
    }

    private void editGuide(Long id)
    {
        UI.getCurrent().navigate("EditGuide");
    }


    private void export(Long id)
    {
        guideService.delete(id);
    }

}