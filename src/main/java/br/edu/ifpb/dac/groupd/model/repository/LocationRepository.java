package br.edu.ifpb.dac.groupd.model.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.edu.ifpb.dac.groupd.model.entities.Bracelet;
import br.edu.ifpb.dac.groupd.model.entities.Location;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
	List<Location> findByBracelet(Bracelet bracelet);
	
	@Query("SELECT l FROM Location l WHERE l.bracelet.id = :braceletId")
	Page<Location> findByBraceletId(@Param("braceletId") Long id, Pageable pageable);	
	
	@Query(
		"SELECT l FROM Location l, Bracelet b " + 
		"WHERE l.bracelet.id = b.id " + 
		"AND b.user.id = :userId " +
		"AND l.creationDate BETWEEN :startDate AND :endDate"
	)
	Page<Location> findByUserAndCreationDateBetween(
		@Param("startDate") LocalDateTime startDate, 
		@Param("endDate") LocalDateTime endDate, 
		@Param("userId") Long userId, 
		Pageable pageable
	);

	@Query("SELECT l FROM Location l, Bracelet b " +
		"WHERE l.bracelet.id = b.id " + 
		"AND LOWER(b.name) LIKE LOWER(CONCAT('%',:name,'%')) " +
		"AND b.user.id = :userId")
	Page<Location> findByUserAndBraceletWithPartOfName(
		@Param("name") String name, 
		@Param("userId") Long userId, 
		Pageable pageable
	);
}
