package edu.brown.cs.scij.gametests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import edu.brown.cs.scij.game.Color;
import edu.brown.cs.scij.game.Player;

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
  public void colorTest() {
    Player p = new Player(1, "p");
    p.setPlayerColor(Color.BLACK);
    assertTrue(p.getPlayerColor() == Color.BLACK);
  }

  //@Test
  public void scoreTest() {
    Player p = new Player(1, "p");
    p.setScore(1000000);
    assertTrue(p.getScore() == 1000000);
    p.addScore(1000000);
    assertTrue(p.getScore() == 2000000);
    p.addScore(-2000000);
    assertTrue(p.getScore() != 0);
  }

  //@Test
  public void meepleTest() {
    Player p = new Player(1, "p");
    p.setNumMeeples(5);
    assertTrue(p.getNumMeeples() == 5);
    p.useMeeple();
    assertTrue(p.getNumMeeples() == 4);
    p.returnMeeple();
    assertTrue(p.getNumMeeples() == 5);
    p.setNumMeeples(0);
    p.useMeeple();
    assertTrue(p.getNumMeeples() == 0);
    p.setNumMeeples(7);
    p.returnMeeple();
    assertTrue(p.getNumMeeples() == 7);
  }

  /*
   * @Test
   * public void factoryTest(){
   * PlayerFactory pf = new PlayerFactory();
   * Player p1 = pf.newPlayer(1, "p1");
   * Player p2 = pf.newPlayer(2, "p2");
   * assertTrue(!p1.equals(p2));
   * Player p12 = pf.newPlayer(1, "p1");
   * assertTrue(!p1.equals(p12));
   * assertTrue(p1.getId() == p12.getId());
   * assertTrue(p1.getName().equals(p12.getName()));
   * assertTrue(p1.getNumMeeples() == p12.getNumMeeples());
   * assertTrue(p1.getPlayerColor() == p12.getPlayerColor());
   * assertTrue(p1.getScore() == p12.getScore());
   * }
   */
}
