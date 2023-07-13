package com.example.mysqlpersistencia.democonmysq.model.service;


import com.example.mysqlpersistencia.democonmysq.model.domain.Game;
import com.example.mysqlpersistencia.democonmysq.model.domain.Player;
import com.example.mysqlpersistencia.democonmysq.model.dto.GameDTO;
import com.example.mysqlpersistencia.democonmysq.model.dto.PlayerDTO;

import java.util.List;

public interface PlayerService {
    Player createPlayer(PlayerDTO playerDTO);

    PlayerDTO updatePlayerName(Long playerId, String newName);

    GameDTO rollDice(Long playerId);

    void deletePlayerGames(Long playerId);

    List<PlayerDTO> getAllPlayers();
    List<GameDTO> getPlayerGames(Long playerId);

    double getAverageSuccessPercentage();

    PlayerDTO getLoserPlayer();

    PlayerDTO getWinnerPlayer();
}
