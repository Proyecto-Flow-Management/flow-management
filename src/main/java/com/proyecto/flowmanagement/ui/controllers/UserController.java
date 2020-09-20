package com.proyecto.flowmanagement.ui.controllers;

import com.proyecto.flowmanagement.backend.persistence.entity.User;
import com.proyecto.flowmanagement.backend.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(tags="User", description = "User Endpoint")
@RequestMapping(value = "flowManagement/User", produces = "application/json")
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation("Get all users")
    @RequestMapping(value = "/")
    public List<User> listUsers() { return userService.findAll(); }

    public User listUser(String filterText) {
        return userService.find(filterText);
    }

    public void deleteUser(User user){
        userService.delete(user);
    }

    public void saveUser(User user) {
        userService.save(user);
    }
}
