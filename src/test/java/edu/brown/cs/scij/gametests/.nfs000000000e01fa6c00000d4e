package edu.brown.cs.scij.gametests;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

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

public class RefereeTest {

  @Test
  public void constructorTest() {
    Referee r = new Referee();
    assertTrue(r.getTurnNumber() == 0);
    assertTrue(r.getBoard().getBoard().isEmpty());
    assertTrue(r.getPlayers().size() == 0);
    assertTrue(r.getDeck().isEmpty() == false);
  }

  @Test
  public void buildDeckTest() {
    Referee r = new Referee();
    Referee r2 = new Referee();
    int s1 = r.getDeck().getTiles().size();
    int s2 = r2.getDeck().getTiles().size();
    assertTrue(s1 == s2);
  }

  // @Test
  public void placeMeepleCityTest() throws InvalidEdgeException,
      NullTileException,
      OutOfMeeplesException, UnMeeplableException, PosnTakenException {
    Player p1 = new Player(1, "p1");
    Posn center = new Posn(0, 0);
    Referee r = new Referee();
    List<Player> pList = new ArrayList<>();
    pList.add(p1);
    r.setPlayers(pList);
    Center c = new Center(Feature.CITY);
    Edge top = new Edge(Feature.FIELD);
    Edge right = new Edge(Feature.CITY);
    Edge bottom = new Edge(Feature.CITY);
    Edge left = new Edge(Feature.FIELD);
    Tile t = new Tile(c, top, right, bottom, left, 0);
    r.place(center, t);
    int numMeeples = p1.getNumMeeples();
    assertTrue(t.getRight().getMeeple() == null);
    r.placeMeeple(center, p1, Direction.RIGHT);
    assertTrue(numMeeples - 1 == p1.getNumMeeples());
    assertTrue(t.getRight().getMeeple() != null);
    assertTrue(t.getRight().getMeeple().getPlayer().getId() == p1.getId());
    try {
      r.placeMeeple(center, p1, Direction.LEFT);
      assertTrue(false);
    } catch (UnMeeplableException ume) {
      try {
        r.placeMeeple(center, p1, Direction.DOWN);
        assertTrue(false);
      } catch (UnMeeplableException ume2) {
        assertTrue(true);
      }
    }
  }

  @Test
  public void placeMeepleRoadTest() throws InvalidEdgeException,
      NullTileException,
      OutOfMeeplesException, PosnTakenException {

    Player p1 = new Player(1, "p1");
    Posn center = new Posn(0, 0);
    Referee r = new Referee();
    List<Player> pList = new ArrayList<>();
    pList.add(p1);
    r.setPlayers(pList);
    Center c = new Center(Feature.ROAD);
    Edge top = new Edge(Feature.FIELD);
    Edge right = new Edge(Feature.ROAD);
    Edge bottom = new Edge(Feature.FIELD);
    Edge left = new Edge(Feature.ROAD);
    Tile t = new Tile(c, top, right, bottom, left, 0);
    r.place(center, t);
    int numMeeples = p1.getNumMeeples();
    assertTrue(t.getRight().getMeeple() == null);
    try {
      r.placeMeeple(center, p1, Direction.RIGHT);
      assertTrue(numMeeples - 1 == p1.getNumMeeples());
      assertTrue(t.getRight().getMeeple() != null);
      assertTrue(t.getRight().getMeeple().getPlayer().getId() == p1.getId());
      r.placeMeeple(center, p1, Direction.DOWN);
      assertTrue(false);
    } catch (UnMeeplableException ume) {
      try {
        r.placeMeeple(center, p1, Direction.LEFT);
        assertTrue(false);
      } catch (UnMeeplableException ume2) {
        assertTrue(true);
      }
    }
  }

  // @Test
  public void cityScoringTest() throws InvalidEdgeException,
      PosnTakenException, NullTileException, OutOfMeeplesException,
      UnMeeplableException {
    Center c = new Center(Feature.FIELD);
    Edge top = new Edge(Feature.FIELD);
    Edge right = new Edge(Feature.CITY);
    Edge bottom = new Edge(Feature.FIELD);
    Edge left = new Edge(Feature.FIELD);
    Tile t1 = new Tile(c, top, right, bottom, left, 0);
    Tile t2 = new Tile(c, top, left, bottom, right, 0);
    Player p = new Player(1, "Colby");
    Referee r = new Referee();
    List<Player> pList = new ArrayList<>();
    pList.add(p);
    r.setPlayers(pList);
    Posn center = new Posn(0, 0);
    Posn ri = new Posn(1, 0);
    r.place(center, t1);
    r.place(ri, t2);
    r.placeMeeple(center, p, Direction.RIGHT);
    int score = p.getScore();
    r.scoreCity(center);
    assertTrue(score + 4 == p.getScore());
    r.placeMeeple(ri, p, Direction.LEFT);
    score = p.getScore();
    r.scoreCity(ri);
    assertTrue(score + 4 == p.getScore());
    r = new Referee();
    pList = new ArrayList<>();
    pList.add(p);
    r.setPlayers(pList);
    Posn bo = new Posn(0, -1);
    Posn le = new Posn(-1, 0);
    Posn to = new Posn(0, 1);
    Tile t3 = new Tile(c, right, top, bottom, left, 0);
    Tile t4 = new Tile(c, top, left, right, bottom, 0);
    c = new Center(Feature.CITY);
    Tile t5 = new Tile(c, right, right, right, right, 0);
    r.place(center, t5);
    r.place(le, t1);
    r.place(bo, t3);
    r.place(to, t4);
    r.place(ri, t2);
    r.placeMeeple(le, p, Direction.RIGHT);
    score = p.getScore();
    r.scoreCity(le);
    assertTrue(score + 10 == p.getScore());
    r.placeMeeple(ri, p, Direction.LEFT);
    score = p.getScore();
    r.scoreCity(ri);
    assertTrue(score + 10 == p.getScore());
    r.placeMeeple(bo, p, Direction.UP);
    score = p.getScore();
    r.scoreCity(bo);
    assertTrue(score + 10 == p.getScore());
    r.placeMeeple(to, p, Direction.DOWN);
    score = p.getScore();
    r.scoreCity(to);
    assertTrue(score + 10 == p.getScore());
    r.placeMeeple(center, p, Direction.CENTER);
    score = p.getScore();
    r.scoreCity(center);
    assertTrue(score + 10 == p.getScore());
    r.placeMeeple(center, p, Direction.LEFT);
    score = p.getScore();
    r.scoreCity(center);
    assertTrue(score + 10 == p.getScore());
    r.placeMeeple(center, p, Direction.RIGHT);
    score = p.getScore();
    r.scoreCity(center);
    assertTrue(score + 10 == p.getScore());
    r.placeMeeple(center, p, Direction.UP);
    score = p.getScore();
    r.scoreCity(center);
    assertTrue(score + 10 == p.getScore());
    r.placeMeeple(center, p, Direction.DOWN);
    score = p.getScore();
    r.scoreCity(center);
    assertTrue(score + 10 == p.getScore());
  }

