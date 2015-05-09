package edu.brown.cs.scij.game;

/**
 * A Meeple contains only a reference to the player who owns it.
 * @author szellers
 *
 */
public class Meeple {
  private final Player player;

  /**
   * Creates a new Meeple owned by the given Player.
   * @param player the owner of this Meeple
   */
  public Meeple(Player player) {
    this.player = player;
  }

  /**
   * Gets the player who owns this Meeple.
   * @return the player who ons this Meeple.
   */
  public Player getPlayer() {
    return player;
  }

  @Override
  public String toString() {
    return player.getName();
  }
}
