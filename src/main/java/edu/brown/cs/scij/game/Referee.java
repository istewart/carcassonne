package edu.brown.cs.scij.game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
import edu.brown.cs.scij.tile.OutOfMeeplesException;
import edu.brown.cs.scij.tile.Tile;
import edu.brown.cs.scij.tile.TileFeature;
import edu.brown.cs.scij.tile.UnMeeplableException;

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

  // Probably don't need this, handled by the handlers/main
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
        String input = r.readLine();
        String[] coord = input.split(" ");
        chosenPosn =
            new Posn(Integer.parseInt(coord[0]), Integer.parseInt(coord[1]));
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
   * @param prevTile the tile that was last placed on the board
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
    for (Posn p : board.getBoard().keySet()) {
      scoreRoadAt(p);
    }
  }

  public void scoreCityEndgame() {
    for (Posn p : board.getBoard().keySet()) {
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
      Set<TileFeature> meepledCities = new HashSet<>();
      Finished f = new Finished(true);
      grabMeepleAtCenter(curTile, meepledCities);
      if (right.getFeature() == Feature.CITY) {
        if (right.hasMeeple()) {
          meepledCities.add(right);
        }
        rightScore =
            scoreCityHelper(p.withX(p.getX() + 1), p, visited,
                meepledCities, Direction.LEFT, 0, f);
      }
      if (left.getFeature() == Feature.CITY) {
        if (left.hasMeeple()) {
          meepledCities.add(left);
        }
        leftScore =
            scoreCityHelper(p.withX(p.getX() - 1), p, visited,
                meepledCities, Direction.RIGHT, 0, f);
      }
      if (bottom.getFeature() == Feature.CITY) {
        if (bottom.hasMeeple()) {
          meepledCities.add(bottom);
        }
        downScore =
            scoreCityHelper(p.withY(p.getY() - 1), p, visited,
                meepledCities, Direction.UP, 0, f);
      }
      if (top.getFeature() == Feature.CITY) {
        if (top.hasMeeple()) {
          meepledCities.add(top);
        }
        upScore =
            scoreCityHelper(p.withY(p.getY() + 1), p, visited,
                meepledCities, Direction.DOWN, 0, f);
      }
      int score = 1 + upScore + downScore + leftScore + rightScore;
      if (isGameOver()) {
        scoreMeeples(meepledCities, score);
      } else if (f.isFinished()) {
        scoreMeeples(meepledCities, score * 2);
      }
    } else {
      // check in all directions for cities, but each one is its own city
      if (right.getFeature() == Feature.CITY) {
        // check to the right
        Set<TileFeature> rightCityMeeples = new HashSet<>();
        Set<Posn> visitedRight = new HashSet<>();
        Finished rightF = new Finished(true);
        visitedRight.add(p);
        if (right.hasMeeple()) {
          rightCityMeeples.add(right);
        }
        rightScore = scoreCityHelper(p.withX(p.getX() + 1), p, visitedRight,
            rightCityMeeples, Direction.LEFT, 1 + curTile.getShield(), rightF);

        if (isGameOver()) {
          scoreMeeples(rightCityMeeples, rightScore);
        } else if (rightF.isFinished()) {
          scoreMeeples(rightCityMeeples, rightScore * 2);
        }

      }
      if (left.getFeature() == Feature.CITY) {
        // check to the left
        Set<TileFeature> leftCityMeeples = new HashSet<>();
        Set<Posn> visitedLeft = new HashSet<>();
        Finished leftF = new Finished(true);
        visitedLeft.add(p);
        if (left.hasMeeple()) {
          leftCityMeeples.add(left);
        }
        leftScore = scoreCityHelper(p.withX(p.getX() - 1), p, visitedLeft,
            leftCityMeeples, Direction.RIGHT, 1 + curTile.getShield(), leftF);

        if (isGameOver()) {
          scoreMeeples(leftCityMeeples, leftScore);
        } else if (leftF.isFinished()) {
          scoreMeeples(leftCityMeeples, leftScore * 2);
        }
      }
      if (bottom.getFeature() == Feature.CITY) {
        // check the bottom direction
        Set<TileFeature> downCityMeeples = new HashSet<>();
        Set<Posn> visitedDown = new HashSet<>();
        Finished downF = new Finished(true);
        visitedDown.add(p);
        if (bottom.hasMeeple()) {
          downCityMeeples.add(bottom);
        }
        downScore = scoreCityHelper(p.withY(p.getY() - 1), p, visitedDown,
            downCityMeeples, Direction.UP, 1 + curTile.getShield(), downF);

        if (isGameOver()) {
          scoreMeeples(downCityMeeples, downScore);
        } else if (downF.isFinished()) {
          scoreMeeples(downCityMeeples, downScore * 2);
        }
      }
      if (top.getFeature() == Feature.CITY) {
        // check the up direction
        Set<TileFeature> upCityMeeples = new HashSet<>();
        Set<Posn> visitedUp = new HashSet<>();
        Finished upF = new Finished(true);
        visitedUp.add(p);
        if (top.hasMeeple()) {
          upCityMeeples.add(top);
        }
        upScore = scoreCityHelper(p.withY(p.getY() - 1), p, visitedUp,
            upCityMeeples, Direction.DOWN, 1 + curTile.getShield(), upF);

        if (isGameOver()) {
          scoreMeeples(upCityMeeples, upScore);
        } else if (upF.isFinished()) {
          scoreMeeples(upCityMeeples, upScore * 2);
        }
      }
    }
  }

  private int scoreCityHelper(Posn curPosn, Posn prevPosn, Set<Posn> visited,
      Set<TileFeature> meepledCities, Direction d, int count, Finished f) {
    // recursively check each direction until there is no more center that
    // is a city. If the prev posn maps to a null tile, then return 0. If a tile
    // has a shield, it must be connected to the current city checking, because
    // of the way the tiles are set up.
    if (curPosn.equals(prevPosn)) {
      return 0;
    }
    if (visited.contains(curPosn)) {
      return count;
    } else {
      visited.add(curPosn);
    }
    Tile curTile = board.getBoard().get(curPosn);
    if (curTile == null) {
      f.setFinished(false);
      return count;
    } else {
      grabMeepleAtEdge(curTile, d, meepledCities);
      if (curTile.getCenter().getFeature() != Feature.CITY) {
        // city ends at this tile
        return count + 1 + curTile.getShield();
      } else {
        // city keeps going
        grabMeepleAtCenter(curTile, meepledCities);
        int downScore = 0;
        int upScore = 0;
        int leftScore = 0;
        int rightScore = 0;
        if (curTile.getBottom().getFeature() == Feature.CITY) {
          grabMeepleAtEdge(curTile, Direction.DOWN, meepledCities);
          downScore =
              scoreCityHelper(curPosn.withY(curPosn.getY() - 1),
                  curPosn, visited, meepledCities, Direction.UP, count + 1
                      + curTile.getShield(), f);
        }
        if (curTile.getTop().getFeature() == Feature.CITY) {
          grabMeepleAtEdge(curTile, Direction.UP, meepledCities);
          upScore =
              scoreCityHelper(curPosn.withY(curPosn.getY() + 1),
                  curPosn, visited, meepledCities, Direction.DOWN, count + 1
                      + curTile.getShield(), f);
        }
        if (curTile.getRight().getFeature() == Feature.CITY) {
          grabMeepleAtEdge(curTile, Direction.RIGHT, meepledCities);
          rightScore =
              scoreCityHelper(curPosn.withX(curPosn.getX() + 1),
                  curPosn, visited, meepledCities, Direction.LEFT, count + 1
                      + curTile.getShield(), f);
        }
        if (curTile.getLeft().getFeature() == Feature.CITY) {
          grabMeepleAtEdge(curTile, Direction.LEFT, meepledCities);
          leftScore =
              scoreCityHelper(curPosn.withX(curPosn.getX() - 1),
                  curPosn, visited, meepledCities, Direction.RIGHT, count + 1
                      + curTile.getShield(), f);
        }

        return upScore + downScore + leftScore + rightScore;
      }
    }
  }

  private int scoreRoadHelper(Posn curPosn, Posn prevPosn, Set<Posn> visited,
      Set<TileFeature> meepledRoads, Direction d, int count, Finished f) {
    if (curPosn.equals(prevPosn)) {
      return 0;
    }
    if (visited.contains(curPosn)) {
      return count;
    } else {
      visited.add(curPosn);
    }
    Tile curTile = board.getBoard().get(curPosn);
    if (curTile == null) {
      f.setFinished(false);
      return count;
    } else {
      grabMeepleAtEdge(curTile, d, meepledRoads);
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
          grabMeepleAtEdge(curTile, Direction.DOWN, meepledRoads);
          downScore = scoreRoadHelper(curPosn.withY(curPosn.getY() - 1),
              curPosn, visited, meepledRoads, Direction.UP, count + 1, f);
        }
        if (curTile.getRight().getFeature() == Feature.ROAD) {
          // check meeple on right and continue in this direction
          grabMeepleAtEdge(curTile, Direction.RIGHT, meepledRoads);
          rightScore = scoreRoadHelper(curPosn.withX(curPosn.getX() + 1),
              curPosn, visited, meepledRoads, Direction.LEFT, count + 1, f);
        }
        if (curTile.getTop().getFeature() == Feature.ROAD) {
          // check meeple on top and continue in this direction
          grabMeepleAtEdge(curTile, Direction.UP, meepledRoads);
          upScore = scoreRoadHelper(curPosn.withY(curPosn.getY() + 1), curPosn,
              visited, meepledRoads, Direction.DOWN, count + 1, f);
        }
        if (curTile.getLeft().getFeature() == Feature.ROAD) {
          // check meeple on left and continue in this direction
          grabMeepleAtEdge(curTile, Direction.LEFT, meepledRoads);
          upScore = scoreRoadHelper(curPosn.withX(curPosn.getX() - 1), curPosn,
              visited, meepledRoads, Direction.RIGHT, count + 1, f);
        }

        return downScore + upScore + leftScore + rightScore;
      }
    }
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
        Set<TileFeature> rightRoadMeeples = new HashSet<>();
        Set<Posn> visitedRight = new HashSet<>();
        Finished rightF = new Finished(true);
        visitedRight.add(p);
        if (right.hasMeeple()) {
          rightRoadMeeples.add(right);
        }
        rightScore = scoreRoadHelper(p.withX(p.getX() + 1), p, visitedRight,
            rightRoadMeeples, Direction.LEFT, 0, rightF);
        if (isGameOver()) {
          scoreMeeples(rightRoadMeeples, rightScore + 1);
        } else if (rightF.isFinished()) {
          scoreMeeples(rightRoadMeeples, rightScore + 1);
        }
      }
      if (left.getFeature() == Feature.ROAD) {
        // check to the left
        Set<TileFeature> leftRoadMeeples = new HashSet<>();
        Set<Posn> visitedLeft = new HashSet<>();
        Finished leftF = new Finished(true);
        visitedLeft.add(p);
        if (left.hasMeeple()) {
          leftRoadMeeples.add(left);
        }
        leftScore = scoreRoadHelper(p.withX(p.getX() - 1), p, visitedLeft,
            leftRoadMeeples, Direction.RIGHT, 0, leftF);
        if (isGameOver()) {
          scoreMeeples(leftRoadMeeples, leftScore + 1);
        } else if (leftF.isFinished()) {
          scoreMeeples(leftRoadMeeples, leftScore + 1);
        }
      }
      if (top.getFeature() == Feature.ROAD) {
        // check to the top
        Set<TileFeature> topRoadMeeples = new HashSet<>();
        Set<Posn> visitedTop = new HashSet<>();
        Finished upF = new Finished(true);
        visitedTop.add(p);
        if (top.hasMeeple()) {
          topRoadMeeples.add(top);
        }
        upScore = scoreRoadHelper(p.withY(p.getY() + 1), p, visitedTop,
            topRoadMeeples, Direction.DOWN, 0, upF);
        if (isGameOver()) {
          scoreMeeples(topRoadMeeples, upScore + 1);
        } else if (upF.isFinished()) {
          scoreMeeples(topRoadMeeples, upScore + 1);
        }
      }
      if (bottom.getFeature() == Feature.ROAD) {
        // check to the bottom
        Set<TileFeature> bottomRoadMeeples = new HashSet<>();
        Set<Posn> visitedBottom = new HashSet<>();
        Finished downF = new Finished(true);
        visitedBottom.add(p);
        if (bottom.hasMeeple()) {
          bottomRoadMeeples.add(bottom);
        }
        downScore = scoreRoadHelper(p.withY(p.getY() - 1), p, visitedBottom,
            bottomRoadMeeples, Direction.UP, 0, downF);
        if (isGameOver()) {
          scoreMeeples(bottomRoadMeeples, downScore + 1);
        } else if (downF.isFinished()) {
          scoreMeeples(bottomRoadMeeples, downScore + 1);
        }
      }
    } else {
      Set<Posn> visited = new HashSet<>();
      Set<TileFeature> meepledRoads = new HashSet<>();
      grabMeepleAtCenter(curTile, meepledRoads);
      Finished f = new Finished(true);

      upScore = 0;
      downScore = 0;
      leftScore = 0;
      rightScore = 0;
      if (right.getFeature() == Feature.ROAD) {
        // check to the right
        if (right.hasMeeple()) {
          meepledRoads.add(right);
        }
        rightScore = scoreRoadHelper(p.withX(p.getX() + 1), p, visited,
            meepledRoads, Direction.LEFT, 0, f);
      }
      if (left.getFeature() == Feature.ROAD) {
        // check to the left
        if (left.hasMeeple()) {
          meepledRoads.add(left);
        }
        leftScore = scoreRoadHelper(p.withX(p.getX() - 1), p, visited,
            meepledRoads, Direction.RIGHT, 0, f);
      }
      if (top.getFeature() == Feature.ROAD) {
        // check to the top
        if (top.hasMeeple()) {
          meepledRoads.add(top);
        }
        upScore = scoreRoadHelper(p.withY(p.getY() + 1), p, visited,
            meepledRoads, Direction.DOWN, 0, f);
      }
      if (bottom.getFeature() == Feature.ROAD) {
        // check to the bottom
        if (bottom.hasMeeple()) {
          meepledRoads.add(bottom);
        }
        downScore = scoreRoadHelper(p.withY(p.getY() - 1), p, visited,
            meepledRoads, Direction.UP, 0, f);
      }

      int score = 1 + upScore + downScore + leftScore + rightScore;
      if (isGameOver()) {
        scoreMeeples(meepledRoads, score + 1);
      } else if (f.isFinished()) {
        scoreMeeples(meepledRoads, score + 1);
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

  private void grabMeepleAtEdge(Tile t, Direction d,
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

  private void grabMeepleAtCenter(Tile t, Set<TileFeature> meepled) {
    if (t.getCenter().hasMeeple()) {
      meepled.add(t.getCenter());
    }
  }

  private void scoreMeeples(Set<TileFeature> meepledFeatures, int baseScore) {
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

  public void placeMeeple(Posn posn, Player player,
      Direction d) throws NullTileException, OutOfMeeplesException,
      UnMeeplableException {
    Tile t = board.getBoard().get(posn);
    System.out.println(String.valueOf(t));
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
    return players.get(turnNumber % players.size());
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
    /*
     * Player p = new Player(0, "Scott");
     * try {
     * System.out.println(p);
     * p.useMeeple();
     * System.out.println(p);
     * } catch (OutOfMeeplesException e) {
     * // TODO Auto-generated catch block
     * e.printStackTrace();
     * }
     * Referee r = new Referee();
     * try {
     * r.getBoard()
     * .place(
     * new Posn(0, 0),
     * new Tile(new Center(Feature.CITY), new Edge(Feature.CITY),
     * new Edge(Feature.CITY), new Edge(Feature.CITY), new Edge(
     * Feature.CITY), 0));
     * r.placeMeeple(new Posn(0, 0), p, Direction.CENTER);
     * System.out.println(p);
     * } catch (PosnTakenException | InvalidEdgeException e) {
     * // TODO Auto-generated catch block
     * e.printStackTrace();
     * } catch (NullTileException e) {
     * // TODO Auto-generated catch block
     * e.printStackTrace();
     * } catch (OutOfMeeplesException e) {
     * // TODO Auto-generated catch block
     * e.printStackTrace();
     * } catch (UnMeeplableException e) {
     * // TODO Auto-generated catch block
     * e.printStackTrace();
     * }
     */

    Referee r = new Referee();
    r.setupGame();
    r.newPlayer(new Player(0, "Scott"));
    r.newPlayer(new Player(1, "Ian"));
    r.shuffleOrder();
    r.run();

  }

}
