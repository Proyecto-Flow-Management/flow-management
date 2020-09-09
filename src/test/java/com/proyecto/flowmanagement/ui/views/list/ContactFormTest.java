package com.proyecto.flowmanagement.ui.views.list;

import com.proyecto.flowmanagement.backend.entity.Contact;
import com.proyecto.flowmanagement.backend.entity.Rol;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class ContactFormTest {
    private List<Rol> roles;
    private Rol rol1;
    private Rol rol2;
    private Contact marcUsher;

    @Before
    public void setupData() {
        marcUsher = new Contact();
        marcUsher.setFirstName("Marc");
        marcUsher.setLastName("Usher");
        marcUsher.setEmail("marc@usher.com");
        marcUsher.setStatus(Contact.Status.NotContacted);
    }

    @Test
    public void formFieldsPopulated() {
        ContactForm form = new ContactForm(roles);
        form.setContact(marcUsher);
        Assert.assertEquals("Marc", form.firstName.getValue());
        Assert.assertEquals("Usher", form.lastName.getValue());
        Assert.assertEquals("marc@usher.com", form.email.getValue());
        Assert.assertEquals(Contact.Status.NotContacted, form.status.getValue());
    }

    @Test
    public void saveEventHasCorrectValues() {
        ContactForm form = new ContactForm(roles);
        Contact contact = new Contact();
        form.setContact(contact);

        form.firstName.setValue("John");
        form.lastName.setValue("Doe");
        form.email.setValue("john@doe.com");
        form.status.setValue(Contact.Status.Customer);

        AtomicReference<Contact> savedContactRef = new AtomicReference<>(null);
        form.addListener(ContactForm.SaveEvent.class, e -> {
            savedContactRef.set(e.getContact());
        });
        form.save.click();
        Contact savedContact = savedContactRef.get();

        Assert.assertEquals("John", savedContact.getFirstName());
        Assert.assertEquals("Doe", savedContact.getLastName());
        Assert.assertEquals("john@doe.com", savedContact.getEmail());
        Assert.assertEquals(Contact.Status.Customer, savedContact.getStatus());
    }
}