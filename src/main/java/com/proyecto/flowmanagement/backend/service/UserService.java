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

    // Esto saquenlo de aca!!!
    @PostConstruct
    public void populateTestData() {

        if (rolRepository.count() == 0) {
            rolRepository.saveAll(
                    Stream.of("Administrator 0", "User 1")
                            .map(name->{
                                String[] split = name.split(" ");
                                return new Rol(split[0],split[1]);
                            })
                            .collect(Collectors.toList()));
        }

        if (userRepository.count() == 0) {
            Random r = new Random(0);
            List<Rol> roles = rolRepository.findAll();
            userRepository.saveAll(
                    Stream.of("Bernardo Rychtenberg 0","Marcelo Crivelli 1", "Hector Barnada 1", "Rafael Pelacchi 1")
                            .map(name -> {
                                String[] split = name.split(" ");
                                User user = new User();
                                user.setFirstName(split[0]);
                                user.setLastName(split[1]);
                                user.setRol(roles.get(Integer.parseInt(split[2])));
                                user.setStatus(EStatus.values()[r.nextInt(EStatus.values().length)]);
                                String email = (user.getFirstName() + "." + user.getLastName() + "@gmail.com").toLowerCase();
                                user.setEmail(email);
                                return user;
                            }).collect(Collectors.toList()));
        }
    }
}