package edu.brown.cs.scij.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import edu.brown.cs.scij.tile.Center;
import edu.brown.cs.scij.tile.Edge;
import edu.brown.cs.scij.tile.Feature;
import edu.brown.cs.scij.tile.InvalidTileException;
import edu.brown.cs.scij.tile.Tile;
import edu.brown.cs.scij.tile.TileFeature;

public class Referee {
  // private PlayerFactory pf;
  private List<Player> players;
  // private boolean isGameOver; pretty sure we don't need this
  private Deck deck;
  private int turnNumber;
  private Board board;

  public Referee() {
    // TODO should be done but there might be something else but I can't think
    // of more setup (and it should go in setupGame()
    setupGame();
  }

  public Board getBoard() {
    return board;
  }

  public List<Player> getPlayers() {
	return players;
}

public void setPlayers(List<Player> players) {
	this.players = players;
}

public Deck getDeck() {
	return deck;
}

public void setDeck(Deck deck) {
	this.deck = deck;
}

public int getTurnNumber() {
	return turnNumber;
}

public void setTurnNumber(int turnNumber) {
	this.turnNumber = turnNumber;
}

public void setBoard(Board board) {
	this.board = board;
}

/**
   * Places a tile on the board. Returns a boolean letting the user know if the
   * game is over. (Game is over when there are no tiles left).
   *
   * @param p Where the player wants to place their tile
   * @param t The tile the
   * @return
   * @throws PosnTakenException
   */
  public void place(Posn p, Tile t) throws PosnTakenException {
    // TODO pretty sure throw, right, because if the Posn is taken the
    // handler/main gameplay function should handle that.
    board.place(p, t);
  }

  // Should the referee handle all interaction with the deck? Yes, right?
  public Tile drawTile() {
    return deck.drawTile();
  }

  public boolean isGameOver() {
    return deck.isEmpty();
  }

  public void setupGame() {
    this.turnNumber = 0;
    // this.isGameOver = false;
    List<Tile> tiles = null;
    try{
    	tiles = buildDeck();
    } catch (InvalidTileException ite) {
    	System.out.println("wont be reached, tiles currentlyhardcoded");
    }
    deck = new Deck(tiles);
    board = new Board();
  }

  // main handles this
  public void run() {
    // TODO run everything
  }

  // Probably don't need this, handled by the handlers/main
  public void takeTurn() {
    // TODO take turn
  }

  /**
   * Scores the board after the last tile and meeple have been placed has been
   * placed.
   *
   * @param prevTile the tile that was last placed on the board
   */
  public void score(Posn p/*Tile prevTile*/) {
    // TODO score
    if (isGameOver()) {
      gameOverScoring();
    } else {
      normalScoring(p/*prevTile*/);
    }
  }

  private void gameOverScoring() {
    for (Posn p : board.getMeeplePosns()) {
      // gameOverScoreTile(p);
      Tile t = board.getBoard().get(p);
      switch (t.getMeepledFeature()) {
        case CITY:
          scoreCityEnd(p, t);
          break;
        case ENDPOINT: // not possible
          break;
        case FIELD: // not possible
          break;
        case MONASTERY:
          scoreMonasteryEnd(p, t);
          break;
        case RIVER: // not possible
          break;
        case ROAD:
          scoreRoadEnd(p, t);
          break;
        default:
          break;

      }
    }
  }

  private void scoreCityEnd(Posn p, Tile t) {
    // CAN BE CENTER OR EDGE
    Queue<Posn> q = new LinkedList<>();
    if (t.getCenter().getMeeple() != null) {
      checkEdgesForCity(p, t, q);
    } else if (t.getTop().getMeeple() != null) {
      if (t.getCenter().getFeature() == Feature.CITY) {
        checkEdgesForCity(p, t, q);
      } else {
        q.add(p.withY(p.getY() + 1));
      }
    } else if (t.getRight().getMeeple() != null) {
      if (t.getCenter().getFeature() == Feature.CITY) {
        checkEdgesForCity(p, t, q);
      } else {
        q.add(p.withX(p.getX() + 1));
      }
    } else if (t.getBottom().getMeeple() != null) {
      if (t.getCenter().getFeature() == Feature.CITY) {
        checkEdgesForCity(p, t, q);
      } else {
        q.add(p.withY(p.getY() - 1));
      }
    } else if (t.getLeft().getMeeple() != null) {
      if (t.getCenter().getFeature() == Feature.CITY) {
        checkEdgesForCity(p, t, q);
      } else {
        q.add(p.withX(p.getX() - 1));
      }
    }
    scoreCityEndHelp(q);
  }

  private void scoreCityEndHelp(Queue<Posn> q) {
    // TODO need to rethink this logic...
    // Need some kind of visited or set of visited posns, but
    // one can have multiple city portions on the same tile, so be careful
    // ALSO THERE CAN BE OTHER PEOPLES MEEPLES ON THE NEW TILES
    while (!q.isEmpty()) {
      Posn p = q.remove();
    }
  }

