package com.davidklhui.slotgame.repository;

import com.davidklhui.slotgame.model.Payline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaylineRepository extends JpaRepository<Payline, Integer> {
}
