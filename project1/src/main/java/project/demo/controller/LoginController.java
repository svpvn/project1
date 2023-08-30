package project.demo.controller;


import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
	@GetMapping("/login")
	public String login() {
		//map url vao 1 ham, tra ve ten file view
		return "login.html";
	}
	
	@PostMapping("/login")
	public String login(HttpSession session,
			@RequestParam("username") String username,
			@RequestParam("password") String password){
		//Khi một yêu cầu được gửi đến username?giá trị sẽ được lấy từ tham số username 
		// và truyền vào phần xử lý của phương thức username.
		if(username.equals("admin") && password.equals("123")) {
			session.setAttribute("username", username);
			return "redirect:/hello";
		}else {
			return "redirect:/login";
		}
	}
}
