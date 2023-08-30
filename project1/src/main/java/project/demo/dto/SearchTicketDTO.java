package project.demo.dto;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class SearchTicketDTO extends SearchDTO {
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date start;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date end;
	
	private boolean status;
	// de int thi kh check dc null 
	private Integer departmentId;
}
