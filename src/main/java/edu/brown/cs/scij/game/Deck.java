package edu.brown.cs.scij.game;

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
      // TODO should this throw a null pointer exception?
      // should it be the caller's job to check null?
    }
    return tiles.remove(0);
  }

  public boolean isEmpty() {
    return tiles.isEmpty();
  }

  // Shouldn't need this, done in Referee
  /*public Deck shuffle() {
    Collections.shuffle(tiles);
    return this;
  }*/

  /*public static void main(String[] args) {
    List<String> list = new ArrayList<>();
    list.add("hey");
    System.out.println(list.remove(0));
    System.out.println(list.remove(0));
  }*/
}
