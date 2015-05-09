package edu.brown.cs.scij.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableMap;

import edu.brown.cs.scij.tile.Direction;
import edu.brown.cs.scij.tile.Feature;
import edu.brown.cs.scij.tile.Tile;
import edu.brown.cs.scij.tile.TileFeature;

/**
 * A Board is a map of Posns to Tiles.
 * @author szellers
 *
 */
public class Board {
  private Map<Posn, Tile> board;
  private Set<Posn> adjacentPosns;
  private Set<Posn> meeplePosns;

  /**
   * Creates a new Board.
   */
  public Board() {
    this.board = new HashMap<>();
    this.adjacentPosns = new HashSet<>();
    this.meeplePosns = new HashSet<>();
  }

  /**
   * Gets the positions of the Meeples on the Board.
   * @return the Set of Posns where Meeples are located on the Board
   */

  public Set<Posn> getMeeplePosns() {
    return meeplePosns;
  }

  /**
   * Gets the map of posns to tiles that is the representation of the board.
   * @return the map of posns to tiles
   */
  public Map<Posn, Tile> getBoard() {
    return ImmutableMap.copyOf(board);
  }

  /**
   * Places Tile t at Posn p on the Board without regards to valid moves. If the
   * Posn is already taken, PosnTakenExcetption is thrown.
   * @param p The Posn to place the tile
   * @param t The Tile to place at the position
   * @return The Board object, for redrawing
   * @throws PosnTakenException if the Posn is already on the board.
   */
  public Board forcePlace(Posn p, Tile t) throws PosnTakenException {
    Tile there = board.get(p);
    if (there == null) {
      board.put(p, t);

      TileFeature top = t.getTop();
      TileFeature bottom = t.getBottom();
      TileFeature left = t.getLeft();
      TileFeature right = t.getRight();

      adjacentPosns.remove(p);
      Posn up = p.withY(p.getY() + 1);
      Posn rightP = p.withX(p.getX() + 1);
      Posn down = p.withY(p.getY() - 1);
      Posn leftP = p.withX(p.getX() - 1);

      if (board.containsKey(up)) {
        if (board.get(up).getBottom().touchesMeeple()) {
          top.setTouchesMeeple(true);
          setTouchesMeeple(p, Direction.UP);
        }
      }
      if (board.containsKey(down)) {
        if (board.get(down).getTop().touchesMeeple()) {
          bottom.setTouchesMeeple(true);
          setTouchesMeeple(p, Direction.DOWN);
        }
      }
      if (board.containsKey(leftP)) {
        if (board.get(leftP).getRight().touchesMeeple()) {
          left.setTouchesMeeple(true);
          setTouchesMeeple(p, Direction.LEFT);
        }
      }
      if (board.containsKey(rightP)) {
        if (board.get(rightP).getLeft().touchesMeeple()) {
          right.setTouchesMeeple(true);
          setTouchesMeeple(p, Direction.RIGHT);
        }
      }

      if (!adjacentPosns.contains(up) && !board.containsKey(up)) {
        adjacentPosns.add(up);
      }
      if (!adjacentPosns.contains(rightP) && !board.containsKey(rightP)) {
        adjacentPosns.add(rightP);
      }
      if (!adjacentPosns.contains(down) && !board.containsKey(down)) {
        adjacentPosns.add(down);
      }
      if (!adjacentPosns.contains(leftP) && !board.containsKey(leftP)) {
        adjacentPosns.add(leftP);
      }
    } else {
      throw new PosnTakenException("There is already a tile here");
    }
    // TODO should this be void or do we want to return the board?
    return this;
  }

