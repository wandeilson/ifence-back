package br.edu.ifpb.dac.groupd.model.repository.firebase;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.edu.ifpb.dac.groupd.model.entities.DeviceToken;

public interface DeviceTokenRepository extends JpaRepository<DeviceToken, Long> {
    
    @Query(value = "SELECT t FROM DeviceToken t WHERE t.user.id = :userID AND t.token = :token")
	Optional<DeviceToken> findTokenByUserAndToken(@Param("userID") Long userID, @Param("token") String token);
}
