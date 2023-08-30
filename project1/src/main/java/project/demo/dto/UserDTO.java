package project.demo.dto; // data tranform object

import java.util.Date;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import lombok.Data;
//ORM framework: object - Record Table
//JPA - Hibernate
//JDBC - MySQL

@Data
public class UserDTO {
	private int id;
	@Min(value = 0,message = "{min.msg}")
	private int age;
	@NotBlank(message = "{not.blank}")
	private String name;
	private String avatarURL;
	private String username;
	private String password;
	private String homeAddress;
	//manytone
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date birthdate;
	
	private DepartmentDTO department;
	
	private MultipartFile file;
}
