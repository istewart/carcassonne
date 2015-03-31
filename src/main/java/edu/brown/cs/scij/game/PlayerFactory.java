package edu.brown.cs.scij.game;

public class PlayerFactory {

  public PlayerFactory() {

  }

  public Player newPlayer(int id, String name) {
    return new Player(id, name);
  }
}
