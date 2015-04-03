package edu.brown.cs.scij.game;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableMap;

import edu.brown.cs.scij.tile.Tile;

public class Board {
  private Map<Posn, Tile> board;
  private Set<Posn> adjacentPosns;

  public Board() {
    this.board = new HashMap<>();
    this.adjacentPosns = new HashSet<>();
  }

  public Map<Posn, Tile> getBoard() {
    return ImmutableMap.copyOf(board);
  }

  public Board place(Posn p, Tile t) {
    Tile there = board.get(p);
    if (there == null) {
      board.put(p, t);
      adjacentPosns.remove(p);
      Posn up = new Posn(p.getX(), p.getY() + 1); // TODO should this be -1?
      Posn right = new Posn(p.getX() + 1, p.getY());
      Posn down = new Posn(p.getX(), p.getY() - 1);
      Posn left = new Posn(p.getX() - 1, p.getY());

      if (!adjacentPosns.contains(up)) {
        adjacentPosns.add(up);
      }
      if (!adjacentPosns.contains(right)) {
        adjacentPosns.add(right);
      }
      if (!adjacentPosns.contains(down)) {
        adjacentPosns.add(down);
      }
      if (!adjacentPosns.contains(left)) {
        adjacentPosns.add(left);
      }
    } else {
      throw new IllegalArgumentException("There is already a tile here.");
    }
    // TODO should this be void or do we want to return the board?
    return this;
  }

  public List<Posn> validMoves(Tile tile) {
    // TODO
    for (Posn p : adjacentPosns) {

    }
    return null;
  }
}
