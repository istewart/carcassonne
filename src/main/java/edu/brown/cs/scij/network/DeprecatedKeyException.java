package edu.brown.cs.scij.network;

/**
 * This exception is thrown if you attempts to access a deprecated
 * {@link Key}.
 *
 * @author  Joseph Bellavia
 */
class DeprecatedKeyException extends Exception {
  public DeprecatedKeyException() {
    super("Deprecated Key accessed illegally.");
  }
}
