package br.edu.ifpb.dac.groupd.business.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import br.edu.ifpb.dac.groupd.business.exception.*;
import br.edu.ifpb.dac.groupd.model.entities.Fence;
import br.edu.ifpb.dac.groupd.model.repository.FenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.edu.ifpb.dac.groupd.business.service.converter.BraceletConverterService;
import br.edu.ifpb.dac.groupd.model.entities.Bracelet;
import br.edu.ifpb.dac.groupd.model.entities.User;
import br.edu.ifpb.dac.groupd.model.repository.BraceletRepository;
import br.edu.ifpb.dac.groupd.model.repository.UserRepository;
import br.edu.ifpb.dac.groupd.presentation.dto.BraceletRequest;

@Service
public class BraceletService {
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private BraceletRepository braceletRepo;
	
	@Autowired
	private BraceletConverterService converter;
	@Autowired
	private FenceService fenceService;
	@Autowired
	private FenceRepository fenceRepository;
	
	// User bracelet
	public Bracelet createBracelet(Long id, BraceletRequest dto) throws UserNotFoundException, BraceletNameAlreadyInUseException {
		
		Optional<User> register = userRepo.findById(id);
		
		if (register.isEmpty())
			throw new UserNotFoundException(id);
		
		User user = register.get();
		
		Bracelet mapped = converter.requestToBracelet(dto);

		AtomicBoolean nameAlreadyInUse = new AtomicBoolean(false);
		List<Bracelet> allBraceletsFromUser = braceletRepo.findAllBraceletsByUser(id);
		allBraceletsFromUser.forEach(bracelet ->{
			if(bracelet.getName().equals(mapped.getName())) {
				nameAlreadyInUse.set(true);
			}
		});

		if(nameAlreadyInUse.get()){
			throw new BraceletNameAlreadyInUseException();
		}
		
		Bracelet bracelet = braceletRepo.save(mapped);
		
		user.addBracelet(bracelet);
		userRepo.save(user);
		
		return bracelet;
	}
	public Page<Bracelet> getAllBracelets(Long id, Pageable pageable) throws UserNotFoundException {
		boolean register = userRepo.existsById(id);
		
		if (!register)
			throw new UserNotFoundException(id);
		
		return braceletRepo.findAllBraceletsByUser(id, pageable);
	}
	public Bracelet findByBraceletId(Long id, Long braceletId) throws UserNotFoundException, BraceletNotFoundException {
		Optional<User> register = userRepo.findById(id);
		
		if (register.isEmpty())
			throw new UserNotFoundException(id);
		
		User user = register.get();
		
		for(Bracelet bracelet : user.getBracelets()) {
			if(bracelet.getId().equals(braceletId)) {
				return bracelet;
			}
		}
		throw new BraceletNotFoundException(braceletId);
	}
	public Page<Bracelet> searchBraceletByName(Long id, String name, Pageable pageable) throws UserNotFoundException {
		Optional<User> register = userRepo.findById(id);
		
		if(register.isEmpty())
			throw new UserNotFoundException(id);
		
		return braceletRepo.findBraceletsByName(id, name, pageable);
	}
	
	public Bracelet updateBracelet(Long id, Long braceletId, BraceletRequest dto) throws UserNotFoundException, BraceletNotFoundException {
		Optional<User> register = userRepo.findById(id);
		
		if (register.isEmpty())
			throw new UserNotFoundException(id);
		
		User user = register.get();
		
		boolean existe = user.getBracelets()
			.stream()
			.anyMatch(b->b.getId().equals(braceletId));

		if(!existe) {
			throw new BraceletNotFoundException(braceletId);
		}
		
		Bracelet mapped = converter.requestToBracelet(dto);
		mapped.setId(braceletId);
		mapped.setUser(user);
		
		return braceletRepo.save(mapped);
	}
	public void deleteBracelet(Long id, Long braceletId) throws UserNotFoundException, BraceletNotFoundException, BraceletRegisteredInFenceException {
		Optional<User> register = userRepo.findById(id);
		
		if (register.isEmpty())
			throw new UserNotFoundException(id);
		
		User user = register.get();
		Set<Bracelet> bracelets = user.getBracelets();
		
		Optional<Bracelet> registerBracelet = bracelets.stream().filter((bracelet)->bracelet.getId().equals(braceletId)).findFirst();
		if(register.isEmpty()) {
			throw new BraceletNotFoundException(braceletId);
		}
		AtomicBoolean braceletInFence = new AtomicBoolean(false);
		List<Fence> allFencesForUser = fenceRepository.findAllFencesByUser(id);
		allFencesForUser.forEach(fence -> {
			if (fence.getBracelets().contains(registerBracelet.get())) {
				braceletInFence.set(true);
			}
		});

		if(braceletInFence.get()){
			throw new BraceletRegisteredInFenceException("");
		}

		user.removeBracelet(registerBracelet.get());
		userRepo.save(user);
		braceletRepo.deleteById(braceletId);
	}
}
