package project.demo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import project.demo.dto.PageDTO;
import project.demo.dto.SearchTicketDTO;
import project.demo.dto.TicketDTO;
import project.demo.entity.Department;
import project.demo.entity.Ticket;
import project.demo.repository.TicketRepo;

public interface TicketService {
	
	List<TicketDTO> getAll();
	
	void create(TicketDTO ticketDTO);

	void update(TicketDTO ticketDTO);

	void delete(int id);

	TicketDTO getById(int id);

	PageDTO<List<TicketDTO>> search(SearchTicketDTO searchTicketDTO);
}

@Service
class TicketServiceIml implements TicketService {
	@Autowired
	TicketRepo ticketRepo;

	@Override
	public List<TicketDTO> getAll() {
		List<Ticket> ticketList = ticketRepo.findAll();

		return ticketList.stream().map(u -> convert(u)).collect(Collectors.toList());
	
	}
	
	@Override
	@Transactional
	public void create(TicketDTO ticketDTO) {
		Ticket ticket = new ModelMapper().map(ticketDTO, Ticket.class);
		// save entity
		ticketRepo.save(ticket);
	}

	@Override
	@Transactional
	public void update(TicketDTO ticketDTO) {
		// check
		Ticket ticket = ticketRepo.findById(ticketDTO.getId()).orElse(null);
		if (ticket != null) {
			ticket.setClientName(ticketDTO.getClientName());
			ticket.setClientPhone(ticketDTO.getClientPhone());
			ticket.setContent(ticketDTO.getContent());
			ticket.setStatus(ticketDTO.isStatus());
			ticket.setCreatedAt(ticketDTO.getCreatedAt());
			ticket.setProcessDate(ticketDTO.getProcessDate());
			
			Department department = new Department();
			department.setId(ticketDTO.getDepartment().getId());
			
			ticket.setDepartment(department);
			
			ticketRepo.save(ticket);
		}
	}

	@Override
	public void delete(int id) {
		ticketRepo.deleteById(id);

	}

	@Override
	public TicketDTO getById(int id) {
		Ticket ticket = ticketRepo.findById(id).orElse(null);
		if (ticket != null) {
			return convert(ticket);
		}
		return null;
	}


	private TicketDTO convert(Ticket ticket) {
		return new ModelMapper().map(ticket, TicketDTO.class);
	}

	@Override
	public PageDTO<List<TicketDTO>> search(SearchTicketDTO searchTicketDTO) {
		Sort sortBy = Sort.by("createdAt").ascending();
		// check null
		if (StringUtils.hasText(searchTicketDTO.getSortedField())) {
			sortBy = Sort.by(searchTicketDTO.getSortedField()).ascending();
		}

		if (searchTicketDTO.getCurrentPage() == null) {
			searchTicketDTO.setCurrentPage(0);
		}

		if (searchTicketDTO.getSize() == null) {
			searchTicketDTO.setSize(5);
		}
		
		PageRequest pageRequest = PageRequest.of(searchTicketDTO.getCurrentPage(), searchTicketDTO.getSize(), sortBy);
		Page<Ticket> page = ticketRepo.findAll(pageRequest);
		if (searchTicketDTO.getStart() != null && searchTicketDTO.getEnd() != null) {
			page = ticketRepo.searchByDate(searchTicketDTO.getStart(), searchTicketDTO.getEnd(), pageRequest);
		}
		if (searchTicketDTO.getDepartmentId() != null) {
			page = ticketRepo.searchByDepartmentId(searchTicketDTO.getDepartmentId(), pageRequest);
		}
		if (StringUtils.hasText(searchTicketDTO.getKeyword())) {
			page = ticketRepo.searchByName(searchTicketDTO.getKeyword(), pageRequest);
		}

		PageDTO<List<TicketDTO>> pageDTO = new PageDTO<>();
		pageDTO.setTotalPages(page.getTotalPages());
		pageDTO.setTotalElements(page.getTotalElements());

		List<TicketDTO> userDTOs = page.get().map(u -> convert(u)).collect(Collectors.toList());
		pageDTO.setData(userDTOs);
		return pageDTO;
	}

}
