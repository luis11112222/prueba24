package com.example.mysqlpersistencia.democonmysq.model.repository;

import com.example.mysqlpersistencia.democonmysq.model.domain.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long> {
}
