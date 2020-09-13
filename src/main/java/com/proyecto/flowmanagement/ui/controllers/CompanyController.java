package com.proyecto.flowmanagement.ui.controllers;

import com.proyecto.flowmanagement.backend.entity.Company;
import com.proyecto.flowmanagement.backend.service.CompanyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(tags="Company", description = "Company Endpoint")
@RequestMapping(value = "flowManagement/Company", produces = "application/json")
public class CompanyController {
    @Autowired
    private CompanyService companyService;

    @ApiOperation("Get all companies")
    @RequestMapping(value = "/")
    public List<Company> listCompany() { return companyService.findAll(); }
}
