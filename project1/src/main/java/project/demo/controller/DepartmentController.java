package project.demo.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import project.demo.dto.DepartmentDTO;
import project.demo.dto.PageDTO;
import project.demo.dto.SearchDTO;
import project.demo.service.DepartmentService;

@Controller
@RequestMapping("/department")
public class DepartmentController {
	@Autowired // DI : dependency inject
	DepartmentService departmentService;

	@GetMapping("/new")
	public String create(Model model) {
		model.addAttribute("department", new DepartmentDTO());
		return "new-department.html";
	}

	@PostMapping("/new")
	// @ModelAttribute map tat cac thuoc tinh input vao userDTO
	public String create(@ModelAttribute("department") @Valid DepartmentDTO departmentDTO,
			BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			return "new-department.html"; // khi co loi xay ra, return views
		}

		departmentService.create(departmentDTO);
		return "redirect:/department/search";
	}

	@GetMapping("/edit")
	public String edit(@RequestParam("id") int id, Model model) {
		DepartmentDTO departmentDTO = departmentService.getById(id); // doc User tu db
		model.addAttribute("department", departmentDTO); // day thong tin user qua view
		return "edit-department.html";
	}

	@PostMapping("/edit")
	public String edit(@ModelAttribute DepartmentDTO departmentDTO) {
		departmentService.update(departmentDTO);
		
		return "redirect:/department/search";
	}

	@GetMapping("/delete")
	public String delete(@RequestParam("id") int id) {
		departmentService.delete(id);
		return "redirect:/department/search";
	}

//	@GetMapping("/list")
//	public String list(Model model) {
//		return "redirect:/department/search";
//	}

	@GetMapping("/search")
	public String search(Model model, @ModelAttribute @Valid SearchDTO searchDTO, BindingResult bindingResult) {
		// khi empty, mac dinh la null, neu int co ban bat buoc phai la so

		if (bindingResult.hasErrors()) {
			return "departments.html"; // khi co loi xay ra, return views
		}

		PageDTO<List<DepartmentDTO>> pageDTO = departmentService.search(searchDTO);
		model.addAttribute("departmentList", pageDTO.getData());
		model.addAttribute("totalPage", pageDTO.getTotalPages());
		model.addAttribute("totalElements", pageDTO.getTotalElements());
		model.addAttribute("searchDTO", searchDTO);
		return "departments.html";
	}
}
