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
import project.demo.dto.SearchTicketDTO;
import project.demo.dto.TicketDTO;
import project.demo.service.DepartmentService;
import project.demo.service.TicketService;

@Controller
@RequestMapping("/ticket")
public class TicketController {
	@Autowired
	TicketService ticketService;

	@Autowired
	DepartmentService departmentService;

	@GetMapping("/list")
	// model day dl qua view = rq cua servlet
	public String list(Model model) {

		List<TicketDTO> TicketDTOs = ticketService.getAll();
		model.addAttribute("ticketList", TicketDTOs);
		model.addAttribute("searchDTO", new SearchDTO());
		return "tickets.html";
	}

	@GetMapping("/new")
	public String newUser(Model model) {
		PageDTO<List<DepartmentDTO>> pageDTO = departmentService.search(new SearchDTO());

		model.addAttribute("ticket", new TicketDTO());
		model.addAttribute("departmentList", pageDTO.getData());
		return "new-ticket.html";
	}

	@PostMapping("/new")
	// @ModelAttribute map tat cac thuoc tinh input vao userDTO
	public String newUser(@ModelAttribute("ticket") @Valid TicketDTO ticketDTO, BindingResult bindingResult,
			Model model) throws Exception  {

		if (bindingResult.hasErrors()) {
			PageDTO<List<TicketDTO>> pageDTO = ticketService.search(new SearchTicketDTO());
			model.addAttribute("ticketList", pageDTO.getData());
			return "new-ticket.html";
		}
		ticketService.create(ticketDTO);
		return "redirect:/ticket/list";
	}

	@GetMapping("/delete")
	public String delete(@RequestParam("id") int id) {
		ticketService.delete(id);
		return "redirect:/ticket/list";
	}

	@GetMapping("/search")
	public String search(Model model, @ModelAttribute @Valid SearchTicketDTO searchTicketDTO, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			return "tickets.html"; // khi co loi xay ra, return views
		}

		PageDTO<List<TicketDTO>> pageDTO = ticketService.search(searchTicketDTO);
		model.addAttribute("ticketList", pageDTO.getData());
		model.addAttribute("totalPage", pageDTO.getTotalPages());
		model.addAttribute("totalElements", pageDTO.getTotalElements());
		model.addAttribute("searchDTO", searchTicketDTO);

		return "tickets.html";
	}

	@GetMapping("/edit")
	public String edit(@RequestParam("id") int id, Model model) {
		TicketDTO ticketDTO = ticketService.getById(id); // doc User tu db
		PageDTO<List<DepartmentDTO>> pageDTO = departmentService.search(new SearchDTO());

		model.addAttribute("departmentList", pageDTO.getData());
		model.addAttribute("ticket", ticketDTO); 

		return "edit-ticket.html";
	}

	@PostMapping("/edit")
	public String edit(@ModelAttribute TicketDTO ticketDTO, Model model) {
		
		ticketService.update(ticketDTO);
		return "redirect:/ticket/list";

	}

}
