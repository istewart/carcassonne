package edu.brown.cs.scij.gametests;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.brown.cs.scij.game.Meeple;
import edu.brown.cs.scij.game.Player;

public class MeepleTest {

	@Test
	public void constructorTest(){
		Player p = new Player(1, "p");
		Meeple m = new Meeple(p);
		
		assertTrue(!m.getPlayer().equals(p));
		assertTrue(m.getPlayer().getId() == p.getId());
		assertTrue(m.getPlayer().getName().equals(p.getName()));
		assertTrue(m.getPlayer().getNumMeeples() == p.getNumMeeples());
		assertTrue(m.getPlayer().getPlayerColor() == p.getPlayerColor());
		assertTrue(m.getPlayer().getScore() == p.getScore());
	}
}
