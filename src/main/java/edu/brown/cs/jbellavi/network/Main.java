package edu.brown.cs.jbellavi.network;

/**
 * Entry point for testing.
 */
public final class Main {

  private Main() { }

  /**
   * Entry point to this modular code.
   */
  public static void main(String [] args) {
    Network.getNetwork(args).go();
  }
}
