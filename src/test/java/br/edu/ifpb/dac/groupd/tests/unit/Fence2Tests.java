package br.edu.ifpb.dac.groupd.tests.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalTime;

import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;

import br.edu.ifpb.dac.groupd.business.exception.FenceEmptyException;
import br.edu.ifpb.dac.groupd.business.exception.NoBraceletAvailableException;
import br.edu.ifpb.dac.groupd.business.service.FenceService;
import br.edu.ifpb.dac.groupd.model.entities.Coordinate;
import br.edu.ifpb.dac.groupd.model.entities.Fence;

public class Fence2Tests {

    static Fence fence = new Fence();

    @BeforeAll
    public static void initThis() {
        
    }

    // Validations
    @BeforeEach
    public static boolean validationNameHaveNumbers(String name) {
        String regex = ".*\\d+.*";
        return name.matches(regex);

    }

    public static boolean setCoordinates(Coordinate coordinate) {
        if (coordinate.equals(fence.getCoordinate())) {
            return false;
        }
        return true;
    }

    public static boolean validationCoordinate(Coordinate coordinate) {
        if (coordinate.getLatitude() < -90 || coordinate.getLatitude() > 90) {
            return false;
        }
        if (coordinate.getLongitude() < -180 || coordinate.getLatitude() > 180) {
            return false;
        }
        return true;
    }

    @Test
    @Order(1)
    public void someTests() {
        // Testar o nome da cerca com Letras e numeros
        fence.setName("Izabel123"); 
        assertEquals(validationNameHaveNumbers("Izabel123"), true);

        // Testar o nome da cerca com apenas letras

        fence.setName("Izabel"); 
        assertEquals(validationNameHaveNumbers("Izabel"), fence.getName().equals(true));

        // Testar se a cerca está ativa
        try {
            fence.setActive(true);
            assertTrue(fence.isActive());
        } catch (FenceEmptyException | NoBraceletAvailableException e) {
            e.printStackTrace();
        }

        // Testa se a coordenada é nula
        assertTrue("true", setCoordinates(new Coordinate(null, 190.5)));

        // Testa se os valores das coordenadas são inválidos
        assertFalse("false", validationCoordinate(new Coordinate(-500.5, 170.5)));
    }

    @Test
    // Testa se a fence foi criada corretamente
    public void testIfFenceCreated() {
        fence.setCoordinate(new Coordinate(45.5, 150.5));
        fence.setStartTime(LocalTime.of(22, 40, 30));
        fence.setFinishTime(LocalTime.of(23, 40, 30));
        fence.setRadius(25.50);
        assertNotNull("Ok", fence);
    }

}
