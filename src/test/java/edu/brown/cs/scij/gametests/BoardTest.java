package edu.brown.cs.scij.gametests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import edu.brown.cs.scij.game.Board;
import edu.brown.cs.scij.game.NullTileException;
import edu.brown.cs.scij.game.Player;
import edu.brown.cs.scij.game.Posn;
import edu.brown.cs.scij.game.PosnTakenException;
import edu.brown.cs.scij.game.Referee;
import edu.brown.cs.scij.tile.Center;
import edu.brown.cs.scij.tile.Direction;
import edu.brown.cs.scij.tile.Edge;
import edu.brown.cs.scij.tile.Feature;
import edu.brown.cs.scij.tile.InvalidEdgeException;
import edu.brown.cs.scij.tile.OutOfMeeplesException;
import edu.brown.cs.scij.tile.Tile;
import edu.brown.cs.scij.tile.UnMeeplableException;

public class BoardTest {

  @Test
  public void validMoveWithMeeple() throws InvalidEdgeException,
      PosnTakenException, NullTileException, OutOfMeeplesException,
      UnMeeplableException {
    Center c = new Center(Feature.FIELD);
    Edge top = new Edge(Feature.FIELD);
    Edge bottom = new Edge(Feature.FIELD);
    Edge left = new Edge(Feature.CITY);
    Edge right = new Edge(Feature.CITY);
    Tile t = new Tile(c, top, right, bottom, left, 0);

    Center c1 = new Center(Feature.CITY);
    Edge top1 = new Edge(Feature.CITY);
    Edge bottom1 = new Edge(Feature.FIELD);
    Edge left1 = new Edge(Feature.CITY);
    Edge right1 = new Edge(Feature.CITY);
    Tile toTest = new Tile(c1, top1, right1, bottom1, left1, 1);

    // List<Posn> validMoves = new ArrayList<>();
    Player p = new Player(0, "Scott");
    Referee r = new Referee();
    r.newPlayer(p);
    r.place(new Posn(0, 0), t);
    r.placeMeeple(new Posn(0, 0), p, Direction.LEFT);

    System.out.println(r.getBoard().validMoves(toTest));
  }

  @Test
  public void validMovesOtherTest() throws InvalidEdgeException,
      PosnTakenException {
    Center c = new Center(Feature.ROAD);
    Edge top = new Edge(Feature.ROAD);
    Edge bottom = new Edge(Feature.FIELD);
    Edge left = new Edge(Feature.ROAD);
    Edge right = new Edge(Feature.FIELD);
    Tile t = new Tile(c, top, right, bottom, left, 0);

    Center c1 = new Center(Feature.ENDPOINT);
    Edge top1 = new Edge(Feature.ROAD);
    Edge bottom1 = new Edge(Feature.ROAD);
    Edge left1 = new Edge(Feature.ROAD);
    Edge right1 = new Edge(Feature.CITY);
    Tile toTest = new Tile(c1, top1, right1, bottom1, left1, 0);
    Board b = new Board();
    b.place(new Posn(0, 0), t);
    List<Posn> validMoves = new ArrayList<>();
    validMoves.add(new Posn(0, 1));
    assertEquals(b.validMoves(toTest), validMoves);
  }

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

