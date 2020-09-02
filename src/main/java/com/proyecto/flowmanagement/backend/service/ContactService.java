package com.proyecto.flowmanagement.backend.service;

import com.proyecto.flowmanagement.backend.entity.Company;
import com.proyecto.flowmanagement.backend.entity.Contact;
import com.proyecto.flowmanagement.backend.entity.Rol;
import com.proyecto.flowmanagement.backend.repository.CompanyRepository;
import com.proyecto.flowmanagement.backend.repository.ContactRepository;
import com.proyecto.flowmanagement.backend.repository.RolRepository;
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
public class ContactService {
    private static final Logger LOGGER = Logger.getLogger(ContactService.class.getName());

    @Autowired
    private ContactRepository contactRepository;
    private RolRepository rolRepository;
    private CompanyRepository companyRepository;

    public ContactService(ContactRepository contactRepository,
                          RolRepository rolRepository,
                          CompanyRepository companyRepository) {
        this.contactRepository = contactRepository;
        this.rolRepository = rolRepository;
        this.companyRepository = companyRepository;
    }

    public List<Contact> findAll() {
        return contactRepository.findAll();
    }

    public List<Contact> findAll(String filterText) {
        if(filterText  == null || filterText.isEmpty()){
            return contactRepository.findAll();
        } else {
            return contactRepository.search(filterText);
        }
    }

    public long count() {
        return contactRepository.count();
    }

    public void delete(Contact contact) {
        contactRepository.delete(contact);
    }

    public void save(Contact contact) {
        if (contact == null) {
            LOGGER.log(Level.SEVERE,
                    "Contact is null. Are you sure you have connected your form to the application?");
            return;
        }
        contactRepository.save(contact);
    }

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

        if (companyRepository.count() == 0) {
            companyRepository.saveAll(
                    Stream.of("UTE", "ANTEL")
                            .map(Company::new)
                            .collect(Collectors.toList()));
        }

        if (contactRepository.count() == 0) {
            Random r = new Random(0);
            List<Rol> roles = rolRepository.findAll();
            contactRepository.saveAll(
                    Stream.of("Bernardo Rychtenberg 0","Marcelo Crivelli 1", "Hector Barnada 1", "Rafael Pelacchi 1")
                            .map(name -> {
                                String[] split = name.split(" ");
                                Contact contact = new Contact();
                                contact.setFirstName(split[0]);
                                contact.setLastName(split[1]);
                                contact.setRol(roles.get(Integer.parseInt(split[2])));
                                contact.setStatus(Contact.Status.values()[r.nextInt(Contact.Status.values().length)]);
                                String email = (contact.getFirstName() + "." + contact.getLastName() + "@gmail.com").toLowerCase();
                                contact.setEmail(email);
                                return contact;
                            }).collect(Collectors.toList()));
        }
    }
}