package edu.brown.cs.scij.game;

/**
 * an exception to be thrown when there is no tile present.
 * @author szellers
 *
 */
public class NullTileException extends Exception {

  /**
   * Generated serialVersionUID.
   */
  private static final long serialVersionUID = -8876638146145802633L;

  /**
   * overloaded constructor for NullTileException.
   * @param s what caused the exception
   */
  public NullTileException(String s) {
    super(s);
  }

  /**
   * default constructor for NullTileException.
   */
  public NullTileException() {
    super();
  }
}
