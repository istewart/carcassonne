package edu.brown.cs.jbellavi.network;

/**
 * This exception is thrown if you attempts to access a deprecated
 * {@link Key}.
 */
class DeprecatedKeyException extends Exception {
  public DeprecatedKeyException() {
    super("Deprecated Key accessed illegally.");
  }
}
