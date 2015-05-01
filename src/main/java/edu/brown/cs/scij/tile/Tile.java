package edu.brown.cs.scij.tile;

import java.util.List;

/**
 * main class for a tile in carcassonne.
 * @author scij
 *
 */
public class Tile {
  static int numTiles = 0;
  private final Center[] centers;
  private Edge top;
  private Edge bottom;
  private Edge left;
  private Edge right;
  private int id;
  private final int shield;
  private int rotation = 0; // TODO change in rotateLeft
  private int numRoads;
  private int pngID = 0;

  /**
   * constructor for Tile.
   * @param center the Center of the tile
   * @param top the top edge of the tile
   * @param right the right edge of the tile
   * @param bottom the bottom edge of the tile
   * @param left the left edge of the tile
   * @param shield how many shields are on the tile
   */
  public Tile(Center center, Edge top, Edge right, Edge bottom, Edge left,
      int shield) {
    if (center == null || top == null || right == null || bottom == null
        || left == null) {
      throw new UnsupportedOperationException(
          "All centers and edges of a tile must be non-null");
    }
    this.centers = new Center[2];
    this.centers[0] = center;
    this.centers[1] = new Center(Feature.FIELD);
    this.top = top;
    this.right = right;
    this.bottom = bottom;
    this.left = left;
    this.shield = shield;
    this.id = numTiles;
    numTiles++;
    setNumRoads();
  }
  
  /**
   * overloaded constructor for 2 centered Tile.
   * @param center1 the first Center of the tile
   * @param center2 the second Center of the tile
   * @param top the top edge of the tile
   * @param right the right edge of the tile
   * @param bottom the bottom edge of the tile
   * @param left the left edge of the tile
   * @param shield how many shields are on the tile
   */
  public Tile(Center center1, Center center2, Edge top, Edge right, Edge bottom, Edge left,
      int shield) {
    if (center1 == null || center2 == null || top == null || right == null || bottom == null
        || left == null) {
      throw new UnsupportedOperationException(
          "All centers and edges of a tile must be non-null");
    }
    this.centers = new Center[2];
    this.centers[0] = center1;
    this.centers[1] = center2;
    this.top = top;
    this.right = right;
    this.bottom = bottom;
    this.left = left;
    this.shield = shield;
    this.id = numTiles;
    numTiles++;
    setNumRoads();
  }

  /**
   * overloaded constructor for tiles with image ids.
   * @param pngID the id of the image
   * @param center the Center of the tile
   * @param top the top edge of the tile
   * @param right the right edge of the tile
   * @param bottom the bottom edge of the tile
   * @param left the left edge of the tile
   * @param shield how many shields are on the tile
   */
  public Tile(int pngID, Center center, Edge top, Edge right, Edge bottom,
      Edge left,
      int shield) {
    if (center == null || top == null || right == null || bottom == null
        || left == null) {
      throw new UnsupportedOperationException(
          "All centers and edges of a tile must be non-null");
    }
    this.centers = new Center[2];
    this.centers[0] = center;
    this.centers[1] = new Center(Feature.FIELD);
    this.top = top;
    this.right = right;
    this.bottom = bottom;
    this.left = left;
    this.shield = shield;
    this.pngID = pngID;
    this.id = numTiles;
    numTiles++;
    setNumRoads();
  }
  
  /**
   * overloaded constructor for 2 centered Tile with image id.
   * @param pngID the id of the image
   * @param center1 the first Center of the tile
   * @param center2 the second Center of the tile
   * @param top the top edge of the tile
   * @param right the right edge of the tile
   * @param bottom the bottom edge of the tile
   * @param left the left edge of the tile
   * @param shield how many shields are on the tile
   */
  public Tile(int pngID, Center center1, Center center2, Edge top, Edge right, Edge bottom, Edge left,
      int shield) {
    if (center1 == null || center2 == null || top == null || right == null || bottom == null
        || left == null) {
      throw new UnsupportedOperationException(
          "All centers and edges of a tile must be non-null");
    }
    this.centers = new Center[2];
    this.centers[0] = center1;
    this.centers[1] = center2;
    this.top = top;
    this.right = right;
    this.bottom = bottom;
    this.left = left;
    this.shield = shield;
    this.pngID = pngID;
    this.id = numTiles;
    numTiles++;
    setNumRoads();
  }

  /**
   * sets the number of roads on a tile.
   */
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

  /**
   * getter for top.
   * @return top
   */
  public Edge getTop() {
    return top;
  }

  /**
   * getter for center.
   * @return centers[0]
   */
  public Center getCenter1() {
    return centers[0];
  }

  /**
   * tells whether the tile has one center.
   * @return whether the tile has one center
   */
  public boolean hasOneCenter(){
	  return centers[1] == null;
  }
  
  /**
   * getter for second center
   * @return centers[1]
   */
  public Center getCenter2() {
	  return centers[1];
  }
  /**
   * getter for bottom.
   * @return bottom
   */
  public Edge getBottom() {
    return bottom;
  }

  /**
   * getter for left.
   * @return left
   */
  public Edge getLeft() {
    return left;
  }

  /**
   * getter for right.
   * @return right
   */
  public Edge getRight() {
    return right;
  }

  /**
   * getter for rotation.
   * @return rotation
   */
  public int getRotation() {
    return rotation;
  }

  /**
   * setter for rotation.
   * @param rotation what to set rotation to
   */
  public void setRotation(int rotation) {
    this.rotation = rotation;
  }

  /**
   * getter for id.
   * @return id
   */
  public int getId() {
    return id;
  }

  /**
   * getter for shield.
   * @return shield.
   */
  public int getShield() {
    return shield;
  }

  /**
   * determines whether a road ends on the tile.
   * @return whether a road ends on a tile.
   */
  public boolean roadEnds() {
    if (numRoads == 2) {
      return false;
    }
    return true;
  }

  /**
   * rotates the tile to the left.
   */
  public void rotateLeft() {
    Edge tmptop = top;
    top = right;
    right = bottom;
    bottom = left;
    left = tmptop;
    if (rotation == 0) {
      rotation = 270;
    } else {
      rotation -= 90;
    }
  }

  /**
   * rotates the tile to the right.
   */
  public void rotateRight() {
    Edge tmptop = top;
    top = left;
    left = bottom;
    bottom = right;
    right = tmptop;
    if (rotation == 270) {
      rotation = 0;
    } else {
      rotation += 90;
    }
  }

  @Override
  public String toString() {
    return "Tile: id:" + id + ", c:" + centers.toString() + ", t:"
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
