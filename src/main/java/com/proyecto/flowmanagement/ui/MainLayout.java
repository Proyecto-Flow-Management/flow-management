package com.proyecto.flowmanagement.ui;

import com.proyecto.flowmanagement.backend.persistence.entity.Guide;
import com.proyecto.flowmanagement.backend.service.Impl.GuideGeneratorServiceImp;
import com.proyecto.flowmanagement.backend.service.Impl.GuideServiceImpl;
import com.proyecto.flowmanagement.ui.views.list.GuideList;
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
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;


@CssImport("./styles/shared-styles.css")
public class MainLayout extends AppLayout {

    public MainLayout(){
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        H1 logo = new H1("Flow Management");
        logo.addClassName("logo");

//        Anchor logout = new Anchor("login", "Log out");
        Button logout = new Button("Log out");
        logout.addClickListener(event -> UI.getCurrent().getPage().setLocation("/logout"));

        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo, logout);
        header.addClassName("header");
        header.setWidth("100%");
        header.expand(logo);
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        addToNavbar(header);
    }

    private void createDrawer() {
        RouterLink listLink = new RouterLink("Usuarios", UserList.class);
        RouterLink guideLink = new RouterLink("Guides", GuideList.class);
        listLink.setHighlightCondition(HighlightConditions.sameLocation());

        addToDrawer(new VerticalLayout(
                listLink,
                guideLink
        ));
    }


}
