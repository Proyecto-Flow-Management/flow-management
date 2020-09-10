package com.proyecto.flowmanagement.ui.views.list;

import com.proyecto.flowmanagement.backend.entity.User;
import com.proyecto.flowmanagement.backend.entity.Rol;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class UserFormTest {
    private List<Rol> roles;
    private Rol rol1;
    private Rol rol2;
    private User marcUsher;

    @Before
    public void setupData() {
        marcUsher = new User();
        marcUsher.setFirstName("Marc");
        marcUsher.setLastName("Usher");
        marcUsher.setEmail("marc@usher.com");
        marcUsher.setStatus(User.Status.Status1);
    }

    @Test
    public void formFieldsPopulated() {
        UserForm form = new UserForm(roles);
        form.setUser(marcUsher);
        Assert.assertEquals("Marc", form.firstName.getValue());
        Assert.assertEquals("Usher", form.lastName.getValue());
        Assert.assertEquals("marc@usher.com", form.email.getValue());
        Assert.assertEquals(User.Status.Status1, form.status.getValue());
    }

    @Test
    public void saveEventHasCorrectValues() {
        UserForm form = new UserForm(roles);
        User user = new User();
        form.setUser(user);

        form.firstName.setValue("John");
        form.lastName.setValue("Doe");
        form.email.setValue("john@doe.com");
        form.status.setValue(User.Status.Status2);

        AtomicReference<User> savedUserRef = new AtomicReference<>(null);
        form.addListener(UserForm.SaveEvent.class, e -> {
            savedUserRef.set(e.getUser());
        });
        form.save.click();
        User savedUser = savedUserRef.get();

        Assert.assertEquals("John", savedUser.getFirstName());
        Assert.assertEquals("Doe", savedUser.getLastName());
        Assert.assertEquals("john@doe.com", savedUser.getEmail());
        Assert.assertEquals(User.Status.Status2, savedUser.getStatus());
    }
}