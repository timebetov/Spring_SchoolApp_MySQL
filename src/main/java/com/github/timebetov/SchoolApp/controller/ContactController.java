package com.github.timebetov.SchoolApp.controller;

import com.github.timebetov.SchoolApp.config.TimeSchoolProps;
import com.github.timebetov.SchoolApp.model.Contact;
import com.github.timebetov.SchoolApp.service.ContactService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Slf4j
@Controller
public class ContactController {

    private final ContactService contactService;

    @Autowired
    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @RequestMapping("/contact")
    public String displayContactPage(Model model) {

        model.addAttribute("contact", new Contact());
        return "contact.html";
    }

    @RequestMapping(value = "/saveMsg", method = POST)
    public String saveMessage(@Valid @ModelAttribute("contact") Contact contact, Errors errors) {

        if (errors.hasErrors()) {
            log.error("Contact from validation failed due to: " + errors);
            return "contact.html";
        }
        contactService.saveMessageDetails(contact);
        return "redirect:/contact";
    }

    @RequestMapping("/displayMessages/page/{pageNum}")
    public ModelAndView displayMessages(Model model, @PathVariable(name = "pageNum") int pageNum,
                                        @RequestParam("sortField") String sortField,
                                        @RequestParam("sortDir") String sortDir) {

        Page<Contact> msgPage = contactService.findMsgsWithOpenStatus(pageNum, sortField, sortDir);
        List<Contact> contactMsgs = msgPage.getContent();
        ModelAndView modelAndView = new ModelAndView("messages.html");
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", msgPage.getTotalPages());
        model.addAttribute("totalMsgs", msgPage.getTotalElements());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        modelAndView.addObject("contactMessages", contactMsgs);
        return modelAndView;
    }

    @RequestMapping(value = "/closeMsg", method = GET)
    public String closeMsg(@RequestParam int id, @RequestParam int pageNum) {

        contactService.updateMsgStatus(id);
        return "redirect:/displayMessages/page/"+pageNum+"?sortField=name&sortDir=desc";
    }
}
