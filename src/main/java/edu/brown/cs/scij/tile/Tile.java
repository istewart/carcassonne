package edu.brown.cs.scij.tile;

import java.util.ArrayList;
import java.util.List;

public class Tile {
  static int numTiles = 0;
  private final Center center;
  private Edge top;
  private Edge bottom;
  private Edge left;
  private Edge right;
  private int id;
  private final int shield; // I think shield should be a boolean in Center or
                            // Edge, not an int here -Colby
  private int rotation = 0; // TODO change in rotateLeft
  private int numRoads;
  private Feature meepledFeature;
  //this still isnt how this works^ could be multiple meepled features

  public Tile(Center center, Edge top, Edge right, Edge bottom, Edge left,
      int shield) {
    if (center == null || top == null || right == null || bottom == null
        || left == null) {
      throw new UnsupportedOperationException(
          "All centers and edges of a tile must be non-null");
    }
    this.center = center;
    this.top = top;
    this.right = right;
    this.bottom = bottom;
    this.left = left;
    this.shield = shield;
    this.id = numTiles;
    numTiles++;
    setNumRoads();
    this.meepledFeature = null;
  }

  private void setNumRoads() {
    int roads = 0;
    if (top.getFeature() == Feature.ROAD) {
      roads++;
    }
    if (right.getFeature() == Feature.ROAD) {
      roads++;
    }
    if (bottom.getFeature() == Feature.ROAD) {
      roads++;
    }
    if (left.getFeature() == Feature.ROAD) {
      roads++;
    }
    numRoads = roads;
  }

  public Feature getMeepledFeature() {
    return meepledFeature;
  }

  public void setMeepledFeature(Feature f) {
    meepledFeature = f;
  }

  public Edge getTop() {
    return top;
  }

  public Center getCenter() {
    return center;
  }

  public Edge getBottom() {
    return bottom;
  }

  public Edge getLeft() {
    return left;
  }

  public Edge getRight() {
    return right;
  }

  public int getId() {
    return id;
  }

  public int getRotation() {
    return rotation;
  }

  public void setRotation(int rotation) {
    this.rotation = rotation;
  }

  public int getShield() {
    return shield;
  }

  public boolean roadEnds() {
    if (numRoads == 2) {
      return false;
    }
    return true;
  }

  public Tile rotateLeft() {
    Edge tmptop = top;
    top = right;
    right = bottom;
    bottom = left;
    left = tmptop;
    return this;
  }

  public Tile rotateRight() {
    Edge tmptop = top;
    top = left;
    left = bottom;
    bottom = right;
    right = tmptop;
    return this;
  }

  /*
   * public void placeMeeple(Player player, TileFeature feature)
   * throws OutOfMeeplesException, InvalidMeeplePlacementException {
   * if (player.getNumMeeples() == 0) {
   * throw new OutOfMeeplesException("You have no meeples left!");
   * } else if (!feature.isMeeplable()) {
   * throw new InvalidMeeplePlacementException("no. cant meeple there");
   * } else {
   * player.useMeeple();
   * Meeple m = new Meeple(player);
   * feature.setMeeple(m);
   * meepledFeature = feature.getFeature();
   * }
   * }
   */

  public List<TileFeature> validMeeples() {
    List<TileFeature> meepleableLocations = new ArrayList<>();
    if (top.isMeeplable) {
      meepleableLocations.add(top);
    }
    if (left.isMeeplable) {
      meepleableLocations.add(left);
    }
    if (right.isMeeplable) {
      meepleableLocations.add(right);
    }
    if (bottom.isMeeplable) {
      meepleableLocations.add(bottom);
    }
    if (center.isMeeplable) {
      meepleableLocations.add(center);
    }
    return meepleableLocations;
  }

  @Override
  public String toString() {
    return "Tile: id:" + id + ", c:" + center.toString() + ", t:"
        + top.toString() + ", r:" + right.toString() + ", b:"
        + bottom.toString() + ", l:" + left.toString() + ", shields" + shield;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof Tile)) {
      return false;
    }
    Tile other = (Tile) obj;
    if (id != other.id) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + id;
    return result;
  }

}
