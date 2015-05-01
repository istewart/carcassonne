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

public class Referee {
  private List<Player> players;
  private Deck deck;
  private int turnNumber;
  private Board board;
  private Tile curTile = null;
  private Posn curPosn = null;
  private Player curPlayer;
  private boolean isGameOver;

  public Referee() {
    // TODO should be done but there might be something else but I can't think
    // of more setup (and it should go in setupGame()
    setupGame();
  }

  public Player getCurPlayer() {
    return curPlayer;
  }

  public Tile getCurTile() {
    return curTile;
  }

  public void setCurPosn(Posn p) {
    curPosn = p;
  }

  public Posn getCurPosn() {
    return curPosn;
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
   * @param t The tile to place
   * @throws PosnTakenException if the posn is taken
   */
  public void place(Posn p, Tile t) throws PosnTakenException {
    // TODO pretty sure throw, right, because if the Posn is taken the
    // handler/main gameplay function should handle that.
    board.place(p, t);
  }

  // Should the referee handle all interaction with the deck? Yes, right?
  public Tile drawTile() /* throws EmptyDeckException */{
    Tile t = deck.drawTile();
    curTile = t;
    return t;
  }

  public void setGameOver(boolean b) {
    isGameOver = b;
  }

  public boolean isGameOver() {
    if (deck.isEmpty()) {
      isGameOver = true;
    }
    return isGameOver;
  }

  public void setupGame() {
    this.turnNumber = 0;
    players = new ArrayList<Player>();
    // this.isGameOver = false;
    List<Tile> tiles = null;
    try {
      tiles = buildDeck();
    } catch (InvalidEdgeException ite) {
      System.out.println("wont be reached, tiles currently hardcoded");
    }
    deck = new Deck(tiles);
    board = new Board();
  }

  // main/handlers handles this
  public void run() {
    Player curPlayer;
    try (BufferedReader r =
        new BufferedReader(new InputStreamReader(System.in));) {
      while (!isGameOver()) {
        curPlayer = nextPlayer();
        System.out.println(String
            .format("It is %s's turn", curPlayer.getName()));
        takeTurn(curPlayer, r);
        turnNumber++;
        System.out.println(players.toString());
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  // Don't need this, handled by the handlers/main
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
          chosenPosn =
              new Posn(Integer.parseInt(coord[0]), Integer.parseInt(coord[1]));
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
      // System.out.println(t.validMeeples().toString());
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
            board.getMeeplePosns().remove(p);
          } catch (NullMeepleException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
        }
      }
    }
  }

  public void scoreRoad(Posn p) {
    scoreRoadAt(p);
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
    Set<Posn> meepledPosns = new HashSet<>(board.getMeeplePosns());

    for (Posn p : meepledPosns) {
      scoreRoadAt(p);
    }
  }

