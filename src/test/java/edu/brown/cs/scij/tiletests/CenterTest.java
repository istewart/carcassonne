package edu.brown.cs.scij.tiletests;
import static org.junit.Assert.*;

import org.junit.Test;

import edu.brown.cs.scij.game.Meeple;
import edu.brown.cs.scij.game.Player;
import edu.brown.cs.scij.tile.Center;
import edu.brown.cs.scij.tile.Feature;
import edu.brown.cs.scij.tile.InvalidEdgeException;
import edu.brown.cs.scij.tile.UnMeeplableException;

public class CenterTest {
	
	@Test
	public void constructorTest() throws InvalidEdgeException{
		Center c = new Center(Feature.CITY);
		assertTrue(c.isMeeplable());
		assertTrue(c.getMeeple() == null);
	}
	
	@Test
	public void meepleTest() throws InvalidEdgeException, UnMeeplableException{
		Center c = new Center(Feature.MONASTERY);
		assertTrue(c.isMeeplable());
		assertTrue(c.getMeeple() == null);
		Player p = new Player(1, "p");
		Meeple m = new Meeple(p);
		c.setMeeple(m);
		assertTrue(c.getMeeple().equals(m));
	}
	
	@Test
	public void equalsTest() throws InvalidEdgeException{
		Center cc = new Center(Feature.CITY);
		Center cro = new Center(Feature.ROAD);
		Center cri = new Center(Feature.RIVER);
		Center cc2 = new Center(Feature.CITY);
		assertTrue(!cc.equals(cro));
		assertTrue(!cc.equals(cri));
		assertTrue(!cro.equals(cri));
		assertTrue(cc.equals(cc2));
	}
	
	@Test
	public void allowedTest(){
			new Center(Feature.CITY);
			new Center(Feature.MONASTERY);
			new Center(Feature.ROAD);
			new Center(Feature.RIVER);
			new Center(Feature.FIELD);
			new Center(Feature.ENDPOINT);
			assertTrue(true);
	}
}
