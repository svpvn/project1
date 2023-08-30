package project.demo.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import project.demo.dto.DepartmentDTO;
import project.demo.dto.PageDTO;
import project.demo.dto.SearchDTO;
import project.demo.dto.UserDTO;
import project.demo.service.DepartmentService;
import project.demo.service.UserService;

@Controller
public class UserController {

	@Autowired // DI
	UserService userService;
	
	@Autowired
	DepartmentService departmentService;

	@GetMapping("/user/list")
	// model day dl qua view = rq cua servlet
	public String list(Model model) {

		List<UserDTO> UserDTOs = userService.getAll();
		model.addAttribute("userList", UserDTOs);
		model.addAttribute("searchDTO", new SearchDTO());
		return "user.html";
	}

	@GetMapping("/user/new")
	public String newUser(Model model) {
		PageDTO<List<DepartmentDTO>> pageDTO =
				departmentService.search(new SearchDTO());
		
		model.addAttribute("user", new UserDTO());
		model.addAttribute("departmentList", pageDTO.getData());
		return "new-user.html";
	}

	@PostMapping("/user/new")
	// @ModelAttribute map tat cac thuoc tinh input vao userDTO
	public String newUser(@ModelAttribute("user") @Valid UserDTO userDTO, 
			BindingResult bindingResult,
			Model model)
			throws 	IOException {

		if (bindingResult.hasErrors()) {
			PageDTO<List<DepartmentDTO>> pageDTO =
					departmentService.search(new SearchDTO());
			
			model.addAttribute("departmentList", pageDTO.getData());
			return "new-user.html"; // khi co loi xay ra, return views
		}

		if (!userDTO.getFile().isEmpty()) {
			String filename = userDTO.getFile().getOriginalFilename();
			// luu lai file vao o cung may chu
			File saveFile = new File("D:\\JavaWeb\\" + filename);
			userDTO.getFile().transferTo(saveFile);
			userDTO.setAvatarURL(filename);
		}
		userService.create(userDTO);
		return "redirect:/user/list";
	}

	@GetMapping("/user/download")
	public void download(@RequestParam("filename") String filename, HttpServletResponse resp) throws IOException {
		File file = new File("D:\\" + filename);
		Files.copy(file.toPath(), resp.getOutputStream());
	}

	@GetMapping("/user/delete")
	public String delete(@RequestParam("id") int id) {
		userService.delete(id);
		return "redirect:/user/list";
	}

	@GetMapping("/user/search")
	public String search(Model model, @ModelAttribute @Valid SearchDTO searchDTO, BindingResult bindingResult) {
		// khi empty, mac dinh la null, neu int co ban bat buoc phai la so

		if (bindingResult.hasErrors()) {
			return "user.html"; // khi co loi xay ra, return views
		}

		PageDTO<List<UserDTO>> pageUser = userService.searchName(searchDTO);
		model.addAttribute("userList", pageUser.getData());
		model.addAttribute("totalPage", pageUser.getTotalPages());
		model.addAttribute("totalElements", pageUser.getTotalElements());
		model.addAttribute("searchDTO", searchDTO);
		return "user.html";
	}

	@GetMapping("/user/edit")
	public String edit(@RequestParam("id") int id, Model model) {
		UserDTO userDTO = userService.getById(id); // doc User tu db
		PageDTO<List<DepartmentDTO>> pageDTO =
				departmentService.search(new SearchDTO());
		
		model.addAttribute("departmentList", pageDTO.getData());
		model.addAttribute("user", userDTO); // day thong tin user qua view
		
		return "edit-user.html";
	}

	@PostMapping("/user/edit")
	public String edit(@ModelAttribute UserDTO userDTO,
			BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			PageDTO<List<DepartmentDTO>> pageDTO =
					departmentService.search(new SearchDTO());
			
			model.addAttribute("departmentList", pageDTO.getData());
			
			return "edit-user.html";
		}	
		userService.update(userDTO);
		return "redirect:/user/list";

	}
}
