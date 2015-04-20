package edu.brown.cs.scij.gametests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import edu.brown.cs.scij.game.Meeple;
import edu.brown.cs.scij.game.Player;

public class MeepleTest {

  @Test
  public void constructorTest() {
    Player p = new Player(1, "p");
    Meeple m = new Meeple(p);

    assertTrue(m.getPlayer().equals(p));
  }
}
