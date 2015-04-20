package edu.brown.cs.scij.gametests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import edu.brown.cs.scij.game.Referee;
import edu.brown.cs.scij.tile.Tile;

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
    Tile t1 = null;
    Tile t2 = null;
    for (int i = 0; i < s1; i++) {
      t1 = r.drawTile();
      t2 = r2.drawTile();
      if (!t1.equals(t2)) {
        assertTrue(true);
        break;
      }
    }
  }

  @Test
  public void meepleTest() {
    // TODO cant do this till we do meeple
  }

  /*
   * public void cityScoringTest() throws InvalidEdgeException,
   * PosnTakenException{
   * Center c = new Center(Feature.FIELD);
   * Edge top = new Edge(Feature.FIELD);
   * Edge right = new Edge(Feature.CITY);
   * Edge bottom = new Edge(Feature.FIELD);
   * Edge left = new Edge(Feature.FIELD);
   * Tile t1 = new Tile(c, top, right, bottom, left, 0);
   * Tile t2 = new Tile(c, top, left, bottom, right, 0);
   * Player p = new Player(1, "Colby");
   * Referee r = new Referee();
   * Posn center = new Posn(0, 0);
   * Posn ri = new Posn(1, 0);
   * r.place(center, t1);
   * r.place(ri, t2);
   * t1.placeMeeple(p, );
   * assertTrue(r.scoreCity("something which indicates city on t1") == 4);
   * t2.placeMeeple(p, );
   * assertTrue(r.scoreCity("something which indicates city on t2") == 4);
   * r = new Referee();
   * Posn bo = new Posn(0, -1);
   * Posn le = new Posn(-1, 0);
   * Posn to = new Posn(0, 1);
   * Tile t3 = new Tile(c, right, top, bottom, left, 0);
   * Tile t4 = new Tile(c, top, left, right, bottom, 0);
   * c = new Center(Feature.CITY);
   * Tile t5 = new Tile(c, right, right, right, right, 0);
   * r.place(center, t5);
   * r.place(le, t1);
   * r.place(bo, t3);
   * r.place(to, t4);
   * r.place(ri, t2);
   * t1.placeMeeple(p, );
   * assertTrue(r.scoreCity("something which indicates city on t1") == 10);
   * t2.placeMeeple(p, );
   * assertTrue(r.scoreCity("something which indicates city on t2") == 10);
   * t3.placeMeeple(p, );
   * assertTrue(r.scoreCity("something which indicates city on t3") == 10);
   * t4.placeMeeple(p, );
   * assertTrue(r.scoreCity("something which indicates city on t4") == 10);
   * t5.placeMeeple(p, );
   * assertTrue(r.scoreCity("something whi1ch indicates city on t5") == 10);
   * //TODO shield stuff
   * }
   * 
   * public void roadScoringTest() throws InvalidEdgeException{
   * Center c = new Center(Feature.ENDPOINT);
   * Edge top = new Edge(Feature.FIELD);
   * Edge right = new Edge(Feature.FIELD);
   * Edge bottom = new Edge(Feature.ROAD);
   * Edge left = new Edge(Feature.FIELD);
   * Tile botRoad = new Tile(c, top, right, bottom, left, 0);
   * Referee r = new Referee();
   * Posn to = new Posn(0, 1);
   * Posn bo = new Posn(0,0);
   * r.place(to, botRoad);
   * Tile topRoad = new Tile(c, bottom, right, top, left, 0);
   * r.place(bo, topRoad);
   * Player p = new Player(1, "Colby");
   * t1.placeMeeple(p, );
   * assertTrue(r.scoreRoad("something which indicates road on botRoad") == 2);
   * t2.placeMeeple(p, );
   * assertTrue(r.scoreRoad("something which indicates road on topRoad") == 2);
   * r = new Referee();
   * c = new Center(Feature.ROAD);
   * Tile roadThrough = new Tile(c, bottom, right, bottom, left, 0);
   * Posn ct = new Posn(0,0);
   * bo = new Posn(0, -1);
   * r.place(to, botRoad);
   * r.place(ct, roadThrough);
   * r.place(bo, topRoad);
   * botRoad.placeMeeple(p, );
   * assertTrue(r.scoreRoad("something which indicates road on botRoad") == 3);
   * topRoad.placeMeeple(p, );
   * assertTrue(r.scoreRoad("something which indicates road on topRoad") == 3);
   * roadThrough.placeMeeple(p, );
   * assertTrue(r.scoreRoad("something which indicates road on roadThrough") ==
   * 3);
   * r = new Referee();
   * Tile horizRoad = new Tile(c, right, bottom, right, bottom, 0);
   * Tile topRight = new Tile(c, bottom, bottom, right, right, 0);
   * Tile botLeft = new Tile(c, right, right, bottom, bottom, 0);
   * r.place(to, botRoad);
   * r.place(ct, roadThrough);
   * r.place(bo, topRight);
   * r.place(new Posn(1, -1), horizRoad);
   * r.place(new Posn(2, -1), botLeft);
   * r.place(new Posn(2, -2), topRoad);
   * botRoad.placeMeeple(p, );
   * assertTrue(r.scoreRoad("something which indicates road on botRoad") == 6);
   * topRoad.placeMeeple(p, );
   * assertTrue(r.scoreRoad("something which indicates road on topRoad") == 6);
   * roadThrough.placeMeeple(p, );
   * assertTrue(r.scoreRoad("something which indicates road on roadThrough") ==
   * 6);
   * horizRoad.placeMeeple(p, );
   * assertTrue(r.scoreRoad("something which indicates road on horizRoad") ==
   * 6);
   * botLeft.placeMeeple(p, );
   * assertTrue(r.scoreRoad("something which indicates road on botLeft") == 6);
   * topRight.placeMeeple(p, );
   * assertTrue(r.scoreRoad("something which indicates road on topRight") == 6);
   * }
   * 
   * public void monasteryScoringTest() throws InvalidEdgeException,
   * PosnTakenException{
   * Center c = new Center(Feature.MONASTERY);
   * Edge top = new Edge(Feature.FIELD);
   * Edge left = new Edge(Feature.FIELD);
   * Edge bottom = new Edge(Feature.FIELD);
   * Edge right = new Edge(Feature.FIELD);
   * Tile t = new Tile(c, top, left, bottom, right, 0);
   * Referee r = new Referee();
   * Posn tl = new Posn(-1,1);
   * Posn tc = new Posn(0,1);
   * Posn tr = new Posn(1,1);
   * Posn ml = new Posn(-1,0);
   * Posn mc = new Posn(0,0);
   * Posn mr = new Posn(1,0);
   * Posn bl = new Posn(-1,-1);
   * Posn bc = new Posn(0,-1);
   * Posn br = new Posn(1,-1);
   * r.place(mc, t);
   * r.place(tl, new Tile(c, top, left, bottom, right, 0));
   * r.place(tc, new Tile(c, top, left, bottom, right, 0));
   * r.place(tr, new Tile(c, top, left, bottom, right, 0));
   * r.place(ml, new Tile(c, top, left, bottom, right, 0));
   * r.place(mr, new Tile(c, top, left, bottom, right, 0));
   * r.place(bl, new Tile(c, top, left, bottom, right, 0));
   * r.place(bc, new Tile(c, top, left, bottom, right, 0));
   * r.place(br, new Tile(c, top, left, bottom, right, 0));
   * Player p = new Player(1, "colby");
   * t.placeMeeple(p, );
   * assertTrue(r.scoreMonastery("indicates monastery") == 9);
   * }
   * 
   * @Test
   * public void gameOverCityScoringTest() {
   * // do cities have to be completed?
   * }
   * 
   * @Test
   * public void gameOverRoadScoringTest() {
   * // do roads have to be completed?
   * }
   * 
   * public void gameOverMonasteryScoringTest(){
   * Center c = new Center(Feature.MONASTERY);
   * Edge top = new Edge(Feature.FIELD);
   * Edge left = new Edge(Feature.FIELD);
   * Edge bottom = new Edge(Feature.FIELD);
   * Edge right = new Edge(Feature.FIELD);
   * Tile t = new Tile(c, top, left, bottom, right, 0);
   * Referee r = new Referee();
   * Posn tc = new Posn(0,1);
   * Posn tr = new Posn(1,1);
   * Posn ml = new Posn(-1,0);
   * Posn mc = new Posn(0,0);
   * Posn bl = new Posn(-1,-1);
   * Posn bc = new Posn(0,-1);
   * r.place(mc, t);
   * r.place(tc, new Tile(c, top, left, bottom, right, 0));
   * r.place(tr, new Tile(c, top, left, bottom, right, 0));
   * r.place(ml, new Tile(c, top, left, bottom, right, 0));
   * r.place(bl, new Tile(c, top, left, bottom, right, 0));
   * r.place(bc, new Tile(c, top, left, bottom, right, 0));
   * Player p = new Player(1, "colby");
   * t.placeMeeple(p, );
   * assertTrue(r.scoreMonastery("indicates monastery") == 6);
   * }
   */
}
