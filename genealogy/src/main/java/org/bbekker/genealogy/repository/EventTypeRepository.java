package org.bbekker.genealogy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EventTypeRepository extends JpaRepository<EventType, String> {

	List<EventType> findByCategory(String category);

	EventType findByQualifier(String qualifier);

}
