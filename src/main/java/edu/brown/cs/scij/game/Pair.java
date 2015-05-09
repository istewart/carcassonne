package edu.brown.cs.scij.game;

/**
 * Holds a pair of any two items.
 * @author szellers
 *
 * @param <T1> the first type
 * @param <T2> the second type
 */
public class Pair<T1, T2> {
  private T1 p1;
  private T2 p2;

  /**
   * Creates a new Pair with items p1 and p2.
   * @param p1 the first item of the pair
   * @param p2 the second item of the pair
   */
  public Pair(T1 p1, T2 p2) {
    this.p1 = p1;
    this.p2 = p2;
  }

  /**
   * Gets the first item of the pair.
   * @return the first item of the pair
   */
  public T1 getP1() {
    return p1;
  }

  /**
   * Gets the second item of the pair.
   * @return the second item of the pair
   */
  public T2 getP2() {
    return p2;
  }

  @Override
  public String toString() {
    return String.format("(%s, %s)", p1.toString(), p2.toString());
  }

}
