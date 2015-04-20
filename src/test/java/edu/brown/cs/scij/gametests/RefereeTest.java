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
    assertTrue(r.getPlayers().size() == 4);
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

  @Test
  public void cityScoringTest() {
    // TODO cant do this till scott does scoring
  }

  @Test
  public void roadScoringTest() {
    // TODO cant do this till scott does scoring
  }

  @Test
  public void monasteryScoringTest() {
    // TODO cant do this till scott does scoring
  }

  @Test
  public void gameOverScoringTest() {
    // TODO cant do this till scott does scoring
  }
}
