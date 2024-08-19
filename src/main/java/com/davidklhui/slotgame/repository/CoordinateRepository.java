package com.davidklhui.slotgame.repository;

import com.davidklhui.slotgame.model.Coordinate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CoordinateRepository extends JpaRepository<Coordinate, Integer> {
}
