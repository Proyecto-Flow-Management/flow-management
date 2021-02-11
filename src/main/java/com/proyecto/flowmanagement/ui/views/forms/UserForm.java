package com.proyecto.flowmanagement.ui.views.forms;

import com.proyecto.flowmanagement.backend.persistence.entity.SecurePasswordStorage;
import com.proyecto.flowmanagement.backend.persistence.entity.User;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

import java.util.LinkedList;
import java.util.List;

@CssImport("./styles/user-form.css")
public class UserForm extends FormLayout {
    private User user;
    private String oldHash = "";

    public boolean isValid;
    TextField firstName = new TextField("Nombre");
    TextField lastName = new TextField("Apellido");
    EmailField email = new EmailField("Email");
    TextField username = new TextField("Username");
    PasswordField password = new PasswordField("Contraseña");

    Button save = new Button("Guardar");
    Button delete = new Button("Eliminar");
    Button close = new Button("Cancelar");

    Binder<User> binder = new BeanValidationBinder<>(User.class);

    public UserForm() {
        addClassName("user-form");
        isValid = false;
        configureElements();
    }

    public void configureElements() {
        this.email.setErrorMessage("Ingresa una dirección de mail correcta.");
        this.username.setRequired(true);
        this.username.setErrorMessage("El campo usuario es obligatorio.");
        this.password.setPlaceholder("Ingresa la contraseña.");
        this.password.setRevealButtonVisible(false);
        this.password.setRequired(true);
        binder.bindInstanceFields(this);

        add(firstName,
                lastName,
                email,
                username,
                password,
                createButtonsLayout());
    }

    public void setUser(User user) {
        this.user = user;
        binder.readBean(user);
        this.password.clear();
    }

    public String getOldHash(){
        return this.oldHash;
    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(click -> validateAndSave());
        delete.addClickListener(click -> fireEvent(new DeleteEvent(this, user)));
        close.addClickListener(click -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(evt -> save.setEnabled(binder.isValid()));

        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        List<String> mensajesError = validarCampos();
        if (mensajesError.isEmpty()) {
            try {
                binder.writeBean(user);
                fireEvent(new SaveEvent(this, user));
            } catch (ValidationException e) {
                e.printStackTrace();
            }
        }
        else{
            for (String mensaje:mensajesError
                 ) {
                mostrarMensajeError(mensaje);
            }
        }
    }

    public List<String> validarCampos(){
        List<String> mensajesError = new LinkedList<>();

        if (this.email.isEmpty()){
            mensajesError.add("El campo email es obligatorio");
        }
        if (this.username.isEmpty()){
            mensajesError.add("El campo username es obligatorio");
        }
        if (this.password.isEmpty()){
            mensajesError.add("El campo password es obligatorio");
        }
        if (!this.email.getValue().matches("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$")) {
            mensajesError.add("No es una dirección de mail válida.");
        }

        return mensajesError;
    }

    private void mostrarMensajeError(String mensajeValidacion) {
        Span mensaje = new Span(mensajeValidacion);
        Notification notification = new Notification(mensaje);
        notification.setDuration(3000);
        notification.setPosition(Notification.Position.MIDDLE);
        notification.open();
    }

    // Events
    public static abstract class UserFormEvent extends ComponentEvent<UserForm> {
        private User user;

        protected UserFormEvent(UserForm source, User user) {
            super(source, false);
            this.user = user;
        }

        public User getUser() {
            return user;
        }
    }

    public static class SaveEvent extends UserFormEvent {
        SaveEvent(UserForm source, User user) {
            super(source, user);
        }
    }

    public static class DeleteEvent extends UserFormEvent {
        DeleteEvent(UserForm source, User user) {
            super(source, user);
        }
    }

    public static class CloseEvent extends UserFormEvent {
        CloseEvent(UserForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}