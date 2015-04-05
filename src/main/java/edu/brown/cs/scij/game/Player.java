package edu.brown.cs.scij.game;

public class Player {
  private Color playerColor;
  private final int id;
  private final String name;
  private int score;
  private int numMeeples;

  public Player(int id, String name) {
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

  public int getId() {
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
    /*TODO if (this.score + score)*/
    this.score = this.score + score;
  }

  public int getNumMeeples() {
    return numMeeples;
  }

  public void setNumMeeples(int numMeeples) {
    this.numMeeples = numMeeples;
  }

  public void useMeeple() {
    numMeeples--;
  }

  public void returnMeeple() {
    numMeeples++;
  }
}
