package edu.brown.cs.scij.game;

/**
 * Holds a pair of any two items.
 * @author szellers
 *
 * @param <T1>
 * @param <T2>
 */
public class Pair<T1, T2> {
  private T1 p1;
  private T2 p2;

  public Pair(T1 p1, T2 p2) {
    this.p1 = p1;
    this.p2 = p2;
  }

  public T1 getP1() {
    return p1;
  }

  public T2 getP2() {
    return p2;
  }

}
