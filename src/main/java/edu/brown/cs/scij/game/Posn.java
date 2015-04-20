package edu.brown.cs.scij.game;

public class Posn {
  private int x;
  private int y;

  public Posn(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public Posn withX(int newX) {
    return new Posn(newX, y);
  }

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
