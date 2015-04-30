package edu.brown.cs.scij.tile;

/**
 * exception which is caused when a player tries to play
 * a meeple and doesnt have any left.
 * @author scij
 *
 */
public class OutOfMeeplesException extends Exception {

  /**
   * Auto-Generated serialVersionUID
   */
  private static final long serialVersionUID = -5044351314190520268L;

  /**
   * constructor for OutOfMeeplesException.
   * @param message what caused the exception
   */
  public OutOfMeeplesException(String message) {
    super(message);
  }

  /**
   * overloaded default constructor.
   */
  public OutOfMeeplesException() {

  }

}
