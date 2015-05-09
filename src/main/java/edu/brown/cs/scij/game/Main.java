package edu.brown.cs.scij.game;

import edu.brown.cs.scij.network.BackEnd;
import edu.brown.cs.scij.network.CarcBackEnd;
import edu.brown.cs.scij.network.MainServer;
import edu.brown.cs.scij.network.Network;

import joptsimple.OptionException;
import joptsimple.OptionParser;
import joptsimple.OptionSet;

/**
 * the main class for carcassonne.
 * @author szellers
 *
 */
public class Main {

  private static Referee r = null;

  /**
   * Not allowed to create a new Main object.
   */
  private Main() {
    throw new UnsupportedOperationException(
        "Can't construct Main object from Carcassonne");
  }

  /**
   * This main method simply begins our program and makes the necessary objects
   * our program will utilize later.
   *
   * @param args
   *        no args
   */
  public static void main(String[] args) {
    OptionParser parser = new OptionParser();
    parser.accepts("debug");
    OptionSet options = null;
    try {
      options = parser.parse(args);
    } catch (OptionException e3) {
      System.out.println("Invalid arguments.");
      return;
    }

    r = new Referee();
    BackEnd cbe = new CarcBackEnd(r);
    if (options.has("debug")) {
      Network.getNetwork(args).setBackEnd(cbe).setServer(
          new MainServer().talk()).go();
    } else {
      Network.getNetwork(args).setBackEnd(cbe).setServer(new MainServer()).go();
    }
  }
}
