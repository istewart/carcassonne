package edu.brown.cs.scij.game;

/**
 * The Boolean class wraps a value of the primitive type boolean in an object.
 * An object of type Boolean contains a single field whose type is boolean.
 * Side note: Nobody knows why this class exists.
 * @see <a href="http://docs.oracle.com/javase/8/docs/api/java/lang/Boolean.html">Boolean</a>
 *
 * @author  Scott fuckin' Zellers
 */
public class Finished {
  private boolean finished;

  /**
   * Changes the value of this Boolean object to a boolean primitive.
   * @param finished  Changes the value of this finished Boolean
   */
  public Finished(boolean finished) {
    this.finished = finished;
  }

  /**
   * Changes the value of this Boolean object to a boolean primitive.
   * @param b  Changes the value of this finished Boolean
   */
  public void setFinished(boolean b) {
    finished = b;
  }

  /**
   * Returns the value of this Boolean object as a boolean primitive.
   * @return the primitive boolean value of this object
   */
  public boolean isFinished() {
    return finished;
  }

}
