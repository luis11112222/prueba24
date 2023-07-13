package com.example.mysqlpersistencia.democonmysq.model.repository;

import com.example.mysqlpersistencia.democonmysq.model.domain.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
}
