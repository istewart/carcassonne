package edu.brown.cs.scij.tiletests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import edu.brown.cs.scij.game.Player;
import edu.brown.cs.scij.tile.Center;
import edu.brown.cs.scij.tile.Edge;
import edu.brown.cs.scij.tile.Feature;
import edu.brown.cs.scij.tile.InvalidTileException;
import edu.brown.cs.scij.tile.OutOfMeeplesException;
import edu.brown.cs.scij.tile.Tile;

public class TileTest {

  //@Test
  public void constructorTest() throws InvalidTileException {
    Center c = new Center(Feature.MONASTERY);
    Edge top = new Edge(Feature.FIELD);
    Edge bottom = new Edge(Feature.FIELD);
    Edge left = new Edge(Feature.FIELD);
    Edge right = new Edge(Feature.FIELD);
    Tile t = new Tile(c, top, right, bottom, left, 0);
    assertTrue(t.getCenter().equals(c));
    assertTrue(t.getTop().equals(top));
    assertTrue(t.getBottom().equals(bottom));
    assertTrue(t.getLeft().equals(left));
    assertTrue(t.getRight().equals(right));
    assertTrue(t.getShield() == 0);
    assertTrue(t.getId() == 0);
    assertTrue(t.getRotation() == 0);
  }

  @Test
  public void rotateTest() throws InvalidTileException {
    Center c = new Center(Feature.MONASTERY);
    Edge top = new Edge(Feature.FIELD);
    Edge bottom = new Edge(Feature.FIELD);
    Edge left = new Edge(Feature.FIELD);
    Edge right = new Edge(Feature.FIELD);
    Tile t = new Tile(c, top, right, bottom, left, 0);
    t = t.rotateLeft();
    assertTrue(t.getCenter().equals(c));
    assertTrue(t.getTop().equals(right));
    assertTrue(t.getBottom().equals(left));
    assertTrue(t.getLeft().equals(top));
    assertTrue(t.getRight().equals(bottom));
    t = t.rotateRight();
    assertTrue(t.getCenter().equals(c));
    assertTrue(t.getTop().equals(top));
    assertTrue(t.getBottom().equals(bottom));
    assertTrue(t.getLeft().equals(left));
    assertTrue(t.getRight().equals(right));
  }

  //@Test
  public void roadtest() throws InvalidTileException {
    Center c = new Center(Feature.MONASTERY);
    Edge top = new Edge(Feature.FIELD);
    Edge bottom = new Edge(Feature.FIELD);
    Edge left = new Edge(Feature.FIELD);
    Edge right = new Edge(Feature.FIELD);
    Tile t = new Tile(c, top, right, bottom, left, 0);
    assertTrue(!t.roadEnds());
  }

  //@Test
  public void placeMeepleTest() throws InvalidTileException {
    Center c = new Center(Feature.MONASTERY);
    Edge top = new Edge(Feature.FIELD);
    Edge bottom = new Edge(Feature.FIELD);
    Edge left = new Edge(Feature.FIELD);
    Edge right = new Edge(Feature.FIELD);
    Tile t = new Tile(c, top, right, bottom, left, 0);
    Player p = new Player(1, "p");
    try {
      t.placeMeeple(p, c);
      assertTrue(p.getNumMeeples() == 6);
      t.placeMeeple(p, top);
      assertTrue(false);
    } catch (OutOfMeeplesException e) {
      assertTrue(true);
    }
  }
}