  @Test
  public void crazyValidMovesTest() throws InvalidEdgeException,
      PosnTakenException {
    Board b = new Board();
    Center endC = new Center(Feature.ENDPOINT);
    Edge roadE = new Edge(Feature.ROAD);
    Tile t13 = new Tile(endC, roadE, roadE, roadE, roadE, 0);
    Posn p1 = new Posn(-2, 2);
    Posn p2 = new Posn(-1, 2);
    Posn p3 = new Posn(0, 2);
    Posn p6 = new Posn(-2, 1);
    Posn p7 = new Posn(-1, 1);
    Posn p8 = new Posn(0, 1);
    Posn p9 = new Posn(1, 1);
    Posn p10 = new Posn(2, 1);
    Posn p11 = new Posn(-2, 0);
    Posn p12 = new Posn(-1, 0);
    Posn p13 = new Posn(0, 0);
    Posn p14 = new Posn(1, 0);
    Posn p15 = new Posn(2, 0);
    Posn p16 = new Posn(-2, -1);
    Posn p17 = new Posn(-1, -1);
    Posn p18 = new Posn(0, -1);
    Posn p19 = new Posn(1, -1);
    Posn p20 = new Posn(2, -1);
    Posn p23 = new Posn(0, -2);
    Posn p24 = new Posn(1, -2);
    Posn p25 = new Posn(2, -2);
    b.place(p13, t13);
    Center roadC = new Center(Feature.ROAD);
    Edge fieldE = new Edge(Feature.FIELD);
    Tile t14 = new Tile(roadC, roadE, fieldE, fieldE, roadE, 0);
    List<Posn> validMoves = b.validMoves(t14);
    assertTrue(validMoves.size() == 2);
    assertTrue(validMoves.contains(p14));
    assertTrue(validMoves.contains(p18));
    b.place(p14, t14);
    Tile t19 = new Tile(endC, fieldE, fieldE, roadE, fieldE, 0);
    validMoves = b.validMoves(t19);
    assertTrue(validMoves.size() == 4);
    assertTrue(validMoves.contains(p8));
    assertTrue(validMoves.contains(p9));
    assertTrue(validMoves.contains(p15));
    assertTrue(validMoves.contains(p19));
    b.place(p19, t19);
    Center cityC = new Center(Feature.CITY);
    Edge cityE = new Edge(Feature.CITY);
    Tile t20 = new Tile(cityC, cityE, cityE, fieldE, fieldE, 0);
    validMoves = b.validMoves(t20);
    assertTrue(validMoves.size() == 2);
    assertTrue(validMoves.contains(p15));
    assertTrue(validMoves.contains(p20));
    b.place(p20, t20);
    Posn p26 = new Posn(3, -1);
    Tile t12 = new Tile(roadC, fieldE, roadE, fieldE, roadE, 0);
    validMoves = b.validMoves(t12);
    assertTrue(validMoves.size() == 2);
    assertTrue(validMoves.contains(p12));
    assertTrue(validMoves.contains(p25));
    b.place(p12, t12);
    Tile t24 = new Tile(roadC, roadE, fieldE, fieldE, roadE, 0);
    validMoves = b.validMoves(t24);
    assertTrue(validMoves.size() == 3);
    assertTrue(validMoves.contains(p24));
    assertTrue(validMoves.contains(p18));
    assertTrue(validMoves.contains(p7));
    b.place(p24, t24);
    Posn p27 = new Posn(1, -3);
    Tile t15 = new Tile(cityC, fieldE, cityE, cityE, fieldE, 0);
    validMoves = b.validMoves(t15);
    assertTrue(validMoves.size() == 4);
    assertTrue(validMoves.contains(p15));
    assertTrue(validMoves.contains(p17));
    assertTrue(validMoves.contains(p25));
    assertTrue(validMoves.contains(p27));
    b.place(p15, t15);
    Posn p28 = new Posn(3, 0);
    Tile t8 = new Tile(roadC, roadE, fieldE, roadE, fieldE, 0);
    validMoves = b.validMoves(t8);
    assertTrue(validMoves.size() == 3);
    assertTrue(validMoves.contains(p8));
    assertTrue(validMoves.contains(p9));
    assertTrue(validMoves.contains(p18));
    b.place(p8, t8);
    Center fieldC = new Center(Feature.FIELD);
    Tile t7 = new Tile(fieldC, fieldE, fieldE, fieldE, cityE, 0);
    validMoves = b.validMoves(t7);
    assertTrue(validMoves.size() == 6);
    assertTrue(validMoves.contains(p7));
    assertTrue(validMoves.contains(p10));
    assertTrue(validMoves.contains(p28));
    assertTrue(validMoves.contains(p26));
    assertTrue(validMoves.contains(p17));
    assertTrue(validMoves.contains(p27));
    b.place(p7, t7);
    Tile t6 = new Tile(fieldC, fieldE, cityE, fieldE, fieldE, 0);
    validMoves = b.validMoves(t6);
    assertTrue(validMoves.size() == 6);
    assertTrue(validMoves.contains(p6));
    assertTrue(validMoves.contains(p2));
    assertTrue(validMoves.contains(p10));
    assertTrue(validMoves.contains(p25));
    assertTrue(validMoves.contains(p27));
    assertTrue(validMoves.contains(p17));
    b.place(p6, t6);
    Posn p29 = new Posn(-3, 1);
    Center monasteryC = new Center(Feature.MONASTERY);
    Tile t17 = new Tile(monasteryC, fieldE, fieldE, fieldE, fieldE, 0);
    validMoves = b.validMoves(t17);
    assertTrue(validMoves.size() == 7);
    assertTrue(validMoves.contains(p29));
    assertTrue(validMoves.contains(p1));
    assertTrue(validMoves.contains(p2));
    assertTrue(validMoves.contains(p10));
    assertTrue(validMoves.contains(p25));
    assertTrue(validMoves.contains(p27));
    assertTrue(validMoves.contains(p17));
    b.place(p17, t17);
    Tile t18 = new Tile(roadC, roadE, fieldE, roadE, fieldE, 0);
    validMoves = b.validMoves(t18);
    assertTrue(validMoves.size() == 5);
    assertTrue(validMoves.contains(p3));
    assertTrue(validMoves.contains(p9));
    assertTrue(validMoves.contains(p16));
    assertTrue(validMoves.contains(p29));
    assertTrue(validMoves.contains(p18));
    b.place(p18, t18);
    Tile t1 = new Tile(cityC, cityE, fieldE, fieldE, cityE, 0);
    validMoves = b.validMoves(t1);
    assertTrue(validMoves.size() == 7);
    assertTrue(validMoves.contains(p29));
    assertTrue(validMoves.contains(p1));
    assertTrue(validMoves.contains(p2));
    assertTrue(validMoves.contains(p10));
    assertTrue(validMoves.contains(p28));
    assertTrue(validMoves.contains(p26));
    assertTrue(validMoves.contains(p16));
    b.place(p1, t1);
    Tile t23 = new Tile(roadC, roadE, roadE, fieldE, fieldE, 0);
    validMoves = b.validMoves(t23);
    assertTrue(validMoves.size() == 3);
    assertTrue(validMoves.contains(p2));
    assertTrue(validMoves.contains(p10));
    assertTrue(validMoves.contains(p23));
    b.place(p23, t23);
    Posn p32 = new Posn(0, -3);
    Tile t9 = new Tile(roadC, fieldE, roadE, roadE, fieldE, 0);
    validMoves = b.validMoves(t9);
    assertTrue(validMoves.size() == 6);
    assertTrue(validMoves.contains(p3));
    assertTrue(validMoves.contains(p9));
    assertTrue(validMoves.contains(p25));
    assertTrue(validMoves.contains(p27));
    assertTrue(validMoves.contains(p32));
    assertTrue(validMoves.contains(p11));
  }
  

