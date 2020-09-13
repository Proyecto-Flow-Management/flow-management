package com.proyecto.flowmanagement.ui.views.list;

import com.proyecto.flowmanagement.backend.entity.Contact;
import com.proyecto.flowmanagement.backend.entity.Rol;
import com.proyecto.flowmanagement.ui.MainLayout;
import com.proyecto.flowmanagement.ui.controllers.CompanyController;
import com.proyecto.flowmanagement.ui.controllers.ContactController;
import com.proyecto.flowmanagement.ui.controllers.RolController;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.stereotype.Component;

@Component
@Route(value = "", layout = MainLayout.class)
@PageTitle("Contacts | Flow Management")
public class ListView extends VerticalLayout {

    private final ContactForm form;
    Grid<Contact> grid = new Grid<>(Contact.class);
    TextField filterText = new TextField();
    private ContactController contactController;
    private CompanyController companyController;
    private RolController rolController;

    public ListView(ContactController contactController, CompanyController companyController, RolController rolController) {
        this.contactController = contactController;
        this.companyController = companyController;
        this.rolController = rolController;
        addClassName("list-view");
        setSizeFull();
        configureGrid();


        form = new ContactForm(companyController.listCompany(), rolController.listRol());
        form.addListener(ContactForm.SaveEvent.class, this::saveContact);
        form.addListener(ContactForm.DeleteEvent.class, this::deleteContact);
        form.addListener(ContactForm.CloseEvent.class, e -> closeEditor());

        Div content = new Div(grid, form);
        content.addClassName("content");
        content.setSizeFull();

        add(getToolBar(), content);
        updateList();
        closeEditor();
    }

    private void deleteContact(ContactForm.DeleteEvent evt) {
        contactController.deleteContact(evt.getContact());
        updateList();
        closeEditor();
    }

    private void saveContact(ContactForm.SaveEvent evt) {
        contactController.saveContact(evt.getContact());
        updateList();
        closeEditor();
    }

    private void closeEditor() {
        form.setContact(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private HorizontalLayout getToolBar() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addContactButton = new Button("Add contact", click -> addContact());

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addContactButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void addContact() {
        grid.asSingleSelect().clear();
        editContact(new Contact());
    }

    private void updateList() {
        grid.setItems(contactController.listContact(filterText.getValue()));
    }

    private void  configureGrid() {
        grid.addClassName("contact-grid");
        grid.setSizeFull();
        grid.setColumns("firstName", "lastName");

        grid.addColumn(contact-> {
            Rol rol = contact.getRol();
            return rol == null ? "-" : rol.getName();
        }).setHeader("Rol").setSortable(true);

        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(evt -> editContact(evt.getValue()));
    }

    private void editContact(Contact contact) {
        if(contact == null) {
            closeEditor();
        } else {
            form.setContact(contact);
            form.setVisible(true);
            addClassName("editing");
        }
    }

}
