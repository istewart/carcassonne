package edu.brown.cs.scij.tile;

/**
 * an exception to be thrown when an edge is attempted with
 * a feature that cannot be an edge.
 * @author scij
 *
 */
public class InvalidEdgeException extends Exception {

  /**
   * Generated serialVersionUID.
   */
  private static final long serialVersionUID = 4080762199317524528L;

  /**
   * constructor for InvalidEdgeException.
   * @param message what caused the exception.
   */
  public InvalidEdgeException(String message) {
    super(message);
  }

  /**
   * overloaded default constructor.
   */
  public InvalidEdgeException() {

  }
}
