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
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null) {
      return false;
    }
    if (!(o instanceof Posn)) {
      return false;
    }

    Posn p = (Posn) o;
    return x == p.getX() && y == p.getY();
  }

  // TODO better hashcode builtin eclipse
  @Override
  public int hashCode() {
    return x ^ y;
  }
}
