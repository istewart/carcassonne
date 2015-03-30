package edu.brown.cs.scij.game;

import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import edu.brown.cs.scij.tile.Tile;

public class Board {
  private Map<Posn, Tile> board;

  public Board() {
    // TODO
  }

  public Map<Posn, Tile> getBoard() {
    return ImmutableMap.copyOf(board);
  }

  public Board place(Posn p, Tile t) {
    // TODO place into the map. If already taken,
    // what do we do?
    return this;
  }

  public List<Posn> validMoves(Tile tile) {
    // TODO
    return null;
  }

}
