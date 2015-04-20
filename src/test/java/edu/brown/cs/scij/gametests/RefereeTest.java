package edu.brown.cs.scij.gametests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import edu.brown.cs.scij.game.Player;
import edu.brown.cs.scij.game.Posn;
import edu.brown.cs.scij.game.PosnTakenException;
import edu.brown.cs.scij.game.Referee;
import edu.brown.cs.scij.tile.Center;
import edu.brown.cs.scij.tile.Direction;
import edu.brown.cs.scij.tile.Edge;
import edu.brown.cs.scij.tile.Feature;
import edu.brown.cs.scij.tile.InvalidEdgeException;
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

  
  public void cityScoringTest() throws InvalidEdgeException,
    PosnTakenException{
	    Center c = new Center(Feature.FIELD);
	    Edge top = new Edge(Feature.FIELD);
	    Edge right = new Edge(Feature.CITY);
	    Edge bottom = new Edge(Feature.FIELD);
	    Edge left = new Edge(Feature.FIELD);
	    Tile t1 = new Tile(c, top, right, bottom, left, 0);
	    Tile t2 = new Tile(c, top, left, bottom, right, 0);
	    Player p = new Player(1, "Colby");
	    Referee r = new Referee();
	    Posn center = new Posn(0, 0);
	    Posn ri = new Posn(1, 0);
	    r.place(center, t1);
	    r.place(ri, t2);
	    r.placeMeeple(center, p, Direction.RIGHT);
	    int score = p.getScore();
	    r.scoreCity(center);
	    assertTrue(score+4 == p.getScore());
	    r.placeMeeple(ri, p, Direction.LEFT);
	    score = p.getScore();
	    r.scoreCity(ri);
	    assertTrue(score+4 == p.getScore());
	    r = new Referee();
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
	    //TODO shield stuff
    }
    
    public void roadScoringTest() throws InvalidEdgeException, PosnTakenException{
	    Center c = new Center(Feature.ENDPOINT);
	    Edge top = new Edge(Feature.FIELD);
	    Edge right = new Edge(Feature.FIELD);
	    Edge bottom = new Edge(Feature.ROAD);
	    Edge left = new Edge(Feature.FIELD);
	    Tile botRoad = new Tile(c, top, right, bottom, left, 0);
	    Referee r = new Referee();
	    Posn to = new Posn(0, 1);
	    Posn bo = new Posn(0,0);
	    r.place(to, botRoad);
	    Tile topRoad = new Tile(c, bottom, right, top, left, 0);
	    r.place(bo, topRoad);
	    Player p = new Player(1, "Colby");
	    r.placeMeeple(to, p, Direction.DOWN);
	    int score = p.getScore();
	    r.scoreRoad(to);
	    assertTrue(score + 2 == p.getScore());
	    r.placeMeeple(bo, p, Direction.UP);
	    score = p.getScore();
	    r.scoreRoad(bo);
	    assertTrue(score + 2 == p.getScore());
	    r = new Referee();
	    c = new Center(Feature.ROAD);
	    Tile roadThrough = new Tile(c, bottom, right, bottom, left, 0);
	    Posn ct = new Posn(0,0);
	    bo = new Posn(0, -1);
	    r.place(to, botRoad);
	    r.place(ct, roadThrough);
	    r.place(bo, topRoad);
	    r.placeMeeple(to, p, Direction.DOWN);
	    score = p.getScore();
	    r.scoreRoad(to);
	    assertTrue(score + 3 == p.getScore());
	    r.placeMeeple(bo, p, Direction.UP);
	    score = p.getScore();
	    r.scoreRoad(bo);
	    assertTrue(score + 3 == p.getScore());
	    r.placeMeeple(ct, p, Direction.CENTER);
	    score = p.getScore();
	    r.scoreRoad(ct);
	    assertTrue(score + 3 == p.getScore());
	    r.placeMeeple(ct, p, Direction.UP);
	    score = p.getScore();
	    r.scoreRoad(ct);
	    assertTrue(score + 3 == p.getScore());
	    r.placeMeeple(ct, p, Direction.DOWN);
	    score = p.getScore();
	    r.scoreRoad(ct);
	    assertTrue(score + 3 == p.getScore());
	    r = new Referee();
	    Tile horizRoad = new Tile(c, right, bottom, right, bottom, 0);
	    Tile topRight = new Tile(c, bottom, bottom, right, right, 0);
	    Tile botLeft = new Tile(c, right, right, bottom, bottom, 0);
	    r.place(to, botRoad);
	    r.place(ct, roadThrough);
	    r.place(bo, topRight);
	    Posn horizPosn = new Posn(1, -1);
	    r.place(horizPosn, horizRoad);
	    Posn botLeftPosn = new Posn(2, -1);
	    r.place(botLeftPosn, botLeft);
	    Posn topRoadPosn = new Posn(2, -2);
	    r.place(topRoadPosn, topRoad);
	    r.placeMeeple(to, p, Direction.DOWN);
	    score = p.getScore();
	    r.scoreRoad(to);
	    assertTrue(score + 6 == p.getScore());
	    r.placeMeeple(ct, p, Direction.UP);
	    score = p.getScore();
	    r.scoreRoad(ct);
	    assertTrue(score + 6 == p.getScore());
	    r.placeMeeple(ct, p, Direction.CENTER);
	    score = p.getScore();
	    r.scoreRoad(ct);
	    assertTrue(score + 6 == p.getScore());
	    r.placeMeeple(ct, p, Direction.DOWN);
	    score = p.getScore();
	    r.scoreRoad(ct);
	    assertTrue(score + 6 == p.getScore());
	    r.placeMeeple(bo, p, Direction.UP);
	    score = p.getScore();
	    r.scoreRoad(bo);
	    assertTrue(score + 6 == p.getScore());
	    r.placeMeeple(bo, p, Direction.CENTER);
	    score = p.getScore();
	    r.scoreRoad(bo);
	    assertTrue(score + 6 == p.getScore());
	    r.placeMeeple(bo, p, Direction.RIGHT);
	    score = p.getScore();
	    r.scoreRoad(bo);
	    assertTrue(score + 6 == p.getScore());
	    r.placeMeeple(horizPosn, p, Direction.LEFT);
	    score = p.getScore();
	    r.scoreRoad(horizPosn);
	    assertTrue(score + 6 == p.getScore());
	    r.placeMeeple(horizPosn, p, Direction.CENTER);
	    score = p.getScore();
	    r.scoreRoad(horizPosn);
	    assertTrue(score + 6 == p.getScore());
	    r.placeMeeple(horizPosn, p, Direction.RIGHT);
	    score = p.getScore();
	    r.scoreRoad(horizPosn);
	    assertTrue(score + 6 == p.getScore());
	    r.placeMeeple(botLeftPosn, p, Direction.LEFT);
	    score = p.getScore();
	    r.scoreRoad(botLeftPosn);
	    assertTrue(score + 6 == p.getScore());
	    r.placeMeeple(botLeftPosn, p, Direction.CENTER);
	    score = p.getScore();
	    r.scoreRoad(botLeftPosn);
	    assertTrue(score + 6 == p.getScore());
	    r.placeMeeple(botLeftPosn, p, Direction.DOWN);
	    score = p.getScore();
	    r.scoreRoad(botLeftPosn);
	    assertTrue(score + 6 == p.getScore());
	    r.placeMeeple(topRoadPosn, p, Direction.UP);
	    score = p.getScore();
	    r.scoreRoad(topRoadPosn);
	    assertTrue(score + 6 == p.getScore());
	    
    }
    
    public void monasteryScoringTest() throws InvalidEdgeException,
    PosnTakenException{
	    Center c = new Center(Feature.MONASTERY);
	    Edge top = new Edge(Feature.FIELD);
	    Edge left = new Edge(Feature.FIELD);
	    Edge bottom = new Edge(Feature.FIELD);
	    Edge right = new Edge(Feature.FIELD);
	    Tile t = new Tile(c, top, left, bottom, right, 0);
	    Referee r = new Referee();
	    Posn tl = new Posn(-1,1);
	    Posn tc = new Posn(0,1);
	    Posn tr = new Posn(1,1);
	    Posn ml = new Posn(-1,0);
	    Posn mc = new Posn(0,0);
	    Posn mr = new Posn(1,0);
	    Posn bl = new Posn(-1,-1);
	    Posn bc = new Posn(0,-1);
	    Posn br = new Posn(1,-1);
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
	    r.placeMeeple(mc, p, Direction.CENTER);
	    int score = p.getScore();
	    r.scoreMonastery(mc);
	    assertTrue(score + 9 == p.getScore());
    }
    
    @Test
    public void gameOverCityScoringTest() {
    // do cities have to be completed?
    }
    
    @Test
    public void gameOverRoadScoringTest() {
    // do roads have to be completed?
    }
    
    public void gameOverMonasteryScoringTest() throws PosnTakenException, InvalidEdgeException{
	    Center c = new Center(Feature.MONASTERY);
	    Edge top = new Edge(Feature.FIELD);
	    Edge left = new Edge(Feature.FIELD);
	    Edge bottom = new Edge(Feature.FIELD);
	    Edge right = new Edge(Feature.FIELD);
	    Tile t = new Tile(c, top, left, bottom, right, 0);
	    Referee r = new Referee();
	    Posn tc = new Posn(0,1);
	    Posn tr = new Posn(1,1);
	    Posn ml = new Posn(-1,0);
	    Posn mc = new Posn(0,0);
	    Posn bl = new Posn(-1,-1);
	    Posn bc = new Posn(0,-1);
	    r.place(mc, t);
	    r.place(tc, new Tile(c, top, left, bottom, right, 0));
	    r.place(tr, new Tile(c, top, left, bottom, right, 0));
	    r.place(ml, new Tile(c, top, left, bottom, right, 0));
	    r.place(bl, new Tile(c, top, left, bottom, right, 0));
	    r.place(bc, new Tile(c, top, left, bottom, right, 0));
	    Player p = new Player(1, "colby");
	    r.placeMeeple(mc, p, Direction.CENTER);
	    int score = p.getScore();
	    r.scoreMonasteryEndgame();
	    assertTrue(score + 6 == p.getScore());
    }
   
}
