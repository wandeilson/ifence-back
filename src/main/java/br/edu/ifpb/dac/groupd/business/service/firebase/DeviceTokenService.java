package br.edu.ifpb.dac.groupd.business.service.firebase;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ifpb.dac.groupd.model.entities.DeviceToken;
import br.edu.ifpb.dac.groupd.model.entities.User;
import br.edu.ifpb.dac.groupd.model.repository.firebase.DeviceTokenRepository;

@Service
public class DeviceTokenService {
    @Autowired
    DeviceTokenRepository repository;

    public String registerToken(String token, User user) {
        if (token == null) {
            return null;
        }
    
        Optional<DeviceToken> deviceToken = repository.findTokenByUserAndToken(user.getId(), token);
        
        return deviceToken.isPresent() ? 
            deviceToken.get().getToken() : 
            repository.save(new DeviceToken(null, token, user)).getToken();
    

    }
}
