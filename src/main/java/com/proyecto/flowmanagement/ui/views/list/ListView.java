package com.proyecto.flowmanagement.ui.views.list;

import com.proyecto.flowmanagement.backend.entity.Company;
import com.proyecto.flowmanagement.backend.entity.Contact;
import com.proyecto.flowmanagement.backend.entity.Rol;
import com.proyecto.flowmanagement.backend.service.CompanyService;
import com.proyecto.flowmanagement.backend.service.ContactService;
import com.proyecto.flowmanagement.backend.service.RolService;
import com.proyecto.flowmanagement.ui.MainLayout;
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
    private ContactService contactService;
    private RolService rolService;

    public ListView(ContactService contactService, CompanyService companyService, RolService rolService) {
        this.contactService = contactService;
        this.rolService = rolService;
        addClassName("list-view");
        setSizeFull();
        configureGrid();


        form = new ContactForm(companyService.findAll(),rolService.findAll());
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
        contactService.delete(evt.getContact());
        updateList();
        closeEditor();
    }

    private void saveContact(ContactForm.SaveEvent evt) {
        contactService.save(evt.getContact());
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
        grid.setItems(contactService.findAll(filterText.getValue()));
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