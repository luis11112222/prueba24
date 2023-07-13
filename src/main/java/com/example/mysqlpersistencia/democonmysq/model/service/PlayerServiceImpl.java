package com.example.mysqlpersistencia.democonmysq.model.service;

import com.example.mysqlpersistencia.democonmysq.model.domain.Game;
import com.example.mysqlpersistencia.democonmysq.model.domain.Player;
import com.example.mysqlpersistencia.democonmysq.model.dto.GameDTO;
import com.example.mysqlpersistencia.democonmysq.model.dto.PlayerDTO;
import com.example.mysqlpersistencia.democonmysq.model.repository.GameRepository;
import com.example.mysqlpersistencia.democonmysq.model.repository.PlayerRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PlayerServiceImpl implements PlayerService {
    private final PlayerRepository playerRepository;
    private final GameRepository gameRepository;

    public PlayerServiceImpl(PlayerRepository playerRepository, GameRepository gameRepository) {
        this.playerRepository = playerRepository;
        this.gameRepository = gameRepository;
    }

    @Override
    public Player createPlayer(PlayerDTO playerDTO) {
        Player player = new Player();
        player.setName(playerDTO.getName());
        player.setRegistrationDate(new Date());

        return playerRepository.save(player);
    }
    @Override
    public PlayerDTO updatePlayerName(Long playerId, String newName) {
        Player player = getPlayerById(playerId);

        player.setName(newName);

        Player updatedPlayer = playerRepository.save(player);

        return mapPlayerToDTO(updatedPlayer);
    }

    @Override
    @Transactional
    public GameDTO rollDice(Long playerId) {
        Player player = getPlayerById(playerId);

        int dice1 = rollDice();
        int dice2 = rollDice();

        boolean won = (dice1 + dice2) == 7;

        Game game = new Game();
        game.setDice1(dice1);
        game.setDice2(dice2);
        game.setWon(won);
        game.setPlayer(player);

        Game savedGame = gameRepository.save(game);

        return mapGameToDTO(savedGame);
    }

    @Override
    @Transactional
    public void deletePlayerGames(Long playerId) {
        Player player = getPlayerById(playerId);

        player.getGames().clear();
    }
    @Override
    public List<GameDTO> getPlayerGames(Long playerId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new IllegalArgumentException("Player not found with ID: " + playerId));

        List<GameDTO> gameDTOs = new ArrayList<>();
        for (Game game : player.getGames()) {
            GameDTO gameDTO = new GameDTO();
            gameDTO.setId(game.getId());
            gameDTO.setDice1(game.getDice1());
            gameDTO.setDice2(game.getDice2());
            gameDTO.setWon(game.isWon());
            gameDTOs.add(gameDTO);
        }

        return gameDTOs;
    }

    @Override
    public List<PlayerDTO> getAllPlayers() {
        List<Player> players = playerRepository.findAll();

        return players.stream()
                .map(this::mapPlayerToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public double getAverageSuccessPercentage() {
        List<Player> players = playerRepository.findAll();

        double totalSuccessPercentage = players.stream()
                .mapToDouble(this::calculatePlayerSuccessPercentage)
                .sum();

        return totalSuccessPercentage / players.size();
    }

    @Override
    public PlayerDTO getLoserPlayer() {
        List<Player> players = playerRepository.findAll();

        Player loserPlayer = players.stream()
                .min(Comparator.comparingDouble(this::calculatePlayerSuccessPercentage))
                .orElse(null);

        return mapPlayerToDTO(loserPlayer);
    }

    @Override
    public PlayerDTO getWinnerPlayer() {
        List<Player> players = playerRepository.findAll();

        Player winnerPlayer = players.stream()
                .max(Comparator.comparingDouble(this::calculatePlayerSuccessPercentage))
                .orElse(null);

        return mapPlayerToDTO(winnerPlayer);
    }

    private Player getPlayerById(Long playerId) {
        return playerRepository.findById(playerId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid player ID"));
    }

    private int rollDice() {
        Random random = new Random();
        return random.nextInt(6) + 1;
    }

    private double calculatePlayerSuccessPercentage(Player player) {
        List<Game> games = player.getGames();

        if (games.isEmpty()) {
            return 0.0;
        }

        long wonGamesCount = games.stream()
                .filter(Game::isWon)
                .count();

        return (double) wonGamesCount / games.size() * 100;
    }

    private PlayerDTO mapPlayerToDTO(Player player) {
        PlayerDTO playerDTO = new PlayerDTO();
        playerDTO.setId(player.getId());
        playerDTO.setName(player.getName());
        playerDTO.setSuccessPercentage(calculatePlayerSuccessPercentage(player));
        return playerDTO;
    }

    private GameDTO mapGameToDTO(Game game) {
        GameDTO gameDTO = new GameDTO();
        gameDTO.setId(game.getId());
        gameDTO.setDice1(game.getDice1());
        gameDTO.setDice2(game.getDice2());
        gameDTO.setWon(game.isWon());
        return gameDTO;
    }
}