package edu.brown.cs.scij.tiletests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import edu.brown.cs.scij.game.Meeple;
import edu.brown.cs.scij.game.Player;
import edu.brown.cs.scij.tile.Edge;
import edu.brown.cs.scij.tile.Feature;
import edu.brown.cs.scij.tile.InvalidEdgeException;
import edu.brown.cs.scij.tile.UnMeeplableException;

public class EdgeTest {

  @Test
  public void constructorTest() throws InvalidEdgeException {
    Edge e = new Edge(Feature.CITY);
    assertTrue(e.isMeeplable());
    assertTrue(e.getMeeple() == null);
  }

  @Test
  public void meepleTest() throws InvalidEdgeException, UnMeeplableException {
    Edge e = new Edge(Feature.FIELD);
    assertTrue(!e.isMeeplable());
    assertTrue(e.getMeeple() == null);
    Player p = new Player(1, "p");
    Meeple m = new Meeple(p);
    e.setMeeple(m);
    assertTrue(e.getMeeple().equals(m));
  }

  @Test
  public void equalsTest() throws InvalidEdgeException {
    Edge ee = new Edge(Feature.FIELD);
    Edge ero = new Edge(Feature.ROAD);
    Edge eri = new Edge(Feature.RIVER);
    Edge ee2 = new Edge(Feature.FIELD);
    assertTrue(!ee.equals(ero));
    assertTrue(!ee.equals(eri));
    assertTrue(!ero.equals(eri));
    assertTrue(ee.equals(ee2));
  }

  @Test
  public void allowedTest() {
    try {
      new Edge(Feature.CITY);
      new Edge(Feature.ENDPOINT);
      new Edge(Feature.ROAD);
      new Edge(Feature.RIVER);
      new Edge(Feature.FIELD);
      assertTrue(true);
      new Edge(Feature.MONASTERY);
      assertTrue(false);
    } catch (InvalidEdgeException ite) {
      assertTrue(true);
    }
  }
}