  /**
   * Places Tile t at Posn p on the Board. If the Posn is already taken,
   * PosnTakenExcetption is thrown.
   *
   * @param p The Posn to place the tile
   * @param t The Tile to place at the position
   * @return The Board object, for redrawing
   * @throws PosnTakenException if the Posn is already on the board.
   * @throws IllegalArgumentException if the posn isn't in the list of valid
   *         moves
   */
  public Board place(Posn p, Tile t) throws PosnTakenException {

    Tile there = board.get(p);
    if (there == null) {
      if (!validMoves(t).contains(p)) {
        throw new IllegalArgumentException(
            "That is not a valid place to put that tile!");
      }
      board.put(p, t);

      TileFeature top = t.getTop();
      TileFeature bottom = t.getBottom();
      TileFeature left = t.getLeft();
      TileFeature right = t.getRight();

      adjacentPosns.remove(p);
      Posn up = p.withY(p.getY() + 1);
      Posn rightP = p.withX(p.getX() + 1);
      Posn down = p.withY(p.getY() - 1);
      Posn leftP = p.withX(p.getX() - 1);

      if (board.containsKey(up)) {
        if (board.get(up).getBottom().touchesMeeple()) {
          top.setTouchesMeeple(true);
          setTouchesMeeple(p, Direction.UP);
        }
      }
      if (board.containsKey(down)) {
        if (board.get(down).getTop().touchesMeeple()) {
          bottom.setTouchesMeeple(true);
          setTouchesMeeple(p, Direction.DOWN);
        }
      }
      if (board.containsKey(leftP)) {
        if (board.get(leftP).getRight().touchesMeeple()) {
          left.setTouchesMeeple(true);
          setTouchesMeeple(p, Direction.LEFT);
        }
      }
      if (board.containsKey(rightP)) {
        if (board.get(rightP).getLeft().touchesMeeple()) {
          right.setTouchesMeeple(true);
          setTouchesMeeple(p, Direction.RIGHT);
        }
      }

      if (!adjacentPosns.contains(up) && !board.containsKey(up)) {
        adjacentPosns.add(up);
      }
      if (!adjacentPosns.contains(rightP) && !board.containsKey(rightP)) {
        adjacentPosns.add(rightP);
      }
      if (!adjacentPosns.contains(down) && !board.containsKey(down)) {
        adjacentPosns.add(down);
      }
      if (!adjacentPosns.contains(leftP) && !board.containsKey(leftP)) {
        adjacentPosns.add(leftP);
      }
    } else {
      throw new PosnTakenException("There is already a tile here");
    }
    return this;
  }

