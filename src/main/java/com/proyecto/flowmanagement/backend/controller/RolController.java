package com.proyecto.flowmanagement.backend.controller;

import com.proyecto.flowmanagement.backend.entity.Contact;
import com.proyecto.flowmanagement.backend.entity.Rol;
import com.proyecto.flowmanagement.backend.service.ContactService;
import com.proyecto.flowmanagement.backend.service.RolService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(tags = "Rol", description = "Rol Endpoint")
@RequestMapping(value = "flowManagement/Rol", produces = "application/json")
public class RolController {

    @Autowired
    private RolService rolService;

    @ApiOperation("Get all roles")
    @RequestMapping(value="/")
    public List<Rol> listContact(){return rolService.findAll();}
}