  private void checkEdgesForCity(Posn p, Tile t, Queue<Posn> q) {
    if (t.getTop().getFeature() == Feature.CITY) {
      q.add(p.withY(p.getY() + 1));
    }
    if (t.getRight().getFeature() == Feature.CITY) {
      q.add(p.withX(p.getX() + 1));
    }
    if (t.getBottom().getFeature() == Feature.CITY) {
      q.add(p.withY(p.getY() - 1));
    }
    if (t.getLeft().getFeature() == Feature.CITY) {
      q.add(p.withX(p.getX() - 1));
    }
  }

  private void scoreMonasteryEnd(Posn p, Tile t) {
    // MUST BE CENTER PIECE
    if (!(t.getCenter().getFeature() == Feature.MONASTERY && t.getCenter()
        .getMeeple() != null)) {
      throw new IllegalStateException();
    }
    Player player = t.getCenter().getMeeple().getPlayer();
    int score = 1;
    if (board.getBoard().get(new Posn(p.getX() - 1, p.getY() - 1)) != null) {
      score++;
    }
    if (board.getBoard().get(new Posn(p.getX() - 1, p.getY())) != null) {
      score++;
    }
    if (board.getBoard().get(new Posn(p.getX() - 1, p.getY() + 1)) != null) {
      score++;
    }
    if (board.getBoard().get(new Posn(p.getX(), p.getY() - 1)) != null) {
      score++;
    }
    if (board.getBoard().get(new Posn(p.getX(), p.getY() + 1)) != null) {
      score++;
    }
    if (board.getBoard().get(new Posn(p.getX() + 1, p.getY() - 1)) != null) {
      score++;
    }
    if (board.getBoard().get(new Posn(p.getX() + 1, p.getY())) != null) {
      score++;
    }
    if (board.getBoard().get(new Posn(p.getX() + 1, p.getY() + 1)) != null) {
      score++;
    }

    player.addScore(score);
  }

  private void scoreRoadEnd(Posn p, Tile t) {
    // CAN BE CENTER OR EDGE
    // TODO need to rethink this logic
  }

  private void normalScoring(Posn p/*Tile prevTile*/) {
    // TODO rethink logic. There can be meeples or no meeples on this tile.
    // Need to check every directrion for everything. Major thing left.
    Tile prevTile = board.getBoard().get(p);
    /*switch (prevTile.getMeepledFeature()) {
      case CITY:
        scoreCity(p, prevTile);
        break;
      case ENDPOINT: // not possible
        break;
      case FIELD: // Not possible
        break;
      case MONASTERY:
        scoreMonastery(p, prevTile);
        break;
      case RIVER:
        break;
      case ROAD:
        scoreRoad(p, prevTile);
        break;
      default:
        break;

    }*/
  }

  private void scoreCity(Posn p, Tile t) {
    // TODO rethink logic... See scoreCtyEnd/scoreCityEndHelp
  }

  private void scoreMonastery(Posn p, Tile t) {
    // TODO rethink logic.
    // A placed tile can only finish monasteries surrounding it
    // Check all nine spaces around this tile to find a monastery, then check
    // those surrrounding 8
    /*if (!(t.getCenter().getFeature() == Feature.MONASTERY && t.getCenter()
        .getMeeple() != null)) {
      throw new IllegalStateException();
    }*/

  }

  private void scoreRoad(Posn p, Tile t) {
    // TODO rethink logic
  }

  public void placeMeeple(Posn posn, Tile tile, Player player,
      TileFeature feature) {
    Set<Posn> meepled = board.getMeeplePosns();
    meepled.add(posn);
  }

  /**
   * Adds a player to the current game.
   *
   * @param player the player to add to the game
   */
  public void newPlayer(Player player) {
    players.add(player);
  }

  /**
   * Shuffles the order of the players (used usually only at the start of the
   * game)
   */
  public void shuffleOrder() {
    Collections.shuffle(players);
  }

  public Player nextPlayer() {
    return players.get(turnNumber % 4);
  }