  // @Test
  public void oneShieldCityTest() throws InvalidEdgeException,
      PosnTakenException, NullTileException, OutOfMeeplesException,
      UnMeeplableException {
    Center c = new Center(Feature.FIELD);
    Edge top = new Edge(Feature.CITY);
    Edge right = new Edge(Feature.FIELD);
    Edge bottom = new Edge(Feature.FIELD);
    Edge left = new Edge(Feature.FIELD);
    Tile topCity = new Tile(c, top, right, bottom, left, 1);
    Tile bottomCity = new Tile(c, bottom, right, top, left, 0);
    Referee r = new Referee();
    Posn topPosn = new Posn(-4, -4);
    Posn bottomPosn = new Posn(-4, -5);
    r.place(topPosn, bottomCity);
    r.place(bottomPosn, topCity);
    Player p = new Player(1, "Colby");
    List<Player> pList = new ArrayList<>();
    pList.add(p);
    r.setPlayers(pList);
    r.placeMeeple(topPosn, p, Direction.DOWN);
    int score = p.getScore();
    r.scoreCity(topPosn);
    assertTrue(score + 6 == p.getScore());
  }

  @Test
  public void roadScoringTest() throws InvalidEdgeException,
      PosnTakenException, NullTileException, OutOfMeeplesException,
      UnMeeplableException {
    Center c = new Center(Feature.ENDPOINT);
    Edge field = new Edge(Feature.FIELD);
    Edge roadOnBottom = new Edge(Feature.ROAD);
    Tile botRoad = new Tile(c, field, field, roadOnBottom, field, 0);
    Referee r = new Referee();
    Posn to = new Posn(0, 1);
    Posn bo = new Posn(0, 0);
    r.place(to, botRoad);
    Edge roadOnTop = new Edge(Feature.ROAD);
    Tile topRoad = new Tile(c, roadOnTop, field, field, field, 0);
    r.place(bo, topRoad);
    Player p = new Player(1, "Colby");
    List<Player> pList = new ArrayList<>();
    pList.add(p);
    r.setPlayers(pList);
    r.placeMeeple(to, p, Direction.DOWN);
    int score = p.getScore();
    r.scoreRoad(to);
    assertTrue(score + 2 == p.getScore());
    r.placeMeeple(bo, p, Direction.UP);
    score = p.getScore();
    r.scoreRoad(bo);
    assertTrue(score + 2 == p.getScore());
    r = new Referee();
    pList = new ArrayList<>();
    pList.add(p);
    r.setPlayers(pList);
    c = new Center(Feature.ROAD);
    Edge roadCenterTop = new Edge(Feature.ROAD);
    Edge roadCenterBottom = new Edge(Feature.ROAD);
    Tile roadThrough =
        new Tile(c, roadCenterTop, field, roadCenterBottom, field, 0);
    Posn ct = new Posn(0, 0);
    bo = new Posn(0, -1);
    r.place(to, botRoad);
    r.place(ct, roadThrough);
    r.place(bo, topRoad);
    r.placeMeeple(to, p, Direction.DOWN);
    score = p.getScore();
    r.scoreRoad(to);
    assertTrue(score + 3 == p.getScore());
    r = new Referee();
    roadOnTop = new Edge(Feature.ROAD);
    roadOnBottom = new Edge(Feature.ROAD);
    topRoad = new Tile(c, roadOnTop, field, field, field, 0);
    c = new Center(Feature.ENDPOINT);
    botRoad = new Tile(c, field, field, roadOnBottom, field, 0);
    c = new Center(Feature.ROAD);
    roadCenterTop = new Edge(Feature.ROAD);
    roadCenterBottom = new Edge(Feature.ROAD);
    roadThrough =
        new Tile(c, roadCenterTop, field, roadCenterBottom, field, 0);
    pList = new ArrayList<>();
    pList.add(p);
    r.setPlayers(pList);
    Edge road = new Edge(Feature.ROAD);
    Edge road2 = new Edge(Feature.ROAD);
    Edge road3 = new Edge(Feature.ROAD);
    Edge road4 = new Edge(Feature.ROAD);
    Edge road5 = new Edge(Feature.ROAD);
    Edge road6 = new Edge(Feature.ROAD);
    c = new Center(Feature.ROAD);
    Tile horizRoad = new Tile(c, field, road, field, road2, 0);
    c = new Center(Feature.ROAD);
    Tile topRight = new Tile(c, road6, road5, field, field, 0);
    c = new Center(Feature.ROAD);
    Tile botLeft = new Tile(c, field, field, road4, road3, 0);
    r.place(to, botRoad);
    r.place(ct, roadThrough);
    r.place(bo, topRight);
    Posn horizPosn = new Posn(1, -1);
    r.place(horizPosn, horizRoad);
    Posn botLeftPosn = new Posn(2, -1);
    r.place(botLeftPosn, botLeft);
    Posn topRoadPosn = new Posn(2, -2);
    r.place(topRoadPosn, topRoad);
    r.placeMeeple(botLeftPosn, p, Direction.LEFT);
    score = p.getScore();
    r.scoreRoad(botLeftPosn);
    assertTrue(score + 6 == p.getScore());

  }

