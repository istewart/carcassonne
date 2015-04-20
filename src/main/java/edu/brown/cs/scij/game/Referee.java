package edu.brown.cs.scij.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import edu.brown.cs.scij.tile.Center;
import edu.brown.cs.scij.tile.Direction;
import edu.brown.cs.scij.tile.Edge;
import edu.brown.cs.scij.tile.Feature;
import edu.brown.cs.scij.tile.InvalidEdgeException;
import edu.brown.cs.scij.tile.NullMeepleException;
import edu.brown.cs.scij.tile.Tile;
import edu.brown.cs.scij.tile.TileFeature;

public class Referee {
  private List<Player> players;
  private Deck deck;
  private int turnNumber;
  private Board board;

  private static final int FINISHED_ROAD = 2;
  private static final int UNFINISHED_ROAD = 1;

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
  public Tile drawTile() /* throws EmptyDeckException */{
    return deck.drawTile();
  }

  public boolean isGameOver() {
    return deck.isEmpty();
  }

  public void setupGame() {
    this.turnNumber = 0;
    players = new ArrayList<Player>();
    // this.isGameOver = false;
    List<Tile> tiles = null;
    try {
      tiles = buildDeck();
    } catch (InvalidEdgeException ite) {
      System.out.println("wont be reached, tiles currentlyhardcoded");
    }
    deck = new Deck(tiles);
    board = new Board();
  }

  // main/handlers handles this
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
  public void score(Posn p) {
    // TODO score
    if (isGameOver()) {
      gameOverScoring();
    } else {
      normalScoring(p);
    }
  }

  public void gameOverScoring() {
    scoreMonasteryEndgame();
    scoreRoadEndgame();
    scoreCityEndgame();
  }

  public void normalScoring(Posn p) {
    scoreMonastery(p);
    scoreRoad(p);
    scoreCity(p);
  }

  public void scoreMonastery(Posn p) {
    // if the tile is a monastery:
    scoreMonasteryHelper(p);

    // if the tile is not a monastery, check the surrounding tiles for finished
    // monasteries:
    List<Posn> surrounding = getSurroundingTiles(p);
    for (Posn posn : surrounding) {
      scoreMonasteryHelper(posn);
    }

  }

