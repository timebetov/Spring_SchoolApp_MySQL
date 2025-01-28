package com.github.timebetov.SchoolApp.service;

import com.github.timebetov.SchoolApp.constants.SchoolConstants;
import com.github.timebetov.SchoolApp.model.Contact;
import com.github.timebetov.SchoolApp.repository.ContactRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;

    public boolean saveMessageDetails(Contact contact) {

        boolean isSaved = false;
        contact.setStatus(SchoolConstants.OPEN);
        contact.setCreatedBy(SchoolConstants.ANONYMOUS);
        contact.setCreatedAt(LocalDateTime.now());
        int result = contactRepository.saveContactMsg(contact);
        if (result > 0) {
            isSaved = true;
        }
        return isSaved;
    }

    public List<Contact> findMsgsWithOpenStatus() {

        List<Contact> contactMsgs = contactRepository.findMsgsWithStatus(SchoolConstants.OPEN);
        return contactMsgs;
    }

    public boolean updateMsgStatus(int contactId, String updatedBy) {

        int result = contactRepository.updateMsgStatus(contactId, SchoolConstants.CLOSE, updatedBy);
        return (result > 0);
    }
}