  @Test
  public void monasteryScoringTest() throws InvalidEdgeException,
      PosnTakenException, NullTileException, OutOfMeeplesException,
      UnMeeplableException {
    Center c = new Center(Feature.MONASTERY);
    Edge top = new Edge(Feature.FIELD);
    Edge left = new Edge(Feature.FIELD);
    Edge bottom = new Edge(Feature.FIELD);
    Edge right = new Edge(Feature.FIELD);
    Tile t = new Tile(c, top, left, bottom, right, 0);
    Referee r = new Referee();
    Posn tl = new Posn(-1, 1);
    Posn tc = new Posn(0, 1);
    Posn tr = new Posn(1, 1);
    Posn ml = new Posn(-1, 0);
    Posn mc = new Posn(0, 0);
    Posn mr = new Posn(1, 0);
    Posn bl = new Posn(-1, -1);
    Posn bc = new Posn(0, -1);
    Posn br = new Posn(1, -1);
    r.place(mc, t);
    r.place(tl, new Tile(c, top, left, bottom, right, 0));
    r.place(tc, new Tile(c, top, left, bottom, right, 0));
    r.place(tr, new Tile(c, top, left, bottom, right, 0));
    r.place(ml, new Tile(c, top, left, bottom, right, 0));
    r.place(mr, new Tile(c, top, left, bottom, right, 0));
    r.place(bl, new Tile(c, top, left, bottom, right, 0));
    r.place(bc, new Tile(c, top, left, bottom, right, 0));
    r.place(br, new Tile(c, top, left, bottom, right, 0));
    Player p = new Player(1, "colby");
    List<Player> pList = new ArrayList<>();
    pList.add(p);
    r.setPlayers(pList);
    r.placeMeeple(mc, p, Direction.CENTER);
    int score = p.getScore();
    r.scoreMonastery(mc);
    assertTrue(score + 9 == p.getScore());
  }

  // @Test
  public void gameOverCityScoringTest() throws InvalidEdgeException,
      PosnTakenException, NullTileException, OutOfMeeplesException,
      UnMeeplableException {
    Center c = new Center(Feature.FIELD);
    Edge top = new Edge(Feature.FIELD);
    Edge right = new Edge(Feature.CITY);
    Edge bottom = new Edge(Feature.FIELD);
    Edge left = new Edge(Feature.FIELD);
    Tile rightCity = new Tile(c, top, right, bottom, left, 0);
    c = new Center(Feature.CITY);
    Tile horizCity1 = new Tile(c, top, right, bottom, right, 0);
    Tile horizCity2 = new Tile(c, top, right, bottom, right, 0);
    Tile horizCity3 = new Tile(c, top, right, bottom, right, 0);
    Tile horizCity4 = new Tile(c, top, right, bottom, right, 0);
    Referee r = new Referee();
    Posn center = new Posn(0, 0);
    r.place(center, rightCity);
    r.place(new Posn(1, 0), horizCity1);
    r.place(new Posn(2, 0), horizCity2);
    r.place(new Posn(3, 0), horizCity3);
    r.place(new Posn(4, 0), horizCity4);
    Player p = new Player(1, "c");
    List<Player> pList = new ArrayList<>();
    pList.add(p);
    r.setPlayers(pList);
    r.placeMeeple(center, p, Direction.RIGHT);
    int score = p.getScore();
    r.scoreCityEndgame();
    assertTrue(score + 5 == p.getScore());
  }

  // @Test
  public void gameOverCityShieldScoringTest() throws PosnTakenException,
      InvalidEdgeException, NullTileException, OutOfMeeplesException,
      UnMeeplableException {
    Center c = new Center(Feature.FIELD);
    Edge top = new Edge(Feature.FIELD);
    Edge right = new Edge(Feature.CITY);
    Edge bottom = new Edge(Feature.FIELD);
    Edge left = new Edge(Feature.FIELD);
    Tile rightCity = new Tile(c, top, right, bottom, left, 0);
    c = new Center(Feature.CITY);
    Tile horizCity1 = new Tile(c, top, right, bottom, right, 1);
    Tile horizCity2 = new Tile(c, top, right, bottom, right, 0);
    Tile horizCity3 = new Tile(c, top, right, bottom, right, 1);
    Tile horizCity4 = new Tile(c, top, right, bottom, right, 0);
    Referee r = new Referee();
    Posn center = new Posn(0, 0);
    r.place(center, rightCity);
    r.place(new Posn(1, 0), horizCity1);
    r.place(new Posn(2, 0), horizCity2);
    r.place(new Posn(3, 0), horizCity3);
    r.place(new Posn(4, 0), horizCity4);
    Player p = new Player(1, "c");
    List<Player> pList = new ArrayList<>();
    pList.add(p);
    r.setPlayers(pList);
    r.placeMeeple(center, p, Direction.RIGHT);
    int score = p.getScore();
    r.scoreCityEndgame();
    assertTrue(score + 7 == p.getScore());
  }