  public void scoreMonasteryHelper(Posn p) {
    Tile t = board.getBoard().get(p);
    if (t == null) {
      return;
    }
    Center c = t.getCenter();
    if (c.getFeature() == Feature.MONASTERY) {
      Meeple m = c.getMeeple();
      if (m != null) {
        // check surrounding tiles for tiles
        int count = numSurroundingTiles(p);
        if (count == 9) {
          m.getPlayer().addScore(count);
          try {
            c.removeMeeple();
          } catch (NullMeepleException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
        }
      }
    }
  }

  public List<Posn> getSurroundingTiles(Posn p) {
    List<Posn> surrounding = new ArrayList<>();
    int x = p.getX();
    int y = p.getY();
    if (board.getBoard().containsKey(new Posn(x + 1, y))) {
      surrounding.add(new Posn(x + 1, y));
    }
    if (board.getBoard().containsKey(new Posn(x + 1, y + 1))) {
      surrounding.add(new Posn(x + 1, y + 1));
    }
    if (board.getBoard().containsKey(new Posn(x + 1, y - 1))) {
      surrounding.add(new Posn(x + 1, y - 1));
    }
    if (board.getBoard().containsKey(new Posn(x, y))) {
      surrounding.add(new Posn(x, y));
    }
    if (board.getBoard().containsKey(new Posn(x, y + 1))) {
      surrounding.add(new Posn(x, y + 1));
    }
    if (board.getBoard().containsKey(new Posn(x, y - 1))) {
      surrounding.add(new Posn(x, y - 1));
    }
    if (board.getBoard().containsKey(new Posn(x - 1, y))) {
      surrounding.add(new Posn(x - 1, y));
    }
    if (board.getBoard().containsKey(new Posn(x - 1, y + 1))) {
      surrounding.add(new Posn(x - 1, y + 1));
    }
    if (board.getBoard().containsKey(new Posn(x - 1, y - 1))) {
      surrounding.add(new Posn(x - 1, y - 1));
    }

    return surrounding;
  }

  public int numSurroundingTiles(Posn p) {
    int count = 0;
    int x = p.getX();
    int y = p.getY();
    if (board.getBoard().containsKey(new Posn(x + 1, y))) {
      count++;
    }
    if (board.getBoard().containsKey(new Posn(x + 1, y + 1))) {
      count++;
    }
    if (board.getBoard().containsKey(new Posn(x + 1, y - 1))) {
      count++;
    }
    if (board.getBoard().containsKey(new Posn(x, y))) {
      count++;
    }
    if (board.getBoard().containsKey(new Posn(x, y + 1))) {
      count++;
    }
    if (board.getBoard().containsKey(new Posn(x, y - 1))) {
      count++;
    }
    if (board.getBoard().containsKey(new Posn(x - 1, y))) {
      count++;
    }
    if (board.getBoard().containsKey(new Posn(x - 1, y + 1))) {
      count++;
    }
    if (board.getBoard().containsKey(new Posn(x - 1, y - 1))) {
      count++;
    }

    return count;
  }

  public void scoreRoad(Posn p) {
    scoreRoadAt(p);
  }

  public int scoreRoadHelper(Posn curPosn, Posn prevPosn, Set<Posn> visited,
      Set<TileFeature> meepledRoads, Direction d, int count) {
    if (curPosn.equals(prevPosn)) {
      if (isGameOver()) {
        return count;
      }
      return 0;
    }
    if (visited.contains(curPosn)) {
      return count;
    }
    Tile curTile = board.getBoard().get(curPosn);
    if (curTile == null) {
      if (isGameOver()) {
        return count;
      }
      return 0;
    } else {
      grabMeepleAtOppositeFeature(curTile, d, meepledRoads);
      if (curTile.roadEnds()) {
        return count + 1;
      } else {
        // if the road doesn't end at this tile, there is always a chance that
        // the meeple was placed in the center.
        grabMeepleAtCenter(curTile, meepledRoads);
        int downScore = 0;
        int upScore = 0;
        int leftScore = 0;
        int rightScore = 0;
        if (curTile.getBottom().getFeature() == Feature.ROAD) {
          // check meeple on bottom and continue in this direction
          grabMeepleAtOppositeFeature(curTile, Direction.DOWN, meepledRoads);
          downScore = scoreRoadHelper(curPosn.withY(curPosn.getY() - 1),
              curPosn, visited, meepledRoads, Direction.UP, count + 1);
        } else if (curTile.getRight().getFeature() == Feature.ROAD) {
          // check meeple on right and continue in this direction
          grabMeepleAtOppositeFeature(curTile, Direction.RIGHT, meepledRoads);
          rightScore = scoreRoadHelper(curPosn.withX(curPosn.getX() + 1),
              curPosn, visited, meepledRoads, Direction.LEFT, count + 1);
        } else if (curTile.getTop().getFeature() == Feature.ROAD) {
          // check meeple on top and continue in this direction
          grabMeepleAtOppositeFeature(curTile, Direction.UP, meepledRoads);
          upScore = scoreRoadHelper(curPosn.withY(curPosn.getY() + 1), curPosn,
              visited, meepledRoads, Direction.DOWN, count + 1);
        } else if (curTile.getLeft().getFeature() == Feature.ROAD) {
          // check meeple on top and continue in this direction
          grabMeepleAtOppositeFeature(curTile, Direction.LEFT, meepledRoads);
          upScore = scoreRoadHelper(curPosn.withX(curPosn.getX() - 1), curPosn,
              visited, meepledRoads, Direction.RIGHT, count + 1);
        }

        return downScore + upScore + leftScore + rightScore;
      }
    }
  }

  public void scoreCity(Posn p) {
    scoreCityAt(p);
  }

  public void scoreMonasteryEndgame() {
    Tile currTile;
    Posn currPosn;
    Center c;
    for (Entry<Posn, Tile> e : board.getBoard().entrySet()) {
      currTile = e.getValue();
      currPosn = e.getKey();
      c = currTile.getCenter();

      if (c.getFeature() == Feature.MONASTERY && c.hasMeeple()) {
        c.getMeeple().getPlayer().addScore(numSurroundingTiles(currPosn));
      }
    }
  }

  public void scoreRoadEndgame() {
    for (Posn p : board.getBoard().keySet()) {
      scoreRoadAt(p);
    }
  }

  public void scoreCityEndgame() {
    for (Posn p : board.getBoard().keySet()) {
      scoreCityAt(p);
    }
  }

  public void scoreCityAt(Posn p) {
    Tile curTile = board.getBoard().get(p);
    Edge top = curTile.getTop();
    Edge bottom = curTile.getBottom();
    Edge left = curTile.getLeft();
    Edge right = curTile.getRight();
    // TODO COPY ROAD PRETTY MUCH, but the center has to be a city to continue
    // going in any other direction, and scoring included shields, so can't use
    // the same check as roads when they check if the score is > 2, have to find
    // something else.
    int upScore = 0;
    int downScore = 0;
    int leftScore = 0;
    int rightScore = 0;
    if (curTile.getCenter().getFeature() == Feature.CITY) {
      // check in all directions for cities, there are at least two connected,
      // and they count as the same city
    } else {
      // check in all directions for cities, but each one is its own city
      if (right.getFeature() == Feature.CITY) {
        // check to the right
        Set<TileFeature> rightCityMeeples = new HashSet<>();
        Set<Posn> visitedRight = new HashSet<>();
        visitedRight.add(p);
        if (right.hasMeeple()) {
          rightCityMeeples.add(right);
        }
        rightScore = scoreCityHelper(p.withX(p.getX() + 1), null, visitedRight,
            rightCityMeeples, Direction.LEFT, 1);
      }
    }
  }

  public int scoreCityHelper(Posn curPosn, Posn prevPosn, Set<Posn> visited,
      Set<TileFeature> meepled, Direction d, int count) {
    // TODO recursively checkj each direction until there is no more center that
    // is a city. If the prev posn maps to a null tile, then return 0. If a tile
    // has a shield, it must be connected to the current city checking, because
    // of the way the tiles are set up.
    return -1;
  }

  public void scoreRoadAt(Posn p) {
    Tile curTile = board.getBoard().get(p);
    Edge top = curTile.getTop();
    Edge bottom = curTile.getBottom();
    Edge left = curTile.getLeft();
    Edge right = curTile.getRight();
    int upScore = 0;
    int downScore = 0;
    int leftScore = 0;
    int rightScore = 0;
    if (curTile.roadEnds()) {
      // Check all sides for a road ending
      if (right.getFeature() == Feature.ROAD) {
        // check to the right
        Set<TileFeature> rightRoadMeeples = new HashSet<>();
        Set<Posn> visitedRight = new HashSet<>();
        visitedRight.add(p);
        if (right.hasMeeple()) {
          rightRoadMeeples.add(right);
        }
        rightScore = scoreRoadHelper(p.withX(p.getX() + 1), null, visitedRight,
            rightRoadMeeples, Direction.LEFT, 1);
        if (rightScore > 1) {
          scoreMeeples(rightRoadMeeples, rightScore);
        }
      }
      if (left.getFeature() == Feature.ROAD) {
        // check to the left
        Set<TileFeature> leftRoadMeeples = new HashSet<>();
        Set<Posn> visitedLeft = new HashSet<>();
        visitedLeft.add(p);
        if (left.hasMeeple()) {
          leftRoadMeeples.add(left);
        }
        leftScore = scoreRoadHelper(p.withX(p.getX() - 1), null, visitedLeft,
            leftRoadMeeples, Direction.RIGHT, 1);
        if (leftScore > 1) {
          scoreMeeples(leftRoadMeeples, leftScore);
        }
      }
      if (top.getFeature() == Feature.ROAD) {
        // check to the top
        Set<TileFeature> topRoadMeeples = new HashSet<>();
        Set<Posn> visitedTop = new HashSet<>();
        visitedTop.add(p);
        if (top.hasMeeple()) {
          topRoadMeeples.add(top);
        }
        upScore = scoreRoadHelper(p.withY(p.getY() + 1), null, visitedTop,
            topRoadMeeples, Direction.DOWN, 1);
        if (upScore > 1) {
          scoreMeeples(topRoadMeeples, upScore);
        }
      }
      if (bottom.getFeature() == Feature.ROAD) {
        // check to the bottom
        Set<TileFeature> bottomRoadMeeples = new HashSet<>();
        Set<Posn> visitedBottom = new HashSet<>();
        visitedBottom.add(p);
        if (bottom.hasMeeple()) {
          bottomRoadMeeples.add(bottom);
        }
        downScore = scoreRoadHelper(p.withY(p.getY() - 1), null, visitedBottom,
            bottomRoadMeeples, Direction.UP, 1);
        if (downScore > 1) {
          scoreMeeples(bottomRoadMeeples, downScore);
        }
      }
    } else {
      Set<Posn> visited = new HashSet<>();
      Set<TileFeature> meepledRoads = new HashSet<>();
      grabMeepleAtCenter(curTile, meepledRoads);

      upScore = 0;
      downScore = 0;
      leftScore = 0;
      rightScore = 0;
      if (right.getFeature() == Feature.ROAD) {
        // check to the right
        if (right.hasMeeple()) {
          meepledRoads.add(right);
        }
        rightScore = scoreRoadHelper(p.withX(p.getX() + 1), null, visited,
            meepledRoads, Direction.LEFT, 1);
      }
      if (left.getFeature() == Feature.ROAD) {
        // check to the left
        if (left.hasMeeple()) {
          meepledRoads.add(left);
        }
        leftScore = scoreRoadHelper(p.withX(p.getX() - 1), null, visited,
            meepledRoads, Direction.RIGHT, 1);
      }
      if (top.getFeature() == Feature.ROAD) {
        // check to the top
        if (top.hasMeeple()) {
          meepledRoads.add(top);
        }
        upScore = scoreRoadHelper(p.withY(p.getY() + 1), null, visited,
            meepledRoads, Direction.DOWN, 1);
      }
      if (bottom.getFeature() == Feature.ROAD) {
        // check to the bottom
        if (bottom.hasMeeple()) {
          meepledRoads.add(bottom);
        }
        downScore = scoreRoadHelper(p.withY(p.getY() - 1), null, visited,
            meepledRoads, Direction.UP, 1);
      }

      int score = upScore + downScore + leftScore + rightScore;
      scoreMeeples(meepledRoads, score);
    }
  }

  public void grabMeepleAtOppositeFeature(Tile t, Direction d,
      Set<TileFeature> meepled) {
    if (d == Direction.RIGHT && t.getRight().hasMeeple()) {
      meepled.add(t.getRight());
    } else if (d == Direction.LEFT && t.getLeft().hasMeeple()) {
      meepled.add(t.getLeft());
    } else if (d == Direction.DOWN && t.getBottom().hasMeeple()) {
      meepled.add(t.getBottom());
    } else if (d == Direction.UP && t.getTop().hasMeeple()) {
      meepled.add(t.getTop());
    }
  }

  public void grabMeepleAtCenter(Tile t, Set<TileFeature> meepled) {
    if (t.getCenter().hasMeeple()) {
      meepled.add(t.getCenter());
    }
  }

  public void scoreMeeples(Set<TileFeature> meepledFeatures, int baseScore) {
    Map<Player, Integer> meeples = new HashMap<>();
    for (Player p : players) {
      meeples.put(p, 0);
    }
    for (TileFeature tf : meepledFeatures) {
      Player p = tf.getMeeple().getPlayer();
      int numMeeples = meeples.get(p);
      meeples.put(p, numMeeples + 1);
      try {
        tf.removeMeeple();
      } catch (NullMeepleException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        System.out
            .println("shouldn't get here, if we do, reach scott at 2**-*4*-**2*");
        System.out.println(e.getMessage());
      }
    }

    int maxMeeples = 0;
    for (Integer meepleCounts : meeples.values()) {
      if (meepleCounts > maxMeeples) {
        maxMeeples = meepleCounts;
      }
    }

    for (Map.Entry<Player, Integer> meepleCount : meeples.entrySet()) {
      if (meepleCount.getValue() == maxMeeples) {
        meepleCount.getKey().addScore(baseScore);
      }
    }
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

  private List<Tile> buildDeck() throws InvalidEdgeException {
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
