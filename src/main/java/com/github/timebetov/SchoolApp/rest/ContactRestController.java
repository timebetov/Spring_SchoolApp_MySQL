package com.github.timebetov.SchoolApp.rest;

import com.github.timebetov.SchoolApp.constants.SchoolConstants;
import com.github.timebetov.SchoolApp.model.Contact;
import com.github.timebetov.SchoolApp.model.Response;
import com.github.timebetov.SchoolApp.repository.ContactRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/contact")
@CrossOrigin(origins = "*")
public class ContactRestController {

    @Autowired
    ContactRepository contactRepository;

    @GetMapping("/getMessagesByStatus")
    public List<Contact> getMessagesByStatus(@RequestParam(name = "status") String status) {
        return contactRepository.findByStatus(status);
    }

    @GetMapping("/getAllMsgsByStatus")
    public List<Contact> getAllMsgsByStatus(@RequestBody Contact contact) {
        if (null != contact && contact.getStatus() != null) {
            return contactRepository.findByStatus(contact.getStatus());
        } else {
            return List.of();
        }
    }

    @PostMapping("/saveMsg")
    public ResponseEntity<Response> saveMsg(@RequestHeader("invocationFrom") String invocationFrom,
                                            @Valid @RequestBody Contact contact) {

        log.info(String.format("Header invocationFrom = %s", invocationFrom));
        contactRepository.save(contact);
        Response response = new Response();
        response.setStatusCode("200");
        response.setStatusMsg("Message saved successfully");
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("isMsgSaved", "true")
                .body(response);
    }

    @DeleteMapping("/deleteMsg")
    public ResponseEntity<Response> deleteMsg(RequestEntity<Contact> requestEntity) {

        HttpHeaders headers = requestEntity.getHeaders();
        headers.forEach((key, value) -> {
            log.info(String.format(
                    "Header '%s' = %s", key, value.stream().collect(Collectors.joining("|"))
            ));
        });
        Contact contact = requestEntity.getBody();
        contactRepository.deleteById(contact.getContactId());

        Response response = new Response();
        response.setStatusCode("200");
        response.setStatusMsg("Message deleted successfully");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PatchMapping("/closeMsg")
    public ResponseEntity<Response> closeMsg(@RequestBody Contact contactId) {

        Response response = new Response();
        Optional<Contact> contact = contactRepository.findById(contactId.getContactId());
        if (contact.isPresent()) {
            contact.get().setStatus(SchoolConstants.CLOSE);
            contactRepository.save(contact.get());
        } else {
            response.setStatusCode("400");
            response.setStatusMsg("Invalid Contact ID received");
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(response);
        }
        response.setStatusCode("200");
        response.setStatusMsg("Message successfully closed");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

}