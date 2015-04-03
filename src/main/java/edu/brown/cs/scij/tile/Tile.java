package edu.brown.cs.scij.tile;

import java.util.ArrayList;
import java.util.List;

import edu.brown.cs.scij.game.Board;

public class Tile {
  static int numTiles = 0;
  private final Center center;
  private Edge top;
  private Edge bottom;
  private Edge left;
  private Edge right;
  private int id;
  private final int shield;
  private int rotation = 0;
  private int numRoads;

  public Tile(Center center, Edge top, Edge right, Edge bottom, Edge left,
      int shield) {
    this.center = center;
    this.top = top;
    this.right = right;
    this.bottom = bottom;
    this.left = left;
    this.shield = shield;
    this.id = numTiles;
    numTiles++;
    setNumRoads();
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
    /*
    if (center.getFeature() == Feature.ENDPOINT
        || center.getFeature() == Feature.MONASTERY ) {
      return true;
    }
    return false;
    */
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

  public List<TileFeature> validMeeples(Board board) {
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
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null) {
      return false;
    }
    if (!(o instanceof Tile)) {
      return false;
    }

    Tile t = (Tile) o;

    return center.equals(t.getCenter()) && top.equals(t.getTop())
        && right.equals(t.getRight()) && bottom.equals(t.getBottom())
        && left.equals(t.getLeft()) && shield == t.getShield()
        && id == t.getId();
    // TODO SHOULD WE ONLY HAVE TO CHECK EQUALITY BASED ON ID? OR SHOULD WE NOT
    // CHECK EQUALITY BASED ON ID AT ALL BECAUSE OF DUPLICATES?
  }

  @Override
  public String toString() {
    return "Tile: id:" + id + ", c:" + center.toString() + ", t:"
        + top.toString() + ", r:" + right.toString() + ", b:"
        + bottom.toString() + ", l:" + left.toString() + ", shields" + shield;
  }
  /*
   * SHOULD FEATURE BE AN INTERFACE AND
   * CENTER AND EDGE BE ABSTRACT CLASSES/INTERFACES
   * AND THEN MONASTERY/ROAD/ETC FEATURES BE CLASSES?
   */

}
