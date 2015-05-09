package edu.brown.cs.scij.tile;

/**
 * caused when you try to remove a meeple that isnt there.
 * @author scij
 *
 */
public class NullMeepleException extends Exception {

  /**
   * Generated serialVersionUID.
   */
  private static final long serialVersionUID = 7931450358630054421L;

  /**
   * constructor for NullMeepleException.
   * @param s what caused the exception
   */
  public NullMeepleException(String s) {
    super(s);
  }

  /**
   * overloaded default constructor.
   */
  public NullMeepleException() {
    super();
  }
}