  private List<Tile> buildDeck() throws InvalidTileException {
    Feature road = Feature.ROAD;
    Feature endpoint = Feature.ENDPOINT;
    Feature city = Feature.CITY;
    Feature field = Feature.FIELD;
    Feature monastery = Feature.MONASTERY;
    Feature river = Feature.RIVER;

    int i = 0;

    List<Tile> tiles = new ArrayList<>();
    // 1x 4-road piece w/endpoint
    tiles.add(new Tile(new Center(endpoint), new Edge(road), new Edge(road),
        new Edge(road), new Edge(road), 0));

    // 4x 3-road 1-field w/endpoint
    for (i = 0; i < 4; i++) {
      tiles.add(new Tile(new Center(endpoint), new Edge(field), new Edge(road),
          new Edge(road), new Edge(road), 0));
    }

    // 3x 3-road 1-city w/endpoint
    for (i = 0; i < 3; i++) {
      tiles.add(new Tile(new Center(endpoint), new Edge(city), new Edge(road),
          new Edge(road), new Edge(road), 0));
    }

    // 8x straight road
    for (i = 0; i < 8; i++) {
      tiles.add(new Tile(new Center(road), new Edge(field), new Edge(road),
          new Edge(field), new Edge(road), 0));
    }

    // 9x curved road
    for (i = 0; i < 9; i++) {
      tiles.add(new Tile(new Center(road), new Edge(field), new Edge(field),
          new Edge(road), new Edge(road), 0));
    }

    // 4x 1-city w/straight road
    for (i = 0; i < 4; i++) {
      tiles.add(new Tile(new Center(road), new Edge(city), new Edge(road),
          new Edge(field), new Edge(road), 0));
    }

    // 3x 1-city w/curved road from left
    for (i = 0; i < 3; i++) {
      tiles.add(new Tile(new Center(field), new Edge(city), new Edge(field),
          new Edge(road), new Edge(road), 0));
    }

    // 3x 1-city w/curved road from right
    for (i = 0; i < 3; i++) {
      tiles.add(new Tile(new Center(field), new Edge(city), new Edge(road),
          new Edge(road), new Edge(field), 0));
    }

    // 3x 2-sided city w/curved road
    for (i = 0; i < 3; i++) {
      tiles.add(new Tile(new Center(city), new Edge(city), new Edge(city),
          new Edge(road), new Edge(road), 0));
    }

    // 2x 2-sided city w/curved road and shield
    for (i = 0; i < 2; i++) {
      tiles.add(new Tile(new Center(city), new Edge(city), new Edge(city),
          new Edge(road), new Edge(road), 1));
    }

    // 2x one-roaded monastery
    for (i = 0; i < 2; i++) {
      tiles.add(new Tile(new Center(monastery), new Edge(field),
          new Edge(field), new Edge(road), new Edge(field), 0));
    }

    // 1x 1-road 3-city
    tiles.add(new Tile(new Center(city), new Edge(city), new Edge(city),
        new Edge(road), new Edge(city), 0));

    // 2x 1-road 3-city w/shield
    for (i = 0; i < 2; i++) {
      tiles.add(new Tile(new Center(city), new Edge(city), new Edge(city),
          new Edge(road), new Edge(city), 1));
    }

    // 4x 4-field w/monastery
    for (i = 0; i < 4; i++) {
      tiles.add(new Tile(new Center(monastery), new Edge(field),
          new Edge(field), new Edge(field), new Edge(field), 0));
    }

    // 5x 3-field 1-city
    for (i = 0; i < 5; i++) {
      tiles.add(new Tile(new Center(field), new Edge(city), new Edge(field),
          new Edge(field), new Edge(field), 0));
    }

    // 1x 2-city (bridge) 2-field
    tiles.add(new Tile(new Center(city), new Edge(field), new Edge(city),
        new Edge(field), new Edge(city), 0));

    // 2x 2-city (bridge) 2-field w/shield
    for (i = 0; i < 2; i++) {
      tiles.add(new Tile(new Center(city), new Edge(field), new Edge(city),
          new Edge(field), new Edge(city), 1));
    }

    // 3x 2-city (corner) 2-field
    for (i = 0; i < 3; i++) {
      tiles.add(new Tile(new Center(city), new Edge(city), new Edge(city),
          new Edge(field), new Edge(field), 0));
    }

    // 2x 2-city (corner) 2-field w/shield
    for (i = 0; i < 2; i++) {
      tiles.add(new Tile(new Center(city), new Edge(city), new Edge(city),
          new Edge(field), new Edge(field), 1));
    }

    // 3x 2-city (not-connected, opposite sides) 2-field
    for (i = 0; i < 3; i++) {
      tiles.add(new Tile(new Center(field), new Edge(city), new Edge(field),
          new Edge(city), new Edge(field), 0));
    }

    // 2x 2-city (not-connected, adjacent sides) 2-field
    for (i = 0; i < 2; i++) {
      tiles.add(new Tile(new Center(field), new Edge(city), new Edge(city),
          new Edge(field), new Edge(field), 0));
    }

    // 3x 3-city 1-field
    for (i = 0; i < 3; i++) {
      tiles.add(new Tile(new Center(city), new Edge(city), new Edge(city),
          new Edge(field), new Edge(city), 0));
    }

    // 1x 3-city 1-field w/shield
    tiles.add(new Tile(new Center(city), new Edge(city), new Edge(city),
        new Edge(field), new Edge(city), 1));

    // 1x 4-city w/shield
    tiles.add(new Tile(new Center(city), new Edge(city), new Edge(city),
        new Edge(city), new Edge(city), 1));

    Collections.shuffle(tiles);

    /* 
     * TODO add river pieces:
     * add the ten that aren't the end pieces, shuffle them.
     * prepend the starting piece and append the end piece.
     * add the river pieces to the tiles list, without shuffling again.
     */
    return tiles;
  }

  public static void main(String[] args) {
    Referee r = new Referee();
    r.setupGame();
  }

}
