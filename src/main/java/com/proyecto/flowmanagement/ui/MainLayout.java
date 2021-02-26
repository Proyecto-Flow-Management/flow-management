package com.proyecto.flowmanagement.ui;

import com.proyecto.flowmanagement.backend.persistence.entity.Guide;
import com.proyecto.flowmanagement.backend.service.Impl.GuideGeneratorServiceImp;
import com.proyecto.flowmanagement.backend.service.Impl.GuideServiceImpl;
import com.proyecto.flowmanagement.ui.views.list.ErrorList;
import com.proyecto.flowmanagement.ui.views.list.GuideList;
import com.proyecto.flowmanagement.ui.views.list.GuideListEliminados;
import com.proyecto.flowmanagement.ui.views.list.UserList;
import com.proyecto.flowmanagement.ui.views.pages.GuideCreator;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;


@CssImport("./styles/shared-styles.css")
@CssImport("./styles/antel-styles.css")
public class MainLayout extends AppLayout {

    public MainLayout() throws IOException {
        createHeader();
        createDrawer();
    }

    private void createHeader() throws IOException {
        H1 logo = new H1("Flow Management");
        logo.addClassName("logo");

        logo.addClassName("text-custom");

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }

        H1 loggedUsername = new H1(username);
        loggedUsername.addClassName("text-custom");
        loggedUsername.addClassName("username");

//        Anchor logout = new Anchor("login", "Log out");
        Button logout = new Button("Log out");
        logout.addClassName("clase-icono");
        logout.addClickListener(event -> UI.getCurrent().getPage().setLocation("/logout"));

        // Local comentado, deploy descomentado: mvn clean package -Pproduction -DskipTests
//        unzipJar("./", "application.jar");

        /// Archivos que estan en el directorio donde esta corriendo el programa
//        String listFiles = "";
//
//        File directory = new File("./");
//
//        File[] filesArray = directory.listFiles();
//
//        //sort all files
//        Arrays.sort(filesArray);
//
//        //print the sorted values
//        for (File file : filesArray) {
//            if (file.isFile()) {
//                listFiles += "File : " + file.getName() + "| ";
//            } else if (file.isDirectory()) {
//                listFiles += "Directory : " + file.getName() + "| ";
//            } else {
//                listFiles += "Unknown : " + file.getName() + "| ";
//            }
//        }
//
//        H1 archivos = new H1(listFiles);
//        archivos.addClassName("logo");

//        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo,archivos, loggedUsername, logout);
        DrawerToggle icono = new DrawerToggle();
        icono.addClassName("clase-icono");
        HorizontalLayout header = new HorizontalLayout(icono, logo, loggedUsername, logout);
        header.addClassName("header");
        header.addClassName("header-custom");
        header.setWidth("100%");
        header.expand(logo);
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        addToNavbar(header);
    }

    private void createDrawer() {
        RouterLink listLink = new RouterLink("Usuarios", UserList.class);
        RouterLink guideLink = new RouterLink("Guides", GuideList.class);
        RouterLink guideDelete = new RouterLink("Papelera", GuideListEliminados.class);
        RouterLink errores = new RouterLink("Errores", ErrorList.class);
 
        listLink.addClassName("custom-link");
        guideLink.addClassName("custom-link");
        guideDelete.addClassName("custom-link");
        errores.addClassName("custom-link");

        listLink.setHighlightCondition(HighlightConditions.sameLocation());
        VerticalLayout layout = new VerticalLayout();
        layout.setHeightFull();
        layout.setClassName("header-custom");
        layout.add(listLink,
                guideLink,
                guideDelete,
                errores);
        addToDrawer(layout);
    }

    public static void unzipJar(String destinationDir, String jarPath) throws IOException {
        File file = new File(jarPath);
        JarFile jar = new JarFile(file);

        // fist get all directories,
        // then make those directory on the destination Path
        for (Enumeration<JarEntry> enums = jar.entries(); enums.hasMoreElements();) {
            JarEntry entry = (JarEntry) enums.nextElement();

            String fileName = destinationDir + File.separator + entry.getName();
            File f = new File(fileName);

            if (fileName.endsWith("/") && entry.getName().equals("BOOT-INF/classes/XMLResources/")) {
                f.mkdirs();
            }

        }

        //now create all files
        for (Enumeration<JarEntry> enums = jar.entries(); enums.hasMoreElements();) {
            JarEntry entry = (JarEntry) enums.nextElement();

            String fileName = destinationDir + File.separator + entry.getName();
            if (entry.getName().contains("BOOT-INF/classes/XMLResources")){
                File f = new File(fileName);
                if (!fileName.endsWith("/")) {
                    InputStream is = jar.getInputStream(entry);
                    FileOutputStream fos = new FileOutputStream(f);

                    // write contents of 'is' to 'fos'
                    while (is.available() > 0) {
                        fos.write(is.read());
                    }

                    fos.close();
                    is.close();
                }
            }
        }
    }

}
