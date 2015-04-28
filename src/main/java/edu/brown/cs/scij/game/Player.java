package edu.brown.cs.scij.game;

import edu.brown.cs.scij.tile.OutOfMeeplesException;

public class Player {
  private Color playerColor;
  private final String id;
  private final String name;
  private int score;
  private int numMeeples;

  public Player(String id, String name) {
    this.id = id;
    this.name = name;
    this.score = 0;
    this.numMeeples = 7;
  }

  public Color getPlayerColor() {
    return playerColor;
  }

  public void setPlayerColor(Color playerColor) {
    this.playerColor = playerColor;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public int getScore() {
    return score;
  }

  public void setScore(int score) {
    if (score < 0) {
      throw new IllegalArgumentException("score cannot go below 0");
    }
    this.score = score;
  }

  public void addScore(int score) {
    if (score < 0) {
      throw new IllegalArgumentException("can't get negative points");
    }
    this.score = this.score + score;
  }

  public int getNumMeeples() {
    return numMeeples;
  }

  public void setNumMeeples(int numMeeples) {
    this.numMeeples = numMeeples;
  }

  public void useMeeple() throws OutOfMeeplesException {
    if (numMeeples == 0) {
      throw new OutOfMeeplesException("No meeples left!");
    }
    numMeeples--;
  }

  public void returnMeeple() {
    if (numMeeples != 7) {
      numMeeples++;
    }
  }

  @Override
  public String toString() {
    return name + " :: Score: " + score + " Meeples Left: " + numMeeples;
  }
}
