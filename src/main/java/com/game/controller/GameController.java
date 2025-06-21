package com.game.controller;

import com.game.model.GameMove;
import com.game.model.GameRoom;
import com.game.model.GameState;
import com.game.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
public class GameController {

    @Autowired
    private GameService gameService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/game/{gameId}")
    public String game(@PathVariable String gameId, Model model) {
        // Convert game ID to uppercase for consistency
        gameId = gameId.toUpperCase();
        GameRoom gameRoom = gameService.getGame(gameId);
        if (gameRoom != null) {
            model.addAttribute("gameId", gameId);
            model.addAttribute("gameRoom", gameRoom);
            return "game";
        }
        return "redirect:/";
    }

    @PostMapping("/create")
    @ResponseBody
    public Map<String, String> createGame(@RequestParam String playerName) {
        GameRoom gameRoom = gameService.createGame();
        gameRoom.setPlayer1(playerName);
        
        Map<String, String> response = new HashMap<>();
        response.put("gameId", gameRoom.getGameId());
        response.put("playerId", gameRoom.getPlayer1().getId());
        
        // Broadcast game state immediately
        broadcastGameState(gameRoom);
        
        return response;
    }

    @PostMapping("/join/{gameId}")
    @ResponseBody
    public ResponseEntity<?> joinGame(@PathVariable String gameId, @RequestParam String playerName) {
        // Convert game ID to uppercase for consistency
        gameId = gameId.toUpperCase();
        GameRoom gameRoom = gameService.getGame(gameId);
        
        if (gameRoom == null) {
            return ResponseEntity.badRequest().body("Game not found");
        }
        
        if (gameRoom.getPlayer2() != null) {
            return ResponseEntity.badRequest().body("Game is already full");
        }
        
        gameRoom.setPlayer2(playerName);
        
        Map<String, String> response = new HashMap<>();
        response.put("gameId", gameId);
        response.put("playerId", gameRoom.getPlayer2().getId());
        
        // Broadcast game state immediately
        broadcastGameState(gameRoom);
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/app/move")
    @ResponseBody
    public ResponseEntity<?> makeMove(@RequestBody GameMove move) {
        try {
            // Convert game ID to uppercase for consistency
            move.setGameId(move.getGameId().toUpperCase());
            GameRoom gameRoom = gameService.getGame(move.getGameId());
            
            System.out.println("Move request - Game ID: " + move.getGameId() + ", Player ID: " + move.getPlayerId() + ", Position: " + move.getPosition());
            
            if (gameRoom == null) {
                System.out.println("Game not found: " + move.getGameId());
                return ResponseEntity.badRequest().body("Game not found");
            }
            
            if (gameRoom.getPlayer2() == null) {
                System.out.println("Player 2 not joined yet");
                return ResponseEntity.badRequest().body("Waiting for opponent to join");
            }
            
            if (!gameRoom.isGameStarted()) {
                System.out.println("Game not started yet");
                return ResponseEntity.badRequest().body("Game not started");
            }
            
            if (!gameRoom.isPlayerTurn(move.getPlayerId())) {
                System.out.println("Not player's turn - Current turn: " + (gameRoom.getGameState().isXNext() ? "X" : "O"));
                return ResponseEntity.badRequest().body("Not your turn");
            }
            
            GameState gameState = gameRoom.getGameState();
            if (gameState.makeMove(move.getPosition())) {
                System.out.println("Move successful at position: " + move.getPosition());
                // Broadcast updated game state to all players
                broadcastGameState(gameRoom);
                return ResponseEntity.ok(gameState);
            }
            
            System.out.println("Invalid move at position: " + move.getPosition());
            return ResponseEntity.badRequest().body("Invalid move");
        } catch (Exception e) {
            System.err.println("Error making move: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error making move: " + e.getMessage());
        }
    }

    @PostMapping("/app/reset")
    public ResponseEntity<String> resetGame(@RequestBody GameMove request) {
        GameRoom gameRoom = gameService.getGame(request.getGameId());
        
        if (gameRoom == null) {
            return ResponseEntity.badRequest().body("Game not found");
        }
        
        // Only allow reset if game is over
        if (gameRoom.getGameState().getWinner() == null) {
            return ResponseEntity.badRequest().body("Game is still in progress");
        }
        
        // Reset the game state
        gameRoom.resetGame();
        
        // Notify all players about the reset
        messagingTemplate.convertAndSend("/topic/game/" + request.getGameId(), gameRoom);
        
        return ResponseEntity.ok("Game reset successfully");
    }

    @GetMapping("/game/{gameId}/state")
    @ResponseBody
    public ResponseEntity<?> getGameState(@PathVariable String gameId) {
        // Convert game ID to uppercase for consistency
        gameId = gameId.toUpperCase();
        GameRoom gameRoom = gameService.getGame(gameId);
        
        if (gameRoom == null) {
            return ResponseEntity.badRequest().body("Game not found");
        }
        
        return ResponseEntity.ok(gameRoom);
    }

    @PostMapping("/start/{gameId}")
    @ResponseBody
    public ResponseEntity<?> startGame(@PathVariable String gameId, @RequestParam String playerId) {
        try {
            // Convert game ID to uppercase for consistency
            gameId = gameId.toUpperCase();
            GameRoom gameRoom = gameService.getGame(gameId);
            
            if (gameRoom == null) {
                return ResponseEntity.badRequest().body("Game not found");
            }
            
            // Check if both players have joined
            if (!gameRoom.areBothPlayersJoined()) {
                return ResponseEntity.badRequest().body("Waiting for both players to join");
            }
            
            // Validate the player making the request
            if (!isValidPlayer(gameRoom, playerId)) {
                return ResponseEntity.badRequest().body("Invalid player");
            }
            
            // Set the starting player based on who clicked the play button
            boolean isPlayer1Starting = playerId.equals(gameRoom.getPlayer1().getId());
            gameRoom.startGame(isPlayer1Starting);
            
            // Create response
            Map<String, Object> response = new HashMap<>();
            response.put("gameStarted", true);
            response.put("currentPlayer", isPlayer1Starting ? gameRoom.getPlayer1().getName() : gameRoom.getPlayer2().getName());
            response.put("gameState", gameRoom.getGameState());
            
            // Log game start details
            System.out.println("Starting game: " + gameId);
            System.out.println("Game started status: " + gameRoom.isGameStarted());
            System.out.println("Starting player: " + (isPlayer1Starting ? "Player 1" : "Player 2"));
            System.out.println("Current player: " + (isPlayer1Starting ? gameRoom.getPlayer1().getName() : gameRoom.getPlayer2().getName()));
            
            // Broadcast updated game state
            broadcastGameState(gameRoom);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("Error starting game: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error starting game: " + e.getMessage());
        }
    }

    private boolean isValidPlayer(GameRoom gameRoom, String playerId) {
        if (gameRoom.getPlayer1() != null && playerId.equals(gameRoom.getPlayer1().getId())) {
            return true;
        }
        if (gameRoom.getPlayer2() != null && playerId.equals(gameRoom.getPlayer2().getId())) {
            return true;
        }
        return false;
    }

    private void broadcastGameState(GameRoom gameRoom) {
        try {
            System.out.println("Broadcasting game state: " + gameRoom.getGameId());
            System.out.println("Player1: " + (gameRoom.getPlayer1() != null ? gameRoom.getPlayer1().getName() : "null"));
            System.out.println("Player2: " + (gameRoom.getPlayer2() != null ? gameRoom.getPlayer2().getName() : "null"));
            System.out.println("Game started: " + gameRoom.isGameStarted());
            System.out.println("Both players joined: " + gameRoom.areBothPlayersJoined());
            
            messagingTemplate.convertAndSend(
                "/topic/game/" + gameRoom.getGameId(), 
                gameRoom
            );
        } catch (Exception e) {
            System.err.println("Error broadcasting game state: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 