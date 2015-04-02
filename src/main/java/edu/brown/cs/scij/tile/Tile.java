package edu.brown.cs.scij.tile;

import java.util.List;

import edu.brown.cs.scij.game.Board;

public class Tile {
  private Center center;
  private Edge top;
  private Edge bottom;
  private Edge left;
  private Edge right;
  private int id;
  private int rotation;
  private boolean shield;

  public Tile() {

  }

  public Edge getTop() {
    return top;
  }

  public Center getCenter() {
    return center;
  }

  public void setCenter(Center center) {
    this.center = center;
  }

  public Edge getBottom() {
    return bottom;
  }

  public void setBottom(Edge bottom) {
    this.bottom = bottom;
  }

  public Edge getLeft() {
    return left;
  }

  public void setLeft(Edge left) {
    this.left = left;
  }

  public Edge getRight() {
    return right;
  }

  public void setRight(Edge right) {
    this.right = right;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getRotation() {
    return rotation;
  }

  public void setRotation(int rotation) {
    this.rotation = rotation;
  }

  public boolean isShield() {
    return shield;
  }

  public void setShield(boolean shield) {
    this.shield = shield;
  }

  public void setTop(Edge top) {
    this.top = top;
  }

  public Tile rotateLeft() {
    // TODO rotate left
    return null;
  }

  public Tile rotateRight() {
    // TODO rotate right
    return null;
  }

  public List<Feature> validMeeple(Board board) {
    // TODO
    return null;
  }

  /*
   * SHOULD FEATURE BE AN INTERFACE AND
   * CENTER AND EDGE BE ABSTRACT CLASSES/INTERFACES
   * AND THEN MONASTERY/ROAD/ETC FEATURES BE CLASSES?
   */

}
