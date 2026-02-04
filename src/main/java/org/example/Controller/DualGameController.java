package org.example.Controller;

import org.example.Model.GameStateSnapshot;
import org.example.Model.TriPeaksGame;

public class DualGameController {
    private final String solverPath;
    private GameStateSnapshot initialState;

    private TriPeaksGame playerGame;
    private TriPeaksGame aiGame;

    private int playerMoves = 0;
    private int aiMoves = 0;

    private boolean playerFinished = false;
    private boolean aiFinished = false;

    public DualGameController(String solverPath){
        this.solverPath = solverPath;
    }

    public void iniziaPartita() throws Exception{
        TriPeaksGame baseGame = new TriPeaksGame(solverPath);
        baseGame.inizia();

        initialState = GameStateSnapshot.cattura(baseGame);
        playerGame = GameStateSnapshot.ripristina(initialState, solverPath);
        aiGame = GameStateSnapshot.ripristina(initialState,solverPath);

        playerMoves = 0;
        aiMoves = 0;

        System.out.println("✅ Partita duello inizializzata!");
    }

    public TriPeaksGame getPlayerGame() {
        return playerGame;
    }

    public TriPeaksGame getAiGame() {
        return aiGame;
    }

    public int getPlayerMoves() {
        return playerMoves;
    }

    public int getAiMoves() {
        return aiMoves;
    }

    public void incrementPlayerMoves() {
        playerMoves++;
    }

    public void incrementAiMoves() {
        aiMoves++;
    }

    public boolean isPlayerFinished() {
        return playerFinished;
    }

    public void setPlayerFinished(boolean playerFinished) {
        this.playerFinished = playerFinished;
    }

    public boolean isAiFinished() {
        return aiFinished;
    }

    public void setAiFinished(boolean aiFinished) {
        this.aiFinished = aiFinished;
    }
}