  @Test
  public void veryValidMeepleTest() throws InvalidEdgeException,
      PosnTakenException, NullTileException {
    Center c = new Center(Feature.CITY);
    Edge city = new Edge(Feature.CITY);
    Edge city2 = new Edge(Feature.CITY);
    Edge city3 = new Edge(Feature.CITY);
    Edge city4 = new Edge(Feature.CITY);
    Tile t = new Tile(c, city, city2, city3, city4, 0);
    Posn p = new Posn(0, 0);
    Player p1 = new Player(1, "p1");
    List<Player> players = new ArrayList<>();
    players.add(p1);
    Referee r = new Referee();
    r.setPlayers(players);
    r.place(p, t);
    List<Direction> directions = r.getBoard().validMeeples(p);
    assertTrue(directions.size() == 5);
    assertTrue(directions.contains(Direction.CENTER));
    assertTrue(directions.contains(Direction.UP));
    assertTrue(directions.contains(Direction.DOWN));
    assertTrue(directions.contains(Direction.LEFT));
    assertTrue(directions.contains(Direction.RIGHT));
  }

  @Test
  public void validMeepleRoadTest() throws InvalidEdgeException,
      PosnTakenException, NullTileException {
    Center c = new Center(Feature.ENDPOINT);
    Edge road = new Edge(Feature.ROAD);
    Edge road2 = new Edge(Feature.ROAD);
    Edge road3 = new Edge(Feature.ROAD);
    Edge road4 = new Edge(Feature.ROAD);
    Tile t = new Tile(c, road, road2, road3, road4, 0);
    Posn p = new Posn(0, 0);
    Player p1 = new Player(1, "p1");
    List<Player> players = new ArrayList<>();
    players.add(p1);
    Referee r = new Referee();
    r.setPlayers(players);
    r.place(p, t);
    List<Direction> directions = r.getBoard().validMeeples(p);
    assertTrue(directions.size() == 4);
    assertTrue(directions.contains(Direction.UP));
    assertTrue(directions.contains(Direction.DOWN));
    assertTrue(directions.contains(Direction.LEFT));
    assertTrue(directions.contains(Direction.RIGHT));
  }

