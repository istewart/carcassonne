package edu.brown.cs.scij.game;

/**
 * An exception to be thrown when one tries to place a tile on a posn which is
 * taken.
 * @author szellers
 *
 */
public class PosnTakenException extends Exception {
  /**
   * Generated serialVersionUID
   */
  private static final long serialVersionUID = -4288297233942595920L;

  /**
   * constructor for PosnTakenException.
   * @param message what caused the exception.
   */
  public PosnTakenException(String message) {
    super(message);
  }
}