  @Test
  public void gameOverRoadScoringTest() throws InvalidEdgeException,
      PosnTakenException, NullTileException, OutOfMeeplesException,
      UnMeeplableException {
    Center c = new Center(Feature.ENDPOINT);
    Center c2 = new Center(Feature.ROAD);
    Center c3 = new Center(Feature.ROAD);
    Center c4 = new Center(Feature.ROAD);
    Center c5 = new Center(Feature.ROAD);
    Edge road1 = new Edge(Feature.ROAD);
    Edge road2 = new Edge(Feature.ROAD);
    Edge road3 = new Edge(Feature.ROAD);
    Edge road4 = new Edge(Feature.ROAD);
    Edge road5 = new Edge(Feature.ROAD);
    Edge road6 = new Edge(Feature.ROAD);
    Edge road7 = new Edge(Feature.ROAD);
    Edge road8 = new Edge(Feature.ROAD);
    Edge field = new Edge(Feature.FIELD);
    Tile botRoad = new Tile(c, field, field, road1, field, 0);
    c = new Center(Feature.ROAD);
    Tile topRight = new Tile(c2, road2, road3, field, field, 0);
    Tile botLeft = new Tile(c3, field, field, road4, road5, 0);
    Tile vert = new Tile(c4, road6, field, road7, field, 0);
    Tile topRoad = new Tile(c5, road8, field, field, field, 0);
    Referee r = new Referee();
    Posn center = new Posn(0, 0);
    r.place(center, botRoad);
    r.place(new Posn(0, -1), topRight);
    r.place(new Posn(1, -1), botLeft);
    r.place(new Posn(1, -2), vert);
    r.place(new Posn(1, -3), topRoad);
    Player p = new Player(1, "c");
    List<Player> pList = new ArrayList<>();
    pList.add(p);
    r.setPlayers(pList);
    r.placeMeeple(center, p, Direction.DOWN);
    int score = p.getScore();
    r.scoreRoadEndgame();
    assertTrue(score + 5 == p.getScore());
  }

  @Test
  public void twoPlayerRoadCase() throws InvalidEdgeException,
      PosnTakenException, NullTileException, OutOfMeeplesException,
      UnMeeplableException {
    Center c = new Center(Feature.ENDPOINT);
    Edge roadRight = new Edge(Feature.ROAD);
    Edge roadLeft = new Edge(Feature.ROAD);
    Edge field = new Edge(Feature.FIELD);
    Tile rightRoad = new Tile(c, field, roadRight, field, field, 0);
    Tile leftRoad = new Tile(c, field, field, field, roadLeft, 0);
    Player p1 = new Player(1, "p1");
    Player p2 = new Player(2, "p2");
    Referee r = new Referee();
    List<Player> pList = new ArrayList<>();
    pList.add(p1);
    pList.add(p2);
    r.setPlayers(pList);
    Posn left = new Posn(-1, 0);
    Posn right = new Posn(1, 0);
    Posn center = new Posn(0, 0);
    r.place(left, rightRoad);
    r.place(right, leftRoad);
    r.placeMeeple(left, p1, Direction.RIGHT);
    r.placeMeeple(right, p2, Direction.LEFT);
    int score1 = p1.getScore();
    int score2 = p2.getScore();
    c = new Center(Feature.ROAD);
    Edge centerRoadRight = new Edge(Feature.ROAD);
    Edge centerRoadLeft = new Edge(Feature.ROAD);
    Tile centerR =
        new Tile(c, field, centerRoadRight, field, centerRoadLeft, 0);
    r.place(center, centerR);
    r.scoreRoad(center);
    // System.out.println("score: " + p1.getScore() + " was: " + score1);
    assertTrue(score1 + 3 == p1.getScore());
    assertTrue(score2 + 3 == p2.getScore());
  }

  // @Test
  public void twoToOneCityTest() throws InvalidEdgeException,
      PosnTakenException, NullTileException, OutOfMeeplesException,
      UnMeeplableException {
    Center c = new Center(Feature.FIELD);
    Edge city = new Edge(Feature.CITY);
    Edge field = new Edge(Feature.FIELD);
    Tile left = new Tile(c, field, city, field, field, 0);
    Tile top = new Tile(c, field, field, city, field, 0);
    Tile right = new Tile(c, field, field, field, city, 0);
    Tile bottom = new Tile(c, city, field, field, field, 0);
    c = new Center(Feature.CITY);
    Tile center = new Tile(c, city, city, city, city, 0);
    Player p1 = new Player(1, "p1");
    Player p2 = new Player(2, "p2");
    Referee r = new Referee();
    List<Player> pList = new ArrayList<>();
    pList.add(p1);
    pList.add(p2);
    r.setPlayers(pList);
    Posn leftP = new Posn(-1, 0);
    Posn topP = new Posn(0, 1);
    Posn rightP = new Posn(1, 0);
    Posn centerP = new Posn(0, 0);
    Posn bottomP = new Posn(0, -1);
    r.place(leftP, left);
    r.placeMeeple(leftP, p1, Direction.RIGHT);
    r.place(topP, top);
    r.placeMeeple(topP, p1, Direction.DOWN);
    r.place(rightP, right);
    r.placeMeeple(rightP, p2, Direction.LEFT);
    int score1 = p1.getScore();
    int score2 = p2.getScore();
    r.place(centerP, center);
    r.place(bottomP, bottom);
    r.scoreCity(bottomP);
    assertTrue(score1 + 10 == p1.getScore());
    assertTrue(score2 == p2.getScore());
  }

