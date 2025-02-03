package com.github.timebetov.SchoolApp.controller;

import com.github.timebetov.SchoolApp.model.Holiday;
import com.github.timebetov.SchoolApp.repository.HolidaysRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
public class HolidaysController {

    @Autowired
    private HolidaysRepository holidaysRepository;

    @GetMapping("/holidays/{display}")
    public String displayHolidays(@PathVariable String display, Model model) {

        if (display != null) {
            if (display.equals("all")) {
                model.addAttribute("festival", true);
                model.addAttribute("federal", true);
            } else if (display.equals("festival")) {
                model.addAttribute("festival", true);
            } else if (display.equals("federal")) {
                model.addAttribute("federal", true);
            }
        }

        Iterable<Holiday> holidays = holidaysRepository.findAll();
        List<Holiday> holidayList = StreamSupport
                .stream(holidays.spliterator(), false)
                .toList();

        Holiday.Type[] types = Holiday.Type.values();
        for (Holiday.Type type : types) {
            model.addAttribute(type.toString(),
                    (holidayList.stream().filter(holiday -> holiday.getType().equals(type)).collect(Collectors.toList())));
        }

        return "holidays.html";
    }
}
