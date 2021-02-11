package com.proyecto.flowmanagement.backend.service.Impl;

import com.proyecto.flowmanagement.backend.persistence.entity.User;
import com.proyecto.flowmanagement.backend.persistence.repository.RolRepository;
import com.proyecto.flowmanagement.backend.persistence.repository.UserRepository;
import com.proyecto.flowmanagement.backend.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class UserServiceImpl implements IUserService {

    private static final Logger LOGGER = Logger.getLogger(UserServiceImpl.class.getName());

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RolRepository rolRepository;

    public List<User> findAll() {
        return new LinkedList<>();
    }

    public User find(String filterText) {
        return userRepository.search(filterText).get(0);
    }

    public List<User> findAll(String filterText) {
        return userRepository.search(filterText);
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
