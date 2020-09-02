package com.proyecto.flowmanagement.backend.controller;

import com.proyecto.flowmanagement.backend.entity.Contact;
import com.proyecto.flowmanagement.backend.service.ContactService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(tags = "Contact", description = "Contact Endpoint")
@RequestMapping(value = "flowManagement/Contact", produces = "application/json")
public class ContactController {

    @Autowired
    private ContactService contactService;

    @ApiOperation("Get all contacts")
    @RequestMapping(value="/")
    public List<Contact> listContact(){return contactService.findAll();}
}
