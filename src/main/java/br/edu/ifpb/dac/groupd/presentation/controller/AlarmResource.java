package br.edu.ifpb.dac.groupd.presentation.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import br.edu.ifpb.dac.groupd.model.entities.Fence;
import br.edu.ifpb.dac.groupd.model.entities.Location;
import br.edu.ifpb.dac.groupd.model.repository.FenceRepository;
import br.edu.ifpb.dac.groupd.model.repository.LocationRepository;
import br.edu.ifpb.dac.groupd.presentation.dto.AlarmResponseMin;
import br.edu.ifpb.dac.groupd.presentation.dto.FenceResponse;
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
	
	@GetMapping("/all/{userId}")
	public ResponseEntity<?> findAllAlarmsByUser (@PathVariable ("userId") Long userId) {
		List<Alarm> allAlarms = alarmService.findAllAlarmsByUser(userId);
		AlarmResponseMin alarmResponseMin = new AlarmResponseMin();
		List<AlarmResponseMin> allAllarmsResponsesMin = new ArrayList<>();
		for (Alarm a: allAlarms) {
			allAllarmsResponsesMin.add(alarmToAlarmResponseMin(a,alarmResponseMin));
		}
		return ResponseEntity.ok().body(allAllarmsResponsesMin);
	}

	private AlarmResponseMin addUniqueAlarmsResponses(AlarmResponseMin a,
															List<AlarmResponseMin> listAlarms){
		int cont = 0;
		for (AlarmResponseMin alarm: listAlarms) {
			if(a.getId().equals(alarm.getId())){
				cont++;
			}
		}
		if (cont==1){
			System.out.println("add é true");
			return a;

		}
		else{
			System.out.println("tamanho "+ cont);
			return null;
		}
	}

	private AlarmResponseMin alarmToAlarmResponseMin(Alarm a, AlarmResponseMin alarmResponseMin){
		alarmResponseMin.setDistance(a.getDistance());
		alarmResponseMin.setBraceletName(a.getLocation().getBracelet().getName());
		alarmResponseMin.setBraceletId(a.getLocation().getBracelet().getId());
		alarmResponseMin.setExceeded(a.getExceeded());
		alarmResponseMin.setId(a.getId());
		FenceResponse fenceResponse = new FenceResponse();
		fenceResponse.setId(a.getFence().getId());
		fenceResponse.setCoordinate(a.getFence().getCoordinate());
		fenceResponse.setName(a.getFence().getName());
		fenceResponse.setRadius(a.getFence().getRadius());
		fenceResponse.setFinishTime(a.getFence().getFinishTime());
		fenceResponse.setStartTime(a.getFence().getStartTime());
		alarmResponseMin.setFence(fenceResponse);

		return alarmResponseMin;
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
		List<Alarm> allAlarms = alarmService.getAll();
		AlarmResponseMin alarmResponseMin = new AlarmResponseMin();
		List<AlarmResponseMin> allAllarmsResponsesMin = new ArrayList<>();
		for (Alarm a: allAlarms) {
			allAllarmsResponsesMin.add(alarmToAlarmResponseMin(a,alarmResponseMin));
		}
		return ResponseEntity.ok().body(allAllarmsResponsesMin);
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
