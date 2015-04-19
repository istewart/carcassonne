package edu.brown.cs.scij.game;

/**
 * An EmptyDeckException hould be thrown when someone tries to access an element
 * of an empty deck.
 * @author szellers
 *
 */
public class EmptyDeckException extends Exception {

  /**
   * Generated Serial Version UID
   */
  private static final long serialVersionUID = 8421876375898402656L;

  /**
   * Creates a new EmptyDeckException with the given detail message.
   * @param s the detail message
   */
  public EmptyDeckException(String s) {
    super(s);
  }

  /**
   * Creates a new EmptyDeckException
   */
  public EmptyDeckException() {

  }

}
