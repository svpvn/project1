package project.demo.dto;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class TicketDTO {
	private Integer id;
	private String clientName;
	private String clientPhone;
	private String content;
	private boolean status;
	private Date createdAt;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date processDate;
	private DepartmentDTO department;
	

}
