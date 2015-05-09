package edu.brown.cs.scij.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import edu.brown.cs.scij.game.Board;
import edu.brown.cs.scij.game.NullTileException;
import edu.brown.cs.scij.game.Player;
import edu.brown.cs.scij.game.Posn;
import edu.brown.cs.scij.game.PosnTakenException;
import edu.brown.cs.scij.game.Referee;
import edu.brown.cs.scij.game.Spectator;
import edu.brown.cs.scij.tile.Direction;
import edu.brown.cs.scij.tile.OutOfMeeplesException;
import edu.brown.cs.scij.tile.Tile;
import edu.brown.cs.scij.tile.UnMeeplableException;

public class CarcBackEnd implements BackEnd {
  private Referee r;
  private Server s;

  public CarcBackEnd(Referee r) {
    this.r = r;
  }

  public static final int MAX_PLAYERS = 4;

  @Override
  public synchronized Object answer(int player, String field,
      Map<String, String> val) {
    Tile t;
    List<Player> players;
    Map<String, Object> toReturn = new HashMap<>();
    if (r.getCurPlayer() != null && player != r.getCurPlayer().getId()) {
      t = r.getCurTile();
      s.putField("currTile", t);
      toReturn.put("currTile", t);
      s.putField("board", r.getBoard());
      toReturn.put("board", r.getBoard());
      s.putField("players", r.getPlayers());
      toReturn.put("players", r.getPlayers());
      Player curPlayer = r.getCurPlayer();
      s.putField("currentPlayer", curPlayer);
      toReturn.put("currentPlayer", curPlayer);
      s.putField("tilesLeft", r.getDeck().getTiles().size());
      toReturn.put("tilesLeft", r.getDeck().getTiles().size());
      return toReturn;
    }
    List<Posn> validMoves;

    assert (player == r.getCurPlayer().getId());
    // System.out.println("Player ID: " + player);

    switch (field) {
      case "rotate":
        // receive left or right
        // return tile, valid moves, valid meeples
        t = r.getCurTile();
        if (t != null) {
          if (val.get("rotate").equals("left")) {
            t.rotateLeft();
          } else {
            t.rotateRight();
          }
          toReturn.put("currTile", t);
          validMoves = r.getBoard().validMoves(t);
          toReturn.put("validMoves", validMoves);
          toReturn.put("players", r.getPlayers());
          return toReturn;
        }
        break;
      case "newPlayer":
        players = r.getPlayers();
        if (players.size() < MAX_PLAYERS) {
          String name = val.get("name");
          r.newPlayer(new Player(player, name));
          s.putField("players", players);
          return ImmutableMap.of("success", "success", "players", players);
        } else {
          String specName = val.get("name");
          r.newSpectator(new Spectator(player, specName));
          s.putField("players", players);
          return ImmutableMap.of("success", "failure", "players", players);
          // return ImmutableMap.of("success", "failure", "spectators", r
          // .getSpectators().size());
        }
      case "gameStart":
        s.seal();

        t = r.drawTile();
        Board b = r.getBoard();
        players = r.getPlayers();
        validMoves = b.validMoves(t);
        r.shuffleOrder();
        s.putField("currTile", t);
        toReturn.put("currTile", t);
        s.putField("board", b);
        toReturn.put("board", b);
        s.putField("players", players);
        toReturn.put("players", players);
        s.putField("spectators", r.getSpectators().size());
        toReturn.put("spectators", r.getSpectators().size());
        s.putField("validMoves", validMoves);
        toReturn.put("validMoves", validMoves);
        Player curPlayer = r.nextPlayer();
        s.putField("currentPlayer", curPlayer);
        toReturn.put("currentPlayer", curPlayer);
        s.putField("tilesLeft", r.getDeck().getTiles().size());
        toReturn.put("tilesLeft", r.getDeck().getTiles().size());
        s.putField("gameStart", true);
        toReturn.put("gameStart", true);
        // putField() current board, current tile, list of players,
        // current player, valid moves
        return toReturn;
      case "placeTile":
        // receiving: posn
        String posn = val.get("move");
        String[] xy = posn.split(",");
        Posn p = new Posn(Integer.parseInt(xy[0]), Integer.parseInt(xy[1]));
        // try {
        try {
          r.getBoard().place(p, r.getCurTile());
          r.setCurPosn(p);
        } catch (IllegalArgumentException | PosnTakenException e1) {
          s.putField("board", r.getBoard());
          toReturn.put("board", r.getBoard());
          s.putField("currentPlayer", r.getCurPlayer());
          toReturn.put("currentPlayer", r.getCurPlayer());
          s.putField("players", r.getPlayers());
          toReturn.put("players", r.getPlayers());
          s.putField("spectators", r.getSpectators().size());
          toReturn.put("spectators", r.getSpectators().size());
          s.putField("gameOver", r.isGameOver());
          toReturn.put("gameOver", r.isGameOver());
          s.putField("currTile", r.getCurTile());
          toReturn.put("currTile", r.getCurTile());
          validMoves = r.getBoard().validMoves(r.getCurTile());
          s.putField("validMoves", validMoves);
          toReturn.put("validMoves", validMoves);
          s.putField("validMeeples", new ArrayList<>());
          toReturn.put("validMeeples", new ArrayList<>());
          s.putField("tilesLeft", r.getDeck().getTiles().size());
          toReturn.put("tilesLeft", r.getDeck().getTiles().size());
          s.putField("error", "That wasn't a valid move! Please try again");
          toReturn.put("error", "That wasn't a valid move! Please try again");
          return toReturn;
        }

        // putField: board, current player, list of all players, valid meeples,
        // current tile

        if (r.getCurPlayer().getNumMeeples() > 0) {
          try {
            List<Direction> validMeeples = r.getBoard().validMeeples(p);
            s.putField("validMeeples", validMeeples);
            toReturn.put("validMeeples", validMeeples);
          } catch (NullTileException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
        } else {
          s.putField("validMeeples", new ArrayList<>());
          toReturn.put("validMeeples", new ArrayList<>());
        }

        return toReturn;
      case "placeMeeple":
        // TODO receiving: direction
        // returning: currtile, board, validmoves, players, currplayer
        String dir = val.get("meeple");
        Direction d = null;
        Tile curTile = r.getCurTile();
        if (dir != null) {
          try {
            switch (dir) {
              case "UP":
                d = Direction.UP;
                break;
              case "LEFT":
                d = Direction.LEFT;
                break;
              case "DOWN":
                d = Direction.DOWN;
                break;
              case "RIGHT":
                d = Direction.RIGHT;
                break;
              case "CENTER":
                d = Direction.CENTER;
                break;
              default:
                System.out.println("DEFAULT WAS REQAACHED IN PLACE MEEPLE");
                break;
            }
            r.placeMeeple(r.getCurPosn(), r.getCurPlayer(), d);
          } catch (UnMeeplableException e) {
            e.printStackTrace();
          } catch (NullTileException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          } catch (OutOfMeeplesException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
        }
        r.score(r.getCurPosn());

        s.putField("board", r.getBoard());
        toReturn.put("board", r.getBoard());
        Player nextPlayer = r.nextPlayer();
        s.putField("currentPlayer", nextPlayer);
        toReturn.put("currentPlayer", nextPlayer);

        s.putField("gameOver", r.isGameOver());
        toReturn.put("gameOver", r.isGameOver());
        players = r.getPlayers();
        if (r.isGameOver()) {
          int max = 0;
          List<Player> winners = new ArrayList<>();
          for (Player playah : players) {
            if (playah.getScore() > max) {
              max = playah.getScore();
            }
          }

          for (Player playah : players) {
            if (playah.getScore() == max) {
              winners.add(playah);
            }
          }
          s.putField("winners", winners);
          toReturn.put("winners", winners);
          players.sort(null);
          s.putField("players", players);
          toReturn.put("players", players);

        } else {
          s.putField("players", players);
          toReturn.put("players", players);
        }

        curTile = r.drawTile();
        if (curTile == null) {
          validMoves = new ArrayList<>();
        } else {
          validMoves = r.getBoard().validMoves(curTile);
          while (validMoves.isEmpty()) {
            validMoves = checkValidMoves(r.getBoard(), curTile);
          }
        }
        s.putField("spectators", r.getSpectators().size());
        toReturn.put("spectators", r.getSpectators().size());
        s.putField("currTile", r.getCurTile());
        toReturn.put("currTile", r.getCurTile());
        s.putField("validMoves", validMoves);
        toReturn.put("validMoves", validMoves);
        s.putField("validMeeples", new ArrayList<>());
        toReturn.put("validMeeples", new ArrayList<>());
        s.putField("tilesLeft", r.getDeck().getTiles().size());
        toReturn.put("tilesLeft", r.getDeck().getTiles().size());
        return toReturn;

      default:
        System.out.println("GOT TO DEWFAULT BIZNATCHES");
        // TODO not sure what to do when it's none of these
    }
    // return only what the person requesting should know
    return toReturn;
  }

  private List<Posn> checkValidMoves(Board b, Tile curTile) {
    List<Posn> validMoves;
    curTile.rotateLeft();
    // check rotation 270
    validMoves = r.getBoard().validMoves(curTile);
    if (validMoves.isEmpty()) {
      curTile.rotateLeft();
      // check rotation 180
      validMoves = r.getBoard().validMoves(curTile);
      if (validMoves.isEmpty()) {
        curTile.rotateLeft();
        // check rotation 90
        validMoves = r.getBoard().validMoves(curTile);
        if (validMoves.isEmpty()) {
          // r.getDeck().getTiles().add(curTile);
          // Collections.shuffle(r.getDeck().getTiles());
          curTile = r.drawTile();
          validMoves = checkValidMoves(b, curTile);

        }
      }
    }
    return validMoves;
  }

  @Override
  public BackEnd setServer(Server setS) {
    this.s = setS;
    return this;
  }

}
