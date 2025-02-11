package com.github.timebetov.SchoolApp.controller;

import com.github.timebetov.SchoolApp.model.Courses;
import com.github.timebetov.SchoolApp.model.Person;
import com.github.timebetov.SchoolApp.model.TimeClass;
import com.github.timebetov.SchoolApp.repository.CoursesRepository;
import com.github.timebetov.SchoolApp.repository.PersonRepository;
import com.github.timebetov.SchoolApp.repository.TimeClassRepository;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("admin")
public class AdminController {

    @Autowired
    TimeClassRepository timeClassRepository;

    @Autowired
    PersonRepository personRepository;

    @Autowired
    CoursesRepository coursesRepository;

    @RequestMapping("/displayClasses")
    public ModelAndView displayClasses(Model model) {

        List<TimeClass> timeClasses = timeClassRepository.findAll();
        ModelAndView modelAndView = new ModelAndView("classes.html");
        modelAndView.addObject("timeClasses", timeClasses);
        modelAndView.addObject("timeClass", new TimeClass());
        return modelAndView;
    }

    @PostMapping("/addNewClass")
    public ModelAndView addNewClass(Model model, @ModelAttribute("timeClass") TimeClass timeClass) {

        timeClassRepository.save(timeClass);
        return new ModelAndView("redirect:/admin/displayClasses");
    }

    @RequestMapping("/deleteClass")
    public ModelAndView deleteClass(Model model, @RequestParam int id) {

        Optional<TimeClass> timeClass = timeClassRepository.findById(id);
        for (Person person : timeClass.get().getPersons()) {
            person.setTimeClass(null);
            personRepository.save(person);
        }
        timeClassRepository.deleteById(id);
        ModelAndView modelAndView = new ModelAndView("redirect:/admin/displayClasses");
        return modelAndView;
    }

    @GetMapping("/displayStudents")
    public ModelAndView displayStudents(Model model, @RequestParam int classId, HttpSession session,
                                        @RequestParam(value = "error", required = false) String error) {

        String errorMessage = null;
        ModelAndView modelAndView = new ModelAndView("students.html");
        Optional<TimeClass> timeClass = timeClassRepository.findById(classId);
        modelAndView.addObject("timeClass", timeClass.get());
        modelAndView.addObject("person", new Person());
        session.setAttribute("timeClass", timeClass.get());
        if (error != null) {
            errorMessage = "Imvalid email entered!!";
            modelAndView.addObject("errorMessage", errorMessage);
        }
        return modelAndView;
    }

    @PostMapping("/addStudent")
    public ModelAndView addStudent(Model model, @ModelAttribute("person") Person person, HttpSession session) {

        ModelAndView modelAndView = new ModelAndView();
        TimeClass timeClass = (TimeClass) session.getAttribute("timeClass");
        Person personEntity = personRepository.readByEmail(person.getEmail());
        if (null == personEntity || !(personEntity.getPersonId() > 0)) {
            modelAndView.setViewName("redirect:/admin/displayStudents?classId="+timeClass.getClassId()
            +"&error=true");
            return modelAndView;
        }
        personEntity.setTimeClass(timeClass);
        personRepository.save(personEntity);
        timeClass.getPersons().add(personEntity);
        timeClassRepository.save(timeClass);
        modelAndView.setViewName("redirect:/admin/displayStudents?classId="+timeClass.getClassId());
        return modelAndView;
    }

    @GetMapping("/deleteStudent")
    public ModelAndView deleteStudent(Model model, @RequestParam int personId, HttpSession session) {

        TimeClass timeClass = (TimeClass) session.getAttribute("timeClass");
        Optional<Person> person = personRepository.findById(personId);
        person.get().setTimeClass(null);
        timeClass.getPersons().remove(person.get());
        TimeClass timeClassSaved = timeClassRepository.save(timeClass);
        session.setAttribute("timeClass", timeClassSaved);
        ModelAndView modelAndView = new ModelAndView("redirect:/admin/displayStudents?classId="+timeClass.getClassId());
        return modelAndView;
    }

    @GetMapping("/displayCourses")
    public ModelAndView displayCourses(Model model) {

        List<Courses> courses = coursesRepository.findAll();
        ModelAndView modelAndView = new ModelAndView("courses_secure.html");
        modelAndView.addObject("courses", courses);
        modelAndView.addObject("course", new Courses());
        return modelAndView;
    }

    @PostMapping("/addNewCourse")
    public ModelAndView addNewCourse(Model model, @ModelAttribute("course") Courses course) {

        ModelAndView modelAndView = new ModelAndView();
        coursesRepository.save(course);
        modelAndView.setViewName("redirect:/admin/displayCourses");
        return modelAndView;
    }

    @GetMapping("/viewStudents")
    public ModelAndView viewStudents(Model model, @RequestParam int id, HttpSession session,
                                     @RequestParam(required = false) String error) {

        String errorMessage = null;
        ModelAndView modelAndView = new ModelAndView("course_students.html");
        Optional<Courses> courses = coursesRepository.findById(id);
        modelAndView.addObject("courses", courses.get());
        modelAndView.addObject("person", new Person());
        session.setAttribute("courses", courses.get());
        if (error != null) {
            errorMessage = "Invalid Email entered!!";
            modelAndView.addObject("errorMessage", errorMessage);
        }
        return modelAndView;
    }

    @PostMapping("/addStudentToCourse")
    public ModelAndView addStudentToCourse(Model model, @ModelAttribute("person") Person person,
                                           HttpSession session) {

        ModelAndView modelAndView = new ModelAndView();
        Courses courses = (Courses) session.getAttribute("courses");
        Person personEntity = personRepository.readByEmail(person.getEmail());
        if (personEntity == null || !(personEntity.getPersonId() > 0)) {
            modelAndView.setViewName("redirect:/admin/viewStudents?id="+courses.getCourseId()+"&error=true");
            return modelAndView;
        }
        personEntity.getCourses().add(courses);
        courses.getPersons().add(personEntity);
        personRepository.save(personEntity);
        session.setAttribute("courses", courses);
        modelAndView.setViewName("redirect:/admin/viewStudents?id="+courses.getCourseId());
        return modelAndView;
    }

    @GetMapping("/deleteStudentFromCourse")
    public ModelAndView deleteStudentFromCourse(Model model, @RequestParam int personId,
                                                HttpSession session) {

        Courses courses = (Courses) session.getAttribute("courses");
        Optional<Person> person = personRepository.findById(personId);
        person.get().getCourses().remove(courses);
        courses.getPersons().remove(person.get());
        personRepository.save(person.get());
        session.setAttribute("courses", courses);
        ModelAndView modelAndView = new ModelAndView("redirect:/admin/viewStudents?id="+courses.getCourseId());
        return modelAndView;
    }

}