  // @Test
  public void threeToOneCityTest() throws InvalidEdgeException,
      PosnTakenException, NullTileException, OutOfMeeplesException,
      UnMeeplableException {
    Center c = new Center(Feature.FIELD);
    Edge city = new Edge(Feature.CITY);
    Edge field = new Edge(Feature.FIELD);
    Tile left = new Tile(c, field, city, field, field, 0);
    Tile top = new Tile(c, field, field, city, field, 0);
    Tile right = new Tile(c, field, field, field, city, 0);
    Tile bottom = new Tile(c, city, field, field, field, 0);
    c = new Center(Feature.CITY);
    Tile center = new Tile(c, city, city, city, city, 0);
    Player p1 = new Player(1, "p1");
    Player p2 = new Player(2, "p2");
    Referee r = new Referee();
    List<Player> pList = new ArrayList<>();
    pList.add(p1);
    pList.add(p2);
    r.setPlayers(pList);
    Posn leftP = new Posn(-1, 0);
    Posn topP = new Posn(0, 1);
    Posn rightP = new Posn(1, 0);
    Posn centerP = new Posn(0, 0);
    Posn bottomP = new Posn(0, -1);
    r.place(leftP, left);
    r.placeMeeple(leftP, p1, Direction.RIGHT);
    r.place(topP, top);
    r.placeMeeple(topP, p1, Direction.DOWN);
    r.place(rightP, right);
    r.placeMeeple(rightP, p2, Direction.LEFT);
    int score1 = p1.getScore();
    int score2 = p2.getScore();
    r.place(bottomP, bottom);
    r.placeMeeple(bottomP, p1, Direction.UP);
    r.place(centerP, center);
    r.scoreCity(centerP);
    assertTrue(score1 + 10 == p1.getScore());
    assertTrue(score2 == p2.getScore());
  }

  // @Test
  public void twoToTwoCityTest() throws InvalidEdgeException,
      PosnTakenException, NullTileException, OutOfMeeplesException,
      UnMeeplableException {
    Center c = new Center(Feature.ENDPOINT);
    Edge city = new Edge(Feature.CITY);
    Edge field = new Edge(Feature.FIELD);
    Tile left = new Tile(c, field, city, field, field, 0);
    Tile top = new Tile(c, field, field, city, field, 0);
    Tile right = new Tile(c, field, field, field, city, 0);
    Tile bottom = new Tile(c, city, field, field, field, 0);
    c = new Center(Feature.CITY);
    Tile center = new Tile(c, city, city, city, city, 0);
    Player p1 = new Player(1, "p1");
    Player p2 = new Player(2, "p2");
    Referee r = new Referee();
    List<Player> pList = new ArrayList<>();
    pList.add(p1);
    pList.add(p2);
    r.setPlayers(pList);
    Posn leftP = new Posn(-1, 0);
    Posn topP = new Posn(0, 1);
    Posn rightP = new Posn(1, 0);
    Posn centerP = new Posn(0, 0);
    Posn bottomP = new Posn(0, -1);
    r.place(leftP, left);
    r.placeMeeple(leftP, p1, Direction.RIGHT);
    r.place(topP, top);
    r.placeMeeple(topP, p1, Direction.DOWN);
    r.place(rightP, right);
    r.placeMeeple(rightP, p2, Direction.LEFT);
    int score1 = p1.getScore();
    int score2 = p2.getScore();
    r.place(bottomP, bottom);
    r.placeMeeple(bottomP, p2, Direction.UP);
    r.place(centerP, center);
    r.scoreCity(centerP);
    assertTrue(score1 + 10 == p1.getScore());
    assertTrue(score2 + 10 == p2.getScore());
  }

  @Test
  public void twoToOneRoadTest() throws InvalidEdgeException,
      PosnTakenException, NullTileException, OutOfMeeplesException,
      UnMeeplableException {
    Center c = new Center(Feature.ENDPOINT);
    Center c2 = new Center(Feature.ENDPOINT);
    Center c3 = new Center(Feature.ROAD);
    Center c4 = new Center(Feature.ROAD);
    Center c5 = new Center(Feature.ROAD);
    Edge road1 = new Edge(Feature.ROAD);
    Edge road2 = new Edge(Feature.ROAD);
    Edge road3 = new Edge(Feature.ROAD);
    Edge road4 = new Edge(Feature.ROAD);
    Edge road5 = new Edge(Feature.ROAD);
    Edge road6 = new Edge(Feature.ROAD);
    Edge road7 = new Edge(Feature.ROAD);
    Edge road8 = new Edge(Feature.ROAD);
    Edge field = new Edge(Feature.FIELD);
    Tile left = new Tile(c, field, road1, field, field, 0);
    Tile right = new Tile(c2, field, field, field, road2, 0);
    Tile horiz1 = new Tile(c3, field, road3, field, road4, 0);
    Tile horiz2 = new Tile(c4, field, road5, field, road6, 0);
    Tile horiz3 = new Tile(c5, field, road7, field, road8, 0);
    c = new Center(Feature.CITY);
    Player p1 = new Player(1, "PLAYER1");
    Player p2 = new Player(2, "PLAYER2");
    Referee r = new Referee();
    List<Player> pList = new ArrayList<>();
    pList.add(p1);
    pList.add(p2);
    r.setPlayers(pList);
    Posn leftP = new Posn(-2, 0);
    Posn leftCenterP = new Posn(-1, 0);
    Posn centerP = new Posn(0, 0);
    Posn rightCenterP = new Posn(1, 0);
    Posn rightP = new Posn(2, 0);
    r.place(leftP, left);
    r.placeMeeple(leftP, p1, Direction.RIGHT);
    r.place(centerP, horiz2);
    r.placeMeeple(centerP, p2, Direction.CENTER);
    r.place(rightP, right);
    r.placeMeeple(rightP, p1, Direction.LEFT);
    int score1 = p1.getScore();
    int score2 = p2.getScore();
    r.place(leftCenterP, horiz1);
    r.place(rightCenterP, horiz3);
    r.scoreRoad(rightCenterP);
    assertTrue(score1 + 5 == p1.getScore());
    assertTrue(score2 == p2.getScore());
  }

