package edu.brown.cs.scij.game;

import java.util.List;

import edu.brown.cs.scij.tile.Tile;

/**
 * A Deck is a List of Tiles from which Tiles can be drawn to play a game of
 * Carcassonne.
 * @author szellers
 *
 */
public class Deck {
  private List<Tile> tiles;

  /**
   * Creates a deck out of the given list of tiles.
   * @param tiles the tiles of which to create the deck
   */
  public Deck(List<Tile> tiles) {
    this.tiles = tiles;
  }

  /**
   * Draws the top card of the deck. If there are no more tiles in the deck,
   * throws EmptyDeckException.
   * @return the top tile of the deck. If the deck is empty, throws
   *         EmptyDeckException
   * // @throws EmptyDeckException if called on an empty deck
   */
  public Tile drawTile() /* throws EmptyDeckException */{
    if (isEmpty()) {
      return null/* throw new EmptyDeckException() */;
    }
    return tiles.remove(0);
  }

  /**
   * Checks if the deck is empty.
   * @return true if there are no more tiles left in the deck, else false
   */
  public boolean isEmpty() {
    return tiles.isEmpty();
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("Deck:\n");
    for (Tile tile : tiles) {
      sb.append(tile.toString() + "\n");
    }
    return sb.toString();
  }

  /**
   * Gets the deck as a list of tiles.
   * @return the list of tiles
   */
  public List<Tile> getTiles() {
    return tiles;
  }

  /**
   * Sets the tiles as the given list.
   * @param tiles the list with which to make the new deck.
   */
  public void setTiles(List<Tile> tiles) {
    this.tiles = tiles;
  }

}
