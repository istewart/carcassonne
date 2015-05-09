package edu.brown.cs.scij.game;

import edu.brown.cs.scij.tile.OutOfMeeplesException;

/**
 * A carcassonne player.
 * @author szellers
 *
 */
public class Player {
  private final int id;
  private final String name;
  private int score;
  private int numMeeples;

  /**
   * Constructor for player.
   * @param id the players id
   * @param name the players name
   */
  public Player(int id, String name) {
    this.id = id;
    this.name = name;
    this.score = 0;
    this.numMeeples = 7;
  }

  /**
   * getter for id.
   * @return id
   */
  public int getId() {
    return id;
  }

  /**
   * getter for name.
   * @return name
   */
  public String getName() {
    return name;
  }

  /**
   * getter for score.
   * @return score
   */
  public int getScore() {
    return score;
  }

  /**
   * setter for score.
   * @param score what to set score to
   */
  public void setScore(int score) {
    if (score < 0) {
      throw new IllegalArgumentException("score cannot go below 0");
    }
    this.score = score;
  }

  /**
   * adds input score to previous score.
   * @param score what to add to score.
   */
  public void addScore(int score) {
    if (score < 0) {
      throw new IllegalArgumentException("can't get negative points");
    }
    this.score = this.score + score;
  }

  /**
   * getter for numMeeples.
   * @return numMeeples
   */
  public int getNumMeeples() {
    return numMeeples;
  }

  /**
   * setter for numMeeples.
   * @param numMeeples what to set numMeeples to.
   */
  public void setNumMeeples(int numMeeples) {
    this.numMeeples = numMeeples;
  }

  /**
   * uses a meeple, subtracting from available.
   * @throws OutOfMeeplesException if there are no more meeples.
   */
  public void useMeeple() throws OutOfMeeplesException {
    if (numMeeples == 0) {
      throw new OutOfMeeplesException("No meeples left!");
    }
    numMeeples--;
  }

  /**
   * returns a meeple, adding to available.
   */
  public void returnMeeple() {
    if (numMeeples != 7) {
      numMeeples++;
    }
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + id;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof Player)) {
      return false;
    }
    Player other = (Player) obj;
    if (id != other.id) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return name + " :: Score: " + score + " Meeples Left: " + numMeeples;
  }
}
