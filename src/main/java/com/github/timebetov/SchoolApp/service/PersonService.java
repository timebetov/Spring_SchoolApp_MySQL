package com.github.timebetov.SchoolApp.service;

import com.github.timebetov.SchoolApp.constants.SchoolConstants;
import com.github.timebetov.SchoolApp.model.Person;
import com.github.timebetov.SchoolApp.model.Roles;
import com.github.timebetov.SchoolApp.repository.PersonRepository;
import com.github.timebetov.SchoolApp.repository.RolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private RolesRepository rolesRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean createNewPerson(Person person) {

        boolean isSaved = false;
        Roles role = rolesRepository.getByRoleName(SchoolConstants.STUDENT_ROLE);
        person.setRoles(role);
        person.setPwd(passwordEncoder.encode(person.getPwd()));
        person = personRepository.save(person);
        if (null != person && person.getPersonId() > 0) {
            isSaved = true;
        }
        return isSaved;
    }
}
