package net.javaguides.springboot.tutorial.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import net.javaguides.springboot.tutorial.entity.Student;
import net.javaguides.springboot.tutorial.repository.StudentRepository;

@RestController
@RequestMapping("/students/")
public class StudentController {

	private final StudentRepository studentRepository;

	@Autowired
	public StudentController(StudentRepository studentRepository) {
		this.studentRepository = studentRepository;
	}

	@GetMapping("signup")
	public ModelAndView showSignUpForm(Student student) {
		return new ModelAndView("add-student");
	}

	@GetMapping("list")
	public ModelAndView showUpdateForm(Model model) {
		ModelAndView andView = new ModelAndView("index");
		
		model.addAttribute("students", studentRepository.findAll());
		return andView;
	}

	@PostMapping("add")
	public ModelAndView addStudent(@Valid Student student, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return new ModelAndView("add-student");
		}
		ModelAndView andView = new ModelAndView("index");
		model.addAttribute("students", studentRepository.findAll());
		studentRepository.save(student);
		return andView;
	}

	@GetMapping("edit/{id}")
	public String showUpdateForm(@PathVariable("id") long id, Model model) {
		Student student = studentRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid student Id:" + id));
		model.addAttribute("student", student);
		return "update-student";
	}

	@PostMapping("update/{id}")
	public String updateStudent(@PathVariable("id") long id, @Valid Student student, BindingResult result,
			Model model) {
		if (result.hasErrors()) {
			student.setId(id);
			return "update-student";
		}

		studentRepository.save(student);
		model.addAttribute("students", studentRepository.findAll());
		return "index";
	}

	@GetMapping("delete/{id}")
	public String deleteStudent(@PathVariable("id") long id, Model model) {
		Student student = studentRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid student Id:" + id));
		studentRepository.delete(student);
		model.addAttribute("students", studentRepository.findAll());
		return "index";
	}
}
