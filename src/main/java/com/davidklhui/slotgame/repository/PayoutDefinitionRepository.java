package com.davidklhui.slotgame.repository;

import com.davidklhui.slotgame.model.PayoutDefinition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PayoutDefinitionRepository extends JpaRepository<PayoutDefinition, Integer> {
}
