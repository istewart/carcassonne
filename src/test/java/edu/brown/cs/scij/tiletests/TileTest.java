package edu.brown.cs.scij.tiletests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import edu.brown.cs.scij.tile.Center;
import edu.brown.cs.scij.tile.Edge;
import edu.brown.cs.scij.tile.Feature;
import edu.brown.cs.scij.tile.InvalidEdgeException;
import edu.brown.cs.scij.tile.Tile;

public class TileTest {

  @Test
  public void constructorTest() throws InvalidEdgeException {
    Center c = new Center(Feature.MONASTERY);
    Edge top = new Edge(Feature.FIELD);
    Edge bottom = new Edge(Feature.FIELD);
    Edge left = new Edge(Feature.FIELD);
    Edge right = new Edge(Feature.FIELD);
    Tile t = new Tile(c, top, right, bottom, left, 0);
    assertTrue(t.getCenter1().equals(c));
    assertTrue(t.getCenter2().getFeature() == Feature.FIELD);
    assertTrue(t.getTop().equals(top));
    assertTrue(t.getBottom().equals(bottom));
    assertTrue(t.getLeft().equals(left));
    assertTrue(t.getRight().equals(right));
    assertTrue(t.getShield() == 0);
    assertTrue(t.getRotation() == 0);
  }
  
  @Test
  public void constructorTestTwo() throws InvalidEdgeException {
    Center c = new Center(Feature.MONASTERY);
    Center c2 = new Center(Feature.CITY);
    Edge top = new Edge(Feature.FIELD);
    Edge bottom = new Edge(Feature.FIELD);
    Edge left = new Edge(Feature.FIELD);
    Edge right = new Edge(Feature.FIELD);
    Tile t = new Tile(c, c2, top, right, bottom, left, 0);
    assertTrue(t.getCenter1().equals(c));
    assertTrue(t.getCenter2().equals(c2));
    assertTrue(t.getTop().equals(top));
    assertTrue(t.getBottom().equals(bottom));
    assertTrue(t.getLeft().equals(left));
    assertTrue(t.getRight().equals(right));
    assertTrue(t.getShield() == 0);
    assertTrue(t.getRotation() == 0);
  }

  @Test
  public void rotateTest() throws InvalidEdgeException {
    Center c = new Center(Feature.MONASTERY);
    Edge top = new Edge(Feature.FIELD);
    Edge bottom = new Edge(Feature.FIELD);
    Edge left = new Edge(Feature.FIELD);
    Edge right = new Edge(Feature.FIELD);
    Tile t = new Tile(c, top, right, bottom, left, 0);
    t.rotateLeft();
    assertTrue(t.getCenter1().equals(c));
    assertTrue(t.getCenter2().getFeature() == Feature.FIELD);
    assertTrue(t.getTop().equals(right));
    assertTrue(t.getBottom().equals(left));
    assertTrue(t.getLeft().equals(top));
    assertTrue(t.getRight().equals(bottom));
    assertTrue(t.getRotation() == 270);
    t.rotateRight();
    assertTrue(t.getCenter1().equals(c));
    assertTrue(t.getCenter2().getFeature() == Feature.FIELD);
    assertTrue(t.getTop().equals(top));
    assertTrue(t.getBottom().equals(bottom));
    assertTrue(t.getLeft().equals(left));
    assertTrue(t.getRight().equals(right));
    assertTrue(t.getRotation() == 0);
  }

  @Test
  public void roadtest() throws InvalidEdgeException {
    Center c = new Center(Feature.MONASTERY);
    Edge top = new Edge(Feature.FIELD);
    Edge bottom = new Edge(Feature.FIELD);
    Edge left = new Edge(Feature.FIELD);
    Edge right = new Edge(Feature.FIELD);
    Tile t = new Tile(c, top, right, bottom, left, 0);
    assertTrue(t.roadEnds());
  }
}
