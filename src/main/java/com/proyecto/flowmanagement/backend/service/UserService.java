package com.proyecto.flowmanagement.backend.service;

import com.proyecto.flowmanagement.backend.def.EStatus;
import com.proyecto.flowmanagement.backend.persistence.entity.User;
import com.proyecto.flowmanagement.backend.persistence.entity.Rol;
import com.proyecto.flowmanagement.backend.persistence.repository.UserRepository;
import com.proyecto.flowmanagement.backend.persistence.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserService {

    private static final Logger LOGGER = Logger.getLogger(UserService.class.getName());

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RolRepository rolRepository;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User find(String filterText) {
        return userRepository.search(filterText).get(0);
    }

    public long count() {
        return userRepository.count();
    }

    public void delete(User user) {
        userRepository.delete(user);
    }

    public void save(User user) {
        if (user == null) {
            LOGGER.log(Level.SEVERE,
                    "User is null. Are you sure you have connected your form to the application?");
            return;
        }
        userRepository.save(user);
    }
}