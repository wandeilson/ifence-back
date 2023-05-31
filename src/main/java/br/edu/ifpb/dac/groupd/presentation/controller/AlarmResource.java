package br.edu.ifpb.dac.groupd.presentation.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import br.edu.ifpb.dac.groupd.model.entities.Fence;
import br.edu.ifpb.dac.groupd.model.entities.Location;
import br.edu.ifpb.dac.groupd.model.repository.FenceRepository;
import br.edu.ifpb.dac.groupd.model.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.edu.ifpb.dac.groupd.business.exception.AlarmNotFoundException;
import br.edu.ifpb.dac.groupd.business.service.AlarmService;
import br.edu.ifpb.dac.groupd.business.service.converter.AlarmConverterService;
import br.edu.ifpb.dac.groupd.model.entities.Alarm;
import br.edu.ifpb.dac.groupd.presentation.dto.AlarmResponse;
import br.edu.ifpb.dac.groupd.presentation.dto.PeriodRequest;

@RestController
@RequestMapping("/alarms")
public class AlarmResource {
	
	@Autowired
	private AlarmService alarmService;
	
	@Autowired
	private LocationRepository locationRepository;
	
	@Autowired
	private FenceRepository fenceRepository;
	
	@Autowired
	private AlarmConverterService alarmConverter;
	
	@PostMapping("/{idLocation}/{idFence}")
	public ResponseEntity<?> create(@PathVariable("idLocation") Long idLocation,
									@PathVariable("idFence") Long idFence) throws AlarmNotFoundException{
		Optional<Location> locationOptional = locationRepository.findById(idLocation);
		Optional<Fence> fenceOptional = fenceRepository.findById(idFence);
		Fence fence = null;
		Location location = null;
		if (locationOptional.isPresent()){
			location = locationOptional.get();
		}
		
		if (fenceOptional.isPresent()){
			fence = fenceOptional.get();
		}

		Alarm alarm = alarmService.saveAlarm(location, fence);

		return ResponseEntity.ok(alarm);

	}
	

	@PatchMapping("/{id}")
	public ResponseEntity<?> alarmSeen(@PathVariable("id") Long idAlarm) throws AlarmNotFoundException{
		Alarm alarm = alarmService.alarmSeen(idAlarm);
		AlarmResponse dto = alarmConverter.alarmToResponse(alarm);
		
		return ResponseEntity.ok(dto);

	}
	
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteAlarm(@PathVariable("id") Long idAlarm) throws AlarmNotFoundException {
		alarmService.deleteAlarm(idAlarm);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> findAlarmById(@PathVariable("id") Long alarmId) throws AlarmNotFoundException{
		Alarm alarm = alarmService.findAlarmById(alarmId);
		

		AlarmResponse dto = alarmConverter.alarmToResponse(alarm);
		
		return ResponseEntity.ok(dto);
	}
	
	@GetMapping("/search")
	public ResponseEntity<?> find(
			@RequestParam(value = "idAlarm",required = true) Long idAlarm,
			@RequestParam(value = "seen", required = false) boolean seen) {
		
		Alarm filter = new Alarm();
		
		filter.setId(idAlarm);
		filter.setSeen(seen);
		
		List<Alarm> alarms = alarmService.findFilter(filter);
		List<AlarmResponse> dtos = alarms.stream().map(alarmConverter::alarmToResponse)
			.toList();
		
		return ResponseEntity.ok(dtos);
	}

	@GetMapping
	public ResponseEntity<?> findAll(){
		return ResponseEntity.ok( alarmService.getAll());
	}
	
	@GetMapping("/fence/{id}")
	public ResponseEntity<?> findByFence(@PathVariable("id") Long fenceId, Pageable pageable){
		Page<AlarmResponse> alarms = alarmService.findByFenceId(fenceId, pageable)
				.map(alarmConverter::alarmToResponse);
		
		return ResponseEntity.ok(alarms);
	}
	@GetMapping("/bracelet/{id}")
	public ResponseEntity<?> findByBracelet(@PathVariable("id") Long braceletId, Pageable pageable){
		Page<AlarmResponse> alarms = alarmService.findByBraceletId(braceletId, pageable)
				.map(alarmConverter::alarmToResponse);
		
		return ResponseEntity.ok(alarms);
	}

	@GetMapping("/bracelet")
	public ResponseEntity<?> findByBraceletId(
			Principal principal,
			@RequestParam(value = "id", required = false) Long braceletId,
			@RequestParam(value = "name", required = false) String braceletName,
			Pageable pageable) {
		Page<AlarmResponse> alarms;
		if (braceletId != null) {
			alarms = alarmService.findByBracelet(
				braceletId, 
				getPrincipalId(principal),
				pageable
				)
				.map(alarmConverter::alarmToResponse);
		} else {
			if (braceletName == null) {
				braceletName = "";
			}
			alarms = alarmService.findByBracelet(
				braceletName, 
				getPrincipalId(principal),
				pageable
				)
				.map(alarmConverter::alarmToResponse);
		}
		
		return ResponseEntity.ok(alarms);
	}

	@GetMapping("/period")
	public ResponseEntity<?> findHistoryByPeriod(Principal principal,
			@Valid PeriodRequest periodRequest,
			Pageable pageable) {
		Page<Alarm> alarms = alarmService.findByPeriod(
			getPrincipalId(principal), 
			periodRequest.getStartDate(), 
			periodRequest.getEndDate(),
			pageable);

		Page<AlarmResponse> dtos = alarms
			.map(alarmConverter::alarmToResponse);
		
		return ResponseEntity.ok(dtos);
	}

	private Long getPrincipalId(Principal principal) {
		return Long.parseLong(principal.getName());
	}
}