  @Test
  public void threeToOneRoadTest() throws InvalidEdgeException,
      PosnTakenException, NullTileException, OutOfMeeplesException,
      UnMeeplableException {
    Center c = new Center(Feature.ENDPOINT);
    Center c2 = new Center(Feature.ENDPOINT);
    Center c3 = new Center(Feature.ROAD);
    Center c4 = new Center(Feature.ROAD);
    Center c5 = new Center(Feature.ROAD);
    Center c6 = new Center(Feature.ROAD);
    Center c7 = new Center(Feature.ROAD);
    Edge road = new Edge(Feature.ROAD);
    Edge road2 = new Edge(Feature.ROAD);
    Edge road3 = new Edge(Feature.ROAD);
    Edge road4 = new Edge(Feature.ROAD);
    Edge road5 = new Edge(Feature.ROAD);
    Edge road6 = new Edge(Feature.ROAD);
    Edge road7 = new Edge(Feature.ROAD);
    Edge road8 = new Edge(Feature.ROAD);
    Edge road9 = new Edge(Feature.ROAD);
    Edge road10 = new Edge(Feature.ROAD);
    Edge road11 = new Edge(Feature.ROAD);
    Edge road12 = new Edge(Feature.ROAD);
    Edge field = new Edge(Feature.FIELD);
    Tile left = new Tile(c, field, road, field, field, 0);
    Tile right = new Tile(c2, field, field, field, road2, 0);
    Tile horiz1 = new Tile(c3, field, road8, field, road3, 0);
    Tile horiz2 = new Tile(c4, field, road9, field, road4, 0);
    Tile horiz3 = new Tile(c5, field, road10, field, road5, 0);
    Tile horiz4 = new Tile(c6, field, road11, field, road6, 0);
    Tile horiz5 = new Tile(c7, field, road12, field, road7, 0);
    c = new Center(Feature.CITY);
    Player p1 = new Player(1, "p1");
    Player p2 = new Player(2, "p2");
    Referee r = new Referee();
    List<Player> pList = new ArrayList<>();
    pList.add(p1);
    pList.add(p2);
    r.setPlayers(pList);
    Posn leftLeftP = new Posn(-3, 0);
    Posn leftP = new Posn(-2, 0);
    Posn leftCenterP = new Posn(-1, 0);
    Posn centerP = new Posn(0, 0);
    Posn rightCenterP = new Posn(1, 0);
    Posn rightP = new Posn(2, 0);
    Posn rightRightP = new Posn(3, 0);
    r.place(leftLeftP, left);
    r.placeMeeple(leftLeftP, p1, Direction.RIGHT);
    r.place(leftCenterP, horiz2);
    r.placeMeeple(leftCenterP, p2, Direction.CENTER);
    r.place(rightCenterP, horiz4);
    r.placeMeeple(rightCenterP, p1, Direction.CENTER);
    r.place(rightRightP, right);
    r.placeMeeple(rightRightP, p1, Direction.LEFT);
    int score1 = p1.getScore();
    int score2 = p2.getScore();
    r.place(leftP, horiz1);
    r.place(centerP, horiz3);
    r.place(rightP, horiz5);
    r.scoreRoad(rightP);
    assertTrue(score1 + 7 == p1.getScore());
    assertTrue(score2 == p2.getScore());
  }

  @Test
  public void twoToTwoRoadTest() throws InvalidEdgeException,
      NullTileException, OutOfMeeplesException, UnMeeplableException,
      PosnTakenException {
    Center c = new Center(Feature.ENDPOINT);
    Center c2 = new Center(Feature.ENDPOINT);
    Center c3 = new Center(Feature.ROAD);
    Center c4 = new Center(Feature.ROAD);
    Center c5 = new Center(Feature.ROAD);
    Center c6 = new Center(Feature.ROAD);
    Center c7 = new Center(Feature.ROAD);
    Edge road = new Edge(Feature.ROAD);
    Edge road2 = new Edge(Feature.ROAD);
    Edge road3 = new Edge(Feature.ROAD);
    Edge road4 = new Edge(Feature.ROAD);
    Edge road5 = new Edge(Feature.ROAD);
    Edge road6 = new Edge(Feature.ROAD);
    Edge road7 = new Edge(Feature.ROAD);
    Edge road8 = new Edge(Feature.ROAD);
    Edge road9 = new Edge(Feature.ROAD);
    Edge road10 = new Edge(Feature.ROAD);
    Edge road11 = new Edge(Feature.ROAD);
    Edge road12 = new Edge(Feature.ROAD);
    Edge field = new Edge(Feature.FIELD);
    Tile left = new Tile(c, field, road, field, field, 0);
    Tile right = new Tile(c2, field, field, field, road2, 0);
    c = new Center(Feature.ROAD);
    Tile horiz1 = new Tile(c3, field, road3, field, road8, 0);
    Tile horiz2 = new Tile(c4, field, road4, field, road9, 0);
    Tile horiz3 = new Tile(c5, field, road5, field, road10, 0);
    Tile horiz4 = new Tile(c6, field, road6, field, road11, 0);
    Tile horiz5 = new Tile(c7, field, road7, field, road12, 0);
    Player p1 = new Player(1, "p1");
    Player p2 = new Player(2, "p2");
    Referee r = new Referee();
    List<Player> pList = new ArrayList<>();
    pList.add(p1);
    pList.add(p2);
    r.setPlayers(pList);
    Posn leftLeftP = new Posn(-3, 0);
    Posn leftP = new Posn(-2, 0);
    Posn leftCenterP = new Posn(-1, 0);
    Posn centerP = new Posn(0, 0);
    Posn rightCenterP = new Posn(1, 0);
    Posn rightP = new Posn(2, 0);
    Posn rightRightP = new Posn(3, 0);
    r.place(leftLeftP, left);
    r.placeMeeple(leftLeftP, p1, Direction.RIGHT);
    r.place(leftCenterP, horiz2);
    r.placeMeeple(leftCenterP, p2, Direction.CENTER);
    r.place(rightCenterP, horiz4);
    r.placeMeeple(rightCenterP, p1, Direction.CENTER);
    r.place(rightRightP, right);
    r.placeMeeple(rightRightP, p2, Direction.LEFT);
    int score1 = p1.getScore();
    int score2 = p2.getScore();
    r.place(leftP, horiz1);
    r.place(centerP, horiz3);
    r.place(rightP, horiz5);
    r.scoreRoad(rightP);
    assertTrue(score1 + 7 == p1.getScore());
    assertTrue(score2 + 7 == p2.getScore());
  }

