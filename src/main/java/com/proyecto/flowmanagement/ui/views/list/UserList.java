package com.proyecto.flowmanagement.ui.views.list;

import com.proyecto.flowmanagement.backend.persistence.entity.SecurePasswordStorage;
import com.proyecto.flowmanagement.backend.persistence.entity.User;
import com.proyecto.flowmanagement.backend.service.Impl.RolServiceImpl;
import com.proyecto.flowmanagement.backend.service.Impl.UserServiceImpl;
import com.proyecto.flowmanagement.ui.MainLayout;
import com.proyecto.flowmanagement.ui.views.forms.UserForm;
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
public class UserList extends VerticalLayout {

    private final UserForm form;
    private SecurePasswordStorage securePasswordStorage;
    private String oldHash = "";

    Grid<User> grid = new Grid<>(User.class);
    TextField filterText = new TextField();

    UserServiceImpl userService;
    RolServiceImpl rolService;

    public UserList(UserServiceImpl userService, RolServiceImpl rolService) {
        this.securePasswordStorage = new SecurePasswordStorage();
        this.userService = userService;
        this.rolService = rolService;
        addClassName("list-view");
        setSizeFull();
        configureGrid();


        form = new UserForm();
        form.addListener(UserForm.SaveEvent.class, this::saveUser);
        form.addListener(UserForm.DeleteEvent.class, this::deleteUser);
        form.addListener(UserForm.CloseEvent.class, e -> closeEditor());

        Div content = new Div(grid, form);
        content.addClassName("content");
        content.setSizeFull();

        add(getToolBar(), content);
        updateList();
        closeEditor();
    }

    private void deleteUser(UserForm.DeleteEvent evt) {
        userService.delete(evt.getUser());
        updateList();
        closeEditor();
    }

    private void saveUser(UserForm.SaveEvent evt) {
        User user = evt.getUser();
        // Cambio la password
        if (!user.getPassword().equals(this.oldHash)) {
            try {
                String salt = securePasswordStorage.getNewSalt();
                user.setSalt(salt);
                String encryptedPassword = securePasswordStorage.getEncryptedPassword(user.getPassword(), salt);
                user.setPassword(encryptedPassword);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        userService.save(evt.getUser());
        updateList();
        closeEditor();
    }

    private void closeEditor() {
        form.setUser(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private HorizontalLayout getToolBar() {
        filterText.setPlaceholder("Filtrar por nombre...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addUserButton = new Button("Crear Usuario", click -> addUser());

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addUserButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void addUser() {
        grid.asSingleSelect().clear();
        editUser(new User());
    }

    private void updateList() {
        grid.setItems(userService.findAll(filterText.getValue()));
    }

    private void  configureGrid() {
        grid.addClassName("user-grid");
        grid.setSizeFull();
        grid.setColumns("firstName", "lastName");

        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(evt -> editUser(evt.getValue()));
    }

    private void editUser(User user) {
        if(user == null) {
            closeEditor();
        } else {
            if (user.getPassword() != null){
                this.oldHash = user.getPassword();
            }
            form.setUser(user);
            form.setVisible(true);
            addClassName("editing");
        }
    }

}
