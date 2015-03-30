package edu.brown.cs.scij.game;

import java.util.Collections;
import java.util.List;

import edu.brown.cs.scij.tile.Tile;

public class Deck {
  private List<Tile> tiles;

  public Deck(List<Tile> tiles) {
    this.tiles = tiles;
  }

  public Tile drawTile() {
    if (isEmpty()) {
      return null;
      // TODO should this throw a null pointer excpetion?
      // should it be the caller's job to check null?
    }
    return tiles.get(0);
  }

  public boolean isEmpty() {
    return tiles.isEmpty();
  }

  public Deck shuffle() {
    Collections.shuffle(tiles);
    return this;
  }
}
