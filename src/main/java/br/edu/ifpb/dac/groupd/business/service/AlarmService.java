package br.edu.ifpb.dac.groupd.business.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import br.edu.ifpb.dac.groupd.presentation.controller.EmailService;
import br.edu.ifpb.dac.groupd.presentation.dto.EmailDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.edu.ifpb.dac.groupd.business.exception.AlarmNotFoundException;
import br.edu.ifpb.dac.groupd.business.service.firebase.PushNotificationService;
import br.edu.ifpb.dac.groupd.model.entities.Alarm;
import br.edu.ifpb.dac.groupd.model.entities.DeviceToken;
import br.edu.ifpb.dac.groupd.model.entities.Fence;
import br.edu.ifpb.dac.groupd.model.entities.Location;
import br.edu.ifpb.dac.groupd.model.repository.AlarmRepository;
import br.edu.ifpb.dac.groupd.presentation.dto.firebase.PushNotificationRequest;

@Service
public class AlarmService {

	@Autowired
	private PushNotificationService pushNotificationService;

	@Autowired
	private AlarmRepository alarmRepository;

	@Autowired
	private CoordinateService coordinateService;

	@Autowired
	EmailService emailService;

	public Alarm saveAlarm(Location location, Fence fence) {
		Double distance = coordinateService.calculateDistance(fence.getCoordinate(), location.getCoordinate());
		if (fence.getRadius() > distance) {
			return null;
		}

		Double exceeded = distance - fence.getRadius();
		Alarm alarm = new Alarm();

		alarm.setFence(fence);
		alarm.setLocation(location);
		alarm.setSeen(false);
		alarm.setDistance(distance);
		alarm.setExceeded(exceeded);

		notifyUser(location);


		return alarmRepository.save(alarm);

	}

	public List<Alarm> findAllAlarmsByUser(Long userId){
		return alarmRepository.findAllAlarmsByUser(userId);
	}

	public List<Alarm> getAll() {
		return alarmRepository.findAll();
	}

	public Page<Alarm> findByFenceId(Long fenceId, Pageable pageable) {
		return alarmRepository.findByFence(fenceId, pageable);
	}

	public Page<Alarm> findByBraceletId(Long braceletId, Pageable pageable) {
		return alarmRepository.findByBracelet(braceletId, pageable);
	}

	public Alarm findByLocationId(Long locationId) throws AlarmNotFoundException {
		Optional<Alarm> register = alarmRepository.findByLocation(locationId);

		if (register.isEmpty())
			throw new AlarmNotFoundException(
					String.format("Não foi encontrado alarmes para a location de identificador %d", locationId));

		return register.get();
	}

	public Alarm alarmSeen(Long idAlarm) throws AlarmNotFoundException {
		Optional<Alarm> register = alarmRepository.findById(idAlarm);

		if (register.isEmpty()) {
			throw new AlarmNotFoundException(idAlarm);
		}
		Alarm alarm = register.get();
		alarm.setSeen(true);

		return alarmRepository.save(alarm);
	}

	public Alarm findAlarmById(Long idAlarm) throws AlarmNotFoundException {
		Optional<Alarm> register = alarmRepository.findById(idAlarm);

		if (register.isEmpty())
			throw new AlarmNotFoundException(idAlarm);

		return register.get();
	}

	public void deleteAlarm(Long idAlarm) throws AlarmNotFoundException {
		if (!alarmRepository.existsById(idAlarm))
			throw new AlarmNotFoundException(idAlarm);

		alarmRepository.deleteById(idAlarm);
	}

	public List<Alarm> findFilter(Alarm filter) {
		Example<Alarm> example = Example.of(filter, ExampleMatcher.matching()
				.withIgnoreCase()
				.withStringMatcher(StringMatcher.CONTAINING));
		return alarmRepository.findAll(example);
	}

	public Page<Alarm> findByBracelet(Long braceletId, Long userId, Pageable pageable) {
		return alarmRepository.findByBraceletAndUser(braceletId, userId, pageable);
	}

	public Page<Alarm> findByBracelet(String braceletName, Long userId, Pageable pageable) {
		return alarmRepository.findByUserAndBraceletWithPartOfName(braceletName, userId, pageable);
	}

	public Page<Alarm> findByPeriod(Long userId, LocalDateTime initialDate, LocalDateTime finalDate,
			Pageable pageable) {
		return alarmRepository.findByUserAndCreationDateBetween(initialDate, finalDate, userId, pageable);
	}

	private void notifyUser(Location location) {
		String emailUser = location.getBracelet().getUser().getEmail();
		EmailDto emailDto = new EmailDto();
		String bracelet = location.getBracelet().getName();
		String title = String.format("Alarme de Pulseira disparado");
		emailDto.setSubject(title);
		String message = String.format("A pulseira de %s saiu da cerca %s",
				location.getBracelet().getName(), location.getBracelet().getMonitor().getName());
		emailDto.setText(message);
		emailDto.setTo(emailUser);
		emailService.sendEmail(emailDto);

	/*
		for (DeviceToken device : location.getBracelet().getUser().getDevices()) {
			PushNotificationRequest request = PushNotificationRequest
					.builder()
					.title(String.format("%s saiu da cerca", location.getBracelet().getName()))
					.message(String.format("A pulseira de %s saiu da cerca %s",
							location.getBracelet().getName(),
							location.getBracelet().getMonitor().getName()))
					.topic("Alarme")
					.token(device.getToken())
					.build();
			pushNotificationService.sendPushNotificationToToken(request);
		}
		*/
	}
}
