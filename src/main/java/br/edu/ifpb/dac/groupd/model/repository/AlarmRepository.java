package br.edu.ifpb.dac.groupd.model.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import br.edu.ifpb.dac.groupd.model.entities.Fence;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.edu.ifpb.dac.groupd.model.entities.Alarm;

@Repository
public interface AlarmRepository extends JpaRepository<Alarm, Long> {
	List<Alarm> findBySeen(Boolean seen);

	@Query(value="SELECT a FROM Alarm a WHERE a.fence.user.id = :id")
	List<Alarm> findAllAlarmsByUser(@Param("id") Long id);
	
	@Query("SELECT a FROM Alarm a WHERE a.fence.id = :fenceId")
	Page<Alarm> findByFence(@Param("fenceId") Long fenceId, Pageable pageable);
	
	@Query("SELECT a FROM Alarm a WHERE a.location.bracelet.id = :braceletId")
	Page<Alarm> findByBracelet(@Param("braceletId") Long braceletId, Pageable pageable);
	
	@Query("SELECT a FROM Alarm a WHERE a.location.id = :locationId")
	Optional<Alarm> findByLocation(@Param("locationId") Long locationId);

	@Query("SELECT a FROM Alarm a, Location l " +
		"WHERE l.id = a.location.id " +
		"AND a.location.bracelet.id = :braceletId " +
		"AND l.bracelet.user.id = :userId")
	Page<Alarm> findByBraceletAndUser(
		@Param("braceletId") Long braceletId, 
		@Param("userId") Long userId, 
		Pageable pageable
	);

	@Query("SELECT a FROM Alarm a, Location l " +
		"WHERE l.id = a.location.id " +
		"AND LOWER(a.location.bracelet.name) LIKE LOWER(CONCAT('%',:name,'%')) " +
		"AND l.bracelet.user.id = :userId")
	Page<Alarm> findByUserAndBraceletWithPartOfName(
		@Param("name") String name, 
		@Param("userId") Long userId, 
		Pageable pageable
	);

	@Query("SELECT a FROM Alarm a, Location l " +
		"WHERE a.location.id = l.id " +
		"AND a.location.bracelet.user.id = :userId " +
		"AND l.creationDate BETWEEN :startDate AND :endDate"
	)
	Page<Alarm> findByUserAndCreationDateBetween(
		@Param("startDate") LocalDateTime startDate, 
		@Param("endDate") LocalDateTime endDate, 
		@Param("userId") Long userId, 
		Pageable pageable
	);
}
