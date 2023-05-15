package br.edu.ifpb.dac.groupd.tests.integration;

import br.edu.ifpb.dac.groupd.business.exception.BraceletNotFoundException;
import br.edu.ifpb.dac.groupd.business.exception.LocationCreationDateInFutureException;
import br.edu.ifpb.dac.groupd.business.exception.UserNotFoundException;
import br.edu.ifpb.dac.groupd.business.service.LocationService;
import br.edu.ifpb.dac.groupd.business.service.interfaces.PasswordEncoderService;
import br.edu.ifpb.dac.groupd.model.entities.User;
import br.edu.ifpb.dac.groupd.model.repository.UserRepository;
import br.edu.ifpb.dac.groupd.presentation.dto.LocationRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class LocationServiceTests {
    @Autowired
    private LocationService locationService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoderService passEncoder;


    @Test
    @DisplayName("Criando location com usuario não existente")
    void createLocationWithoutUser(){
        LocationRequest locationRequest = new LocationRequest();
        assertThrows(UserNotFoundException.class, ()-> locationService.create(22L,locationRequest));
    }
    @Test
    @DisplayName("Criando location com usuario sem Bracelet")
    void createLocationWithUserWithoutBracelet(){
        LocationRequest locationRequest = new LocationRequest();
        User user = new User();
        user.setName("Usuario");
        user.setEmail("userteste22@email.com");
        user.setPassword(passEncoder.encode("minhaSenha@2023"));
        assertThrows(BraceletNotFoundException.class, ()-> locationService.create(userRepository.save(user).getId(), locationRequest));
    }

    @Test
    @DisplayName("Criando ")
    void teste3(){
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setCreationDate(LocalDateTime.now().plusSeconds(200));
        locationRequest.setBraceletId(59L);
        User user = new User();
        user.setName("Usuarioo");
        user.setEmail("userteste2222@email.com");
        user.setPassword(passEncoder.encode("minhaSenha@2023"));
        assertThrows(LocationCreationDateInFutureException.class, ()-> locationService.create(userRepository.save(user).getId(), locationRequest));
    }
}
