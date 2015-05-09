package edu.brown.cs.scij.gametests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import edu.brown.cs.scij.game.Player;
import edu.brown.cs.scij.tile.OutOfMeeplesException;

public class PlayerTest {

  @Test
  public void constructorTest() {
    Player p1 = new Player(1, "p1");
    assertTrue(p1.getId() == 1);
    assertTrue(p1.getName().equals("p1"));
    assertTrue(p1.getScore() == 0);
    assertTrue(p1.getNumMeeples() == 7);
  }

  @Test
  public void scoreTest() {
    Player p = new Player(1, "p");
    p.setScore(1000000);
    assertTrue(p.getScore() == 1000000);
    p.addScore(1000000);
    assertTrue(p.getScore() == 2000000);
    try {
      p.addScore(-2000000);
      assertTrue(false);
    } catch (IllegalArgumentException iae) {
      assertTrue(p.getScore() != 0);
    }
  }

  @Test
  public void meepleTest() throws OutOfMeeplesException {
    Player p = new Player(1, "p");
    p.setNumMeeples(5);
    assertTrue(p.getNumMeeples() == 5);
    p.useMeeple();
    assertTrue(p.getNumMeeples() == 4);
    p.returnMeeple();
    assertTrue(p.getNumMeeples() == 5);
    p.setNumMeeples(0);
    try {
      p.useMeeple();
    } catch (OutOfMeeplesException oome) {
      assertTrue(p.getNumMeeples() == 0);
    }
    p.setNumMeeples(7);
    p.returnMeeple();
    assertTrue(p.getNumMeeples() == 7);
  }
}
