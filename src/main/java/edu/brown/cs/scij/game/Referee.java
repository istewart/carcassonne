package edu.brown.cs.scij.game;

import java.util.Collections;
import java.util.List;

public class Referee {
  // private PlayerFactory pf;
  private List<Player> players;
  private boolean isGameOver;
  private Deck deck;
  private int turnNumber;

  public Referee() {
    // TODO
    this.turnNumber = 0;
    this.isGameOver = false;
  }

  public void setupGame() {
    // TODO board setup/deck setup
  }

  public void run() {
    // TODO run everything
  }

  public void takeTurn() {
    // TODO take turn
  }

  public void score() {
    // TODO score
  }

  public void shuffleOrder() {
    Collections.shuffle(players);
  }

  public Player nextPlayer() {
    return players.get(turnNumber % 4);
  }
}
