package edu.brown.cs.scij.game;

import java.util.ArrayList;
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

  /**
   * Places Tile t at Posn p on the Board. If the Posn is already taken,
   * PosnTakenExcetption is thrown.
   *
   * @param p The Posn to place the tile
   * @param t The Tile to place at the position
   * @return The Board object, for redrawing
   * @throws PosnTakenException if the Posn is already on the board.
   */
  public Board place(Posn p, Tile t) throws PosnTakenException {
    Tile there = board.get(p);
    if (there == null) {
      board.put(p, t);
      adjacentPosns.remove(p);
      Posn up = p.withY(p.getY() + 1); // TODO should this be - 1 to help Ian
                                       // out on front-end??
      Posn right = p.withX(p.getX() + 1);
      Posn down = p.withY(p.getY() - 1);// TODO should this be + 1 to help Ian
                                        // out on front-end??
      Posn left = p.withX(p.getX() - 1);

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
      throw new PosnTakenException("There is already a tile here");
    }
    // TODO should this be void or do we want to return the board?
    return this;
  }

  /**
   * Finds all the places that this tile can go based on its current orientation
   * and the board configuration.
   *
   * @param tile The tile wanting to be placed on the board
   * @return A list of Posns that define the possible valid moves of the given
   *         tile with the current board configuration
   */
  public List<Posn> validMoves(Tile tile) {
    List<Posn> validPosns = new ArrayList<>();
    for (Posn p : adjacentPosns) {
      Tile above = board.get(p.withY(p.getY() + 1)); // TODO Should this be - 1
                                                     // for Ian?
      Tile below = board.get(p.withY(p.getY() - 1)); // TODO Should this be + 1
                                                     // for Ian?
      Tile right = board.get(p.withX(p.getX() + 1));
      Tile left = board.get(p.withX(p.getX() - 1));
      boolean aboveValid = true;
      boolean belowValid = true;
      boolean rightValid = true;
      boolean leftValid = true;

      if (above != null && tile.getTop() != above.getBottom()) {
        aboveValid = false;
      }
      if (below != null && tile.getBottom() != below.getTop()) {
        belowValid = false;
      }
      if (right != null && tile.getRight() != right.getLeft()) {
        rightValid = false;
      }
      if (left != null && tile.getLeft() != above.getRight()) {
        leftValid = false;
      }

      if (aboveValid && belowValid && rightValid && leftValid) {
        validPosns.add(p);
      }
    }
    return null;
  }
}
