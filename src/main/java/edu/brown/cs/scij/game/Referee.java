package edu.brown.cs.scij.game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;

import edu.brown.cs.scij.tile.Center;
import edu.brown.cs.scij.tile.Direction;
import edu.brown.cs.scij.tile.Edge;
import edu.brown.cs.scij.tile.Feature;
import edu.brown.cs.scij.tile.InvalidEdgeException;
import edu.brown.cs.scij.tile.NullMeepleException;
import edu.brown.cs.scij.tile.OutOfMeeplesException;
import edu.brown.cs.scij.tile.Tile;
import edu.brown.cs.scij.tile.TileFeature;
import edu.brown.cs.scij.tile.UnMeeplableException;

/**
 * The Referee keeps track of one game of Carcassonne. There is a function to
 * run a command line version of the game if so wished and also commands that
 * can run through the game if wished.
 * @author szellers
 *
 */
public class Referee {
  private List<Player> players;
  private List<Spectator> spectators;
  private Deck deck;
  private int turnNumber;
  private Board board;
  private Tile curTile = null;
  private Posn curPosn = null;
  private Player curPlayer;
  private boolean isGameOver;

  /**
   * Creates a new Referee.
   */
  public Referee() {
    setupGame();
  }

  /**
   * Gets the player whose turn it currently is.
   * @return the player whose turn it currently is
   */
  public Player getCurPlayer() {
    return curPlayer;
  }

  /**
   * Gets the current tile.
   * @return the current tile
   */
  public Tile getCurTile() {
    return curTile;
  }

  /**
   * Sets the current position to p.
   * @param p the current position
   */
  public void setCurPosn(Posn p) {
    curPosn = p;
  }

  /**
   * Gets the last position played by a player.
   * @return the last position played by a player
   */
  public Posn getCurPosn() {
    return curPosn;
  }

  /**
   * Gets the current game board.
   * @return the current game board.
   */
  public Board getBoard() {
    return board;
  }

  /**
   * Gets the players as a list of Players.
   * @return the players of this game as a list of Players
   */
  public List<Player> getPlayers() {
    return players;
  }

  /**
   * Sets the game's players to players
   * @param players the players of this game of carcassonne
   */
  public void setPlayers(List<Player> players) {
    this.players = players;
  }

  /**
   * Gets the deck of tiles for this game of carcassonne.
   * @return the deck of tiles for this game of carcassonne
   */
  public Deck getDeck() {
    return deck;
  }

  /**
   * Sets the tile deck to deck.
   * @param deck the deck to to play this game of carcassonne with
   */
  public void setDeck(Deck deck) {
    this.deck = deck;
  }

  /**
   * Gets the turn number of this game.
   * @return the turn number of this game
   */
  public int getTurnNumber() {
    return turnNumber;
  }

  /**
   * Sets the turn number to turnNumber.
   * @param turnNumber the number of turns to set this game to.
   */
  public void setTurnNumber(int turnNumber) {
    this.turnNumber = turnNumber;
  }

  /**
   * Sets the board to board.
   * @param board the board with which to set this game to use.
   */
  public void setBoard(Board board) {
    this.board = board;
  }

  /**
   * Places a tile on the board. Returns a boolean letting the user know if the
   * game is over. (Game is over when there are no tiles left).
   *
   * @param p Where the player wants to place their tile
   * @param t The tile to place
   * @throws PosnTakenException if the posn is taken
   * @throws IllegalArgumentException if the posn is not a valid move
   */
  public void place(Posn p, Tile t) throws PosnTakenException,
      IllegalArgumentException {
    board.place(p, t);
  }

  /**
   * Places a tile on the board, without regards to valid moves. Returns a
   * boolean letting the user know if the game is over. (Game is over when there
   * are no tiles left).
   * 
   * @param p Where the player wants to place their tile
   * @param t The tile to place
   * @throws PosnTakenException if the posn is taken
   */
  public void forcePlace(Posn p, Tile t) throws PosnTakenException {
    board.forcePlace(p, t);
  }

  /**
   * Gets the next tile in the deck, if there is one.
   * @return the next tile in the deck, if there is one.
   */
  public Tile drawTile() {
    Tile t = deck.drawTile();
    curTile = t;
    return t;
  }

  /**
   * Sets gameOver to b.
   * @param b the boolean with which to set gameOver
   */
  public void setGameOver(boolean b) {
    isGameOver = b;
  }

  /**
   * Gets whether or not the game is over.
   * @return true if the game is over, false otherwise
   */
  public boolean isGameOver() {
    if (deck.isEmpty()) {
      isGameOver = true;
    }
    return isGameOver;
  }

  /**
   * Sets up the referee with a new game of Carcassonne. A new deck with the
   * basic Carcassonne tile set is created.
   */
  public void setupGame() {
    this.turnNumber = 0;
    players = new ArrayList<Player>();
    spectators = new ArrayList<>();
    List<Tile> tiles = null;
    try {
      tiles = buildDeck();
    } catch (InvalidEdgeException ite) {
      ite.printStackTrace();
    }
    deck = new Deck(tiles);
    board = new Board();
  }