  public void scoreCityEndgame() {
    Set<Posn> meepledPosns = new HashSet<>(board.getMeeplePosns());

    for (Posn p : meepledPosns) {
      scoreCityAt(p);
    }
  }

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
    if (curTile.getCenter().getFeature() == Feature.CITY) {
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
        rightScore =
            scoreCityHelper(p.withX(p.getX() + 1), p, visited,
                meepledCities, Direction.RIGHT, f);
      }
      if (left.getFeature() == Feature.CITY) {
        if (left.hasMeeple()) {
          meepledCities.put(p, left);
        }
        leftScore =
            scoreCityHelper(p.withX(p.getX() - 1), p, visited,
                meepledCities, Direction.LEFT, f);
      }
      if (bottom.getFeature() == Feature.CITY) {
        if (bottom.hasMeeple()) {
          meepledCities.put(p, bottom);
        }
        downScore =
            scoreCityHelper(p.withY(p.getY() - 1), p, visited,
                meepledCities, Direction.DOWN, f);
      }
      if (top.getFeature() == Feature.CITY) {
        if (top.hasMeeple()) {
          meepledCities.put(p, top);
        }
        upScore =
            scoreCityHelper(p.withY(p.getY() + 1), p, visited,
                meepledCities, Direction.UP, f);
      }
      int score =
          1 + curTile.getShield() + upScore + downScore + leftScore
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
        rightScore =
            scoreCityHelper(p.withX(p.getX() + 1), p, visitedRight,
                rightCityMeeples, Direction.RIGHT, rightF) + 1
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
        leftScore = scoreCityHelper(p.withX(p.getX() - 1), p, visitedLeft,
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
        downScore = scoreCityHelper(p.withY(p.getY() - 1), p, visitedDown,
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
        upScore = scoreCityHelper(p.withY(p.getY() + 1), p, visitedUp,
            upCityMeeples, Direction.UP, upF) + 1 + curTile.getShield();

        if (isGameOver()) {
          scoreMeeples(upCityMeeples, upScore);
        } else if (upF.isFinished()) {
          scoreMeeples(upCityMeeples, upScore * 2);
        }
      }
    }
  }

  private int scoreCityHelper(Posn curPosn, Posn prevPosn, Set<Posn> visited,
      Map<Posn, TileFeature> meepledCities, Direction d, Finished f) {
    // recursively check each direction until there is no more center that
    // is a city. If a tile
    // has a shield, it must be connected to the current city checking, because
    // of the way the tiles are set up.
    Queue<Pair<Posn, Direction>> queue = new LinkedList<>();
    Posn toAdd;
    queue.add(new Pair<Posn, Direction>(curPosn, d));
    int score = 0;
    Posn p = curPosn;
    Direction dir = d;
    while (!queue.isEmpty()) {
      Pair<Posn, Direction> pair = queue.poll();
      p = pair.getP1();
      dir = pair.getP2();
      visited.add(p);
      Tile curTile = board.getBoard().get(p);
      if (curTile == null) {
        // the city is not finished
        f.setFinished(false);
      } else {
        score = score + 1 + curTile.getShield();
        grabMeepleAtEdge(p, curTile, dir, meepledCities);
        if (curTile.getCenter().getFeature() == Feature.CITY) {
          // The center is a city, so check all sides
          grabMeepleAtCenter(p, curTile, meepledCities);
          if (curTile.getBottom().getFeature() == Feature.CITY
              && dir != Direction.UP) {
            grabMeepleAtEdge(p, curTile, Direction.UP, meepledCities);
            toAdd = p.withY(p.getY() - 1);
            addToQueue(toAdd, Direction.DOWN, queue, visited);
          }
          if (curTile.getTop().getFeature() == Feature.CITY
              && dir != Direction.DOWN) {
            grabMeepleAtEdge(p, curTile, Direction.DOWN, meepledCities);
            toAdd = p.withY(p.getY() + 1);
            addToQueue(toAdd, Direction.UP, queue, visited);
          }
          if (curTile.getRight().getFeature() == Feature.CITY
              && dir != Direction.LEFT) {
            grabMeepleAtEdge(p, curTile, Direction.LEFT, meepledCities);
            toAdd = p.withX(p.getX() + 1);
            addToQueue(toAdd, Direction.RIGHT, queue, visited);
          }
          if (curTile.getLeft().getFeature() == Feature.CITY
              && dir != Direction.RIGHT) {
            grabMeepleAtEdge(p, curTile, Direction.RIGHT, meepledCities);
            toAdd = p.withX(p.getX() - 1);
            addToQueue(toAdd, Direction.LEFT, queue, visited);
          }
        }
      }
    }
    return score;
  }

  private void addToQueue(Posn p, Direction d,
      Queue<Pair<Posn, Direction>> queue, Set<Posn> visited) {
    if (!visited.contains(p)) {
      queue.add(new Pair<Posn, Direction>(p, d));
    }
  }

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

  private void
      grabMeepleAtCenter(Posn p, Tile t, Map<Posn, TileFeature> meepled) {
    if (t.getCenter().hasMeeple()) {
      meepled.put(p, t.getCenter());
    }
  }

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

  private Player getPlayer(int id) {
    for (Player p : players) {
      if (p.getId() == id) {
        return p;
      }
    }
    return null;
  }

  public void placeMeeple(Posn posn, Player player,
      Direction d) throws NullTileException, OutOfMeeplesException,
      UnMeeplableException {
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
        t.getCenter().setMeeple(new Meeple(player));
        player.useMeeple();
      }
      board.getMeeplePosns().add(posn);
      board.setTouchesMeeple(posn, d);
    } else {
      /*
       * throw new NullTileException(
       * "There is no tile at the given posn, how can I put a meeple on it??");
       */
    }
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
    curPlayer = players.get(turnNumber % players.size());
    turnNumber++;
    return curPlayer;
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
    tiles.add(new Tile(2, new Center(endpoint), new Edge(road), new Edge(road),
        new Edge(road), new Edge(road), 0));

    // 4x 3-road 1-field w/endpoint
    for (i = 0; i < 4; i++) {
      tiles.add(new Tile(7, new Center(endpoint), new Edge(field), new Edge(
          road),
          new Edge(road), new Edge(road), 0));
    }

    // 3x 3-road 1-city w/endpoint
    for (i = 0; i < 3; i++) {
      tiles.add(new Tile(23, new Center(endpoint), new Edge(city), new Edge(
          road),
          new Edge(road), new Edge(road), 0));
    }

    // 8x straight road
    for (i = 0; i < 8; i++) {
      tiles.add(new Tile(10, new Center(road), new Edge(field), new Edge(road),
          new Edge(field), new Edge(road), 0));
    }

    // 9x curved road
    for (i = 0; i < 9; i++) {
      tiles.add(new Tile(15, new Center(road), new Edge(field),
          new Edge(field),
          new Edge(road), new Edge(road), 0));
    }

    // 4x 1-city w/straight road
    for (i = 0; i < 4; i++) {
      tiles.add(new Tile(24, new Center(road), new Edge(city), new Edge(road),
          new Edge(field), new Edge(road), 0));
    }

    // 3x 1-city w/curved road from left
    for (i = 0; i < 3; i++) {
      tiles.add(new Tile(22, new Center(road), new Edge(city),
          new Edge(field),
          new Edge(road), new Edge(road), 0));
    }

    // 3x 1-city w/curved road from right
    for (i = 0; i < 3; i++) {
      tiles.add(new Tile(21, new Center(road), new Edge(city), new Edge(road),
          new Edge(road), new Edge(field), 0));
    }

    // 3x 2-sided city w/curved road
    /*
     * for (i = 0; i < 3; i++) {
     * tiles.add(new Tile(13, new Center(city), new Edge(city), new Edge(city),
     * new Edge(road), new Edge(road), 0));
     * }
     */

    // 2x 2-sided city w/curved road and shield
    /*
     * for (i = 0; i < 2; i++) {
     * tiles.add(new Tile(14, new Center(city), new Edge(city), new Edge(city),
     * new Edge(road), new Edge(road), 1));
     * }
     */

    // 2x one-roaded monastery
    for (i = 0; i < 2; i++) {
      tiles.add(new Tile(18, new Center(monastery), new Edge(field),
          new Edge(field), new Edge(road), new Edge(field), 0));
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
      tiles.add(new Tile(19, new Center(monastery), new Edge(field),
          new Edge(field), new Edge(field), new Edge(field), 0));
    }

    // 5x 3-field 1-city
    for (i = 0; i < 5; i++) {
      tiles.add(new Tile(20, new Center(field), new Edge(city),
          new Edge(field),
          new Edge(field), new Edge(field), 0));
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
          new Edge(field),
          new Edge(city), new Edge(field), 0));
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

    // add river tiles to the deck

    List<Tile> riverTiles = new ArrayList<>();

    // 2x straight river 2 field
    for (i = 0; i < 2; i++) {
      riverTiles.add(new Tile(new Center(river), new Edge(field), new Edge(
          river),
          new Edge(field), new Edge(river), 0));
    }

    // 2x curved river 2 field
    for (i = 0; i < 2; i++) {
      riverTiles.add(new Tile(new Center(river), new Edge(field), new Edge(
          field),
          new Edge(river), new Edge(river), 0));
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
    riverTiles.add(new Tile(new Center(road), new Edge(road), new Edge(
        river), new Edge(road), new Edge(river), 0));

    // 1x curved road, curved river
    riverTiles.add(new Tile(new Center(field), new Edge(road), new Edge(
        road), new Edge(river), new Edge(river), 0));

    Collections.shuffle(riverTiles);

    // 1x river end
    riverTiles.add(riverTiles.size(), new Tile(new Center(river), new Edge(
        field), new Edge(field), new Edge(river), new Edge(field), 0));

    // 1x river start
    riverTiles.add(0, new Tile(new Center(river), new Edge(
        field), new Edge(field), new Edge(river), new Edge(field), 0));

    // tiles.addAll(0, riverTiles);

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
    r.newPlayer(new Player(0, "Scott"));
    r.newPlayer(new Player(1, "Ian"));
    r.shuffleOrder();
    r.run();

  }

}
