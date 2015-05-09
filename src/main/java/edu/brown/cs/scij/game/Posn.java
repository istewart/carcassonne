package edu.brown.cs.scij.game;

/**
 * A simple position class for use on a board.
 * @author szellers
 *
 */
public class Posn {
  private int x;
  private int y;

  /**
   * constructor for posn.
   * @param x the x coord
   * @param y the y coord
   */
  public Posn(int x, int y) {
    this.x = x;
    this.y = y;
  }

  /**
   * getter for x.
   * @return x
   */
  public int getX() {
    return x;
  }

  /**
   * getter for y.
   * @return y
   */
  public int getY() {
    return y;
  }

  /**
   * returns a new posn with the old y and the input x.
   * @param newX what to set x to.
   * @return the new posn
   */
  public Posn withX(int newX) {
    return new Posn(newX, y);
  }

  /**
   * returns a new posn with the old x and the input y.
   * @param newY what to set y to.
   * @return the new posn
   */
  public Posn withY(int newY) {
    return new Posn(x, newY);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + x;
    result = prime * result + y;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof Posn)) {
      return false;
    }
    Posn other = (Posn) obj;
    if (x != other.x) {
      return false;
    }
    if (y != other.y) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return String.format("(%d, %d)", x, y);
  }

}