  /**
   * Runs a text based version of Carcassonne.
   */
  public void run() {
    Player curPlayer;
    try (BufferedReader r =
        new BufferedReader(new InputStreamReader(System.in));) {
      while (!isGameOver()) {
        curPlayer = nextPlayer();
        System.out
            .println(String.format("It is %s's turn", curPlayer.getName()));
        takeTurn(curPlayer, r);
        turnNumber++;
        System.out.println(players.toString());
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  /**
   * Used by the text based version of the game.
   * @param p the player
   * @param r the reader to get input from the user
   * @throws IOException if there is IOException
   */
  public void takeTurn(Player p, BufferedReader r) throws IOException {
    Tile t = drawTile();
    System.out.println(t);

    List<Posn> validMoves = board.validMoves(t);
    System.out.println("The possible valid moves are:");
    System.out.println(validMoves);
    boolean noGood = true;

    Posn chosenPosn = null;
    while (noGood) {
      while (!validMoves.contains(chosenPosn)) {
        System.out
            .println("Where would you like to place the tile? Format: x y");
        System.out
            .println("to rotate left, input left, to rotate right input right");
        String input = r.readLine();
        String[] coord = input.split(" ");
        if (coord.length == 1) {
          if (coord[0].equals("right")) {
            t.rotateRight();
          } else {
            t.rotateLeft();
          }
        } else {
          chosenPosn = new Posn(Integer.parseInt(coord[0]),
              Integer.parseInt(coord[1]));
        }

      }
      try {
        board.place(chosenPosn, t);
        noGood = false;

      } catch (PosnTakenException e) {
        System.out.println(e.getMessage());
      }
    }

    noGood = true;
    while (noGood) {
      System.out.println("Would you like to meeple anywhere on the tile?");
      System.out
          .println("Options: n (no), t (top), b (bottom), l (left), r (right), c (center)");
      String input = r.readLine();
      try {
        switch (input) {
          case "n":
            System.out.println("You didn't place a meeple");
            noGood = false;
            break;
          case "b":
            System.out.println("You placed a meeple on the bottom feature");
            placeMeeple(chosenPosn, p, Direction.DOWN);
            System.out.println(p);
            noGood = false;
            break;
          case "l":
            System.out.println("You placed a meeple on the left feature");
            placeMeeple(chosenPosn, p, Direction.LEFT);
            System.out.println(p);
            noGood = false;
            break;
          case "r":
            System.out.println("You placed a meeple on the right feature");
            placeMeeple(chosenPosn, p, Direction.RIGHT);
            System.out.println(p);
            noGood = false;
            break;
          case "t":
            System.out.println("You placed a meeple on the top feature");
            placeMeeple(chosenPosn, p, Direction.UP);
            System.out.println(p);
            noGood = false;
            break;
          case "c":
            System.out.println("You placed a meeple on the center feature");
            placeMeeple(chosenPosn, p, Direction.CENTER);
            System.out.println(p);
            noGood = false;
            break;
        }
      } catch (OutOfMeeplesException e) {
        System.out.println("You have no more meeples left!");
        noGood = false;
      } catch (NullTileException e) {
        System.out.println("You shouldn't have gotten here!");
        e.printStackTrace();
      } catch (UnMeeplableException e) {
        System.out
            .println("That feature is unmeeplable, please try again or input n to move on");
      }
    }
    score(chosenPosn);
  }

  /**
   * Scores the board after the last tile and meeple have been placed has been
   * placed.
   *
   * @param p The tile that was last placed on the board
   */
  public void score(Posn p) {
    if (isGameOver()) {
      gameOverScoring();
    } else {
      normalScoring(p);
    }
  }

  /**
   * Scores the board after the game is over.
   */
  public void gameOverScoring() {
    scoreMonasteryEndgame();
    scoreRoadEndgame();
    scoreCityEndgame();
  }

  /**
   * Scores the board after the previous tile was placed and a meeple has been
   * placed or skipped on that tile.
   * @param p the position where the previous tile was placed on the board
   */
  public void normalScoring(Posn p) {
    scoreMonastery(p);
    scoreRoad(p);
    scoreCity(p);
  }

  /**
   * Scores monastery during normal gameplay.
   * @param p the previous tile's position
   */
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

  /**
   * Checks the surrounding tiles to score monasteries that may have been
   * finished by the placement of the previous tile.
   * @param p th eprevious position
   */
  public void scoreMonasteryHelper(Posn p) {
    Tile t = board.getBoard().get(p);
    if (t == null) {
      return;
    }
    Center c = t.getCenter1();
    if (c.getFeature() == Feature.MONASTERY) {
      Meeple m = c.getMeeple();
      if (m != null) {
        // check surrounding tiles for tiles
        int count = numSurroundingTiles(p);
        if (count == 9) {
          m.getPlayer().addScore(count);
          try {
            c.removeMeeple();
            board.getMeeplePosns().remove(p);
          } catch (NullMeepleException e) {
            e.printStackTrace();
          }
        }
      }
    }
  }

  /**
   * Scores roads based on the placement of the tile at position p.
   * @param p the previous position
   */
  public void scoreRoad(Posn p) {
    scoreRoadAt(p);
  }

  /**
   * Scores cities based on the placement of the tile at position p.
   * @param p the previous position
   */
  public void scoreCity(Posn p) {
    scoreCityAt(p);
  }

  /**
   * Scores monasteries at endgame.
   */
  public void scoreMonasteryEndgame() {
    Tile currTile;
    Posn currPosn;
    Center c;
    for (Entry<Posn, Tile> e : board.getBoard().entrySet()) {
      currTile = e.getValue();
      currPosn = e.getKey();
      c = currTile.getCenter1();

      if (c.getFeature() == Feature.MONASTERY && c.hasMeeple()) {
        c.getMeeple().getPlayer().addScore(numSurroundingTiles(currPosn));
        try {
          c.removeMeeple();
        } catch (NullMeepleException nme) {
          nme.printStackTrace();
        }
      }
    }
  }

  /**
   * Scores roads at endgame.
   */
  public void scoreRoadEndgame() {
    Set<Posn> meepledPosns = new HashSet<>(board.getMeeplePosns());

    for (Posn p : meepledPosns) {
      scoreRoadAt(p);
    }
  }

  /**
   * Scores cities at endgame.
   */
  public void scoreCityEndgame() {
    Set<Posn> meepledPosns = new HashSet<>(board.getMeeplePosns());

    for (Posn p : meepledPosns) {
      scoreCityAt(p);
    }
  }

  /**
   * Scores cities at position p.
   * @param p the previous position
   */
  private void scoreCityAt(Posn p) {
    Tile curTile = board.getBoard().get(p);
    Edge top = curTile.getTop();
    Edge bottom = curTile.getBottom();
    Edge left = curTile.getLeft();
    Edge right = curTile.getRight();
    int upScore = 0;
    int downScore = 0;
    int leftScore = 0;
    int rightScore = 0;
    if (curTile.getCenter1().getFeature() == Feature.CITY
        || curTile.getCenter2().getFeature() == Feature.CITY) {
      // check in all directions for cities, there are at least two connected,
      // and they count as the same city
      Set<Posn> visited = new HashSet<>();
      visited.add(p);
      Map<Posn, TileFeature> meepledCities = new HashMap<>();
      Finished f = new Finished(true);
      grabMeepleAtCenter(p, curTile, meepledCities);
      if (right.getFeature() == Feature.CITY) {
        if (right.hasMeeple()) {
          meepledCities.put(p, right);
        }
        rightScore = scoreCityHelper(p.withX(p.getX() + 1), visited,
            meepledCities, Direction.RIGHT, f);
      }
      if (left.getFeature() == Feature.CITY) {
        if (left.hasMeeple()) {
          meepledCities.put(p, left);
        }
        leftScore = scoreCityHelper(p.withX(p.getX() - 1), visited,
            meepledCities, Direction.LEFT, f);

      }
      if (bottom.getFeature() == Feature.CITY) {
        if (bottom.hasMeeple()) {
          meepledCities.put(p, bottom);
        }
        downScore = scoreCityHelper(p.withY(p.getY() - 1), visited,
            meepledCities, Direction.DOWN, f);
      }
      if (top.getFeature() == Feature.CITY) {
        if (top.hasMeeple()) {
          meepledCities.put(p, top);
        }
        upScore = scoreCityHelper(p.withY(p.getY() + 1), visited,
            meepledCities, Direction.UP, f);

      }
      int score = 1 + curTile.getShield() + upScore + downScore + leftScore
          + rightScore;
      if (isGameOver()) {
        scoreMeeples(meepledCities, score);
      } else if (f.isFinished()) {
        scoreMeeples(meepledCities, score * 2);
      }
    } else {
      // check in all directions for cities, but each one is its own city
      if (right.getFeature() == Feature.CITY) {
        // check to the right
        Map<Posn, TileFeature> rightCityMeeples = new HashMap<>();
        Set<Posn> visitedRight = new HashSet<>();
        Finished rightF = new Finished(true);
        visitedRight.add(p);
        if (right.hasMeeple()) {
          rightCityMeeples.put(p, right);
        }
        rightScore = scoreCityHelper(p.withX(p.getX() + 1), visitedRight,
            rightCityMeeples, Direction.RIGHT, rightF)
            + 1
            + curTile.getShield();

        if (isGameOver()) {
          scoreMeeples(rightCityMeeples, rightScore);
        } else if (rightF.isFinished()) {
          scoreMeeples(rightCityMeeples, rightScore * 2);
        }

      }
      if (left.getFeature() == Feature.CITY) {
        // check to the left
        Map<Posn, TileFeature> leftCityMeeples = new HashMap<>();
        Set<Posn> visitedLeft = new HashSet<>();
        Finished leftF = new Finished(true);
        visitedLeft.add(p);
        if (left.hasMeeple()) {
          leftCityMeeples.put(p, left);
        }
        leftScore = scoreCityHelper(p.withX(p.getX() - 1), visitedLeft,
            leftCityMeeples, Direction.LEFT, leftF) + 1 + curTile.getShield();

        if (isGameOver()) {
          scoreMeeples(leftCityMeeples, leftScore);
        } else if (leftF.isFinished()) {
          scoreMeeples(leftCityMeeples, leftScore * 2);
        }
      }
      if (bottom.getFeature() == Feature.CITY) {
        // check the bottom direction
        Map<Posn, TileFeature> downCityMeeples = new HashMap<>();
        Set<Posn> visitedDown = new HashSet<>();
        Finished downF = new Finished(true);
        visitedDown.add(p);
        if (bottom.hasMeeple()) {
          downCityMeeples.put(p, bottom);
        }
        downScore = scoreCityHelper(p.withY(p.getY() - 1), visitedDown,
            downCityMeeples, Direction.DOWN, downF) + 1 + curTile.getShield();

        if (isGameOver()) {
          scoreMeeples(downCityMeeples, downScore);
        } else if (downF.isFinished()) {
          scoreMeeples(downCityMeeples, downScore * 2);
        }
      }
      if (top.getFeature() == Feature.CITY) {
        // check the up direction
        Map<Posn, TileFeature> upCityMeeples = new HashMap<>();
        Set<Posn> visitedUp = new HashSet<>();
        Finished upF = new Finished(true);
        visitedUp.add(p);
        if (top.hasMeeple()) {
          upCityMeeples.put(p, top);
        }
        upScore = scoreCityHelper(p.withY(p.getY() + 1), visitedUp,
            upCityMeeples, Direction.UP, upF) + 1 + curTile.getShield();

        if (isGameOver()) {
          scoreMeeples(upCityMeeples, upScore);
        } else if (upF.isFinished()) {
          scoreMeeples(upCityMeeples, upScore * 2);
        }
      }
    }
  }

  /**
   * Iteratively goes through the tiles that contain cities connected to the
   * original city.
   * @param curPosn the Posn of the tile being checked for scoring
   * @param visited the set of positions that have already been checked
   * @param meepledCities the cities that have a meeple on them
   * @param d the Direction from which the last tile came from
   * @param f the Finished object keeping track of whether or not the city is
   *        finished
   * @return the number of tiles connected to the current city being scored.
   */
  private int scoreCityHelper(Posn curPosn, Set<Posn> visited,
      Map<Posn, TileFeature> meepledCities, Direction d, Finished f) {
    // recursively check each direction until there is no more center that
    // is a city. If a tile
    // has a shield, it must be connected to the current city checking, because
    // of the way the tiles are set up.
    Queue<Pair<Posn, Direction>> queue = new LinkedList<>();
    Posn toAdd;
    queue.add(new Pair<Posn, Direction>(curPosn, d));
    if (visited.contains(curPosn)) {
      return 0;
    }
    int score = 0;
    Posn p = curPosn;
    Direction dir = d;
    while (!queue.isEmpty()) {
      Pair<Posn, Direction> pair = queue.poll();
      p = pair.getP1();
      dir = pair.getP2();
      Tile curTile = board.getBoard().get(p);
      if (curTile == null) {
        // the city is not finished
        f.setFinished(false);
      } else {
        grabMeepleAtEdge(p, curTile, dir, meepledCities);
        if (!visited.contains(p)) {
          visited.add(p);

          score = score + 1 + curTile.getShield();

          if (curTile.getCenter1().getFeature() == Feature.CITY
              || curTile.getCenter2().getFeature() == Feature.CITY) {
            // The center is a city, so check all sides
            grabMeepleAtCenter(p, curTile, meepledCities);
            if (curTile.getBottom().getFeature() == Feature.CITY
                && dir != Direction.UP) {
              grabMeepleAtEdge(p, curTile, Direction.UP, meepledCities);
              toAdd = p.withY(p.getY() - 1);
              addToQueue(toAdd, Direction.DOWN, queue);
            }
            if (curTile.getTop().getFeature() == Feature.CITY
                && dir != Direction.DOWN) {
              grabMeepleAtEdge(p, curTile, Direction.DOWN, meepledCities);
              toAdd = p.withY(p.getY() + 1);
              addToQueue(toAdd, Direction.UP, queue);
            }
            if (curTile.getRight().getFeature() == Feature.CITY
                && dir != Direction.LEFT) {
              grabMeepleAtEdge(p, curTile, Direction.LEFT, meepledCities);
              toAdd = p.withX(p.getX() + 1);
              addToQueue(toAdd, Direction.RIGHT, queue);
            }
            if (curTile.getLeft().getFeature() == Feature.CITY
                && dir != Direction.RIGHT) {
              grabMeepleAtEdge(p, curTile, Direction.RIGHT, meepledCities);
              toAdd = p.withX(p.getX() - 1);
              addToQueue(toAdd, Direction.LEFT, queue);
            }
          }
        }

      }
    }
    return score;
  }

  /**
   * Adds the position-direction pair to the queue.
   * @param p the position
   * @param d the direction
   * @param queue the queue to add the pair to
   */
  private void addToQueue(Posn p, Direction d,
      Queue<Pair<Posn, Direction>> queue) {
    queue.add(new Pair<Posn, Direction>(p, d));
  }

  /**
   * Iteratively goes through the tiles that contain roads connected to the
   * original road.
   * @param curPosn the Posn of the tile being checked for scoring
   * @param visited the set of positions that have already been checked
   * @param meepledRoads the roads that have a meeple on them
   * @param d the Direction from which the last tile came from
   * @param f the Finished object keeping track of whether or not the road is
   *        finished
   * @return the number of tiles connected to the current road being scored.
   */
  private int scoreRoadHelper(Posn curPosn, Posn origPosn, Set<Posn> visited,
      Map<Posn, TileFeature> meepledRoads, Direction d, Finished f) {

    Tile curTile = board.getBoard().get(curPosn);
    int score = 0;
    while (curTile != null) {
      grabMeepleAtEdge(curPosn, curTile, d, meepledRoads);
      if (visited.contains(curPosn)) {
        return score;
      }
      visited.add(curPosn);
      score++;

      if (curTile.roadEnds()) {
        return score;
      }

      // if the road doesn't end at this tile, there is always a chance that
      // the meeple was placed in the center.
      grabMeepleAtCenter(curPosn, curTile, meepledRoads);
      if (curTile.getBottom().getFeature() == Feature.ROAD && d != Direction.UP) {
        // check meeple on bottom and continue in this direction
        grabMeepleAtEdge(curPosn, curTile, Direction.UP, meepledRoads);
        curPosn = curPosn.withY(curPosn.getY() - 1);
        d = Direction.DOWN;
      } else if (curTile.getRight().getFeature() == Feature.ROAD
          && d != Direction.LEFT) {
        // check meeple on right and continue in this direction
        grabMeepleAtEdge(curPosn, curTile, Direction.LEFT, meepledRoads);
        curPosn = curPosn.withX(curPosn.getX() + 1);
        d = Direction.RIGHT;
      } else if (curTile.getTop().getFeature() == Feature.ROAD
          && d != Direction.DOWN) {
        // check meeple on top and continue in this direction
        grabMeepleAtEdge(curPosn, curTile, Direction.DOWN, meepledRoads);
        curPosn = curPosn.withY(curPosn.getY() + 1);
        d = Direction.UP;
      } else if (curTile.getLeft().getFeature() == Feature.ROAD
          && d != Direction.RIGHT) {
        // check meeple on left and continue in this direction
        grabMeepleAtEdge(curPosn, curTile, Direction.RIGHT, meepledRoads);
        curPosn = curPosn.withX(curPosn.getX() - 1);
        d = Direction.LEFT;
      }

      curTile = board.getBoard().get(curPosn);
    }
    f.setFinished(false);
    return score;
  }

  /**
   * Scores the road at position p.
   * @param p the position where the previous tile was placed
   */
  private void scoreRoadAt(Posn p) {
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
        Map<Posn, TileFeature> rightRoadMeeples = new HashMap<>();
        Set<Posn> visitedRight = new HashSet<>();
        Finished rightF = new Finished(true);
        visitedRight.add(p);
        if (right.hasMeeple()) {
          rightRoadMeeples.put(p, right);
        }
        rightScore = scoreRoadHelper(p.withX(p.getX() + 1), p, visitedRight,
            rightRoadMeeples, Direction.RIGHT, rightF);
        if (isGameOver() || rightF.isFinished()) {
          scoreMeeples(rightRoadMeeples, rightScore + 1);
        }
      }
      if (left.getFeature() == Feature.ROAD) {
        // check to the left
        Map<Posn, TileFeature> leftRoadMeeples = new HashMap<>();
        Set<Posn> visitedLeft = new HashSet<>();
        Finished leftF = new Finished(true);
        visitedLeft.add(p);
        if (left.hasMeeple()) {
          leftRoadMeeples.put(p, left);
        }
        leftScore = scoreRoadHelper(p.withX(p.getX() - 1), p, visitedLeft,
            leftRoadMeeples, Direction.LEFT, leftF);
        if (isGameOver() || leftF.isFinished()) {
          scoreMeeples(leftRoadMeeples, leftScore + 1);
        }
      }
      if (top.getFeature() == Feature.ROAD) {
        // check to the top
        Map<Posn, TileFeature> topRoadMeeples = new HashMap<>();
        Set<Posn> visitedTop = new HashSet<>();
        Finished upF = new Finished(true);
        visitedTop.add(p);
        if (top.hasMeeple()) {
          topRoadMeeples.put(p, top);
        }
        upScore = scoreRoadHelper(p.withY(p.getY() + 1), p, visitedTop,
            topRoadMeeples, Direction.UP, upF);
        if (isGameOver() || upF.isFinished()) {
          scoreMeeples(topRoadMeeples, upScore + 1);
        }
      }
      if (bottom.getFeature() == Feature.ROAD) {
        // check to the bottom
        Map<Posn, TileFeature> bottomRoadMeeples = new HashMap<>();
        Set<Posn> visitedBottom = new HashSet<>();
        Finished downF = new Finished(true);
        visitedBottom.add(p);
        if (bottom.hasMeeple()) {
          bottomRoadMeeples.put(p, bottom);
        }
        downScore = scoreRoadHelper(p.withY(p.getY() - 1), p, visitedBottom,
            bottomRoadMeeples, Direction.DOWN, downF);
        if (isGameOver() || downF.isFinished()) {
          scoreMeeples(bottomRoadMeeples, downScore + 1);
        }
      }
    } else {
      Set<Posn> visited = new HashSet<>();
      Map<Posn, TileFeature> meepledRoads = new HashMap<>();
      grabMeepleAtCenter(p, curTile, meepledRoads);
      Finished f = new Finished(true);
      visited.add(p);

      upScore = 0;
      downScore = 0;
      leftScore = 0;
      rightScore = 0;
      if (right.getFeature() == Feature.ROAD) {
        // check to the right
        if (right.hasMeeple()) {
          meepledRoads.put(p, right);
        }
        rightScore = scoreRoadHelper(p.withX(p.getX() + 1), p, visited,
            meepledRoads, Direction.RIGHT, f);
      }
      if (left.getFeature() == Feature.ROAD) {
        // check to the left
        if (left.hasMeeple()) {
          meepledRoads.put(p, left);
        }
        leftScore = scoreRoadHelper(p.withX(p.getX() - 1), p, visited,
            meepledRoads, Direction.LEFT, f);
      }
      if (top.getFeature() == Feature.ROAD) {
        // check to the top
        if (top.hasMeeple()) {
          meepledRoads.put(p, top);
        }
        upScore = scoreRoadHelper(p.withY(p.getY() + 1), p, visited,
            meepledRoads, Direction.UP, f);
      }
      if (bottom.getFeature() == Feature.ROAD) {
        // check to the bottom
        if (bottom.hasMeeple()) {
          meepledRoads.put(p, bottom);
        }
        downScore = scoreRoadHelper(p.withY(p.getY() - 1), p, visited,
            meepledRoads, Direction.DOWN, f);
      }

      int score = 1 + upScore + downScore + leftScore + rightScore;
      // System.out.println("Total Score = " + score);
      if (isGameOver() || f.isFinished()) {
        scoreMeeples(meepledRoads, score);
      }
    }
  }

  /**
   * Gets the positions of the surrounding tiles as a list based on the Posn p.
   * @param p the position of the tile with which to check its surroundings
   * @return the list of surrounding Posns
   */
  private List<Posn> getSurroundingTiles(Posn p) {
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

  /**
   * Returns the number of surrounding tiles.
   * @param p the position with which to check surrounding tiles
   * @return the nubmer of surrounding tiles
   */
  private int numSurroundingTiles(Posn p) {
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

  /**
   * If there is a meeple on tile t at posn p at the opposite direction of d,
   * then put key p and meeple on t.(opposite direction of d) into meepled.
   * @param p the Posn of which to check the meeple being there
   * @param t the tile to check if there's a meeple there
   * @param d the opposite direction of which to check meeple placement
   * @param meepled a map of posn to meeple because only one meeple can be on
   *        any one tile
   */
  private void grabMeepleAtEdge(Posn p, Tile t, Direction d,
      Map<Posn, TileFeature> meepled) {
    if (d == Direction.LEFT && t.getRight().hasMeeple()) {
      meepled.put(p, t.getRight());
    } else if (d == Direction.RIGHT && t.getLeft().hasMeeple()) {
      meepled.put(p, t.getLeft());
    } else if (d == Direction.UP && t.getBottom().hasMeeple()) {
      meepled.put(p, t.getBottom());
    } else if (d == Direction.DOWN && t.getTop().hasMeeple()) {
      meepled.put(p, t.getTop());
    }
  }

  /**
   * If there is a meeple on tile t at posn p in the center
   * then put key p and meeple on t.getCenter1 into meepled.
   * @param p the Posn of which to check the meeple being there
   * @param t the tile to check if there's a meeple there
   * @param meepled a map of posn to tilefeature because only one meeple can be
   *        on
   *        any one tile
   */
  private void
      grabMeepleAtCenter(Posn p, Tile t, Map<Posn, TileFeature> meepled) {
    if (t.getCenter1().hasMeeple()) {
      meepled.put(p, t.getCenter1());
    }
  }

  /**
   * Scores the meeples in the meepledFeatures map.
   * @param meepledFeatures the map of posn -> tilefeature
   * @param baseScore the score to give to the player with the most meeples on
   *        this finished scorable feature
   */
  private void scoreMeeples(Map<Posn, TileFeature> meepledFeatures,
      int baseScore) {
    Map<Player, Integer> meeples = new HashMap<>();
    // System.out.println(meepledFeatures);
    for (Player p : players) {
      meeples.put(p, 0);
    }
    for (Entry<Posn, TileFeature> tf : meepledFeatures.entrySet()) {
      Player p = tf.getValue().getMeeple().getPlayer();
      int numMeeples = meeples.get(p);
      meeples.put(p, numMeeples + 1);
      try {
        tf.getValue().removeMeeple();
        board.getMeeplePosns().remove(tf.getKey());
      } catch (NullMeepleException e) {
        e.printStackTrace();
        System.out
            .println("shouldn't get here, if we do, reach scott at 2**-*4*-**2*");
        System.out.println(e.getMessage());
      }
    }

    int maxMeeples = 1;
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

  /**
   * Places a meeple on the tile at posn p in direction d.
   * @param posn the last posn placed on the board
   * @param player the current player
   * @param d the direction the player wants to meeple
   * @throws NullTileException if the tile based at Posn p is null.
   * @throws OutOfMeeplesException if the player is out of meeples.
   * @throws UnMeeplableException if the tilefeature the player wants to meeple
   *         is not meepleable
   */
  public void placeMeeple(Posn posn, Player player, Direction d)
      throws NullTileException, OutOfMeeplesException, UnMeeplableException {
    Tile t = board.getBoard().get(posn);
    if (!board.validMeeples(posn).contains(d)) {
      throw new UnMeeplableException();
    }
    if (t != null) {
      if (d == Direction.RIGHT) {
        t.getRight().setMeeple(new Meeple(player));
        player.useMeeple();
      } else if (d == Direction.LEFT) {
        t.getLeft().setMeeple(new Meeple(player));
        player.useMeeple();
      } else if (d == Direction.UP) {
        t.getTop().setMeeple(new Meeple(player));
        player.useMeeple();
      } else if (d == Direction.DOWN) {
        t.getBottom().setMeeple(new Meeple(player));
        player.useMeeple();
      } else if (d == Direction.CENTER) {
        t.getCenter1().setMeeple(new Meeple(player));
        player.useMeeple();
      }
      board.getMeeplePosns().add(posn);
      board.setTouchesMeeple(posn, d);
    }
  }

  /**
   * Adds a player to the current game.
   * @param player the player to add to the game
   */
  public void newPlayer(Player player) {
    if (players.size() < 4) {
      players.add(player);
    }
  }

  /**
   * Adds a spectator to the current game.
   * @param s the spectator to add to the game
   */
  public void newSpectator(Spectator s) {
    spectators.add(s);
  }

  /**
   * Gets the spectators of this game.
   * @return the spectators of this game as a list of spectators
   */
  public List<Spectator> getSpectators() {
    return spectators;
  }

  /**
   * Shuffles the order of the players (used usually only at the start of the
   * game)
   */
  public void shuffleOrder() {
    Collections.shuffle(players);
  }

  /**
   * Gets the next player of this game.
   * @return the next player
   */
  public Player nextPlayer() {
    curPlayer = players.get(turnNumber % players.size());
    turnNumber++;
    return curPlayer;
  }

  /**
   * Builds a deck with the standard Carcassonne tileset.
   * @return a List of Tiles
   * @throws InvalidEdgeException if an Edge is tried to be made with a
   *         monastery or endpoint
   */
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
    tiles.add(new Tile(2, new Center(endpoint), new Edge(road), new Edge(road),
        new Edge(road), new Edge(road), 0));

    // 4x 3-road 1-field w/endpoint
    for (i = 0; i < 4; i++) {
      tiles.add(new Tile(7, new Center(endpoint), new Edge(field), new Edge(
          road), new Edge(road), new Edge(road), 0));
    }

    // 3x 3-road 1-city w/endpoint
    for (i = 0; i < 3; i++) {
      tiles.add(new Tile(23, new Center(endpoint), new Edge(city), new Edge(
          road), new Edge(road), new Edge(road), 0));
    }

    // 8x straight road
    for (i = 0; i < 8; i++) {
      tiles.add(new Tile(10, new Center(road), new Edge(field), new Edge(road),
          new Edge(field), new Edge(road), 0));
    }

    // 9x curved road
    for (i = 0; i < 9; i++) {
      tiles.add(new Tile(15, new Center(road), new Edge(field),
          new Edge(field), new Edge(road), new Edge(road), 0));
    }

    // 4x 1-city w/straight road (one gets added to the front of the list so
    // only add 3 now
    for (i = 0; i < 3; i++) {
      tiles.add(new Tile(24, new Center(road), new Edge(city), new Edge(road),
          new Edge(field), new Edge(road), 0));
    }

    // 3x 1-city w/curved road from left
    for (i = 0; i < 3; i++) {
      tiles.add(new Tile(22, new Center(road), new Edge(city), new Edge(field),
          new Edge(road), new Edge(road), 0));
    }

    // 3x 1-city w/curved road from right
    for (i = 0; i < 3; i++) {
      tiles.add(new Tile(21, new Center(road), new Edge(city), new Edge(road),
          new Edge(road), new Edge(field), 0));
    }

    // 3x 2-sided city w/curved road
    for (i = 0; i < 3; i++) {
      tiles.add(new Tile(13, new Center(city), new Center(road),
          new Edge(city), new Edge(city), new Edge(road), new Edge(road), 0));
    }

    // 2x 2-sided city w/curved road and shield
    for (i = 0; i < 2; i++) {
      tiles.add(new Tile(14, new Center(city), new Center(road),
          new Edge(city), new Edge(city), new Edge(road), new Edge(road), 1));
    }

    // 2x one-roaded monastery
    for (i = 0; i < 2; i++) {
      tiles.add(new Tile(18, new Center(monastery), new Edge(field), new Edge(
          field), new Edge(road), new Edge(field), 0));
    }

    // 1x 1-road 3-city
    tiles.add(new Tile(5, new Center(city), new Edge(city), new Edge(city),
        new Edge(road), new Edge(city), 0));

    // 2x 1-road 3-city w/shield
    for (i = 0; i < 2; i++) {
      tiles.add(new Tile(6, new Center(city), new Edge(city), new Edge(city),
          new Edge(road), new Edge(city), 1));
    }

    // 4x 4-field w/monastery
    for (i = 0; i < 4; i++) {
      tiles.add(new Tile(19, new Center(monastery), new Edge(field), new Edge(
          field), new Edge(field), new Edge(field), 0));
    }

    // 5x 3-field 1-city
    for (i = 0; i < 5; i++) {
      tiles.add(new Tile(20, new Center(field), new Edge(city),
          new Edge(field), new Edge(field), new Edge(field), 0));
    }

    // 1x 2-city (bridge) 2-field
    tiles.add(new Tile(8, new Center(city), new Edge(field), new Edge(city),
        new Edge(field), new Edge(city), 0));

    // 2x 2-city (bridge) 2-field w/shield
    for (i = 0; i < 2; i++) {
      tiles.add(new Tile(9, new Center(city), new Edge(field), new Edge(city),
          new Edge(field), new Edge(city), 1));
    }

    // 3x 2-city (corner) 2-field
    for (i = 0; i < 3; i++) {
      tiles.add(new Tile(11, new Center(city), new Edge(city), new Edge(city),
          new Edge(field), new Edge(field), 0));
    }

    // 2x 2-city (corner) 2-field w/shield
    for (i = 0; i < 2; i++) {
      tiles.add(new Tile(12, new Center(city), new Edge(city), new Edge(city),
          new Edge(field), new Edge(field), 1));
    }

    // 3x 2-city (not-connected, opposite sides) 2-field
    for (i = 0; i < 3; i++) {
      tiles.add(new Tile(17, new Center(field), new Edge(city),
          new Edge(field), new Edge(city), new Edge(field), 0));
    }

    // 2x 2-city (not-connected, adjacent sides) 2-field
    for (i = 0; i < 2; i++) {
      tiles.add(new Tile(16, new Center(field), new Edge(city), new Edge(city),
          new Edge(field), new Edge(field), 0));
    }

    // 3x 3-city 1-field
    for (i = 0; i < 3; i++) {
      tiles.add(new Tile(3, new Center(city), new Edge(city), new Edge(city),
          new Edge(field), new Edge(city), 0));
    }

    // 1x 3-city 1-field w/shield
    tiles.add(new Tile(4, new Center(city), new Edge(city), new Edge(city),
        new Edge(field), new Edge(city), 1));

    // 1x 4-city w/shield
    tiles.add(new Tile(1, new Center(city), new Edge(city), new Edge(city),
        new Edge(city), new Edge(city), 1));

    Collections.shuffle(tiles);

    // In the instruction manual, this tile is said to be the first tile, so
    // we add it to the top of the deck.
    tiles.add(0, new Tile(24, new Center(road), new Edge(city), new Edge(road),
        new Edge(field), new Edge(road), 0));

    // add river tiles to the deck

    List<Tile> riverTiles = new ArrayList<>();

    // 2x straight river 2 field
    for (i = 0; i < 2; i++) {
      riverTiles.add(new Tile(new Center(river), new Edge(field), new Edge(
          river), new Edge(field), new Edge(river), 0));
    }

    // 2x curved river 2 field
    for (i = 0; i < 2; i++) {
      riverTiles.add(new Tile(new Center(river), new Edge(field), new Edge(
          field), new Edge(river), new Edge(river), 0));
    }

    // 1x curved river city on other sides
    riverTiles.add(new Tile(new Center(city), new Edge(city), new Edge(city),
        new Edge(river), new Edge(river), 0));

    // 1x straight river city on other sides
    riverTiles.add(new Tile(new Center(city), new Edge(city), new Edge(river),
        new Edge(city), new Edge(river), 0));

    // 1x monastery center road on bottom straight river
    riverTiles.add(new Tile(new Center(monastery), new Edge(field), new Edge(
        river), new Edge(road), new Edge(river), 0));

    // 1x city top, road bottom, straight river
    riverTiles.add(new Tile(new Center(endpoint), new Edge(city), new Edge(
        river), new Edge(road), new Edge(river), 0));

    // 1x straight road, straight river
    riverTiles.add(new Tile(new Center(road), new Edge(road), new Edge(river),
        new Edge(road), new Edge(river), 0));

    // 1x curved road, curved river
    riverTiles.add(new Tile(new Center(field), new Edge(road), new Edge(road),
        new Edge(river), new Edge(river), 0));

    Collections.shuffle(riverTiles);

    // 1x river end
    riverTiles.add(riverTiles.size(), new Tile(new Center(river), new Edge(
        field), new Edge(field), new Edge(river), new Edge(field), 0));

    // 1x river start
    riverTiles.add(0, new Tile(new Center(river), new Edge(field), new Edge(
        field), new Edge(river), new Edge(field), 0));

    // tiles.addAll(0, riverTiles);
    return tiles;
  }
}
