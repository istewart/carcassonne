package edu.brown.cs.scij.gametests;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import edu.brown.cs.scij.game.Board;
import edu.brown.cs.scij.game.Posn;
import edu.brown.cs.scij.game.PosnTakenException;
import edu.brown.cs.scij.tile.Center;
import edu.brown.cs.scij.tile.Edge;
import edu.brown.cs.scij.tile.Feature;
import edu.brown.cs.scij.tile.InvalidEdgeException;
import edu.brown.cs.scij.tile.Tile;

public class BoardTest {

  @Test
  public void constructorTest() {
    Board b = new Board();
    assertTrue(b.getBoard().isEmpty());
  }

  @Test
  public void placeTest() throws InvalidEdgeException {
    Board b = new Board();
    assertTrue(b.getBoard().isEmpty());
    Center c = new Center(Feature.MONASTERY);
    Edge top = new Edge(Feature.FIELD);
    Edge bottom = new Edge(Feature.FIELD);
    Edge left = new Edge(Feature.FIELD);
    Edge right = new Edge(Feature.FIELD);
    Tile t = new Tile(c, top, right, bottom, left, 0);
    Posn p = new Posn(0, 0);
    try {
      b = b.place(p, t);
      assertTrue(b.getBoard().get(p).equals(t));
      Tile t2 = new Tile(c, top, bottom, left, right, 0);
      b = b.place(p, t2);
      assertTrue(false);
    } catch (PosnTakenException e) {
      assertTrue(b.getBoard().get(p).equals(t));
    }

  }

  @Test
  public void validMovesTest() throws InvalidEdgeException, PosnTakenException {
    // these tiles do not actually exist but are used for testing
    Center c = new Center(Feature.MONASTERY);
    Edge top = new Edge(Feature.ROAD);
    Edge right = new Edge(Feature.CITY);
    Edge bottom = new Edge(Feature.RIVER);
    Edge left = new Edge(Feature.FIELD);
    Tile centerT = new Tile(c, top, right, bottom, left, 0);
    Board b = new Board();
    Posn p = new Posn(0, 0);
    b.place(p, centerT);
    c = new Center(Feature.ENDPOINT);
    top = new Edge(Feature.ROAD);
    right = new Edge(Feature.ROAD);
    bottom = new Edge(Feature.ROAD);
    left = new Edge(Feature.ROAD);
    Tile otherT = new Tile(c, top, right, bottom, left, 0);
    System.out.println("here");
    List<Posn> validMoves = b.validMoves(otherT);
    assertTrue(validMoves.size() == 1);
    assertTrue(validMoves.get(0).equals(new Posn(0, 1)));
    c = new Center(Feature.ENDPOINT);
    top = new Edge(Feature.CITY);
    right = new Edge(Feature.CITY);
    bottom = new Edge(Feature.CITY);
    left = new Edge(Feature.CITY);
    otherT = new Tile(c, top, right, bottom, left, 0);
    validMoves = b.validMoves(otherT);
    assertTrue(validMoves.size() == 1);
    assertTrue(validMoves.get(0).equals(new Posn(1, 0)));
    c = new Center(Feature.ENDPOINT);
    top = new Edge(Feature.RIVER);
    right = new Edge(Feature.RIVER);
    bottom = new Edge(Feature.RIVER);
    left = new Edge(Feature.RIVER);
    otherT = new Tile(c, top, right, bottom, left, 0);
    validMoves = b.validMoves(otherT);
    assertTrue(validMoves.size() == 1);
    assertTrue(validMoves.get(0).equals(new Posn(0, -1)));
    c = new Center(Feature.ENDPOINT);
    top = new Edge(Feature.FIELD);
    right = new Edge(Feature.FIELD);
    bottom = new Edge(Feature.FIELD);
    left = new Edge(Feature.FIELD);
    otherT = new Tile(c, top, right, bottom, left, 0);
    validMoves = b.validMoves(otherT);
    assertTrue(validMoves.size() == 1);
    assertTrue(validMoves.get(0).equals(new Posn(-1, 0)));
    c = new Center(Feature.ENDPOINT);
    bottom = new Edge(Feature.ROAD);
    left = new Edge(Feature.CITY);
    top = new Edge(Feature.RIVER);
    right = new Edge(Feature.FIELD);
    otherT = new Tile(c, top, right, bottom, left, 0);
    validMoves = b.validMoves(otherT);
    assertTrue(validMoves.size() == 4);
    assertTrue(validMoves.contains(new Posn(1, 0)));
    assertTrue(validMoves.contains(new Posn(-1, 0)));
    assertTrue(validMoves.contains(new Posn(0, 1)));
    assertTrue(validMoves.contains(new Posn(0, -1)));
  }
}
