package com.game.service;

import com.game.model.GameRoom;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class GameService {
    private final ConcurrentHashMap<String, GameRoom> games = new ConcurrentHashMap<>();

    public GameRoom createGame() {
        GameRoom gameRoom = new GameRoom();
        games.put(gameRoom.getGameId(), gameRoom);
        return gameRoom;
    }

    public GameRoom getGame(String gameId) {
        return games.get(gameId);
    }

    public void removeGame(String gameId) {
        games.remove(gameId);
    }
} 