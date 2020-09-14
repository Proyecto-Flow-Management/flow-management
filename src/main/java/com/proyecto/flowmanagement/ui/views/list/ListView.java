package com.proyecto.flowmanagement.ui.views.list;

import com.proyecto.flowmanagement.backend.entity.User;
import com.proyecto.flowmanagement.backend.entity.Rol;
import com.proyecto.flowmanagement.ui.MainLayout;
import com.proyecto.flowmanagement.ui.controllers.UserController;
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
@PageTitle("Users | Flow Management")
public class ListView extends VerticalLayout {

    private final UserForm form;
    Grid<User> grid = new Grid<>(User.class);
    TextField filterText = new TextField();
    private UserController userController;
    private RolController rolController;

    public ListView(UserController userController, RolController rolController) {
        this.userController = userController;
        this.rolController = rolController;
        addClassName("list-view");
        setSizeFull();
        configureGrid();


        form = new UserForm(rolController.listRol());
        form.addListener(UserForm.SaveEvent.class, this::saveContact);
        form.addListener(UserForm.DeleteEvent.class, this::deleteContact);
        form.addListener(UserForm.CloseEvent.class, e -> closeEditor());

        Div content = new Div(grid, form);
        content.addClassName("content");
        content.setSizeFull();

        add(getToolBar(), content);
        updateList();
        closeEditor();
    }

    private void deleteContact(UserForm.DeleteEvent evt) {
        userController.deleteUser(evt.getUser());
        updateList();
        closeEditor();
    }

    private void saveContact(UserForm.SaveEvent evt) {
        userController.saveUser(evt.getUser());
        updateList();
        closeEditor();
    }

    private void closeEditor() {
        form.setUser(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private HorizontalLayout getToolBar() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addUserButton = new Button("Add user", click -> addUser());

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addUserButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void addUser() {
        grid.asSingleSelect().clear();
        editUser(new User());
    }

    private void updateList() {
        grid.setItems(userController.listUser(filterText.getValue()));
    }

    private void  configureGrid() {
        grid.addClassName("user-grid");
        grid.setSizeFull();
        grid.setColumns("firstName", "lastName");

        grid.addColumn(user-> {
            Rol rol = user.getRol();
            return rol == null ? "-" : rol.getName();
        }).setHeader("Rol").setSortable(true);

        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(evt -> editUser(evt.getValue()));
    }

    private void editUser(User user) {
        if(user == null) {
            closeEditor();
        } else {
            form.setUser(user);
            form.setVisible(true);
            addClassName("editing");
        }
    }

}
