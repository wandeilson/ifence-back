package br.edu.ifpb.dac.groupd.tests.unit;

import java.time.LocalDateTime;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;
import org.springframework.beans.factory.annotation.Autowired;

import br.edu.ifpb.dac.groupd.model.entities.Location;

import static org.junit.jupiter.api.Assertions.*;

@Testable
@DisplayName("Location")
class LocationTests {

	private Location location;
	
	private Set<ConstraintViolation<Location>> violations;
	
	@Autowired
	private static Validator validator;
	
	@BeforeAll
	public static void setUpBeforeAll() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
	}
	@BeforeEach
	public void setUpBeforeEach() {
		location = new Location();
	}
	@Test
	@DisplayName("Null Creation Date")
	void testNullCreationDate() {
		location.setCreationDate(null);
		
		violations = validator.validateProperty(location, "creationDate");
		
		assertNotEquals(0, violations.size(), "Valid Timestamp");
	}
	@Test
	@DisplayName("Future Creation Date")
	void testFutureCreationDate() {
		LocalDateTime date = LocalDateTime.now().plusSeconds(1);
		
		location.setCreationDate(date);
		
		violations = validator.validateProperty(location, "creationDate");
		
		assertNotEquals(0, violations.size(), "Valid Timestamp");
	}
	@Test
	@DisplayName("Valid Creation Date")
	void testValidCreationDate() {
		LocalDateTime date = LocalDateTime.now().minusNanos(1);
		
		location.setCreationDate(date);
		
		violations = validator.validateProperty(location, "creationDate");
		
		assertEquals(0, violations.size(), "Valid Timestamp");
	}

	@Test
	@DisplayName("Coordinate is null")
	void testCoordinateIsNull(){
		assertNull(location.getCoordinate(), "Coordinate cannot be null");
	}

	@Test
	@DisplayName("Bracelet is null")
	void testBraceletIsNull(){
		assertNull(location.getBracelet(), "Bracelet cannot be null");
	}
}
