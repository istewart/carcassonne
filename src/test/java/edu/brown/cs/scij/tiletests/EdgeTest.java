package edu.brown.cs.scij.tiletests;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.brown.cs.scij.game.Meeple;
import edu.brown.cs.scij.game.Player;
import edu.brown.cs.scij.tile.Edge;
import edu.brown.cs.scij.tile.Feature;

public class EdgeTest {
	
	@Test
	public void constructorTest(){
		Edge e = new Edge(Feature.CITY);
		assertTrue(e.isMeeplable());
		assertTrue(e.getMeeple() == null);
	}
	
	@Test
	public void meepleTest(){
		Edge e = new Edge(Feature.FIELD);
		assertTrue(e.isMeeplable());
		assertTrue(e.getMeeple() == null);
		Player p = new Player(1, "p");
		Meeple m = new Meeple(p);
		e.setMeeple(m);
		assertTrue(e.getMeeple().equals(m));
		//might want to add a remove meeple
	}
	
	@Test
	public void equalsTest(){
		Edge ee = new Edge(Feature.ENDPOINT);
		Edge ero = new Edge(Feature.ROAD);
		Edge eri = new Edge(Feature.RIVER);
		Edge ee2 = new Edge(Feature.ENDPOINT);
		assertTrue(!ee.equals(ero));
		assertTrue(!ee.equals(eri));
		assertTrue(!ero.equals(eri));
		assertTrue(ee.equals(ee2));
	}
	
	//TODO Should test for what is allowed to be what
}