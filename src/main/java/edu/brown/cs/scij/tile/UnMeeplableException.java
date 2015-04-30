package edu.brown.cs.scij.tile;

/**
 * exception which is thrown when something is attempted
 * to be meepled which cannot be meepled.
 * @author scij
 *
 */
public class UnMeeplableException extends Exception {

  /**
   * Generated serialVersionUID.
   */
  private static final long serialVersionUID = 1787076037473991959L;

  /**
   * constructor for UnMeeplableException.
   * @param s what caused the exception
   */
  public UnMeeplableException(String s) {
    super(s);
  }

  /**
   * overloaded default constructor.
   */
  public UnMeeplableException() {
    super();
  }
}
