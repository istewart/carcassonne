package edu.brown.cs.scij.gametests;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.brown.cs.scij.game.Referee;

public class RefereeTest {

	@Test
	public void constructorTest(){
		Referee r = new Referee();
		//TODO referee doesnt have getters. will work with scott on completing/testing for next checkpoint. 
		// testing referee comes last, like testing whole system. 
	}
	
	@Test
	public void shuffleTest(){
		//TODO shuffles deck correctly
	}
	
	@Test
	public void buildDeckTest(){
		//TODO creation of game, all tiles in deck correctly
	}
	
	@Test
	public void scoringTest(){
		//TODO this will be huge too, every possible way to score covered
	}
}
