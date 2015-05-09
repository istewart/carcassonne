package edu.brown.cs.scij.game;

/**
 * A spectator is just someone who has an ID and a name and is not part of the
 * game.
 * @author szellers
 *
 */
public class Spectator {
  private int id;
  private String name;

  /**
   * Creates a new Spectator with the given id and name.
   * @param id the id of the spectator
   * @param name the name of the spectator
   */
  public Spectator(int id, String name) {
    this.id = id;
    this.name = name;
  }

  /**
   * Gets the ID of the Spectator.
   * @return the ID of the Spectator
   */
  public int getId() {
    return id;
  }

  /**
   * Sets the id of the spectator to id.
   * @param id the id to set this spectator to
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * Gets the name of the spectator.
   * @return the name of the spectator
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the name of the spectator to name.
   * @param name the name to set this spectator with.
   */
  public void setName(String name) {
    this.name = name;
  }

}
