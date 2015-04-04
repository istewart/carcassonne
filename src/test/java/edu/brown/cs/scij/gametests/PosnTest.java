package edu.brown.cs.scij.gametests;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.brown.cs.scij.game.Posn;

public class PosnTest {
	
	@Test
	public void constructorTest(){
		Posn p = new Posn(1,2);
		assertTrue(p.getX() == 1);
		assertTrue(p.getY() == 2);
	}
	
	@Test
	public void withTest(){
		Posn p = new Posn(1,1);
		assertTrue(p.withX(2).getX() == 2);
		assertTrue(p.withY(2).getY() == 2);
	}
	
	@Test
	public void equalsTest(){
		Posn p1 = new Posn(1,1);
		Posn p2 = new Posn(2,2);
		Posn p12 = new Posn(1,1);
		assertTrue(!p1.equals(p2));
		assertTrue(p1.equals(p12));
	}
}
