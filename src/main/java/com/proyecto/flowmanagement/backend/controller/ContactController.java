package com.proyecto.flowmanagement.backend.controller;
import com.proyecto.flowmanagement.backend.entity.Contact;
import com.proyecto.flowmanagement.backend.service.ContactService;
import com.proyecto.flowmanagement.ui.MainLayout;
import com.vaadin.flow.router.Route;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(tags = "Contact", description = "Contact Endpoint")
@Route(value = "flowManagement/Contact", layout = MainLayout.class)
public class ContactController {

    @Autowired
    private ContactService contactService;

    @ApiOperation("Get all Contacts")
    @RequestMapping("/")
    public List<Contact> findAll(){
        return contactService.findAll();
    }
}
