package com.davidklhui.slotgame.repository;

import com.davidklhui.slotgame.model.Symbol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SymbolRepository extends JpaRepository<Symbol, Integer> {
}
