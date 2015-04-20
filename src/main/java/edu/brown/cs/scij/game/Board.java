package edu.brown.cs.scij.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableMap;

import edu.brown.cs.scij.tile.Tile;

/**
 * A Board is a map of Posns to Tiles.
 * @author szellers
 *
 */
public class Board {
  private Map<Posn, Tile> board;
  private Set<Posn> adjacentPosns;

  // private Set<Posn> meeplePosns;

  /**
   * Creates a new Board.
   */
  public Board() {
    this.board = new HashMap<>();
    this.adjacentPosns = new HashSet<>();
    // this.meeplePosns = new HashSet<>();
  }

  /**
   * Gets the positions of the Meeples on the Board.
   * @return the Set of Posns where Meeples are located on the Board
   */
  /*
   * public Set<Posn> getMeeplePosns() {
   * return meeplePosns;
   * }
   */

  /**
   * Gets the map of posns to tiles that is the representation of the board.
   * @return the map of posns to tiles
   */
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
    // TODO I don't think this should return board, we only ever have to redraw
    // one tile every call, we shouldn't have to redraw the whole board. Plus,
    // the referee has the reference to the changing board, so if we want to get
    // the board we can
    Tile there = board.get(p);
    if (there == null) {
      board.put(p, t);
      adjacentPosns.remove(p);
      Posn up = p.withY(p.getY() + 1);
      Posn right = p.withX(p.getX() + 1);
      Posn down = p.withY(p.getY() - 1);
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
      Tile above = board.get(p.withY(p.getY() + 1));
      Tile below = board.get(p.withY(p.getY() - 1));
      Tile right = board.get(p.withX(p.getX() + 1));
      Tile left = board.get(p.withX(p.getX() - 1));
      boolean aboveValid = true;
      boolean belowValid = true;
      boolean rightValid = true;
      boolean leftValid = true;

      if (above != null && !tile.getTop().equals(above.getBottom())) {
        aboveValid = false;
      }
      if (below != null && !tile.getBottom().equals(below.getTop())) {
        belowValid = false;
      }
      if (right != null && !tile.getRight().equals(right.getLeft())) {
        rightValid = false;
      }
      if (left != null && !tile.getLeft().equals(left.getRight())) {
        leftValid = false;
      }

      if (aboveValid && belowValid && rightValid && leftValid) {
        validPosns.add(p);
      }
    }
    return validPosns;
  }
}
