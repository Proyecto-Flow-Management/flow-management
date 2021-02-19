package com.proyecto.flowmanagement.ui.views.login;

import com.proyecto.flowmanagement.backend.persistence.entity.Guide;
import com.proyecto.flowmanagement.backend.persistence.entity.User;
import com.proyecto.flowmanagement.backend.service.Impl.GuideGeneratorServiceImp;
import com.proyecto.flowmanagement.backend.service.Impl.GuideServiceImpl;
import com.proyecto.flowmanagement.backend.service.Impl.UserServiceImpl;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Route("login")
@PageTitle("Login | Flow Management")
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    LoginForm login = new LoginForm();
    UserServiceImpl userService;

    public LoginView(UserServiceImpl userService) {
        this.userService = userService;

        if(userService.findAll().size() == 0)
        {
            User newUser = new User();
            newUser.setFirstName("Admin");
            newUser.setLastName("Admin");
            newUser.setEmail("admin@ucu.edu.uy");
            newUser.setUsername("admin");
            newUser.setPassword("$2a$10$zxvEq8XzYEYtNjbkRsJEbukHeRx3XS6MDXHMu8cNuNsRfZJWwswDy");
            newUser.setRole("ROLE_ADMIN");
            newUser.setEnabled(1);
            userService.save(newUser);
        }

        addClassName("login-view");
        setSizeFull(); 
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);

        login.setAction("login");

        add(
                new H1("Flow Management"),
                login
        );
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        // inform the user about an authentication error
        if(event.getLocation()
                .getQueryParameters()
                .getParameters()
                .containsKey("error"))
        {
            login.setError(true);
        }
    }
}
