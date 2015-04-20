package edu.brown.cs.scij.network;

/**
 * Entry point for testing.
 */
public final class Main {

  private Main() { }

  /**
   * Entry point to this modular code.
   * @param args The command-line arguments
   */
  public static void main(String [] args) {
    Network.getNetwork(args)
      .setBackEnd(new DummyBackEnd())
      .setServer(new DummyServer().talk())
      .go();
  }
}
