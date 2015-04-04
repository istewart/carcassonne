package edu.brown.cs.scij.gametests;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.brown.cs.scij.game.Board;
import edu.brown.cs.scij.game.Posn;
import edu.brown.cs.scij.game.PosnTakenException;
import edu.brown.cs.scij.tile.Center;
import edu.brown.cs.scij.tile.Edge;
import edu.brown.cs.scij.tile.Feature;
import edu.brown.cs.scij.tile.Tile;

public class BoardTest {

	@Test
	public void constructorTest(){
		Board b = new Board();
		assertTrue(b.getBoard().isEmpty());
	}
	
	@Test
	public void placeTest(){
		Board b = new Board();
		assertTrue(b.getBoard().isEmpty());
		Center c = new Center(Feature.MONASTERY);
		Edge top = new Edge(Feature.FIELD);
		Edge bottom = new Edge(Feature.FIELD);
		Edge left = new Edge(Feature.FIELD);
		Edge right = new Edge(Feature.FIELD);
		Tile t = new Tile(c, top, right, bottom, left, 0);
		Posn p = new Posn(0,0);
		try {
			b = b.place(p, t);
			assertTrue(b.getBoard().get(p).equals(t));
			Tile t2 = new Tile(null, null, null, null, null, 0);
			b = b.place(p, t2);
			assertTrue(false);
		} catch (PosnTakenException e) {
			assertTrue(b.getBoard().get(p).equals(t));
		}
		
	}
	
	@Test
	public void validMovesTest(){
		//TODO this test should be broken up but could be enormous.
		// should test all possible combinations of edges in some way
	}
}
