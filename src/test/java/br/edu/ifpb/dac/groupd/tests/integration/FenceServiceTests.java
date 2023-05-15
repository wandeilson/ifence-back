package br.edu.ifpb.dac.groupd.tests.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.security.Principal;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.test.context.SpringBootTest;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import br.edu.ifpb.dac.groupd.business.exception.FenceEmptyException;
import br.edu.ifpb.dac.groupd.business.exception.FenceNotFoundException;
import br.edu.ifpb.dac.groupd.business.exception.NoBraceletAvailableException;
import br.edu.ifpb.dac.groupd.business.exception.UserNotFoundException;
import br.edu.ifpb.dac.groupd.business.service.FenceService;

import br.edu.ifpb.dac.groupd.business.service.converter.FenceConverterService;
import br.edu.ifpb.dac.groupd.model.entities.Bracelet;
import br.edu.ifpb.dac.groupd.model.entities.Coordinate;
import br.edu.ifpb.dac.groupd.model.entities.Fence;

import br.edu.ifpb.dac.groupd.model.entities.User;
import br.edu.ifpb.dac.groupd.model.repository.FenceRepository;
import br.edu.ifpb.dac.groupd.presentation.controller.FenceResource;
import br.edu.ifpb.dac.groupd.presentation.dto.FenceRequest;
import br.edu.ifpb.dac.groupd.presentation.dto.FenceResponse;

@SpringBootTest
public class FenceServiceTests {

    @Autowired
    private Fence fence;

    @Autowired
    private FenceRequest fenceRequest;

    @Autowired
    private FenceResource controller;

    @Autowired
    private FenceConverterService converter;

    @Autowired
    private FenceRepository repository;
    
    @Autowired
    private FenceService service;

    private FenceRequest fenceDto;

    private Principal principal;

    @Autowired
    private User user;

    @BeforeEach
    public void init() throws FenceEmptyException, NoBraceletAvailableException{
        LocalTime now = LocalTime.now();
        Set<Bracelet> bracelets = new HashSet<Bracelet>();
		
		fenceDto.setStartTime(now.plusMinutes(1l));
		fenceDto.setFinishTime(now);

        fence = new Fence();
        fence.setName("Cerca");
        fence.setCoordinate(new Coordinate(-90.0, -180.0));
        fence.setStartTime(now);
        fence.setFinishTime(now.plusHours(2l).plusMinutes(30l));
        fence.setRadius(1.0);
        fence.setActive(true);
        fence.setUser(user);

        user = new User();
        user.setName("Izabel");
        user.setEmail("izabel.vieira@gmail.com");
        user.setPassword("1237693bel");
        user.setBracelets(bracelets);

    }
    
    @Test
    @Order(1)
    public void DBTest() throws UserNotFoundException, FenceNotFoundException {
        
        Fence savedFence = repository.save(fence);
        
        assertEquals(savedFence.getName(), fence.getName());

        Fence DBFence = service.findFenceById(user.getId(), savedFence.getId());
        
        assertAll(
            () -> assertEquals(savedFence.getName(), DBFence.getName()),
            () -> assertEquals(savedFence.getId(), DBFence.getId()),
            () -> assertEquals(savedFence.getCoordinate(), DBFence.getCoordinate()),
            () -> assertNull(DBFence.getUser())
        );
    }

    @Test
    @Order(2)
    public void update() throws UserNotFoundException, FenceNotFoundException{
        
        FenceResponse fenceDto = converter.fenceToResponse(fence);
        fenceDto.setName("Izabel V.");
        
        ResponseEntity response = controller.updateFence(principal, fence.getId(), fenceRequest);

        assertEquals(200, response.getStatusCodeValue());

        Fence DBFence = service.findFenceById(user.getId(), fence.getId());

        assertEquals(fenceDto.getName(), DBFence.getName());
    }
}