  @Test
  public void validMeepleAfterPlaceTest() throws InvalidEdgeException,
      PosnTakenException, NullTileException, OutOfMeeplesException,
      UnMeeplableException {
    Center c = new Center(Feature.CITY);
    Edge city = new Edge(Feature.CITY);
    Edge city2 = new Edge(Feature.CITY);
    Edge city3 = new Edge(Feature.CITY);
    Edge city4 = new Edge(Feature.CITY);
    Tile t = new Tile(c, city, city2, city3, city4, 0);
    Posn p = new Posn(0, 0);
    Player p1 = new Player(1, "p1");
    List<Player> players = new ArrayList<>();
    players.add(p1);
    Referee r = new Referee();
    r.setPlayers(players);
    r.place(p, t);
    r.placeMeeple(p, p1, Direction.CENTER);
    List<Direction> directions = r.getBoard().validMeeples(p);
    assertTrue(directions.size() == 0);
  }

  @Test
  public void validMeepleCurvedRoadTest() throws InvalidEdgeException,
      PosnTakenException, NullTileException, OutOfMeeplesException,
      UnMeeplableException {
    Center c = new Center(Feature.ROAD);
    Edge road = new Edge(Feature.ROAD);
    Edge road2 = new Edge(Feature.ROAD);
    Edge field = new Edge(Feature.FIELD);
    Tile t = new Tile(c, field, field, road, road2, 0);
    Posn p = new Posn(0, 0);
    Player p1 = new Player(1, "p1");
    List<Player> players = new ArrayList<>();
    players.add(p1);
    Referee r = new Referee();
    r.setPlayers(players);
    r.place(p, t);
    List<Direction> directions = r.getBoard().validMeeples(p);
    assertTrue(directions.size() == 3);
    assertTrue(directions.contains(Direction.CENTER));
    assertTrue(directions.contains(Direction.DOWN));
    assertTrue(directions.contains(Direction.LEFT));
  }

  @Test
  public void validMeepleLongRoadTest() throws InvalidEdgeException,
      PosnTakenException, NullTileException, OutOfMeeplesException,
      UnMeeplableException {
    Center c = new Center(Feature.ENDPOINT);
    Edge road = new Edge(Feature.ROAD);
    Edge field = new Edge(Feature.FIELD);
    Tile farleft = new Tile(c, field, road, field, field, 0);
    Posn farleftP = new Posn(-5, 0);
    c = new Center(Feature.ROAD);
    road = new Edge(Feature.ROAD);
    Edge road2 = new Edge(Feature.ROAD);
    Tile left4 = new Tile(c, field, road, field, road2, 0);
    Posn left4P = new Posn(-4, 0);
    c = new Center(Feature.ROAD);
    road = new Edge(Feature.ROAD);
    road2 = new Edge(Feature.ROAD);
    Tile left3 = new Tile(c, field, road, field, road2, 0);
    Posn left3P = new Posn(-3, 0);
    c = new Center(Feature.ROAD);
    road = new Edge(Feature.ROAD);
    road2 = new Edge(Feature.ROAD);
    Tile left2 = new Tile(c, field, road, field, road2, 0);
    Posn left2P = new Posn(-2, 0);
    c = new Center(Feature.ROAD);
    road = new Edge(Feature.ROAD);
    road2 = new Edge(Feature.ROAD);
    Tile left1 = new Tile(c, field, road, field, road2, 0);
    Posn left1P = new Posn(-1, 0);
    c = new Center(Feature.ROAD);
    road = new Edge(Feature.ROAD);
    road2 = new Edge(Feature.ROAD);
    Tile center = new Tile(c, field, road, field, road2, 0);
    Posn centerP = new Posn(0, 0);
    Player p1 = new Player(1, "p1");
    List<Player> players = new ArrayList<>();
    players.add(p1);
    Referee r = new Referee();
    r.setPlayers(players);
    r.place(farleftP, farleft);
    r.placeMeeple(farleftP, p1, Direction.RIGHT);
    r.place(left4P, left4);
    r.place(left3P, left3);
    r.place(left2P, left2);
    r.place(left1P, left1);
    r.place(centerP, center);
    List<Direction> directions = r.getBoard().validMeeples(centerP);
    assertTrue(directions.size() == 0);
  }
}
