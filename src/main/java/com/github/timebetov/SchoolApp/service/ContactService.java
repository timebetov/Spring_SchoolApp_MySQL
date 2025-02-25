package com.github.timebetov.SchoolApp.service;

import com.github.timebetov.SchoolApp.config.TimeSchoolProps;
import com.github.timebetov.SchoolApp.constants.SchoolConstants;
import com.github.timebetov.SchoolApp.model.Contact;
import com.github.timebetov.SchoolApp.repository.ContactRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private TimeSchoolProps timeSchoolProps;

    public boolean saveMessageDetails(Contact contact) {

        boolean isSaved = false;
        contact.setStatus(SchoolConstants.OPEN);
        Contact savedContact = contactRepository.save(contact);
        if (null != savedContact && savedContact.getContactId() > 0) {
            isSaved = true;
        }
        return isSaved;
    }

    public Page<Contact> findMsgsWithOpenStatus(int pageNum, String sortField, String sortDir) {

        int pageSize = timeSchoolProps.getPageSize();
        if (null != timeSchoolProps.getContact() && null != timeSchoolProps.getContact().get("pageSize")) {
            pageSize = Integer.parseInt(timeSchoolProps.getContact().get("pageSize").trim());
        }
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize,
                sortDir.equals("asc")
                        ? Sort.by(sortField).ascending()
                        : Sort.by(sortField).descending());
        Page<Contact> msgPage = contactRepository.findByStatus(SchoolConstants.OPEN, pageable);
        return msgPage;
    }

    public boolean updateMsgStatus(int contactId) {

        boolean isUpdated = false;
        int rows = contactRepository.updateStatusById(SchoolConstants.CLOSE, contactId);
        if (rows > 0) {
            isUpdated = true;
        }
        return isUpdated;
    }
}
