package br.edu.ifpb.dac.groupd.tests.unit;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import br.edu.ifpb.dac.groupd.business.service.AlarmService;
import br.edu.ifpb.dac.groupd.model.entities.Fence;
import br.edu.ifpb.dac.groupd.model.entities.Location;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;
import org.springframework.beans.factory.annotation.Autowired;

import br.edu.ifpb.dac.groupd.model.entities.Alarm;

@Testable
@DisplayName("Alarm")
class AlarmTests {

	private Alarm alarm;
	
	private Set<ConstraintViolation<Alarm>> violations;
	
	@Autowired
	private static Validator validator;
	
	@BeforeAll
	public static void setUpBeforeAll() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        System.out.println("Antes de todos");
	}
	
	@BeforeEach
	public void setUpBeforeEach() {
		alarm = new Alarm();
		System.out.println("antes de cada test");
		System.out.println(alarm.toString());
	}
	
	@Test
	@DisplayName("Fence Not Null")
	void testFenceNotNull() {
		alarm.setFence(null);
		
		violations = validator.validateProperty(alarm, "fence");
		assertNotEquals(0, violations.size(),"Valid not fence");
	}
	
	
	@Test
	@DisplayName("Location Null")
	void testLocationNull() {
		alarm.setLocation(null);
		
		violations = validator.validateProperty(alarm, "location");
		assertEquals(0, violations.size(),"Valid location");
	}

	@Test
	void invalidFence(){
		//testando adicionar uma fence inválida, sem id
		Fence teste = new Fence();
		assertThrows(Exception.class, ()->alarm.setFence(teste));
	}

	@Test
	void usedFence(){
		Fence used = new Fence();
		Alarm first = new Alarm();
		first.setFence(used);
		assertThrows(Exception.class, ()->alarm.setFence(used));
	}


	@Test
	@DisplayName("Location already used by other alarm")
	void usedLocation(){
		Alarm exists = new Alarm();
		Location location = new Location();
		exists.setLocation(location);
		assertThrows(Exception.class, ()-> alarm.setLocation(location));
	}

	//testando settar uma distancia negativa

	@Test
	void negativeDistance(){
		assertThrows(Exception.class, ()-> alarm.setDistance(-12.87));
	}
	//testando setar uma exceeded negativa
	@Test
	void negativeExceeded(){
		assertThrows(Exception.class, ()-> alarm.setExceeded(-12.87));
	}

	@Test
	@DisplayName("trying to save one alarm with invalid location and fence")
	void nullFence(){
		AlarmService alarmService = new AlarmService();
		Location location = new Location();
		Fence fence = new Fence();
		assertEquals(null,alarmService.saveAlarm(location, fence));
		//tentando settar uma fence null para que o if seja executado e retorne null
	}

}