  /**
   * Sets whether the tile at position p touches a meeple.
   * @param p the position
   * @param d the direction
   */
  public void setTouchesMeeple(Posn p, Direction d) {
    Tile t = board.get(p);
    Feature feature = null;
    if (d == Direction.RIGHT) {
      feature = t.getRight().getFeature();
      Posn right = p.withX(p.getX() + 1);
      if (board.containsKey(right)) {
        if (!board.get(right).getLeft().touchesMeeple()) {
          setTouchesMeeple(right, Direction.LEFT);
        }
      }
      if (t.getCenter1().getFeature() == feature
          || t.getCenter2().getFeature() == feature) {
        if (t.getCenter1().getFeature() == feature) {
          if (t.getCenter1().touchesMeeple()) {
            return;
          }
          t.getCenter1().setTouchesMeeple(true);
        }
        if (t.getCenter2().getFeature() == feature) {
          if (t.getCenter2().touchesMeeple()) {
            return;
          }
          t.getCenter2().setTouchesMeeple(true);
        }
        if (t.getLeft().getFeature() == feature) {
          if (t.getLeft().touchesMeeple()) {
            return;
          }
          t.getLeft().setTouchesMeeple(true);
          Posn left = p.withX(p.getX() - 1);
          if (board.containsKey(left)) {
            board.get(left).getRight().setTouchesMeeple(true);
            setTouchesMeeple(left, Direction.RIGHT);
          }
        }
        if (t.getTop().getFeature() == feature) {
          if (t.getTop().touchesMeeple()) {
            return;
          }
          t.getTop().setTouchesMeeple(true);
          Posn top = p.withY(p.getY() + 1);
          if (board.containsKey(top)) {
            board.get(top).getBottom().setTouchesMeeple(true);
            setTouchesMeeple(top, Direction.DOWN);
          }
        }
        if (t.getBottom().getFeature() == feature) {
          if (t.getBottom().touchesMeeple()) {
            return;
          }
          t.getBottom().setTouchesMeeple(true);
          Posn bottom = p.withY(p.getY() - 1);
          if (board.containsKey(bottom)) {
            board.get(bottom).getTop().setTouchesMeeple(true);
            setTouchesMeeple(bottom, Direction.UP);
          }
        }
      }
    } else if (d == Direction.LEFT) {
      feature = t.getLeft().getFeature();
      Posn left = p.withX(p.getX() - 1);
      if (board.containsKey(left)) {
        if (!board.get(left).getRight().touchesMeeple()) {
          setTouchesMeeple(left, Direction.RIGHT);
        }
      }
      if (t.getCenter1().getFeature() == feature
          || t.getCenter2().getFeature() == feature) {
        if (t.getCenter1().getFeature() == feature) {
          if (t.getCenter1().touchesMeeple()) {
            return;
          }
          t.getCenter1().setTouchesMeeple(true);
        }
        if (t.getCenter2().getFeature() == feature) {
          if (t.getCenter2().touchesMeeple()) {
            return;
          }
          t.getCenter2().setTouchesMeeple(true);
        }
        if (t.getRight().getFeature() == feature) {
          if (t.getRight().touchesMeeple()) {
            return;
          }
          t.getRight().setTouchesMeeple(true);
          Posn right = p.withX(p.getX() + 1);
          if (board.containsKey(right)) {
            board.get(right).getLeft().setTouchesMeeple(true);
            setTouchesMeeple(right, Direction.LEFT);
          }
        }
        if (t.getTop().getFeature() == feature) {
          if (t.getTop().touchesMeeple()) {
            return;
          }
          t.getTop().setTouchesMeeple(true);
          Posn top = p.withY(p.getY() + 1);
          if (board.containsKey(top)) {
            board.get(top).getBottom().setTouchesMeeple(true);
            setTouchesMeeple(top, Direction.DOWN);
          }
        }
        if (t.getBottom().getFeature() == feature) {
          if (t.getBottom().touchesMeeple()) {
            return;
          }
          t.getBottom().setTouchesMeeple(true);
          Posn bottom = p.withY(p.getY() - 1);
          if (board.containsKey(bottom)) {
            board.get(bottom).getTop().setTouchesMeeple(true);
            setTouchesMeeple(bottom, Direction.UP);
          }
        }
      }
    } else if (d == Direction.UP) {
      feature = t.getTop().getFeature();
      Posn up = p.withY(p.getY() + 1);
      if (board.containsKey(up)) {
        if (!board.get(up).getBottom().touchesMeeple()) {
          setTouchesMeeple(up, Direction.DOWN);
        }
      }
      if (t.getCenter1().getFeature() == feature
          || t.getCenter2().getFeature() == feature) {
        if (t.getCenter1().getFeature() == feature) {
          if (t.getCenter1().touchesMeeple()) {
            return;
          }
          t.getCenter1().setTouchesMeeple(true);
        }
        if (t.getCenter2().getFeature() == feature) {
          if (t.getCenter2().touchesMeeple()) {
            return;
          }
          t.getCenter2().setTouchesMeeple(true);
        }
        if (t.getRight().getFeature() == feature) {
          if (t.getRight().touchesMeeple()) {
            return;
          }
          t.getRight().setTouchesMeeple(true);
          Posn right = p.withX(p.getX() + 1);
          if (board.containsKey(right)) {
            board.get(right).getLeft().setTouchesMeeple(true);
            setTouchesMeeple(right, Direction.LEFT);
          }
        }
        if (t.getLeft().getFeature() == feature) {
          if (t.getLeft().touchesMeeple()) {
            return;
          }
          t.getLeft().setTouchesMeeple(true);
          Posn left = p.withX(p.getX() - 1);
          if (board.containsKey(left)) {
            board.get(left).getRight().setTouchesMeeple(true);
            setTouchesMeeple(left, Direction.RIGHT);
          }
        }
        if (t.getBottom().getFeature() == feature) {
          if (t.getBottom().touchesMeeple()) {
            return;
          }
          t.getBottom().setTouchesMeeple(true);
          Posn bottom = p.withY(p.getY() - 1);
          if (board.containsKey(bottom)) {
            board.get(bottom).getTop().setTouchesMeeple(true);
            setTouchesMeeple(bottom, Direction.UP);
          }
        }
      }

    } else if (d == Direction.DOWN) {
      feature = t.getBottom().getFeature();
      Posn down = p.withY(p.getY() - 1);
      if (board.containsKey(down)) {
        if (!board.get(down).getTop().touchesMeeple()) {
          setTouchesMeeple(down, Direction.UP);
        }
      }
      if (t.getCenter1().getFeature() == feature
          || t.getCenter2().getFeature() == feature) {
        if (t.getCenter1().getFeature() == feature) {
          if (t.getCenter1().touchesMeeple()) {
            return;
          }
          t.getCenter1().setTouchesMeeple(true);
        }
        if (t.getCenter2().getFeature() == feature) {
          if (t.getCenter2().touchesMeeple()) {
            return;
          }
          t.getCenter2().setTouchesMeeple(true);
        }
        if (t.getRight().getFeature() == feature) {
          if (t.getRight().touchesMeeple()) {
            return;
          }
          t.getRight().setTouchesMeeple(true);
          Posn right = p.withX(p.getX() + 1);
          if (board.containsKey(right)) {
            board.get(right).getLeft().setTouchesMeeple(true);
            setTouchesMeeple(right, Direction.LEFT);
          }
        }
        if (t.getLeft().getFeature() == feature) {
          if (t.getLeft().touchesMeeple()) {
            return;
          }
          t.getLeft().setTouchesMeeple(true);
          Posn left = p.withX(p.getX() - 1);
          if (board.containsKey(left)) {
            board.get(left).getRight().setTouchesMeeple(true);
            setTouchesMeeple(left, Direction.RIGHT);
          }
        }
        if (t.getTop().getFeature() == feature) {
          if (t.getTop().touchesMeeple()) {
            return;
          }
          t.getTop().setTouchesMeeple(true);
          Posn top = p.withY(p.getY() + 1);
          if (board.containsKey(top)) {
            board.get(top).getBottom().setTouchesMeeple(true);
            setTouchesMeeple(top, Direction.DOWN);
          }
        }
      }
    }
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
    if (adjacentPosns.isEmpty()) {
      validPosns.add(new Posn(0, 0));
    }
    for (Posn p : adjacentPosns) {
      Tile above = board.get(p.withY(p.getY() + 1));
      Tile below = board.get(p.withY(p.getY() - 1));
      Tile right = board.get(p.withX(p.getX() + 1));
      Tile left = board.get(p.withX(p.getX() - 1));
      boolean aboveValid = true;
      boolean belowValid = true;
      boolean rightValid = true;
      boolean leftValid = true;

      if (above != null
          && tile.getTop().getFeature() != above.getBottom().getFeature()) {
        aboveValid = false;
      }
      if (below != null
          && tile.getBottom().getFeature() != below.getTop().getFeature()) {
        belowValid = false;
      }
      if (right != null
          && tile.getRight().getFeature() != right.getLeft().getFeature()) {
        rightValid = false;
      }
      if (left != null
          && tile.getLeft().getFeature() != left.getRight().getFeature()) {
        leftValid = false;
      }

      if (aboveValid && belowValid && rightValid && leftValid) {
        validPosns.add(p);
      }
    }
    return validPosns;
  }

