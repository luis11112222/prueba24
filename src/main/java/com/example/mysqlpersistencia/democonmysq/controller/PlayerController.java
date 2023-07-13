package com.example.mysqlpersistencia.democonmysq.controller;

import com.example.mysqlpersistencia.democonmysq.model.domain.Game;
import com.example.mysqlpersistencia.democonmysq.model.domain.Player;
import com.example.mysqlpersistencia.democonmysq.model.dto.GameDTO;
import com.example.mysqlpersistencia.democonmysq.model.dto.PlayerDTO;
import com.example.mysqlpersistencia.democonmysq.model.service.PlayerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/players")
public class PlayerController {
    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @PostMapping
    public ResponseEntity<Player> createPlayer(@RequestBody PlayerDTO playerDTO) {
        Player player = playerService.createPlayer(playerDTO);
        return new ResponseEntity<>(player, HttpStatus.CREATED);
    }

    @PutMapping("/{playerId}")
    public ResponseEntity<PlayerDTO> updatePlayerName(@PathVariable Long playerId, @RequestBody String newName) {
        PlayerDTO updatedPlayer = playerService.updatePlayerName(playerId, newName);
        return new ResponseEntity<>(updatedPlayer, HttpStatus.OK);
    }


    @PostMapping("/{playerId}/games")
    public ResponseEntity<GameDTO> rollDice(@PathVariable Long playerId) {
        GameDTO game = playerService.rollDice(playerId);
        return new ResponseEntity<>(game, HttpStatus.OK);
    }

    @DeleteMapping("/{playerId}/games")
    public ResponseEntity<Void> deletePlayerGames(@PathVariable Long playerId) {
        playerService.deletePlayerGames(playerId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @GetMapping("/{playerId}/games")
    public ResponseEntity<List<GameDTO>> getPlayerGames(@PathVariable Long playerId) {
        List<GameDTO> games = playerService.getPlayerGames(playerId);
        return new ResponseEntity<>(games, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<PlayerDTO>> getAllPlayers() {
        List<PlayerDTO> players = playerService.getAllPlayers();
        return new ResponseEntity<>(players, HttpStatus.OK);
    }

    @GetMapping("/ranking")
    public ResponseEntity<Double> getAverageSuccessPercentage() {
        double averagePercentage = playerService.getAverageSuccessPercentage();
        return new ResponseEntity<>(averagePercentage, HttpStatus.OK);
    }

    @GetMapping("/ranking/loser")
    public ResponseEntity<PlayerDTO> getLoserPlayer() {
        PlayerDTO loserPlayer = playerService.getLoserPlayer();
        return new ResponseEntity<>(loserPlayer, HttpStatus.OK);
    }

    @GetMapping("/ranking/winner")
    public ResponseEntity<PlayerDTO> getWinnerPlayer() {
        PlayerDTO winnerPlayer = playerService.getWinnerPlayer();
        return new ResponseEntity<>(winnerPlayer, HttpStatus.OK);
    }
}