  // @Test
  public void circularRoadTest() /*
                                  * throws InvalidEdgeException,
                                  * PosnTakenException, NullTileException,
                                  * OutOfMeeplesException,
                                  * UnMeeplableException
                                  */{

    try {
      Center c = new Center(Feature.ROAD);
      Center c1 = new Center(Feature.ROAD);
      Center c2 = new Center(Feature.ROAD);
      Center c3 = new Center(Feature.ROAD);
      Edge road = new Edge(Feature.ROAD);
      Edge road1 = new Edge(Feature.ROAD);
      Edge road2 = new Edge(Feature.ROAD);
      Edge road3 = new Edge(Feature.ROAD);
      Edge road4 = new Edge(Feature.ROAD);
      Edge road5 = new Edge(Feature.ROAD);
      Edge road6 = new Edge(Feature.ROAD);
      Edge road7 = new Edge(Feature.ROAD);
      Edge field = new Edge(Feature.FIELD);
      Tile topRight = new Tile(c, road, road1, field, field, 0);
      Tile bottomRight = new Tile(c1, field, road2, road3, field, 0);
      Tile bottomLeft = new Tile(c2, field, field, road4, road5, 0);
      Tile topLeft = new Tile(c3, road6, field, field, road7, 0);
      Posn topRightP = new Posn(-1, -1);
      Posn bottomRightP = new Posn(-1, 0);
      Posn bottomLeftP = new Posn(0, 0);
      Posn topLeftP = new Posn(0, -1);
      Referee r = new Referee();
      Player p = new Player(1, "p");
      List<Player> pList = new ArrayList<>();
      pList.add(p);
      r.setPlayers(pList);
      r.place(topRightP, topRight);
      r.placeMeeple(topRightP, p, Direction.UP);
      r.place(bottomRightP, bottomRight);
      r.place(bottomLeftP, bottomLeft);
      int score = p.getScore();
      r.place(topLeftP, topLeft);
      r.scoreRoad(topLeftP);
      assertTrue(score + 4 == p.getScore());
    } catch (InvalidEdgeException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (PosnTakenException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (NullTileException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (OutOfMeeplesException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (UnMeeplableException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  @Test
  public void circularTestWithEndpoint() throws InvalidEdgeException,
      PosnTakenException, NullTileException, OutOfMeeplesException,
      UnMeeplableException {
    System.out.println("*************************");
    Center c = new Center(Feature.ENDPOINT);
    Center c1 = new Center(Feature.ROAD);
    Center c2 = new Center(Feature.ROAD);
    Center c3 = new Center(Feature.ROAD);
    Edge road = new Edge(Feature.ROAD);
    Edge road1 = new Edge(Feature.ROAD);
    Edge road2 = new Edge(Feature.ROAD);
    Edge road3 = new Edge(Feature.ROAD);
    Edge road4 = new Edge(Feature.ROAD);
    Edge road5 = new Edge(Feature.ROAD);
    Edge road6 = new Edge(Feature.ROAD);
    Edge road7 = new Edge(Feature.ROAD);
    Edge field = new Edge(Feature.FIELD);
    Tile topRight = new Tile(c, road, road1, field, field, 0);
    Tile bottomRight = new Tile(c1, field, road2, road3, field, 0);
    Tile bottomLeft = new Tile(c2, field, field, road4, road5, 0);
    Tile topLeft = new Tile(c3, road6, field, field, road7, 0);
    Posn topRightP = new Posn(-1, -1);
    Posn bottomRightP = new Posn(-1, 0);
    Posn bottomLeftP = new Posn(0, 0);
    Posn topLeftP = new Posn(0, -1);
    Referee r = new Referee();
    Player p = new Player(1, "p");
    List<Player> pList = new ArrayList<>();
    pList.add(p);
    r.setPlayers(pList);
    r.place(topRightP, topRight);
    r.placeMeeple(topRightP, p, Direction.UP);
    r.place(bottomRightP, bottomRight);
    r.place(bottomLeftP, bottomLeft);
    int score = p.getScore();
    r.place(topLeftP, topLeft);
    r.scoreRoad(topLeftP);
    System.out.println(score);
    System.out.println(p.getScore());
    System.out.println("*************************");
    assertTrue(score + 4 == p.getScore());
  }

  // @Test
  public void twoPlayerCityCase() throws InvalidEdgeException,
      PosnTakenException, NullTileException, OutOfMeeplesException,
      UnMeeplableException {
    Center c = new Center(Feature.FIELD);
    Edge city = new Edge(Feature.CITY);
    Edge field = new Edge(Feature.FIELD);
    Tile bottomCity = new Tile(c, field, field, city, field, 0);
    Tile leftCity = new Tile(c, field, field, field, city, 0);
    c = new Center(Feature.CITY);
    Tile topRightCity = new Tile(c, city, city, field, field, 0);
    Posn up = new Posn(0, 1);
    Posn mid = new Posn(0, 0);
    Posn right = new Posn(1, 0);
    Referee r = new Referee();
    Player p1 = new Player(1, "p1");
    Player p2 = new Player(2, "p2");
    List<Player> pList = new ArrayList<>();
    pList.add(p1);
    pList.add(p2);
    r.setPlayers(pList);
    int score1 = p1.getScore();
    int score2 = p2.getScore();
    r.place(up, bottomCity);
    r.place(right, leftCity);
    r.placeMeeple(up, p1, Direction.DOWN);
    r.placeMeeple(right, p2, Direction.LEFT);
    r.place(mid, topRightCity);
    r.scoreCity(mid);
    assertTrue(score1 + 6 == p1.getScore());
    assertTrue(score2 + 6 == p2.getScore());
  }

  // @Test
  public void twoPlayerSameRoadCase() throws InvalidEdgeException,
      PosnTakenException, NullTileException, OutOfMeeplesException,
      UnMeeplableException {
    Center c = new Center(Feature.ENDPOINT);
    Edge road = new Edge(Feature.ROAD);
    Edge field = new Edge(Feature.FIELD);
    Tile rightRoad = new Tile(c, field, road, field, field, 0);
    Tile leftRoad = new Tile(c, field, field, field, road, 0);
    Player p = new Player(1, "p");
    Referee r = new Referee();
    List<Player> pList = new ArrayList<>();
    pList.add(p);
    r.setPlayers(pList);
    Posn left = new Posn(-1, 0);
    Posn right = new Posn(1, 0);
    Posn center = new Posn(0, 0);
    r.place(left, rightRoad);
    r.place(right, leftRoad);
    r.placeMeeple(left, p, Direction.RIGHT);
    r.placeMeeple(right, p, Direction.LEFT);
    int score = p.getScore();
    c = new Center(Feature.ROAD);
    Tile centerR = new Tile(c, field, road, field, road, 0);
    r.place(center, centerR);
    r.scoreRoad(center);
    assertTrue(score + 3 == p.getScore());
  }

  // @Test
  public void twoPlayerSameCityCase() throws PosnTakenException,
      InvalidEdgeException, NullTileException, OutOfMeeplesException,
      UnMeeplableException {
    Center c = new Center(Feature.FIELD);
    Edge city = new Edge(Feature.CITY);
    Edge field = new Edge(Feature.FIELD);
    Tile bottomCity = new Tile(c, field, field, city, field, 0);
    Tile leftCity = new Tile(c, field, field, field, city, 0);
    c = new Center(Feature.CITY);
    Tile topRightCity = new Tile(c, city, city, field, field, 0);
    Posn up = new Posn(0, 1);
    Posn mid = new Posn(0, 0);
    Posn right = new Posn(1, 0);
    Referee r = new Referee();
    Player p = new Player(1, "p");
    List<Player> pList = new ArrayList<>();
    pList.add(p);
    r.setPlayers(pList);
    int score = p.getScore();
    r.place(up, bottomCity);
    r.place(right, leftCity);
    r.placeMeeple(up, p, Direction.DOWN);
    r.placeMeeple(right, p, Direction.LEFT);
    r.place(mid, topRightCity);
    r.scoreCity(mid);
    assertTrue(score + 6 == p.getScore());
  }

  @Test
  public void gameOverMonasteryScoringTest() throws PosnTakenException,
      InvalidEdgeException, NullTileException, OutOfMeeplesException,
      UnMeeplableException {
    Center c = new Center(Feature.MONASTERY);
    Edge top = new Edge(Feature.FIELD);
    Edge left = new Edge(Feature.FIELD);
    Edge bottom = new Edge(Feature.FIELD);
    Edge right = new Edge(Feature.FIELD);
    Tile t = new Tile(c, top, left, bottom, right, 0);
    Referee r = new Referee();
    Posn tc = new Posn(0, 1);
    Posn tr = new Posn(1, 1);
    Posn ml = new Posn(-1, 0);
    Posn mc = new Posn(0, 0);
    Posn bl = new Posn(-1, -1);
    Posn bc = new Posn(0, -1);
    r.place(mc, t);
    c = new Center(Feature.FIELD);
    r.place(tc, new Tile(c, top, left, bottom, right, 0));
    r.place(tr, new Tile(c, top, left, bottom, right, 0));
    r.place(ml, new Tile(c, top, left, bottom, right, 0));
    r.place(bl, new Tile(c, top, left, bottom, right, 0));
    r.place(bc, new Tile(c, top, left, bottom, right, 0));
    Player p = new Player(1, "colby");
    List<Player> pList = new ArrayList<>();
    pList.add(p);
    r.setPlayers(pList);
    r.placeMeeple(mc, p, Direction.CENTER);
    int score = p.getScore();
    r.scoreMonasteryEndgame();
    assertTrue(score + 6 == p.getScore());
  }

}
