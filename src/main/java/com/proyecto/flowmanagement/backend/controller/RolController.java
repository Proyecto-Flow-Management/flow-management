package com.proyecto.flowmanagement.backend.controller;

import com.proyecto.flowmanagement.backend.entity.Rol;
import com.proyecto.flowmanagement.backend.service.RolService;
import com.proyecto.flowmanagement.ui.MainLayout;
import com.vaadin.flow.router.Route;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(tags = "Rol", description = "Rol Endpoint")
@Route(value = "flowManagement/Rol", layout = MainLayout.class)
public class RolController {

    @Autowired
    private RolService rolService;

    @ApiOperation("Get all Contacts")
    @RequestMapping("/")
    public List<Rol> findAll(){
        return rolService.findAll();
    }
}
