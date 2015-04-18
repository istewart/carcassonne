package edu.brown.cs.scij.gametests;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import edu.brown.cs.scij.game.Deck;
import edu.brown.cs.scij.tile.Center;
import edu.brown.cs.scij.tile.Edge;
import edu.brown.cs.scij.tile.Feature;
import edu.brown.cs.scij.tile.InvalidTileException;
import edu.brown.cs.scij.tile.Tile;

public class DeckTest {

	@Test
	public void drawTest() throws InvalidTileException{
		Center c = new Center(Feature.MONASTERY);
		Edge top = new Edge(Feature.FIELD);
		Edge bottom = new Edge(Feature.FIELD);
		Edge left = new Edge(Feature.FIELD);
		Edge right = new Edge(Feature.FIELD);
		Tile t1 = new Tile(c, top, right, bottom, left, 0);
		Center c2 = new Center(Feature.CITY);
		Edge top2 = new Edge(Feature.CITY);
		Edge bottom2 = new Edge(Feature.CITY);
		Edge left2 = new Edge(Feature.CITY);
		Edge right2 = new Edge(Feature.CITY);
		Tile t2 = new Tile(c2, top2, right2, bottom2, left2, 1);
		List<Tile> tiles = new ArrayList<>();
		tiles.add(t1);
		tiles.add(t2);
		Deck d = new Deck(tiles);
		assertTrue(!d.isEmpty());
		assertTrue(d.drawTile().equals(t1));
		assertTrue(!d.isEmpty());
		assertTrue(d.drawTile().equals(t2));
		assertTrue(d.isEmpty());
		assertTrue(d.drawTile() == null);
	}
}