  /**
   * Returns a list of the possible directions on the tile a meeple could be
   * placed on.
   * @param p the posn the tile is on
   * @return a list of meeple placement directions
   * @throws NullTileException if the the tile isnt there
   */
  public List<Direction> validMeeples(Posn p) throws NullTileException {
    Tile t = board.get(p);
    if (t == null) {
      throw new NullTileException();
    }

    TileFeature top = t.getTop();
    TileFeature bottom = t.getBottom();
    TileFeature right = t.getRight();
    TileFeature left = t.getLeft();
    TileFeature center = t.getCenter1();

    List<Direction> meepleableLocations = new ArrayList<>();
    Feature feature = null;
    if (top.isMeeplable()) {
      feature = top.getFeature();
      if (feature == Feature.ROAD || feature == Feature.CITY) {
        if (!top.touchesMeeple()) {
          meepleableLocations.add(Direction.UP);
        }
      }
    }
    if (left.isMeeplable()) {
      feature = left.getFeature();
      if (feature == Feature.ROAD || feature == Feature.CITY) {
        if (!left.touchesMeeple()) {
          meepleableLocations.add(Direction.LEFT);
        }
      }
    }
    if (right.isMeeplable()) {
      feature = right.getFeature();
      if (feature == Feature.ROAD || feature == Feature.CITY) {
        if (!right.touchesMeeple()) {
          meepleableLocations.add(Direction.RIGHT);
        }
      }
    }
    if (bottom.isMeeplable()) {
      feature = bottom.getFeature();
      if (feature == Feature.ROAD || feature == Feature.CITY) {
        if (!bottom.touchesMeeple()) {
          meepleableLocations.add(Direction.DOWN);
        }
      }
    }
    if (center.isMeeplable()) {
      feature = center.getFeature();
      if (feature == Feature.MONASTERY) {
        meepleableLocations.add(Direction.CENTER);
      }
    }

    return meepleableLocations;
  }
}
